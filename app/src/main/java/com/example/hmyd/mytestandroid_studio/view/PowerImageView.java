package com.example.hmyd.mytestandroid_studio.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.example.hmyd.mytestandroid_studio.R;
import com.example.hmyd.mytestandroid_studio.tools.FormatTools;

/**
 * @author kongdy
 *         on 2016/3/18
 * imageView加强类，可以播放gif
 */
public class PowerImageView extends ImageView {

    // 默认播放时长
    private final static int DEFAULT_MOVIE_DURATION = 1000;

    private Movie moive;

    private boolean pause; // 暂停

    private long mMovieStartTime; // 播放开始时间

    private int mCurrentTime; // 播放最近时间

    private boolean firstEnter = true;


    public PowerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pause = false;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(firstEnter && moive == null) {
           // moive = Movie.decodeStream(FormatTools.getInstance().drawable2InputStream(getDrawable()));
            moive = Movie.decodeStream(FormatTools.getInstance().resId2InputStream(R.drawable.test_gif,getContext()));
        }
        if(moive != null) {
            if(!pause) {
                updatePlayTime();
                drawGifFrame(canvas);
                invalidateView();
            } else {
                drawGifFrame(canvas);
            }
        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 画gif图片的每一帧
     * @param canvas
     */
    private void drawGifFrame(Canvas canvas) {
        final float mLeft = getLeft();
        final float mTop = getTop();
        final float mScaleX = getScaleX();
        final float mScaleY = getScaleY();
        moive.setTime(mCurrentTime);
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(mScaleX,mScaleY);
        moive.draw(canvas,mLeft/mScaleX,mTop/mScaleY);
        canvas.restore();
    }

    /**
     * 刷新播放时间
     */
    private void updatePlayTime() {
        long now = android.os.SystemClock.uptimeMillis();
        // 如果是第一帧，获取开始时间
        if(mMovieStartTime == 0) {
            mMovieStartTime = now;
        }

        int dur = moive.duration();
        if(dur == 0) {
            dur = DEFAULT_MOVIE_DURATION;
        }
        // 计算帧数
        mCurrentTime = (int)((now-mMovieStartTime)%dur);
        Log.v("frame","mFrame:"+mCurrentTime);
    }

    /**
     * 刷新view
     */
    private void invalidateView() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            postInvalidateOnAnimation();
        } else {
            invalidate();
        }
    }

    public boolean isPause() {
        return pause;
    }

    public void setPauser(boolean pause) {
        this.pause = pause;
    }


    public void setMovieResource(int movieResId){
        moive = Movie.decodeStream(FormatTools.getInstance().resId2InputStream(movieResId,getContext()));
        requestLayout();
    }
}
