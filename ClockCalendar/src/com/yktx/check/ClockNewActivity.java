package com.yktx.check;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;
import com.yktx.check.adapter.SearchBuildingAdapter;
import com.yktx.check.bean.AlarmBean;
import com.yktx.check.bean.BasicInfoBean;
import com.yktx.check.bean.CreateTaskBean;
import com.yktx.check.bean.GetCategoryBean;
import com.yktx.check.bean.MoreAlertTimeBean;
import com.yktx.check.bean.RecommendBean;
import com.yktx.check.bean.SearchBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.fragment.ClockNewBuildingFragment;
import com.yktx.check.fragment.ClockNewBuildingFragment.GOTopButton;
import com.yktx.check.fragment.ClockNewBuildingFragment.listenerSetHeght;
import com.yktx.check.fragment.ClockNewBuildingFragment.onClickAdd;
import com.yktx.check.service.Service;
import com.yktx.check.util.CallAlarmUtil;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.CustomRelativeLayout;
import com.yktx.check.util.CustomRelativeLayout.OnSizeChangedListener;
import com.yktx.check.util.Tools;
import com.yktx.check.widget.ListViewAndScrollView;
import com.yktx.check.widget.OldPagerSlidingTabStrip;
import com.yktx.check.widget.PagerSlidingTabStrip;
import com.yktx.sqlite.DBHelper;

public class ClockNewActivity extends FragmentActivity implements
ServiceListener {

	Calendar canlendar = Calendar.getInstance();

	private ImageView mNewBack, mTitleRightImage;
	private EditText mNewState;
	/**
	 * 选中搜索项
	 */
	boolean isSelectSearch;
	CustomRelativeLayout rl;

	EditText mNewInput;
	//	private RangeSeekBar<Integer> mRangeSeekBar;
	//	private TextView mMinText,mMaxText;
	private boolean isStart;
	private int conTime;
	private int isSee;// 是否隐私, 1为隐私, 0为不隐私
	private int isCheckTime;// 是否限时, 1为限时, 0为不限时
	private int[] colorArray = { R.color.clock_new_image1,
			R.color.clock_new_image2, R.color.clock_new_image3 };
	private String[] colorStringArray = { "E9CD69", "81e6da", "B0E2FF" };
	int colorNum;
	private String CoverUrl;
	private List<RecommendBean> recommendBeans;
	private List<List<RecommendBean>> lists;
	private int TIMECODE = 123;
	private int LIMITCODE = 124;
	public final static int FINISHCODE = 125;
	public static OldPagerSlidingTabStrip new_tabs;
	private ViewPager new_pager;
	private Context mContext;
	public DisplayMetrics mDisplayMetrics;
	ArrayList<GetCategoryBean> getCategoryBeanList = new ArrayList<GetCategoryBean>();
	String beginTime, endTime;// 开始，结束时间
	private LinearLayout new_limitTimeLayout, new_alertTimeLayout;
	private TextView new_alertTimeText, new_limitTimeText, mNewSuccess,
	new_inputStateText, mTitleContent,new_isVisibleText,snakbarTitle;
	private ImageView new_alertTimeImage, new_limitTimeImage,
	new_isVisibleImage;

	ListViewAndScrollView searchListView;
	SearchBuildingAdapter searchBuildingAdapter;

	public ScrollView parentScrollView;
	public ImageLoader imageLoader = ImageLoader.getInstance();
	public DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.toumingimg)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	.displayer(new RoundedBitmapDisplayer(5)).cacheInMemory(false)
	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
	int type = 1;
	private String userID;
	private SharedPreferences settings;
	private Editor mEditor;
	private BasicInfoBean basicInfoBean;
	private boolean isVisity;
	ArrayList<SearchBean> searchBeanList;
	private RelativeLayout mTitleLayout,snakbarLayout;
	private boolean isAdd;
	//	DBHelper dbHelper;
	String ClockTitle;

	public boolean isInput;
	private View new_line;

	boolean isSuccess;//判断完成不可重复点击
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		findViews();
		init();
		setListeners();
		ConnGetCategory();
	}

	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_clock_new);
		rl = (CustomRelativeLayout)findViewById(R.id.relativeLayout);
		rl.setOnSizeChangedListener(new OnSizeChangedListener() {

			@Override
			public void onSizeChanged(int w, int h, int oldw, int oldh) {
				// TODO Auto-generated method stub
				Tools.getLog(Tools.i, "aaa", "w =============== "+w);
				Tools.getLog(Tools.i, "aaa", "h =============== "+h);
				Tools.getLog(Tools.i, "aaa", "oldw =============== "+oldw);
				Tools.getLog(Tools.i, "aaa", "oldh =============== "+oldh);
				if(h > oldh){
					//关闭软键盘
					if(searchListView.getVisibility() == View.VISIBLE){
						searchListView.setVisibility(View.GONE);
					}
				}
			}
		});
		mContext = this;
		//		dbHelper = new DBHelper(mContext);
		settings = getSharedPreferences("clock", MODE_PRIVATE);
		mEditor = settings.edit();
		userID = settings.getString("userid", null);
		basicInfoBean = (BasicInfoBean) getIntent().getSerializableExtra("bean");
		lists = new ArrayList<List<RecommendBean>>();
		mDisplayMetrics = getResources().getDisplayMetrics();
		mNewBack = (ImageView) findViewById(R.id.title_item_leftImage);
		mTitleContent = (TextView) findViewById(R.id.title_item_content);
		mTitleRightImage = (ImageView) findViewById(R.id.title_item_rightImage);
		mNewInput = (EditText) findViewById(R.id.new_inputClock);
		mNewState = (EditText) findViewById(R.id.new_inputState);
		mNewSuccess = (TextView) findViewById(R.id.title_item_rightText);
		searchListView = (ListViewAndScrollView) findViewById(R.id.searchList);
		mTitleLayout = (RelativeLayout) findViewById(R.id.title_item_layout);

		//新改的
		new_limitTimeLayout = (LinearLayout) findViewById(R.id.new_limitTimeLayout);
		new_alertTimeLayout = (LinearLayout) findViewById(R.id.new_alertTimeLayout);
		new_alertTimeText  = (TextView) findViewById(R.id.new_alertTimeText);
		new_limitTimeText  = (TextView) findViewById(R.id.new_limitTimeText);
		new_alertTimeImage = (ImageView) findViewById(R.id.new_alertTimeImage);
		new_limitTimeImage = (ImageView) findViewById(R.id.new_limitTimeImage);
		new_isVisibleImage = (ImageView) findViewById(R.id.new_isVisibleImage);
		new_inputStateText = (TextView) findViewById(R.id.new_inputStateText);
		new_isVisibleText = (TextView) findViewById(R.id.new_isVisibleText);

		snakbarLayout = (RelativeLayout) findViewById(R.id.snakbarLayout);
		snakbarTitle = (TextView) findViewById(R.id.snakbarTitle);
		new_line = findViewById(R.id.new_line);

		new_tabs = (OldPagerSlidingTabStrip) findViewById(R.id.tabs);
		new_pager = (ViewPager) findViewById(R.id.pager);
		parentScrollView = (ScrollView) findViewById(R.id.parentScrollView);
		//		parentScrollView.setVerticalScrollBarEnabled(true);//不活动的时候隐藏，活动的时候也隐藏
	}

	public static int pagerHeight;
	boolean isFirst = true;


	private void initTab(String[] titles){
		new_pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),titles));
		new_tabs.setViewPager(new_pager);
		setTabsValue();
		ViewTreeObserver vto = new_tabs.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				if (isFirst) {
					isFirst = false;
					pagerHeight = (int) (settings.getInt("allHeight", 0) - new_tabs.getHeight() - 48*BaseActivity.DENSITY);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, pagerHeight);
					Tools.getLog(Tools.i, "bbb", "pagerHeight =========== " + pagerHeight);
					new_pager.setLayoutParams(params);
				}
			}
		});

	}
	public void SuccessType(boolean isOK){
		if(isOK){
			mNewInput.setBackgroundResource(R.drawable.clock_new_toplayout_shape);
			mNewInput.setTextColor(getResources().getColor(R.color.white));
			mNewSuccess.setTextColor(getResources().getColor(R.color.meibao_color_1));
			new_line.setVisibility(View.GONE);
		}else{
			mNewInput.setBackgroundResource(R.drawable.toumingimg);
			mNewInput.setTextColor(getResources().getColor(R.color.meibao_color_11));
			mNewInput.setHintTextColor(getResources().getColor(R.color.meibao_color_11));
			mNewSuccess.setTextColor(getResources().getColor(R.color.meibao_color_11));
			new_line.setVisibility(View.VISIBLE);
		}
	}
	public void init() {
		// TODO Auto-generated method stub
		// mMinText.setText(getTime(0));
		// mMaxText.setText(getTime(1440));
		// getBuildingConn(type);

		//		mNewInput.setFocusable(true);   
		//		mNewInput.setFocusableInTouchMode(true);   
		//		mNewInput.requestFocus();  
		//		
		//		mNewState.setFocusable(true);   
		//		mNewState.setFocusableInTouchMode(true);   
		//		mNewState.requestFocus();  
		TaskID =  Tools.getUuid();
		mTitleLayout.setBackgroundColor(getResources().getColor(R.color.meibao_color_15));
		timer = new Timer();
		searchBuildingAdapter = new SearchBuildingAdapter(this);
		// mNewInput.setAdapter(searchBuildingAdapter);
		searchListView.setAdapter(searchBuildingAdapter);
		searchListView.setScrollView(parentScrollView);
		mNewSuccess.setText("完成");
		//		mNewSuccess.setTextColor(getResources().getColor(R.color.meibao_color_11));
		SuccessType(false);//取消变色

		mTitleRightImage.setVisibility(View.GONE);
		mTitleContent.setText("新建打卡");
		if (basicInfoBean != null) {
			//			mNewSuccess.setTextColor(getResources().getColor(
			//					R.color.meibao_color_1));//完成变色
			SuccessType(true);//完成变色
			//			mNewInput.setEnabled(false); //输入框不可编辑
			mNewInput.setText(basicInfoBean.getTitle());
			mNewState.setText(basicInfoBean.getDescription());

			if (basicInfoBean.getTime_limit_flag().equals("1")) {
				isCheckTime = 1;
				new_limitTimeText.setText(basicInfoBean.getBegin_time() + "-"
						+ basicInfoBean.getEnd_time());
				new_limitTimeImage.setImageResource(R.drawable.clock_new_limit);
				new_limitTimeText.setVisibility(View.VISIBLE);
			} else {
				isCheckTime = 0;
				new_limitTimeText.setText("");
				new_limitTimeImage
				.setImageResource(R.drawable.clock_new_unlimit);
				new_limitTimeText.setVisibility(View.GONE);
			}
		}
		if(alertTimeBeans.size() == 0){
			MoreAlertTimeBean alertTimeBean = new MoreAlertTimeBean();
			alertTimeBean.setId(Tools.getUuid());
			alertTimeBean.setTaskId(TaskID);
			long currentTime = System.currentTimeMillis();
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			Date date = new Date(currentTime);
			alertTimeBean.setTime(formatter.format(date));
			alertTimeBean.setStatus("1");
			alertTimeBean.setPickervisity("2");
			alertTimeBeans.add(alertTimeBean);
			MobclickAgent.onEvent(mContext, "clicksetalarm");//默认设置提醒一次
		}
		new_alertTimeImage
		.setImageResource(R.drawable.clock_new_alert);
		new_alertTimeText.setVisibility(View.VISIBLE);
		new_alertTimeText.setText(alertTimeBeans.get(0).getTime());
		snakbarLayout.setVisibility(View.INVISIBLE);
	}

	public int getStatusBarHeight() {
		return Resources.getSystem().getDimensionPixelSize(
				Resources.getSystem().getIdentifier("status_bar_height",
						"dimen", "android"));
	}

	String searchStr;
	Timer timer;
	private static final int TIMER_SEARCH = 102;
	private final int SearchTime = 1000;

	public void setListeners() {
		// TODO Auto-generated method stub
		mNewInput.addTextChangedListener(new TextWatcher() {

			private int selectionStart;
			private int selectionEnd;
			private CharSequence temp;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				isInput = true;
				if (isSelectSearch) {
					isSelectSearch = false;
					return;
				} else {
					int infoNum = temp.length();
					if (infoNum == 0) {
						SuccessType(false);//取消变色
					} else {
						SuccessType(true);//完成变色
					}
					searchStr = mNewInput.getText().toString();
					selectionStart = mNewInput.getSelectionStart();
					selectionEnd = mNewInput.getSelectionEnd();
					final int textLength = mNewInput.getText().length();
					if (Tools.getLineSize(searchStr) > 30) {
						Toast.makeText(ClockNewActivity.this, "字数超限",
								Toast.LENGTH_SHORT).show();
						s.delete(selectionStart - 1, selectionEnd);
						int tempSelection = selectionStart;
						mNewInput.setText(s);
						mNewInput.setSelection(tempSelection);
					}

					timer.cancel();
					timer = new Timer();
					/**
					 * 延时一秒执行
					 */
					timer.schedule(new TimerTask() {
						public void run() {
							Message msg = new Message();
							msg.what = TIMER_SEARCH;
							msg.arg1 = textLength;
							mHandler.sendMessage(msg);
						}
					}, SearchTime);
				}
			}
		});

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
					Toast.makeText(mContext,
							"你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
							.show();
					s.delete(editStart-1, editEnd);

					temp = s;
					infoNum = temp.length();
					mNewState.setText(temp);
					mNewState.setSelection(infoNum);
				}
				new_inputStateText.setText(temp.length()+"/200");
			}
		});
		new_limitTimeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(mContext, "clicklimit");
				Intent  in = new Intent(mContext, ClockLimitActivity.class);
				String time = new_limitTimeText.getText().toString();
				if(time !=null){
					in.putExtra("limittime", time);
				}
				startActivityForResult(in, LIMITCODE);
			}
		});
		new_alertTimeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(mContext, "clickalarm");
				Intent  in = new Intent(mContext, ClockMoreAlertActivity.class);
				//				String time = new_alertTimeText.getText().toString();
				//				if(time !=null){
				//					in.putExtra("ischeck", time);
				//				}
				in.putExtra("taskid", TaskID);
				in.putExtra("onlist", alertTimeBeans);
				startActivityForResult(in, TIMECODE);
				//				Intent  in = new Intent(mContext, ClockMoreAlertActivity.class);
				//				startActivity(in);
			}
		});
		new_isVisibleImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(mContext, "clickvisity");
				if(isVisity){
					isVisity = false;
					new_isVisibleImage.setImageResource(R.drawable.clock_new_unvisible);
					new_isVisibleText.setText("");
				}else{
					showVisibleDialog();
				}
			}
		});
		mNewBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

			}
		});
		mNewSuccess.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isSuccess){
					return;
				}
				ClockTitle = mNewInput.getText().toString();
				String ClockState = mNewState.getText().toString();
				Toast toast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
				if(ClockTitle == null||ClockTitle.length()==0){
					toast.setText("打卡标题不可为空！");
					toast.show();
					isSuccess = false;
					return ;
				}
				if(isInput){
					MobclickAgent.onEvent(mContext, "newtitleinput");
				}else{
					MobclickAgent.onEvent(mContext, "newtitleadd");
				}

				Tools.getLog(Tools.d, "aaa", "ClockTitle=" + ClockTitle);
				Tools.getLog(Tools.d, "aaa", "ClockState=" + ClockState);
				Tools.getLog(Tools.d, "aaa", "CoverUrl=" + CoverUrl);



				//					long dayTime = 1000L * 60 * 60 * 24;
				//					String arrayTime[] = tixingTime.split(":");
				//					int hour = Integer.parseInt(arrayTime[0]);
				//					int minute = Integer.parseInt(arrayTime[1]);
				//
				//					canlendar.setTimeInMillis(System.currentTimeMillis());
				//					canlendar.set(Calendar.HOUR_OF_DAY, hour);
				//					canlendar.set(Calendar.MINUTE, minute);
				//					canlendar.set(Calendar.SECOND, 0);
				//					canlendar.set(Calendar.MILLISECOND, 0);
				//
				//					Intent intent = new Intent(ClockNewActivity.this,
				//							CallAlarm.class);
				//					PendingIntent sender = PendingIntent.getBroadcast(
				//							ClockNewActivity.this, 1, intent, 0);
				//					/*
				//					 * setRepeating() 可让闹钟重复执行
				//					 */
				//					AlarmManager am;
				//					am = (AlarmManager) getSystemService(ALARM_SERVICE);
				//					am.setRepeating(AlarmManager.RTC_WAKEUP,
				//							canlendar.getTimeInMillis(), dayTime, sender);
				//					/* 更新显示的设定闹钟时间 */
				Conn(TaskID,ClockTitle, ClockState ,isVisity?"1":"0",isCheckTime+"",beginTime,endTime);
			}

		});

		searchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				isInput = false;
				isSelectSearch = true;
				SearchBean searchBean = searchBeanList.get(arg2);
				mNewInput.setText(searchBean.getTitle());
				if(isVisity){
					isVisity = false;
					new_isVisibleImage.setImageResource(R.drawable.clock_new_unvisible);
				}

				//				GetAlarmList(searchBean.getTaskId());//不把提醒同步过来
				mNewState.setText(searchBean.getDescription());
				if (searchBean.getTime_limit_flag().equals("1")) {
					isCheckTime = 1;
					new_limitTimeText.setText(searchBean.getBegin_time() + "-"
							+ searchBean.getEnd_time());
					new_limitTimeImage.setImageResource(R.drawable.clock_new_limit);
					new_limitTimeText.setVisibility(View.VISIBLE);
				} else {
					isCheckTime = 0;
					new_limitTimeText.setText("");
					new_limitTimeImage
					.setImageResource(R.drawable.clock_new_unlimit);
					new_limitTimeText.setVisibility(View.GONE);
				}
				SuccessType(true);//完成变色
				searchListView.setVisibility(View.GONE);
				ClockApplication.closeKeybord(mNewInput, mContext);
			}
		});
	}


	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		new_tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		new_tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		new_tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, mDisplayMetrics));
		// 设置Tab Indicator的高度
		new_tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, mDisplayMetrics));
		//设置Tab标题文字的大小（默认的） 
		new_tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 14, mDisplayMetrics));
		//设置选中的文字的大小
		new_tabs.setSelectTextSice((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, mDisplayMetrics));
		// 设置Tab Indicator的颜色
		new_tabs.setIndicatorColor(getResources().getColor(R.color.meibao_color_1));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		new_tabs.setSelectedTextColor(getResources().getColor(R.color.meibao_color_10));
		//设置没有选中的文字颜色
		new_tabs.setTextColorResource(R.color.meibao_color_10);
		// 取消点击Tab时的背景色
		new_tabs.setTabBackground(0);
	}

	public void GetAlarmList(String taskid){
		String str = "?userId="+userID+"&taskId="+taskid;
		Service.getService(Contanst.HTTP_ALARM_GETBYRASKID, null, str,
				ClockNewActivity.this).addList(null).request(UrlParams.GET);
	}
	public void ConnGetCategory(){
		String str = "?userId="+userID;
		Service.getService(Contanst.HTTP_GET_CATEGORY, null, str,
				ClockNewActivity.this).addList(null).request(UrlParams.GET);
	}

	public void ConnSearch(String keyWord) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("keyWord", keyWord));
			params.add(new BasicNameValuePair("userId", userID));
		} catch (Exception e) {

		}
		Service.getService(Contanst.HTTP_SEARCH, null, null,
				ClockNewActivity.this).addList(params).request(UrlParams.POST);
	}

	public String TaskID,addTaskid,addTitle;

	public void Conn(String taskid,String title, String state,String privateFlag,String timeLimitFlag,String beginTimes,String endTimes) {
		isSuccess = true;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("taskId", taskid));// UUID.randomUUID().toString())
			params.add(new BasicNameValuePair("userId", userID));// UUID.randomUUID().toString())
			params.add(new BasicNameValuePair("title", title));
			params.add(new BasicNameValuePair("description", state));
			//			if(isVisity){
			//				params.add(new BasicNameValuePair("privateFlag", "1"));
			//			}else{
			//				params.add(new BasicNameValuePair("privateFlag", "0"));
			//			}
			params.add(new BasicNameValuePair("privateFlag", privateFlag));
			params.add(new BasicNameValuePair("autoShareFlag", 0+""));
			//			if(settings.getBoolean("isWeiboOpen", false)){
			//				params.add(new BasicNameValuePair("autoShareFlag", 1+""));
			//			} else {
			//				params.add(new BasicNameValuePair("autoShareFlag", 0+""));
			//			}
			params.add(new BasicNameValuePair("timeLimitFlag", timeLimitFlag));
			if(timeLimitFlag.equals("1")){
				params.add(new BasicNameValuePair("beginTime", beginTimes));
				params.add(new BasicNameValuePair("endTime", endTimes));
			}
			params.add(new BasicNameValuePair("alertTime", new_alertTimeText.getText().toString()));
			params.add(new BasicNameValuePair("cover", CoverUrl));
			params.add(new BasicNameValuePair("onlineFlag", 1+""));
			//			params.add(new BasicNameValuePair("offLineCTime", ""));
		} catch (Exception e) {

		}
		Service.getService(Contanst.HTTP_CREATETASK, null, null,
				ClockNewActivity.this).addList(params).request(UrlParams.POST);
	}

	public void AddAlertConn(String taskid, ArrayList<MoreAlertTimeBean> list){
		Gson gson = new Gson();
		String str = gson.toJson(list);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("taskId", taskid));// UUID.randomUUID().toString())
			params.add(new BasicNameValuePair("userId", userID));
			params.add(new BasicNameValuePair("alarmListJson", str));
		} catch (Exception e) {

		}
		Service.getService(Contanst.HTTP_ALERT_CREATE, null, null,
				ClockNewActivity.this).addList(params).request(UrlParams.POST);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Tools.getLog(Tools.i, "aaa", "keyCode ---------- " + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Tools.getLog(Tools.i, "aaa", "KEYCODE_BACKKEYCODE_BACKKEYCODE_BACK");
			if (searchListView.getVisibility() == View.VISIBLE) {
				searchListView.setVisibility(View.GONE);
				return true;
			}


		}
		return super.onKeyDown(keyCode, event);
	}
	private static String getTime(int hour,int minute){
		String hours = hour+"";
		String minutes = minute+"";
		if(hours.length() == 1){
			hours = 0+hours;
		}
		if(minutes.length() == 1){
			minutes = 0+minutes;
		}
		return hours+":"+minutes;
	}

	@Override
	public void getJOSNdataSuccess(Object bean, String sccmsg, int connType) {
		// TODO Auto-generated method stub
		Tools.getLog(Tools.i, "aaa", "===========getJOSNdataSuccess=============");
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_OK;
		msg.obj = bean;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}

	@Override
	public void getJOSNdataFail(String errcode, String errmsg, int connType) {
		// TODO Auto-generated method stub
		Tools.getLog(Tools.i, "aaa", "==========getJOSNdataFail===========");
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_FAIL;
		msg.obj = errmsg;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.GETRECOMMEND:
					recommendBeans = (List<RecommendBean>) msg.obj;
					lists.add(recommendBeans);
					type++;
					if (type > 3) {
						return;
					}
					// getBuildingConn(type);
					break;
				case Contanst.CREATETASK:
					CreateTaskBean bean = (CreateTaskBean) msg.obj;
					//					ArrayList<AlarmBean> alarmList = ClockApplication.getInstance().getAlarmList();
					for(int i = 0;i<alertTimeBeans.size();i++){
						alertTimeBeans.get(i).setTitle(ClockTitle);
						if(alertTimeBeans.get(i).getStatus().equals("1")){
							AlarmBean alarmBean = new AlarmBean();
							alarmBean.setAlarmIndex(i);
							alarmBean.setAlarmTime(alertTimeBeans.get(i).getTime());
							alarmBean.setTastID(TaskID);
							alarmBean.setTastTitle(ClockTitle);
							//							alarmList.add(alarmBean);
							CallAlarmUtil.setAlarm(ClockNewActivity.this, alarmBean);
						}
					}

					//					dbHelper.addAlarmList(alertTimeBeans);
					if(isAdd){
						AddAlertConn(addTaskid,addAlertTimeBeans);
					}else{
						AddAlertConn(TaskID,alertTimeBeans);
					}

					//					alertTimeBeans = dbHelper.getAlarmList(TaskID);
					Tools.getLog(Tools.d, "aaa", "存入的数据:"+alertTimeBeans.toString());
					//					ClockApplication.getInstance().setAlarmList(alarmList);
					Tools.getLog(Tools.d, "aaa", "CreateTaskBean:" + bean);
					setResult(FINISHCODE);
					ClockMainActivity.isBackToday = true;

					break;
				case Contanst.GET_CATEGORY:

					getCategoryBeanList = (ArrayList<GetCategoryBean>) msg.obj;
					String[] titles = new String[getCategoryBeanList.size()];
					for(int i = 0; i < getCategoryBeanList.size(); i++){
						titles[i] = getCategoryBeanList.get(i).getTitle();
						Tools.getLog(Tools.i, "bbb", "titles ============== "+titles[i]);
					}
					initTab(titles);
					break;

				case Contanst.SEARCH:
					searchBeanList = (ArrayList<SearchBean>) msg.obj;
					if (searchBeanList.size() > 0) {
						searchListView.setVisibility(View.VISIBLE);
						searchBuildingAdapter.setList(searchBeanList);
						searchBuildingAdapter.setSearchStr(searchStr);
						searchBuildingAdapter.notifyDataSetChanged();
					} else {
						searchListView.setVisibility(View.GONE);
					}

					break;
				case Contanst.ALERT_CREATE:
					Tools.getLog(Tools.d, "aaa", "=======添加数据成功!=====");
					if(isAdd){
						snakbarTitle.setText("已将#"+addTitle+"#添加到主页的打卡列表中");
						animAlertStart();

					}else{
						finish();
					}

					break;
				case Contanst.ALARM_GETBYRASKID:
					ArrayList<MoreAlertTimeBean> beans = (ArrayList<MoreAlertTimeBean>) msg.obj;
					Tools.getLog(Tools.d, "aaa", "获得的alarm："+beans.toString());
					int alarmNum = 0;
					for(int i = 0; i<beans.size(); i++){
						if(beans.get(i).getStatus().equals("1")){
							alarmNum ++;
						}
					}

					new_alertTimeText.setText(alarmNum+"");
					if(alarmNum == 0){
						new_alertTimeImage
						.setImageResource(R.drawable.clock_new_unalert);
						new_alertTimeText.setVisibility(View.GONE);
					}else{
						new_alertTimeImage
						.setImageResource(R.drawable.clock_new_alert);
						new_alertTimeText.setVisibility(View.VISIBLE);
					}
					break;
				}

				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
				switch (msg.arg1) {
				case Contanst.GETRECOMMEND:
					Tools.getLog(Tools.d, "aaa", "GETRECOMMEND:"+message);
					break;
				case Contanst.CREATETASK:
					break;
				}

				break;
			case TIMER_SEARCH:

				if (msg.arg1 > 0) {
					ConnSearch(searchStr);
				}
				break;
			}
		}
	};

	String tixingTime;
	ArrayList<MoreAlertTimeBean> alertTimeBeans = new ArrayList<MoreAlertTimeBean>();
	ArrayList<MoreAlertTimeBean> addAlertTimeBeans = new ArrayList<MoreAlertTimeBean>();
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Tools.getLog(Tools.i, "aaa", "Activity.RESULT_OK === "
				+ Activity.RESULT_OK);
		Tools.getLog(Tools.i, "aaa", "requestCode === " + resultCode);
		Bitmap bitmap = null;

		if (requestCode == TIMECODE) {
			if (resultCode == RESULT_OK) {
				//				alertTimeBeans = dbHelper.getAlarmList(TaskID);
				alertTimeBeans = (ArrayList<MoreAlertTimeBean>) data.getSerializableExtra("list");
				int fristposition = 0;
				int alarmNum = 0;
				for(int i = 0; i<alertTimeBeans.size(); i++){
					if(alertTimeBeans.get(i).getStatus().equals("1")){
						alarmNum ++;
						if(alarmNum == 1){
							fristposition = i;
						}
					}
				}

				new_alertTimeText.setText(alarmNum+"");
				if(alarmNum == 0){
					new_alertTimeImage
					.setImageResource(R.drawable.clock_new_unalert);
					new_alertTimeText.setVisibility(View.GONE);
				}else if(alarmNum == 1){
					new_alertTimeImage
					.setImageResource(R.drawable.clock_new_alert);
					new_alertTimeText.setVisibility(View.VISIBLE);
					new_alertTimeText.setText(alertTimeBeans.get(fristposition).getTime());
				}else{
					new_alertTimeImage
					.setImageResource(R.drawable.clock_new_alert);
					new_alertTimeText.setVisibility(View.VISIBLE);
				}

				//						tixingTime = data.getStringExtra("time");
				//				new_alertTimeText.setText(tixingTime);
				//				if (tixingTime != null && tixingTime.length() != 0) {
				//					new_alertTimeImage
				//					.setImageResource(R.drawable.clock_new_alert);
				//					new_alertTimeText.setVisibility(View.VISIBLE);
				//				}else {
				//					Tools.getLog(Tools.i, "aaa", "dasdasdasdasd=================");
				//					tixingTime = null;
				//					new_alertTimeImage
				//					.setImageResource(R.drawable.clock_new_unalert);
				//					new_alertTimeText.setVisibility(View.GONE);
				//				}


			} 
		} else if (requestCode == LIMITCODE) {
			if (resultCode == RESULT_OK) {
				String time = data.getStringExtra("limittime");
				if (time != null) {
					isCheckTime = 1;
					new_limitTimeText.setText(time);
					String[] arrayTime = time.split("-");
					beginTime = arrayTime[0];
					endTime = arrayTime[1];
					new_limitTimeImage
					.setImageResource(R.drawable.clock_new_limit);
					new_limitTimeText.setVisibility(View.VISIBLE);
				}else {
					isCheckTime = 0;
					new_limitTimeText.setText("");
					new_limitTimeImage
					.setImageResource(R.drawable.clock_new_unlimit);
					new_limitTimeText.setVisibility(View.GONE);
				}
			} 
		}
	}

	ClockNewBuildingFragment fragment[];

	public class MyPagerAdapter extends FragmentPagerAdapter {
		private String[] titles;

		public MyPagerAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			this.titles = titles;
			fragment = new ClockNewBuildingFragment[titles.length];
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			if (fragment[position] == null) {
				fragment[position] = new ClockNewBuildingFragment(
						getCategoryBeanList.get(position).getId(),
						parentScrollView, goTopButton,userID);

			}
			fragment[position].setClickAdd(clickAdd);
			return fragment[position];

		}

	}

	GOTopButton goTopButton = new GOTopButton() {

		@Override
		public void getTopBottom(String title) {
			isSelectSearch = true;
			mNewInput.setText(title);
			Tools.getLog(Tools.i, "aaa", "aaaaaaaaaaaaaaaaaa");
			// TODO Auto-generated method stub
			for (int i = 0; i < fragment.length; i++) {
				if (fragment[i] != null)
					fragment[i].listView.setSelection(0);
			}
			parentScrollView.scrollTo(0, 0);
		}

	};
	/** 下面切换的添加。。*/
	onClickAdd clickAdd = new onClickAdd() {

		@Override
		public void clickAdd(RecommendBean recommendBean) {
			// TODO Auto-generated method stub
			//			isSelectSearch = true;
			isInput = false;
			isAdd = true;
			addTaskid = Tools.getUuid();
			addTitle = recommendBean.getTitle();
			addAlertTimeBeans = new ArrayList<MoreAlertTimeBean>();
			MoreAlertTimeBean alertTimeBean = new MoreAlertTimeBean();
			alertTimeBean.setId(Tools.getUuid());
			alertTimeBean.setTaskId(addTaskid);
			long currentTime = System.currentTimeMillis();
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			Date date = new Date(currentTime);
			alertTimeBean.setTime(formatter.format(date));
			alertTimeBean.setStatus("1");
			alertTimeBean.setPickervisity("2");
			addAlertTimeBeans.add(alertTimeBean);
			MobclickAgent.onEvent(mContext, "clicksetalarm");//默认设置提醒一次
			Conn(addTaskid, addTitle, recommendBean.getDescription(),"0" ,recommendBean.getTime_limit_flag(),
					recommendBean.getBegin_time(),recommendBean.getEnd_time());

			//			GetAlarmList(recommendBean.getId());
			//			if(isVisity){
			//				isVisity = false;
			//				new_isVisibleImage.setImageResource(R.drawable.clock_new_unvisible);
			//				new_isVisibleText.setText("");
			//			}
			//			mNewInput.setText(recommendBean.getTitle());
			//			mNewState.setText(recommendBean.getDescription());
			//			if (recommendBean.getTime_limit_flag().equals("1")) {
			//				isCheckTime = 1;
			//				new_limitTimeText.setText(recommendBean.getBegin_time() + "-"
			//						+ recommendBean.getEnd_time());
			//				new_limitTimeImage.setImageResource(R.drawable.clock_new_limit);
			//				new_limitTimeText.setVisibility(View.VISIBLE);
			//			} else {
			//				isCheckTime = 0;
			//				new_limitTimeText.setText("");
			//				new_limitTimeImage
			//				.setImageResource(R.drawable.clock_new_unlimit);
			//				new_limitTimeText.setVisibility(View.GONE);
			//			}
			//			mNewSuccess.setTextColor(getResources().getColor(
			//					R.color.meibao_color_1));
			//			parentScrollView.scrollTo(0, 0);
		}
	};

	listenerSetHeght setHeght = new listenerSetHeght() {

		@Override
		public void setHeight(int height) {
			// TODO Auto-generated method stub
		}
	};
	public void showVisibleDialog(){
		new AlertDialog.Builder(mContext)
		.setTitle("隐私设置")
		.setMessage("设置为隐私后，所有打卡信息\n仅能被自己看到")
		.setPositiveButton("设为隐私", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(mContext, "clicksetvisity");
				isVisity = true;
				new_isVisibleImage.setImageResource(R.drawable.clock_new_visible);
				new_isVisibleText.setText("已隐私");
			}
		})
		.setNegativeButton("返回", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		}).show();
		//		final TaskVisityDialog taskVisityDialog = new TaskVisityDialog(ClockNewActivity.this);
		//		taskVisityDialog.show();
		//		taskVisityDialog.mCancel.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				taskVisityDialog.dismiss();
		//			}
		//		});
		//		taskVisityDialog.mResult.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				isVisity = true;
		//				new_isVisibleImage.setImageResource(R.drawable.clock_new_visible);
		//				taskVisityDialog.dismiss();
		//			}
		//		});


	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
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
		MobclickAgent.onResume(this);
	}
	public void animAlertStart() {
		Tools.getLog(Tools.i, "aaa", "开始动画：");
		int height = snakbarLayout.getHeight();
		Tools.getLog(Tools.i, "aaa", "animAlertStart height ============= "
				+ height);
		TranslateAnimation animationStart = new TranslateAnimation(0, 0,
				height, 0);

		animationStart.setDuration(500L);// 设置动画持续时间
		snakbarLayout.startAnimation(animationStart);
		animationStart.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				snakbarLayout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				snakbarLayout.setVisibility(View.VISIBLE);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int height = snakbarLayout.getHeight();
						Animation mAnimation = new TranslateAnimation(0, 0, 0,
								height);
						mAnimation.setDuration(250L);
						// building_dialog_Layout.setAnimation(mAnimation);
						snakbarLayout.startAnimation(mAnimation);

						mAnimation.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation arg0) {
								// TODO Auto-generated method stub
								snakbarLayout
								.setVisibility(View.VISIBLE);
							}

							@Override
							public void onAnimationRepeat(Animation arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animation arg0) {
								// TODO Auto-generated method stub
								snakbarLayout
								.setVisibility(View.GONE);

							}
						});
					}
				}, 3000L);

			}
		});

	}


}
