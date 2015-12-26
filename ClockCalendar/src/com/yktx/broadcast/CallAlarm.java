package com.yktx.broadcast; /* import相关class */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.clock.service.StatusService;
import com.yktx.check.util.Tools;
/* 调用闹钟Alert的Receiver */

public class CallAlarm extends BroadcastReceiver
{
  @Override
  public void onReceive(Context context, Intent intent)
  {
	  /* create Intent，调用AlarmAlert.class */
	  String title = intent.getStringExtra("TastTitle");
	  Tools.getLog(Tools.i, "alarmtime", "title ============ "+title);
	  Intent in=new Intent(context,StatusService.class);
	  if(title != null)
		  in.putExtra("TastTitle", title);
	  context.startService(in);  

  }
}