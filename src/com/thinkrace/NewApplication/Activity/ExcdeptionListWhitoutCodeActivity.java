package com.thinkrace.NewApplication.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import com.thingrace.newapplication.R;
import com.thinkrace.NewApplication.Adapter.ExcdeptionListWhitoutCodeAdapter;
import com.thinkrace.NewApplication.Library.MyPullUpListView;
import com.thinkrace.NewApplication.Library.MyPullUpListView.MyPullUpListViewCallBack;
import com.thinkrace.NewApplication.Logic.ExcdeptionListWhitoutCodeDAL;
import com.thinkrace.NewApplication.Model.ExcdeptionListWhitoutCodeModel;
import com.thinkrace.NewApplication.Model.ExcdeptionListWhitoutCodeRequestModel;
import com.thinkrace.NewApplication.Model.ToolsClass;
import com.thinkrace.NewApplication.Uti.Constant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExcdeptionListWhitoutCodeActivity extends Activity {
	private Context context;
	private SharedPreferences globalVariablesp;

	private ArrayList<ExcdeptionListWhitoutCodeModel> excdeptionListWhitoutCodeModelList;
	private AsyncTaskExcdeptionListWhitoutCode asyncTaskExcdeptionListWhitoutCode;
	private ExcdeptionListWhitoutCodeRequestModel excdeptionListWhitoutCodeRequestModel;
	private ExcdeptionListWhitoutCodeDAL excdeptionListWhitoutCodeDAL;
	private ExcdeptionListWhitoutCodeAdapter excdeptionListWhitoutCodeAdapter;
	private ListView listView;

	private Dialog progressDialog;
	private boolean IsFirst = true;
	private Button back;

	private int visibleLastIndex = 0; // 最后的可视项索引
	private int visibleItemCount; // 当前窗口可见项总数

	private View loadMoreView;
	private TextView textView;
	// private int MaxItem = 50;
	private Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.excdeption_list_whitout_code_view);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ToolsClass.startNewActivity(context, LoginActivity.class);
				Intent intent = new Intent();
				// 把返回数据存入Intent
				intent.putExtra("result", "back");
				// 设置返回数据
				ExcdeptionListWhitoutCodeActivity.this.setResult(RESULT_OK,
						intent);
				// 关闭Activity
				ExcdeptionListWhitoutCodeActivity.this.finish();
			}
		});
		loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
		textView = (TextView) findViewById(R.id.loadMore);
		// textView.setVisibility(View.INVISIBLE);
		// loadMoreButton =
		// (Button)loadMoreView.findViewById(R.id.loadMoreButton);
		// loadMoreButton.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// loadMoreButton.setText("正在加载中..."); //设置按钮文字
		// handler.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// loadMoreData();
		// excdeptionListWhitoutCodeAdapter.notifyDataSetChanged();
		// loadMoreButton.setText("查看更多..."); //恢复按钮文字
		// }
		// },2000);
		//
		// }
		// });

		globalVariablesp = getSharedPreferences("globalvariable", 0);
		context = ExcdeptionListWhitoutCodeActivity.this;
		excdeptionListWhitoutCodeModelList = new ArrayList<ExcdeptionListWhitoutCodeModel>();
		asyncTaskExcdeptionListWhitoutCode = new AsyncTaskExcdeptionListWhitoutCode();
		excdeptionListWhitoutCodeRequestModel = new ExcdeptionListWhitoutCodeRequestModel();
		excdeptionListWhitoutCodeRequestModel.Id = globalVariablesp.getInt(
				"UserID", -1);
		excdeptionListWhitoutCodeRequestModel.TypeID = Constant.LoginType_User;
		excdeptionListWhitoutCodeRequestModel.UserID = globalVariablesp.getInt(
				"UserID", -1);
		excdeptionListWhitoutCodeRequestModel.DataCode = globalVariablesp
				.getString("DataCode", "");
		excdeptionListWhitoutCodeRequestModel.Token = globalVariablesp
				.getString("Access_Token", "");
		excdeptionListWhitoutCodeDAL = new ExcdeptionListWhitoutCodeDAL();

		getView();

		asyncTaskExcdeptionListWhitoutCode = new AsyncTaskExcdeptionListWhitoutCode();
		asyncTaskExcdeptionListWhitoutCode.executeOnExecutor(Executors
				.newCachedThreadPool());
		progressDialog.show();
	}

	public void getView() {

		progressDialog = new ToolsClass().createLoadingDialog(context, context
				.getResources().getString(R.string.app_Loding));
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				asyncTaskExcdeptionListWhitoutCode.cancel(true);
			}
		});

		listView = (ListView) findViewById(R.id.Warn_ListView);
		listView.addFooterView(loadMoreView);
		excdeptionListWhitoutCodeAdapter = new ExcdeptionListWhitoutCodeAdapter(
				context, excdeptionListWhitoutCodeModelList);
		listView.setAdapter(excdeptionListWhitoutCodeAdapter);
		listView.setOnScrollListener(new OnScrollListener() {

			private int visibleItemCount;

			// private int totalItemCount;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				int itemsLastIndex = excdeptionListWhitoutCodeAdapter
						.getCount() - 1; // 数据集最后一项的索引
				int lastIndex = itemsLastIndex + 1; // 加上底部的loadMoreView项
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& visibleLastIndex == lastIndex) {

					// 如果是自动加载,可以在这里放置异步加载数据的代码
					// if(visibleLastIndex == totalItemCount -
					// 1){Toast.makeText(context, "没有更多数据...",
					// Toast.LENGTH_SHORT).show();}
					// else{
					// textView.setVisibility(View.VISIBLE);
					excdeptionListWhitoutCodeRequestModel.PageNo++;
					asyncTaskExcdeptionListWhitoutCode = new AsyncTaskExcdeptionListWhitoutCode();
					asyncTaskExcdeptionListWhitoutCode
							.executeOnExecutor(Executors.newCachedThreadPool());
					Log.e("========================= ",
							"========================");
					Log.e("scrollState = ", scrollState + "");
					Log.e("========================= ",
							"========================");
					// textView.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				int MaxItem = 51;
				this.visibleItemCount = visibleItemCount;
				visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
				if (totalItemCount == MaxItem) {
					Toast.makeText(context, "没有更多数据...", Toast.LENGTH_SHORT)
							.show();
					listView.removeFooterView(loadMoreView);
				}

				Log.e("========================= ", "========================");
				Log.e("firstVisibleItem = ", firstVisibleItem + "");
				Log.e("visibleItemCount = ", visibleItemCount + "");
				Log.e("totalItemCount = ", totalItemCount + "");
				Log.e("========================= ", "========================");
			}
		});
	}

	// private void loadMoreData(){
	// excdeptionListWhitoutCodeRequestModel.PageNo++;
	// asyncTaskExcdeptionListWhitoutCode = new
	// AsyncTaskExcdeptionListWhitoutCode();
	// asyncTaskExcdeptionListWhitoutCode.executeOnExecutor(Executors.newCachedThreadPool());
	// }

	class AsyncTaskExcdeptionListWhitoutCode extends
			AsyncTask<Integer, String, String> {

		@Override
		protected String doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
			}
			excdeptionListWhitoutCodeDAL = new ExcdeptionListWhitoutCodeDAL();
			return excdeptionListWhitoutCodeDAL
					.ExcdeptionListWhitoutCode(excdeptionListWhitoutCodeRequestModel);
		}

		@Override
		protected void onPostExecute(String resultString) {
			// TODO Auto-generated method stub
			if (!resultString.equals("NetworkError")) {
				if (excdeptionListWhitoutCodeDAL.returnState() == Constant.State_0
						|| excdeptionListWhitoutCodeDAL.returnState() == Constant.State_100) {
					if (excdeptionListWhitoutCodeRequestModel.PageNo == 1) {
						excdeptionListWhitoutCodeModelList.clear();
					}
					excdeptionListWhitoutCodeModelList
							.addAll(excdeptionListWhitoutCodeDAL
									.returnExcdeptionListWhitoutCodeReturnModel().Items);
					if (excdeptionListWhitoutCodeDAL
							.returnExcdeptionListWhitoutCodeReturnModel().Items
							.size() > 0) {
						excdeptionListWhitoutCodeRequestModel.PageNo++;
					}
				}
				excdeptionListWhitoutCodeAdapter.notifyDataSetChanged();

			} else {
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.app_NetworkError), Toast.LENGTH_SHORT)
						.show();
			}

			progressDialog.dismiss();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (IsFirst) {
			progressDialog.show();
		}
		super.onResume();
	}

}
