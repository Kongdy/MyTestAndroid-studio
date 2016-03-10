package com.example.hmyd.mytestandroid_studio;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * activity����
 * @author wangk
 */
public abstract class BasicActivity extends Activity {
	
	public String label;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setParams();
		AppBasic.getInstance().addToActivityStacks(this, label==null?"":label);
	}
	public abstract void setParams();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.base_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

//	public void getOverflowMenu() {
//		try {
//			ViewConfiguration config = ViewConfiguration.get(this);
//			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
//			if(menuKeyField != null) {
//				menuKeyField.setAccessible(true);
//				menuKeyField.setBoolean(config,false);
//			}
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.test1:
				Toast.makeText(this, "test1", Toast.LENGTH_LONG).show();
				break;
			case R.id.test2:
				Toast.makeText(this, "test2", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
		}
		return false;
	}
}
