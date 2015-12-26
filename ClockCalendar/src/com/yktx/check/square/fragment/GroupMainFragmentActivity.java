package com.yktx.check.square.fragment;

import java.lang.reflect.Field;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.yktx.check.ClockSearchActivity;
import com.yktx.check.R;
import com.yktx.check.widget.OldPagerSlidingTabStrip;

public class GroupMainFragmentActivity extends FragmentActivity  {
	/**
	 * 最新
	 */
	private NewFragment newFragment;
	/**
	 * 最热
	 */
	private RecommendFragment recommendFragment;
	/**
	 * 动态
	 */
	private BoutiqueFragment boutiqueFragment;
	/**
	 * 关注
	 */
	private AttentionFragment2 attentionFragment;
	/**
	 * 关注
	 */
	private ReadCardFragment cradFragment ;
	private RelativeLayout rightHead;

	/**
	 * PagerSlidingTabStrip的实例
	 */
	public static OldPagerSlidingTabStrip tabs;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;


	/**
	 * 是否刷新activity（在fragment写的状态值）
	 */
	//	public static boolean isReflush;

	/**
	 * 刷新前选中的项
	 */
	//	public static int ReflushItem = 0;
	/**
	 * 刷新前选中的项
	 */
	//	public static int ReflushFragmentItem = 0; 

	//	public static boolean isLoadAgain = false;

	public static final String MAIN_NEW_HTTP = "MAIN_NEW_HTTP";
	public static final String MAIN_NEAR_HTTP = "MAIN_NEAR_HTTP";
	public static final String MAIN_RECOMMEND_HTTP = "MAIN_RECOMMEND_HTTP";
	public static final String MAIN_HOT_HTTP = "MAIN_HOT_HTTP";
	public static final String MAIN_ATTENTION_HTTP = "MAIN_ATTENTION_HTTP";
	public static final String MAIN_HISTORY_HTTP = "MAIN_HISTORY_HTTP";
	public static ViewPager pager;
	private ImageView title_back,title_search;

	public static SharedPreferences setting;

	public static Editor mEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_main_group_fragment);
		setOverflowShowingAlways();
		dm = getResources().getDisplayMetrics();
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (OldPagerSlidingTabStrip) findViewById(R.id.tabs);
		title_back = (ImageView) findViewById(R.id.title_back);
		title_search = (ImageView) findViewById(R.id.title_search);

		setting = getSharedPreferences("clock", MODE_PRIVATE);
		mEditor = setting.edit();
		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		title_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(GroupMainFragmentActivity.this, ClockSearchActivity.class);
				startActivity(in);
				overridePendingTransition(R.anim.fade, R.anim.hold);
			}
		});
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		pager.setOffscreenPageLimit(4);
		//		boolean isPush = getIntent().getBooleanExtra("isPush", false);
		//		if (isPush) {
		//		if(getIntent().getBooleanExtra("isDynamic", false)){//消息点精品推荐 进 精品页
		//			pager.setCurrentItem(1);
		//		}else{
		pager.setCurrentItem(0);
		//		}

		//		tabs.set
		//		}
		//		
		tabs.setViewPager(pager);
		setTabsValue();

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);       //统计时长
		//		if(!isReflush){
		//			
		//		}
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 0, dm));
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		tabs.setSelectTextSice((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(getResources().getColor(R.color.meibao_color_1));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(getResources().getColor(
				R.color.meibao_color_1));
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
		//		if(setting.getBoolean("isred", false)){
		//			Tools.getLog(Tools.d,"aaa", "开启小红点");
		//		}else{
		//			Tools.getLog(Tools.d,"aaa", "guanbi小红点");
		//		}
		//		
		//		if(!tabs.RedVisity()){
		//			tabs.setRedVisity(setting.getBoolean("isred", false));
		//		}

		//		tabs.addBadgeView(1, true);
	}

	public class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);

		}

		private final String[] titles = {  "精品", "榜单" ,  "最新"};

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

				if (boutiqueFragment == null) {
					boutiqueFragment = new BoutiqueFragment();
				}
				return boutiqueFragment;
			case 1:
				if (recommendFragment == null) {
					recommendFragment = new RecommendFragment();
				}
				return recommendFragment;
				//			case 2:
				//				if (attentionFragment == null) {
				//					attentionFragment = new AttentionFragment2();
				//				}
				//				return attentionFragment;
			case 2:
				if (newFragment == null) {
					newFragment = new NewFragment();
				}
				return newFragment;

			default:
				return null;
			}
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
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		tabs = null;
		pager = null;
	}
	//	@Override
	//	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
	//		// TODO Auto-generated method stub
	//		super.onActivityResult(arg0, arg1, arg2);
	//		if(isReflush){
	//			isLoadAgain = true;
	//		}
	//	}

}
