package com.yktx.check.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.charting.util.MultiAxisChartView;
import com.charting.util.MultiAxisTimeChartView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yktx.check.BaseActivity;
import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.ClockGroupInfoFragmentActivity;
import com.yktx.check.PointExplainActivity;
import com.yktx.check.R;
import com.yktx.check.TaskInfoActivity;
import com.yktx.check.bean.CustomDate;
import com.yktx.check.bean.JobStatsBean;
import com.yktx.check.bean.LastTwoUsersBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;
import com.yktx.check.widget.CalendarView.CallBack;
import com.yktx.view.TaskCaledarView;

public class ChartMeteringView extends LinearLayout implements ServiceListener,CallBack{
	private Context mContext;
	private Activity mActivity;
	private LinearLayout chartViewLayout,chartingView;
	MultiAxisChartView chartingFirst;
	MultiAxisTimeChartView chartingTime;
	private int curTabIndex;
	private TextView taskCountButton,taskTimeButton,taskCalendar,infoLookMore,
	//	taskBuildingTitleText,
	taskCalendarText;
	private TextView taskUserName
	//	, infoComment, taskUserName2, infoComment2,infoCount,infoCount2
	;
	ArrayList<JobStatsBean> jobList = new ArrayList<JobStatsBean>(10);
	private LastTwoUsersBean lastTwoJobBean = new LastTwoUsersBean();
	TaskCaledarView caledarView;
	//	private LinearLayout taskLastTwoLayout;
	private RelativeLayout taskNameLayout,taskLastTwoTitleLayout
	//	, infoLookMoreLayout
	;
	private ImageView 
	//	infoHaveImage,infoHaveImage2,
	task_item_isVisityImage; 
	private TextView task_item_clockName,task_item_TotalDateCount,task_item_qiqiuNum,task_item_TotalcheckCount,task_item_content;

	public DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.toumingimg).showImageForEmptyUri(null)
	.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheOnDisk(true)
	// .displayer(new RoundedBitmapDisplayer(150))
	// .displayer(new RoundedBitmapDisplayer(6))
	// .cacheInMemory(false)
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();


	private ImageView task_item_medalImage;
	public ChartMeteringView(Activity context) {
		super(context);
		mActivity = context;
		mContext = context;
		init();
		setlistener();
	}
	
	boolean isOhter;
	public LinearLayout getBestView(boolean isOhter) {
		this.isOhter = isOhter; 
		return chartViewLayout;

	}
	public void init(){
		chartViewLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.taskinfo_chartview, null);
		chartingView = (LinearLayout) chartViewLayout.findViewById(R.id.showView);
		taskCountButton = (TextView) chartViewLayout.findViewById(R.id.taskCountButton);
		taskTimeButton = (TextView) chartViewLayout.findViewById(R.id.taskTimeButton);
		taskCalendar = (TextView) chartViewLayout.findViewById(R.id.taskCalendar);
		taskCalendarText = (TextView) chartViewLayout.findViewById(R.id.taskCalendarText);

		taskUserName = (TextView) chartViewLayout.findViewById(R.id.taskUserName);
		//		infoComment = (TextView) chartViewLayout.findViewById(R.id.infoComment);
		//		taskUserName2 = (TextView) chartViewLayout.findViewById(R.id.taskUserName2);
		//		infoComment2 = (TextView) chartViewLayout.findViewById(R.id.infoComment2);
		//		infoCount = (TextView) chartViewLayout.findViewById(R.id.infoCount);
		//		infoCount2 = (TextView) chartViewLayout.findViewById(R.id.infoCount2);
		infoLookMore = (TextView) chartViewLayout.findViewById(R.id.infoLookMore);

		//		taskLastTwoLayout = (LinearLayout) chartViewLayout.findViewById(R.id.taskLastTwoLayout);

		//		infoHaveImage = (ImageView) chartViewLayout.findViewById(R.id.infoHaveImage);
		//		infoHaveImage2 = (ImageView) chartViewLayout.findViewById(R.id.infoHaveImage2);
		task_item_isVisityImage = (ImageView) chartViewLayout.findViewById(R.id.task_item_isVisityImage);
		//		taskBuildingLayout1 = (RelativeLayout) chartViewLayout.findViewById(R.id.taskBuildingLayout1);
		//		taskBuildingLayout2 = (RelativeLayout) chartViewLayout.findViewById(R.id.taskBuildingLayout2);
		//		taskBuildingMoreLayout = (RelativeLayout) chartViewLayout.findViewById(R.id.taskBuildingMoreLayout);
		//		taskBuildingTitleText = (TextView) chartViewLayout.findViewById(R.id.taskBuildingTitleText);
		taskLastTwoTitleLayout = (RelativeLayout) chartViewLayout.findViewById(R.id.taskLastTwoTitleLayout);
		//		taskinfoBottomListLayout = (RelativeLayout) chartViewLayout.findViewById(R.id.taskinfoBottomListLayout);
		//		infoLookMoreLayout = (RelativeLayout) chartViewLayout.findViewById(R.id.infoLookMoreLayout);
		taskNameLayout = (RelativeLayout) chartViewLayout.findViewById(R.id.taskNameLayout);

		task_item_clockName = (TextView) chartViewLayout.findViewById(R.id.task_item_clockName);
		task_item_TotalDateCount = (TextView) chartViewLayout.findViewById(R.id.task_item_TotalDateCount);
		task_item_TotalcheckCount = (TextView) chartViewLayout.findViewById(R.id.task_item_TotalcheckCount);
		task_item_qiqiuNum = (TextView) chartViewLayout.findViewById(R.id.task_item_qiqiuNum);
		task_item_content = (TextView) chartViewLayout.findViewById(R.id.task_item_content);
		task_item_medalImage = (ImageView) chartViewLayout.findViewById(R.id.task_item_medalImage);

		//		task_description = (TextView) chartViewLayout.findViewById(R.id.task_description);
		//		if(TaskInfoActivity.mDescription != null &&TaskInfoActivity.mDescription.length() != 0){
		//			task_description.setVisibility(View.VISIBLE);
		//			task_description.setText(TaskInfoActivity.mDescription);
		//		}else{
		//			task_description.setVisibility(View.GONE);
		//		}

		caledarView = new TaskCaledarView(mActivity, this, TaskInfoActivity.mTaskId);
		SelectClockStateConn(null, null);
		task_item_clockName.setText(TaskInfoActivity.byIdDetailBean.getTitle());
		task_item_TotalDateCount.setText(TaskInfoActivity.byIdDetailBean.getTotalDateCount());
		task_item_TotalcheckCount.setText(TaskInfoActivity.byIdDetailBean.getTotalCheckCount());
		String description = TaskInfoActivity.byIdDetailBean.getDescription();
		if(description != null&&description.length() != 0){
			task_item_content.setText(description);
			task_item_content.setVisibility(View.VISIBLE);
		}else{
			if(isOhter){
				task_item_content.setText("Ta很懒还没有编写卡片说明");
			}else{
				task_item_content.setText("点击右边设置按钮编写卡片说明");
			}
			
		}

//		if(TaskInfoActivity.byIdDetailBean.getBadgeSource().equals("0")){
//			task_item_medalImage.setImageResource(R.drawable.xz0);
//		}else{
//			ImageLoader.getInstance().displayImage(UrlParams.QINIU_IP +TaskInfoActivity.byIdDetailBean.getBadgePath()+"?imageMogr2/thumbnail/39x45"
//					, task_item_medalImage, options);
//		}

		String qiqiuNum = TaskInfoActivity.byIdDetailBean.getPoint();
		String str = "气球数：";
		if(qiqiuNum != null && qiqiuNum.length() != 0){
			task_item_qiqiuNum.setText(str+qiqiuNum);
		}else{
			task_item_qiqiuNum.setText(str+"0");
		}
		String privateFlag = TaskInfoActivity.byIdDetailBean.getPrivateFlag();
		if("1".equals(privateFlag)){
			task_item_isVisityImage.setVisibility(View.VISIBLE);
		}else{
			task_item_isVisityImage.setVisibility(View.GONE);
		}
	}

	public void setlistener(){


		taskCountButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(curTabIndex == 2){
					return;
				}
				curTabIndex = 2;
				chartingView.removeAllViews();
				initMultiAxisChartView(chartingFirst);
				//				taskCountButton.setBackgroundResource(R.drawable.taskinfo_select);
				//				taskTimeButton.setBackgroundResource(R.drawable.taskinfo_unselect);
				//				taskCalendar.setBackgroundResource(R.drawable.taskinfo_unselect);
				taskCountButton.setTextColor(getResources().getColor(R.color.meibao_color_1));
				taskTimeButton.setTextColor(getResources().getColor(R.color.meibao_color_11));
				taskCalendar.setTextColor(getResources().getColor(R.color.meibao_color_11));
			}
		});
		taskTimeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(curTabIndex == 3){
					return;
				}
				curTabIndex = 3;
				chartingView.removeAllViews();
				initMultiAxisChartView(chartingTime);
				//				taskCountButton.setBackgroundResource(R.drawable.taskinfo_unselect);
				//				taskTimeButton.setBackgroundResource(R.drawable.taskinfo_select);
				//				taskCalendar.setBackgroundResource(R.drawable.taskinfo_unselect);
				taskCountButton.setTextColor(getResources().getColor(R.color.meibao_color_11));
				taskTimeButton.setTextColor(getResources().getColor(R.color.meibao_color_1));
				taskCalendar.setTextColor(getResources().getColor(R.color.meibao_color_11));
			}
		});
		taskCalendar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(curTabIndex == 1){
					return;
				}
				curTabIndex = 1;
				chartingView.removeAllViews();
				chartingView.addView(caledarView.getBestView());
				//				taskCountButton.setBackgroundResource(R.drawable.taskinfo_unselect);
				//				taskTimeButton.setBackgroundResource(R.drawable.taskinfo_unselect);
				//				taskCalendar.setBackgroundResource(R.drawable.taskinfo_select);
				taskCountButton.setTextColor(getResources().getColor(R.color.meibao_color_11));
				taskTimeButton.setTextColor(getResources().getColor(R.color.meibao_color_11));
				taskCalendar.setTextColor(getResources().getColor(R.color.meibao_color_1));
			}
		});
		task_item_medalImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, PointExplainActivity.class);
				mContext.startActivity(in);	
			}
		});
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

	private View initMultiAxisChartView( View view) {
		// 缩放控件放置在FrameLayout的上层，用于放大缩小图表
		FrameLayout.LayoutParams frameParm = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		frameParm.gravity = Gravity.BOTTOM | Gravity.RIGHT;
		// 图表显示范围在占屏幕大小的90%的区域内
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int scrWidth = (int) (dm.widthPixels);
		int scrHeight = (int) (BaseActivity.ScreenWidth * 6/7 + 20*BaseActivity.DENSITY);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				scrWidth, scrHeight);
		// 居中显示
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		// 图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中

		// chartLayout.addView(chartingFirst, layoutParams);
		chartingView.removeAllViews();
		chartingView.addView(view, layoutParams);
		return chartingView;
	}

	public void SelectClockStateConn(String beginDate, String endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("?taskId=");
		sb.append(TaskInfoActivity.mTaskId);

		Service.getService(Contanst.HTTP_GETSTATISTIC, null, sb.toString(),
				ChartMeteringView.this).addList(null).request(UrlParams.GET);
	}

	public void getLastTwoJobsConn() {
		StringBuffer sb = new StringBuffer();
		sb.append("?taskId=");
		sb.append(TaskInfoActivity.mTaskId);
		Service.getService(Contanst.HTTP_BUILDING_GETLASTTWOUSERS, null, sb.toString(),
				ChartMeteringView.this).addList(null).request(UrlParams.GET);
	}
	//	public void CreatorConn() {
	//		StringBuffer sb = new StringBuffer();
	////		if (userID != null && userID.length() != 0) {
	//			sb.append("?userId=");
	//			sb.append(TaskInfoActivity.thisJobUserid);
	////		}
	//		sb.append("&buildingId=");
	//		sb.append(TaskInfoActivity.buildingID);
	//
	//		Service.getService(Contanst.HTTP_GETBASICINFO, null, sb.toString(),
	//				char).addList(null)
	//				.request(UrlParams.GET);
	//	}

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
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.GETSTATISTIC:
					jobList = (ArrayList<JobStatsBean>) msg.obj;
					chartingFirst = new MultiAxisChartView(mContext, jobList);
					chartingTime = new MultiAxisTimeChartView(mContext, jobList,
							"00:00", "23:10");
					curTabIndex = 1;
					chartingView.removeAllViews();
					chartingView.addView(caledarView.getBestView());
					getLastTwoJobsConn();
					break;
				case Contanst.BUILDING_GETLASTTWOUSERS:
					lastTwoJobBean = (LastTwoUsersBean) msg.obj;
					initView();
					Tools.getLog(Tools.i, "aaa",  "刷新！！！！！！");
					//					getByTaskIdConn(1);
					break;	
				}
				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				switch (msg.arg1) {
				case Contanst.GETSTATISTIC:
					Tools.getLog(Tools.d, "aaa", "GETSTATISTIC:	"+message);
					break;
				case Contanst.GETLASTTHREE:
					break;
				}
				break;
			}
		}
	};

	private void initView() {
		int TotalManCount = lastTwoJobBean.getTotalManCount();
		Tools.getLog(Tools.d, "aaa", "===BuildingCount==="+TotalManCount);
		if(TotalManCount==0){
			//			taskLastTwoLayout.setVisibility(View.GONE);
			taskLastTwoTitleLayout.setVisibility(View.GONE);
		}else {

			//			StringBuffer sb = new StringBuffer();
			//			ArrayList<LastThree> list = lastTwoJobBean.getLastThrees();
			//			for(int i = 0; i < 	list.size(); i++){
			//
			//				if(i == 1){
			//					sb.append(",");
			//					sb.append(list.get(i).getName());
			//				} else {
			//					sb.append(list.get(i).getName());
			//				}
			//
			//			}
			//			if(lastTwoJobBean.getTotalJobCount() > 2){
			//				sb.append("...");
			//			}
			String str = lastTwoJobBean.getUsers();
			taskUserName.setText(str);

			//每个名字前都有个图片
//						Tools.getLog(Tools.d, "aaa", "没改之前的：====="+str);
//						String userName[] = str.split(",");
//						ArrayList<ImageSpan> ImageSpanlist = new ArrayList<ImageSpan>();
//						Bitmap bitmap = null;
//						bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.xq_people);
//						for(int i= 0;i<5;i++){
//							ImageSpan imageSpan1 = new ImageSpan(mContext, bitmap);
//							ImageSpanlist.add(imageSpan1);
//						}
//						ImageSpan imageSpan1 = new ImageSpan(mContext, bitmap);
//						String str1 = str.replace(",", "  ");
//						Tools.getLog(Tools.d, "aaa", "改完之后的：====="+str1);
//						SpannableString spannableString;
//						for(int i= 0;i<userName.length;i++){
//							if(i == 0){
//								spannableString = new SpannableString(
//										"  "+userName[i]);
//								spannableString.setSpan(ImageSpanlist.get(i),0,
//										1,
//										Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//							}else{
//								spannableString = new SpannableString(
//										"   "+userName[i]);
//								spannableString.setSpan(ImageSpanlist.get(i),1,
//										2,
//										Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//							}
//							
//							taskUserName.append(spannableString);
//						}







			Tools.getLog(Tools.d, "aaa", "taskUserName.getText().toString();;;;;;"+taskUserName.getText().toString());
			Tools.getLog(Tools.d, "aaa", "length;;;;;;"+taskUserName.getText().toString().length());

			infoLookMore
			.setText("一共有"+TotalManCount + "人打卡");
		}
		//		}else if(BuildingCount == 1){
		//			taskUserName.setText(lastTwoJobBean.getLastThrees().get(0).getName());
		////			infoComment.setText(lastTwoJobBean.getLastThrees().get(0)
		////					.getSignature());
		////			infoCount.setText(TimeUtil.getTimes(lastTwoJobBean.getLastThrees().get(0).getCheck_time()));
		//			if(lastTwoJobBean.getLastThrees().get(0).getPicCount().equals("0")){
		//				infoHaveImage.setVisibility(View.GONE);
		//			}
		//			infoLookMore
		//			.setText(lastTwoJobBean.getTotalJobCount() + "人次");
		//			taskBuildingLayout2.setVisibility(View.GONE);
		//			infoLookMore.setVisibility(View.GONE);
		//			taskLastTwoTitleLayout.setVisibility(View.VISIBLE);
		//		}else{
		//			if(lastTwoJobBean.getLastThrees().size() == 0){
		//				
		//				return;
		//			}
		//			taskUserName.setText(lastTwoJobBean.getLastThrees().get(0).getName());
		//			infoComment.setText(lastTwoJobBean.getLastThrees().get(0)
		//					.getSignature());
		//			taskUserName2.setText(lastTwoJobBean.getLastThrees().get(1).getName());
		//			infoComment2.setText(lastTwoJobBean.getLastThrees().get(1)
		//					.getSignature());
		//			infoLookMore
		//			.setText(lastTwoJobBean.getTotalJobCount() + "人次");
		//			infoCount.setText(TimeUtil.getTimes(lastTwoJobBean.getLastThrees().get(0).getCheck_time()));
		//			infoCount2.setText(TimeUtil.getTimes(lastTwoJobBean.getLastThrees().get(1).getCheck_time()));
		//			if(lastTwoJobBean.getLastThrees().get(0).getPicCount().equals("0")){
		//				infoHaveImage.setVisibility(View.GONE);
		//			}
		//			if(lastTwoJobBean.getLastThrees().get(1).getPicCount().equals("0")){
		//				infoHaveImage2.setVisibility(View.GONE);
		//			}
		//			taskBuildingLayout2.setVisibility(View.VISIBLE);
		//			taskLastTwoTitleLayout.setVisibility(View.VISIBLE);
		//		}
		//		taskLastTwoLayout.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				Tools.getLog(Tools.d, "aaa", "进入点击事件！");
		//				Intent in = new Intent(mContext, ClockGroupInfoActivity.class);
		//				in.putExtra("buildingId", lastTwoJobBean.getLastThrees().get(0).getBuildingId());
		//				mContext.startActivity(in);
		//
		//			}
		//		});
		taskNameLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Tools.getLog(Tools.d, "aaa", "绿条+++++++++++++++++++++点击更多进入buliding页！");
				Intent in = new Intent(mContext, ClockGroupInfoFragmentActivity.class);
				in.putExtra("buildingId", lastTwoJobBean.getBuildingId());
				mContext.startActivity(in);
			}
		});
		taskLastTwoTitleLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Tools.getLog(Tools.d, "aaa", "点击更多进入buliding页！");
				Intent in = new Intent(mContext, ClockGroupInfoFragmentActivity.class);
				in.putExtra("buildingId", lastTwoJobBean.getBuildingId());
				mContext.startActivity(in);
			}
		});

		//		taskBuildingLayout1.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				Tools.getLog(Tools.d, "aaa", "进入点击事件！1111111");
		//				Intent in = new Intent(mContext, ClockGroupInfoActivity.class);
		//				in.putExtra("buildingId", lastTwoJobBean.getLastThrees().get(0).getBuildingId());
		//				mContext.startActivity(in);
		//			}
		//		});
		//		taskBuildingLayout2.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				Tools.getLog(Tools.d, "aaa", "进入点击事件！22222222");
		//				Intent in = new Intent(mContext, ClockGroupInfoActivity.class);
		//				in.putExtra("buildingId", lastTwoJobBean.getLastThrees().get(0).getBuildingId());
		//				mContext.startActivity(in);
		//			}
		//		});
		//		taskBuildingMoreLayout.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				Tools.getLog(Tools.d, "aaa", "进入点击事件！moreMore");
		//				Intent in = new Intent(mContext, ClockGroupInfoActivity.class);
		//				in.putExtra("buildingId", lastTwoJobBean.getLastThrees().get(0).getBuildingId());
		//				mContext.startActivity(in);
		//			}
		//		});
	}
	public void isHaveBottomList(int num){
		//		if(num == 0){
		//			taskinfoBottomListLayout.setVisibility(View.GONE);
		//		}else{
		//			taskinfoBottomListLayout.setVisibility(View.VISIBLE);
		//		}

	}

	@Override
	public void clickDate(CustomDate date, int row) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMesureCellHeight(int cellSpace, int curRow) {
		// TODO Auto-generated method stub
		caledarView.viewPager.getLayoutParams().height = BaseActivity.ScreenWidth * 6/7;
		chartingView.getLayoutParams().height = (int) (BaseActivity.ScreenWidth * 6/7 + 20*BaseActivity.DENSITY);
	}

	@Override
	public void changeDate(CustomDate date, int row) {
		// TODO Auto-generated method stub
		taskCalendarText.setText(date.getYear()+"-"+date.getMonth());
	}

	@Override
	public void startVibrator() {
		// TODO Auto-generated method stub

	}
}
