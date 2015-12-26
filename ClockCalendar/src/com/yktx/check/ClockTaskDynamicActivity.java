package com.yktx.check;

import java.lang.reflect.Field;

import com.umeng.analytics.MobclickAgent;
import com.yktx.check.square.fragment.CommentFragment;
import com.yktx.check.square.fragment.VoteFragment;
import com.yktx.check.widget.OldPagerSlidingTabStrip;

import android.os.Bundle;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ClockTaskDynamicActivity extends FragmentActivity {
	ImageView ClockTaskDynamic_title_back;
	OldPagerSlidingTabStrip ClockTaskDynamic_tabs;
	ViewPager ClockTaskDynamic_pager;
	
	/**
	 * 最新
	 */
	private VoteFragment voteFragment;
	/**
	 * 最热
	 */
	private CommentFragment commentFragment;
	
	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;
	
	int CurrentItem;//默认选中哪一个 0 打气  1评论
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		setContentView(R.layout.activity_clock_task_dynamic);
		setOverflowShowingAlways();
		
		
		dm = getResources().getDisplayMetrics();
		
		ClockTaskDynamic_title_back = (ImageView) findViewById(R.id.ClockTaskDynamic_title_back);
		ClockTaskDynamic_tabs = (OldPagerSlidingTabStrip) findViewById(R.id.ClockTaskDynamic_tabs);
		ClockTaskDynamic_pager = (ViewPager) findViewById(R.id.ClockTaskDynamic_pager);
		
		ClockTaskDynamic_pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
//		pager.setOffscreenPageLimit(4);
		
		CurrentItem = getIntent().getIntExtra("CurrentItem", 0);
		
		ClockTaskDynamic_pager.setCurrentItem(CurrentItem);//默认选中那一页
		ClockTaskDynamic_tabs.setCurrentItem(CurrentItem); 
		
		ClockTaskDynamic_tabs.setViewPager(ClockTaskDynamic_pager);
		setTabsValue();
		
		
		ClockTaskDynamic_title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		ClockTaskDynamic_tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		ClockTaskDynamic_tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		ClockTaskDynamic_tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 0, dm));
		// 设置Tab Indicator的高度
		ClockTaskDynamic_tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		ClockTaskDynamic_tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		ClockTaskDynamic_tabs.setSelectTextSice((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		ClockTaskDynamic_tabs.setIndicatorColor(getResources().getColor(R.color.meibao_color_1));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		ClockTaskDynamic_tabs.setSelectedTextColor(getResources().getColor(
				R.color.meibao_color_1));
		// 取消点击Tab时的背景色
		ClockTaskDynamic_tabs.setTabBackground(0);
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

		private final String[] titles = {  "打气", "评论"};

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
				
				if (voteFragment == null) {
					voteFragment = new VoteFragment(ClockTaskDynamicActivity.this);;
				}
				return voteFragment;
			case 1:
				if (commentFragment == null) {
					commentFragment = new CommentFragment(ClockTaskDynamicActivity.this);
				}
				return commentFragment;
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

}
