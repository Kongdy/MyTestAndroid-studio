package com.example.hmyd.mytestandroid_studio.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author kongdy
 *         on 2016/3/18
 * 转换工具类
 */
public class FormatTools {

    private static FormatTools tools;

    /**
     * 保证单例模式
     */
    private FormatTools() {
    }

    /**
     * 获得转换工具实例
     * @return
     */
    public static FormatTools getInstance() {
        if(tools == null) {
            tools = new FormatTools();
        }
        return tools;
    }

    /**
     * 把字节转换为输入流
     * @param bytes
     * @return
     */
    public InputStream byte2InputStream(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return bais;
    }


    /**
     * 把drawable花在画布上转成bitmap
     * @param drawable
     * @return
     */
    public Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() == PixelFormat.OPAQUE ? Bitmap.Config.RGB_565:
                        Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight()
        );
        drawable.draw(canvas);
        return  bitmap;
    }

    /**
     * 先把bitmap压入输出流，然后转换成字节，再转成输入流
     * <h1>转换的结果为JPEG格式<h1/>
     * @param bitmap
     * @return
     */
    public InputStream bitmap2Inputstream(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /**
     * drawable转InputStream
     * @param drawable
     * @return
     */
    public InputStream drawable2InputStream(Drawable drawable) {
        if(drawable == null) {
            return null;
        }
        Bitmap bm = this.drawable2Bitmap(drawable);
        return this.bitmap2Inputstream(bm);
    }

    /**
     * 资源id转换为bitmap
     * @param resId
     * @param context
     * @return
     */
    public Bitmap resId2bitmap(int resId, Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resId);
        return bitmap;
    }

    public InputStream resId2InputStream(int resId,Context context) {
        Bitmap bm = resId2bitmap(resId,context);
        return this.bitmap2Inputstream(bm);
    }

}
