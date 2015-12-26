package com.clock.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

public class NetCheckReceiver extends BroadcastReceiver {

	// android 中网络变化时所发的Intent的名字
	private static final String netACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(netACTION)) {
			// Intent中ConnectivityManager.EXTRA_NO_CONNECTIVITY这个关键字表示着当前是否连接上了网络
			// true 代表网络断开 false 代表网络没有断开
			Tools.getLog(Tools.d,"kkk", "netACTION ======== "+intent.getAction());
			if (intent.getBooleanExtra(
					ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
				Contanst.isConnStop = true;
			} else {
				Contanst.isConnStop = false;
				Intent in = new Intent(context, ConnFailJobService.class);
				context.startService(in);
			}
		}
	}
}
