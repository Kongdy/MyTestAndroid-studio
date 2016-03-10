package com.example.hmyd.mytestandroid_studio.listener;

import com.example.hmyd.mytestandroid_studio.view.MyScrollerView;

/**
 * �Զ���ӿڡ����ڱ�¶scrollerview�ռ��еĻ�������
 * @author wangk
 *
 */
public interface ScrollViewListener {
	void ScrollChanged(MyScrollerView view,int x,int y,int oldx,int oldy);
}
