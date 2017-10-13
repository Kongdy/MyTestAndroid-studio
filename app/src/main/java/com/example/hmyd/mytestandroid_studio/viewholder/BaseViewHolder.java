package com.example.hmyd.mytestandroid_studio.viewholder;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author kongdy
 * @date 2017-04-24 15:54
 * @TIME 15:54
 * recyclerView 的viewHolder基类
 **/
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding viewDataBinding;

    public BaseViewHolder(View itemView) {
        super(itemView);
        viewDataBinding = DataBindingUtil.bind(itemView);
    }

    public BaseViewHolder setBinding(int variableId,Object object) {
        viewDataBinding.setVariable(variableId,object);
        viewDataBinding.executePendingBindings();
        return this;
    }
}
