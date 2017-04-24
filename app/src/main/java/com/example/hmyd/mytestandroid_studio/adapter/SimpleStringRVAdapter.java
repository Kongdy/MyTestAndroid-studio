package com.example.hmyd.mytestandroid_studio.adapter;

import com.example.hmyd.mytestandroid_studio.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @author kongdy
 * @date 2017-04-24 15:42
 * @TIME 15:42
 **/

public class SimpleStringRVAdapter extends BaseRecyclerAdapter<String> {

    public SimpleStringRVAdapter(int layoutId, List<String> mDatas, int mVariabldId) {
        super(layoutId, mDatas, mVariabldId);
    }

    @Override
    public void convert(BaseViewHolder holder, String s) {
    }
}
