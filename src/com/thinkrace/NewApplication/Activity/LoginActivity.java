package com.thinkrace.NewApplication.Activity;

import java.util.concurrent.Executors;

import com.thingrace.newapplication.R;
import com.thingrace.newapplication.R.id;
import com.thingrace.newapplication.R.layout;
import com.thingrace.newapplication.R.string;
import com.thinkrace.NewApplication.Logic.LoginDAL;
import com.thinkrace.NewApplication.Model.LoginModel;
import com.thinkrace.NewApplication.Model.LoginUserInfoModel;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	public static LoginActivity instance = null;
	private Context context;
	private SharedPreferences globaVariablesp;
	private LoginModel loginModel;
	private LoginDAL loginDAL;
	private AsyncLogin asyncLogin;
	private EditText Account_EditText;
	private EditText Password_EditText;
	private TextView Register_TextView;
	private TextView ForgotPassword_TextView;
	private TextView version_TextView;
	private Button Login_Button;
	private Dialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		context = LoginActivity.this;
		globaVariablesp = getSharedPreferences("globalvariable", 0);
		instance = this;
		loginModel = new LoginModel();
		loginDAL = new LoginDAL();
		asyncLogin = new AsyncLogin();

		progressDialog = new Dialog(context);
		progressDialog.setTitle("加载中");
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				asyncLogin.cancel(true);
			}
		});

		Account_EditText = (EditText) findViewById(R.id.Account_EditText);
		Password_EditText = (EditText) findViewById(R.id.Password_EditText);
		Register_TextView = (TextView) findViewById(R.id.Register_TextView);
		ForgotPassword_TextView = (TextView) findViewById(R.id.ForgotPassword_TextView);
		Login_Button = (Button) findViewById(R.id.Login_Bitton);
		Login_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Account_EditText.getText().toString().equals("")) {
					Toast.makeText(context, R.string.Login_Account_Empty,
							Toast.LENGTH_SHORT).show();
				} else if (Password_EditText.getText().toString().equals("")) {
					Toast.makeText(context, R.string.Login_Password_Empty,
							Toast.LENGTH_SHORT).show();
				} else {
					// Toast.makeText(context, "test",
					// Toast.LENGTH_SHORT).show();
					// ToolsClass.startNewActivity(context, WarnActivity.class);
					loginModel.Name = Account_EditText.getText().toString();
					loginModel.Pass = Password_EditText.getText().toString();
					globaVariablesp
							.edit()
							.putString("LoginAccount",
									Account_EditText.getText().toString())
							.putString("LoginPassword",
									Password_EditText.getText().toString())
							.commit();
					asyncLogin = new AsyncLogin();
					asyncLogin.executeOnExecutor(Executors
							.newCachedThreadPool());
					progressDialog.show();
				}
			}
		});
	}

	class AsyncLogin extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			loginDAL = new LoginDAL();
			return loginDAL.Login(loginModel);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if ("NetworkError".equals(result)) {
				Toast.makeText(context, R.string.app_NetworkError,
						Toast.LENGTH_SHORT).show();
			} else {
				if (loginDAL.returnState() == Constant.State_0) {
					Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
					if (loginDAL.returnloginType() == Constant.LoginType_User) {
						LoginUserInfoModel loginUserInfoModel = loginDAL
								.returnLoginUserInfoModel();
						globaVariablesp
								.edit()
								.putInt("UserID",
										loginUserInfoModel.Item.UserId)
								.putString("UserName",
										loginUserInfoModel.Item.Username)
								.putString("LoginName",
										loginUserInfoModel.Item.LoginName)
								.putString("UserHeadImage",
										loginUserInfoModel.Item.Avatar)
								.putString("AppDownloadURL",
										loginUserInfoModel.Item.CodeUrl)
								.putString("UserEmail",
										loginUserInfoModel.Item.Email)
								.putString("TimeZone",
										loginUserInfoModel.Item.Timezone)
								.putInt("DeviceCount",
										loginUserInfoModel.Item.DeviceCount)
								.commit();
						if (loginUserInfoModel.Item.DeviceCount > 0) {
							 ToolsClass.startNewActivity(context,
							 BaiduMapActivity.class);
//							startActivityForResult(new Intent(
//									LoginActivity.this,
//									ExcdeptionListWhitoutCodeActivity.class), 1);

						} else {
							globaVariablesp.edit()
									.putString("WaitFormMark", "Login")
									.commit();
							 ToolsClass.startNewActivity(context,
							 BaiduMapActivity.class);
//							startActivityForResult(new Intent(
//									LoginActivity.this,
//									ExcdeptionListWhitoutCodeActivity.class), 1);

						}
					}
				} else if (loginDAL.returnState() == Constant.State_1000) {
					Toast.makeText(
							context,
							context.getResources().getString(
									R.string.Login_AccountDoesNotExist),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							context,
							context.getResources().getString(
									R.string.Login_Failure), Toast.LENGTH_SHORT)
							.show();
				}
			}
			progressDialog.dismiss();
		}

	}

	public void LoadLoginInformation() {
		Account_EditText.setText(globaVariablesp.getString("LoginAccount", ""));
		Password_EditText.setText(globaVariablesp
				.getString("LoginPassword", ""));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String result = data.getExtras().getString("result");//

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		LoadLoginInformation();
		super.onResume();
	}
}
