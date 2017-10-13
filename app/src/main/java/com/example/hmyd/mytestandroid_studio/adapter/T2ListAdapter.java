package com.example.hmyd.mytestandroid_studio.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.hmyd.mytestandroid_studio.R;
import com.example.hmyd.mytestandroid_studio.model.ContactModel;

import java.util.List;

/**
 * @author kongdy
 * @date 2017/10/13 14:02
 * @describe TODO
 **/

public class T2ListAdapter extends BaseQuickAdapter<ContactModel, BaseViewHolder> {

    public T2ListAdapter(@Nullable List<ContactModel> data) {
        super(R.layout.layout_contact_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactModel item) {
        helper.setText(R.id.actv_contact_name,item.getName());
    }
}
