package com.yktx.check;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.yktx.check.ClockMyActivity.MyPagerAdapter;
import com.yktx.check.adapter.ClockMyAdapter;
import com.yktx.check.adapter.MedalGridViewAdapter;
import com.yktx.check.bean.CreateUserBean;
import com.yktx.check.bean.MedalBean;
import com.yktx.check.bean.MyTaskAllBean;
import com.yktx.check.bean.MyTaskBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.fragment.ClockFansFragment;
import com.yktx.check.fragment.ClockFollowFragment;
import com.yktx.check.fragment.ClockMyFragment;
import com.yktx.check.fragment.ClockMyFragment.clickLongItem;
import com.yktx.check.fragment.ClockMyJobListFragment;
import com.yktx.check.gridview.NoScrollGridView;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.LvHeightUtil;
import com.yktx.check.util.Tools;
import com.yktx.check.widget.OldPagerSlidingTabStrip;

@SuppressLint("NewApi")
public class ClockOtherUserActivity extends FragmentActivity implements
		ServiceListener {
	// private ListView mListView;
	private ImageView mBack, title_item_rightImage, mHeadImage,
			clock_my_firstitem_headImage;// , clock_my_firstitem_imageBackground
	private TextView mTitleContent, clock_my_firstitem_userName,
			title_item_rightText, clock_my_firstitem_point
			// ,clock_my_firstitem_guanzhu,
			// clock_my_firstitem_takeClockNum
			// ,clock_my_firstitem_fans
			;
	private RelativeLayout mTitleLayout;
	private String OtherUserId, userID;
	private CreateUserBean createUserBean;
	private List<MyTaskBean> mNewList;
	private MyTaskAllBean mTaskAllBean;
	private ClockMyAdapter mNewAdapter;
	private ImageView sizeNullImage;
	private LinearLayout title_item_Fanslayout;// clock_other_user_fragment,
	// private ImageView clock_other_user_belowHeadImage1,
	// clock_other_user_belowHeadImage2, clock_other_user_belowHeadImage3;
	private int type;
	private Context mContext;
	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.shezhi_bg)
			.showImageForEmptyUri(null).showImageOnFail(null)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
			.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(400))
			// 启用内存缓存
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	SharedPreferences settings;
	private boolean isFans;

	ScrollView parentScrollView;
	public LinearLayout clock_other_user_belowHeadlayout;

	NoScrollGridView medalGridView;
	MedalGridViewAdapter gridViewAdapter;
	LinearLayout medalLayout;
	ArrayList<MedalBean> list = new ArrayList<MedalBean>(10);

	private boolean isConn;
	private ImageView clock_my_firstitem_userNameImage,
			clock_my_firstitem_BRImage;
	private LinearLayout
	// clock_my_firstitem_attention,
	// clock_my_firstitem_fansLayout,
	// clock_my_firstitem_takeClockLayout,
	clock_my_firstitem_pointLayout;

	/**
	 * 滚动距离
	 */
	private static int USER_HEAD_HEIGHT;

	// private FragmentManager fm;

	public DisplayMetrics mDisplayMetrics;
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

	ViewPager ClockOther2_pager;
	OldPagerSlidingTabStrip ClockOther2_tabs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		// fm = getFragmentManager();
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

	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_clock_other_user2);
		mBack = (ImageView) findViewById(R.id.title_item_leftImage);
		settings = getSharedPreferences("clock", MODE_PRIVATE);
		userID = settings.getString("userid", null);
		parentScrollView = (ScrollView) findViewById(R.id.parentScrollView);
		title_item_rightImage = (ImageView) findViewById(R.id.title_item_rightImage);
		title_item_rightText = (TextView) findViewById(R.id.title_item_rightText);
		mTitleContent = (TextView) findViewById(R.id.title_item_content);
		// mListView = (ListView) findViewById(R.id.clock_other_user_ListView);
		mTitleLayout = (RelativeLayout) findViewById(R.id.title_item_layout);
		clock_other_user_belowHeadlayout = (LinearLayout) findViewById(R.id.clock_other_user_belowHeadlayout);
		clock_my_firstitem_headImage = (ImageView) findViewById(R.id.clock_my_firstitem_headImage);
		clock_my_firstitem_userName = (TextView) findViewById(R.id.clock_my_firstitem_userName);
		// clock_my_firstitem_imageBackground = (ImageView)
		// findViewById(R.id.clock_my_firstitem_imageBackground);
		clock_my_firstitem_point = (TextView) findViewById(R.id.clock_my_firstitem_point);
		// clock_my_firstitem_fans = (TextView)
		// findViewById(R.id.clock_my_firstitem_fans);
		// clock_my_firstitem_guanzhu = (TextView)
		// findViewById(R.id.clock_my_firstitem_guanzhu);
		// clock_my_firstitem_takeClockNum = (TextView)
		// findViewById(R.id.clock_my_firstitem_takeClockNum);

		sizeNullImage = (ImageView) findViewById(R.id.sizeNullImage);
		clock_my_firstitem_userNameImage = (ImageView) findViewById(R.id.clock_my_firstitem_userNameImage);
		clock_my_firstitem_BRImage = (ImageView) findViewById(R.id.clock_my_firstitem_BRImage);

		// clock_other_user_fragment = (LinearLayout)
		// findViewById(R.id.clock_other_user_fragment);
		title_item_Fanslayout = (LinearLayout) findViewById(R.id.title_item_FansLayout);
		// clock_my_firstitem_attention = (LinearLayout)
		// findViewById(R.id.clock_my_firstitem_attention);
		// clock_my_firstitem_takeClockLayout = (LinearLayout)
		// findViewById(R.id.clock_my_firstitem_takeClockLayout);
		clock_my_firstitem_pointLayout = (LinearLayout) findViewById(R.id.clock_my_firstitem_pointLayout);

		// clock_my_firstitem_fansLayout = (LinearLayout)
		// findViewById(R.id.clock_my_firstitem_fansLayout);
		// clock_other_user_belowHeadImage1 = (ImageView)
		// findViewById(R.id.clock_other_user_belowHeadImage1);
		// clock_other_user_belowHeadImage2 = (ImageView)
		// findViewById(R.id.clock_other_user_belowHeadImage2);
		// clock_other_user_belowHeadImage3 = (ImageView)
		// findViewById(R.id.clock_other_user_belowHeadImage3);
		OtherUserId = getIntent().getStringExtra("userid");
		medalGridView = (NoScrollGridView) findViewById(R.id.medalGridView);
		medalLayout = (LinearLayout) findViewById(R.id.medalLayout);
		medalLayout.setVisibility(View.GONE);
		clock_my_firstitem_userNameImage.setVisibility(View.GONE);// 隐藏名字后面的钢笔

		setOverflowShowingAlways();

		ClockOther2_pager = (ViewPager) findViewById(R.id.ClockOther2_pager);
		ClockOther2_tabs = (OldPagerSlidingTabStrip) findViewById(R.id.ClockOther2_tabs);

		ClockOther2_pager.setAdapter(new MyPagerAdapter(
				getSupportFragmentManager()));
		// pager.setOffscreenPageLimit(4);

		ClockOther2_tabs.setViewPager(ClockOther2_pager);
		setTabsValue();

	}

	//
	// @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	// @SuppressLint("NewApi")
	// private void setDefaultFragment() {
	// FragmentManager fm = getFragmentManager();
	// FragmentTransaction transaction = fm.beginTransaction();
	// clockMyFragment = new ClockMyFragment(OtherUserId, parentScrollView,
	// clock_other_user_belowHeadlayout);
	// transaction.replace(R.id.clock_other_user_fragment, clockMyFragment);
	// transaction.commit();
	// }

	clickLongItem longItem = new clickLongItem() {

		@Override
		public void longItem(int position) {
			// TODO Auto-generated method stub

		}
	};

	protected void init() {
		// TODO Auto-generated method stub
		mTitleContent.setText("");
		mTitleContent.setTextColor(0x00444444);
		int pagerHeight = (int) (settings.getInt("allHeight", 0) - 48 * BaseActivity.DENSITY);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, pagerHeight);
		Tools.getLog(Tools.i, "bbb", "pagerHeight =========== " + pagerHeight);
		// clock_other_user_fragment.setLayoutParams(params);
		// setDefaultFragment();
		getUserInfoConn();

		gridViewAdapter = new MedalGridViewAdapter(this);
		medalGridView.setAdapter(gridViewAdapter);
	}

	protected void setListeners() {
		// TODO Auto-generated method stub

		clock_my_firstitem_headImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				String[] urls = { createUserBean.getAvartarPath() };
				String[] imageSource = { createUserBean.getImageSource() + "" };

				Intent intent = new Intent(mContext, ImagePagerActivity.class);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 1);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_SOURCE,
						imageSource);
				mContext.startActivity(intent);
			}
		});

		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// mListView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1,
		// int position, long arg3) {
		// // TODO Auto-generated method stub
		// MyTaskBean myTaskBean = mNewList.get(position);
		// Intent in = new Intent(mContext, TaskInfoActivity.class);
		// in.putExtra("taskid", myTaskBean.getTaskId());
		// in.putExtra("totalCheckCount", myTaskBean.getTotalCheckCount());//
		// 打卡次数
		// in.putExtra("totalDateCount", myTaskBean.getTotalDateCount());
		// in.putExtra("title", myTaskBean.getTitle());
		// in.putExtra("description", mNewList.get(position)
		// .getDescription());
		// in.putExtra("isother", true);
		// startActivity(in);
		// }
		// });
		// clock_other_user_belowHeadImage1
		// .setOnClickListener(new OnClickListener() {
		//
		// @SuppressLint("NewApi")
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// if (type == 0) {
		// return;
		// }
		// type = 0;
		// clock_other_user_belowHeadImage1
		// .setImageResource(R.drawable.geren_kapian2);
		// clock_other_user_belowHeadImage2
		// .setImageResource(R.drawable.geren_mingxi1);
		// clock_other_user_belowHeadImage3
		// .setImageResource(R.drawable.geren_guanzhu1);
		// showFragment(1);
		// // if (clockMyFragment == null) {
		// // clockMyFragment = new ClockMyFragment(OtherUserId,
		// // parentScrollView,
		// // clock_other_user_belowHeadlayout);
		// // }
		// // FragmentManager fm = getFragmentManager();
		// // FragmentTransaction transaction = fm.beginTransaction();
		// // transaction.replace(R.id.clock_other_user_fragment,
		// // clockMyFragment);
		// // transaction.commit();
		// }
		// });
		// clock_other_user_belowHeadImage2
		// .setOnClickListener(new OnClickListener() {
		//
		// @TargetApi(Build.VERSION_CODES.HONEYCOMB)
		// @SuppressLint("NewApi")
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// if (type == 1) {
		// return;
		// }
		// type = 1;
		// clock_other_user_belowHeadImage1
		// .setImageResource(R.drawable.geren_kapian1);
		// clock_other_user_belowHeadImage2
		// .setImageResource(R.drawable.geren_mingxi2);
		// clock_other_user_belowHeadImage3
		// .setImageResource(R.drawable.geren_guanzhu1);
		// sizeNullImage.setVisibility(View.GONE);
		//
		// showFragment(2);
		// // if (clockMyJobListFragment == null) {
		// // clockMyJobListFragment = new ClockMyJobListFragment(
		// // mContext, OtherUserId, parentScrollView,
		// // clock_other_user_belowHeadlayout);
		// // }
		// //
		// // // Fragment = new ClockMyFragment(mEverList, isLeft);
		// // FragmentManager fm = getFragmentManager();
		// // FragmentTransaction transaction = fm.beginTransaction();
		// // transaction.replace(R.id.clock_other_user_fragment,
		// // clockMyJobListFragment);
		// // transaction.commit();
		// }
		// });
		// clock_other_user_belowHeadImage3
		// .setOnClickListener(new OnClickListener() {
		//
		// @SuppressLint("NewApi")
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// if (type == 2) {
		// return;
		// }
		// type = 2;
		// clock_other_user_belowHeadImage1
		// .setImageResource(R.drawable.geren_kapian1);
		// clock_other_user_belowHeadImage2
		// .setImageResource(R.drawable.geren_mingxi1);
		// clock_other_user_belowHeadImage3
		// .setImageResource(R.drawable.geren_guanzhu2);
		//
		// showFragment(3);
		// // if (clockFollowFragment == null) {
		// // clockFollowFragment = new ClockFollowFragment(
		// // mContext, OtherUserId, true,
		// // parentScrollView,
		// // clock_other_user_belowHeadlayout);
		// // }
		// // // Fragment = new ClockMyFragment(mEverList, isLeft);
		// // FragmentManager fm = getFragmentManager();
		// // FragmentTransaction transaction = fm.beginTransaction();
		// // transaction.replace(R.id.clock_other_user_fragment,
		// // clockFollowFragment);
		// // transaction.commit();
		// }
		// });
		// clock_my_firstitem_fansLayout.setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// Intent in = new Intent(mContext, ClockFansActivity.class);
		// in.putExtra("thisjobuserid", OtherUserId);
		// in.putExtra("isother", true);
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
		// in.putExtra("userid", OtherUserId);
		// in.putExtra("isother", true);
		// startActivity(in);
		// }
		// });
		// clock_my_firstitem_takeClockLayout.setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// Intent in = new Intent(mContext, ClockDetailActivity.class);
		// in.putExtra("userid", OtherUserId);
		// in.putExtra("name", createUserBean.getName());
		// in.putExtra("isother", true);
		// startActivity(in);
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
					}
				});

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
							.argb(alf, 0x44, 0x44, 0x44));

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
		// clockMyFragment = new ClockMyFragment(OtherUserId,
		// parentScrollView,
		// clock_other_user_belowHeadlayout);
		// ft.add(R.id.clock_other_user_fragment, clockMyFragment);
		// }
		// break;
		// case 2:
		// if (clockMyJobListFragment != null)
		// ft.show(clockMyJobListFragment);
		// else {
		//
		// clockMyJobListFragment = new ClockMyJobListFragment(
		// mContext, OtherUserId, parentScrollView,
		// clock_other_user_belowHeadlayout);
		// ft.add(R.id.clock_other_user_fragment, clockMyJobListFragment);
		// }
		// break;
		// case 3:
		// if (clockFollowFragment != null)
		// ft.show(clockFollowFragment);
		// else {
		// clockFollowFragment = new ClockFollowFragment(
		// mContext, OtherUserId, true,
		// parentScrollView,
		// clock_other_user_belowHeadlayout);
		// ft.add(R.id.clock_other_user_fragment, clockFollowFragment);
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

	public void getUserInfoConn() {
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(OtherUserId);
		sb.append("&curUserId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_GETBYIDUSER, null, sb.toString(),
				ClockOtherUserActivity.this).addList(null)
				.request(UrlParams.GET);
	}

	/**
	 * 获取勋章
	 */
	public void getBadeConn(String userID) {
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_USER_GETBADGE, null, sb.toString(),
				ClockOtherUserActivity.this).addList(null)
				.request(UrlParams.GET);

	}

	// public void Conn() {
	// StringBuffer sb = new StringBuffer();
	// sb.append("?userId=");
	// sb.append(OtherUserId);
	// sb.append("&curUserId=");
	// sb.append(userID);
	// Service.getService(Contanst.HTTP_GETALL, null, sb.toString(),
	// ClockOtherUserActivity.this).addList(null)
	// .request(UrlParams.GET);
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
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.GETBYIDUSER:
					createUserBean = (CreateUserBean) msg.obj;
					initHeadView();
					getBadeConn(createUserBean.getId() + "");
					// Conn();
					break;
				case Contanst.GETALL:
					mTaskAllBean = (MyTaskAllBean) msg.obj;
					mNewList = mTaskAllBean.getOngoingTasks();
					if (mNewList == null || mNewList.size() == 0) {
						sizeNullImage.setVisibility(View.VISIBLE);
					} else {
						sizeNullImage.setVisibility(View.GONE);
						mNewAdapter = new ClockMyAdapter(mContext, true);
						mNewAdapter.setList(mNewList, true);
						// mListView.setAdapter(mNewAdapter);
						// LvHeightUtil
						// .setListViewHeightBasedOnChildren(mListView);
					}

					break;
				case Contanst.USER_FOLLOW:
					isConn = false;
					Toast.makeText(mContext, "加入关注," + fansName,
							Toast.LENGTH_SHORT).show();
					getUserInfoConn();
					break;
				case Contanst.USER_UNFOLLOW:
					isConn = false;
					Toast.makeText(mContext, "取消关注," + fansName,
							Toast.LENGTH_SHORT).show();
					getUserInfoConn();
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
				case Contanst.GETBYIDUSER:
					Tools.getLog(Tools.d, "aaa", "GETBYIDUSER:" + message);
					break;
				case Contanst.GETALL:
					Tools.getLog(Tools.d, "aaa", "GETALL:" + message);
					break;
				case Contanst.USER_GETBADGE:

					break;
				case Contanst.USER_FOLLOW:
					isConn = false;
					break;
				case Contanst.USER_UNFOLLOW:
					isConn = false;
					break;
				}
				break;
			}
		}
	};
	String fansName;

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
		// clock_my_firstitem_fans.setText(createUserBean.getFansCount());
		fansName = createUserBean.getName();
		int relation = createUserBean.getRelation();
		if (relation == -1) {
		} else if (relation == 0 || relation == 1) {
			// title_item_rightImage
			// .setImageResource(R.drawable.geren_nav_guanzhu);
			// title_item_rightText.setTextColor(mContext.getResources().getColor(
			// R.color.meibao_color_9));
			// title_item_rightText.setText("关注");
			clock_my_firstitem_BRImage
					.setImageResource(R.drawable.geren_tr_guanzhu);
			isFans = false;

		} else if (relation == 2 || relation == 3) {
			// holder.fansfragment_listview_item_fanstypeLayout.setVisibility(View.VISIBLE);
			// title_item_rightImage
			// .setImageResource(R.drawable.geren_guanzhu_yiguan);
			// title_item_rightText.setTextColor(mContext.getResources().getColor(
			// R.color.meibao_color_11));
			// title_item_rightText.setText("已关注");
			clock_my_firstitem_BRImage
					.setImageResource(R.drawable.geren_tr_yiguan);
			isFans = true;
		}
		clock_my_firstitem_BRImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isConn) {
					return;
				}
				isConn = true;
				if (isFans) {
					connUnFollow();
				} else {
					connFollow();
				}
			}
		});

		// clock_my_firstitem_guanzhu.setText(createUserBean.getFollowingCount());//关注数
		// clock_my_firstitem_fans.setText(createUserBean.getFansCount());//粉丝数
		// clock_my_firstitem_takeClockNum.setText(createUserBean.getTotalJobCount()+"");//共打卡数
		clock_my_firstitem_userName.setText(createUserBean.getName());
		mTitleContent.setText(createUserBean.getName());
		clock_my_firstitem_point.setText(createUserBean.getPoint());
	}

	public void connFollow() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("followedUserId", OtherUserId));
			params.add(new BasicNameValuePair("fansUserId", userID));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_USER_FOLLOW, null, null,
				ClockOtherUserActivity.this).addList(params)
				.request(UrlParams.POST);
	}

	public void connUnFollow() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("followedUserId", OtherUserId));
			params.add(new BasicNameValuePair("fansUserId", userID));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_USER_UNFOLLOW, null, null,
				ClockOtherUserActivity.this).addList(params)
				.request(UrlParams.POST);
	}

	@SuppressLint("NewApi")
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
		// if(type == 0){
		// clockMyFragment = new ClockMyFragment( true,longItem,OtherUserId);
		// FragmentManager fm = getFragmentManager();
		// FragmentTransaction transaction = fm.beginTransaction();
		// transaction.replace(R.id.clock_other_user_fragment, clockMyFragment);
		// transaction.commit();
		// }else if(type == 1){
		// clockMyJobListFragment = new
		// ClockMyJobListFragment(mContext,OtherUserId);
		// FragmentManager fm = getFragmentManager();
		// FragmentTransaction transaction = fm.beginTransaction();
		// transaction.replace(R.id.clock_other_user_fragment,
		// clockMyJobListFragment);
		// transaction.commit();
		// }else if(type == 2){
		// clockFollowFragment = new
		// ClockFollowFragment(mContext,OtherUserId,true);
		// FragmentManager fm = getFragmentManager();
		// FragmentTransaction transaction = fm.beginTransaction();
		// transaction.replace(R.id.clock_other_user_fragment,
		// clockFollowFragment);
		// transaction.commit();
		// }
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		// onPageEnd 在onPause
		// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		ClockOther2_tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		ClockOther2_tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		ClockOther2_tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 0, dm));
		// 设置Tab Indicator的高度
		ClockOther2_tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		ClockOther2_tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		ClockOther2_tabs.setSelectTextSice((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		ClockOther2_tabs.setIndicatorColor(getResources().getColor(
				R.color.meibao_color_1));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		ClockOther2_tabs.setSelectedTextColor(getResources().getColor(
				R.color.meibao_color_1));
		// 取消点击Tab时的背景色
		ClockOther2_tabs.setTabBackground(0);
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
					clockMyFragment = new ClockMyFragment(OtherUserId);
				}
				return clockMyFragment;

			case 1:

				if (clockFollowFragment == null) {
					clockFollowFragment = new ClockFollowFragment(mContext,
							OtherUserId);
				}
				return clockFollowFragment;
			case 2:

				if (clockFansFragment == null) {
					clockFansFragment = new ClockFansFragment(mContext,
							OtherUserId);
				}
				return clockFansFragment;
			case 3:

				if (clockMyJobListFragment == null) {
					clockMyJobListFragment = new ClockMyJobListFragment(
							mContext, OtherUserId);
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
