package com.yktx.check;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AbsListView.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clock.service.AddShowPhotoService;
import com.duguang.baseanimation.ui.listivew.deletelistview.QiQiuUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.sso.UMSsoHandler;
import com.yktx.check.ClockMyActivity.MyPagerAdapter;
import com.yktx.check.adapter.MyexpandableListAdapter;
import com.yktx.check.adapter.MyexpandableListAdapter.SharedThisJob;
import com.yktx.check.adapter.MyexpandableListAdapter.TaskInfoOnClick;
import com.yktx.check.adapter.MyexpandableListAdapter.giveUpFlagClick;
import com.yktx.check.bean.ByDateBean;
import com.yktx.check.bean.ByIdDetailBean;
import com.yktx.check.bean.ByTaskIdBean;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.bean.CreateJobBean;
import com.yktx.check.bean.Group;
import com.yktx.check.bean.ImageListBean;
import com.yktx.check.bean.JobStatsBean;
import com.yktx.check.bean.LastTwoJobBean;
import com.yktx.check.bean.LastTwoUsersBean;
import com.yktx.check.bean.MsgToUserBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.bean.VotesBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.dialog.AddCommentDialog;
import com.yktx.check.dialog.AddCommentDialog.onCLickCommentSuccess;
import com.yktx.check.dialog.AllTaskFinishDialog2;
import com.yktx.check.dialog.GiveUpJobDialog;
import com.yktx.check.dialog.TakeClockDialog;
import com.yktx.check.dialog.TakeClockSuccessDialog;
import com.yktx.check.dialog.TaskInfoDialog;
import com.yktx.check.dialog.TakeClockDialog.TaskClockDialogOnCLickClockSuccess;
import com.yktx.check.dialog.TakeClockSuccessDialog.OnCLickSuccessShare;
import com.yktx.check.dialog.TaskInfoDialog.onCLickClockSuccess;
import com.yktx.check.fragment.ClockFansFragment;
import com.yktx.check.fragment.ClockFollowFragment;
import com.yktx.check.fragment.ClockMyFragment;
import com.yktx.check.fragment.ClockMyJobListFragment;
import com.yktx.check.listview.PinnedHeaderExpandableListView;
import com.yktx.check.listview.PinnedHeaderExpandableListView.IXListViewListener;
import com.yktx.check.listview.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.yktx.check.listview.PinnedHeaderExpandableListView.setClickHeadViewlistener;
import com.yktx.check.service.Service;
import com.yktx.check.square.fragment.BoutiqueFragment;
import com.yktx.check.taskinfo.fragment.TaskGridViewFragment;
import com.yktx.check.taskinfo.fragment.TaskInfoFragment;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.FileURl;
import com.yktx.check.util.ImageTool;
import com.yktx.check.util.MyUMSDK;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;
import com.yktx.check.widget.CalendarView;
import com.yktx.check.widget.OldPagerSlidingTabStrip;
import com.yktx.sqlite.DBHelper;

public class TaskInfoActivity extends FragmentActivity implements
		ServiceListener {

	public static String mTaskId;
	public static String mTotalCheckCount;
	public static String mTotalDateCount;
	public static String mDescription;
	private String mTitleContent;
	private LastTwoUsersBean lastTwoJobBean = new LastTwoUsersBean();
	// private XListView infoListView;
	// private MyexpandableListAdapter myExpandableListAdapter;
	// private TaskInfoAdapter adapter;
	private ImageView title_item_leftImage, title_item_createJob;
	public static ByIdDetailBean byIdDetailBean;

	ArrayList<JobStatsBean> jobList = new ArrayList<JobStatsBean>(10);
	boolean isConn, isReflush = true;
	int mClickPosition;
	AddCommentDialog dialog;
	public DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.zw_image)
			.showImageOnLoading(R.anim.loading_image_animation)
			.showImageOnFail(R.drawable.zw_image).showImageOnLoading(null)
			.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
			.cacheOnDisk(true)
			// .displayer(new RoundedBitmapDisplayer(400))
			.cacheInMemory(false)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

	private MyUMSDK myShare;
	private String thisJobUserid;
	TakeClockDialog taskClockDialog;
	private TextView shareTitle, clock_main_alertText;
	private ImageView leftImage;
	private RelativeLayout clock_main_alertLayout;

	private boolean isOther;
	boolean isCannotDaka;

	QiQiuUtils qiQiuUtils;
	FrameLayout donghua;
	boolean isAlone;

	DBHelper dbHelper;
	private ArrayList<ByDateBean> byDateBeanList = new ArrayList<ByDateBean>();
	String today;

	OldPagerSlidingTabStrip taskInfo_tabs;
	ViewPager taskInfo_pager;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;
	private SharedPreferences settings;
	private Editor mEditor;
	private String userID;
	private Activity mContext;
	/**
	 * 日历
	 */
	TaskGridViewFragment gridViewFragment;

	/**
	 * task详情
	 */
	TaskInfoFragment taskInfoFragment;

	TextView taskinfo_top_layout_name, taskinfo_top_layout_State,
			taskinfo_top_layout_info, taskUserName, taskUserMaxNum;

	ImageView taskinfo_top_layout_Image, taskinfo_top_layout_Yinsi,
			taskinfo_top_layout_progress1, taskinfo_top_layout_progress2,
			taskinfo_top_layout_progress3, taskinfo_top_layout_progress4,
			taskinfo_top_layout_progress5, taskinfo_top_layout_progress6,
			taskinfo_top_layout_progress7, taskinfo_top_layout_set;

	RelativeLayout taskLastTwoTitleLayout, loadingView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_task_info);

		dm = getResources().getDisplayMetrics();
		settings = getSharedPreferences("clock", MODE_PRIVATE);
		mEditor = settings.edit();
		userID = settings.getString("userid", null);
		mContext = TaskInfoActivity.this;

		findViews();
		init();
		setListeners();
	}

	protected void findViews() {
		// TODO Auto-generated method stub

		mTaskId = getIntent().getStringExtra("taskid");
		thisJobUserid = getIntent().getStringExtra("userid");
		isCannotDaka = getIntent().getBooleanExtra("isCannotDaka", false);
		if (thisJobUserid == null || thisJobUserid.length() == 0) {
			thisJobUserid = userID;
		}
		Tools.getLog(Tools.d, "aaa", "mTotalDateCount" + mTotalCheckCount
				+ ",currentStreak" + mTotalDateCount);
		title_item_leftImage = (ImageView) findViewById(R.id.title_item_leftImage);
		title_item_createJob = (ImageView) findViewById(R.id.title_item_createJob);

		clock_main_alertLayout = (RelativeLayout) findViewById(R.id.clock_main_alertLayout);
		clock_main_alertText = (TextView) findViewById(R.id.clock_main_alertText);
		shareTitle = (TextView) findViewById(R.id.shareTitle);
		leftImage = (ImageView) findViewById(R.id.leftImage);

		taskInfo_tabs = (OldPagerSlidingTabStrip) findViewById(R.id.taskinfo_tabs);
		taskInfo_pager = (ViewPager) findViewById(R.id.taskinfo_pager);

		taskinfo_top_layout_name = (TextView) findViewById(R.id.taskinfo_top_layout_name);
		taskinfo_top_layout_State = (TextView) findViewById(R.id.taskinfo_top_layout_State);
		taskinfo_top_layout_info = (TextView) findViewById(R.id.taskinfo_top_layout_info);
		taskUserName = (TextView) findViewById(R.id.taskUserName);
		taskUserMaxNum = (TextView) findViewById(R.id.taskUserMaxNum);
		taskLastTwoTitleLayout = (RelativeLayout)findViewById(R.id.taskLastTwoTitleLayout);
		taskinfo_top_layout_Image = (ImageView) findViewById(R.id.taskinfo_top_layout_Image);
		taskinfo_top_layout_Yinsi = (ImageView) findViewById(R.id.taskinfo_top_layout_Yinsi);
		taskinfo_top_layout_progress1 = (ImageView) findViewById(R.id.taskinfo_top_layout_progress1);
		taskinfo_top_layout_progress2 = (ImageView) findViewById(R.id.taskinfo_top_layout_progress2);
		taskinfo_top_layout_progress3 = (ImageView) findViewById(R.id.taskinfo_top_layout_progress3);
		taskinfo_top_layout_progress4 = (ImageView) findViewById(R.id.taskinfo_top_layout_progress4);
		taskinfo_top_layout_progress5 = (ImageView) findViewById(R.id.taskinfo_top_layout_progress5);
		taskinfo_top_layout_progress6 = (ImageView) findViewById(R.id.taskinfo_top_layout_progress6);
		taskinfo_top_layout_progress7 = (ImageView) findViewById(R.id.taskinfo_top_layout_progress7);
		taskinfo_top_layout_set = (ImageView) findViewById(R.id.taskinfo_top_layout_set);

		loadingView = (RelativeLayout) findViewById(R.id.loadingView);

		donghua = new FrameLayout(mContext);
		qiQiuUtils = new QiQiuUtils(donghua, mContext);

		dbHelper = new DBHelper(mContext);

	}

	protected void init() {
		// TODO Auto-generated method stub

		today = TimeUtil.getYYMMDD(System.currentTimeMillis());
		byDateBeanList = dbHelper.getTaskList();
		myShare = new MyUMSDK(mContext);
		isOther = getIntent().getBooleanExtra("isother", false);
		if (isCannotDaka) {
			title_item_createJob.setVisibility(View.GONE);
		}

		if (isOther || !thisJobUserid.equals(userID)) {
			isOther = true;
			title_item_createJob.setVisibility(View.GONE);
		}
		getByIdConn();// 个人信息

	}

	private int curTabIndex;
	TaskInfoDialog taskDialog;
	String sharedialogStr;
	private onCLickClockSuccess mCLickClockSuccess = new onCLickClockSuccess() {
		// 详情分享的dialog
		@Override
		public void onClickSuccess(int index) {
			// TODO Auto-generated method stub

			switch (index) {
			case 0: // 新浪
				if (isShareImage) {

					// if(isOther){
					if (isAlone) {
						MobclickAgent.onEvent(mContext,
								"detailJobshareWeiboclick");
						myShare.sinaUMShared(sharedialogStr, shareTaskUrl,
								shareBitmap, true, 4);// 最后一个参数是那一页 1 为主页打卡成功
														// 2为打卡成就 3详情页Task打卡成就
														// 4详情页Job
					} else {
						MobclickAgent.onEvent(mContext,
								"detailTaskshareWeiboclick");
						myShare.sinaUMShared(sharedialogStr, shareTaskUrl,
								shareBitmap, true, 3);// 最后一个参数是那一页 1 为主页打卡成功
														// 2为打卡成就 3详情页Task打卡成就
														// 4详情页Job
					}

				} else {
					if (isAlone) {
						MobclickAgent.onEvent(mContext,
								"detailJobshareWeiboclick");
						myShare.sinaUMShared(sharedialogStr, shareTaskUrl,
								shareBitmap, true, 4);// 最后一个参数是那一页 1 为主页打卡成功
														// 2为打卡成就 3详情页Task打卡成就
														// 4详情页Job
					} else {
						MobclickAgent.onEvent(mContext,
								"detailTaskshareWeiboclick");
						myShare.sinaUMShared(sharedialogStr, shareTaskUrl,
								shareBitmap, true, 3);// 最后一个参数是那一页 1 为主页打卡成功
														// 2为打卡成就 3详情页Task打卡成就
														// 4详情页Job
					}
				}

				Toast.makeText(mContext, "分享成功！", Toast.LENGTH_SHORT).show();
				taskDialog.dismiss();
				break;
			case 1: // 朋友圈
				if (isShareImage)
					if (isAlone) {
						MobclickAgent.onEvent(mContext,
								"detailTaskshareWeChatclick");
						myShare.friendsterUMShared("打卡7", sharedialogStr,
								shareTaskUrl, shareBitmap, false, 4);// 最后一个参数是那一页
																		// 1
																		// 为主页打卡成功
																		// 2为打卡成就
																		// 3详情页Task打卡成就
																		// 4详情页Job
					} else {
						MobclickAgent.onEvent(mContext,
								"detailJobshareWeChatclick");
						myShare.friendsterUMShared("打卡7", sharedialogStr,
								shareTaskUrl, shareBitmap, false, 3);// 最后一个参数是那一页
																		// 1
																		// 为主页打卡成功
																		// 2为打卡成就
																		// 3详情页Task打卡成就
																		// 4详情页Job
					}

				else if (isAlone) {
					MobclickAgent.onEvent(mContext,
							"detailTaskshareWeChatclick");
					myShare.friendsterUMShared("打卡7", sharedialogStr,
							shareTaskUrl, null, false, 4);// 最后一个参数是那一页 1
															// 为主页打卡成功 2为打卡成就
															// 3详情页Task打卡成就
															// 4详情页Job
				} else {
					MobclickAgent
							.onEvent(mContext, "detailJobshareWeChatclick");
					myShare.friendsterUMShared("打卡7", sharedialogStr,
							shareTaskUrl, null, false, 3);// 最后一个参数是那一页 1
															// 为主页打卡成功 2为打卡成就
															// 3详情页Task打卡成就
															// 4详情页Job
				}

				taskDialog.dismiss();
				break;
			case 2: // QQ空间
				MobclickAgent.onEvent(mContext, "mainmodalQQclick");
				if (isShareImage)
					if (isAlone) {
						MobclickAgent.onEvent(mContext,
								"detailTaskshareQQclick");
						myShare.qzeroUMShared(mContext, sharedialogStr, "打卡7",
								shareTaskUrl, shareBitmap, 4);// 最后一个参数是那一页 1
																// 为主页打卡成功
																// 2为打卡成就
																// 3详情页Task打卡成就
																// 4详情页Job
					} else {
						MobclickAgent
								.onEvent(mContext, "detailJobshareQQclick");
						myShare.qzeroUMShared(mContext, sharedialogStr, "打卡7",
								shareTaskUrl, shareBitmap, 3);// 最后一个参数是那一页 1
																// 为主页打卡成功
																// 2为打卡成就
																// 3详情页Task打卡成就
																// 4详情页Job
					}

				else if (isAlone) {
					MobclickAgent.onEvent(mContext, "detailTaskshareQQclick");
					myShare.qzeroUMShared(mContext, sharedialogStr, "打卡7",
							shareTaskUrl, null, 4);// 最后一个参数是那一页 1 为主页打卡成功
													// 2为打卡成就 3详情页Task打卡成就
													// 4详情页Job
				} else {
					MobclickAgent.onEvent(mContext, "detailJobshareQQclick");
					myShare.qzeroUMShared(mContext, sharedialogStr, "打卡7",
							shareTaskUrl, null, 3);// 最后一个参数是那一页 1 为主页打卡成功
													// 2为打卡成就 3详情页Task打卡成就
													// 4详情页Job
				}

				taskDialog.dismiss();
				break;

			case 3: // 邀请
				// inviteDialog();
				break;

			case 4: // 设置
				Intent intent = new Intent(mContext, ClockSetActivity.class);
				if (byIdDetailBean != null) {
					intent.putExtra("byid", byIdDetailBean);
				}
				startActivityForResult(intent, 111);
				taskDialog.dismiss();
				break;

			case 5: // 删除
				showdialogFinish();
				taskDialog.dismiss();
				break;

			}

		}
	};

	private void showdialogFinish() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(this, R.style.CustomDiaLog_by_SongHang));
		builder.setTitle("提示");
		builder.setMessage("是否确认删除？之前打卡数据将无法恢复");
		builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// deleteDialog();
				deleteTaskConn();
			}
		});
		builder.setNeutralButton("暂停", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, ClockSetActivity.class);
				if (byIdDetailBean != null) {
					intent.putExtra("byid", byIdDetailBean);
				}
				startActivityForResult(intent, 111);
			}
		});
		builder.setNegativeButton("返回", null);
		builder.show();
	}

	boolean isShareImage;
	Bitmap shareBitmap;
	String shareTaskUrl = "", shareTaskStr;

	private void deleteTaskConn() {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("taskId", mTaskId));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_TASK_DELETE, null, null,
				TaskInfoActivity.this).addList(params).request(UrlParams.POST);

	}

	protected void setListeners() {
		// TODO Auto-generated method stub
		title_item_leftImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		title_item_createJob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 打卡
				if (byIdDetailBean.getTimeLimitFlag() == "1") {
					String today = TimeUtil.getYYMMDD(System
							.currentTimeMillis());
					String startTime = today + " "
							+ byIdDetailBean.getBeginTime() + ":00";
					String endTime = today + " " + byIdDetailBean.getEndTime()
							+ ":00";
					Tools.getLog(Tools.d, "aaa", "start:" + startTime);
					Tools.getLog(Tools.d, "aaa", "end:" + endTime);
					if (TimeUtil.getUnixLong(startTime) >= System
							.currentTimeMillis()
							|| TimeUtil.getUnixLong(endTime) <= System
									.currentTimeMillis()) {
						shareTitle.setText("打卡失败！");
						leftImage.setImageResource(R.drawable.home_dakashibai);
						clock_main_alertText.setText("请在时间内打卡。");
						Message msg = new Message();
						msg.what = Contanst.CreateJobFail;
						mHandler.sendMessage(msg);
						animAlertStart();
						return;
					}
				}
				taskClockDialog = new TakeClockDialog(TaskInfoActivity.this);
				taskClockDialog.setOnClickClockSuccess(cLickClockSuccess);
				taskClockDialog.setTaskNameStr(byIdDetailBean.getTitle());
				taskClockDialog.show();
			}
		});
	}

	public ArrayList<ImageListBean> filenames = new ArrayList<ImageListBean>(3);// 相册选取的集合的名字
	String jobid;
	boolean isShowAllTast;
	int imageNum = 0;// 上传图片数量

	String signature, quantity;
	TaskClockDialogOnCLickClockSuccess cLickClockSuccess = new TaskClockDialogOnCLickClockSuccess() {

		@Override
		public void onClickSuccess(String content, String num, String unit,
				ArrayList<ImageListBean> list) {
			filenames = list;

			signature = content;
			quantity = num;
			connBaiduLocation();

		}
	};

	private Vibrator vibrator;

	private void jobSound() {
		vibrator.vibrate(500); // 重复两次上面的pattern 如果只想震动一次，index设为
		SoundPool soundPool;
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);

		soundPool.load(this, R.raw.a6, 1);
		soundPool.play(1, 1, 1, 0, 0, 1);
	}

	public void animAlertStart() {
		int height = clock_main_alertLayout.getHeight();
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
						// clock_main_alertLayout.setAnimation(mAnimation);
						clock_main_alertLayout.startAnimation(mAnimation);

						mAnimation
								.setAnimationListener(new AnimationListener() {

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

	GiveUpJobDialog upJobDialog;
	boolean isToday;

	public void getLastTwoJobsConn() {
		StringBuffer sb = new StringBuffer();
		sb.append("?taskId=");
		sb.append(mTaskId);
		Service.getService(Contanst.HTTP_BUILDING_GETLASTTWOUSERS, null,
				sb.toString(), TaskInfoActivity.this).addList(null)
				.request(UrlParams.GET);
	}

	private void getByIdConn() {
		StringBuffer sb = new StringBuffer();
		sb.append("?taskId=");
		sb.append(mTaskId);

		Service.getService(Contanst.HTTP_GETBYIDTASK, null, sb.toString(),
				TaskInfoActivity.this).addList(null).request(UrlParams.GET);
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
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {

				case Contanst.BAIDU_LOCATION:
					createJobConn((String) msg.obj);
					break;

				case Contanst.GETSTATISTIC:
					// jobList = (ArrayList<JobStatsBean>) msg.obj;
					curTabIndex = 1;

					break;
				case Contanst.BUILDING_GETLASTTWOUSERS:
					lastTwoJobBean = (LastTwoUsersBean) msg.obj;
					showTitle(byIdDetailBean);

					Tools.getLog(Tools.i, "aaa", "刷新！！！！！！");
					// getByTaskIdConn(1);
					if (loadingView.getVisibility() == View.VISIBLE) {
						loadingView.setVisibility(View.GONE);
					}
					break;
				case Contanst.GETBYIDTASK:
					byIdDetailBean = (ByIdDetailBean) msg.obj;
					setOverflowShowingAlways();
					taskInfo_pager.setAdapter(new MyPagerAdapter(
							getSupportFragmentManager()));
					taskInfo_tabs.setViewPager(taskInfo_pager);
					setTabsValue();
					getLastTwoJobsConn();
					break;
				case Contanst.GET_MSGTOUSER:
					MsgToUserBean msgBean = (MsgToUserBean) msg.obj;
					Tools.getLog(Tools.d, "aaa",
							"=================Contanst.GET_MSGTOUSER:==="
									+ msgBean.toString());
					break;
				case Contanst.TASK_DELETE:
					TaskInfoActivity.this.finish();
					break;
				case Contanst.CREATEJOB:
					createJobBean = (CreateJobBean) msg.obj;
					for (int i = 0; i < byDateBeanList.size(); i++) {
						if (byDateBeanList.get(i).getTaskId()
								.equals(createJobBean.getTaskId())) {
							byDateBeanList.get(i).setJobCount(
									byDateBeanList.get(i).getJobCount() + 1);
						}
					}
					Tools.getLog(Tools.i, "aaa", "打卡成功！！！！！！！！");
					if (filenames.size() > 0) {
						Intent intent_baonew = new Intent(
								TaskInfoActivity.this,
								AddShowPhotoService.class);
						intent_baonew.putExtra("state",
								AddShowPhotoService.AddShowPhotoRun);
						intent_baonew.putExtra("productid", userID);
						intent_baonew.putExtra("list", filenames);
						intent_baonew.putExtra("uuid", jobid);
						intent_baonew.putExtra("type", 1);
						startService(intent_baonew);
					}
					TakeClockSuccessDialog clockSuccessDialog;

					int index = 0;

					if (signature != null && signature.length() != 0) {
						index = 1;
					}
					if (quantity != null && quantity.length() != 0) {
						index = 1;
					}
					if (imageNum == 0) {
						clockSuccessDialog = new TakeClockSuccessDialog(
								mContext, index,
								createJobBean.getManCountToday(),
								createJobBean.getBuildingId(),
								byIdDetailBean.getTitle(),
								createJobBean.getCheckDateCount());
					} else {
						clockSuccessDialog = new TakeClockSuccessDialog(
								mContext, 2, createJobBean.getManCountToday(),
								createJobBean.getBuildingId(),
								byIdDetailBean.getTitle(),
								createJobBean.getCheckDateCount());
					}
					clockSuccessDialog
							.setOnCLickSuccessShare(onCLickSuccessShare);
					clockSuccessDialog
							.setOnDismissListener(new OnDismissListener() {

								@Override
								public void onDismiss(DialogInterface arg0) {
									// TODO Auto-generated method stub
									Tools.getLog(Tools.d, "aaa",
											"clockSuccessDialog ===================== setOnDismissListener");
									updateDateColor();
								}
							});
					clockSuccessDialog.show();
					getByIdConn();// 个人信息
					isReflush = true;
					isConn = true;
					break;

				}
				break;
			case Contanst.BEST_INFO_FAIL:// 通过Id获得详细信息的
				String message = (String) msg.obj;
				switch (msg.arg1) {

				case Contanst.BAIDU_LOCATION:
					createJobConn(null);
					break;

				case Contanst.BUILDING_GETLASTTWOUSERS:
					break;
				case Contanst.GETBYTASKID:
					onLoad();
					break;
				case Contanst.GETBYIDTASK:
					break;
				}
				break;
			}
		}
	};

	CreateJobBean createJobBean;
	OnCLickSuccessShare onCLickSuccessShare = new OnCLickSuccessShare() {
		// 拍照打卡的Dialog
		@Override
		public void onClickSuccess(final int shareID) {
			// TODO Auto-generated method stub

			final MyUMSDK myUMSDK = new MyUMSDK(TaskInfoActivity.this);
			final StringBuffer sb = new StringBuffer();
			boolean isContentHave = false;
			sb.append("#" + byIdDetailBean.getTitle() + "#");
			sb.append("Day1");
			String Signature = createJobBean.getSignature();
			String Quantity = createJobBean.getQuantity();
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
			Tools.getLog(Tools.d, "aaa", "shareID:" + shareID);
			final String shareUrl = "http://123.57.5.108:8087/architect/share?jobId="
					+ jobid;
			if (imageNum > 0) {
				String url = "";
				for (int i = 0; i < filenames.size(); i++) {
					if (filenames.get(i).getIsCheck()) {
						url = FileURl.LOAD_FILE
								+ filenames.get(i).getImageUrl();
					}
				}
				// 最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
				ImageLoader.getInstance().loadImage(url,
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								// TODO Auto-generated method stub
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								// TODO Auto-generated method stub
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap mShareImageBitmap) {
								// TODO Auto-generated method stub

								switch (shareID) {
								case 0: // sina
									MobclickAgent.onEvent(mContext,
											"mainmodalWeiboclick");
									myUMSDK.sinaUMShared(sb.toString(),
											shareUrl, mShareImageBitmap, false,
											1);
									// }
									break;
								case 1: // 朋友圈
									// if (imageNum == 0) {
									MobclickAgent.onEvent(mContext,
											"mainmodalWeChatclick");
									myUMSDK.friendsterUMShared("打卡7",
											sb.toString(), shareUrl,
											mShareImageBitmap, false, 1);
									break;
								case 2: // QQ空间
									// if (imageNum == 0){
									MobclickAgent.onEvent(mContext,
											"mainmodalQQclick");
									int i = myUMSDK.qzeroUMShared(mContext,
											sb.toString(), "打卡7", shareUrl,
											mShareImageBitmap, 1);
									Tools.getLog(Tools.d, "aaa", "i:" + i);
									break;
								}
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
								// TODO Auto-generated method stub

							}
						});

			} else {

				switch (shareID) {
				case 0: // sina
					// if (imageNum == 0) {
					myUMSDK.sinaUMShared(sb.toString(), shareUrl, null, false,
							1);
					break;
				case 1: // 朋友圈
					// if (imageNum == 0) {
					myUMSDK.friendsterUMShared("打卡7", sb.toString(), shareUrl,
							null, false, 1);
					break;
				case 2: // QQ空间
					// if (imageNum == 0){
					int i = myUMSDK.qzeroUMShared(mContext, sb.toString(),
							"打卡7", shareUrl, null, 1);
					Tools.getLog(Tools.d, "aaa", "i:" + i);

					break;
				}
			}

		}
	};

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
			}
			return;
		}
		Tools.getLog(Tools.d, "aaa", "设置的返回！！！！！！！！");
		if (requestCode == 111) {
			if (resultCode == 222) {
				finish();
				return;
			}
			getByIdConn();
			return;
		}

		Tools.getLog(Tools.i, "aaa", "Activity.RESULT_OK === "
				+ Activity.RESULT_OK);
		Tools.getLog(Tools.i, "aaa", "resultCode === " + resultCode);
		Bitmap bitmap = null;
		String ImageUrl = null;
		if (resultCode == Activity.RESULT_OK) {
			filenames = taskClockDialog.getFilenames();
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Tools.getLog(Tools.i, "aaa",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			if (requestCode == taskClockDialog.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE) {
				Bitmap photo = null;
				// 根据需要，也可以加上Option这个参数
				try {
					String path = FileURl.ImageFilePath + "/"
							+ taskClockDialog.cameraPhotoName;

					int degree = ImageTool.getBitmapDegree(path);
					Tools.getLog(Tools.i, "aaa", "degree ============ "
							+ degree);

					FileInputStream in = new FileInputStream(path);

					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 10;

					photo = ImageTool.rotateBitMap(
							BitmapFactory.decodeStream(in, null, options),
							degree);

					// TODO Auto-generated catch block
					if (ImageTool.saveBitmapToAlbum(TaskInfoActivity.this,
							photo)) {
						taskClockDialog.reflashCanmera();
					}

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					ImageLoader.getInstance().clearDiskCache();
					ImageLoader.getInstance().clearMemoryCache();
				}

				// }
			} else {
				Tools.getLog(Tools.d, "aaa", "返回相册选着的照片！！！！");
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

				taskClockDialog.reflashList(filenames);
			}
			Tools.getLog(Tools.d, "aaa", "图片的转换：" + ImageUrl);
		}
		if (requestCode == taskClockDialog.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE) {
			Tools.getLog(Tools.d, "aaa", "从相册选择完图片的情况：" + filenames.toString());
			for (int i = filenames.size() - 1; i >= 3; i--) {
				filenames.remove(i);
			}
		}

	}

	private void onLoad() {
		// infoListView.stopRefresh();
		isConn = false;
		isReflush = false;
	}

	public void showTitle(final ByIdDetailBean byIdDetailBean) {
		mDescription = byIdDetailBean.getDescription();
		taskinfo_top_layout_name.setText(byIdDetailBean.getTitle());
		ImageLoader.getInstance().displayImage(
				Tools.getImagePath(byIdDetailBean.getBadgeSource())
						+ byIdDetailBean.getBadgePath(),
				taskinfo_top_layout_Image, options);
		if (byIdDetailBean.getPrivateFlag().equals("1")) {
			taskinfo_top_layout_Yinsi.setVisibility(View.VISIBLE);
		} else {
			taskinfo_top_layout_Yinsi.setVisibility(View.GONE);
		}
		String description = byIdDetailBean.getDescription();
		if (description != null && description.length() != 0) {
			taskinfo_top_layout_State.setText(description);
			taskinfo_top_layout_State.setVisibility(View.VISIBLE);
		} else {
			if (isOther) {
				taskinfo_top_layout_State.setText("Ta很懒还没有编写卡片说明");
			} else {
				taskinfo_top_layout_State.setText("点击右边设置按钮编写卡片说明");
			}

		}
		StringBuffer sb = new StringBuffer();
		String checkNum = byIdDetailBean.getTotalCheckCount();
		String dateNum = byIdDetailBean.getTotalDateCount();
		String point = byIdDetailBean.getPoint();

		if (checkNum != null && !checkNum.equals("0")) {
			sb.append(checkNum + "次");
		}
		if (dateNum != null && !dateNum.equals("0")) {
			sb.append("  ");
			sb.append(dateNum + "天");
		}
		if (point != null && !point.equals("0")) {
			sb.append("  ");
			sb.append(point + "个气球");
		}

		if (lastTwoJobBean.getUsers().equals("")) {
			taskLastTwoTitleLayout.setVisibility(View.GONE);
		} else {
			taskLastTwoTitleLayout.setVisibility(View.VISIBLE);
		}
		taskUserName.setText(lastTwoJobBean.getUsers());
		taskUserMaxNum.setText("等" + lastTwoJobBean.getTotalManCount()
				+ "人也在打卡");

		int progress = byIdDetailBean.getProgress();
		switch (progress) {
		case 0:
			taskinfo_top_layout_progress1.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress2.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress3.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress4.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress5.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress6.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress7.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 1:
			taskinfo_top_layout_progress1.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress2.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress3.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress4.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress5.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress6.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress7.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 2:
			taskinfo_top_layout_progress1.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress2.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress3.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress4.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress5.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress6.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress7.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 3:
			taskinfo_top_layout_progress1.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress2.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress3.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress4.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress5.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress6.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress7.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 4:
			taskinfo_top_layout_progress1.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress2.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress3.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress4.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress5.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress6.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress7.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 5:
			taskinfo_top_layout_progress1.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress2.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress3.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress4.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress5.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress6.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			taskinfo_top_layout_progress7.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 6:
			taskinfo_top_layout_progress1.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress2.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress3.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress4.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress5.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress6.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1));
			taskinfo_top_layout_progress7.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_1_light));
			break;
		}

		taskinfo_top_layout_info.setText(sb.toString());
		taskinfo_top_layout_set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				shareTaskUrl = "http://123.57.5.108:8087/architect/taskAchievement?taskId="
						+ mTaskId;
				isAlone = false;
				taskDialog = new TaskInfoDialog(TaskInfoActivity.this, false);
				taskDialog.setOnClickClockSuccess(mCLickClockSuccess);
				sharedialogStr = "#" + byIdDetailBean.getTitle() + "#已累计坚持"
						+ byIdDetailBean.getTotalDateCount() + "天";
				taskDialog.show();
				isShareImage = false;
				shareBitmap = null;
			}
		});
		taskLastTwoTitleLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Tools.getLog(Tools.d, "aaa", "点击更多进入buliding页！");
				Intent in = new Intent(mContext,
						ClockGroupInfoFragmentActivity.class);
				in.putExtra("buildingId", lastTwoJobBean.getBuildingId());
				mContext.startActivity(in);
			}
		});

	}

	private void createJobConn(String city) {
		// TODO Auto-generated method stub

		imageNum = 0;
		String url = "";
		for (int i = 0; i < filenames.size(); i++) {
			if (filenames.get(i).getIsCheck()) {
				url = FileURl.LOAD_FILE + filenames.get(i).getImageUrl();
				imageNum++;

			}
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			jobid = Tools.getUuid();
			params.add(new BasicNameValuePair("jobId", jobid));
			params.add(new BasicNameValuePair("taskId", byIdDetailBean.getId()));
			if (signature != null && signature.length() != 0) {
				params.add(new BasicNameValuePair("signature", signature));
			}
			if (quantity != null && quantity.length() != 0) {
				params.add(new BasicNameValuePair("quantity", quantity));
			}

			params.add(new BasicNameValuePair("giveUpFlag", "0"));
			params.add(new BasicNameValuePair("picNum", imageNum + ""));
			params.add(new BasicNameValuePair("checkTime", System
					.currentTimeMillis() + ""));
			params.add(new BasicNameValuePair("onlineFlag", "1"));
			params.add(new BasicNameValuePair("clientLocalTime", System
					.currentTimeMillis() + ""));
			params.add(new BasicNameValuePair("currentGiveUpFlag", "0"));

			if (city != null) {
				params.add(new BasicNameValuePair("city", city));
				params.add(new BasicNameValuePair("longitude", longitude));
				params.add(new BasicNameValuePair("latitude", latitude));
			}
			Tools.getLog(Tools.i, "aaa", "params ======================= "
					+ params.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_CREATEJOB, null, null,
				TaskInfoActivity.this).addList(params).request(UrlParams.POST);
		if (!settings.getBoolean("sound", false)) {
			jobSound();
		}

	}

	String latitude, longitude;

	private void connBaiduLocation() {
		latitude = settings.getString("latitude", "-1");
		longitude = settings.getString("longitude", "-1");
		if (latitude.equals("-1")) {
			createJobConn(null);
		} else {
			Service.getService(Contanst.HTTP_BAIDU_LOCATION, null,
					latitude + "," + longitude, TaskInfoActivity.this)
					.addList(null).request(UrlParams.GET);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		MobclickAgent.onPageStart("SplashScreen"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		vibrator.cancel();
	}

	private void updateDateColor() {
		int total = 0;
		for (int i = 0; i < byDateBeanList.size(); i++) {
			Tools.getLog(Tools.d, "aaa",
					"byDateBeanList.get(i).getJobCount()====="
							+ byDateBeanList.get(i).getJobCount());

			if (byDateBeanList.get(i).getJobCount() > 0) {
				total++;
			}
		}
		if (total == 0) {
			ClockMainActivity.curMap.put(today, "0");
		} else if (total == byDateBeanList.size()) {
			// 今天的卡全部打完
			if (ClockMainActivity.curMap == null) {
				return;
			}

			if (!ClockMainActivity.curMap.get(today).equals("2")) {
				ClockMainActivity.curMap.put(today, "2");
				AllTaskFinishDialog2 allDialog = new AllTaskFinishDialog2(
						TaskInfoActivity.this);
				allDialog.show();
				allDialog.shareImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						MobclickAgent.onEvent(mContext,
								"mainhomepageshareclick");
						Intent in = new Intent(mContext,
								ShareAchievementActivity.class);
						in.putExtra("userid", userID);
						startActivity(in);
						overridePendingTransition(R.anim.slide_alpha_in_right,
								R.anim.slide_alpha_out_left);
					}
				});
			}

		} else {
			ClockMainActivity.curMap.put(today, "1");
		}
	}

	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");

			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		taskInfo_tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		taskInfo_tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		taskInfo_tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 0, dm));
		// 设置Tab Indicator的高度
		taskInfo_tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		taskInfo_tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		taskInfo_tabs.setSelectTextSice((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		taskInfo_tabs.setIndicatorColor(getResources().getColor(
				R.color.meibao_color_1));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		taskInfo_tabs.setSelectedTextColor(getResources().getColor(
				R.color.meibao_color_1));
		// 取消点击Tab时的背景色
		taskInfo_tabs.setTabBackground(0);
	}

	public class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);

		}

		private final String[] titles = { "日历", "明细" };

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
			switch (position) {
			case 0:
				if (gridViewFragment == null) {
					gridViewFragment = new TaskGridViewFragment(mContext,
							userID, byIdDetailBean.getcTime(), mTaskId);
				}
				return gridViewFragment;
			case 1:
				if (taskInfoFragment == null) {
					taskInfoFragment = new TaskInfoFragment(
							TaskInfoActivity.this, mTaskId, thisJobUserid,
							isOther, byIdDetailBean);// ,
														// parentScrollView,tabLayout
				}
				return taskInfoFragment;

			default:
				return null;
			}
		}

	}

}
