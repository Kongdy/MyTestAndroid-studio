package com.example.hmyd.mytestandroid_studio.adapter;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.example.hmyd.mytestandroid_studio.widgets.IndicatorBaseAdapter;
import com.example.hmyd.mytestandroid_studio.widgets.IndicatorIconAdapter;

import java.util.List;

/**
 * @author kongdy
 *         on 2016/3/16
 *   indicator适配
 */
public class IndicatorTabWithIconAdapter extends IndicatorBaseAdapter implements IndicatorIconAdapter {

    private List<Fragment> fragments;

    private List<String> labels;

    private List<Bitmap> icons;

    private FrameLayout parentLayout;


    public IndicatorTabWithIconAdapter(List<Fragment> fragments, List<String> labels, List<Bitmap> icons,
                                       FrameLayout parentLayout) {
        this.fragments = fragments;
        this.labels = labels;
        this.icons = icons;
        this.parentLayout = parentLayout;
    }

    @Override
    public int getFragmentCount() {
        return fragments.size();
    }

    @Override
    public int getLabelsCount() {
        return labels.size();
    }

    @Override
    public Fragment getFragment(int position) {
        return fragments.get(position%getFragmentCount());
    }

    @Override
    public String getLabel(int position) {
        return labels.get(position%getLabelsCount());
    }

    @Override
    public FrameLayout getParentLayout() {
        return parentLayout;
    }

    @Override
    public int getIconCount() {

        return icons == null?0:icons.size();
    }

    @Override
    public Bitmap getIcon(int position) {
        if(icons != null && icons.size() > 0){
            return icons.get(position%getIconCount());
        } else {
            return null;
        }
    }
}
