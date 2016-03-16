package com.example.hmyd.mytestandroid_studio.widgets;

import android.support.v4.view.ViewPager;

/**
 * @author kongdy
 *         on 2016/3/16
 * indicator父类接口
 */
public interface IndicatorBase extends ViewPager.OnPageChangeListener{


    /**
     * 设置适配器
     * @param adapter 适配器
     */
    void setAdapter(IndicatorBaseAdapter adapter);

    /**
     * 设置适配器
     * @param adapter 适配器
     * @param initPosition 初始显示得fragment坐标
     */
    void setAdapter(IndicatorBaseAdapter adapter,int initPosition);

    /**
     * 界面刷新，也是初始化的过程
     */
    void notifyDataSetChanged();


    /**
     * 设置/跳转fragment
     * @param index
     */
    void selectPosition(int index);

}
