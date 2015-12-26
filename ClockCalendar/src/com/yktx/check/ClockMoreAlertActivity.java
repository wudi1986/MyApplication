package com.yktx.check;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yktx.check.adapter.ClockMoreAlertAdapter;
import com.yktx.check.bean.MoreAlertTimeBean;
import com.yktx.check.util.Tools;

public class ClockMoreAlertActivity extends BaseActivity {
	private ListView mListView;
	private ImageView mLeftImage, mRightImage;
	private TextView mTitleContent;
	private ArrayList<MoreAlertTimeBean> alertTimeBeans = new ArrayList<MoreAlertTimeBean>();
	private ClockMoreAlertAdapter adapter;
	private String taskid;
	private RelativeLayout mTitleLayout;

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_clock_more_alert);
		mListView = (ListView) findViewById(R.id.clock_more_alert_listview);
		mLeftImage = (ImageView) findViewById(R.id.title_item_leftImage);
		mRightImage = (ImageView) findViewById(R.id.title_item_rightImage);
		mTitleContent = (TextView) findViewById(R.id.title_item_content);
		mTitleLayout = (RelativeLayout) findViewById(R.id.title_item_layout);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		mTitleLayout.setBackgroundColor(getResources().getColor(R.color.white));
		taskid = getIntent().getStringExtra("taskid");
		alertTimeBeans = (ArrayList<MoreAlertTimeBean>) getIntent().getSerializableExtra("onlist");
		Tools.getLog(Tools.d, "aaa", "taskid=======" + taskid);
		
		Tools.getLog(Tools.d, "aaa", "开始=======数据库存入的数据" + alertTimeBeans.toString());
		if(alertTimeBeans == null){
			alertTimeBeans = dbHelper.getAlarmList(taskid);
		}
		if(alertTimeBeans.size() == 0){
			MoreAlertTimeBean alertTimeBean = new MoreAlertTimeBean();
			alertTimeBean.setId(Tools.getUuid());
			alertTimeBean.setTaskId(taskid);
			alertTimeBean.setTime("00:00");
			alertTimeBeans.add(alertTimeBean);
		}
		
		mTitleContent.setText("提醒");
		mRightImage.setImageResource(R.drawable.clock_new_building_add);
		
		adapter = new ClockMoreAlertAdapter(mContext);
		// adapter.setListListener(listListener);
		mListView.setAdapter(adapter);
		adapter.setList(alertTimeBeans);

	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		mRightImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				alertTimeBeans = adapter.getAlertTimeBeans();
				MoreAlertTimeBean alertTimeBean = new MoreAlertTimeBean();
				alertTimeBean.setId(Tools.getUuid());
				alertTimeBean.setTaskId(taskid);
				alertTimeBean.setTime("00:00");
				alertTimeBeans.add(alertTimeBean);
				Tools.getLog(Tools.d, "aaa", "" + alertTimeBeans.toString());
				adapter.setList(alertTimeBeans);
				adapter.notifyDataSetChanged();
			}
		});
		mLeftImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// finish();
				alertTimeBeans = adapter.getAlertTimeBeans();
//				Tools.getLog(Tools.d, "aaa", "" + alertTimeBeans.toString());
//				dbHelper.addAlarmList(alertTimeBeans);
//				alertTimeBeans = dbHelper.getAlarmList(taskid);
				Tools.getLog(Tools.d, "aaa", "数据库存入的数据" + alertTimeBeans.toString());
				Intent in = getIntent();
				in.putExtra("list", alertTimeBeans);
				setResult(RESULT_OK, in);
				finish();
				
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			alertTimeBeans = adapter.getAlertTimeBeans();
//			Tools.getLog(Tools.d, "aaa", "" + alertTimeBeans.toString());
//			dbHelper.addAlarmList(alertTimeBeans);
//			alertTimeBeans = dbHelper.getAlarmList(taskid);
			Tools.getLog(Tools.d, "aaa", "数据库存入的数据" + alertTimeBeans.toString());
			Intent in = getIntent();
			in.putExtra("list", alertTimeBeans);
			setResult(RESULT_OK, in);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
		MobclickAgent.onPause(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this);
	}
}
