package com.example.hmyd.mytestandroid_studio;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hmyd.mytestandroid_studio.adapter.MyArrayAdapter;
import com.example.hmyd.mytestandroid_studio.listener.ScrollViewListener;
import com.example.hmyd.mytestandroid_studio.tools.Utils;
import com.example.hmyd.mytestandroid_studio.view.MyScrollerView;

/**
 * listviewͷ标题栏动态浮动
 * 
 * @author wangk
 *
 */
public class T1Activity extends BasicActivity {

	private TextView t0;
	private TextView t1;
	private TextView h1;
	private ListView l1;

	private RelativeLayout main_content;

	private MyScrollerView myScrollView;

	private List<String> data;

	private OnGlobalLayoutListener mGlobalLayoutListener;

	private boolean isHiddenAll; // �ж�listview���ϲ����Ƿ���ȫ����

	private RelativeLayout top_view;
	
	private RelativeLayout head2;
	
	private RelativeLayout bottom_view;

	/**
	 * ������Ļ��ǰ���������һЩ���ֵ���
	 */
	private void screenControll(final View view) {
		mGlobalLayoutListener = new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				view.getWindowVisibleDisplayFrame(rect); // ��ȡ��ǰview��������
				// int unDisplayHeight =
				// view.getRootView().getHeight()-rect.bottom; // ��ȡ���ɼ�����߶�
				if (rect.bottom < 1000) {
					isHiddenAll = true;
				} else {
					isHiddenAll = false;
				}
				System.out.println("height:" + rect.bottom);
			}
		};
		myScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// listview���ϲ�����ȫ���صĻ��ѻ����¼��ַ���listview
				if (isHiddenAll) {
					l1.dispatchTouchEvent(event);
					return true;
				}
				return false;
			}
		});
		view.getViewTreeObserver().addOnGlobalLayoutListener(
				mGlobalLayoutListener);
	}

	@Override
	public void setParams(Bundle savedInstanceState) {
		setContentView(R.layout.activity_t1);
		data = new ArrayList<String>();
		for (int i = 0; i < 200; i++) {
			data.add("aaaaaa"
					+ Base64.encode(String.valueOf(i + 10).getBytes(),
					Base64.DEFAULT));
		}
		// ��ʼ����Ļ��С
		Utils.initScreenSize(this);

		t0 = (TextView) findViewById(R.id.text0); // �������Ӷ���λ
		t1 = (TextView) findViewById(R.id.text1); // ����Ԥ��ռλ
		h1 = (TextView) findViewById(R.id.head1); // listviewͷ����
		l1 = (ListView) findViewById(R.id.mylist); // �б�
		main_content = (RelativeLayout) findViewById(R.id.main_layout); // ����������
		myScrollView = (MyScrollerView) findViewById(R.id.myScrollView); // ����㻬������
		top_view = (RelativeLayout) findViewById(R.id.top_view); // ��������
		head2 = (RelativeLayout) findViewById(R.id.head2); // ��������
		bottom_view = (RelativeLayout) findViewById(R.id.bottom_view); // �������ָ�����

		// δ��ֹ��������������ʱ��listview����Ϊ���ֱ仯ͻȻ��Ӧ���֣���������oͷ���⸸����һ���߶�
//		ViewGroup.LayoutParams params = h1.getLayoutParams();
//		ViewGroup.LayoutParams params1 = bottom_view.getLayoutParams();
//		params1.height = params.height;
//		params1.width = params.width;
//
//		bottom_view.setLayoutParams(params1);
//		head2.setLayoutParams(params1);

		MyArrayAdapter adapter = new MyArrayAdapter(getApplicationContext(),
				data, l1);
		l1.setAdapter(adapter);
		adapter.forceChangeListviewHeight();
		myScrollView.smoothScrollTo(0, 0); // ��Ϊ�ı�listview�߶Ⱥ����ɿ�ʼ���治�ڶ�����������ʻ��ض���
		isHiddenAll = false;
		myScrollView.setOnScrollViewListener(new ScrollViewListener() {

			@Override
			public void ScrollChanged(MyScrollerView view, int x, int y,
									  int oldx, int oldy) {
				if (oldy > (l1.getY()-h1.getHeight())&& oldy != 0 && y!= 0) {
					//	System.out.println("���y:" + y + ",h1.getY():" + h1.getY()
					//			+ ",oldy:" + oldy);
					if(h1.getParent()!= head2) {
						bottom_view.removeView(h1);
						head2.addView(h1);
					}
				} else {
					//	System.out.println("δ���y:" + y + ",h1.getY():" + h1.getY()
					//			+ ",oldy:" + oldy);
					if(h1.getParent()!= bottom_view) {
						head2.removeView(h1);
						bottom_view.addView(h1);
					}
				}
			}
		});
	}
}
