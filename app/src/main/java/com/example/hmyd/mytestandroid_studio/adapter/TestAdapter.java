package com.example.hmyd.mytestandroid_studio.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.hmyd.mytestandroid_studio.R;

import java.util.List;

/**
 * @author kongdy
 * @date 2017-10-12 14:38
 * @TIME 14:38
 **/

public class TestAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public TestAdapter(@Nullable List<String> data) {
        super(R.layout.view_list_test_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.content,item);
    }
}
