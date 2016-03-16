package com.example.hmyd.mytestandroid_studio.fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragment基类
 * 
 * @author wangk
 *
 */
public abstract class BasicFragment extends Fragment implements View.OnClickListener {
	
	public View rootView;

	private int LayoutResId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 设置布局id
	 * @return
     */
	public abstract int getLayoutResId();

	@Nullable
	@Override
	public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(getLayoutResId(),container,false);
		setParams();
		return rootView;
	}

	protected abstract void setParams();

	@Override
	public abstract void onClick(View v);
}
