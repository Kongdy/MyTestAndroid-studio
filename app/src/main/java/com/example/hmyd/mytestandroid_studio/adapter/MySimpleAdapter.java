package com.example.hmyd.mytestandroid_studio.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hmyd.mytestandroid_studio.R;

public class MySimpleAdapter extends BaseAdapter {
	private Context context;
	private String[] data;
	
	public MySimpleAdapter(Context context, String[] data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
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
		String str = data[position];
		holder.tv.setText(str);
		return convertView;
	}

	class ViewHolder {
		TextView tv;
	}
}
