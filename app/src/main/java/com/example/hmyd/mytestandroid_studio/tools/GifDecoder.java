package com.example.hmyd.mytestandroid_studio.tools;

import android.util.Log;

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

    private int status; // current status

    private boolean transprency = false; // whether use transparent
    private int transIndex; // transparent color index

    private int bgIndex; // background color index
    private int bgColor; // background color

    private InputStream in; // 获取的图片流
    private int width; // 图宽
    private int height; // 图高

    private int ix,iy,iw,ih; // image rectangle ,指图片的矩阵

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
            pixels = new byte[npix];
        }
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


}
