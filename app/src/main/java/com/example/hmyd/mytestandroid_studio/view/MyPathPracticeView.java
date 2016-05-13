package com.example.hmyd.mytestandroid_studio.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author kongdy
 *         on 2016/5/13
 */
public class MyPathPracticeView extends View {

    private Path path;
    private Paint paint;
    private PathEffect effect;
    private float phase;

    public MyPathPracticeView(Context context) {
        super(context);
        initTools();
    }

    public MyPathPracticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTools();
    }

    public MyPathPracticeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTools();
    }


    /**
     * init tool
     */
    private void initTools() {
        path = new Path();
        paint = new Paint();
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        path.moveTo(0,0);

        // random 15 point
        for (int i = 0;i < 15;i++) {
            path.lineTo(i*40, (float) (Math.random()*150));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
      //  super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.translate(180,180);
        Path p = new Path();
        p.addRect(0, 0, 8, 8, Path.Direction.CCW);
        effect = new ComposePathEffect(new DiscretePathEffect(3.0f,5.0f),new PathDashPathEffect(p,
                12,phase,PathDashPathEffect.Style.ROTATE));
        paint.setPathEffect(effect);
        paint.setColor(Color.RED);
        canvas.drawPath(path,paint);
        phase+=1;
        invalidate();
    }
}
