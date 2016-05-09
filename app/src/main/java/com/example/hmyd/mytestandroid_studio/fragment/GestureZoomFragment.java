package com.example.hmyd.mytestandroid_studio.fragment;

import com.example.hmyd.mytestandroid_studio.R;
import com.example.hmyd.mytestandroid_studio.view.PowerImageView;

import android.view.View;

/**
 * 手势放大缩小图片
 * @author wangk
 */
public class GestureZoomFragment extends BasicFragment {

	private PowerImageView controll_view;

	@Override
	public int getLayoutResId() {
		return R.layout.layout_gesture_zoom;
	}


	@Override
	protected void setParams() {
		controll_view = (PowerImageView) rootView.findViewById(R.id.controll_view);
		controll_view.setImageResource(R.drawable.test_super_big);
	}

	@Override
	public void onClick(View v) {

	}

}
