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
import android.widget.Toast;

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
    private int cursorWidth = -1;
    private int cursorHeight = -1;

    private Rect rect = null;
    private Rect bitmapRect = null;
    private Point drawPosition;

    private Point absoluteDownPosition;
    private Point startPosition;

    private DrawThread drawThread = null;

    private GifImageType anmiationType = GifImageType.SYNC_DECODER;

    private float pointDistance;
    private int touchMode = -1;

    private static final int BEGINMOVE = 1;
    private static final int BEGINZOOM = 2;
    private static final int IDLE_TOUCH_MODE = -1;


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
        if(showWidth == -1 && drawPosition != null) {
            canvas.drawBitmap(currentImage,drawPosition.x,drawPosition.y,null);
        } else if(rect != null){
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
            if(showWidth == -1) {
                showWidth = width;
                cursorWidth = showWidth;
            }
            if(showHeight == -1) {
                showHeight = height;
                cursorHeight = showHeight;
            }

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
     * 实现手势缩放、移动、旋转
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
                touchMode = BEGINMOVE;
                break;
            case MotionEvent.ACTION_MOVE:
                final int width = this.getWidth();
                final int height = this.getHeight();
                int newWidth;
                int newHeight;
                if(touchMode == BEGINZOOM) {
                    float nowPointDistance = getPointersDistance(event);
                    float changeScale = nowPointDistance/pointDistance;

                    newWidth = (int) ((showWidth*changeScale)-showWidth);
                    newHeight = (int) ((showHeight*changeScale)-showHeight);
//                    Log.e("n,p,w,h",nowPointDistance+","+pointDistance+","+width+
//                            ","+height+",showWidth:"+showWidth+",showWidth:"+showWidth+",newWidth:"+
//                            newWidth+",newHeight:"+newHeight+",cursorWidth:"+cursorWidth);
                    if((newWidth > 0 && width < 8*showWidth) || (newWidth < 0 && width > (showWidth*2)/3)) {
                        this.setPosition(absoluteDownPosition.x-startPosition.x-newWidth/2,
                                absoluteDownPosition.y-startPosition.y-newHeight/2,
                                absoluteDownPosition.x+cursorWidth+newWidth/2-startPosition.x,absoluteDownPosition.y+
                                        cursorHeight+newHeight/2-
                                        startPosition.y);
                          setShowDimension(getWidth(),getHeight());
//                        cursorWidth = getWidth();
//                        cursorHeight = getHeight();
                    }
                } else if(touchMode == BEGINMOVE){
                    this.setPosition(absoluteDownPosition.x-startPosition.x,absoluteDownPosition.y-startPosition.y,
                            absoluteDownPosition.x+width-startPosition.x,absoluteDownPosition.y+height-
                                    startPosition.y);
                    absoluteDownPosition.x = (int) event.getRawX();
                    absoluteDownPosition.y = (int) event.getRawY();
                }
                break;
            case MotionEvent.ACTION_UP:
                touchMode = IDLE_TOUCH_MODE;
                pointDistance = 0;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                touchMode = IDLE_TOUCH_MODE;
                cursorWidth = getWidth();
                cursorHeight = getHeight();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                touchMode = BEGINZOOM;
                pointDistance = getPointersDistance(event);
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
