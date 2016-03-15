package com.example.hmyd.mytestandroid_studio;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

/**
 * 图片增强
 * @author wangk
 *
 */
public class PicPowerActivity extends BasicActivity {

	private FragmentManager fm;

	private GestureZoomFragment gestureZoomFragment; // 图片手势放大缩小fragment

	@Override
	public void onClick(View v) {

	}

	@Override
	public void setParams(Bundle savedInstanceState) {
		setContentView(R.layout.activity_pic_power);
		fm = getSupportFragmentManager();

	}
}
