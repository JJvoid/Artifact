package com.thinkrace.NewApplication.Logic;

import java.sql.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.thinkrace.NewApplication.Model.ExcdeptionListWhitoutCodeRequestModel;
import com.thinkrace.NewApplication.Model.ExcdeptionListWhitoutCodeReturnModel;
import com.thinkrace.NewApplication.Model.ResolveData;
import com.thinkrace.NewApplication.Uti.HttpURLConnectionJson;

import android.util.Log;

public class ExcdeptionListWhitoutCodeDAL {
	private String resultString = null;
	private Gson gson;

	public String ExcdeptionListWhitoutCode(
			ExcdeptionListWhitoutCodeRequestModel excdeptionListWhitoutCodeRequestModel) {

		gson = new GsonBuilder()
				.registerTypeAdapter(java.sql.Date.class,
						new SQLDateSerializer())
				.setDateFormat(java.text.DateFormat.LONG).create();

		Log.i("HttpURLConnection",
				"ExcdeptionListWhitoutCode:Json="
						+ gson.toJson(excdeptionListWhitoutCodeRequestModel));

		HttpURLConnectionJson Connection = new HttpURLConnectionJson(
				"ExceptionMessage/ExcdeptionListWhitoutCode",
				gson.toJson(excdeptionListWhitoutCodeRequestModel));
		try {
			resultString = Connection.doPost();
			Log.i("HttpURLConnection", "ExcdeptionListWhitoutCode:result="
					+ resultString);
			return resultString;
		} catch (Exception e) {
			e.printStackTrace();
			return "NetworkError";
		}
	}

	public int returnState() {
		return new ResolveData().returnState(resultString);
	}

	public ExcdeptionListWhitoutCodeReturnModel returnExcdeptionListWhitoutCodeReturnModel() {
		return new ResolveData()
				.returnExcdeptionListWhitoutCodeReturnModel(resultString);
	}

	public class SQLDateSerializer implements JsonSerializer<java.sql.Date> {

		@Override
		public JsonElement serialize(Date arg0, java.lang.reflect.Type arg1,
				JsonSerializationContext arg2) {
			return new JsonPrimitive(arg0.toString());
		}

	}

}
