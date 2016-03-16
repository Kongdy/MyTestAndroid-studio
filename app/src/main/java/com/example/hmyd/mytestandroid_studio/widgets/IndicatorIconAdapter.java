package com.example.hmyd.mytestandroid_studio.widgets;

import android.graphics.Bitmap;

/**
 * @author kongdy
 *         on 2016/3/16
 * indicator图标实现接口
 */
public interface IndicatorIconAdapter {

    public int getIconCount();

    public Bitmap getIcon(int position);
}
