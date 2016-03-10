package com.example.hmyd.mytestandroid_studio;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * fragmentActivity����
 * @author wangk
 */
public abstract class BasicFragmentActivity extends FragmentActivity {
	
	public String label;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setParams();
		AppBasic.getInstance().addToActivityStacks(this, label==null?"":label);
	}

	public abstract void setParams();
	
}
