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
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yktx.check.util.Tools;
import com.yktx.check.widget.SlideSwitch;
import com.yktx.check.widget.SlideSwitch.SlideListener;

public class ClockLimitActivity extends BaseActivity {
	private TimePicker mStartPicker,mStopPicker;
	private RelativeLayout StartLayout,StopLayout;
	private ImageView mSlideSwitch;
	private ImageView StartLayout_image,StopLayout_image,mBack,mRightImage;
	private LinearLayout clock_limit_infolayout;
	private boolean isStartOpen,isStopOpen,isOpen;
	private String timeContent;
	private int startHour,startMinute,stopHour,stopMinute;
	private TextView mTitleContent;
	private RelativeLayout mTitleLayout;
	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_clock_limit);
		mStartPicker = (TimePicker) findViewById(R.id.StartPicker);
		mStopPicker = (TimePicker) findViewById(R.id.StopPicker);
		StartLayout = (RelativeLayout) findViewById(R.id.StartLayout);
		StopLayout = (RelativeLayout) findViewById(R.id.StopLayout);
		mSlideSwitch = (ImageView) findViewById(R.id.clock_limit_SlideSwitch);
		StartLayout_image = (ImageView) findViewById(R.id.StartLayout_image);
		StopLayout_image = (ImageView) findViewById(R.id.StopLayout_image);
		clock_limit_infolayout = (LinearLayout) findViewById(R.id.clock_limit_infolayout);
		mBack = (ImageView) findViewById(R.id.title_item_leftImage);
		mRightImage = (ImageView) findViewById(R.id.title_item_rightImage);
		mTitleContent = (TextView) findViewById(R.id.title_item_content);
		mTitleLayout = (RelativeLayout) findViewById(R.id.title_item_layout);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		mTitleLayout.setBackgroundColor(getResources().getColor(R.color.white));
		mTitleContent.setText("限时");
		mRightImage.setVisibility(View.GONE);
		timeContent = getIntent().getStringExtra("limittime");
		if(timeContent == null||timeContent.length() == 0||timeContent.indexOf(":") == -1||timeContent.indexOf("-") == -1||!maohao()){
			clock_limit_infolayout.setVisibility(View.GONE);
			startHour = mStartPicker.getCurrentHour();
			startMinute = mStartPicker.getCurrentMinute();
			stopHour = mStopPicker.getCurrentHour();
			stopMinute =  mStopPicker.getCurrentMinute();
		}else{
			mSlideSwitch.setImageResource(R.drawable.switch_on);
			clock_limit_infolayout.setVisibility(View.VISIBLE);
			String[] arrayTime = timeContent.split("-");
			String[] arrayStartTime = arrayTime[0].split(":");
			startHour = Integer.parseInt(arrayStartTime[0]);
			startMinute = Integer.parseInt(arrayStartTime[1]);
			if (arrayTime.length > 1) {
				String[] arrayStopTime = arrayTime[1].split(":");
				stopHour = Integer.parseInt(arrayStopTime[0]);
				stopMinute = Integer.parseInt(arrayStopTime[1]);
			}
			
			mStartPicker.setCurrentHour(startHour);
			mStartPicker.setCurrentMinute(startMinute);

			mStopPicker.setCurrentHour(stopHour);
			mStopPicker.setCurrentMinute(stopMinute);

			clock_limit_infolayout.setVisibility(View.VISIBLE);
			isStartOpen = true;
			isStopOpen = true;
			isOpen =true;
		}

		mStartPicker.setIs24HourView(true);
		mStopPicker.setIs24HourView(true);

	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				backThis();
			}
		});
		mSlideSwitch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isOpen){
					MobclickAgent.onEvent(mContext, "clicksetlimit");
					isOpen =false;
					clock_limit_infolayout.setVisibility(View.GONE);
					mSlideSwitch.setImageResource(R.drawable.switch_off);
				}else{
					isOpen =true; 
					clock_limit_infolayout.setVisibility(View.VISIBLE);
					mSlideSwitch.setImageResource(R.drawable.switch_on);
				}
			}
		});
//		mSlideSwitch.setSlideListener(new SlideListener() {
//			@Override
//			public void open() {
//				// TODO Auto-generated method stub
//				isOpen =true; 
//				clock_limit_infolayout.setVisibility(View.VISIBLE);
//			}
//
//			@Override
//			public void close() {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		mStartPicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				Tools.getLog(Tools.i, "ccc", "时间："+hourOfDay+":"+minute);
				int cStart = hourOfDay*60+minute;
				int cStop = stopHour*60+stopMinute;
				if(cStart == cStop){
					Toast.makeText(mContext, "开始时间和结束时间不可相同", Toast.LENGTH_SHORT).show();
					//					isTimeOK = false;
					mStartPicker.setCurrentHour(startHour);
					mStartPicker.setCurrentMinute(startMinute);
					return;
				}
				if(cStart > cStop){
					Toast.makeText(mContext, "结束时间不能小于开始时间", Toast.LENGTH_SHORT).show();
					//					isTimeOK = false;
					mStartPicker.setCurrentHour(startHour);
					mStartPicker.setCurrentMinute(startMinute);
					return;
				}
				startHour = hourOfDay;
				startMinute = minute;
				//				isTimeOK = true;
			}
		});
		mStopPicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				int cStart =startHour*60+startMinute;
				int cStop =  hourOfDay*60+minute;
				if(cStart == cStop){
					Toast.makeText(mContext, "开始时间和结束时间不可相同", Toast.LENGTH_SHORT).show();
					//					isTimeOK = false;
					mStopPicker.setCurrentHour(stopHour);
					mStopPicker.setCurrentMinute(stopMinute);
					return;
				}
				if(cStart > cStop){
					Toast.makeText(mContext, "结束时间不能小于开始时间", Toast.LENGTH_SHORT).show();
					//					isTimeOK = false;
					mStopPicker.setCurrentHour(stopHour);
					mStopPicker.setCurrentMinute(stopMinute);
					return;
				}
				stopHour = hourOfDay;
				stopMinute = minute;
				//				isTimeOK = true;
			}
		});
//		StartLayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if(isStartOpen){
//					isStartOpen = false;
//					mStartPicker.setVisibility(View.GONE);
//				}else{
//					isStartOpen = true;
//					mStartPicker.setVisibility(View.VISIBLE);
//				}
//			}
//		});
//		StopLayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if(isStopOpen){
//					isStopOpen = false;
//					mStopPicker.setVisibility(View.GONE);
//				}else{
//					isStopOpen = true;
//					mStopPicker.setVisibility(View.VISIBLE);
//				}
//			}
//		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			backThis();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void backThis() {
//		Tools.getLog(Tools.i, "aaa", "mSlideSwitch.getIsOpen() ========================= "+mSlideSwitch.getIsOpen());
		Intent in = new  Intent();
		if (!isOpen) {
			setResult(RESULT_OK, in);
			finish();
			return;
		}
		int cStart = startHour * 60 + startMinute;
		int cStop = stopHour * 60 + stopMinute;
		if (cStart == cStop) {
			Toast.makeText(mContext, "开始时间和结束时间不可相同", Toast.LENGTH_SHORT)
					.show();
			// isTimeOK = false;
			return;
		}
		
		StringBuffer sb = new StringBuffer();
		if(isOpen){
			if(startHour< 10){
				sb.append("0"+startHour);
			}else{
				sb.append(startHour);
			}
			sb.append(":");
			if(startMinute< 10){
				sb.append("0"+startMinute);
			}else{
				sb.append(startMinute);
			}
			sb.append("-");
			if(stopHour< 10){
				sb.append("0"+stopHour);
			}else{
				sb.append(stopHour);
			}
			sb.append(":");
			if(stopMinute< 10){
				sb.append("0"+stopMinute);
			}else{
				sb.append(stopMinute);
			}
			in.putExtra("limittime", sb.toString());
		}
		setResult(RESULT_OK, in);
		finish();
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
	public boolean maohao(){
		String s1=":";
		char c=s1.charAt(0);//c为需要计数的字符
		int i,index=-1,count=0;
		for(i=0;i<timeContent.length();i++)

			if((timeContent.indexOf(c,index+1))!=-1){

				index=timeContent.indexOf(c,index+1);

				count++;

			}
		if(count == 2){
			return true;
		}else{
			return false;
		}
	}
}
