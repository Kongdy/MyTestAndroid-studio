package com.example.hmyd.mytestandroid_studio;

import com.example.hmyd.mytestandroid_studio.fragment.BasicFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 手势放大缩小图片
 * @author wangk
 */
public class GestureZoomFragment extends BasicFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.layout_gesture_zoom, container, false);
		return rootView;
	}

	@Override
	public void onClick(View v) {

	}
}
