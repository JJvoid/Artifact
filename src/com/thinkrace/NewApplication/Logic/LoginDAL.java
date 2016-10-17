package com.thinkrace.NewApplication.Logic;

import java.sql.Date;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.thinkrace.NewApplication.Model.LoginDeviceInfoModel;
import com.thinkrace.NewApplication.Model.LoginModel;
import com.thinkrace.NewApplication.Model.LoginUserInfoModel;
import com.thinkrace.NewApplication.Model.ResolveData;
import com.thinkrace.NewApplication.Uti.HttpURLConnectionJson;

import android.util.Log;

public class LoginDAL {
	private String resultString = null;
	private Gson gson;

	public String Login(LoginModel loginModel) {

		gson = new GsonBuilder()
				.registerTypeAdapter(java.sql.Date.class,
						new SQLDateSerializer())
				.setDateFormat(java.text.DateFormat.LONG).create();

		Log.i("HttpURLConnection", "Login:Json=" + gson.toJson(loginModel));

		HttpURLConnectionJson Connection = new HttpURLConnectionJson(
				"User/Login", gson.toJson(loginModel));
		try {
			resultString = Connection.doPost();
			Log.i("HttpURLConnection", "Login:result=" + resultString);
			return resultString;
		} catch (Exception e) {
			e.printStackTrace();
			return "NetworkError";
		}
	}

	public int returnState() {
		return new ResolveData().returnState(resultString);
	}

	public int returnloginType() {
		return new ResolveData().returnloginType(resultString);
	}

	public String returnAccessToken() {
		return new ResolveData().returnAccessToken(resultString);
	}

	public LoginUserInfoModel returnLoginUserInfoModel() {
		return new ResolveData().returnLoginUserInfoModel(resultString);
	}

	public LoginDeviceInfoModel returnLoginDeviceInfoModel() {
		return new ResolveData().returnLoginDeviceInfoModel(resultString);
	}

	public class SQLDateSerializer implements JsonSerializer<java.sql.Date> {

		@Override
		public JsonElement serialize(Date arg0, java.lang.reflect.Type arg1,
				JsonSerializationContext arg2) {
			return new JsonPrimitive(arg0.toString());
		}

	}
}
