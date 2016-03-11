package com.example.hmyd.mytestandroid_studio;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.hmyd.mytestandroid_studio.tools.Utils;

/**
 * activity基类
 * @author wangk
 */
public abstract class BasicActivity extends AppCompatActivity
									implements View.OnClickListener {
	
	public String label;

	private Toolbar toolbar;

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setParams(savedInstanceState);
		// 初始化屏幕宽高
		Utils.initScreenSize(this);
		//透明状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		//透明导航栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		AppBasic.getInstance().addToActivityStacks(this, label==null?"":label);
		declareParamsSetting();
	}



	private void declareParamsSetting() {
		toolbar = (Toolbar) findViewById(R.id.my_tool_bar);
		if(toolbar != null) {
			toolbar.setTitle(getTitle());
			toolbar.setTitleTextColor(Color.WHITE);
			toolbar.setNavigationIcon(R.mipmap.ic_launcher);
			setSupportActionBar(toolbar);
		} else {
//			Toolbar.LayoutParams params = new Toolbar.LayoutParams();
//			addContentView(LayoutInflater.from(this).inflate(R.layout.view_title_layout),);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main,menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.function_menu:
				Toast.makeText(getApplicationContext(),"functionMenu",Toast.LENGTH_SHORT).show();
				break;
			case R.id.test1:
				Toast.makeText(getApplicationContext(),getString(R.string.test),Toast.LENGTH_SHORT).show();
				break;
			case R.id.test2:
				Toast.makeText(getApplicationContext(),getString(R.string.test1),Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	public View getRootView() {
		return ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
	}

	public View getRootViewGroup() {
		return ((ViewGroup)findViewById(android.R.id.content));
	}

	/**
	 * 获取标题栏toolbar对象
	 * @return
     */
	public Toolbar getToolbar() {
		return toolbar;
	}


	/**
	 * 点击事件
	 * @param v
     */
	@Override
	public abstract void onClick(View v);

	/**
	 *
	 * @param savedInstanceState
     */
	public abstract void setParams(Bundle savedInstanceState);

}
