package com.example.hmyd.mytestandroid_studio.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hmyd.mytestandroid_studio.AppBasic;
import com.example.hmyd.mytestandroid_studio.R;
import com.example.hmyd.mytestandroid_studio.httpmanager.HttpApiBase;
import com.example.hmyd.mytestandroid_studio.model.BaseModel;
import com.example.hmyd.mytestandroid_studio.model.TModel;
import com.example.hmyd.mytestandroid_studio.tools.Utils;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * activity基类
 * @author wangk
 */
public abstract class BasicActivity extends AppCompatActivity
									implements View.OnClickListener {
	
	public String label;

	private Toolbar toolbar;

	private Timer backPressedTimer; // 返回task

	public boolean hasTask;

	public ImageButton title_left;

	public  TextView title_center;

	public TextView title_right_txt;

	/**
	 * 隐藏菜单，调用的时候务必设为不隐藏
	 */
	public AppCompatSpinner title_right_spinner;

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setParams(savedInstanceState);
		// 初始化屏幕宽高
		Utils.initScreenSize(this);
		hasTask = false;

		//透明状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		//透明导航栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		AppBasic.getInstance(this).addToActivityStacks(this, label==null?"":label);
		declareParamsSetting();
	}


	private void declareParamsSetting() {
		toolbar = (Toolbar) findViewById(R.id.my_tool_bar);
		if(toolbar != null) {
			toolbar.setTitle("");
			title_left = (ImageButton) toolbar.findViewById(R.id.title_left);
			title_right_txt = (TextView) toolbar.findViewById(R.id.title_right);
			title_center = (TextView) toolbar.findViewById(R.id.title_center);
			title_right_spinner = (AppCompatSpinner) toolbar.findViewById(R.id.title_right_menu);

			title_left.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if( AppBasic.getInstance(BasicActivity.this).isLastActivity()) {
						backOp();
					} else {
						AppBasic.getInstance(BasicActivity.this).finishCurrentActivity();
					}

				}
			});
			title_center.setText(getTitle());
			setSupportActionBar(toolbar);
		} else {

		}
	}

    protected Callback<BaseModel> baseModelCallback = new Callback<BaseModel>() {
        @Override
        public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
        }

        @Override
        public void onFailure(Call<BaseModel> call, Throwable t) {

        }
    };

    protected void onRequest() {
        HttpApiBase.getMyRetrofit().test().enqueue(new Callback<TModel>() {
            @Override
            public void onResponse(Call<TModel> call, Response<TModel> response) {

            }

            @Override
            public void onFailure(Call<TModel> call, Throwable t) {

            }
        });
       // HttpApiBase.getMyRetrofit().test2()
        ;
    }


	/**
	 * 获取根布局
	 * @return
     */
	public View getRootView() {
		return ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
	}

	/**
	 * 获取根布局组
	 * @return
     */
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
	 *
	 * @param savedInstanceState
	 */
	public abstract void setParams(Bundle savedInstanceState);

	/**
	 * 点击事件
	 * @param v
     */
	@Override
	public abstract void onClick(View v);

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 拦截返回按钮,并且点击次数为一次，退出并结束当前activiity
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(AppBasic.getInstance(this).isLastActivity()) {
				backOp();
			}
			else {
				AppBasic.getInstance(this).finishCurrentActivity();
			}
			return true;
		}
		return super.onKeyDown(keyCode,event);
	}


	/**
	 * 返回操作
	 */
	public void backOp() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				hasTask = false;
			}
		};

		backPressedTimer = new Timer();
		backPressedTimer.schedule(task,2000);
		if(hasTask) {
			AppBasic.getInstance(this).finishCurrentActivity();
		} else {
			// Toast.Length_short略长于两秒
			Toast.makeText(this,getString(R.string.exit_try),Toast.LENGTH_SHORT).show();
		}
		hasTask = true;
	}

}
