package com.example.hmyd.mytestandroid_studio.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import com.example.hmyd.mytestandroid_studio.R;
import com.example.hmyd.mytestandroid_studio.adapter.IndicatorTabWithIconAdapter;
import com.example.hmyd.mytestandroid_studio.fragment.BlurPicFragment;
import com.example.hmyd.mytestandroid_studio.fragment.GestureZoomFragment;
import com.example.hmyd.mytestandroid_studio.tools.BitmapHelp;
import com.example.hmyd.mytestandroid_studio.tools.Utils;
import com.example.hmyd.mytestandroid_studio.view.MyFragmentIndicatorWithIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片增强,所有图片控件均使用自定义powerImageview，增强控件
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
		labels.add("手势控制"); // 手势放大缩小、旋转等
		labels.add("图片处理"); // 图片处理。高斯模糊等
		labels.add("页3");

		icons = new ArrayList<>();
		icons.add(BitmapHelp.getInstance(this).adjustSizeBitmap((int)
		Utils.getRawSize(this, TypedValue.COMPLEX_UNIT_DIP,35),(int)
				Utils.getRawSize(this, TypedValue.COMPLEX_UNIT_DIP,35),R.mipmap.gesture_pic_menu_item));
        icons.add(BitmapHelp.getInstance(this).adjustSizeBitmap((int)
                Utils.getRawSize(this, TypedValue.COMPLEX_UNIT_DIP,35),(int)
                Utils.getRawSize(this, TypedValue.COMPLEX_UNIT_DIP,35),R.mipmap.power_pic_menu_item));

		adapter = new IndicatorTabWithIconAdapter(fragments,labels,icons,content);
		tabWithindicator.setAdapter(adapter);

	}
}
