package com.thinkrace.NewApplication.Model;

import java.sql.Date;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ResolveData {

	private Gson gson;

	public ResolveData() {
		gson = new GsonBuilder()
				.registerTypeAdapter(java.sql.Date.class,
						new SQLDateSerializer())
				.setDateFormat(java.text.DateFormat.LONG).create();
	}

	public class SQLDateSerializer implements JsonSerializer<java.sql.Date> {

		@Override
		public JsonElement serialize(Date arg0, java.lang.reflect.Type arg1,
				JsonSerializationContext arg2) {
			return new JsonPrimitive(arg0.toString());
		}

	}

	public int returnState(String ResolveString) {
		try {
			JSONObject jsonObject = new JSONObject(ResolveString);
			return jsonObject.optInt("State");
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int returnloginType(String ResolveString) {
		try {
			JSONObject jsonObject = new JSONObject(ResolveString);
			return jsonObject.optInt("LoginType");
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public LoginUserInfoModel returnLoginUserInfoModel(String json) {
		try {
			return gson.fromJson(json, LoginUserInfoModel.class);
		} catch (Exception e) {
			return gson.fromJson(json, LoginUserInfoModel.class);
		}
	}

	public String returnAccessToken(String ResolveString) {
		try {
			JSONObject jsonObject = new JSONObject(ResolveString);
			return jsonObject.getString("AccessToken");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public LoginDeviceInfoModel returnLoginDeviceInfoModel(String json) {
		try {
			return gson.fromJson(json, LoginDeviceInfoModel.class);
		} catch (Exception e) {
			return gson.fromJson(json, LoginDeviceInfoModel.class);
		}
	}

	public ExcdeptionListWhitoutCodeReturnModel returnExcdeptionListWhitoutCodeReturnModel(
			String json) {
		try {
			return gson.fromJson(json,
					ExcdeptionListWhitoutCodeReturnModel.class);
		} catch (Exception e) {
			return gson.fromJson(json,
					ExcdeptionListWhitoutCodeReturnModel.class);
		}
	}
}
