package com.example.hmyd.mytestandroid_studio.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.hmyd.mytestandroid_studio.R;

/**
 * @author kongdy
 * @date 2017/12/10 16:13
 * @describe TODO
 **/

public class RotateImage extends View {

    private Bitmap bitmap;

    private Paint paint;

    private Matrix matrix = new Matrix();

    public RotateImage(Context context) {
        this(context,null);
    }

    public RotateImage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public RotateImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.gesture_pic_menu_item);
        matrix.setScale(0.3f,0.3f);
        matrix.postTranslate(200,200);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(null != bitmap)
            canvas.drawBitmap(bitmap,matrix,paint);
    }

    public void doRotate()
    {
        matrix.postRotate(1,((bitmap.getWidth()*0.3f)/2)+200,
                ((bitmap.getHeight()*0.3f)/2)+200);
        postInvalidate();
    }
}
