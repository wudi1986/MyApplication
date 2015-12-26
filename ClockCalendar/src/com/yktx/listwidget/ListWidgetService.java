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

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.yktx.check.R;
import com.yktx.check.bean.ByDateBean;
import com.yktx.check.util.Tools;
import com.yktx.sqlite.DBHelper;

@SuppressLint("NewApi")
public class ListWidgetService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {

		Tools.getLog(Tools.d, "kkk", "onGetViewFactory");
		return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
	}
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
	private List<ByDateBean> mWidgetItems = new ArrayList<ByDateBean>();
	private Context mContext;

	public ListRemoteViewsFactory(Context context, Intent intent) {
		mContext = context;
		DBHelper dbHelper = new DBHelper(mContext);
		mWidgetItems = dbHelper.getTaskList();

		Tools.getLog(Tools.d, "kkk", "mWidgetItems ============= "
				+ mWidgetItems.size());
	}

	public void onCreate() {
		Tools.getLog(Tools.d, "kkk", "onCreate");
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
    	
    	
        // We sleep for 3 seconds here to show how the empty view appears in the interim.
        // The empty view is set in the StackWidgetProvider and should be a sibling of the
        // collection view.
    }

	public void onDestroy() {
		Tools.getLog(Tools.d, "kkk", "onDestroy");
		// In onDestroy() you should tear down anything that was setup for your
		// data source,
		// eg. cursors, connections, etc.
		mWidgetItems.clear();
	}

	public int getCount() {

		Tools.getLog(Tools.d, "kkk", "getCount.mWidgetItems.size() ========== "
				+ mWidgetItems.size());
		return mWidgetItems.size();
	}

	public RemoteViews getViewAt(int position) {
		// position will always range from 0 to getCount() - 1.
		Tools.getLog(Tools.d, "kkk", "getViewAtgetViewAtgetViewAt");
		ByDateBean bean = mWidgetItems.get(position);
		// We construct a remote views item based on our widget item xml file,
		// and set the
		// text based on the position.
		RemoteViews rv = new RemoteViews(mContext.getPackageName(),
				R.layout.widget_item);
		rv.setTextViewText(R.id.num, bean.getCurrentStreak() + "");
		if (bean.getPrivateFlag() == 1) {
			rv.setImageViewResource(R.id.yinsiImage, R.drawable.home_task_yinsi);
		} else {
			rv.setImageViewResource(R.id.yinsiImage, R.drawable.toumingimg);
		}
		if (bean.getJobCount() > 0) {
			rv.setImageViewResource(R.id.taskSign, R.drawable.home_task_yida);
			rv.setTextColor(R.id.title,
					mContext.getResources().getColor(R.color.meibao_color_10));
			rv.setTextColor(R.id.num,
					mContext.getResources().getColor(R.color.white));
		} else {
			if (bean.getCurrentStreak() < 0) {
				rv.setImageViewResource(R.id.taskSign,
						R.drawable.home_task_weida3);
				rv.setTextColor(R.id.num,
						mContext.getResources().getColor(R.color.white));
			} else {

				rv.setImageViewResource(R.id.taskSign,
						R.drawable.home_task_weida);
				rv.setTextColor(R.id.num,
						mContext.getResources()
								.getColor(R.color.meibao_color_9));
			}
			rv.setTextColor(R.id.title,
					mContext.getResources().getColor(R.color.meibao_color_9));

		}

		String text = bean.getTitle();
		rv.setTextViewText(R.id.title, text);

		// Next, we set a fill-intent which will be used to fill-in the pending
		// intent template
		// which is set on the collection view in StackWidgetProvider.
		Bundle extras = new Bundle();
		extras.putInt(ListWidgetProvider.EXTRA_ITEM, position);
		extras.putString(ListWidgetProvider.TOAST_ACTION, bean.getTaskId());
		Intent fillInIntent = new Intent();
		fillInIntent.putExtras(extras);
		rv.setOnClickFillInIntent(R.id.title, fillInIntent);
		
		return rv;
	}

	public RemoteViews getLoadingView() {
		Tools.getLog(Tools.d, "kkk", " getLoadingView " );
		// You can create a custom loading view (for instance when getViewAt()
		// is slow.) If you
		// return null here, you will get the default loading view.
		return null;
	}

	public int getViewTypeCount() {
		Tools.getLog(Tools.d, "kkk", " getViewTypeCount " );
		return 1;
	}

	public long getItemId(int position) {
		Tools.getLog(Tools.d, "kkk", " getItemId " +position);
		return position;
	}

	public boolean hasStableIds() {
		Tools.getLog(Tools.d, "kkk", " hasStableIds " );
		return true;
	}
	
	public void onDataSetChanged() {
		Tools.getLog(Tools.d, "kkk", " onDataSetChanged " );
		DBHelper dbHelper = new DBHelper(mContext);
		mWidgetItems = dbHelper.getTaskList();

	}

}
