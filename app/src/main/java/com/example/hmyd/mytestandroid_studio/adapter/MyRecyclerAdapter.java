package com.example.hmyd.mytestandroid_studio.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hmyd.mytestandroid_studio.R;
import com.example.hmyd.mytestandroid_studio.model.TModel;
import com.example.hmyd.mytestandroid_studio.tools.BitmapHelp;


/**
 * recyclerView适配
 * @author wangk
 *
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter {
	
	private Context mContext;
	private List<TModel> data;

	public MyRecyclerAdapter(Context mContext, List<TModel> data) {
		this.mContext = mContext;
		this.data = data;
	}

	@Override
	public int getItemCount() {
		return data==null?0:data.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewholder, int i) {
		Log.d("madapter", "bind:"+i);
		MyViewHolder holder = (MyViewHolder) viewholder;
		holder.positon = i;
		holder.t1.setText(data.get(i).str);
		// 三级缓存加载
//		holder.i1.setImageResource(R.color.m_gray_1);
//		holder.i1.setTag(data.get(i).resid+"");
//		if(holder.i1.getTag()!= null && (data.get(i).resid+"").equals(holder.i1.getTag())) {
//			BitmapHelp.getInstance(mContext).displayBitmapFromResource(data.get(i).resid,holder.i1);
//		}
		BitmapHelp.getInstance(mContext).displayBitmapFromResource(data.get(i).resid,holder.i1);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewgroup, int position) {
		Log.d("madapter", "create:"+position);
		View view = LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.view_recycler_layout, viewgroup,false);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(params);
		return new MyViewHolder(view);
	}
	
	class MyViewHolder extends ViewHolder implements OnClickListener {
		
		public TextView t1;
		
		public ImageView i1;
		
		public int positon;

		public MyViewHolder(View itemView) {
			super(itemView);
			t1 = (TextView) itemView.findViewById(R.id.t1);
			i1 = (ImageView) itemView.findViewById(R.id.i1);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			Toast.makeText(mContext, "it's "+positon, Toast.LENGTH_SHORT).show();
		}
		
	}

}
