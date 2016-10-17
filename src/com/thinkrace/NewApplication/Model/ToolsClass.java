package com.thinkrace.NewApplication.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.thingrace.newapplication.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ToolsClass {

	public static void startNewActivity(Context context, Class<?> class1) {
		Intent intent = new Intent(context, class1);
		context.startActivity(intent);
		((Activity) context).overridePendingTransition(
				R.anim.base_slide_right_in, R.anim.base_slide_remain);
	}

	public Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		final ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		final Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(true);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		loadingDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// 使用ImageView显示动画
				spaceshipImage.startAnimation(hyperspaceJumpAnimation);
			}
		});
		return loadingDialog;

	}

	public String getStringToCal(String date) {
		// 2015-05-10 14:52:29
		// 0123456789
		final String year = date.substring(0, 4);
		final String month = date.substring(5, 7);
		final String day = date.substring(8, 10);
		final String hour = date.substring(11, 13);
		final String minute = date.substring(14, 16);
		String second = "00";
		try {
			second = date.substring(17, 19);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// final int millisecond = Integer.valueOf(date.substring(20, 23));
		Calendar result = new GregorianCalendar(Integer.valueOf(year),
				Integer.valueOf(month) - 1, Integer.valueOf(day),
				Integer.valueOf(hour), Integer.valueOf(minute),
				Integer.valueOf(second));
		// result.set(Calendar.MILLISECOND, millisecond);
		result.set(Calendar.MILLISECOND, 0);
		result.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

		// 1、取得本地时间：

		Calendar cal = Calendar.getInstance();

		// 2、取得时间偏移量：

		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);

		// 3、取得夏令时差：

		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

		// 4、从UTC时间里加上这些差量，即可以取得本地时间：

		result.add(result.MILLISECOND, (zoneOffset + dstOffset));

		int year1 = result.get(Calendar.YEAR);

		int month1 = result.get(Calendar.MONTH) + 1;

		int day1 = result.get(Calendar.DAY_OF_MONTH);

		int hour1 = result.get(Calendar.HOUR_OF_DAY);

		int minute1 = result.get(Calendar.MINUTE);

		int second1 = result.get(Calendar.SECOND);

		StringBuffer UTCTimeBuffer = new StringBuffer();

		String monthStr = "";

		String dayStr = "";

		String hourStr = "";

		String minuteStr = "";

		String secondStr = "";

		if (month1 < 10) {
			monthStr = "0" + month1;
		} else {
			monthStr = monthStr + month1;
		}

		if (day1 < 10) {
			dayStr = "0" + day1;
		} else {
			dayStr = dayStr + day1;
		}

		if (hour1 < 10) {
			hourStr = "0" + hour1;
		} else {
			hourStr = hourStr + hour1;
		}

		if (minute1 < 10) {
			minuteStr = "0" + minute1;
		} else {
			minuteStr = minuteStr + minute1;
		}

		if (second1 < 10) {
			secondStr = "0" + second1;
		} else {
			secondStr = secondStr + second1;
		}

		UTCTimeBuffer.append(year1).append("-").append(monthStr).append("-")

		.append(dayStr);

		UTCTimeBuffer.append(" ").append(hourStr).append(":").append(minuteStr)

		.append(":").append(secondStr);

		try {

			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			format.parse(UTCTimeBuffer.toString());

			return UTCTimeBuffer.toString();

		} catch (ParseException e) {

			e.printStackTrace();

		} catch (java.text.ParseException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		return null;
	}
}