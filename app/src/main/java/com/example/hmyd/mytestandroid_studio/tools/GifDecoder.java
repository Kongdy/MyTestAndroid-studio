package com.example.hmyd.mytestandroid_studio.tools;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.hmyd.mytestandroid_studio.widgets.GifFrame;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kongdy
 *         on 2016/3/21
 * gif解码器，解码图片
 */
public class GifDecoder extends Thread {

    /**
     * 正在解码中
     */
    public static final int STATUS_PARSING = 0;
    /**
     * 解码出错，图片格式错误
     */
    public static final int STATUS_FROMAT_ERRPOR = 1;
    /**
     * 打开失败
     */
    public static final int STATUS_OEPN_FAILED = 2;
    /**
     * 解码成功
     */
    public static final int STATUS_FINISH = -1;
    /**
     * max decoder pixel stack size
     */
    private static final int MAX_STACK_SIZE = 4096;

    private int status; // current status

    private int dispose;// last graphic control extension info
    /**
     * 0 = noaction ; 1 = leave in place;2 = restore to bg;3 = restore to prev
     */
    private int lastDispose = 0;
    private boolean transprency = false; // whether use transparent
    private int transIndex; // transparent color index
    private int delay = 0;// delay in milliseconds (延迟)

    private InputStream in; // 获取的图片流
    private int width; // 图宽
    private int height; // 图高

    private byte[] block = new byte[256]; // current data block
    private int blockSize = 0; // block size

    private int ix,iy,iw,ih; // image rectangle ,指图片的矩阵
    private int lrx,lry,lrw,lrh;

    private boolean lctFlag; // 局部颜色标志 local color table flag
    private boolean interfaceFlag; // 交错颜色标志 interface flag
    private int lctSize; // 局部颜色表大小 local color table size

    private int[] lct; // local color table
    private int[] gct; // global color table
    private int[] act; // active color table

    // (Lempel-Ziv-Welch encoding)兰博-立夫-卫曲编码法
    // LZW decoder working arrays
    private short[] prefix;
    private byte[] suffix;
    private byte[] pixelStack;
    private byte[] pixels;

    private GifFrame gifFrame; // frame that current read
    private int frameCount;

    private Bitmap image; // 当前图层
    private Bitmap lastImage; // 最后一个图层

    private int bgIndex; // background color index
    private int bgColor; // background color
    private int lastBgColor; // previos background color
    private int pixelAspect; // pixel aspect radio

    public GifDecoder(InputStream is) {
        this.in = is;
    }

    @Override
    public void run() {
        readStream();
    }

    /**
     * 读取流
     */
    private void readStream() {
        if(in != null) {
            readHeader();
            if(!err()) {
                readContents();
            }
        } else {
            status = STATUS_OEPN_FAILED;
            Log.e("PGV-GifDecoder","open failed,code="+STATUS_OEPN_FAILED);
        }
    }

    /**
     * 读取图片文件主体部分
     */
    private void readContents() {
        boolean done = false;
        while (!(done || err())) {
            int code = read();
            switch (code){
                case 0x2C: // image separator(分离器),即每一帧的分离器
                    readImage();
                    break;
                case 0x00: // get a bad byte nut keep going and see what will be happend
                    break;
                default:
                    status = STATUS_FROMAT_ERRPOR;
            }
        }
    }

    /**
     * 读取图片
     */
    private void readImage() {
        // 读取到的每一帧，开始都是已一个代表图片大小的矩阵参数开始的，顺序如下
        ix = readShort();
        iy = readShort();
        iw = readShort();
        ih = readShort();
        /**
         * ??? packed 包？
         * 应该是某种有关图片参数的标志
         */
        int packed = read();
        /**
         * 这里每个字节代表的信息:
         * 1 局部颜色表标志
         * 2 交错颜色标志
         * 3 排序标志
         * 4-5 预留位
         * 6-8 局部颜色表大小
         */
        lctFlag = (packed & 0x80) != 0; // 这种运算符，当两个参数不相等会为0,否则等于他们本身
        interfaceFlag = (packed & 0x40) != 0;
        lctSize =  2 << (packed & 7);
        if(lctFlag) {
            lct = readColorTable(lctSize);
            act = lct;
        } else {
            act = gct;
            if(bgIndex == transIndex) {
                bgColor = 0;
            }
        }
        int save = 0;
        if(transprency) {
            save = act[transIndex];
            act[transIndex] = 0; // set transparent color if specified 如果特意指定设置了透明色，那就设置透明色
        }
        if(act == null) {
            status = STATUS_FROMAT_ERRPOR;
        }
        if(err()) {
            return;
        }
        decodeImageData();
        skip();
        if(err()) {
            return;
        }
        frameCount++;
        // create new image to recivie frame data
        image = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_4444);
        // create image ,push frame data to bitmap type image
        setPixels();
    }

    /**
     * push frame data to image
     */
    private void setPixels() {
        int[] dest = new int[width * height];
        // fill in starting image content based on last image's dispose code
        if (lastDispose > 0) {
            if (lastDispose == 3) { // restore to prev
                int n = frameCount - 2;
                if (n > 0) {
                    lastImage = getFrameImage(n - 1);
                } else {
                    lastImage = null;
                }
            }

            if(lastImage != null) {
                // copy to image
                // 这里起到图片的资源的复制作用
                lastImage.getPixels(dest,0,width,0,0,width,height);
                if(lastDispose == 2) { // restore to bg
                    // fill last image rect area with background color
                    int c = 0;
                    if (!transprency) {
                        c = lastBgColor;
                    }
                    for (int i = 0;i < lrh;i++) {
                        int n1 = (lry + i) * width + lrx;
                        int n2 = n1 + lrw;
                        for (int k = n1;k < n2;k++) {
                            dest[k] = c;
                        }
                    }
                }
            }
        }
        // copy each source line to appropriate place in the destination
        int pass = 1;
        int inc = 8;
        int iline = 0;
        for (int i = 0;i < ih;i++) {
            int line = i;
            if(interfaceFlag) {
                if (iline >= ih) {
                    pass++;
                    switch (pass) {
                        case 2:
                            iline = 4;
                            break;
                        case 3:
                            iline = 2;
                            inc = 4;
                            break;
                        case 4:
                            iline = 1;
                            inc = 2;
                    }
                }
                line = iline;
                iline += inc;
            }
            line += iy;
            if(line < height){

            }
        }
    }

    /**
     * get one frame image
     * @param n
     * @return
     */
    public Bitmap getFrameImage(int n) {
        GifFrame frame = getFrame(n);
        if(frame == null) {
            return null;
        } else {
            return frame.image;
        }
    }

    /**
     * get one frame ,the frame include delay and data
     * @param n
     * @return
     */
    public GifFrame getFrame(int n) {
        GifFrame frame = gifFrame;
        int i = 0;
        while(frame != null) {
            if(i == n) {
                return frame;
            } else {
                frame = frame.nextframe;
            }
        }
        return null;
    }

    /**
     * 解析图像
     */
    private void decodeImageData() {
        int NullCode = -1;
        int npix = iw * ih;
        /**
         * 这一大堆，特么什么玩意儿？
         */
        int available,clear,code_mask,code_size,end_of_information,in_code,old_code,bits,code,count,i,datum,data_size,first,top,bi,pi;
        
        if(pixels == null || pixels.length < npix) {
            pixels = new byte[npix]; // allocate new pixel array
        }
        if(prefix == null) {
            prefix = new short[MAX_STACK_SIZE];
        }
        if(suffix == null) {
            suffix = new byte[MAX_STACK_SIZE];
        }
        if(pixelStack == null) {
            pixelStack = new byte[MAX_STACK_SIZE];
        }
        // initialize GIF data stream decoder
        data_size = read();
        clear = 1 << data_size;
        end_of_information = clear+1;
        available = clear+2;
        old_code = NullCode;
        code_size = data_size+1;
        code_mask = (1 << code_size) - 1;
        for (code = 0;code < clear;code++) {
            prefix[code] = 0;
            suffix[code] = (byte)code;
        }

        // decode GIF pixel stream
        datum=bits=count=first=top=pi=bi=0;
        for (i = 0;i < npix;) {
            if(top == 0) {
                if(bits < code_size) {
                    if(count == 0) {
                        count = readBlock();
                        if(count <= 0) {
                            break;
                        }
                        bi = 0;
                    }
                    datum += (((int)block[bi]) & 0xff) << bits;
                    bits += 8;
                    bi++;
                    count--;
                    continue;
                }
                // Get the next code
                code = datum & code_mask;
                bits -= code_size;

                // Interpret the code
                if(code > available || code == end_of_information) {
                    break;
                }

                if(code == clear) {
                    // reset decoder
                    code_size = data_size+1;
                    code_mask = (1 << code_size) - 1;
                    available = clear + 2;
                    old_code = NullCode;
                    continue;
                }

                if(old_code == NullCode) {
                    pixelStack[top++] = suffix[code];
                    old_code = code;
                    first = code;
                    continue;
                }
                in_code = code;
                if(code == available) {
                    pixelStack[top++] = (byte)first;
                    code = old_code;
                }
                while (code > clear) {
                    pixelStack[top++] = suffix[code];
                    code = prefix[code];
                }
                first = ((int)suffix[code]) & 0xff;
                // add a new string to the string table
                if(available > MAX_STACK_SIZE) {
                    break;
                }
                pixelStack[top++] = (byte)first;
                prefix[available] = (short) old_code;
                suffix[available] = (byte) first;
                available++;
                if((available & code_mask) == 0
                        && (available < MAX_STACK_SIZE) ) {
                    code_size++;
                    code_mask += available;
                }
                old_code =  in_code;
            }
            // pop a pixel off the pixel stack
            top -- ;
            pixels[pi++] = pixelStack[top];
            i++;
        }
        for (i = pi;i < npix;i++) {
            pixels[i] = 0;
        }
    }

    /**
     * 读取数据块
     * @return
     */
    private int readBlock() {
        blockSize = read();
        int n = 0;
        if(blockSize > 0) {
            try {
                int count = 0;
                while (n < blockSize) {
                    count = in.read(block,n,blockSize-n);
                    if(count == -1) {
                        break;
                    }
                    n += count;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(n < blockSize) {
                status = STATUS_FROMAT_ERRPOR;
            }
        }
        return n;
    }


    /**
     * 读取头文件
     */
    private void readHeader() {
        String headType = "";
        for (int i = 0;i < 6;i++) {
            headType += (char)read();
        }

        if(!headType.startsWith("GIF")) {
            Log.d("PGV-GifDecoder",headType+"it's not a gif type picture");
            return;
        }

        readLSD();

    }

    private void readLSD() {
        width = readShort();
        height = readShort();
        Log.v("PGV-GifDecoder","gif size:"+width+",height:"+height);
    }


    /**
     * ?  为什么是8位？
     * @return
     */
    private int readShort() {
        return read() | (read()<<8);
    }

    /**
     * 游标，读取流的每个字节
     * 流每次读取都会改变流的位置，借助这个特性
     * 每次读一个字节
     * @return
     */
    private int read() {
        int curByte = 0; // 流的游标

        try {
            curByte = in.read();
        } catch (IOException e) {
            Log.e("PGV-GifDecoder","format error,code="+STATUS_FROMAT_ERRPOR);
            status = STATUS_FROMAT_ERRPOR;
        }
        return curByte;
    }

    private int[] readColorTable(int nColors) {
        int nbytes = nColors * 3; // 每个像素由3个字节组成，这三个字节分别代表rgb
        int[] tab = null;
        byte[] c = new byte[nbytes];
        int n = 0;

        try {
            n = in.read(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(n < nbytes) {
            status = STATUS_FROMAT_ERRPOR;
        } else {
            tab = new int[256];
            int i = 0;
            int j = 0;
            while (i < nColors) {
                int r = ((int)c[j++]) & 0xff;
                int g = ((int)c[j++]) & 0xff;
                int b = ((int)c[j++]) * 0xff;
                tab[i++] = 0xff000000 | (r<<16) | (g<<8) | b;
            }
        }
        return tab;
    }

    /**
     * 判断是否正在解码
     * @return
     */
    public boolean err() {
        return status != STATUS_PARSING;
    }

    /**
     * skips variable length blocks up to and including next zero block
     */
    private void skip() {
        do{
            readBlock();
        }while ((blockSize > 0) && ! err());
    }

}
