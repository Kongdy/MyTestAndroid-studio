package com.example.hmyd.mytestandroid_studio.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author kongdy
 *         on 2016-3-11 11:02:54
 *         图片帮助类
 */
public class BitmapHelp {


    private static BitmapHelp mBitmapHelp;
    private Context context;
    private LruCache<String, Bitmap> memoryCahches;

    private ExecutorService mThreadPool; // 通过线程池来管理图片加载

    /**
     * 单例模式
     */
    private BitmapHelp(Context context) {
        long maxMemory = Runtime.getRuntime().maxMemory(); // 应用最大内存
        int unitMemory = (int) maxMemory / 8; // 获取最大内存十分之一
        this.context = context;
        memoryCahches = new LruCache<String, Bitmap>(unitMemory) {

            // 重写该方法来重新计算bitmap内存大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight(); // 每一行字节乘以像素密度
            }
        };
    }

    private ExecutorService getmThreadPool() {
        if (mThreadPool == null) {
            synchronized (ExecutorService.class) {
                if (mThreadPool == null) {
                    // 在这里newCachedThreadPool和newFixedThreadPool有区别
                    // newCachedThreadPool可能会复用线程，这样可能会造成图片加载
                    // 过程中产生线程复用而造成的图片重复，虽然说性能会提高，
                    // 不过你可以尝试用CachedThreadPool，用另外一种方法来
                    // 避免造成的内存重复加载
                    // 线程池同时最多保持4个线程
                    mThreadPool = Executors.newFixedThreadPool(4);
                }
            }
        }
        return mThreadPool;
    }


    /**
     * 获取唯一实例
     *
     * @return
     */
    public static BitmapHelp getInstance(Context context) {
        if (mBitmapHelp == null) {
            mBitmapHelp = new BitmapHelp(context);
            return mBitmapHelp;
        }
        return mBitmapHelp;
    }

    /**
     * 通过资源图片id生成一个自适应的bitmap
     * 三级缓存加载
     *
     * @param resid
     * @return
     */
    public void getBitmapFromResource(final int resid, final View v, final onPicLoaderListener listener) {

        final Handler handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                listener.onPicLoader((Bitmap) msg.obj, resid + "");
            }
        };

        getmThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapFromCache(resid + "");
                if (bitmap == null) {
                    if (v != null) {
                        bitmap = adjustSizeBitmap(v.getWidth(), v.getHeight(), resid);
                    } else {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), resid);
                    }
                    try {
                        compressMyBitmap(bitmap, 100, resid + "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    addBitmapToMemoryCache(bitmap, resid + "");
                }
                if(bitmap != null) {
                    Message msg = new Message();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                } else {
                    Log.e("bitmap help","create a null bitmap in getBitmapfromResource()");
                }
            }
        });
    }


    /**
     * 三级级缓存拿取图片位图
     *
     * @param picName
     * @return
     */
    public Bitmap getBitmapFromCache(String picName) {
        String tag = picName.replaceAll("[^\\w]", "");
        Bitmap bitmap = getBitmapFromMemoryCache(tag);
        if (bitmap != null) {
            return bitmap;
        } else if (isPicFileExit(tag) && getPicFileSize(tag) > 0) {
            bitmap = getBitmapFromFileCache(tag);
            addBitmapToMemoryCache(bitmap, tag); // 加入到内存缓存
        }
        return bitmap;
    }

    /**
     * 从内存中拿去位图
     *
     * @param picName
     * @return
     */
    public Bitmap getBitmapFromMemoryCache(String picName) {
        String tag = picName.replaceAll("[^\\w]", "");
        return memoryCahches.get(tag);
    }

    public void addBitmapToMemoryCache(Bitmap bitmap, String picName) {
        String tag = picName.replaceAll("[^\\w]", "");
        if(bitmap != null) {
            memoryCahches.put(tag, bitmap);
        }
    }

    /**
     * 自适应大小把图片设置进控件
     *
     * @param resid
     * @param v
     * @return
     */
    public void displayBitmapFromResource(int resid, final View v) {
        getBitmapFromResource(resid, v, new onPicLoaderListener() {
            @Override
            public void onPicLoader(Bitmap bitmap, String picName) {
                if(v != null) {
                    ((ImageView)v).setImageBitmap(bitmap);
                } else {
                    Log.e("bitmap help","setImageBitmap failed,view is null!");
                }
            }
        });
    }


    /**
     * 自动调整位图跟给予的长宽进行size适配
     * 这里修改为了martix缩放，以前的samplesize缩放导致图片过于失真
     * @param viewWidth
     * @param viewHeight
     * @param resid
     * @return
     */
    public Bitmap adjustSizeBitmap(int viewWidth, int viewHeight, int resid) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resid);
        int orgWidth = bitmap.getWidth();
        int orgHeight = bitmap.getHeight();
        // 如果view的控件大小全部是自适应的话，那么就给一个固定大小
        if (viewHeight <= 1) {
            viewHeight = (int)Utils.getRawSize(context, TypedValue.COMPLEX_UNIT_DIP,100); // 默认100dip
        }
        if (viewWidth <= 1) {
            viewWidth = Utils.SCREENT_WIDTH_;
        }
        // 计算缩放比例
        float scaleWidth = ((float)viewWidth)/orgWidth;
        float scaleHeight = ((float)viewHeight)/orgHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap changeBitmap = Bitmap.createBitmap(bitmap,0,0,orgWidth,orgHeight,matrix,true);
        return changeBitmap;
    }



    /**
     * 压缩图片到缓存文件
     *
     * @param bitmap  原始位图
     * @param quality 压缩质量
     * @param picName 图片名字
     * @throws IOException
     */
    public void compressMyBitmap(Bitmap bitmap, int quality, String picName) throws IOException {

        String pPath = Utils.getPicSavePath(context);
        String tag = picName.replaceAll("[^\\w]", "");
        String picPath = pPath + File.separator + tag;
        File picFile = new File(picPath);
        if (!picFile.exists()) {
            picFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(new File(picPath));
        if(bitmap != null && fos != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, fos);
        } else {
            Log.w("bitmapHelp","compress a null bitmap!");
        }

    }

    /**
     * 判断文件是否存在
     *
     * @return
     */
    public boolean isPicFileExit(String picName) {
        String tag = picName.replaceAll("[^\\w]", "");
        String picPath = Utils.getPicSavePath(context) + File.separator + tag;
        return (new File(picPath)).exists();
    }

    /**
     * 判断文件大小
     *
     * @param picName
     * @return
     */
    public long getPicFileSize(String picName) {
        String tag = picName.replaceAll("[^\\w]", "");
        String picPath = Utils.getPicSavePath(context) + File.separator + tag;
        if (isPicFileExit(tag)) {
            return (new File(picPath)).length();
        } else {
            return 0;
        }
    }

    /**
     * 从缓存文件中获取bitmap
     *
     * @param picName
     * @return
     */
    public Bitmap getBitmapFromFileCache(String picName) {
        String tag = picName.replaceAll("[^\\w]", "");
        return BitmapFactory.decodeFile(Utils.getPicSavePath(context) + File.separator + tag);
    }


    /**
     * 停止图片加载类的线程池
     */
    public synchronized void shutDownThreadPool() {
        if (getmThreadPool() != null) {
            getmThreadPool().shutdown();
            mThreadPool = null;
        }
    }


    /**
     * 提供给线程的位图回调接口
     */
    public interface onPicLoaderListener {
        void onPicLoader(Bitmap bitmap, String picName);
    }


    /**
     * 清空缓存文件
     */
    public void clearCacheFile() {
        String picPath = Utils.getPicSavePath(context);
        File picFile = new File(picPath);
        if(picFile.exists()) {
            picFile.delete();
        }
    }

}
