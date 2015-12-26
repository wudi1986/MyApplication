package com.clock.service;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.yktx.check.ClockDynamicActivity;
import com.yktx.check.ClockFansActivity;
import com.yktx.check.ClockMainActivity;
import com.yktx.check.R;
import com.yktx.check.bean.Group;
import com.yktx.check.square.fragment.DynamicFragment;
import com.yktx.check.square.fragment.GroupMainFragmentActivity;
import com.yktx.check.util.Tools;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "aaa";
	private HashMap<String, Integer> hashmap;
	private int statusCode = 0;

	public static final int TO_COMMENT = 1;
	public static final int TO_ZAN = 2;
	public static final int TO_FANS = 3;
	public static final int TO_MAIN = 4;
	public static final int TO_DY = 5;

	String getTopActivity(Context context2) {
		if (context2 != null) {
			ActivityManager manager = (ActivityManager) context2
					.getSystemService(Activity.ACTIVITY_SERVICE);
			List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

			if (runningTaskInfos != null)
				return (runningTaskInfos.get(0).topActivity).toString();
			else
				return null;
		} else
			return null;
	}

	static boolean istalk;
	Context context;
	String productid;
	String applyid;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		SharedPreferences setting = context.getSharedPreferences("clock",
				context.MODE_PRIVATE);
		Editor mEditor = setting.edit();
		hashmap = new HashMap<String, Integer>();
		Bundle bundle = intent.getExtras();
		Tools.getLog(Tools.i, TAG,
				"onReceive ============= " + intent.getAction()
				+ ",          extras:   " + printBundle(bundle));
		Tools.getLog(Tools.i, TAG, "bundle ============= " + bundle.toString());

		String extras = printBundle(bundle);
		JSONObject extrasJson;
		String jobId = "", taskId = "", type = "", userId = "";
		if (extras != null) {
			try {
				extrasJson = new JSONObject(extras);
				if (extrasJson.has("jobId"))
					jobId = extrasJson.getString("jobId");
				if (extrasJson.has("taskId"))
					taskId = extrasJson.getString("taskId");
				if (extrasJson.has("type"))
					type = extrasJson.getString("type");
				if (extrasJson.has("userId"))
					userId = extrasJson.getString("userId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// if ( type.equals("1")) {
		if (ClockMainActivity.xiaoxi_button != null) {
			ClockMainActivity.xiaoxi_button
			.setImageResource(R.drawable.home_hongdian);
			Tools.getLog(Tools.i, "aaa",
					"ClockMainActivity.square_calendar_button != null");
		}
		if (setting.getBoolean("isVisibleToUser", false)) {
			thisDynamic.setRefresh();
		} else {
			mEditor.putBoolean("isred", true);
			mEditor.commit();
		}
		// }

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Tools.getLog(Tools.i, TAG, "接收Registration Id : " + regId);
			// send the Registration Id to your server...
		} else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Tools.getLog(Tools.i, TAG, "接收UnRegistration Id : " + regId);
			// send the UnRegistration Id to your server...
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Tools.getLog(
					Tools.i,
					TAG,
					"接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Tools.getLog(Tools.i, TAG, "接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Tools.getLog(Tools.i, TAG, "接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Tools.getLog(Tools.i, TAG, "用户点击打开了通知");

			// 判断当前软件是否正在运行
			String PackageName = getAppPackageName(context);
			Tools.getLog(Tools.i, "aaa", "PackageName == " + PackageName);
			boolean IsRun = isServiceStarted(context, PackageName);
			//			System.out.println("PackageName:IsRun -> " + PackageName
			//					+ "  - >  " + IsRun);

			Tools.getLog(Tools.i, "aaa", "istalk == " + istalk);
			// if (istalk) {
			// Intent in = new Intent(context, CallActivity.class);
			// in.putExtra("state", 0);
			// in.putExtra("userID", userid + "");
			// in.putExtra("productID", productid);
			// in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(in);
			// } else {
			// // /


			if (IsRun) {
				//				if (type.equals("1") || type.equals("4")) { // 评论
				//					// 当前打开的activity
				//					if (getTopActivity(context).indexOf("ClockCommentActivity") != -1
				//							&& ClockCommentActivity.clockCommentActivity.jobid.equals(jobId)) {
				//						ClockCommentActivity.clockCommentActivity.setRefresh();
				//					} else {
				//						Intent i = new Intent(context,
				//								ClockCommentActivity.class);
				//						i.putExtra("jobid", jobId);
				//						i.putExtra("createUserID", userId);
				//						i.putExtra("taskId", taskId);
				//						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//						context.startActivity(i);
				//					}
				//				} else if (type.equals("2")) {//打气
				//
				//					if (getTopActivity(context).indexOf("ClockVoteActivity") != -1
				//							&& ClockVoteActivity.clockVoteActivity.jobid.equals(jobId)) {
				//						ClockVoteActivity.clockVoteActivity.setRefresh();
				//					} else {
				//						Intent i = new Intent(context, ClockVoteActivity.class);
				//						i.putExtra("jobid", jobId);
				//						i.putExtra("createUserID", userId);
				//						i.putExtra("taskId", taskId);
				//						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//						context.startActivity(i);
				//					}
				//				} else if(type.equals("3")){		//关注
				//					Intent in = new Intent(context, ClockFansActivity.class);
				//					in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//					context.startActivity(in);
				//				} else if(type.equals("5")){		//今日精品
				//					Intent in = new Intent(context, ClockDynamicActivity.class);
				//					in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//					context.startActivity(in);
				//				}else if(type.equals("6")){//被别人提醒去打卡
				//
				//					Intent in = new Intent(context, ClockMainActivity.class);
				//					in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//					context.startActivity(in);
				//
				//				}else if(type.equals("7")){//别人接受了你的提醒
				//
				//					Intent in = new Intent(context, ClockDynamicActivity.class);
				//					in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//					context.startActivity(in);
				//
				//				}
				if (type.equals("")) { // 个人中心
					Intent in = new Intent(context, ClockMainActivity.class);
					in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(in);
				}else{
					Intent in = new Intent(context, ClockDynamicActivity.class);
					in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(in);
				}
			} else {

				Intent i = new Intent(context, ClockMainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				if (type.equals("")) {
//					i.putExtra("goto", TO_FANS);
//				} else if(type.equals("2")){
//					i.putExtra("goto", TO_ZAN);
//				} else if(type.equals("1") || type.equals("4")){
//					i.putExtra("goto", TO_COMMENT);
//				} else if(type.equals("5") || type.equals("7")){
					i.putExtra("goto", TO_MAIN);		
				}else{
					i.putExtra("goto", TO_DY);	
					
				}
				i.putExtra("isPush", true);
				context.startActivity(i);
			}

			// }
			// switch (type) {
			// case 2: // 发布商品
			// Tools.getLog(Tools.i,"aaa", "2222222222222");
			// // Intent i = new Intent(context, ProductActivity.class);
			// // i.putExtra("productid", productid);
			// // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// // // i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// // context.startActivity(i);
			// break;
			// case 3: // 订单
			// Tools.getLog(Tools.i,"aaa", "33333333333");
			// break;
			// case 4: // 双方确认
			// Tools.getLog(Tools.i,"aaa", "444444444444");
			// break;
			// default:
			//
			// break;
			// }

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Tools.getLog(
					Tools.i,
					TAG,
					"用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else {
			Tools.getLog(Tools.i, TAG,
					"Unhandled intent - " + intent.getAction());
		}
	}

	static onRefreshDynamic thisDynamic;

	public static void setDynamic(onRefreshDynamic dynamic) {
		thisDynamic = dynamic;
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_EXTRA)) {

				sb.append(bundle.getString(key));
				return sb.toString();
			}
		}

		return null;
	}

	// android 检测一个android程序是否在运行.
	public static boolean isServiceStarted(Context context, String PackageName) {
		boolean isStarted = false;
		try {

			// ActivityManager mActivityManager = (ActivityManager)
			// context.getSystemService(Context.ACTIVITY_SERVICE);
			// List<RunningAppProcessInfo> run =
			// mActivityManager.getRunningAppProcesses();
			// for(ActivityManager.RunningAppProcessInfo pro: run){
			// if(pro.processName.equals(PackageName)){
			// isStarted = true;
			// break;
			// }
			// }

			ActivityManager mActivityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> run = mActivityManager.getRunningTasks(1);
			if (run.size() > 0) {
				if (PackageName.equals(run.get(0).topActivity.getPackageName())) {
					isStarted = true;
				}
			}

		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return isStarted;
	}

	// 获取程序自身包名
	public static String getAppPackageName(Context context) {
		try {
			String pkName = context.getPackageName();
			return pkName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		Intent msgIntent = new Intent(
				"com.example.jpushdemo.MESSAGE_RECEIVED_ACTION");
		msgIntent.putExtra("message", message);
		if (extras != null) {
			try {
				JSONObject extraJson = new JSONObject(extras);
				if (null != extraJson && extraJson.length() > 0) {
					msgIntent.putExtra("extras", extras);
				}
			} catch (JSONException e) {
			}
		}
		context.sendBroadcast(msgIntent);
	}

	public static interface onRefreshDynamic {
		public void setRefresh();
	}

}
