package com.yktx.check;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clock.service.AddShowPhotoService;
import com.clock.service.MyReceiver;
import com.haarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.update.UmengUpdateAgent;
import com.yktx.check.adapter.ClockMainTodayAdapter;
import com.yktx.check.adapter.SlideAdapter;
import com.yktx.check.adapter.SlideAdapter.OnClickButton;
import com.yktx.check.bean.ByDateBean;
import com.yktx.check.bean.CreateJobBean;
import com.yktx.check.bean.CustomDate;
import com.yktx.check.bean.ImageListBean;
import com.yktx.check.bean.MoreAlertTimeBean;
import com.yktx.check.bean.PerformanceBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.bean.UpdateBean;
import com.yktx.check.bean.UserAdBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.dialog.ADDialog;
import com.yktx.check.dialog.AllTaskFinishDialog2;
import com.yktx.check.dialog.SharedDialog;
import com.yktx.check.dialog.TakeClockDialog;
import com.yktx.check.dialog.TakeClockDialog.TaskClockDialogOnCLickClockSuccess;
import com.yktx.check.dialog.TakeClockSuccessDialog;
import com.yktx.check.dialog.TakeClockSuccessDialog.OnCLickSuccessShare;
import com.yktx.check.dialog.TaskMainLongClickDialog;
import com.yktx.check.service.Service;
import com.yktx.check.square.fragment.GroupMainFragmentActivity;
import com.yktx.check.util.CalendarViewBuilder;
import com.yktx.check.util.CallAlarmUtil;
import com.yktx.check.util.CommonUtils;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.DateUtil;
import com.yktx.check.util.FileURl;
import com.yktx.check.util.ImageTool;
import com.yktx.check.util.MyUMSDK;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;
import com.yktx.check.widget.CalendarView;
import com.yktx.check.widget.CalendarView.CallBack;
import com.yktx.check.widget.CalendarViewPagerLisenter;
import com.yktx.check.widget.CustomViewPagerAdapter;
import com.yktx.view.StickyLayout;
import com.yktx.view.StickyLayout.OnGiveUpTouchEventListener;
import com.yktx.view.StickyLayout.OnMoveOver;
import com.yktx.view.TaskCaledarView;

@SuppressLint("NewApi")
public class ClockMainActivity extends BaseActivity implements CallBack,
		OnGiveUpTouchEventListener, ServiceListener, OnMoveOver {
	private ViewPager viewPager;
	String latitude, longitude;
	private CalendarView[] views;
	private TextView showYearView;
	private TextView showMonthView;
	// private TextView showWeekView;
	private CalendarViewBuilder builder = new CalendarViewBuilder();
	private CustomDate mClickDate;
	LinearLayout footerView, createTask, homeShare;

	private ListView mSlideListView;
	private SlideAdapter adapter;
	private ClockMainTodayAdapter clockMainTodayAdapter;

	public static boolean isMove;
	public static boolean isTodayCanSee;
	public boolean isClickToday;
	public ArrayList<ImageListBean> filenames = new ArrayList<ImageListBean>(3);// 相册选取的集合的名字
	public static HashMap<String, String> curMap = new HashMap<String, String>(
			10);
	FeedbackAgent agent;
	// ScrollView scrollView;
	/**
	 * 	打卡存储bean。为了没有联网后失败做存储
	 */
	TaskItemBean taskItemBean;
	boolean isBottomClick;

	CustomViewPagerAdapter<CalendarView> viewPagerAdapter;
	private ArrayList<ByDateBean> byDateBeanList;
	TakeClockDialog dialog;
	private StickyLayout stickyLayout;
	private boolean isMainFrist; // 判断点击事件是不是第一次，因为有初始化的值是 5
	private static OnSetDialogImage mSetImage;

	public static ImageView square_calendar_button;
	public static ImageView xiaoxi_button;

	ImageView mNowCircleView, mMyCircleView;
	String giveUpReason, signature, quantity, unitStr = "0";
	String jobid;
	int CancelCheckType;
	int positioniItemClick;
	private Vibrator vibrator;
	/**
	 * 是否为创建打卡
	 */
	SharedDialog sharedDialog = null;
	public int requestTask = 111;
	/**
	 * 是否上传图片
	 */
	private boolean isUpLoad;
	private TextView shareTitle, clock_main_alertText;
	private ImageView leftImage, shareImage;

	private RelativeLayout clock_main_alertLayout;


	private final String DATECOUNT = "DateCount";
	private final String JOBCOUNT = "jobCount";
	String today;
	private Bitmap mShareImageBitmap;

	public static void setDialogImage(OnSetDialogImage mDialogImage) {
		mSetImage = mDialogImage;
	}
	

	private String taskId,taskTitle;
	CreateJobBean createJobBean;
	boolean isWeiboOpen;
	private ListView mCurrentListView;
	private Boolean isStopSound;
	private UserAdBean userAdBean;
	private String adUrl,userAdUrl;
	
	ADDialog adDialog; 


	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_clock_main);
		clockApplication.addActivity(this);
		
		agent = new FeedbackAgent(ClockMainActivity.this);
		viewPager = (ViewPager) this.findViewById(R.id.viewpager);
		showMonthView = (TextView) this.findViewById(R.id.show_month_view);
		showYearView = (TextView) this.findViewById(R.id.show_year_view);
//		bannerLayout_Text  = (TextView) findViewById(R.id.bannerLayout_Text);

		// showWeekView = (TextView) this.findViewById(R.id.show_week_view);
		square_calendar_button = (ImageView) findViewById(R.id.square_calendar_button);
		mNowCircleView = (ImageView) findViewById(R.id.now_circle_view);
		mMyCircleView = (ImageView) findViewById(R.id.my_calendar_button);
		xiaoxi_button = (ImageView) findViewById(R.id.xiaoxi_button);
		views = builder.createMassCalendarViews(this, 3, this);
		mSlideListView = (ListView) findViewById(R.id.tastList);
		stickyLayout = (StickyLayout) findViewById(R.id.sticky_layout);
		clock_main_alertText = (TextView) findViewById(R.id.clock_main_alertText);
		clock_main_alertLayout = (RelativeLayout) findViewById(R.id.clock_main_alertLayout);
//		bannerLayout = (RelativeLayout) findViewById(R.id.bannerLayout);
		shareTitle = (TextView) findViewById(R.id.shareTitle);
		leftImage = (ImageView) findViewById(R.id.leftImage);
		shareImage = (ImageView) findViewById(R.id.shareImage);
		mCurrentListView = mSlideListView;
		isClickToday = true;
		UmengUpdateAgent.update(this);
		if (settings.getBoolean("isred", false)) {
			xiaoxi_button.setImageResource(R.drawable.home_hongdian);
			isBottomClick = true;
			animAlertStart();
			leftImage.setImageResource(R.drawable.home_weidu);
			shareTitle.setText("有未读的消息。");
			clock_main_alertText.setText("");
			shareImage.setVisibility(View.VISIBLE);
		} else {
			xiaoxi_button.setImageResource(R.drawable.kongtu);
		}

		// int gotoActivity = getIntent().getIntExtra("goto", 0);
		// switch (gotoActivity) {
		// case MyReceiver.TO_COMMENT:
		// Intent in = new Intent(ClockMainActivity.this,
		// ClockCommentActivity.class);
		// in.putExtra("jobid", getIntent().getStringExtra("jobid"));
		// in.putExtra("createUserID",
		// getIntent().getStringExtra("createUserID"));
		// in.putExtra("taskId", getIntent().getStringExtra("taskId"));
		// startActivity(in);
		//
		// break;
		// case MyReceiver.TO_ZAN:
		// Intent inZan = new Intent(ClockMainActivity.this,
		// ClockVoteActivity.class);
		// inZan.putExtra("jobid", getIntent().getStringExtra("jobid"));
		// startActivity(inZan);
		// break;
		// }
		boolean isPush = getIntent().getBooleanExtra("isPush", false);
		if (isPush) {
			int gotoAct = getIntent().getIntExtra("goto", -1);
			Intent in = null;
			// if(gotoAct == MyReceiver.TO_COMMENT){
			// in = new Intent(ClockMainActivity.this,
			// ClockCommentActivity.class);
			// in.putExtra("jobid", getIntent().getStringExtra("jobid"));
			// in.putExtra("createUserID",
			// getIntent().getStringExtra("createUserID"));
			// in.putExtra("taskId", getIntent().getStringExtra("taskId"));
			// } else if(gotoAct == MyReceiver.TO_ZAN){
			// in = new Intent(ClockMainActivity.this,
			// ClockVoteActivity.class);
			// in.putExtra("jobid", getIntent().getStringExtra("jobid"));
			// in.putExtra("createUserID",
			// getIntent().getStringExtra("createUserID"));
			// in.putExtra("taskId", getIntent().getStringExtra("taskId"));
			// } else if(gotoAct == MyReceiver.TO_FANS){
			// in = new Intent(ClockMainActivity.this, ClockFansActivity.class);
			// in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (gotoAct == MyReceiver.TO_DY) {
				in = new Intent(mContext, ClockDynamicActivity.class);
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			} else if (gotoAct == MyReceiver.TO_MAIN) {
				return;
			}
			in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(in);
		}
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
		viewPagerAdapter = new CustomViewPagerAdapter<CalendarView>(views);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setCurrentItem(498);
		viewPager.setOnPageChangeListener(new CalendarViewPagerLisenter(
				viewPagerAdapter));

		footerView = (LinearLayout) getLayoutInflater().inflate(
				R.layout.clock_main_item_footerview, null);
		createTask = (LinearLayout) footerView.findViewById(R.id.createTask);
		homeShare = (LinearLayout) footerView.findViewById(R.id.homeShare);
		mSlideListView.addFooterView(footerView);
		isWeiboOpen = settings.getBoolean("isWeiboOpen", false);
		
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
//		bannerLayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				mEditor.putString("adurl", userAdUrl);
//				mEditor.commit();
//				Intent in = new Intent(mContext, DailycamShowActivity.class);
//				in.putExtra("title", userAdBean.getTitle());
//				in.putExtra("url", userAdBean.getLink());
//				startActivity(in);
//				bannerLayout.setVisibility(View.GONE);
//			}
//		});
		square_calendar_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 跳转广场
				// if (isLogin) {
				if (Contanst.isConnStop) {
					Toast.makeText(ClockMainActivity.this, "当前无无网络，请稍后再试",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Intent in = new Intent(mContext,
						GroupMainFragmentActivity.class);
				startActivity(in);
				// } else {
				// Intent in = new Intent(ClockMainActivity.this,
				// LoginActivity.class);
				// startActivity(in);
				// }
				// ClockMainActivity.square_calendar_button
				// .setImageResource(R.drawable.kongtu);
			}
		});
		// 为了不让打卡成功的提示下面的Listview 获得焦点
		clock_main_alertLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isBottomClick) {
					Intent in = new Intent(mContext, ClockDynamicActivity.class);
					startActivity(in);
				}
			}
		});
		// 打卡成功的分享按钮的点击事件
		shareImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sharedDialog = new SharedDialog(ClockMainActivity.this, jobid,
						mShareImageBitmap, signature, quantity + "",
						byDateBeanList.get(positioniItemClick).getTitle(),
						byDateBeanList.get(positioniItemClick)
						.getCurrentStreak() + "");
				Window win = sharedDialog.getWindow();
				android.view.WindowManager.LayoutParams params = new android.view.WindowManager.LayoutParams();
				// params.x = -80;//设置x坐标
				// params.y = -60;//设置y坐标
				win.setAttributes((android.view.WindowManager.LayoutParams) params);
				sharedDialog.setCanceledOnTouchOutside(true);//
				// 设置点击Dialog外部任意区域关闭Dialog
				sharedDialog.show();

			}
		});

		stickyLayout.setOnGiveUpTouchEventListener(this);
		stickyLayout.setOnMoveOverListener(this);
		createTask.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Toast.makeText(mContext, "新建打卡！！！",
				// Toast.LENGTH_SHORT).show();
				if (Contanst.isConnStop) {
					Toast.makeText(ClockMainActivity.this, "当前无无网络，请稍后再试",
							Toast.LENGTH_SHORT).show();
					return;
				}
				MobclickAgent.onEvent(mContext, "clicknewtask");
				Intent in = new Intent(mContext, ClockNewActivity.class);
				startActivity(in);
			}
		});
		homeShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Contanst.isConnStop) {
					Toast.makeText(ClockMainActivity.this, "当前无无网络，请稍后再试",
							Toast.LENGTH_SHORT).show();
					return;
				}
				MobclickAgent.onEvent(mContext, "mainhomepageshareclick");
				Intent in = new Intent(mContext, ShareAchievementActivity.class);
				// in.putExtra("text", sb.toString());
				in.putExtra("date", mClickDate.toString());
				in.putExtra("userid", userID);
				startActivity(in);
				overridePendingTransition(R.anim.slide_alpha_in_right,
						R.anim.slide_alpha_out_left);
			}
		});
		String createDate = settings
				.getString("getFistTaskCdate", "2015-05-01");
		ClickDataConn(TimeUtil.getDateString(System.currentTimeMillis()),
				createDate, TimeUtil.getDateString(System.currentTimeMillis()));
		SelectClockStateConn(createDate,
				TimeUtil.getDateString(System.currentTimeMillis()));

		adapter = new SlideAdapter(this);

		clockMainTodayAdapter = new ClockMainTodayAdapter(mContext);
		// adapter.setOnCheckMove(onCheckMove);
		adapter.setOnClickBotton(button);
		setRightAdapter();
		// adapter.setBuildingOnClick(buildingOnClick);

		mSlideListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MobclickAgent.onEvent(mContext, "clicktask");
				if(position>=byDateBeanList.size()){
					return;
				}
				if (today.equals(mClickDate.getDate())) {
					// 判断如果是今天，可以打卡
					positioniItemClick = position;
					if (byDateBeanList.get(position).getTime_limit_flag() == 1) {
						String startTime = today + " "
								+ byDateBeanList.get(position).getBegin_time()
								+ ":00";
						String endTime = today + " "
								+ byDateBeanList.get(position).getEnd_time()
								+ ":00";
						Tools.getLog(Tools.d, "aaa", "start:" + startTime);
						Tools.getLog(Tools.d, "aaa", "end:" + endTime);
						if (TimeUtil.getUnixLong(startTime) >= System
								.currentTimeMillis()
								|| TimeUtil.getUnixLong(endTime) <= System
								.currentTimeMillis()) {
							shareTitle.setText("打卡失败！");
							leftImage
							.setImageResource(R.drawable.home_dakashibai);
							shareImage.setVisibility(View.GONE);
							clock_main_alertText.setText("请在时间内打卡。");
							Message msg = new Message();
							msg.what = Contanst.CreateJobFail;
							mHandler.sendMessage(msg);
							isBottomClick = false;
							animAlertStart();
							return;
						}
					}
					dialog = new TakeClockDialog(ClockMainActivity.this);
					dialog.setOnClickClockSuccess(cLickClockSuccess);
					dialog.setLastNumAndUnit(byDateBeanList.get(position)
							.getLastQuantity(), byDateBeanList.get(position)
							.getLastUnit());
					taskTitle = byDateBeanList.get(position).getTitle();
					dialog.setTaskNameStr(taskTitle);
					dialog.show();

				} else {
					// 点击就弹打卡失败
					shareTitle.setText("打卡失败！");
					leftImage.setImageResource(R.drawable.home_dakashibai);
					shareImage.setVisibility(View.GONE);
					clock_main_alertText.setText("只能打今天的卡");

					Message msg = new Message();
					msg.what = Contanst.CreateJobFail;
					mHandler.sendMessage(msg);
					isBottomClick = false;
					animAlertStart();
				}
			}
		});

		mSlideListView
		.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0,
					View arg1, final int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(arg2>=byDateBeanList.size()){
					return false; 
				}
				if (!isMove) {
					//							if (today.equals(mClickDate.getDate())) { // 判断如果是今天，可以排序
					// Intent in = new
					// Intent(ClockMainActivity.this,
					// SetTaskSortActivity.class);
					// in.putExtra("LIST",
					// (Serializable) byDateBeanList);
					// startActivityForResult(in, requestTask);

					// clickLongDialog(arg2);
					final TaskMainLongClickDialog longClickDialog = new TaskMainLongClickDialog(
							ClockMainActivity.this);
					longClickDialog.show();
					longClickDialog.taskmainlongclick_dialog_details
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if (Contanst.isConnStop) {
								Toast.makeText(
										ClockMainActivity.this,
										"当前无无网络，请稍后再试",
										Toast.LENGTH_SHORT)
										.show();
								longClickDialog.dismiss();
								return;
							}
							Intent in = new Intent(
									ClockMainActivity.this,
									TaskInfoActivity.class);
							in.putExtra("taskid",
									byDateBeanList.get(arg2)
									.getTaskId());
							in.putExtra(
									"totalCheckCount",
									byDateBeanList
									.get(arg2)
									.getTotalCheckCount()
									+ "");// 打卡次数s
							in.putExtra(
									"totalDateCount",
									byDateBeanList
									.get(arg2)
									.getTotalDateCount()
									+ "");
							in.putExtra("title", byDateBeanList
									.get(arg2).getTitle());
							in.putExtra("description",
									byDateBeanList.get(arg2)
									.getDescription());
							ClockMainActivity.this
							.startActivity(in);
							longClickDialog.dismiss();
						}
					});
					longClickDialog.taskmainlongclick_dialog_delete
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							showDeleteDialog(arg2);
							longClickDialog.dismiss();
						}
					});
					longClickDialog.taskmainlongclick_dialog_setting
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if (Contanst.isConnStop) {
								Toast.makeText(
										ClockMainActivity.this,
										"当前无无网络，请稍后再试",
										Toast.LENGTH_SHORT)
										.show();
								longClickDialog.dismiss();
								return;
							}
							Intent intent = new Intent(
									mContext,
									ClockSetActivity.class);
							intent.putExtra("taskid",
									byDateBeanList.get(arg2)
									.getTaskId());
							startActivity(intent);
							longClickDialog.dismiss();
						}
					});
					// }
				} else {
					isMove = false;
				}
				return true;
			}
		});

		mNowCircleView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ClickDataConn(today, null, null);
				builder.backTodayCalendarViews();
				mClickDate = null;
				isClickToday = true;
				setShowDateViewText(TimeUtil.getYear(), TimeUtil.getMonth());
			}
		});

		mMyCircleView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (Contanst.isConnStop) {
					Toast.makeText(ClockMainActivity.this, "当前无无网络，请稍后再试",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (isLogin) {
					Intent in = new Intent(ClockMainActivity.this,
							ClockMyActivity.class);
					startActivity(in);
				} else {
					Intent in = new Intent(ClockMainActivity.this,
							LoginActivity.class);
					startActivity(in);
				}

			}
		});

		xiaoxi_button.setOnClickListener(new OnClickListener() {
			// 反馈
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub


				Intent in = new Intent(mContext, ClockDynamicActivity.class);
				startActivity(in);
				//				if (popupWindow.isShowing()) {
				//					popupWindow.dismiss();
				//				} else {
				//					Tools.getLog(Tools.d, "aaa", "aaa" + xoff);
				//					popupWindow.showAsDropDown(xiaoxi_button, 16, 0);
				//				}
				// agent.sync();
				// agent.startFeedbackActivity();
			}
		});
	}

	private void connBaiduLocation() {
		latitude = settings.getString("latitude", "-1");
		longitude = settings.getString("longitude", "-1");
		if (latitude.equals("-1")) {
			CreateJobConn(positioniItemClick, null);
		} else {
			Service.getService(Contanst.HTTP_BAIDU_LOCATION, null,
					latitude + "," + longitude, ClockMainActivity.this)
					.addList(null).request(UrlParams.GET);
		}
	}

	boolean isSuccessClick = false;

	TaskClockDialogOnCLickClockSuccess cLickClockSuccess = new TaskClockDialogOnCLickClockSuccess() {

		@Override
		public void onClickSuccess(String content, String num,String unit,
				ArrayList<ImageListBean> list) {
			// TODO Auto-generated method stub
			if(jobid != null){
				return;
			}
			if (today.equals(mClickDate.getDate())) { // 判断如果是今天，可以打卡
				MobclickAgent.onEvent(mContext, "clickcreatejob");
				isStopSound = settings.getBoolean("sound", false);
				signature = content;
				quantity = num;
				unitStr = unit;
				isUpLoad = true;
				filenames.clear();
				filenames = list;
				isSuccessClick = true;
				// byDateBeanList.get(positioniItemClick).setLastQuantity(num);
				
				if(ClockApplication.isOpenGPS(mContext)){
					connBaiduLocation();
				}else{
					CreateJobConn(positioniItemClick,null);
					if (!isStopSound) {
						jobSound();
					}
				}
				

			} else {

				shareTitle.setText("打卡失败！");
				leftImage.setImageResource(R.drawable.home_dakashibai);
				shareImage.setVisibility(View.GONE);
				clock_main_alertText.setText("只能打今天的卡");

				Message msg = new Message();
				msg.what = Contanst.CreateJobFail;
				mHandler.sendMessage(msg);
				isBottomClick = false;
				animAlertStart();

			}
		}
	};
	OnClickButton button = new OnClickButton() {
		// 详情
		@Override
		public void clickTaskInfoCheck(String taskID, int position) {
			// TODO Auto-generated method stub
			if (Contanst.isConnStop) {
				Toast.makeText(ClockMainActivity.this, "当前无无网络，请稍后再试",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent in = new Intent(ClockMainActivity.this,
					TaskInfoActivity.class);
			in.putExtra("taskid", byDateBeanList.get(position).getTaskId());
			in.putExtra("totalCheckCount", byDateBeanList.get(position)
					.getTotalCheckCount() + "");// 打卡次数s
			in.putExtra("totalDateCount", byDateBeanList.get(position)
					.getTotalDateCount() + "");
			in.putExtra("title", byDateBeanList.get(position).getTitle());
			in.putExtra("description", byDateBeanList.get(position)
					.getDescription());
			ClockMainActivity.this.startActivity(in);

		}

		// 置顶
		@Override
		public void clickStickFlag(String tastID, int position) {
			// TODO Auto-generated method stub\
			// ByDateBean dateBean = byDateBeanList.get(position);
			// byDateBeanList.remove(position);
			// if (dateBean.getStickFlag() == 0) {
			// StickFlagConn(tastID, 1);
			// dateBean.setStickFlag(1);
			// byDateBeanList.add(0, dateBean);
			// } else {
			// StickFlagConn(tastID, 0);
			// dateBean.setStickFlag(0);
			// byDateBeanList.add(dateBean);
			// }
			// adapter.notifyDataSetChanged();
			// Toast.makeText(ClockMainActivity.this, "分享", Toast.LENGTH_SHORT)
			// .show();
		}

		// 放弃
		@Override
		public void clickCancelCheck(final String tastID, final int jobCount,
				final int type, final int position) {
			// TODO Auto-generated method stub
			// CancelCheckType = type;
			// positioniItemClick = position;
			// final GiveUpJobDialog upJobDialog = new
			// GiveUpJobDialog(mContext);
			// final GiveUpDialog giveUpDialog = new GiveUpDialog(
			// ClockMainActivity.this);
			//
			// switch (type) {
			// case 1:
			// upJobDialog.show();
			// upJobDialog.mTitle.setText("取消放弃");
			// upJobDialog.giveUpJob_dialog_content
			// .setText("是否删除上一次放弃打卡和\n放弃打卡理由");
			// break;
			// case 2:
			// upJobDialog.show();
			// upJobDialog.mTitle.setText("取消打卡");
			// upJobDialog.giveUpJob_dialog_content
			// .setText("确定取消打卡将删除最新的一次\n打卡内容");
			// break;
			// case 3:
			// giveUpDialog.show();
			// break;
			// }
			// if (type == 3) {
			// giveUpDialog.mResult.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// giveUpReason = giveUpDialog.mInput.getText().toString();
			// // byDateBeanList.get(position).setGiveUpFlag("1");
			// isCancelCheck = true;
			// CancelCheckConn(tastID, jobCount, type, position);
			// giveUpDialog.dismiss();
			// }
			// });
			// giveUpDialog.mCancel.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// giveUpDialog.dismiss();
			// }
			// });
			// } else {
			// upJobDialog.mClose.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// upJobDialog.dismiss();
			// }
			// });
			// upJobDialog.mSuccess.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// if (type == 3) {
			// giveUpReason = upJobDialog.mInput.getText()
			// .toString();
			// // byDateBeanList.get(position).setGiveUpFlag("1");
			// isCancelCheck = true;
			// }
			// CancelCheckConn(tastID, jobCount, type, position);
			// upJobDialog.dismiss();
			// }
			// });
			// }
		}
	};

	//	public void clickLongDialog(final int position) {
	//		AlertDialog.Builder builder = new AlertDialog.Builder(
	//				new ContextThemeWrapper(mContext,
	//						R.style.CustomDiaLog_by_SongHang));
	//		builder.setItems(new String[] { "打卡详情", "设置", "删除卡片", "取消" },
	//				new AlertDialog.OnClickListener() {
	//
	//			@Override
	//			public void onClick(DialogInterface dialog, int which) {
	//				// TODO Auto-generated method stub
	//				switch (which) {
	//				case 0:
	//					Intent in = new Intent(ClockMainActivity.this,
	//							TaskInfoActivity.class);
	//					in.putExtra("taskid", byDateBeanList.get(position)
	//							.getTaskId());
	//					in.putExtra("totalCheckCount",
	//							byDateBeanList.get(position)
	//							.getTotalCheckCount() + "");// 打卡次数s
	//					in.putExtra("totalDateCount",
	//							byDateBeanList.get(position)
	//							.getTotalDateCount() + "");
	//					in.putExtra("title", byDateBeanList.get(position)
	//							.getTitle());
	//					in.putExtra("description",
	//							byDateBeanList.get(position)
	//							.getDescription());
	//					ClockMainActivity.this.startActivity(in);
	//					break;
	//				case 1:
	//					Intent intent = new Intent(mContext,
	//							ClockSetActivity.class);
	//					intent.putExtra("taskid",
	//							byDateBeanList.get(position).getTaskId());
	//					startActivity(intent);
	//					break;
	//				case 2:
	//					showDeleteDialog(position);
	//					break;
	//				case 3:
	//
	//					break;
	//
	//				default:
	//					break;
	//				}
	//			}
	//		});
	//		builder.show();
	//	}

	private void setRightAdapter() {
		AnimationAdapter animAdapter = new SwingRightInAnimationAdapter(adapter);
		animAdapter.setAbsListView(mCurrentListView);
		mCurrentListView.setAdapter(animAdapter);
	}

	public void StickFlagConn(String tastID, int StickFlag) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("taskId", tastID));
			params.add(new BasicNameValuePair("stickFlag", StickFlag + ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_UPDATETASK, null, null,
				ClockMainActivity.this).addList(params).request(UrlParams.POST);
	}

	// public void CancelCheckConn(String tastID, int jobCount, int type,
	// int position) {
	// final List<NameValuePair> params = new ArrayList<NameValuePair>();
	// try {
	// switch (type) {
	// case 1:
	// params.add(new BasicNameValuePair("taskId", tastID));
	// params.add(new BasicNameValuePair("cancelGiveUp", "1"));
	// break;
	// case 2:
	// params.add(new BasicNameValuePair("taskId", tastID));
	// params.add(new BasicNameValuePair("remainCheckCount",
	// (jobCount - 1) + ""));
	// break;
	// case 3:
	// CreateJobConn(position);
	// return;
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// Service.getService(Contanst.HTTP_UPDATEJOB, null, null,
	// ClockMainActivity.this).addList(params).request(UrlParams.POST);
	// }

	public File cameraFile;

	public void SelectClockStateConn(String beginDate, String endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(userID);
		sb.append("&beginDate=");
		sb.append(beginDate);
		sb.append("&endDate=");
		sb.append(endDate);

		Service.getService(Contanst.HTTP_GETPERFORMANCE, null, sb.toString(),
				ClockMainActivity.this).addList(null).request(UrlParams.GET);
	}

	public void updateOrderConn() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byDateBeanList.size(); i++) {
			sb.append(byDateBeanList.get(i).getTaskId());
			if (i != byDateBeanList.size() - 1) {
				sb.append(",");
			}
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("taskIds", sb.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Service.getService(Contanst.HTTP_UPDATE_ORDER, null, null,
				ClockMainActivity.this).addList(params).request(UrlParams.POST);
	}
	public void getAdConn(){
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_USER_AD, null, sb.toString(),
				ClockMainActivity.this).addList(null).request(UrlParams.GET);
	}

	// 点击日历的监听
	@Override
	public void clickDate(CustomDate date, int row) {
		// TODO Auto-generated method stub
		String new_date = date.getDate();
		clickRow = row;
		if (new_date.equals(today)) {
			isTodayCanSee = true;
			isClickToday = true;
			mNowCircleView.setVisibility(View.GONE);
		} else {
			isClickToday = false;
			isTodayCanSee = false;
			mNowCircleView.setVisibility(View.VISIBLE);
		}

		if (mClickDate != null && !new_date.equals(mClickDate.getDate())) {
			ClickDataConn(new_date, null, null);
		}
		mClickDate = date;
		setShowDateViewText(date.year, date.month);
	}

	String curDateStr;

	public void ClickDataConn(String data, String beginDate, String endDate) {
		// Hashtable<String, String> hashtable = new Hashtable<String,
		// String>();
		// hashtable.put("userId", "8");
		// hashtable.put("date", data);
		curDateStr = data;
		StringBuffer sb = new StringBuffer();
		if (beginDate == null) {
			sb.append("?userId=");
			sb.append(userID);
			sb.append("&date=");
			sb.append(data);
		} else {

			sb.append("?userId=");
			sb.append(userID);
			sb.append("&date=");
			sb.append(data);
			sb.append("&beginDate=");
			sb.append(beginDate);
			sb.append("&endDate=");
			sb.append(endDate);
		}
		Service.getService(Contanst.HTTP_GETBYDATE, null, sb.toString(),
				ClockMainActivity.this).addList(null).request(UrlParams.GET);
	}

	int cellSpace;

	@Override
	public void onMesureCellHeight(int cellSpace, int curRow) {
		// TODO Auto-generated method stub

		this.cellSpace = cellSpace;
		LinearLayout.LayoutParams btitlelayout = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, cellSpace * 6);
		viewPager.setLayoutParams(btitlelayout);
		if (CalendarView.style == CalendarView.MONTH_STYLE) {
			stickyLayout.setmHeaderHeight(cellSpace * curRow, true);
			stickyLayout.setMinHeadHeight(cellSpace);
		} else {
			stickyLayout.setmHeaderHeight(cellSpace, true);
			stickyLayout.setMinHeadHeight(cellSpace);
		}
	}

	// 滑动日历的监听
	@Override
	public void changeDate(CustomDate date, int curRow) {

		// String[] dateArray = TimeUtil.getyyyyMM(System.currentTimeMillis())
		// .split(":");
		// Tools.getLog(Tools.i, "aaa",
		// "changeDatechangeDatechangeDate" + dateArray[0] + "aaa"
		// + dateArray[1] + "========" + date.getMonth());
		// if (date.year != Integer.parseInt(dateArray[0])
		// || date.month != Integer.parseInt(dateArray[1])) {
		// isTodayCanSee = false;
		// }

		if (CustomDate.DateToUnixTime(date) > System.currentTimeMillis()
				&& DateUtil.getDate(date.toString(), 7) < System
				.currentTimeMillis()) {
			isTodayCanSee = true;
		}
		setShowDateViewText(date.year, date.month);
		if (cellSpace != 0) {
			if (CalendarView.style == CalendarView.MONTH_STYLE) {
				stickyLayout.setmHeaderHeight(cellSpace * curRow, true);
				stickyLayout.setMinHeadHeight(cellSpace);
				maxHeight = cellSpace * curRow;
			}
		}
	}

	public void setShowDateViewText(int year, int month) {
		showYearView.setText(year + " - ");
		showMonthView.setText(month + "");
		if (!isTodayCanSee) {
			mNowCircleView.setVisibility(View.VISIBLE);
		} else {
			mNowCircleView.setVisibility(View.GONE);
		}
		// showWeekView.setText(DateUtil.weekName[DateUtil.getWeekDay() - 1]);
	}

	public static boolean isBackToday;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		today = TimeUtil.getYYMMDD(System.currentTimeMillis());
		getAdConn();//不断刷新
		MobclickAgent.onPageStart("SplashScreen"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this);

		if (CommonUtils.isNetWorkConnected(ClockMainActivity.this)) {
			Contanst.isConnStop = false;
		} else {
			Contanst.isConnStop = true;
		}

		if (square_calendar_button != null) {
			if (settings.getBoolean("isred", false)) {
				xiaoxi_button.setImageResource(R.drawable.home_hongdian);
			} else {
				xiaoxi_button.setImageResource(R.drawable.kongtu);
			}
		}
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		String checkToday = settings.getString("checktoday", null);
		Tools.getLog(Tools.d, "aaa", "checkToday:" + checkToday);

		if(isBackToday){
			builder.backTodayCalendarViews();
			mClickDate = null;
			isClickToday = true;
			setShowDateViewText(TimeUtil.getYear(), TimeUtil.getMonth());
			setRightAdapter();
		} 

		if (checkToday == null || !today.equals(checkToday)) {
			jobDefaultCheckConn();
			Tools.getLog(Tools.d, "aaa", "签到！！！！！！！！！");
		}else{
			if (mClickDate != null) {
				ClickDataConn(mClickDate.toString(), null, null);
			} else {
				ClickDataConn(today, null, null);
				isBackToday = false;
			}
		}

		ImageLoader.getInstance().clearMemoryCache();
		TaskCaledarView.infoDateArray = null;
		if (CalendarView.style == CalendarView.WEEK_STYLE) { // 周
			builder.swtichCalendarViewsStyle(CalendarView.WEEK_STYLE,
					mClickDate);
		} else { // 月
			builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE,
					mClickDate);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		// onPageEnd 在onPause
		// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		vibrator.cancel();
	}

	boolean isTakePicture;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 5668 && resultCode == RESULT_OK) {
			/** 使用SSO授权必须添加如下代码 */
			UMSsoHandler ssoHandler = MyUMSDK.mController.getConfig()
					.getSsoHandler(requestCode);
			if (ssoHandler != null) {
				ssoHandler.authorizeCallBack(requestCode, resultCode, data);
				sharedDialog = null;
			}
			return;
		}
		Tools.getLog(Tools.i, "aaa", "Activity.RESULT_OK === "
				+ Activity.RESULT_OK);
		Tools.getLog(Tools.i, "aaa", "resultCode === " + resultCode);
		Bitmap bitmap = null;
		String ImageUrl = null;
		if (resultCode == Activity.RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Tools.getLog(Tools.i, "aaa",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			if (requestCode == requestTask) {
				byDateBeanList = (ArrayList<ByDateBean>) data
						.getSerializableExtra("LIST");
				if (isClickToday) {
					clockMainTodayAdapter.setList(byDateBeanList);
					clockMainTodayAdapter.notifyDataSetChanged();
				} else {
					adapter.setList(byDateBeanList);
					adapter.notifyDataSetChanged();
				}
				updateOrderConn();

			} else if (requestCode == dialog.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE) {
				isTakePicture = true;
				// if (data != null) {
				// Uri selectedImage = data.getData();
				// // Tools.getLog(Tools.d, "aaa",
				// // "imageUrl:" + selectedImage.getPath());
				// ContentResolver resolver = getContentResolver();
				Bitmap photo = null;
				// 根据需要，也可以加上Option这个参数
				try {
					// if (selectedImage != null) {
					// // // sendPicByUri(selectedImage);
					// photo = MediaStore.Images.Media.getBitmap(resolver,
					// selectedImage);
					//
					// } else {
					// Bundle extras = data.getExtras();
					// if (extras != null) {
					// // 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
					// photo = extras.getParcelable("data");
					// }
					//
					// }
					// if (photo != null) {
					// // String str =
					//
					// if (photo.getWidth() > photo.getHeight()) {
					// photo = ImageTool.rotateBitMap(photo, 1);
					// }
					//
					// if (ImageTool.saveBitmapToAlbum(
					// ClockMainActivity.this, photo)) {
					// dialog.reflashCanmera();
					// }
					//
					// return;
					// }

					// File f = new File(FileURl.ImageFilePath
					// + dialog.cameraPhotoName);
					String path = FileURl.ImageFilePath + "/"
							+ dialog.cameraPhotoName;

					int degree = ImageTool.getBitmapDegree(path);
					Tools.getLog(Tools.i, "aaa", "degree ============ "
							+ degree);

					FileInputStream in = new FileInputStream(path);

					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 10;

					photo = ImageTool.rotateBitMap(
							BitmapFactory.decodeStream(in, null, options),
							degree);

					// Uri u = Uri
					// .parse(android.provider.MediaStore.Images.Media
					// .insertImage(getContentResolver(),
					// f.getAbsolutePath(), null, null));
					// u就是拍摄获得的原始图片的uri，剩下的你想干神马坏事请便……
					// TODO Auto-generated catch block
					if (ImageTool.saveBitmapToAlbum(ClockMainActivity.this,
							photo)) {
						dialog.reflashCanmera();
					}

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					ImageLoader.getInstance().clearMemoryCache();
				}

				// }
			} else {
				filenames = dialog.getFilenames();
				List<String> list = (List<String>) data
						.getSerializableExtra("selectimage");

				for (int j = 0; j < list.size(); j++) {

					for (int i = filenames.size() - 1; i >= 0; i--) {
						if (!filenames.get(i).getIsCheck()) {
							filenames.remove(i);
							break;
						}
					}
					ImageListBean bean = new ImageListBean();
					bean.setImageUrl(list.get(j));
					bean.setCheck(true);
					filenames.add(0, bean);
				}

				dialog.reflashList(filenames);
			}
		}

		if (requestCode == dialog.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE) {
			Tools.getLog(Tools.d, "aaa", "从相册选择完图片的情况：" + filenames.toString());
			for (int i = filenames.size() - 1; i >= 3; i--) {
				filenames.remove(i);
			}
			// mSetImage.setImage(filenames, isTakePicture);
		}
	}

	// /**
	// * 滑动监听
	// */
	// OnCheckMove onCheckMove = new OnCheckMove() {
	//
	// @Override
	// public void getClick(int position) {
	// // TODO Auto-generated method stub
	// positioniItemClick = position;
	// isUpLoad = false;
	// CreateJobConn(position);
	// jobSound();
	// adapter.notifyDataSetChanged();
	// }
	// };
	int imageNum = 0;// 上传图片数量

	private void CreateJobConn(int position, String city) {
		taskItemBean = new TaskItemBean();
		imageNum = 0;
		String url = "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < filenames.size(); i++) {
			if (filenames.get(i).getIsCheck()) {
				url = filenames.get(i).getImageUrl();
				imageNum++;
				sb.append(url);
				sb.append(",");

			}

		}
		if (imageNum == 0) {
			mShareImageBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.share_icon);
			Tools.getLog(Tools.d, "aaa", "照片为空！");
		} else {
			Tools.getLog(Tools.d, "aaa", "照片不为空，数量：" + imageNum);
			MobclickAgent.onEvent(mContext, "photonotnull");
			mShareImageBitmap = ImageLoader.getInstance().loadImageSync(url);
		}

		// Toast.makeText(mContext, "图片数量："+imageNum,
		// Toast.LENGTH_SHORT).show();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			ByDateBean bean = byDateBeanList.get(position);
			jobid = Tools.getUuid();
			params.add(new BasicNameValuePair("jobId", jobid));
			params.add(new BasicNameValuePair("taskId", bean.getTaskId()));
			params.add(new BasicNameValuePair("unit", unitStr));

			if (signature != null && signature.length() != 0) {
				params.add(new BasicNameValuePair("signature", signature));
			}
			if (quantity != null && quantity.length() != 0) {
				params.add(new BasicNameValuePair("quantity", quantity));
			}
			// if (isCancelCheck) {
			// params.add(new BasicNameValuePair("giveUpReason", giveUpReason));
			// params.add(new BasicNameValuePair("giveUpFlag", "1"));
			// } else {
			params.add(new BasicNameValuePair("giveUpFlag", "0"));
			// }

			long checkTime = System .currentTimeMillis();
			params.add(new BasicNameValuePair("checkTime", checkTime + ""));
			params.add(new BasicNameValuePair("onlineFlag", "1"));
			params.add(new BasicNameValuePair("picNum", imageNum + ""));
			params.add(new BasicNameValuePair("clientLocalTime", checkTime + ""));
			params.add(new BasicNameValuePair("currentGiveUpFlag", bean
					.getGiveUpFlag()));
			if (city != null) {
				params.add(new BasicNameValuePair("city", city));
				params.add(new BasicNameValuePair("longitude", longitude));
				params.add(new BasicNameValuePair("latitude", latitude));
			}
			Tools.getLog(Tools.i, "aaa", "params ======================= "
					+ params.toString());
			taskItemBean.setJobId(jobid);
			taskItemBean.setTaskId(bean.getTaskId());
			taskItemBean.setSignature(signature);
			taskItemBean.setQuantity(quantity);
			taskItemBean.setCheck_time(checkTime);
			taskItemBean.setPicCount(imageNum+"");

			android.util.Log
			.i("aaa", "allPath ============== " + sb.toString());
			String allPath = sb.toString();
			if (allPath != null && allPath.length() > 0) {
				allPath = allPath.substring(0, allPath.length() - 1);
				taskItemBean.setAllPath(allPath);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_CREATEJOB, null, null,
				ClockMainActivity.this).addList(params).request(UrlParams.POST);
	}

	private void listSort(int position) {
		ByDateBean bean = byDateBeanList.get(position);
		byDateBeanList.remove(position);
		int jobCount = bean.getJobCount();
		int totalDateCount = bean.getTotalDateCount();
		bean.setJobCount(++jobCount);
		bean.setTotalDateCount(++totalDateCount);
		byDateBeanList.add(bean);
	}

	private void jobSound() {
		vibrator.vibrate(500); // 重复两次上面的pattern 如果只想震动一次，index设为
		SoundPool soundPool;
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);

		soundPool.load(this, R.raw.a6, 1);
		soundPool.play(1, 1, 1, 0, 0, 1);
	}

	public void jobDefaultCheckConn() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("userId", userID));

		} catch (Exception e) {
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_JOB_DEFAULTCHECK, null, null,
				ClockMainActivity.this).addList(params).request(UrlParams.POST);
	}

	public interface OnSetDialogImage {
		/**
		 * 修改TakeClockDialog 的图片
		 */
		public void setImage(ArrayList<ImageListBean> beans,
				boolean isTakePicture);

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

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("null")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			case Contanst.CreateJobFail:
				isBottomClick = false;
				animAlertStart();
				break;

			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.GETPERFORMANCE:
					ArrayList<PerformanceBean> list = (ArrayList<PerformanceBean>) msg.obj;
					dbHelper.insertDailyPerformanceList(list);
					viewPagerAdapter.notifyDataSetChanged();
					break;
					// case Contanst.GETONGOING:
					// break;
				case Contanst.GETBYDATE:

					// LvHeightUtil
					// .setListViewHeightBasedOnChildren(mSlideListView);
					byDateBeanList = (ArrayList<ByDateBean>) msg.obj;
					dbHelper.insertTaskList(byDateBeanList);
					if (isClickToday) {
						mSlideListView.setAdapter(clockMainTodayAdapter);
						clockMainTodayAdapter.setList(byDateBeanList);
						if (mClickDate == null
								|| today.equals(mClickDate.getDate())) {
							// 提醒从网络获取
							GetAllAlarmList();
						}
						if (curDateStr.equals(today))
							updateDateColor();
						clockMainTodayAdapter.notifyDataSetChanged();
					} else {
						mSlideListView.setAdapter(adapter);
						adapter.setIsToday(isClickToday);
						adapter.setList(byDateBeanList);
						if (mClickDate == null
								|| today.equals(mClickDate.getDate())) {
							// 提醒从网络获取
							GetAllAlarmList();
						}
						if (curDateStr.equals(today))
							updateDateColor();
						adapter.notifyDataSetChanged();
					}

					break;
				case Contanst.UPDATEJOB:
					UpdateBean bean = (UpdateBean) msg.obj;

					byDateBeanList.get(positioniItemClick).setCheckTime(
							bean.getCheckTime());
					if (CancelCheckType == 1) {
						byDateBeanList.get(positioniItemClick).setGiveUpFlag(
								"0");
						byDateBeanList.get(positioniItemClick).setJobCount(0);
					} else if (CancelCheckType == 2) {
						byDateBeanList.get(positioniItemClick).setJobCount(
								byDateBeanList.get(positioniItemClick)
								.getJobCount() - 1);
					} else {
						byDateBeanList.get(positioniItemClick).setGiveUpFlag(
								"1");
					}
					if (isClickToday) {
						clockMainTodayAdapter.notifyDataSetChanged();
					} else {
						adapter.notifyDataSetChanged();
					}

					ClickDataConn(today, null, null);// 刷新bydate集合
					break;
				case Contanst.UPDATETASK:

					break;
				case Contanst.CREATEJOB:
					Tools.getLog(Tools.i, "aaa", "CREATEJOB");

					createJobBean = (CreateJobBean) msg.obj;
					Tools.getLog(Tools.d, "aaa", "打卡成功！！！！！！！！！");

					byDateBeanList.get(positioniItemClick).setCheckTime(
							createJobBean.getCheckTime());
					// if (createJobBean.getGiveUpFlag().equals("0")) {
					// Tools.getLog(Tools.d, "aaa", "没错，是正常打卡！！！！！！！！！");
					// // showTitleView(createJobBean, false);// 打卡完成显示的title\
					//
					// } else {
					// Tools.getLog(Tools.d, "aaa", "是放弃打卡！！！！！！！！！");
					// // showTitleView(createJobBean, true);// 打卡完成显示的title
					// byDateBeanList.get(positioniItemClick).setGiveUpFlag(
					// createJobBean.getGiveUpFlag());
					// }
					showTitleView(createJobBean, false);// 打卡完成显示的title
					if (isUpLoad) {
						if (filenames.size() > 0) {
							Intent intent_baonew = new Intent(
									ClockMainActivity.this,
									AddShowPhotoService.class);
							intent_baonew.putExtra("state",
									AddShowPhotoService.AddShowPhotoRun);
							intent_baonew.putExtra("type", 1);
							intent_baonew.putExtra("productid", userID);
							intent_baonew.putExtra("list", filenames);
							intent_baonew.putExtra("uuid", jobid);
							startService(intent_baonew);
						}
					}
					jobid = null;
					//					if(dialog != null ){
					//						dialog.dismiss();
					//					}
					// listSort(positioniItemClick);
					if (isClickToday) {
						clockMainTodayAdapter.notifyDataSetChanged();
					} else {
						adapter.notifyDataSetChanged();
					}
					break;
				case Contanst.UPDATE_ORDER:

					break;
				case Contanst.TASK_DELETE:
					Toast.makeText(mContext, "删除成功！", Toast.LENGTH_SHORT)
					.show();
					ClickDataConn(today, null, null);
					break;
				case Contanst.JOB_DEFAULTCHECK:
					mEditor.putString("checktoday",today);
					mEditor.commit();
					Tools.getLog(Tools.d, "aaa", "新用户每日签到成功！");
					//					Toast.makeText(mContext, "新用户每日签到成功！", Toast.LENGTH_SHORT).show();
					ClickDataConn(today, null, null);// 刷新bydate集合
					break;
				case Contanst.ALARM_GETBYUSERID:
					Tools.getLog(Tools.d, "ccc", "======111111=====");
					ArrayList<MoreAlertTimeBean> alertTimeBeans = (ArrayList<MoreAlertTimeBean>) msg.obj;
					// ArrayList<MoreAlertTimeBean> alertTimeBeans = new
					// ArrayList<MoreAlertTimeBean>();
					// alertTimeBeans = dbHelper.getAllAlarmList();
					Tools.getLog(Tools.d, "alert", alertTimeBeans.toString());

					CallAlarmUtil.closeAllAlarm(ClockMainActivity.this,
							alertTimeBeans.size());
					CallAlarmUtil.createTaskHashMap(ClockMainActivity.this,
							alertTimeBeans);
					break;
				case Contanst.BAIDU_LOCATION:
					Tools.getLog(Tools.d, "aaa", "地理位置：==="+(String) msg.obj);
					CreateJobConn(positioniItemClick, (String) msg.obj);
					if (!isStopSound) {
						jobSound();
					}
					break;
				case Contanst.USER_AD:
					userAdBean = (UserAdBean) msg.obj;
//					showAdDialog();
					adUrl = settings.getString("adurl", null);
					Tools.getLog(Tools.d, "aaa", "USER_AD=========== "
							+ userAdBean.toString());
					userAdUrl = userAdBean.getLink();
					if(userAdBean.getTitle() == null){
//						bannerLayout.setVisibility(View.GONE);
						return;
					}
					if(adUrl != null){
						if(userAdUrl != null || userAdUrl.length() != 0){
							if(adUrl.equals(userAdUrl)){
//								bannerLayout.setVisibility(View.GONE);
							}else{
								showAdDialog();
//								bannerLayout.setVisibility(View.VISIBLE);
//								bannerLayout_Text.setText(userAdUrl);
							}
						}else{
//							bannerLayout.setVisibility(View.GONE);
						}
					}else{
						showAdDialog();
//						bannerLayout.setVisibility(View.VISIBLE);
//						bannerLayout_Text.setText(userAdUrl);
					}
					break;

				}
				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				Tools.getLog(Tools.d, "aaa", "msg.arg1"+msg.arg1);
				switch (msg.arg1) {

				case Contanst.BAIDU_LOCATION:
					Tools.getLog(Tools.d, "aaa", "地理位置：===定位失败");
					CreateJobConn(positioniItemClick, null);
					if (!isStopSound) {
						jobSound();
					}
					break;

				case Contanst.GETBYDATE:
					if (!CommonUtils.isNetWorkConnected(ClockMainActivity.this)) {
						byDateBeanList = dbHelper.getTaskList();
						clockMainTodayAdapter.setList(byDateBeanList);

						if (curDateStr.equals(today))
							updateDateColor();
						clockMainTodayAdapter.notifyDataSetChanged();
					}

					break;
				case Contanst.CREATEJOB:
					if (message.equals("网络异常")) {
						listSort(positioniItemClick);
						if (dbHelper.insertFailJob(taskItemBean) == 0) {

							for (int i = 0; i < byDateBeanList.size(); i++) {
								if (byDateBeanList.get(i).getTaskId()
										.equals(taskItemBean.getTaskId())) {
									int jobCount = byDateBeanList.get(i)
											.getJobCount();
									int currentStreak = byDateBeanList.get(i)
											.getCurrentStreak();
									if (currentStreak <= 0) {
										currentStreak = 1;
									} else {
										if (jobCount <= 0) {
											// 第一次打卡
											currentStreak += 1;
										}
									}

									jobCount++;
									byDateBeanList.get(i).setCurrentStreak(
											currentStreak);
									byDateBeanList.get(i).setJobCount(jobCount);
								}
							}

							if (dbHelper.insertTaskList(byDateBeanList) == 0) {
								updateDateColor();
								if (isClickToday) {
									clockMainTodayAdapter
											.setList(byDateBeanList);
									clockMainTodayAdapter
											.notifyDataSetChanged();
								} else {
									adapter.setList(byDateBeanList);
									adapter.notifyDataSetChanged();
								}
								jobid = null;
								if (dialog != null && dialog.isShowing()) {
									dialog.dismiss();
								}
							}
						}
					}

					break;

				case Contanst.GETPERFORMANCE:
					Tools.getLog(Tools.d, "aaa", "GETPERFORMANCE=" + message);
					break;
					// case Contanst.GETONGOING:
					// Tools.getLog(Tools.d, "aaa", "GETONGOING=" + message);
					// break;
				case Contanst.USER_AD:
//					bannerLayout.setVisibility(View.GONE);
					break;
				}
				break;
			
			}
		}
	};

	private void updateDateColor() {
		int total = 0;
		for (int i = 0; i < byDateBeanList.size(); i++) {
			if (byDateBeanList.get(i).getJobCount() > 0) {
				total++;
			}
		}
		if (total == 0) {
			curMap.put(today, "0");
		} else if (total == byDateBeanList.size()) {
			// 今天的卡全部打完
			if(curMap == null){
				return;
			}

			if (isSuccessClick && !curMap.get(today).equals("2")) {
				curMap.put(today, "2");
				AllTaskFinishDialog2 allDialog = new AllTaskFinishDialog2(
						ClockMainActivity.this);
				allDialog.show();
				allDialog.shareImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						MobclickAgent.onEvent(mContext,
								"mainhomepageshareclick");
						Intent in = new Intent(mContext,
								ShareAchievementActivity.class);
						// in.putExtra("text", sb.toString());
						in.putExtra("date", mClickDate.toString());
						in.putExtra("userid", userID);
						startActivity(in);
						overridePendingTransition(R.anim.slide_alpha_in_right,
								R.anim.slide_alpha_out_left);
					}
				});
				isSuccessClick = false;
			}

		} else {
			curMap.put(today, "1");
		}
		if (CalendarView.style == CalendarView.WEEK_STYLE) { // 周
			builder.swtichCalendarViewsStyle(CalendarView.WEEK_STYLE,
					mClickDate);
		} else { // 月
			builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE,
					mClickDate);
		}
	}

	@Override
	public boolean giveUpTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		// if (scrollView.getScrollY() <= 0) {
		// // View view = mSlideListView.getChildAt(0);
		// //
		// // if (view != null && view.getTop() >= 0) {
		// return true;
		// // }
		// }

		if (mSlideListView.getFirstVisiblePosition() == 0) {

			View view = mSlideListView.getChildAt(0);

			if (view != null && view.getTop() >= 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onMoveOver(int status) {
		// TODO Auto-generated method stub
		Tools.getLog(Tools.i, "aaa",
				"onMoveOveronMoveOveronMoveOver  status =========== " + status);
		LinearLayout.LayoutParams btitlelayout = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, cellSpace * 6);
		btitlelayout.setMargins(0, 0, 0, 0);
		isMoving = false;
		viewPager.setLayoutParams(btitlelayout);
		if (status == StickyLayout.STATUS_COLLAPSED) { // 周
			builder.swtichCalendarViewsStyle(CalendarView.WEEK_STYLE,
					mClickDate);
		} else { // 月
			builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE,
					mClickDate);
		}

	}

	int maxHeight;
	int clickRow;

	boolean isMoving;

	@Override
	public void onMoving(int height, boolean isTouch) {
		// TODO Auto-generated method stub
		// 滑动中
		// 记录第一次height高度
		if (!isMoving) {
			if (!isTouch) {
				return;
			}
			isMoving = true;
			builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE,
					mClickDate);
		}
		int cellMax = maxHeight / cellSpace;
		int canMove = cellMax - 1;
		int canMoveHeight = (clickRow * cellSpace);
		int backHeight = (height - cellSpace) * clickRow / canMove;
		int offY = canMoveHeight - backHeight;
		LinearLayout.LayoutParams btitlelayout = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, cellSpace * 6);
		btitlelayout.setMargins(0, -offY, 0, 0);
		viewPager.setLayoutParams(btitlelayout);

	}

	/**
	 * 双击返回键退出系统 第一次点击时间
	 */
	private long firstClickTime = 0;
	/**
	 * 双击返回键退出程序 第二次点击时间
	 */
	private long secondClickTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		/**
		 * 双击返回键退出程序
		 */
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			secondClickTime = System.currentTimeMillis();

			if ((secondClickTime - firstClickTime) < (1000 * 2)) {
				/*
				 * 杀死程序
				 */
				System.exit(0);
			} else {
				Toast.makeText(this, "再按一次返回键退出应用", Toast.LENGTH_SHORT).show();
				firstClickTime = secondClickTime;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 打卡完成的Title
	 * 
	 * @param createJobBean
	 */
	public void showTitleView(CreateJobBean createJobBean, boolean isForGet) {
		String Strtaskid = settings.getString("taskid", null);
		Tools.getLog(Tools.d, "aaa", "看看存了什么把：" + Strtaskid);
		String taskid = createJobBean.getTaskId();
		String[] StrtaskidArray = null;
		boolean isHave = false;//
		if (Strtaskid != null) {
			if (Strtaskid.contains(",")) {
				StrtaskidArray = Strtaskid.split(",");
				for (int i = 0; i < StrtaskidArray.length; i++) {
					if (StrtaskidArray[i].equals(taskid)) {
						isHave = true;
						break;
					}
				}
				isHave(isHave, isForGet,createJobBean.getManCountToday());
				if (!isHave) {
					mEditor.putString("taskid", Strtaskid + "," + taskid);
					mEditor.commit();
				}
			} else {
				if (Strtaskid.equals(taskid)) {
					isHave(true, isForGet,createJobBean.getManCountToday());
				} else {
					isHave(false, isForGet,createJobBean.getManCountToday());
					mEditor.putString("taskid", Strtaskid + "," + taskid);
					mEditor.commit();

				}
			}
		} else {
			isHave(false, isForGet,createJobBean.getManCountToday());
			mEditor.putString("taskid", taskid);
			mEditor.commit();
		}
	}

	public void isHave(boolean isHave, boolean isForget, int manCountToday) {
		int todayJobCount = byDateBeanList.get(positioniItemClick)
				.getJobCount();
		int totalDateCount = byDateBeanList.get(positioniItemClick)
				.getTotalDateCount();
		taskId = byDateBeanList.get(positioniItemClick).getTaskId();
		//
		// if(!isHave){
		// byDateBeanList.get(positioniItemClick).setTotalDateCount(byDateBeanList.get(positioniItemClick).getTotalDateCount()+1);//
		// 修改上次打卡数量
		// }
		clock_main_alertText.setText("第" + totalDateCount + "天");
		// 功能为：今天第一天打卡显示天数，第二次打卡显示次数
		// if (isForget) {
		// if (totalDateCount == 0) {
		// // 今日已打卡
		// clock_main_alertText.setText("第" + todayJobCount + "次");
		// } else {
		// clock_main_alertText.setText("第" + totalDateCount + "天");
		// }
		// } else {
		// if (isHave) {
		// byDateBeanList.get(positioniItemClick).setTotalDateCount(
		// todayJobCount + 1);
		// byDateBeanList.get(positioniItemClick).setGiveUpFlag("0");
		// // 今日已打卡
		// clock_main_alertText.setText("第" + (todayJobCount + 1)
		// + "次");
		// } else {
		// byDateBeanList.get(positioniItemClick).setJobCount(
		// totalDateCount + 1);
		// byDateBeanList.get(positioniItemClick).setGiveUpFlag("0");
		// clock_main_alertText.setText("第" + (totalDateCount + 1)
		// + "天");
		// }
		// }

		// 显示dialog
		TakeClockSuccessDialog clockSuccessDialog;

		int index = 0;
		if (signature != null && signature.length() != 0) {
			index = 1;
		}
		if (quantity != null && quantity.length() != 0) {
			index = 1;

		}
		if (imageNum == 0) {
			clockSuccessDialog = new TakeClockSuccessDialog(mContext, index,
					manCountToday, createJobBean.getBuildingId(),
					taskTitle,createJobBean.getCheckDateCount());
		} else {
			clockSuccessDialog = new TakeClockSuccessDialog(mContext, 2,
					manCountToday, createJobBean.getBuildingId(),
					taskTitle,createJobBean.getCheckDateCount());
		}
		clockSuccessDialog.setOnCLickSuccessShare(onCLickSuccessShare);
		clockSuccessDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				ClickDataConn(today, null, null);// 刷新bydate集合
			}
		});
		clockSuccessDialog.show();

		// 显示下面的动画
		// shareTitle.setText("打卡成功！");
		// leftImage.setImageResource(R.drawable.home_dakachenggong);
		// shareImage.setVisibility(View.VISIBLE);
		// animAlertStart();
	}

	OnCLickSuccessShare onCLickSuccessShare = new OnCLickSuccessShare() {

		@Override
		public void onClickSuccess(int shareID) {
			// TODO Auto-generated method stub

			MyUMSDK myUMSDK = new MyUMSDK(mContext);
			String Signature = createJobBean.getSignature();
			String Quantity = createJobBean.getQuantity();
			StringBuffer sb = new StringBuffer();
			boolean isContentHave = false;
			sb.append("#" + (byDateBeanList.get(positioniItemClick).getTitle())
					+ "#");
			if (byDateBeanList.get(positioniItemClick).getJobCount() == 1) {
				sb.append("Day"
						+ (byDateBeanList.get(positioniItemClick)
								.getTotalDateCount() + 1) + " ");
			} else {
				sb.append("Day"
						+ byDateBeanList.get(positioniItemClick)
						.getTotalDateCount() + " ");

			}
			if (Signature != null && Signature.length() != 0) {
				sb.append(Signature);
				isContentHave = true;
			}
			if (Quantity != null && Quantity.length() != 0) {
				if (isContentHave) {
					sb.append(";");
				}
				sb.append(Quantity);

			}
			// sb.append(" #打卡7#");
			String shareUrl = "http://123.57.5.108:8087/architect/share?jobId="
					+ jobid;

			//			MobclickAgent.onEvent(mContext, " jobshareClick");
			switch (shareID) {
			case 0: // sina
				MobclickAgent.onEvent(mContext, "mainmodalWeiboclick");
				if (imageNum == 0) {
					myUMSDK.sinaUMShared(sb.toString(),
							"http://123.57.5.108:8087/architect/share?jobId="
									+ jobid, null, false,1);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
				} else {
					myUMSDK.sinaUMShared(sb.toString(),
							"http://123.57.5.108:8087/architect/share?jobId="
									+ jobid, mShareImageBitmap, false,1);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
				}
				break;
			case 1: // 朋友圈
				MobclickAgent.onEvent(mContext, "mainmodalWeChatclick");

				if (imageNum == 0) {
					myUMSDK.friendsterUMShared("打卡7", sb.toString(), shareUrl,
							mShareImageBitmap, false,1);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
				} else {
					myUMSDK.friendsterUMShared("打卡7", sb.toString(), shareUrl,
							null, false,1);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
				}
				break;
			case 2: // QQ空间
				MobclickAgent.onEvent(mContext, "mainmodalQQclick");
				if (imageNum == 0)
					myUMSDK.qzeroUMShared(mContext, sb.toString(), "打卡7",
							shareUrl, mShareImageBitmap,1);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
				else
					myUMSDK.qzeroUMShared(mContext, sb.toString(), "打卡7",
							shareUrl, null,1);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job

				break;
			}
			ClickDataConn(today, null, null);// 刷新bydate集合
		}
	};

	public void animAlertStart() {
		Tools.getLog(Tools.i, "aaa", "开始动画：");
		int height = clock_main_alertLayout.getHeight();
		Tools.getLog(Tools.i, "aaa", "animAlertStart height ============= "
				+ height);
		TranslateAnimation animationStart = new TranslateAnimation(0, 0,
				height, 0);

		animationStart.setDuration(500L);// 设置动画持续时间
		clock_main_alertLayout.startAnimation(animationStart);
		animationStart.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				clock_main_alertLayout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				clock_main_alertLayout.setVisibility(View.VISIBLE);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int height = clock_main_alertLayout.getHeight();
						Animation mAnimation = new TranslateAnimation(0, 0, 0,
								height);
						mAnimation.setDuration(250L);
						// building_dialog_Layout.setAnimation(mAnimation);
						clock_main_alertLayout.startAnimation(mAnimation);

						mAnimation.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation arg0) {
								// TODO Auto-generated method stub
								clock_main_alertLayout
								.setVisibility(View.VISIBLE);
							}

							@Override
							public void onAnimationRepeat(Animation arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animation arg0) {
								// TODO Auto-generated method stub
								clock_main_alertLayout
								.setVisibility(View.GONE);

							}
						});
					}
				}, 3000L);

			}
		});

	}

	@Override
	public void startVibrator() {
		// TODO Auto-generated method stub
		vibrator.vibrate(500);
	}

	//	PopupWindow popupWindow;
	//	int xoff;

	//	public void showPopupWindow() {
	//		// 加载布局
	//		LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(
	//				R.layout.popwindow_linearlayout, null);
	//		TextView fk = (TextView) layout.findViewById(R.id.popwindow_layout_fk);
	//		TextView setHeadImage = (TextView) layout
	//				.findViewById(R.id.popwindow_layout_setHeadImage);
	//
	//		LinearLayout popWindow = (LinearLayout) layout
	//				.findViewById(R.id.popWinLayout);
	//		// 实例化popupWindow
	//		popupWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
	//				LayoutParams.WRAP_CONTENT);
	//		// 控制键盘是否可以获得焦点
	//		popupWindow.setFocusable(true);
	//		popupWindow.setOutsideTouchable(true);
	//		// 设置popupWindow弹出窗体的背景
	//		popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(
	//				R.drawable.home_fk_background));
	//		WindowManager manager = (WindowManager) getSystemService(mContext.WINDOW_SERVICE);
	//		@SuppressWarnings("deprecation")
	//		// 获取xoff
	//		int xpos = manager.getDefaultDisplay().getWidth() / 2
	//		- popupWindow.getWidth() / 2;
	//		xoff = xpos;
	//		// xoff,yoff基于anchor的左下角进行偏移。
	//		popWindow.setOnClickListener(new OnClickListener() {
	//
	//			@Override
	//			public void onClick(View arg0) {
	//				// TODO Auto-generated method stub
	//				popupWindow.dismiss();
	//			}
	//		});
	//		fk.setOnClickListener(new OnClickListener() {
	//			// 反馈
	//			@Override
	//			public void onClick(View arg0) {
	//				// TODO Auto-generated method stub
	//				agent.sync();
	//				agent.startFeedbackActivity();
	//				popupWindow.dismiss();
	//			}
	//		});
	//		setHeadImage.setOnClickListener(new OnClickListener() {
	//			// 用户设置
	//			@Override
	//			public void onClick(View arg0) {
	//				// TODO Auto-generated method stub
	//				if (Contanst.isConnStop) {
	//					Toast.makeText(ClockMainActivity.this, "当前无无网络，请稍后再试",
	//							Toast.LENGTH_SHORT).show();
	//					return;
	//				}
	//				Intent in = new Intent(mContext, ClockSetHeadActivity.class);
	//				startActivity(in);
	//				popupWindow.dismiss();
	//			}
	//		});
	//
	//	}

	public void showDeleteDialog(final int position) {
		new AlertDialog.Builder(mContext)
		.setTitle("确认删除此卡？")
		.setMessage("删除后此卡所有数据将无法恢复")
		.setPositiveButton("删除", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				if (Contanst.isConnStop) {
					Toast.makeText(ClockMainActivity.this,
							"当前无无网络，请稍后再试", Toast.LENGTH_SHORT).show();
					return;
				}
				deleteTaskConn(position);
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		}).show();
	}

	private void deleteTaskConn(int position) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("taskId", byDateBeanList.get(
					position).getTaskId()));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_TASK_DELETE, null, null,
				ClockMainActivity.this).addList(params).request(UrlParams.POST);

	}

	public void GetAllAlarmList(){
		String str = "?userId="+userID;
		Service.getService(Contanst.HTTP_ALARM_GETBYUSERID, null, str,
				ClockMainActivity.this).addList(null).request(UrlParams.GET);
	}
	public void showAdDialog(){
		adDialog = new ADDialog(ClockMainActivity.this,userAdBean);
		if(!adDialog.isShowing()){
			
			adDialog.show();
		}
		
	}

	// private SlideView mLastSlideViewWithStatusOn;

	// @Override
	// public void onSlide(View view, int status) {
	// // TODO Auto-generated method stub
	// if (mLastSlideViewWithStatusOn != null
	// && mLastSlideViewWithStatusOn != view) {
	// mLastSlideViewWithStatusOn.shrink();
	// }
	//
	// if (status == SLIDE_STATUS_ON) {
	// mLastSlideViewWithStatusOn = (SlideView) view;
	// }
	// }
}
