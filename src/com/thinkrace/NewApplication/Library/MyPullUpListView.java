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

		// 为ListView设置滑动监听
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

		// 当滑动到底部时
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& firstVisibleItem != 0) {
			myPullUpListViewCallBack.scrollBottomState();
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
		if (footerView != null) {
			// 判断可视Item是否能在当前页面完全显示
			if (visibleItemCount == totalItemCount) {
				// removeFooterView(footerView);
				footerView.setVisibility(View.GONE);// 隐藏底部布局
			} else {
				// addFooterView(footerView);
				footerView.setVisibility(View.VISIBLE);// 显示底部布局
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
