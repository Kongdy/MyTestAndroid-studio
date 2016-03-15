package com.example.hmyd.mytestandroid_studio.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * fragment基类
 * 
 * @author wangk
 *
 */
public abstract class BasicFragment extends Fragment implements View.OnClickListener {
	
	public View rootView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

	}

	@Override
	public abstract void onClick(View v);
}
