package com.clock.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.umeng.analytics.MobclickAgent;
import com.yktx.check.MainActivity;
import com.yktx.check.R;

public class StatusService extends IntentService
{

	private static final String TAG = "StatusService";

	// private static final int KUKA = 0;

	public StatusService()
	{
		super("StatusService");

	}

	@Override
	protected void onHandleIntent(Intent intent)
	{

		  String title = intent.getStringExtra("TastTitle");
		showNotification(title);//开始下载
		
	}

	// finish下载是否完成
	private void showNotification(String title)
	{
		Notification notification;
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		MobclickAgent.onEvent(this, "showalarmnotification");
		Intent intent = new Intent(this, MainActivity.class);//下拉点击执行的activity
		intent.putExtra("isalarm", true);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification = new Notification(R.drawable.icon, "#"+title+"#需要打卡了，别懒惰呦", System.currentTimeMillis());
			notification.setLatestEventInfo(this, "打卡7", "#"+title+"#需要打卡了，别懒惰呦", contentIntent);
			//#打卡标题#需要打卡了，别懒惰呦
		//下载的默认声音    //notification被notify的时候，触发默认声音和默认震动
		notification.defaults= Notification.DEFAULT_SOUND;
		//点击notification之后，该notification自动消失 
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		//声音
		notification.defaults=Notification.DEFAULT_SOUND;
		
		//r.layout.main是为manager指定一个唯一的id
		manager.notify(R.layout.listview_item, notification);//将自定义的notification放入NotificationManager
		
	}
}