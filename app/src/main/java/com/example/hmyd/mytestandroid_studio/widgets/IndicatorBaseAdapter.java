package com.example.hmyd.mytestandroid_studio.widgets;

import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

/**
 * @author kongdy
 *         on 2016/3/16
 *  indicator适配器接口
 */
public abstract class IndicatorBaseAdapter {

    public abstract int getFragmentCount();

    public abstract int getLabelsCount();

    public abstract Fragment getFragment(int position);

    public abstract String getLabel(int position);

    public abstract FrameLayout getParentLayout();

}
