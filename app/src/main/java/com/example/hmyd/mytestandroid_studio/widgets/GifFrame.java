package com.example.hmyd.mytestandroid_studio.widgets;

import android.graphics.Bitmap;

/**
 * @author kongdy
 *         on 2016/3/22
 *  表示gif 图片的一帧图形
 */
public class GifFrame {
    public Bitmap image; // 当前图像
    public int delay; // 图像延迟

    public GifFrame(int delay, Bitmap image) {
        this.delay = delay;
        this.image = image;
    }

    public GifFrame nextframe; // 下个一帧
}
