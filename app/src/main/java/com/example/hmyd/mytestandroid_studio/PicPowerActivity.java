package com.example.hmyd.mytestandroid_studio;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;

import com.example.hmyd.mytestandroid_studio.adapter.IndicatorTabWithIconAdapter;
import com.example.hmyd.mytestandroid_studio.fragment.BlurPicFragment;
import com.example.hmyd.mytestandroid_studio.fragment.GestureZoomFragment;
import com.example.hmyd.mytestandroid_studio.view.MyFragmentIndicatorWithIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片增强
 * @author wangk
 *
 */
public class PicPowerActivity extends BasicActivity {

	private IndicatorTabWithIconAdapter adapter;

	private GestureZoomFragment gestureZoomFragment; // 图片手势控制fragment
	private BlurPicFragment blurPicFragment; // 图片高斯模糊fragment

	private List<Fragment> fragments;
	private List<String> labels;
	private List<Bitmap> icons;

	private MyFragmentIndicatorWithIcon tabWithindicator;
	private FrameLayout content;


	@Override
	public void onClick(View v) {

	}

	@Override
	public void setParams(Bundle savedInstanceState) {
		setContentView(R.layout.activity_pic_power);

		tabWithindicator = (MyFragmentIndicatorWithIcon) findViewById(R.id.bottom_menu);
		content = (FrameLayout) findViewById(R.id.pic_content);

		fragments = new ArrayList<>();
		gestureZoomFragment = new GestureZoomFragment();
		blurPicFragment = new BlurPicFragment();
		fragments.add(gestureZoomFragment);
		fragments.add(blurPicFragment);

		labels = new ArrayList<>();
		labels.add("页1");
		labels.add("页2");
		labels.add("页3");

		icons = new ArrayList<>();

		adapter = new IndicatorTabWithIconAdapter(fragments,labels,icons,content);
		tabWithindicator.setAdapter(adapter);

	}
}
