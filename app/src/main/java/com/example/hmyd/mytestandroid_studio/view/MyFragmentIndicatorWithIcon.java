package com.example.hmyd.mytestandroid_studio.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.hmyd.mytestandroid_studio.R;

import java.util.List;

/**
 * @author kongdy
 *         on 2016/3/15
 * 跟随fragment，切换的底部菜单
 */
public class MyFragmentIndicatorWithIcon extends LinearLayout implements View.OnClickListener {
    private List<Fragment> fragments;

    private List<String> labels; // 标签文字

    private List<Bitmap> icons; // 标签图片

    private FrameLayout parentLayout;// 用来填充fragment的layout

    private FragmentManager fm;

    private int selectIndex; // 当前选择的position

    private Indicator indicator;

    public MyFragmentIndicatorWithIcon(Context context,List<Fragment> fragments,List<String> labels,List<Bitmap> icons) {
        super(context);
        this.fragments = fragments;
        this.labels = labels;
        this.icons = icons;
    }

    public MyFragmentIndicatorWithIcon(Context context,List<Fragment> fragments,List<String> labels) {
        super(context);
        this.fragments = fragments;
        this.labels = labels;
    }


    /**
     * 设置fragment填充布局，这个方法必须调用,设置完成会立即填充
     * @param parentLayout
     */
    public void setParentLayout(FrameLayout parentLayout) {
        this.parentLayout = parentLayout;
        notifyDataSetChanged();
    }

    public FrameLayout getParentLayout() {
        return parentLayout;
    }


    /**
     * 开始填充
     */
    private void notifyDataSetChanged() {
        indicator = new Indicator(getContext(),R.attr.tabWithIconStyle);
        fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
        if(fm == null) {
            fm = ((FragmentActivity)getContext()).getSupportFragmentManager();
        }
        FragmentTransaction ft = fm.beginTransaction();
        for(int i = 0;i < fragments.size();i++) {
            if(fragments.get(i) != null) {
               if(fm.getFragments().contains(fragments.get(i))){
                    ft.show(fragments.get(i));
               } else {
                   ft.add(R.id.pic_content,fragments.get(i));
               }
            } else {
                Log.e("MyFragmentIndicator","there is a null fragment");
            }
        }
        if(fragments.size() > 1 ){
            selectPosition(selectIndex);
        }
    }

    /**
     * 根据position显示某个fragment
     * @param index
     */
    public void selectPosition(int index) {
        if(fm == null) {
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        for(int i = 0;i < fragments.size();i++) {
            if(fragments.get(i) != null) {
                if(i == index) {
                    ft.show(fragments.get(i));
                } else {
                    ft.hide(fragments.get(i));
                }
            }
        }
    }

    /**
     * 隐藏所有fragment
     */
    public void hideAllFragment() {
        if(fm == null) {
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment fragment:fragments) {
            ft.hide(fragment);
        }
    }

    /**
     * 清除底部菜单所有选择状态
     */
    public void clearAllBottomSelect() {
        // TODO
    }



    @Override
    public void onClick(View v) {
        // TODO
    }
}
