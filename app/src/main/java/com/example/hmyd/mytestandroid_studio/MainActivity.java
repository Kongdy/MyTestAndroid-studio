package com.example.hmyd.mytestandroid_studio;

import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.hmyd.mytestandroid_studio.adapter.MyRecyclerAdapter;
import com.example.hmyd.mytestandroid_studio.model.TModel;

/**
 * recyclerview测试和获取root权限
 */
public class MainActivity extends BasicActivity {

	private RecyclerView myR;

	private List<TModel> data = new ArrayList<>();

//	private int[] resids = { R.drawable.__00000, R.drawable.__00001,
//			R.drawable.__00002, R.drawable.__00003, R.drawable.__00004,
//			R.drawable.__00005, R.drawable.__00006, R.drawable.__00007,
//			R.drawable.__00008, R.drawable.__00009, R.drawable.__00010,
//			R.drawable.__00011, R.drawable.__00012, R.drawable.__00013,
//			R.drawable.__00014, R.drawable.__00015, R.drawable.__00016,
//			R.drawable.__00017, R.drawable.__00018, R.drawable.__00019,
//			R.drawable.__00020, R.drawable.__00021, R.drawable.__00022,
//			R.drawable.__00023,R.drawable.__00024,R.drawable.__00025,
//			R.drawable.__00026,R.drawable.__00027,R.drawable.__00028,
//			R.drawable.__00029,R.drawable.__00030,R.drawable.__00031,
//			R.drawable.__00032,R.drawable.__00033,R.drawable.__00034,
//			R.drawable.__00035,R.drawable.__00036,R.drawable.__00037,
//			R.drawable.__00038,R.drawable.__00039,R.drawable.__00040,
//			R.drawable.__00041,R.drawable.__00042,R.drawable.__00043,
//			R.drawable.__00044,R.drawable.__00045,R.drawable.__00046,
//			R.drawable.__00047,R.drawable.__00048,R.drawable.__00049,
//			R.drawable.__00050,R.drawable.__00051,R.drawable.__00052};
	
	private List<Integer> resids = new ArrayList<>();

	public static boolean upgradeRootPermission(String pkgCodePath) {
		Process process = null;
		DataOutputStream os = null;
		try {
			String cmd = "chmod 777 " + pkgCodePath;
			process = Runtime.getRuntime().exec("su"); // �л���root�ʺ�
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		return true;
	}

	@Override
	public void setParams(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		Log.d("main", "start...............");
		myR = (RecyclerView) findViewById(R.id.my_r_view);
		// ����drawable��������Դ
		try {
			Field[] fields = R.drawable.class.getDeclaredFields();
			for (Field field : fields) {
				int s = field.getInt(new R.drawable());
				resids.add(s);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		LinearLayoutManager manager = new LinearLayoutManager(this);
		myR.setLayoutManager(manager);
		for (int i = 0; i < resids.size(); i++) {
			TModel m = new TModel();
			m.str = "tttttttttttttttttttt" + i;
			m.resid = resids.get(i);
			data.add(m);
		}
		MyRecyclerAdapter adapter = new MyRecyclerAdapter(
				getApplicationContext(), data);
		Log.d("main", "over constructor");
		myR.setAdapter(adapter);
		// boolean isGet = upgradeRootPermission(getPackageCodePath());
		// if(isGet) {
		// ((TextView)findViewById(R.id.tv)).setText("��ȡrootȨ�޳ɹ�");
		// } else {
		// ((TextView)findViewById(R.id.tv)).setText("��ȡrootȨ��ʧ��");
		// }
	}
}
