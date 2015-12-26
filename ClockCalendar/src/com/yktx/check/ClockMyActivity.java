package com.yktx.check;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.clock.service.AddShowPhotoService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.yktx.check.ClockDynamicActivity.MyPagerAdapter;
import com.yktx.check.adapter.MedalGridViewAdapter;
import com.yktx.check.adapter.PopWindowApapter;
import com.yktx.check.bean.CreateUserBean;
import com.yktx.check.bean.ImageListBean;
import com.yktx.check.bean.MedalBean;
import com.yktx.check.bean.MyTaskAllBean;
import com.yktx.check.bean.MyTaskBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.fragment.ClockFansFragment;
import com.yktx.check.fragment.ClockFollowFragment;
import com.yktx.check.fragment.ClockMyFragment;
import com.yktx.check.fragment.ClockMyFragment.PointExplainFragmentClickListener;
import com.yktx.check.fragment.ClockMyJobListFragment;
import com.yktx.check.gridview.NoScrollGridView;
import com.yktx.check.service.Service;
import com.yktx.check.square.fragment.AttentionFragment2;
import com.yktx.check.square.fragment.DynamicFragment;
import com.yktx.check.util.CommonUtils;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.FileURl;
import com.yktx.check.util.Tools;
import com.yktx.check.widget.OldPagerSlidingTabStrip;

@SuppressLint("NewApi")
public class ClockMyActivity extends FragmentActivity implements
		ServiceListener {
	private ImageView mTitleLeft, mTitleRight, clock_my_firstitem_headImage;
	// private ListView mNewListView
	// , mEverListView
	;
	private MyTaskAllBean mTaskAllBean;
	private List<MyTaskBean> mNewList = new ArrayList<MyTaskBean>(),
			mEverList = new ArrayList<MyTaskBean>();
	private CreateUserBean createUserBean;
	// ImageView leftList, rightList;
	private TextView clock_my_firstitem_userName, mTitleContent,
			clock_my_firstitem_point
			// ,clock_my_firstitem_fans,
			// clock_my_firstitem_guanzhu,
			// clock_my_firstitem_takeClockNum
			;
	private RelativeLayout mTitleLayout
	// , clock_my_nowClockLayout,
	// clock_my_everClockLayout
			,
			headImageLayout;
	private ImageView sizeNullImage, clock_my_firstitem_BRImage;// ,clock_my_jobLis

	private View title_item_layoutBackGround;
	boolean isLeft = true;
	private boolean isCamera;
	ScrollView parentScrollView;
	NoScrollGridView medalGridView;
	MedalGridViewAdapter gridViewAdapter;

	// private ListView mCurrentListView;

	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.zw_image)
			.showImageForEmptyUri(R.drawable.zw_image)
			.showImageOnFail(R.anim.loading_image_animation)
			.showImageOnLoading(null).showImageForEmptyUri(null)
			.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
			.cacheOnDisk(true).displayer(new RoundedBitmapDisplayer(200))
			.cacheInMemory(false)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			// .displayer(new RoundedBitmapDisplayer(4))
			.build();

	private boolean isReflush = true;
	private Context mContext;
	private String userID;
	private SharedPreferences settings;
	private Editor mEditor;
	// private LinearLayout clock_my_fragment;

	// ClockFollowFragment clockFollowFragment;
	// ClockMyJobListFragment clockMyJobListFragment;
	public LinearLayout tabLayout, medalLayout;

	ArrayList<MedalBean> list = new ArrayList<MedalBean>(10);
	private int type = 0; // 状态 0 正在 ， 1 暂停 ，2个人的job

	/**
	 * 滚动距离
	 */
	private static int USER_HEAD_HEIGHT;

	FeedbackAgent agent;

	private LinearLayout
	// clock_my_firstitem_attention
	// ,clock_my_firstitem_fansLayout
	// ,
	// clock_my_firstitem_takeClockLayout,
			clock_my_firstitem_userNameLayout,
			clock_my_firstitem_pointLayout;

	public DisplayMetrics mDisplayMetrics;
	public boolean isStopSound;

	/**
	 * 密度
	 */
	private float DENSITY;
	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;
	/**
	 * 我的卡
	 */
	ClockMyFragment clockMyFragment;
	/**
	 * 关注
	 */
	ClockFollowFragment clockFollowFragment;
	/**
	 * 粉丝
	 */
	ClockFansFragment clockFansFragment;

	/**
	 * 详情
	 */
	ClockMyJobListFragment clockMyJobListFragment;
	ViewPager ClockMy2_pager;
	OldPagerSlidingTabStrip ClockMy2_tabs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		DENSITY = mDisplayMetrics.density;
		dm = getResources().getDisplayMetrics();
		USER_HEAD_HEIGHT = (int) (149 * BaseActivity.DENSITY);
		findViews();
		init();
		setListeners();
		showFragment(1);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_clock_my2);
		agent = new FeedbackAgent(ClockMyActivity.this);
		ClockApplication.getInstance().addActivity(ClockMyActivity.this);
		tabLayout = (LinearLayout) findViewById(R.id.tabLayout);
		mTitleLeft = (ImageView) findViewById(R.id.title_item_leftImage);
		mTitleRight = (ImageView) findViewById(R.id.title_item_rightImage);
		mTitleContent = (TextView) findViewById(R.id.title_item_content);
		mTitleLayout = (RelativeLayout) findViewById(R.id.title_item_layout);
		parentScrollView = (ScrollView) findViewById(R.id.parentScrollView);
		clock_my_firstitem_headImage = (ImageView) findViewById(R.id.clock_my_firstitem_headImage);
		clock_my_firstitem_userName = (TextView) findViewById(R.id.clock_my_firstitem_userName);
		clock_my_firstitem_point = (TextView) findViewById(R.id.clock_my_firstitem_point);
		// clock_my_firstitem_fans = (TextView)
		// findViewById(R.id.clock_my_firstitem_fans);
		// clock_my_firstitem_guanzhu = (TextView)
		// findViewById(R.id.clock_my_firstitem_guanzhu);
		// clock_my_firstitem_takeClockNum = (TextView)
		// findViewById(R.id.clock_my_firstitem_takeClockNum);
		sizeNullImage = (ImageView) findViewById(R.id.sizeNullImage);
		// leftList = (ImageView) findViewById(R.id.leftList);
		// rightList = (ImageView) findViewById(R.id.rightList);
		headImageLayout = (RelativeLayout) findViewById(R.id.headImageLayout);
		clock_my_firstitem_BRImage = (ImageView) findViewById(R.id.clock_my_firstitem_BRImage);
		medalLayout = (LinearLayout) findViewById(R.id.medalLayout);
		// clock_my_fragment = (LinearLayout)
		// findViewById(R.id.clock_my_fragment);
		// clock_my_firstitem_fansLayout = (LinearLayout)
		// findViewById(R.id.clock_my_firstitem_fansLayout);
		// clock_my_firstitem_attention = (LinearLayout)
		// findViewById(R.id.clock_my_firstitem_attention);
		// clock_my_firstitem_takeClockLayout = (LinearLayout)
		// findViewById(R.id.clock_my_firstitem_takeClockLayout);
		clock_my_firstitem_userNameLayout = (LinearLayout) findViewById(R.id.clock_my_firstitem_userNameLayout);
		clock_my_firstitem_pointLayout = (LinearLayout) findViewById(R.id.clock_my_firstitem_pointLayout);

		// clock_my_jobList = (ImageView) findViewById(R.id.clock_my_jobList);
		medalGridView = (NoScrollGridView) findViewById(R.id.medalGridView);

		// title_item_feedback = (ImageView)
		// findViewById(R.id.title_item_feedback);
		// title_item_imprint = (ImageView)
		// findViewById(R.id.title_item_imprint);
		// title_item_takeClockSound = (ImageView)
		// findViewById(R.id.title_item_takeClockSound);
		title_item_layoutBackGround = findViewById(R.id.title_item_layoutBackGround);
		setOverflowShowingAlways();

		ClockMy2_pager = (ViewPager) findViewById(R.id.ClockMy2_pager);
		ClockMy2_tabs = (OldPagerSlidingTabStrip) findViewById(R.id.ClockMy2_tabs);

		ClockMy2_pager.setAdapter(new MyPagerAdapter(
				getSupportFragmentManager()));
		// pager.setOffscreenPageLimit(4);

		ClockMy2_tabs.setViewPager(ClockMy2_pager);
		setTabsValue();

	}

	//
	// @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	// @SuppressLint("NewApi")
	// private void setDefaultFragment() {
	// FragmentManager fm = getFragmentManager();
	// FragmentTransaction transaction = fm.beginTransaction();
	// clockMyFragment = new ClockMyFragment(userID, parentScrollView,
	// tabLayout);
	// clockMyFragment.setPointExplainFragmentClickListener(clickListener);
	// transaction.replace(R.id.clock_my_fragment, clockMyFragment);
	// transaction.commit();
	// }
	PointExplainFragmentClickListener clickListener = new PointExplainFragmentClickListener() {

		@Override
		public void setPointExplainFragment() {
			// TODO Auto-generated method stub
			Tools.getLog(Tools.d, "aaa", "listview 的接口回调");
			isReflush = false;
		}
	};

	public void init() {
		// TODO Auto-generated method stub
		settings = getSharedPreferences("clock", MODE_PRIVATE);
		mEditor = settings.edit();

		userID = settings.getString("userid", null);
		mTitleContent.setText("");
		mTitleContent.setTextColor(0x00444444);
		title_item_layoutBackGround.setBackgroundColor(0x00444444);
		clock_my_firstitem_BRImage.setVisibility(View.VISIBLE);

		int pagerHeight = (int) (settings.getInt("allHeight", 0) - 48 * BaseActivity.DENSITY);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, pagerHeight);
		Tools.getLog(Tools.i, "bbb", "pagerHeight =========== " + pagerHeight);
		// clock_my_fragment.setLayoutParams(params);

		gridViewAdapter = new MedalGridViewAdapter(this);
		medalGridView.setAdapter(gridViewAdapter);
		showPopupWindow();// 实例化popwindow
	}

	// private void setRightAdapter() {
	// AnimationAdapter animAdapter =
	// new SwingRightInAnimationAdapter(mNewAdapter);
	// animAdapter.setAbsListView(mCurrentListView);
	// mCurrentListView.setAdapter(animAdapter);
	// }

	@SuppressLint("NewApi")
	public void setListeners() {
		// TODO Auto-generated method stub
		// title_item_takeClockSound.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// if(isStopSound){
		// isStopSound = false;
		// title_item_takeClockSound.setImageResource(R.drawable.geren_sound_on);
		// Toast.makeText(mContext, "打卡声音：开", Toast.LENGTH_SHORT).show();
		// }else{
		// isStopSound = true;
		// title_item_takeClockSound.setImageResource(R.drawable.geren_sound_off);
		// Toast.makeText(mContext, "打卡声音：关", Toast.LENGTH_SHORT).show();
		// }
		// mEditor.putBoolean("sound", isStopSound);
		// mEditor.commit();
		// }
		// });
		mTitleRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// zhuxiao();
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				} else {
					popupWindow.showAsDropDown(mTitleRight, 16, 0);
				}
				// Intent intent = new
				// Intent(mContext,ClockSetHeadActivity.class);
				// startActivity(intent);
			}
		});

		mTitleLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		headImageLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showsetheaddialog();
				isReflush = false;
			}
		});

		// leftList.setOnClickListener(new OnClickListener() {
		//
		// @TargetApi(Build.VERSION_CODES.HONEYCOMB)
		// @SuppressLint("NewApi")
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// if (type == 0) {
		// return;
		// }
		// type = 0;
		// isLeft = true;
		// leftList.setImageResource(R.drawable.geren_kapian2);
		// rightList.setImageResource(R.drawable.geren_guanzhu1);
		// clock_my_jobList.setImageResource(R.drawable.geren_mingxi1);
		// // if(mNewList == null || mNewList.size() == 0){
		// // sizeNullImage.setVisibility(View.VISIBLE);
		// //
		// // } else {
		// // sizeNullImage.setVisibility(View.GONE);
		// //
		// // }
		// showFragment(1);
		//
		// // if (clockMyFragment == null) {
		// // clockMyFragment = new ClockMyFragment(userID,
		// // parentScrollView, tabLayout);
		// // Tools.getLog(Tools.d, "aaa", "刷新ClockMyFragment");
		// // }
		// // FragmentManager fm = getFragmentManager();
		// // FragmentTransaction transaction = fm.beginTransaction();
		// // transaction.replace(R.id.clock_my_fragment, clockMyFragment);
		// // transaction.commit();
		// }
		// });

		// rightList.setOnClickListener(new OnClickListener() {
		//
		// @SuppressLint("NewApi")
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// if (type == 2) {
		// return;
		// }
		// type = 2;
		// isLeft = false;
		// leftList.setImageResource(R.drawable.geren_kapian1);
		// rightList.setImageResource(R.drawable.geren_guanzhu2);
		// clock_my_jobList.setImageResource(R.drawable.geren_mingxi1);
		//
		// // if(mEverList == null || mEverList.size() == 0){
		// // sizeNullImage.setVisibility(View.VISIBLE);
		// // } else {
		// // sizeNullImage.setVisibility(View.GONE);
		// // }
		// // clockMyFragment = new ClockMyFragment(
		// // false,longItem,userID);
		// showFragment(3);
		// // if (clockFollowFragment == null) {
		// // clockFollowFragment = new ClockFollowFragment(mContext,
		// // userID, false, parentScrollView, tabLayout);
		// // }
		// // FragmentManager fm = getFragmentManager();
		// // FragmentTransaction transaction = fm.beginTransaction();
		// // transaction
		// // .replace(R.id.clock_my_fragment, clockFollowFragment);
		// // transaction.commit();
		// }
		// });
		// clock_my_jobList.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// if (type == 1) {
		// return;
		// }
		// type = 1;
		// leftList.setImageResource(R.drawable.geren_kapian1);
		// rightList.setImageResource(R.drawable.geren_guanzhu1);
		// clock_my_jobList.setImageResource(R.drawable.geren_mingxi2);
		// sizeNullImage.setVisibility(View.GONE);
		// showFragment(2);
		// // if (clockMyJobListFragment == null) {
		// // clockMyJobListFragment = new ClockMyJobListFragment(
		// // mContext, userID, parentScrollView, tabLayout);
		// // }
		// // // Fragment = new ClockMyFragment(mEverList, isLeft);
		// // FragmentManager fm = getFragmentManager();
		// // FragmentTransaction transaction = fm.beginTransaction();
		// // transaction.replace(R.id.clock_my_fragment,
		// // clockMyJobListFragment);
		// // transaction.commit();
		// }
		// });
		clock_my_firstitem_userNameLayout
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent in = new Intent(mContext,
								UpdateUserNameActivity.class);
						startActivity(in);

					}
				});
		// clock_my_firstitem_fansLayout.setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// Intent in = new Intent(mContext, ClockFansActivity.class);
		// in.putExtra("thisjobuserid", userID);
		// in.putExtra("isother", false);
		// startActivity(in);
		// }
		// });
		// clock_my_firstitem_attention.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// Intent in = new Intent(mContext, ClockAttentionActivity.class);
		// in.putExtra("userid", userID);
		// in.putExtra("isother", false);
		// startActivity(in);
		// }
		// });
		// clock_my_firstitem_takeClockLayout.setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// isReflush = false;
		// Intent in = new Intent(mContext, ClockDetailActivity.class);
		// in.putExtra("userid", userID);
		// in.putExtra("name", createUserBean.getName());
		// in.putExtra("isother", false);
		// startActivity(in);
		// }
		// });

		parentScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:

					int alf = parentScrollView.getScrollY() * 255
							/ USER_HEAD_HEIGHT > 255 ? 255 : parentScrollView
							.getScrollY() * 255 / USER_HEAD_HEIGHT;
					mTitleContent.setTextColor(Color
							.argb(alf, 0x44, 0x44, 0x44)); // #44 44 44

					title_item_layoutBackGround.setBackgroundColor(Color.argb(
							alf, 0xff, 0xff, 0xff)); // #ff ff ff
					// if(tabY <= 48*BaseActivity.DENSITY){
					// RelativeLayout.LayoutParams btitlelayout = new
					// RelativeLayout.LayoutParams(
					// ViewGroup.LayoutParams.MATCH_PARENT, (int)
					// (48*BaseActivity.DENSITY));
					// clock_my_firstitem_userName.setLayoutParams(btitlelayout);
					// }
					break;
				default:

					break;
				}
				return false;
			}
		});
		// title_item_feedback.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// agent.sync();
		// agent.startFeedbackActivity();
		// isReflush = false;
		// }
		// });
		// title_item_imprint.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// Intent in = new Intent(mContext, ImprintActivity.class);
		// startActivity(in);
		// isReflush = false;
		// }
		// });
		clock_my_firstitem_pointLayout
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent in = new Intent(mContext,
								PointExplainActivity.class);
						startActivity(in);
						isReflush = false;
					}
				});
	}

	public void showFragment(int index) {
		// FragmentTransaction ft = fm.beginTransaction();
		//
		// // 想要显示一个fragment,先隐藏所有fragment，防止重叠
		// hideFragments(ft);
		// switch (index) {
		// case 1:
		// // 如果fragment1已经存在则将其显示出来
		// if (clockMyFragment != null)
		// ft.show(clockMyFragment);
		// // 否则是第一次切换则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
		// else {
		// clockMyFragment = new ClockMyFragment();
		// ft.add(R.id.clock_my_fragment, clockMyFragment);
		// }
		// break;
		// case 2:
		// if (clockMyJobListFragment != null)
		// ft.show(clockMyJobListFragment);
		// else {
		// clockMyJobListFragment = new ClockMyJobListFragment(mContext,
		// userID, parentScrollView, tabLayout);
		// ft.add(R.id.clock_my_fragment, clockMyJobListFragment);
		// }
		// break;
		// case 3:
		// if (clockFollowFragment != null)
		// ft.show(clockFollowFragment);
		// else {
		// clockFollowFragment = new ClockFollowFragment(mContext, userID,
		// false, parentScrollView, tabLayout);
		// ft.add(R.id.clock_my_fragment, clockFollowFragment);
		// }
		// break;
		// }
		// ft.commit();
	}

	// // 当fragment已被实例化，就隐藏起来
	// public void hideFragments(FragmentTransaction ft) {
	// if (clockMyFragment != null)
	// ft.hide(clockMyFragment);
	// if (clockFollowFragment != null)
	// ft.hide(clockFollowFragment);
	// if (clockMyJobListFragment != null)
	// ft.hide(clockMyJobListFragment);
	// }

	int tabY;

	public void initHeadView() {
		ImageLoader
				.getInstance()
				.displayImage(
						Tools.getImagePath(createUserBean.getImageSource())
								+ createUserBean.getAvartarPath()
								+ (createUserBean.getImageSource() == 2 ? "?imageMogr2/thumbnail/132x132"
										: ""), clock_my_firstitem_headImage,
						headOptions, new ImageLoadingListener() {

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
									View view, Bitmap loadedImage) {
								// TODO Auto-generated method stub
								// loadedImage =
								// Bitmap.createBitmap(ImageUtils.fastblur(
								// loadedImage, 50));
								// clock_my_firstitem_imageBackground
								// .setImageBitmap(loadedImage);
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
								// TODO Auto-generated method stub

							}
						});
		// clock_my_firstitem_guanzhu.setText(createUserBean.getFollowingCount());//关注数
		// clock_my_firstitem_fans.setText(createUserBean.getFansCount());//粉丝数
		// clock_my_firstitem_takeClockNum.setText(createUserBean.getTotalJobCount()+"");//共打卡数
		clock_my_firstitem_userName.setText(createUserBean.getName());
		mTitleContent.setText(createUserBean.getName());
		clock_my_firstitem_point.setText(createUserBean.getPoint());

	}

	// public void Conn() {
	// // Hashtable<String, String> param = new Hashtable<String, String>();
	// StringBuffer sb = new StringBuffer();
	// sb.append("?userId=");
	// sb.append(userID);
	// sb.append("&curUserId=");
	// sb.append(userID);
	// Service.getService(Contanst.HTTP_GETALL, null, sb.toString(),
	// ClockMyActivity.this).addList(null).request(UrlParams.GET);
	// }

	public void getUserConn() {
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(userID);
		sb.append("&curUserId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_GETBYIDUSER, null, sb.toString(),
				ClockMyActivity.this).addList(null).request(UrlParams.GET);

	}

	/**
	 * 获取勋章
	 */
	public void getBadeConn() {
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_USER_GETBADGE, null, sb.toString(),
				ClockMyActivity.this).addList(null).request(UrlParams.GET);

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

	private Handler mHandler = new Handler() {
		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.GETBYIDUSER:
					createUserBean = (CreateUserBean) msg.obj;
					initHeadView();
					// Conn();//刷新的东西
					break;
				case Contanst.IMAGEUPLOAD:
					Tools.getLog(Tools.d, "aaa", "图片===上传成功===完成");
					isReflush = true;
					String imageUrl = (String) msg.obj;
					mEditor.putString("userheadimage", imageUrl);
					// mEditor.putInt("imageSource", "2");
					mEditor.commit();
					break;
				case Contanst.TASK_DELETE:
					Toast.makeText(mContext, "删除成功！", Toast.LENGTH_SHORT)
							.show();
					getUserConn();
					break;

				case Contanst.USER_GETBADGE:
					list = (ArrayList<MedalBean>) msg.obj;
					if (list.size() == 0) {
						medalLayout.setVisibility(View.GONE);
					} else {
						if (medalLayout.getVisibility() != View.VISIBLE) {
							medalLayout.setVisibility(View.VISIBLE);
						}
						gridViewAdapter.setList(list);
						gridViewAdapter.notifyDataSetChanged();
					}
					break;
				}

				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				switch (msg.arg1) {
				case Contanst.GETALL:

					break;

				case Contanst.GETBYIDUSER:

					break;

				case Contanst.USER_GETBADGE:

					break;

				}
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 19) {
				if (data != null) {
					startPicCut2(data.getData());
					// Uri selectedImage = data.getData();
					// if (selectedImage != null) {
					// // sendPicByUri(selectedImage);
					// }
				}
			} else if (requestCode == 18) {
				startPicCut1();
				Tools.getLog(Tools.d, "aaa", "拍照的回调？");

			} else if (requestCode == 26) {
				if (data != null) {
					isReflush = false;
					Uri selectedImage = data.getData();
					ContentResolver resolver = getContentResolver();
					Bitmap photo = null;
					Tools.getLog(Tools.d, "aaa", "裁剪图片的返回！");
					try {
						// if (selectedImage != null) {
						// // // sendPicByUri(selectedImage);
						// photo = MediaStore.Images.Media.getBitmap(resolver,
						// selectedImage);
						// clock_my_firstitem_headImage.setImageBitmap(photo);
						// } else {
						// Bundle extras = data.getExtras();
						// if (extras != null) {
						// // 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
						// photo = extras.getParcelable("data");
						// if (photo != null) {
						// clock_my_firstitem_headImage
						// .setImageBitmap(photo);
						// }
						// }
						// }

						// int degree = ImageTool.getBitmapDegree(path);
						// Tools.getLog(Tools.i, "aaa", "degree ============ "
						// + degree);
						//
						// FileInputStream in = new FileInputStream(path);
						//
						// BitmapFactory.Options options = new
						// BitmapFactory.Options();
						// options.inSampleSize = 10;
						//
						// photo = ImageTool.rotateBitMap(
						// BitmapFactory.decodeStream(in, null, options),
						// degree);
						Bundle extras = data.getExtras();
						if (extras != null) {
							// 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
							photo = extras.getParcelable("data");
						}

						String filepath = FileURl.ImageFilePath + "/"
								+ System.currentTimeMillis() + ".jpg";
						File file = new File(filepath);
						if (file.exists()) {
							file.mkdirs();
						}
						FileOutputStream outStreamz = new FileOutputStream(
								filepath);

						photo.compress(Bitmap.CompressFormat.JPEG, 50,
								outStreamz);

						outStreamz.close();

						String path = FileURl.LOAD_FILE + filepath;
						Tools.getLog(Tools.d, "aaa", "path====" + path);
						ImageLoader.getInstance().clearMemoryCache();
						ImageLoader.getInstance().displayImage(path,
								clock_my_firstitem_headImage, headOptions);

						ArrayList<ImageListBean> filenames = new ArrayList<ImageListBean>(
								1);// 相册选取的集合的名字
						ImageListBean bean = new ImageListBean();
						bean.setCheck(true);
						bean.setImageUrl(filepath);
						filenames.add(bean);
						Intent intent_baonew = new Intent(ClockMyActivity.this,
								AddShowPhotoService.class);
						intent_baonew.putExtra("state",
								AddShowPhotoService.AddShowPhotoRun);
						intent_baonew.putExtra("productid", userID);
						intent_baonew.putExtra("list", filenames);
						intent_baonew.putExtra("uuid", userID);
						intent_baonew.putExtra("type", 2);
						startService(intent_baonew);
						//

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this);
		if (isReflush) {
			getUserConn();
			// setDefaultFragment();
			getBadeConn();
		}
		/** 返回刷新 */
		// if(type == 0){
		// clockMyFragment = new ClockMyFragment( true,longItem,userID);
		// FragmentManager fm = getFragmentManager();
		// FragmentTransaction transaction = fm.beginTransaction();
		// transaction.replace(R.id.clock_my_fragment, clockMyFragment);
		// transaction.commit();
		// }else if(type == 1){
		// clockMyJobListFragment = new ClockMyJobListFragment(mContext,userID);
		// FragmentManager fm = getFragmentManager();
		// FragmentTransaction transaction = fm.beginTransaction();
		// transaction.replace(R.id.clock_my_fragment, clockMyJobListFragment);
		// transaction.commit();
		// }else if(type == 2){
		// clockFollowFragment = new ClockFollowFragment(mContext,userID,true);
		// FragmentManager fm = getFragmentManager();
		// FragmentTransaction transaction = fm.beginTransaction();
		// transaction.replace(R.id.clock_my_fragment, clockFollowFragment);
		// transaction.commit();
		// }
		isReflush = true;
	}

	// *显示dialog*/
	private void showsetheaddialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(ClockMyActivity.this,
						R.style.CustomDiaLog_by_SongHang));
		builder.setItems(new String[] { "拍照", "从相册选择", "取消" },
				new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							// opencamera();
							isCamera = true;
							selectPicFromCamera();
							break;
						case 1:
							isCamera = false;
							selectPicFromLocal();
							// Intent intent_gralley = new Intent(
							// UserCenterActivity.this,
							// PhotoActivity.class);
							// intent_gralley.putExtra(CameraActivity.IsRegister,
							// "1");
							// startActivityForResult(intent_gralley,
							// CameraActivity.GRALLEY);

							// Intent intent = new Intent();
							// intent.addCategory(Intent.CATEGORY_OPENABLE);
							// intent.setType("image/*");
							// // 根据版本号不同使用不同的Action
							// if (Build.VERSION.SDK_INT < 19) {
							// intent.setAction(Intent.ACTION_GET_CONTENT);
							// } else {
							// intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
							// }
							//
							// File out = new File(FileURl.ImageFilePath,
							// FileURl.IMAGE_NAME);
							// // out.mkdirs();
							// Uri uri = Uri.fromFile(out);
							// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
							// startActivityForResult(intent,
							// GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);
							break;
						default:
							break;
						}
					}
				});
		builder.show();
	}

	/**
	 * 照相获取图片
	 */
	public String cameraPhotoName;
	public File cameraFile;

	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			String st = getResources().getString(
					R.string.sd_card_does_not_exist);
			Toast.makeText(getApplicationContext(), st, 0).show();
			return;
		}
		cameraPhotoName = System.currentTimeMillis() + ".jpg";
		Tools.getLog(Tools.d, "aaa", 123456789 + "");
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraFile = new File(FileURl.ImageFilePath, cameraPhotoName);
		cameraFile.getParentFile().mkdirs();
		Tools.getLog(Tools.d, "aaa",
				"cameraFile.getParentFile():" + cameraFile.getPath());
		Uri uri = Uri.fromFile(cameraFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, 18);
		if (!Tools.isExitsSdcard()) {
			String st = "SD卡不存在，不能拍照";
			Toast.makeText(mContext, st, 0).show();
			return;
		}

		// Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// startActivityForResult(camera, 18);
	}

	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, 19);
	}

	/**
	 * 裁剪图片的方法1 拍照
	 * 
	 * @param uri
	 */
	public void startPicCut1() {
		Uri uri = Uri.fromFile(cameraFile);
		Intent intentCarema = new Intent("com.android.camera.action.CROP");
		intentCarema.setDataAndType(uri, "image/*");
		// intentCarema.putExtra("crop", true);
		// intentCarema.putExtra("scale", false);//小米点不了保存就是因为这里。。。还有下面、、、
		// intentCarema.putExtra("noFaceDetection", true);// 不需要人脸识别功能
		// intentCarema.putExtra("circleCrop", "");//设定此方法选定区域会是圆形区域
		// aspectX aspectY是宽高比例
		intentCarema.putExtra("aspectX", 1);
		intentCarema.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片的宽高
		intentCarema.putExtra("outputX", 150);
		intentCarema.putExtra("outputY", 150);
		intentCarema.putExtra("return-data", true);
		startActivityForResult(intentCarema, 26);
	}

	/**
	 * 裁剪图片的方法2
	 * 
	 * 从图库中选择
	 * 
	 * @param uri
	 */
	public void startPicCut2(Uri uri) {
		Intent intentCarema = new Intent("com.android.camera.action.CROP");
		intentCarema.setDataAndType(uri, "image/*");
		// intentCarema.putExtra("crop", true);
		// intentCarema.putExtra("scale", false);
		// intentCarema.putExtra("noFaceDetection", true);// 不需要人脸识别功能
		// intentCarema.putExtra("circleCrop", "");//设定此方法选定区域会是圆形区域
		// aspectX aspectY是宽高比例
		intentCarema.putExtra("aspectX", 1);
		intentCarema.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片的宽高
		intentCarema.putExtra("outputX", 150);
		intentCarema.putExtra("outputY", 150);
		intentCarema.putExtra("return-data", true);
		startActivityForResult(intentCarema, 26);
	}

	// private void upLoadImage(Bitmap bitmap) {
	// String photoStr = PictureUtil.save(bitmap);
	// ArrayList<ImageListBean> filenames = new ArrayList<ImageListBean>(3);//
	// 相册选取的集合的名字
	// ImageListBean imageListBean = new ImageListBean();
	// imageListBean.setCheck(true);
	// imageListBean.setImageUrl(photoStr);
	// filenames.add(imageListBean);
	// Intent intent_baonew = new Intent(
	// ClockMyActivity.this,
	// AddShowPhotoService.class);
	// intent_baonew.putExtra("state",
	// AddShowPhotoService.AddShowPhotoRun);
	// intent_baonew.putExtra("type", 1);
	// intent_baonew.putExtra("productid", userID);
	// intent_baonew.putExtra("list", filenames);
	// intent_baonew.putExtra("uuid", userID);
	// startService(intent_baonew);
	// }

	public void zhuxiao() {
		new AlertDialog.Builder(mContext)
				.setTitle("提示")
				.setMessage("是否注销")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						// deleteTaskConn(position);
						JPushInterface.setAlias(mContext, "",
								new TagAliasCallback() {

									@Override
									public void gotResult(int arg0,
											String arg1, Set<String> arg2) {
										// TODO Auto-generated method stub

									}
								});
						mEditor.putBoolean("islogin", false);
						mEditor.putString("userid", null);
						mEditor.commit();
						ClockMainActivity.isLogin = false;
						Intent intent = new Intent(mContext,
								LoginActivity.class);
						startActivity(intent);
						finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub

					}
				}).show();
	}

	PopupWindow popupWindow;
	int xoff;

	public void showPopupWindow() {
		// 加载布局
		LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.popwindow_linearlayout, null);
		TextView popwindow_layout_out = (TextView) layout
				.findViewById(R.id.popwindow_layout_out);
		final TextView popwindow_layout_sound = (TextView) layout
				.findViewById(R.id.popwindow_layout_sound);
		TextView popwindow_layout_Copyright = (TextView) layout
				.findViewById(R.id.popwindow_layout_Copyright);
		TextView popwindow_layout_fk = (TextView) layout
				.findViewById(R.id.popwindow_layout_fk);

		LinearLayout popWindow = (LinearLayout) layout
				.findViewById(R.id.popWinLayout);
		isStopSound = settings.getBoolean("sound", false);
		if (isStopSound) {
			popwindow_layout_sound.setText("打卡声音：关");
		} else {
			popwindow_layout_sound.setText("打卡声音：开");
		}

		// 实例化popupWindow
		popupWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// 控制键盘是否可以获得焦点
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		// 设置popupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(
				R.drawable.home_fk_background));
		WindowManager manager = (WindowManager) getSystemService(mContext.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		// 获取xoff
		int xpos = manager.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;
		xoff = xpos;
		// xoff,yoff基于anchor的左下角进行偏移。
		popWindow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
			}
		});
		popwindow_layout_out.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				zhuxiao();
				popupWindow.dismiss();
			}
		});
		popwindow_layout_sound.setOnClickListener(new OnClickListener() {// 打卡声音的开关

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (isStopSound) {
							isStopSound = false;
							popwindow_layout_sound.setText("打卡声音：开");
							Toast.makeText(mContext, "打卡声音：开",
									Toast.LENGTH_SHORT).show();
						} else {
							isStopSound = true;
							popwindow_layout_sound.setText("打卡声音：关");
							Toast.makeText(mContext, "打卡声音：关",
									Toast.LENGTH_SHORT).show();
						}
						mEditor.putBoolean("sound", isStopSound);
						mEditor.commit();
						popupWindow.dismiss();
					}
				});
		popwindow_layout_Copyright.setOnClickListener(new OnClickListener() {// 版权声明

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						isReflush = false;
						Intent in = new Intent(mContext, ImprintActivity.class);
						startActivity(in);
						popupWindow.dismiss();
					}
				});
		popwindow_layout_fk.setOnClickListener(new OnClickListener() {// 反馈
					// 反馈
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						isReflush = false;
						agent.sync();
						agent.startFeedbackActivity();
						popupWindow.dismiss();
					}
				});

	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		ClockMy2_tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		ClockMy2_tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		ClockMy2_tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 0, dm));
		// 设置Tab Indicator的高度
		ClockMy2_tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		ClockMy2_tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		ClockMy2_tabs.setSelectTextSice((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		ClockMy2_tabs.setIndicatorColor(getResources().getColor(
				R.color.meibao_color_1));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		ClockMy2_tabs.setSelectedTextColor(getResources().getColor(
				R.color.meibao_color_1));
		// 取消点击Tab时的背景色
		ClockMy2_tabs.setTabBackground(0);
		// if(setting.getBoolean("isred", false)){
		// Tools.getLog(Tools.d,"aaa", "开启小红点");
		// }else{
		// Tools.getLog(Tools.d,"aaa", "guanbi小红点");
		// }
		//
		// if(!tabs.RedVisity()){
		// tabs.setRedVisity(setting.getBoolean("isred", false));
		// }

		// tabs.addBadgeView(1, true);
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

	//

	public class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);

		}

		private final String[] titles = { "卡片", "关注", "粉丝", "详情" };

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
				if (clockMyFragment == null) {
					clockMyFragment = new ClockMyFragment(userID);// ,
																	// parentScrollView,tabLayout
				}
				return clockMyFragment;

			case 1:

				if (clockFollowFragment == null) {
					clockFollowFragment = new ClockFollowFragment(mContext,
							userID);
				}
				return clockFollowFragment;
			case 2:

				if (clockFansFragment == null) {
					clockFansFragment = new ClockFansFragment(mContext, userID);
				}
				return clockFansFragment;
			case 3:

				if (clockMyJobListFragment == null) {
					clockMyJobListFragment = new ClockMyJobListFragment(
							mContext, userID);
				}
				return clockMyJobListFragment;

				// case 2:
				// if (attentionFragment == null) {
				// attentionFragment = new AttentionFragment2();
				// }
				// return attentionFragment;
				// case 3:
				// if (newFragment == null) {
				// newFragment = new NewFragment();
				// }
				// return newFragment;
				//
			default:
				return null;
			}
		}

	}

}
