package com.example.hmyd.mytestandroid_studio.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.util.TypedValue;
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
import com.example.hmyd.mytestandroid_studio.tools.Utils;


/**
 * recyclerView适配
 * @author wangk
 *
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter {
	
	private Context mContext;
	private List<TModel> data;

	private boolean isScrolled; // 是否正在滑动
	private RecyclerView myR;

	private List<Integer> visibleItem; // 当前屏幕显示item



	public MyRecyclerAdapter(final Context mContext, final List<TModel> data, final RecyclerView myR) {
		isScrolled = false;
		this.mContext = mContext;
		this.data = data;
		this.myR = myR;
		visibleItem = new ArrayList<>();
		// 监听滑动事件
		myR.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if(newState == 0) {
					isScrolled = false;

					for (int i =0;i< visibleItem.size();i++) {
						ImageView view = (ImageView) myR.findViewWithTag(visibleItem.get(i)+"");
						BitmapHelp.getInstance(mContext).displayBitmapFromResource(visibleItem.get(i),view);
					}
					visibleItem.clear();
				} else {
					isScrolled = true;
				}

			}
		});
	}

	@Override
	public int getItemCount() {
		return data==null?0:data.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewholder, int i) {
		MyViewHolder holder = (MyViewHolder) viewholder;
		holder.positon = i;
		if(visibleItem.size()>(int)(Utils.SCREEN_HEIGHT/Utils.getRawSize(mContext, TypedValue.COMPLEX_UNIT_DIP,100))
				&& visibleItem.size()>0) {
			visibleItem.remove(0);
		}

		holder.t1.setText(data.get(i).str);
	//	holder.i1.setImageResource(data.get(i).resid);
		// 三级缓存加载
		holder.i1.setImageResource(R.color.m_gray_1);
		holder.i1.setTag(data.get(i).resid+"");
		visibleItem.add(data.get(i).resid);
		// 为了内存优化最大化，滑动的时候不加载图片
		if(!isScrolled && holder.i1.getTag()!= null && (data.get(i).resid+"").equals(holder.i1.getTag())) {
			BitmapHelp.getInstance(mContext).displayBitmapFromResource(data.get(i).resid,holder.i1);
		}
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

		/**
		 * 获取当前item控件的所占高度
		 * @return
         */
		public int getViewHolderHeight() {

			return 0;
		}
	}

}
