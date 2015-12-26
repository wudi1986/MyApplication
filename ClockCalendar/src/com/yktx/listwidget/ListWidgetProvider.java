/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yktx.listwidget;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yktx.check.ClockApplication;
import com.yktx.check.R;
import com.yktx.check.R.id;
import com.yktx.check.R.layout;
import com.yktx.check.bean.ByDateBean;
import com.yktx.check.bean.CreateUserBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;
import com.yktx.sqlite.DBHelper;

@SuppressLint("NewApi")
public class ListWidgetProvider extends AppWidgetProvider implements
		ServiceListener {
	public static final String TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION";
	public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";
	static CreateUserBean createUserBean;
	RemoteViews rv;
	String userID;
	private Context mContext;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		mContext = context;
	}

	AppWidgetManager mgr;

	@Override
	public void onReceive(Context context, Intent intent) {
		Tools.getLog(Tools.i,"kkk", "onReceive");
		mgr = AppWidgetManager.getInstance(context);
		mContext = context;
		if (intent.getAction().equals(TOAST_ACTION)) {
			int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
			String taskID = intent.getStringExtra(TOAST_ACTION);
			CreateJobConn(viewIndex, taskID);
			
		} else if(intent.getAction().equals("com.android.REFLASH")) {
			getUserConn();
		}
		super.onReceive(context, intent);
	}

	private void CreateJobConn(int position, String taskID) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			String jobid = Tools.getUuid();
			params.add(new BasicNameValuePair("jobId", jobid));
			params.add(new BasicNameValuePair("taskId", taskID));
			params.add(new BasicNameValuePair("unit", "0"));

			params.add(new BasicNameValuePair("giveUpFlag", "0"));
			// }

			long checkTime = System.currentTimeMillis();
			params.add(new BasicNameValuePair("checkTime", checkTime + ""));
			params.add(new BasicNameValuePair("onlineFlag", "1"));
			params.add(new BasicNameValuePair("picNum", "0"));
			params.add(new BasicNameValuePair("clientLocalTime", checkTime + ""));
			params.add(new BasicNameValuePair("currentGiveUpFlag", "0"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_CREATEJOB, null, null,
				ListWidgetProvider.this).addList(params)
				.request(UrlParams.POST);
	}

	public void getUserConn() {
		SharedPreferences settings = ClockApplication.getInstance()
				.getSharedPreferences("clock", Context.MODE_PRIVATE);
		userID = settings.getString("userid", null);
		Tools.getLog(Tools.d, "kkk", "userID ========== " + userID);
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(userID);
		sb.append("&curUserId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_GETBYIDUSER, null, sb.toString(), this)
				.addList(null).request(UrlParams.GET);

	}

	public void ClickDataConn() {
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(userID);
		sb.append("&date=");
		sb.append(TimeUtil.getYYMMDD(System.currentTimeMillis()));
		Service.getService(Contanst.HTTP_GETBYDATE, null, sb.toString(),
				ListWidgetProvider.this).addList(null).request(UrlParams.GET);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// update each of the widgets with the remote adapter
		mContext = context;
		Tools.getLog(Tools.d, "kkk", "appWidgetIds.length ======= "
				+ appWidgetIds.length);
		getUserConn();
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void getJOSNdataSuccess(Object bean, String sccmsg, int connType) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_OK;
		msg.obj = bean;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}

	@Override
	public void getJOSNdataFail(String errcode, String errmsg, int connType) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_FAIL;
		msg.obj = errmsg;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}

	private Handler mHandler = new Handler() {
		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.GETBYIDUSER:
					Tools.getLog(Tools.i, "kkk",
							"GETBYIDUSER       BEST_INFO_OK");
					createUserBean = (CreateUserBean) msg.obj;
					ClickDataConn();
					break;
				case Contanst.GETBYDATE:
					ArrayList<ByDateBean> byDateBeanList = (ArrayList<ByDateBean>) msg.obj;
					DBHelper dbHelper = new DBHelper(mContext);
					dbHelper.insertTaskList(byDateBeanList);
					Intent in = new Intent(mContext, MyService.class);
					mContext.startService(in);
					break;
				case Contanst.CREATEJOB:
					Tools.getLog(Tools.i, "kkk", "BEST_INFO_OKCREATEJOB");
					Toast.makeText(
							ClockApplication.getInstance()
									.getApplicationContext(), "打卡成功",
							Toast.LENGTH_SHORT).show();
					getUserConn();

					break;
				}

				break;

			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				switch (msg.arg1) {

				case Contanst.GETBYIDUSER:

					break;

				}
				break;
			}
		}
	};

	/**
  	 * 
  	 */

	public static class MyService extends android.app.Service {
		@Override
		public void onStart(Intent intent, int startId) {
			Tools.getLog(Tools.i, "kkk", "MyService          ");

			final ComponentName thisWidget = new ComponentName(this,
					ListWidgetProvider.class);
			final AppWidgetManager manager = AppWidgetManager.getInstance(this);

			Intent intent1 = new Intent(this, ListWidgetService.class);
			intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, thisWidget);
			// When intents are compared, the extras are ignored, so we need to
			// embed the extras
			// into the data so that the extras will not be ignored.
			intent1.setData(Uri.parse(intent1.toUri(Intent.URI_INTENT_SCHEME)));
			final RemoteViews rv = new RemoteViews(getPackageName(),
					R.layout.widget_layout);

			rv.setRemoteAdapter(R.id.listview, intent1);

			// The empty view is displayed when the collection has no items. It
			// should be a sibling
			// of the collection view.
			rv.setEmptyView(R.id.listview, R.id.empty_view);// @1

			// Here we setup the a pending intent template. Individuals items of
			// a collection
			// cannot setup their own pending intents, instead, the collection
			// as a whole can
			// setup a pending intent template, and the individual items can set
			// a fillInIntent
			// to create unique before on an item to item basis.
			Intent toastIntent = new Intent(MyService.this,
					ListWidgetProvider.class);
			toastIntent.setAction(ListWidgetProvider.TOAST_ACTION);
			toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					thisWidget);// @2
			intent1.setData(Uri.parse(intent1.toUri(Intent.URI_INTENT_SCHEME)));
			PendingIntent toastPendingIntent = PendingIntent.getBroadcast(
					MyService.this, 0, toastIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setPendingIntentTemplate(R.id.listview, toastPendingIntent);

			if (createUserBean != null) {
				rv.setTextViewText(R.id.userName, createUserBean.getName());
				rv.setTextViewText(R.id.qiqiuNum, createUserBean.getPoint());
				ImageLoader.getInstance().loadImage(
						Tools.getImagePath(createUserBean.getImageSource())
								+ createUserBean.getAvartarPath(),
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								// TODO Auto-generated method stub
								Tools.getLog(Tools.i, "kkk",
										"createUserBean != null  onLoadingComplete");
								rv.setImageViewBitmap(R.id.userHead,
										loadedImage);
								manager.updateAppWidget(thisWidget, rv);
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
								// TODO Auto-generated method stub

							}
						});
			} else {
				Tools.getLog(Tools.i, "kkk",
						"createUserBean========== null  onLoadingComplete");
				manager.updateAppWidget(thisWidget, rv);
			}
			update(this, rv, manager);
		}

		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}

		private void update(Context context, RemoteViews rv,
				AppWidgetManager manager) {
			int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(
					context, ListWidgetProvider.class));
			for (int i = 0; i < appWidgetIds.length; i++) {
				manager.updateAppWidget(appWidgetIds[i], rv); // 更新 实例
				manager.notifyAppWidgetViewDataChanged(appWidgetIds[i],
						R.id.listview);
			}
		}

	};

}