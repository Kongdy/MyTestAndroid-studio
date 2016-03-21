package com.example.hmyd.mytestandroid_studio.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;

import com.example.hmyd.mytestandroid_studio.tools.GifDecoder;

import java.io.InputStream;


/**
 * @author kongdy
 *         on 2016/3/18
 * 加强控件，可播放gif
 */
public class PowerImageView extends View {

    private GifDecoder decoder;

    public PowerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageResource(int resId) {
        Resources r = this.getResources();
        InputStream is = r.openRawResource(resId);
        decoder = new GifDecoder(is);
        decoder.start();
    }
}
