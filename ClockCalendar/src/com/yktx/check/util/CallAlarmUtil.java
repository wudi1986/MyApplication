package com.yktx.check.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.yktx.broadcast.CallAlarm;
import com.yktx.check.bean.AlarmBean;
import com.yktx.check.bean.MoreAlertTimeBean;

public class CallAlarmUtil {

	/**
	 * @param activity
	 *            intent activity
	 * @param alertTime
	 *            11:20 格式
	 * @param dayTime
	 *            间隔时间
	 * @param alarmIndex
	 *            闹铃下标
	 */
	public static void setAlarm(Activity activity, AlarmBean bean) {
		Tools.getLog(Tools.d, "aaa", bean.toString());
		String alertTime = bean.getAlarmTime();
		if (alertTime == null || alertTime.equals("-1"))
			return;

		Tools.getLog(Tools.i, "alert", "alertTime =============== " + bean.getTastTitle());
		String arrayTime[] = alertTime.split(":");
		Tools.getLog(Tools.i, "bbb", "alertTime==== 数组 =============== " + arrayTime[0]+"=="+arrayTime[1]);
		int hour = Integer.parseInt(arrayTime[0]);
		int minute = Integer.parseInt(arrayTime[1]);
		Calendar canlendar = Calendar.getInstance();
		Tools.getLog(Tools.i, "alert", "currentTimeMillis"+System.currentTimeMillis());
		canlendar.setTimeInMillis(System.currentTimeMillis());
		canlendar.set(Calendar.HOUR_OF_DAY, hour);
		canlendar.set(Calendar.MINUTE, minute);
		canlendar.set(Calendar.SECOND, 0);
		canlendar.set(Calendar.MILLISECOND, 0);

		if (System.currentTimeMillis() > canlendar.getTimeInMillis()) {
			canlendar.set(Calendar.DAY_OF_YEAR, canlendar.get(Calendar.DAY_OF_YEAR) + 1);
		}
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		String dateStr = sdf.format(canlendar.getTime()); 
//		Tools.getLog(Tools.i, "alert", "时间" + dateStr);
		Intent intent = new Intent(activity, CallAlarm.class);
		intent.putExtra("TastTitle", bean.getTastTitle());
		
		PendingIntent sender = PendingIntent.getBroadcast(activity, bean.getAlarmIndex(),
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		/*
		 * setRepeating() 可让闹钟重复执行
		 */
		Tools.getLog(Tools.i, "alert", "canlendar.getTimeInMillis()"+canlendar.getTimeInMillis());
		AlarmManager am;
		am = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
		
		am.setRepeating(AlarmManager.RTC_WAKEUP, canlendar.getTimeInMillis(),
				AlarmManager.INTERVAL_DAY, sender);
		
		Tools.getLog(Tools.i, "bbb",
				"canlendar.getTimeInMillis(), ============ "
						+ canlendar.getTimeInMillis());

	}

	public static final String SP_STR = "TASK_CALL_ALARM";
	public static final String MAX_INDEX = "MAX_INDEX";

	private static final String alarmName = "alarmName";
	private static final String alarmIndex = "alarmIndex";

	/**
	 * 创建hashMap
	 * 
	 * @param mContext
	 * @param list
	 */
	public static ArrayList<AlarmBean> createTaskHashMap(Activity mContext,
			ArrayList<MoreAlertTimeBean> list) {
		ArrayList<AlarmBean> alarmList = new ArrayList<AlarmBean>();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getVibrationFlag().equals("0")&&list.get(i).getStatus().equals("1")){
				AlarmBean bean = new AlarmBean();
				bean.setAlarmIndex(i);
				bean.setTastID(list.get(i).getTaskId());
				bean.setAlarmTime(list.get(i).getTime());
				bean.setTastTitle(list.get(i).getTitle());
				alarmList.add(bean);
				setAlarm(mContext, bean);
			}else{
				Tools.getLog(Tools.d, "alert", "暂停的=+++=打卡："+list.get(i).getTime()+list.get(i).getTitle());
			}
		}
		return alarmList;
	}

	/**
	 * 返回用户设置task闹钟
	 * 
	 * @return
	 */
	public static HashMap<String, Integer> getTaskCallAlarm(Context mContext) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		SharedPreferences settings = mContext.getSharedPreferences("clock",
				Context.MODE_PRIVATE);
		String mapStr = settings.getString(SP_STR, null);
		if (mapStr == null) {
			return null;
		} else {
			try {
				JSONArray message = new JSONArray(mapStr);
				for (int i = 0; i < message.length(); i++) {
					JSONObject data_item = message.getJSONObject(i);
					if (data_item.has(alarmName) && data_item.has(alarmIndex))
						map.put(data_item.getString(alarmName),
								data_item.getInt(alarmIndex));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}

	public static void saveTAskCallAlarm(Context mContext,
			HashMap<String, Integer> map) {
		if (map == null && map.size() == 0)
			return;
		SharedPreferences settings = mContext.getSharedPreferences("clock",
				Context.MODE_PRIVATE);
		Gson gson = new Gson();
		String json = gson.toJson(map);
		Editor mEditor = settings.edit();
		mEditor.putString(SP_STR, json);
		mEditor.commit();
	}

	public static int getMaxIndex(Context mContext) {
		SharedPreferences settings = mContext.getSharedPreferences("clock",
				Context.MODE_PRIVATE);
		return settings.getInt(MAX_INDEX, 0);
	}

	public static void saveMaxIndex(Context mContext, int maxIndex) {
		SharedPreferences settings = mContext.getSharedPreferences("clock",
				Context.MODE_PRIVATE);
		Editor mEditor = settings.edit();
		mEditor.putInt(MAX_INDEX, maxIndex);
		mEditor.commit();
	}

	public static void closeAlarm(Activity activity, int index) {
		Intent intent = new Intent(activity, CallAlarm.class);
		PendingIntent sender = PendingIntent.getBroadcast(activity, index,
				intent, 0); /* 由AlarmManager中移除 */
		AlarmManager am;
		am = (AlarmManager) activity.getSystemService(activity.ALARM_SERVICE);
		am.cancel(sender); /* 以Toast提示已删除设定，并更新显示的闹钟时间 */
	}

	public static void closeAllAlarm(Activity activity, int size) {
		for (int i = 0; i < size; i++) {
			closeAlarm(activity, i);
		}
	}

}
