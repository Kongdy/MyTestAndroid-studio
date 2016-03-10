package com.example.hmyd.mytestandroid_studio;

import java.util.Stack;

import com.example.hmyd.mytestandroid_studio.model.TActivityModel;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * ȫ����
 * @author wangk
 */
public class AppBasic {

	private Stack<TActivityModel> activityStacks = new Stack<TActivityModel>(); // ?????
	private static AppBasic appbasic;

	/**
	 * ���Ψһʵ��
	 * @return
	 */
	public static AppBasic getInstance() {
		if (appbasic == null) {
			appbasic = new AppBasic();
		}
		return appbasic;
	}

	// ���̱߳�֤
	private AppBasic() {
	}

	/**
	 * ���뵽activityջ��
	 * 
	 * @param activity
	 */
	public void addToActivityStacks(Activity activity,String label) {
		TActivityModel model = toModel(activity, label);
		if (!activityStacks.contains(model)) {
			activityStacks.add(model);
		}
	}

	/**
	 * ת��Ϊʵ�����
	 * @param activity
	 * @param label
     * @return
     */
	public TActivityModel toModel(Activity activity,String label) {
		TActivityModel model = new TActivityModel();
		model.activity = activity;
		model.label = label;
		return model;
	}

	/**
	 * ����ָ��activity
	 * 
	 * @param activity
	 */
	public void finishActivity(Activity activity) {
		TActivityModel model = findModel(activity);
		if (activityStacks.contains(model)) {
			activityStacks.remove(model);
			model.activity.finish();
		}
	}
	
	/**
	 * ͨ��activity����һ��model
	 * @param activity
	 * @return
	 */
	public TActivityModel findModel(Activity activity) {
		for (TActivityModel tActivityModel : activityStacks) {
			if(tActivityModel.activity == activity) {
				return tActivityModel;
			}
		}
		return null;
	}

	/**
	 * ������ǰactivity
	 */
	public void finishCurrentActivity() {
		if (!activityStacks.isEmpty()) {
			finishActivity(activityStacks.lastElement().activity);
		}
	}

	/**
	 * ��������activity
	 */
	public void finishAllActivity() {
		for (TActivityModel model : activityStacks) {
			if (null != model) {
				model.activity.finish();
			}
		}
		activityStacks.clear();
	}

	/**
	 * �˳�����
	 * 
	 * @param context
	 */
	public void exitApplication(Context context) {
		try {
			finishAllActivity();
			ActivityManager manager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			manager.killBackgroundProcesses(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
			System.exit(0);
		}
	}

	/**
	 * ��õ�ǰactivity
	 * 
	 * @return
	 */
	public TActivityModel getCurrentActivity() {
		if (!activityStacks.isEmpty()) {
			return activityStacks.lastElement();
		}
		return null;
	}
	
	/**
	 * ��ȡactivityջ
	 * @return
	 */
	public Stack<TActivityModel> getActivityStacks() {
		return activityStacks;
	}
}
