package com.thinkrace.NewApplication.Library;

import com.thingrace.newapplication.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class MyPullUpListView extends ListView implements
		AbsListView.OnScrollListener {
	private View footerView = null;
	private Context context;
	private MyPullUpListViewCallBack myPullUpListViewCallBack;
	private int firstVisibleItem;

	public MyPullUpListView(Context context) {
		super(context);
		this.context = context;
		initListView();
	}

	public MyPullUpListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initListView();
	}

	private void initListView() {

		// ΪListView���û�������
		setOnScrollListener(this);
	}

	public void initBottomView() {

		if (footerView == null) {
			footerView = LayoutInflater.from(this.context).inflate(
					R.layout.list_footer, null);
		}
		addFooterView(footerView);
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {

		// ���������ײ�ʱ
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& firstVisibleItem != 0) {
			myPullUpListViewCallBack.scrollBottomState();
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
		if (footerView != null) {
			// �жϿ���Item�Ƿ����ڵ�ǰҳ����ȫ��ʾ
			if (visibleItemCount == totalItemCount) {
				// removeFooterView(footerView);
				footerView.setVisibility(View.GONE);// ���صײ�����
			} else {
				// addFooterView(footerView);
				footerView.setVisibility(View.VISIBLE);// ��ʾ�ײ�����
			}
		}

	}

	public void setMyPullUpListViewCallBack(
			MyPullUpListViewCallBack myPullUpListViewCallBack) {
		this.myPullUpListViewCallBack = myPullUpListViewCallBack;
	}

	public interface MyPullUpListViewCallBack {

		void scrollBottomState();
	}
}
