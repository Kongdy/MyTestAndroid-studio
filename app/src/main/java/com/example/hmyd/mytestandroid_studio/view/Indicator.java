package com.example.hmyd.mytestandroid_studio.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author kongdy
 *         on 2016/3/15
 *  作为fragmentIndicator的底部菜单
 */
public class Indicator extends LinearLayout {

    private static final int[] ADDED_ATTRS = new int[]{
        /* 0 */ android.R.attr.divider,
        /* 1 */ android.R.attr.showDividers,
        /* 2 */ android.R.attr.dividerPadding
    };

    private static final int ADDED_ATTRS_DIVIDER = 0;
    private static final int ADDED_ATTRS_SHOWDIVIDERS = 1;
    private static final int ADDED_ATTRS_DIVIDERPADDING = 2;

    private Drawable mDivider;  // 分割线背景
    private int mDividerWidth; // 分割线宽度
    private int mDividerHeight; // 分割线高度
    private int mShowDivider; // 分割线显示位置
    private int mDividerPadding; // 分割线内边距

    /**
     * @param context
     * @param defStyleAttr 样式
     */
    public Indicator(Context context,int defStyleAttr) {
        super(context);
        TypedArray a = context.obtainStyledAttributes(null,ADDED_ATTRS,defStyleAttr,0);
        setDividerDrawable(a.getDrawable(ADDED_ATTRS_DIVIDER));
        mDividerPadding = a.getDimensionPixelSize(ADDED_ATTRS_DIVIDERPADDING,0);
        mShowDivider = a.getInteger(ADDED_ATTRS_SHOWDIVIDERS,SHOW_DIVIDER_NONE);
        a.recycle();
    }

    @Override
    public void setDividerDrawable(Drawable divider) {
        if(divider == mDivider) {
            return;
        }
        mDivider = divider;
        if(divider != null) {
            mDividerHeight = divider.getIntrinsicHeight();
            mDividerWidth = divider.getIntrinsicWidth();
        } else {
            mDividerHeight = 0;
            mDividerWidth = 0;
        }
        setWillNotDraw(divider == null);
        requestLayout();
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {

        final int index = indexOfChild(child);
        final int orientation = getOrientation();
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        // 这个东西要放在这里赋值，然后后面再调用它，这么做用处很大吗？
        if(canDrawDividerBeforePosition(index)) {
            if(orientation == VERTICAL) {
                lp.topMargin = mDividerHeight;
            } else {
                lp.leftMargin = mDividerWidth;
            }
        }

        final int count = getChildCount();
        if(index == count-1) {
            if(canDrawDividerBeforePosition(index)) {
                if(orientation == VERTICAL) {
                    lp.bottomMargin = mDividerHeight;
                } else {
                    lp.rightMargin = mDividerWidth;
                }
            }
        }

        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mDivider != null) {
            if(getOrientation() == VERTICAL) {
                drawVerticalDivider(canvas);
            } else {
                drawHorizontalDivider(canvas);
            }
        }
        super.onDraw(canvas);
    }

    /**
     * 画垂直布局的分界线
     */
    private void drawVerticalDivider(Canvas canvas) {
        final int count = getChildCount();
        for (int i = 0;i < count;i++) {
            final View child = getChildAt(i);
            if(child != null && child.getVisibility() != GONE) {
                if(canDrawDividerBeforePosition(i)) {
                    final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    drawDivider(canvas,child.getTop()-lp.topMargin,true);
                }
            }
        }
    }

    /**
     * 画水平布局的分界线
     */
    private void drawHorizontalDivider(Canvas canvas) {
        final int count = getChildCount();
        for (int i = 0;i < count;i++) {
            final View child = getChildAt(i);
            if(child != null && child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                drawDivider(canvas,child.getLeft()-lp.leftMargin,false);
            }
        }
    }


    /**
     * 判断在这个子控件之前是否可以画分割线
     * @param position
     * @return
     */
    private boolean canDrawDividerBeforePosition(int position) {
        if(position == 0 || position == getChildCount()) {
            return false;
        }
        if((mShowDivider & SHOW_DIVIDER_MIDDLE) != 0) {
            for (int i = position -1;i >= 0;i--) {
                if (getChildAt(i).getVisibility() == VISIBLE) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 画线
     * @param canvas 画布
     * @param distance
     * @param isVertical
     */
    private void drawDivider(Canvas canvas,int distance,boolean isVertical) {
        if(isVertical) {
            mDivider.setBounds(getPaddingLeft()+mDividerPadding,distance,getWidth()-getPaddingRight()
            -mDividerPadding,distance+mDividerHeight);
        } else {
            mDivider.setBounds(distance,getPaddingTop()+mDividerPadding,distance+mDividerWidth,
                    getHeight()-mDividerPadding-getPaddingBottom());
        }
        mDivider.draw(canvas);
    }

}
