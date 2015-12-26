package com.yktx.check;

import java.lang.reflect.Field;

import com.umeng.analytics.MobclickAgent;
import com.yktx.check.ClockDynamicActivity.MyPagerAdapter;
import com.yktx.check.square.fragment.AttentionFragment2;
import com.yktx.check.square.fragment.BuildingRankingListFragment;
import com.yktx.check.square.fragment.ClockGroupInfoFragment;
import com.yktx.check.square.fragment.DynamicFragment;
import com.yktx.check.widget.OldPagerSlidingTabStrip;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ImageView;

public class ClockGroupInfoFragmentActivity extends FragmentActivity {
	public SharedPreferences settings;
	public Editor mEditor;
	private Context mContext;

	ImageView ClockGroupInfoFragment_title_back;
	OldPagerSlidingTabStrip ClockGroupInfoFragment_tabs;
	ViewPager ClockGroupInfoFragment_pager;
	
	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;
	
	/**
	 * 打卡明细
	 */
	ClockGroupInfoFragment clockGroupInfoFragment;
	
	/**
	 * 达人榜单
	 */
	BuildingRankingListFragment buildingRankingListFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_clock_group_info_fragment);
		
		mContext = this;
		
		dm = getResources().getDisplayMetrics();
		
		settings = getSharedPreferences("clock", MODE_PRIVATE);
		mEditor = settings.edit();
		setOverflowShowingAlways();
		
		ClockGroupInfoFragment_title_back = (ImageView) findViewById(R.id.ClockGroupInfoFragment_title_back);
		
		ClockGroupInfoFragment_tabs = (OldPagerSlidingTabStrip) findViewById(R.id.ClockGroupInfoFragment_tabs);
		
		ClockGroupInfoFragment_pager = (ViewPager) findViewById(R.id.ClockGroupInfoFragment_pager);
		

		ClockGroupInfoFragment_pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		//		pager.setOffscreenPageLimit(4);


		ClockGroupInfoFragment_tabs.setViewPager(ClockGroupInfoFragment_pager);
		setTabsValue();
		
		
		ClockGroupInfoFragment_title_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
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
		ClockGroupInfoFragment_tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		ClockGroupInfoFragment_tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		ClockGroupInfoFragment_tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 0, dm));
		// 设置Tab Indicator的高度
		ClockGroupInfoFragment_tabs.setIndicatorHeight((int) TypedValue.applyDimension(  
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		ClockGroupInfoFragment_tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		ClockGroupInfoFragment_tabs.setSelectTextSice((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		ClockGroupInfoFragment_tabs.setIndicatorColor(getResources().getColor(R.color.meibao_color_1));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		ClockGroupInfoFragment_tabs.setSelectedTextColor(getResources().getColor(
				R.color.meibao_color_1));
		// 取消点击Tab时的背景色
		ClockGroupInfoFragment_tabs.setTabBackground(0);
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

		private final String[] titles = {  "打卡明细", "达人榜单"};

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
				if (clockGroupInfoFragment == null) {
					clockGroupInfoFragment = new ClockGroupInfoFragment(ClockGroupInfoFragmentActivity.this);
				}
				return clockGroupInfoFragment;

			case 1:

				if (buildingRankingListFragment == null) {
					buildingRankingListFragment = new BuildingRankingListFragment(ClockGroupInfoFragmentActivity.this);;
				}
				return buildingRankingListFragment;


				//			case 2:
				//				if (attentionFragment == null) {
				//					attentionFragment = new AttentionFragment2();
				//				}
				//				return attentionFragment;
				//			case 3:
				//				if (newFragment == null) {
				//					newFragment = new NewFragment();
				//				}
				//				return newFragment;
				//			
			default:
				return null;
			}
		}

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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		clockGroupInfoFragmentResult.setResult(requestCode, resultCode, data);
	}
	
	onClockGroupInfoFragmentResult clockGroupInfoFragmentResult;
	public void setOnClockGroupInfoFragmentResult(onClockGroupInfoFragmentResult result){
		clockGroupInfoFragmentResult = result;
	}
	
	public interface onClockGroupInfoFragmentResult{
		public void setResult(int requestCode, int resultCode, Intent data);
	}

}
