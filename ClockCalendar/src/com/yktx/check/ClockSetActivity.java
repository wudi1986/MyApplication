package com.yktx.check;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.yktx.check.bean.AlarmBean;
import com.yktx.check.bean.ByIdDetailBean;
import com.yktx.check.bean.MoreAlertTimeBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.service.Service;
import com.yktx.check.util.CallAlarmUtil;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.MyUMSDK;
import com.yktx.check.util.Tools;

public class ClockSetActivity extends BaseActivity implements ServiceListener {
	Calendar canlendar = Calendar.getInstance();
	private ImageView mTitleBack, mTitleRightImage;
	private EditText mNewState;
	private TextView mTitleContent, mNewText;
	private TextView mTitleSuccess;
	private boolean isStart;
	private int conTime;
	private int TIMECODE = 123;
	private int LIMITCODE = 124;
	private ByIdDetailBean byIdDetailBean;
	private String mTaskId;
	// private int[] arrayColor = { R.color.clock_new_image1,
	// R.color.clock_new_image2, R.color.clock_new_image3,
	// R.color.clock_new_image4, R.color.clock_new_image5,
	// R.color.clock_new_image6, R.color.clock_new_image7 };
	private boolean isTimeLimit;
	private int colorNum;
	private String isStatus,oldIsStatus;// 0为暂停打卡 1为不暂停
	private LinearLayout clock_set_limitTimeLayout, clock_set_alertTimeLayout;
	private TextView 
	clock_set_alertTimeText,
	clock_set_limitTimeText,
	clock_set_inputStateText, clock_set_text,clock_set_isVisibleText;
	private ImageView clock_set_alertTimeImage, clock_set_limitTimeImage,
	clock_set_isVisibleImage, clock_set_weibo, clock_set_StopSwitch;
	private boolean isVisity, isAutoShare = true;
	String beginTime, endTime, alertTime;// 开始，结束时间
	private RelativeLayout mTitleLayout, clock_set_deleteLayout;

	//	DBHelper dbHelpers;
	int oldNum;
	MyUMSDK mShareSDK;

	boolean isSuccess;//判断完成不可重复点击

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_clock_set);

		mTitleBack = (ImageView) findViewById(R.id.title_item_leftImage);
		mTitleContent = (TextView) findViewById(R.id.title_item_content);
		mTitleRightImage = (ImageView) findViewById(R.id.title_item_rightImage);
		mTitleSuccess = (TextView) findViewById(R.id.title_item_rightText);
		mTitleLayout = (RelativeLayout) findViewById(R.id.title_item_layout);

		mNewText = (TextView) findViewById(R.id.clock_set_inputClock);
		mNewState = (EditText) findViewById(R.id.clock_set_inputState);
		// mOthervisible = (SlideSwitch)
		// findViewById(R.id.clock_set_othervisiblebtn);
		// clock_set_stop = (TextView) findViewById(R.id.clock_set_success);
		// 新的
		clock_set_limitTimeLayout = (LinearLayout) findViewById(R.id.clock_set_limitTimeLayout);
		clock_set_alertTimeLayout = (LinearLayout) findViewById(R.id.clock_set_alertTimeLayout);
		clock_set_alertTimeText = (TextView) findViewById(R.id.clock_set_alertTimeText);
		clock_set_limitTimeText = (TextView) findViewById(R.id.clock_set_limitTimeText);
		clock_set_alertTimeImage = (ImageView) findViewById(R.id.clock_set_alertTimeImage);
		clock_set_limitTimeImage = (ImageView) findViewById(R.id.clock_set_limitTimeImage);
		clock_set_isVisibleImage = (ImageView) findViewById(R.id.clock_set_isVisibleImage);
		clock_set_weibo = (ImageView) findViewById(R.id.clock_set_weiboSwitch);
		clock_set_StopSwitch = (ImageView) findViewById(R.id.clock_set_StopSwitch);
		clock_set_deleteLayout = (RelativeLayout) findViewById(R.id.clock_set_deleteLayout);

		clock_set_inputStateText = (TextView) findViewById(R.id.clock_set_inputStateText);
		clock_set_text = (TextView) findViewById(R.id.clock_set_text);
		clock_set_isVisibleText = (TextView) findViewById(R.id.clock_set_isVisibleText);
		//		dbHelpers = new DBHelper(mContext);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		mShareSDK = new MyUMSDK(mContext);

		GetAllAlarmList();


		mTitleLayout.setBackgroundColor(getResources().getColor(
				R.color.meibao_color_15));
		mTitleContent.setText("打卡设置");
		mTitleRightImage.setVisibility(View.GONE);
		mTitleSuccess.setVisibility(View.VISIBLE);
		mTitleSuccess.setText("保存");
		mTitleSuccess.setTextColor(getResources().getColor(
				R.color.meibao_color_1));
		byIdDetailBean = (ByIdDetailBean) getIntent().getSerializableExtra(
				"byid");
		mTaskId = getIntent().getStringExtra("taskid");
		if (byIdDetailBean != null) {
			initView(byIdDetailBean);
		} else {

			getByIdConn();
		}
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		mNewState.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			int editStart;
			int editEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				editStart = mNewState.getSelectionStart();
				editEnd = mNewState.getSelectionEnd();
				int infoNum = temp.length();
				if (infoNum > 200) {
					Toast.makeText(mContext, "你输入的字数已经超过了限制！",
							Toast.LENGTH_SHORT).show();
					s.delete(editStart - 1, editEnd);

					temp = s;
					infoNum = temp.length();
					mNewState.setText(temp);
					mNewState.setSelection(infoNum);
				}
				clock_set_inputStateText.setText(temp.length() + "/200");
			}
		});
		clock_set_limitTimeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, ClockLimitActivity.class);
				String time = clock_set_limitTimeText.getText().toString();
				if (time != null) {
					in.putExtra("limittime", time);
				}
				startActivityForResult(in, LIMITCODE);
			}
		});
		clock_set_alertTimeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, ClockMoreAlertActivity.class);
				// if (time != null) {
				// in.putExtra("ischeck", time);
				// }
				in.putExtra("taskid", mTaskId);
				in.putExtra("onlist", alertTimeBeans);
				startActivityForResult(in, TIMECODE);

			}
		});
		clock_set_isVisibleImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!isVisity) {
					isVisity = true;
					clock_set_isVisibleImage
					.setImageResource(R.drawable.clock_new_unvisible);
					clock_set_isVisibleText.setText("");
				} else {
					showVisibleDialog();
				}
			}
		});
		clock_set_weibo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!isAutoShare) {
					if (!isWeiboOpen) {
						loginSina(SHARE_MEDIA.SINA);
					} else {
						isAutoShare = true;
						clock_set_weibo.setImageResource(R.drawable.switch_on);
					}
				} else {
					isAutoShare = false;
					clock_set_weibo.setImageResource(R.drawable.switch_off);
				}
			}
		});

		clock_set_StopSwitch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(byIdDetailBean != null){
					isStatus = byIdDetailBean.getStatus();
				}

				if (isStatus.equals("1")) {
					isStatus = "1";
					byIdDetailBean.setStatus("0");
					clock_set_StopSwitch.setImageResource(R.drawable.switch_on);
					// clock_set_stop.setText("重启打卡");
					// clock_set_text.setVisibility(View.GONE);
					// clock_set_stop.setTextColor(getResources().getColor(
					// R.color.meibao_color_7));

				} else {
					isStatus = "0";
					byIdDetailBean.setStatus("1");
					clock_set_StopSwitch
					.setImageResource(R.drawable.switch_off);
					// clock_set_stop.setText("暂停打卡");
					// clock_set_text.setVisibility(View.VISIBLE);
					// clock_set_stop.setTextColor(getResources().getColor(
					// R.color.meibao_color_6));
				}
			}
		});
		mTitleBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent();
				setResult(RESULT_OK, in);
				finish();
			}
		});
		mTitleSuccess.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isSuccess){
					return;
				}
				Tools.getLog(Tools.i, "alert", "isStatus:" + isStatus);
				String description = mNewState.getText().toString();
				Tools.getLog(Tools.i, "alert", "alertTime ============== "
						+ alertTime);
				isSuccess = true;
				// if (alertTime == null || alertTime.length() == 0
				// || alertTime.equals("-1")) {
				// AlarmBean bean = null;
				// ArrayList<AlarmBean> alarmList = ClockApplication
				// .getInstance().getAlarmList();
				// if (alarmList != null) {
				// for (int i = 0; i < alarmList.size(); i++) {
				// if (alarmList.get(i).getTastID().equals(mTaskId)) {
				// alarmList.get(i).setAlarmTime("-1");
				// bean = alarmList.get(i);
				// }
				// }
				//
				// if (bean != null) {
				// ClockApplication.getInstance().setAlarmList(
				// alarmList);
				// CallAlarmUtil.closeAlarm(ClockSetActivity.this,
				// bean.getAlarmIndex());
				// }
				// }
				// } else {
				// AlarmBean bean = null;
				// ArrayList<AlarmBean> alarmList = ClockApplication
				// .getInstance().getAlarmList();
				// Tools.getLog(Tools.i, "aaa",
				// "alarmList:" + alarmList.size());
				// for (int i = 0; i < alarmList.size(); i++) {
				// if (alarmList.get(i).getTastID().equals(mTaskId)) {
				// alarmList.get(i).setAlarmTime(alertTime);
				// bean = alarmList.get(i);
				// }
				// }
				// ClockApplication.getInstance().setAlarmList(alarmList);
				// CallAlarmUtil.setAlarm(ClockSetActivity.this, bean);
				// // long dayTime = 1000L * 60 * 60 * 24;
				// // String arrayTime[] = alertTime.split(":");
				// // int hour = Integer.parseInt(arrayTime[0]);
				// // int minute = Integer.parseInt(arrayTime[1]);
				// //
				// // canlendar.setTimeInMillis(System.currentTimeMillis());
				// // canlendar.set(Calendar.HOUR_OF_DAY, hour);
				// // canlendar.set(Calendar.MINUTE, minute);
				// // canlendar.set(Calendar.SECOND, 0);
				// // canlendar.set(Calendar.MILLISECOND, 0);
				// //
				// // Intent intent = new Intent(ClockSetActivity.this,
				// // CallAlarm.class);
				// // PendingIntent sender = PendingIntent.getBroadcast(
				// // ClockSetActivity.this, 1, intent, 0);
				// // /*
				// // * setRepeating() 可让闹钟重复执行
				// // */
				// // AlarmManager am;
				// // am = (AlarmManager) getSystemService(ALARM_SERVICE);
				// // am.setRepeating(AlarmManager.RTC_WAKEUP,
				// // canlendar.getTimeInMillis(), dayTime, sender);
				// // Tools.getLog(Tools.i, "aaa",
				// // "	canlendar.getTimeInMillis(), ============ "
				// // + canlendar.getTimeInMillis());
				// // /* 更新显示的设定闹钟时间 */
				// }
				UpdateTaskConn(description);
				if(!isStatus.equals(oldIsStatus)){
					StopTask(isStatus);
				}
			}
		});
		clock_set_deleteLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDeleteDialog();
			}
		});

	}

	private void getByIdConn() {
		StringBuffer sb = new StringBuffer();
		sb.append("?taskId=");
		sb.append(mTaskId);

		Service.getService(Contanst.HTTP_GETBYIDTASK, null, sb.toString(),
				ClockSetActivity.this).addList(null).request(UrlParams.GET);
	}
	private void StopTask(String type){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("taskId", mTaskId));
			//			if(type.equals("")){
			//				params.add(new BasicNameValuePair("type", "1"));
			//			}else{
			//				params.add(new BasicNameValuePair("type", "0"));
			//			}
			params.add(new BasicNameValuePair("type", type));

		} catch (Exception e) {
			// TODO: handle exception
		}
		Service.getService(Contanst.HTTP_TASK_SUSPEND, null, null,
				ClockSetActivity.this).addList(params).request(UrlParams.POST);
	}

	public void UpdateTaskConn(String description) {
		isSuccess = true;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {

			params.add(new BasicNameValuePair("taskId", mTaskId));
			params.add(new BasicNameValuePair("description", description));
			//			params.add(new BasicNameValuePair("suspendFlag", isStatus));
			if (isVisity) {
				params.add(new BasicNameValuePair("privateFlag", "0"));
			} else {
				params.add(new BasicNameValuePair("privateFlag", "1"));
			}
			if (isAutoShare) {
				params.add(new BasicNameValuePair("autoShareFlag", "1"));
			} else {
				params.add(new BasicNameValuePair("autoShareFlag", "0"));
			}
			if (isTimeLimit) {
				params.add(new BasicNameValuePair("timeLimitFlag", "1"));
				params.add(new BasicNameValuePair("beginTime", beginTime));
				params.add(new BasicNameValuePair("endTime", endTime));
			} else {
				params.add(new BasicNameValuePair("timeLimitFlag", "0"));
			}
			params.add(new BasicNameValuePair("color", colorNum + ""));
			params.add(new BasicNameValuePair("alertTime", alertTime));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Tools.getLog(Tools.d, "aaa", "params:" + params.toString());
		Service.getService(Contanst.HTTP_UPDATETASK, null, null,
				ClockSetActivity.this).addList(params).request(UrlParams.POST);
	}

	public void AddAlertConn() {
		Gson gson = new Gson();
		String str = gson.toJson(alertTimeBeans);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("taskId", mTaskId));// UUID.randomUUID().toString())
			params.add(new BasicNameValuePair("userId", userID));
			params.add(new BasicNameValuePair("alarmListJson", str));
		} catch (Exception e) {

		}
		Service.getService(Contanst.HTTP_ALERT_CREATE, null, null,
				ClockSetActivity.this).addList(params).request(UrlParams.POST);
	}

	public void getAlarmList(String taskid) {
		String str = "?userId=" + userID + "&taskId=" + taskid;
		Service.getService(Contanst.HTTP_ALARM_GETBYRASKID, null, str,
				ClockSetActivity.this).addList(null).request(UrlParams.GET);
	}
	public void GetAllAlarmList(){
		String str = "?userId="+userID;
		Service.getService(Contanst.HTTP_ALARM_GETBYUSERID, null, str,
				ClockSetActivity.this).addList(null).request(UrlParams.GET);
	}
	TimePickerDialog dialog;

	// public void ShowTimeDialog( int hour, int minute){
	// dialog=new TimePickerDialog(
	// this,
	// new TimePickerDialog.OnTimeSetListener(){
	// public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	// if(isStart){
	// mCheckStartText.setText(getTime(hourOfDay, minute));
	// conTime = hourOfDay*60+minute;
	// }else{
	// if(conTime>hourOfDay*60+minute){
	// Toast.makeText(mContext, "您选择的事件不能大于开始时间", Toast.LENGTH_SHORT).show();
	// // dialog.dismiss();
	// if(!dialog.isShowing()){
	// ShowTimeDialog(hourOfDay,minute);
	// }
	// }else{
	// mCheckEndText.setText(getTime(hourOfDay, minute));
	// }
	// }
	// }
	// },
	// hour,
	// minute,
	// true
	// );
	// dialog.show();
	// }
	private static String getTime(int hour, int minute) {
		String hours = hour + "";
		String minutes = minute + "";
		if (hours.length() == 1) {
			hours = 0 + hours;
		}
		if (minutes.length() == 1) {
			minutes = 0 + minutes;
		}
		return hours + ":" + minutes;
	}

	ArrayList<MoreAlertTimeBean> alertTimeBeans = new ArrayList<MoreAlertTimeBean>();

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TIMECODE) {
			if (resultCode == RESULT_OK) {
				alertTimeBeans = (ArrayList<MoreAlertTimeBean>) data
						.getSerializableExtra("list");
				Tools.getLog(Tools.d, "aaa", alertTimeBeans.toString());
				int fristposition = 0;
				int alarmNum = 0;
				for (int i = 0; i < alertTimeBeans.size(); i++) {
					if (alertTimeBeans.get(i).getStatus().equals("1")) {
						alarmNum++;
						if (alarmNum == 1) {
							fristposition = i;
						}
					}
				}
				clock_set_alertTimeText.setText(alarmNum + "");
				if (alarmNum == 0) {
					clock_set_alertTimeImage
					.setImageResource(R.drawable.clock_new_unalert);
					clock_set_alertTimeText.setVisibility(View.GONE);
				} else if (alarmNum == 1) {
					clock_set_alertTimeImage
					.setImageResource(R.drawable.clock_new_alert);
					clock_set_alertTimeText.setVisibility(View.VISIBLE);
					clock_set_alertTimeText.setText(alertTimeBeans.get(
							fristposition).getTime());

				} else {
					clock_set_alertTimeImage
					.setImageResource(R.drawable.clock_new_alert);
					clock_set_alertTimeText.setVisibility(View.VISIBLE);
				}
			}
		} else if (requestCode == LIMITCODE) {
			if (resultCode == RESULT_OK) {
				String time = data.getStringExtra("limittime");
				if (time != null) {
					isTimeLimit = true;
					clock_set_limitTimeText.setText(time);
					String[] arrayTime = time.split("-");
					beginTime = arrayTime[0];
					endTime = arrayTime[1];
					clock_set_limitTimeImage
					.setImageResource(R.drawable.clock_new_limit);
					clock_set_limitTimeText.setVisibility(View.VISIBLE);
				} else {
					isTimeLimit = false;
					clock_set_limitTimeText.setText("");
					clock_set_limitTimeImage
					.setImageResource(R.drawable.clock_new_unlimit);
					clock_set_limitTimeText.setVisibility(View.GONE);
				}
			}
		}

		UMSsoHandler ssoHandler = mShareSDK.mController.getConfig()
				.getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		mShareSDK.mController.getConfig().setSsoHandler(new SinaSsoHandler());
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent in = new Intent();
			setResult(RESULT_OK, in);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// public void StopAndStartConn(String StickFlag) {
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// try {
	//
	// params.add(new BasicNameValuePair("taskId", mTaskId));
	// params.add(new BasicNameValuePair("suspendFlag", StickFlag));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// Service.getService(Contanst.HTTP_UPDATETASK, null, null,
	// ClockSetActivity.this).addList(params).request(UrlParams.POST);
	// }
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

	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.UPDATETASK:
					CallAlarmUtil.closeAllAlarm(ClockSetActivity.this, oldNum);
					Tools.getLog(Tools.d,"bbb","alertTimeBeans1111111111111111111====="+alertTimeBeans.toString() );
					
					if (alertTimeBeans.size() != 0) {
						for (int i = 0; i < alertTimeBeans.size(); i++) {
							Tools.getLog(Tools.d, "alert",
									alertTimeBeans.toString());
							if (isStatus.equals("1")
									&& alertTimeBeans.get(i).getTaskId()
									.equals(mTaskId)) {
								Tools.getLog(Tools.d, "alert", "暂停打卡："
										+ alertTimeBeans.get(i).getTime());
								alertTimeBeans.get(i).setVibrationFlag("1");
							} else {// if(isStatus.equals("0")&&alertTimeBeans.get(i).getTaskId().equals(mTaskId))
								Tools.getLog(Tools.d, "alert", "正常==打卡："
										+ alertTimeBeans.get(i).getTime());
								alertTimeBeans.get(i).setVibrationFlag("0");
							}
							alertTimeBeans.get(i).setTitle(
									byIdDetailBean.getTitle());
						}
						Tools.getLog(Tools.d,"bbb","alertTimeBeans222222222222222222====="+alertTimeBeans.toString() );
						AddAlertConn();

						//						dbHelpers.addAlarmList(alertTimeBeans);// 添加提醒到数据库中

					}
					Tools.getLog(Tools.d,"alert", "是否 " + isStatus);

					// alertTimeBeans = dbHelper.getAlarmList(mTaskId);

					//					alertTimeBeans = dbHelpers.getAllAlarmList();


					GetAllAlarmList();//获得所有提醒


					for (int i = 0; i < alertTimeBeans.size(); i++) {

						if (alertTimeBeans.get(i).getVibrationFlag()
								.equals("0")
								&& alertTimeBeans.get(i).getStatus()
								.equals("1")) {
							AlarmBean alarmBean = new AlarmBean();
							alarmBean.setAlarmIndex(i);
							alarmBean.setAlarmTime(alertTimeBeans.get(i)
									.getTime());
							alarmBean.setTastID(alertTimeBeans.get(i)
									.getTaskId());
							alarmBean.setTastTitle(alertTimeBeans.get(i)
									.getTitle());// title 为空 原来为
							// Tools.getLog(Tools.d,"aaa",
							// "title:==="+alertTimeBeans.get(i).getTitle());
							// Tools.getLog(Tools.d,"aaa",
							// "byIdDetailBean.getTitle():==="+byIdDetailBean.getTitle());
							// alarmList.add(alarmBean);
							CallAlarmUtil.setAlarm(ClockSetActivity.this,
									alarmBean);
						}
					}

					Intent in = new Intent();
					setResult(RESULT_OK, in);
					finish();
					break;
				case Contanst.ALERT_CREATE:
					Tools.getLog(Tools.d, "aaa", "修改数据成功!");
					break;
				case Contanst.ALARM_GETBYRASKID:
					Tools.getLog(Tools.d, "aaa", "获得提醒！");

					alertTimeBeans = (ArrayList<MoreAlertTimeBean>) msg.obj;
					Tools.getLog(Tools.d, "aaa", "alertTimeBeans数据：==="
							+ alertTimeBeans.toString());
					int fristposition = 0;
					int num = 0;
					for (int i = 0; i < alertTimeBeans.size(); i++) {
						if (alertTimeBeans.get(i).getStatus().equals("1")) {
							num++;
							if (num == 1) {
								fristposition = i;
							}
						}
					}
					Tools.getLog(Tools.d, "ccc", "============111========");
					if (num == 0) {
						clock_set_alertTimeText.setText("");
						clock_set_alertTimeText.setVisibility(View.GONE);
						clock_set_alertTimeImage
						.setImageResource(R.drawable.clock_new_unalert);
					} else if (num == 1) {
						clock_set_alertTimeImage
						.setImageResource(R.drawable.clock_new_alert);
						clock_set_alertTimeText.setVisibility(View.VISIBLE);
						clock_set_alertTimeText.setText(alertTimeBeans.get(
								fristposition).getTime());

					}else {
						alertTime = byIdDetailBean.getAlertTime();
						clock_set_alertTimeText.setText(num + "");
						clock_set_alertTimeText.setVisibility(View.VISIBLE);
						clock_set_alertTimeImage
						.setImageResource(R.drawable.clock_new_alert);
					}
					break;
				case Contanst.GETBYIDTASK:
					byIdDetailBean = (ByIdDetailBean) msg.obj;
					initView(byIdDetailBean);
					break;
				case Contanst.TASK_DELETE:
					Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
					Intent deleteIn = getIntent();
					setResult(222, deleteIn);
					finish();
					break;
				case Contanst.ALARM_GETBYUSERID:
					Tools.getLog(Tools.d, "ccc", "============222========");
					ArrayList<MoreAlertTimeBean> oldalertTimeBeans = (ArrayList<MoreAlertTimeBean>) msg.obj;
					oldNum = oldalertTimeBeans.size();
					break;
				case Contanst.TASK_SUSPEND:
					//					Tools.getLog(Tools.d, "ccc", "============222========");
					//					ArrayList<MoreAlertTimeBean> oldalertTimeBeans = (ArrayList<MoreAlertTimeBean>) msg.obj;
					//					oldNum = oldalertTimeBeans.size();
					Tools.getLog(Tools.d, "aaa", "isStatus+====1为暂停, 0为取消暂停==="+isStatus); 
					break;
				}
				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				switch (msg.arg1) {
				case Contanst.UPDATETASK:

					break;
				}
				break;
			}
		}
	};

	public void showVisibleDialog() {
		new AlertDialog.Builder(mContext)
		.setTitle("隐私设置")
		.setMessage("设置为隐私后，所有打卡信息\n仅能被自己看到")
		.setPositiveButton("设为隐私",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				isVisity = false;
				clock_set_isVisibleImage
				.setImageResource(R.drawable.clock_new_visible);
				clock_set_isVisibleText.setText("已隐私");
			}
		})
		.setNegativeButton("返回", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		}).show();
		// final TaskVisityDialog taskVisityDialog = new TaskVisityDialog(
		// ClockSetActivity.this);
		// taskVisityDialog.show();
		// taskVisityDialog.mCancel.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// taskVisityDialog.dismiss();
		// }
		// });
		// taskVisityDialog.mResult.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// isVisity = false;
		// clock_set_isVisibleImage
		// .setImageResource(R.drawable.clock_new_visible);
		// taskVisityDialog.dismiss();
		// }
		// });

	}

	boolean isWeiboOpen;

	public void initView(ByIdDetailBean byIdDetailBean) {
		mTaskId = byIdDetailBean.getId();
		getAlarmList(mTaskId);
		mNewText.setText(byIdDetailBean.getTitle());
		String Description = byIdDetailBean.getDescription();
		if(Description != null && Description.length() != 0){
			mNewState.setText(byIdDetailBean.getDescription());
			mNewState.setSelection(byIdDetailBean.getDescription().length());
		}

		isWeiboOpen = settings.getBoolean("isWeiboOpen", false);
		if (!isWeiboOpen || byIdDetailBean.getAutoShareFlag() == 0) {
			isAutoShare = false;
			clock_set_weibo.setImageResource(R.drawable.switch_off);
		}
		if (byIdDetailBean.getPrivateFlag().equals("1")) {
			clock_set_isVisibleImage
			.setImageResource(R.drawable.clock_new_visible);
			clock_set_isVisibleText.setText("已隐私");
			isVisity = false;
		} else {
			clock_set_isVisibleImage
			.setImageResource(R.drawable.clock_new_unvisible);
			clock_set_isVisibleText.setText("");
			isVisity = true;
		}
		if (byIdDetailBean.getTimeLimitFlag().equals("1")) {
			beginTime = byIdDetailBean.getBeginTime();
			endTime = byIdDetailBean.getEndTime();
			if (beginTime != null && endTime != null) {
				clock_set_limitTimeText.setText(beginTime + "-" + endTime);
				clock_set_limitTimeImage
				.setImageResource(R.drawable.clock_new_limit);
				clock_set_limitTimeText.setVisibility(View.VISIBLE);
				isTimeLimit = true;
			} else {
				clock_set_limitTimeText.setText("");
				clock_set_limitTimeImage
				.setImageResource(R.drawable.clock_new_unlimit);
				clock_set_limitTimeText.setVisibility(View.GONE);
				isTimeLimit = false;
			}
		} else {
			clock_set_limitTimeText.setText("");
			clock_set_limitTimeImage
			.setImageResource(R.drawable.clock_new_unlimit);
			clock_set_limitTimeText.setVisibility(View.GONE);
			isTimeLimit = false;
		}
		colorNum = Integer.parseInt(byIdDetailBean.getColor());

		//		 mCheckColorImageLayout.setBackgroundColor(getResources().getColor(arrayColor[colorNum]));

		isStatus = byIdDetailBean.getStatus();// 状态,1为正常, 2为暂停
		if (isStatus.equals("1")) {
			isStatus = "0";
			clock_set_StopSwitch.setImageResource(R.drawable.switch_off);
			// clock_set_stop.setText("暂停打卡");
			// clock_set_text.setVisibility(View.VISIBLE);
			// clock_set_stop.setTextColor(getResources().getColor(
			// R.color.meibao_color_6));
		} else {
			isStatus = "1";
			clock_set_StopSwitch.setImageResource(R.drawable.switch_on);
			// clock_set_text.setVisibility(View.GONE);
			// clock_set_stop.setText("重启打卡");
			// clock_set_stop.setTextColor(getResources().getColor(
			// R.color.meibao_color_7));
		}
		oldIsStatus = isStatus;
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		// onPageEnd 在onPause
		// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	public void showDeleteDialog() {
		new AlertDialog.Builder(mContext).setTitle("确认删除此卡？")
		.setMessage("删除后此卡所有数据将无法恢复")
		.setPositiveButton("删除", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				deleteTaskConn();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		}).show();

	}

	private void deleteTaskConn() {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("taskId", mTaskId));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_TASK_DELETE, null, null,
				ClockSetActivity.this).addList(params).request(UrlParams.POST);

	}

	/**
	 * 授权。如果授权成功，则获取用户信息</br>
	 */
	private void loginSina(final SHARE_MEDIA platform) {

		mShareSDK.mController.doOauthVerify(this, platform,
				new UMAuthListener() {

			@Override
			public void onStart(SHARE_MEDIA platform) {
			}

			@Override
			public void onError(SocializeException e,
					SHARE_MEDIA platform) {
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				String ssoUserID = value.getString("uid");
				if (!TextUtils.isEmpty(ssoUserID)) {
					mEditor.putBoolean("isWeiboOpen", true);
					mEditor.commit();
					isAutoShare = true;
					clock_set_weibo.setImageResource(R.drawable.switch_on);
				} else {
					Toast.makeText(ClockSetActivity.this, "授权失败...",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {

			}
		});
	}

}
