package com.example.hmyd.mytestandroid_studio.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.hmyd.mytestandroid_studio.tools.Utils;
import com.example.hmyd.mytestandroid_studio.widgets.GifDecoder;
import com.example.hmyd.mytestandroid_studio.widgets.GifAction;
import com.example.hmyd.mytestandroid_studio.widgets.GifFrame;

import java.io.InputStream;


/**
 * @author kongdy
 *         on 2016/3/18
 * 加强控件，可播放gif
 */
public class PowerImageView extends View implements GifAction{

    /**
     * gif解码器
     */
    private GifDecoder decoder = null;
    /**
     * 当前要显示的帧
     */
    private Bitmap currentImage = null;

    private boolean isRun = true;
    private boolean pause = false;

    private int showWidth = -1;
    private int showHeight = -1;
    private Rect rect = null;
    private Point drawPosition;

    private Point absoluteDownPosition;
    private Point startPosition;

    private DrawThread drawThread = null;

    private GifImageType anmiationType = GifImageType.SYNC_DECODER;

    private float pointDistance;
    private boolean beginZoom;

    public PowerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageResource(int resId) {
        Resources r = this.getResources();
        InputStream is = r.openRawResource(resId);
        setGifDecodeImage(is);
    }

    public void setGifDecodeImage(InputStream is) {
        if(decoder != null) {
            decoder.free();
            decoder=null;
        }
        decoder = new GifDecoder(is,this);
        decoder.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(decoder == null) {
            return;
        }
        if(currentImage == null) {
            currentImage = decoder.getImage();
        }
        if (currentImage == null) {
            return;
        }
        int saveCount = canvas.getSaveCount();
        canvas.save();
        canvas.translate(getPaddingLeft(),getPaddingTop());
        if(showWidth == -1) {
            canvas.drawBitmap(currentImage,drawPosition.x,drawPosition.y,null);
        } else {
            canvas.drawBitmap(currentImage,null,rect,null);
        }
        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int pleft = getPaddingLeft();
        int pright = getPaddingRight();
        int ptop = getPaddingTop();
        int pbottom = getPaddingBottom();

        int widthSize;
        int heightSize;

        int w;
        int h;

        if(decoder == null) {
            w = 1;
            h = 1;
        } else {
            w = decoder.width;
            h = decoder.height;
        }
        w += pleft+pright;
        h += pbottom+ptop;

        w = Math.max(w,getSuggestedMinimumWidth());
        h = Math.max(h,getSuggestedMinimumHeight());

        widthSize = resolveSize(w,widthMeasureSpec);
        heightSize = resolveSize(h,heightMeasureSpec);

        setMeasuredDimension(widthSize,heightSize);

       // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 只显示第一帧图片<br/>
     * 不会播放动画
     */
    public void showCover() {
        if(decoder == null) {
            return;
        }
        pause = true;
        currentImage = decoder.getImage();
        invalidate();
    }

    /**
     * 继续显示动画，即resume
     */
    public void showAnimation() {
        if(pause) {
            pause = false;
        }
    }

    /**
     * 设置gif解码过程中的显示方式
     * @param type
     */
    public void setGifImageType(GifImageType type) {
        if(decoder != null) {
            anmiationType = type;
        }
    }

    public void setShowDimension(int width,int height) {
        if(width > 0 && height > 0) {
            showWidth = width;
            showHeight = height;
            rect = new Rect();
            rect.left = 0;
            rect.top = 0;
            rect.right = width;
            rect.bottom = height;
            drawPosition = new Point();
            drawPosition.x = width/2;
            drawPosition.y = height/2;
        }
    }

    /**
     * 实现手势缩放、移动
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (absoluteDownPosition == null) {
            absoluteDownPosition = new Point();
        }
        if(startPosition == null) {
            startPosition = new Point();
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                absoluteDownPosition.x = (int) event.getRawX();
                absoluteDownPosition.y = (int) event.getRawY();
                startPosition.x = (int) event.getX();
                startPosition.y = absoluteDownPosition.y - this.getTop();
                if(event.getPointerCount() > 1) {
                    pointDistance = getPointersDistance(event);
                    beginZoom = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float nowPointDistance = getPointersDistance(event);
                float changeScale = pointDistance/nowPointDistance;
                final int width = this.getWidth();
                final int height = this.getHeight();
                int newWidth = width;
                int newHeight = height;
                if(beginZoom) {
                    newWidth = (int) (newWidth*changeScale);
                    newHeight = (int) (newHeight*changeScale);
                }
                this.setPosition(absoluteDownPosition.x-startPosition.x,absoluteDownPosition.y-startPosition.y,
                        absoluteDownPosition.x+newWidth-startPosition.x,absoluteDownPosition.y+newHeight-
                startPosition.y);
                absoluteDownPosition.x = (int) event.getRawX();
                absoluteDownPosition.y = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    /**
     * 获取触摸点之间的距离
     * @param event
     * @return
     */
    private float getPointersDistance(MotionEvent event) {
        float distance = 0;
        int pointCount = event.getPointerCount();
        if(pointCount > 1) {
            // calculate distance
            float xs[] = new float[pointCount];
            float ys[] = new float[pointCount];
            for (int i = 0;i < pointCount;i++ ) {
                xs[i] = event.getX(i);
                ys[i] = event.getY(i);
            }
            Utils.mergeSort(xs,pointCount);
            Utils.mergeSort(ys,pointCount);
            for (int j = 0;j < pointCount/2;j++) {
                distance += xs[pointCount-1-j]-xs[j]+ys[pointCount-1-j]-ys[j];
            }
        }
        // To prevent miss a finger suddenly
        return distance/pointCount;
    }

    private void setPosition(int l,int t,int r,int b) {
        this.layout(l,t,r,b);
    }

    @Override
    public void parseOk(boolean parseStatus, int frameIndex) {
        setShowDimension(getWidth(),getHeight());
        if(parseStatus) {
            if (decoder != null) {
                switch (anmiationType) {
                    case WAIT_FINISH:
                        if(frameIndex == -1) {
                            if(decoder.getFrameCount() > 1) { // 当帧数大于1的时候启动播放帧线程
                                DrawThread t = new DrawThread();
                                t.start();
                            } else {
                                reDraw();
                            }
                        }
                        break;
                    case COVER:
                        if(frameIndex == 1) {
                            currentImage = decoder.getImage();
                            reDraw();
                        } else if(frameIndex == -1) {
                            if(decoder.getFrameCount() > 1) {
                                if(drawThread == null) {
                                    drawThread = new DrawThread();
                                    drawThread.start();
                                }
                            } else {
                                reDraw();
                            }
                        }
                        break;
                    case SYNC_DECODER:
                        if(frameIndex == 1) {
                            currentImage = decoder.getImage();
                            reDraw();
                        } else if(frameIndex == -1) {
                            reDraw();
                        } else {
                            if(drawThread == null) {
                                drawThread = new DrawThread();
                                drawThread.start();
                            }
                        }
                        break;
                }
            } else {
                Log.e("PowerImageView","parse error!");
            }
        }
    }

    private void reDraw() {
        if(redrawHandler != null) {
            Message msg = redrawHandler.obtainMessage();
            redrawHandler.sendMessage(msg);
        }
    }

    private Handler redrawHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    private class DrawThread extends Thread {

        private DrawThread() {
        }

        @Override
        public void run() {
            if(decoder == null) {
                return;
            }
            while(isRun) {
                if(pause == false) {
                    GifFrame frame = decoder.next();
                    currentImage = frame.image;
                    long sp = frame.delay;
                    if(redrawHandler != null) {
                        Message msg = redrawHandler.obtainMessage();
                        redrawHandler.sendMessage(msg);
                        SystemClock.sleep(sp);
                    } else {
                        break;
                    }
                } else {
                    SystemClock.sleep(10);
                }
            }
        }
    }

    public enum GifImageType{
        WAIT_FINISH(0),

        SYNC_DECODER(1),

        COVER(2);

        GifImageType(int i) {
            nativeInt = i;
        }
        final int nativeInt;
    }


}
