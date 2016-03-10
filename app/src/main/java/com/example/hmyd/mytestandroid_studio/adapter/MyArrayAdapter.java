package com.example.hmyd.mytestandroid_studio.adapter;

import java.util.List;

import com.example.hmyd.mytestandroid_studio.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * �򵥵ľ��е�һTextView��������
 * @author wangk
 */
public class MyArrayAdapter extends BaseAdapter {
	
	private Context context;
	private List<String> data;
	private int itemHeight;
	private ListView listview;
	
	public MyArrayAdapter(Context context, List<String> data,ListView listview) {
		this.context = context;
		this.data = data;
		this.listview = listview;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * ǿ�Ƹı�listview�߶�
	 */
	public void forceChangeListviewHeight() {
		ListAdapter adapter = listview.getAdapter();
		if(adapter==null) {
			return;
		}
		
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View item = adapter.getView(i,null, listview);
			item.measure(0, 0);
			totalHeight = totalHeight+item.getMeasuredHeight();
		}
		
		ViewGroup.LayoutParams params = listview.getLayoutParams();
		params.height = totalHeight+(adapter.getCount()-1)*listview.getDividerHeight();
		
		listview.setLayoutParams(params);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.layout_array_list_item1, null);
			holder.tv = (TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String str = data.get(position);
		holder.tv.setText(str);
		itemHeight = itemHeight+holder.tv.getHeight();
		return convertView;
	}

	class ViewHolder {
		TextView tv;
	}
}
