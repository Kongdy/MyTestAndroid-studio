package com.example.hmyd.mytestandroid_studio.view;

import com.example.hmyd.mytestandroid_studio.listener.ScrollViewListener;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * ��дscrollerview����scrollerlistener��¶����
 * @author wangk
 */
public class MyScrollerView extends ScrollView {
	
	
	private ScrollViewListener scrollviewlistener; // ��������

	public MyScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MyScrollerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyScrollerView(Context context) {
		super(context);
	}
	
	public void setOnScrollViewListener(ScrollViewListener scrollviewlistener) {
		this.scrollviewlistener = scrollviewlistener;
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(scrollviewlistener != null) {
			scrollviewlistener.ScrollChanged(this, l, t, oldl, oldt);
		}
	}
	
}
