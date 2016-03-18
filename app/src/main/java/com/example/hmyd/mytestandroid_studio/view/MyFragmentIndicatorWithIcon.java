package com.example.hmyd.mytestandroid_studio.view;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.hmyd.mytestandroid_studio.R;
import com.example.hmyd.mytestandroid_studio.tools.BitmapHelp;
import com.example.hmyd.mytestandroid_studio.widgets.IndicatorBase;
import com.example.hmyd.mytestandroid_studio.widgets.IndicatorBaseAdapter;
import com.example.hmyd.mytestandroid_studio.widgets.IndicatorIconAdapter;


/**
 * @author kongdy
 *         on 2016/3/15
 * 跟随fragment，切换的底部菜单
 */
public class MyFragmentIndicatorWithIcon extends HorizontalScrollView implements IndicatorBase {

    public static int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;

    public static int WARP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    private FragmentManager fm;

    private int selectIndex = 0; // 当前选择的position

    private Indicator indicator;

    private IndicatorBaseAdapter adapter;

    private int mMaxTabWidth; // 每个tab的最大宽度

    private Runnable tabAnimalRunnable; // 在点击的时候,如果indicator的长度大于屏幕宽度，会滑到那个控件所在的位置



    public MyFragmentIndicatorWithIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        indicator = new Indicator(getContext(),R.attr.tabWithIconStyle);
        addView(indicator,new ViewGroup.LayoutParams(WARP_CONTENT,MATCH_PARENT));
    }


    public FrameLayout getParentLayout() {
        return adapter.getParentLayout();
    }


    /**
     * 设置适配器
     * @param adapter
     */
    public void setAdapter(IndicatorBaseAdapter adapter) {
        if(adapter == null) {
            return;
        }
        this.adapter = adapter;
        notifyDataSetChanged();
    }

    @Override
    public void setAdapter(IndicatorBaseAdapter adapter, int initPosition) {

    }

    public IndicatorBaseAdapter getAdapter() {
        return adapter;
    }

    /**
     * 开始填充
     */
    public void notifyDataSetChanged() {
        IndicatorIconAdapter iconadapter = null;
        fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
        if(fm == null) {
            fm = ((FragmentActivity)getContext()).getSupportFragmentManager();
        }
        FragmentTransaction ft = fm.beginTransaction();
        indicator.removeAllViews();
        iconadapter = (IndicatorIconAdapter)adapter;
        for(int i = 0;i < adapter.getFragmentCount();i++) {
            if(adapter.getFragment(i) != null) {
               if(fm.getFragments() != null && fm.getFragments().contains(adapter.getFragment(i))){
                    ft.show(adapter.getFragment(i));
               } else {
                   ft.add(adapter.getParentLayout().getId(),adapter.getFragment(i));
               }
                Bitmap bitmap = null;
                if(iconadapter != null && i < iconadapter.getIconCount()) {
                    bitmap = iconadapter.getIcon(i);
                }
                addTab(i,adapter.getLabel(i),bitmap);
            } else {
                Log.e("MyFragmentIndicator","there is a null fragment");
            }
        }
        ft.commitAllowingStateLoss();
        if(adapter.getFragmentCount() >= 1 ){
            selectPosition(selectIndex);
        }
    }

    /**
     * 根据position显示某个fragment
     * @param index
     */
    public void selectPosition(int index) {
        if(fm == null) {
            throw new IllegalStateException("fragmentManager is null");
        }
        FragmentTransaction ft = fm.beginTransaction();
        for(int i = 0;i < adapter.getFragmentCount();i++) {
            final TabView tabview = (TabView) indicator.getChildAt(i);
            if(adapter.getFragment(i) != null) {
                boolean isSelect = i == index;
                if(i == index) {
                    ft.show(adapter.getFragment(i));
                } else {
                    ft.hide(adapter.getFragment(i));
                }
                tabview.setSelected(isSelect);
                if(isSelect) {
                    animalToTab(index);
                }
            }
        }
        selectIndex = index;
        ft.commitAllowingStateLoss();
    }

    /**
     * 隐藏所有fragment
     */
    public void hideAllFragment() {
        if(fm == null) {
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        for(int i = 0;i < adapter.getFragmentCount();i++) {
            if(adapter.getFragment(i) != null) {
               ft.hide(adapter.getFragment(i));
            }
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 清除底部菜单所有选择状态
     */
    public void clearAllBottomSelect() {
        // TODO
    }

    /**
     * 添加底部菜单
     * @param position
     * @param text
     * @param bitmap
     */
    public void addTab(int position,CharSequence text,Bitmap bitmap) {
        TabView tabView = new TabView(getContext());
        tabView.index = position;
        tabView.setFocusable(true);
        tabView.setText(text);
        tabView.setOnClickListener(tabClickListener);
        if(bitmap != null) {
            Drawable drawable = new BitmapDrawable(getResources(),bitmap);
            tabView.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
        }
        indicator.addView(tabView,new LinearLayout.LayoutParams(0,MATCH_PARENT,1));
    }


    /**
     * 以动画的形式过渡到下个tab
     * @param position
     */
    private void animalToTab(int position) {
        if(tabAnimalRunnable != null) {
            removeCallbacks(tabAnimalRunnable);
        }
        final TabView tabiew = (TabView) indicator.getChildAt(position);
        tabAnimalRunnable = new Runnable() {

            @Override
            public void run() {
                final int scrolToPosition = tabiew.getLeft()-(getWidth()-tabiew.getWidth());
                smoothScrollTo(scrolToPosition,0);
                tabAnimalRunnable = null;
            }
        };
        post(tabAnimalRunnable);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(tabAnimalRunnable != null) {
            post(tabAnimalRunnable);
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(tabAnimalRunnable != null) {
            removeCallbacks(tabAnimalRunnable);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        /**
         * EXACTLY为精确尺寸，即给定尺寸或者Math_parent
         * AT_MOST为最大尺寸，例如warp_content
         * UNSPECIFIED为未指定尺寸
         */
        boolean fillViewPortFlag = withMode==MeasureSpec.EXACTLY; // 当布局中的宽度为Math_parent或者指定尺寸，则填充scrollview
        setFillViewport(fillViewPortFlag);

        int childCount = indicator.getChildCount();
        if(childCount > 1 && withMode != MeasureSpec.UNSPECIFIED) {
            // 判断，如果tab个数超过2个，则按宽度的最大尺寸给予，否则，平分宽度
            if(childCount > 2) {
                mMaxTabWidth = (int)(MeasureSpec.getSize(widthMeasureSpec)*0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec)/2;
            }
        } else if(childCount == 1) {
            mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            mMaxTabWidth = -1;
        }

        int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newWidth = getMeasuredWidth();
        if(fillViewPortFlag && newWidth != oldWidth) {
            selectPosition(selectIndex);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class TabView extends TextView {

        private int index;

        public TabView(Context context) {
            super(context,null,R.attr.tabWithIconStyle);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            // 当tab宽度超过计算出来的宽度的时候，则重新指定宽度
            if(mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth,MeasureSpec.EXACTLY)
                ,heightMeasureSpec);
            }
        }

        public int getIndex() {
            return index;
        }
    }

    private OnClickListener tabClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v instanceof TabView) {
                TabView tabview = (TabView)v;
                int newSelectPosition = tabview.getIndex();
                selectPosition(newSelectPosition);
            }
        }
    };

}
