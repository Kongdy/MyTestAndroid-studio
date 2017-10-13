package com.example.hmyd.mytestandroid_studio.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hmyd.mytestandroid_studio.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @author kongdy
 * @date 2017-04-24 15:50
 * @TIME 15:50
 *  recyclerView的adapter基类
 **/
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected int layoutId;
    protected List<T> mDatas;
    protected int mVariabldId;

    private OnItemClickListener OnItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public BaseRecyclerAdapter(int layoutId, List<T> mDatas, int mVariabldId) {
        this.layoutId = layoutId;
        this.mDatas = mDatas;
        this.mVariabldId = mVariabldId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder bv = new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false));
        return bv;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        T model = mDatas.get(position);

        holder.setBinding(mVariabldId,model);
        holder.itemView.setOnClickListener(getClickListener(holder.getAdapterPosition(),holder.itemView));
        holder.itemView.setOnLongClickListener(getLongClickListener(holder.getAdapterPosition(),holder.itemView));

        convert(holder,model);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public abstract void convert(BaseViewHolder holder,T t);

    private View.OnClickListener getClickListener(final int pos,final View view) {
        return (v -> {
            if(OnItemClickListener != null) {
                OnItemClickListener.onItemClick(this,view,pos);
            }
        });
    }

    private View.OnLongClickListener getLongClickListener(final int pos,final View view) {
        return (v->{
            if(onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(this,view,pos);
            }
            return true;
        });
    }


    public BaseRecyclerAdapter.OnItemClickListener getOnItemClickListener() {
        return OnItemClickListener;
    }

    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
        OnItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.Adapter adapter,View view,int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RecyclerView.Adapter adapter,View view,int position);
    }

}
