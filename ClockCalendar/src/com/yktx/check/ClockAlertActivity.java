package com.yktx.check;


import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.yktx.check.util.Tools;
import com.yktx.check.widget.SlideSwitch;
import com.yktx.check.widget.SlideSwitch.SlideListener;

public class ClockAlertActivity extends BaseActivity {
//	private LinearLayout mSeekLayout;
//	WheelView hours,mins;
	private String time;
	private SlideSwitch mAlertCheck;
	private boolean isCheck;
	private ImageView mLeftImage,mRightImage;
	private String[] timeArray;
	private TimePicker clock_alert_TimePicker;
	private int hour,minutes;
	private ImageView mTitleRightImage;
	private TextView mTitleContent;
	private RelativeLayout mTitleLayout;
	private LinearLayout clock_alert_TimePickerLayout;
	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_clock_alert);
		mAlertCheck = (SlideSwitch) findViewById(R.id.new_alertSlideSwitch);
		//		hours.setLabel("hours");//加文字，滑动旁边

		//		mins.setLabel("mins");//加文字，滑动旁边
		mLeftImage = (ImageView) findViewById(R.id.title_item_leftImage);
		mRightImage = (ImageView) findViewById(R.id.title_item_rightImage);
		clock_alert_TimePicker = (TimePicker) findViewById(R.id.clock_alert_TimePicker);
		mTitleContent = (TextView) findViewById(R.id.title_item_content);
		mTitleLayout = (RelativeLayout) findViewById(R.id.title_item_layout);
		clock_alert_TimePickerLayout = (LinearLayout) findViewById(R.id.clock_alert_TimePickerLayout);
		
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		mTitleLayout.setBackgroundColor(getResources().getColor(R.color.white));
		mTitleContent.setText("提醒");
		mRightImage.setVisibility(View.GONE);
		time = getIntent().getStringExtra("ischeck");
		if(time!=null&&time.length()!=0){
			mAlertCheck.setState(true);
			clock_alert_TimePickerLayout.setVisibility(View.VISIBLE);
			timeArray = time.split(":");
			hour = Integer.parseInt(timeArray[0]);
			minutes = Integer.parseInt(timeArray[1]);
			clock_alert_TimePicker.setCurrentHour(Integer.parseInt(timeArray[0]));
			clock_alert_TimePicker.setCurrentMinute(Integer.parseInt(timeArray[1]));
		}
		clock_alert_TimePicker.setIs24HourView(true);
		
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		clock_alert_TimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				hour = hourOfDay;
				minutes = minute;
			}
		});
		mLeftImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Tools.getLog(Tools.i, "aaa", "mAlertCheck.getIsOpen() ============ "+mAlertCheck.getIsOpen());
				SuccessFinish();
			}
		});
		mAlertCheck.setSlideListener(new SlideListener() {
			
			@Override
			public void open() {
				// TODO Auto-generated method stub
				clock_alert_TimePickerLayout.setVisibility(View.VISIBLE);
				hour = clock_alert_TimePicker.getCurrentHour();
				minutes = clock_alert_TimePicker.getCurrentMinute();
				isCheck = true;
			}
			
			@Override
			public void close() {
				// TODO Auto-generated method stub
				clock_alert_TimePickerLayout.setVisibility(View.GONE);
				isCheck = false;
				time = "";
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SuccessFinish();
		}
		return false;
	}
	public void SuccessFinish(){
		if(!mAlertCheck.getIsOpen()){
			Tools.getLog(Tools.i, "aaa", "dasdasdasdasd=================");
			Intent in = getIntent();
//			in.putExtra("time", "-1");
			setResult(RESULT_OK, in);
			finish();
			return;
		}
		StringBuffer sb = new StringBuffer();
		if(hour< 10){
			sb.append("0"+hour);
		}else{
			sb.append(hour);
		}
		sb.append(":");
		if(minutes< 10){
			sb.append("0"+minutes);
		}else{
			sb.append(minutes);
		}
		time = sb.toString();
		Intent in = getIntent();
		in.putExtra("time", time);
		setResult(RESULT_OK, in);
		finish();
	}

}
