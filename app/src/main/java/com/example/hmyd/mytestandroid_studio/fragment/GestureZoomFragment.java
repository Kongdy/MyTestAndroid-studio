package com.example.hmyd.mytestandroid_studio.fragment;

import com.example.hmyd.mytestandroid_studio.R;
import com.example.hmyd.mytestandroid_studio.fragment.BasicFragment;
import com.example.hmyd.mytestandroid_studio.tools.BitmapHelp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 手势放大缩小图片
 * @author wangk
 */
public class GestureZoomFragment extends BasicFragment {

	private ImageView controll_view;

	@Override
	public int getLayoutResId() {
		return R.layout.layout_gesture_zoom;
	}


	@Override
	protected void setParams() {
		controll_view = (ImageView) rootView.findViewById(R.id.controll_view);

		BitmapHelp.getInstance(getActivity()).displayBitmapFromResource(R.mipmap.control_img,controll_view);
	}

	@Override
	public void onClick(View v) {

	}
}
