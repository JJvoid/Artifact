package com.thinkrace.NewApplication.Uti;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpURLConnectionJson {
	private HttpParams httpParams;
	private HttpClient httpClient;
	private String MethodName = "";
	private String params;

	public HttpURLConnectionJson(String methodName, String param) {
		MethodName = methodName;
		this.params = param;
	}

	public String doPost() {
		String strResult = "";
		HttpPost httpRequest = new HttpPost(Constant.Connection_URL
				+ MethodName);
		httpRequest.addHeader("Content-Type", "application/json");
		try {
			httpRequest.setEntity(new StringEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			} else {
				strResult = "NetworkError";
			}
		} catch (ClientProtocolException e) {
			strResult = "NetworkError";
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			strResult = "NetworkError";// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			strResult = "NetworkError";// TODO: handle exception
			e.printStackTrace();
		}
		return strResult;
	}

	public HttpClient getHttpClient() {
		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		HttpClientParams.setRedirecting(httpParams, true);
		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);

		// 创建一个 HttpClient 实例

		// 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient

		// 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient
		httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}

}
