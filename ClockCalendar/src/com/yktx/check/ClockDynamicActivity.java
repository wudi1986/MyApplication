package com.yktx.check;

import java.lang.reflect.Field;

import android.content.Context;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.yktx.check.square.fragment.AttentionFragment2;
import com.yktx.check.square.fragment.DynamicFragment;
import com.yktx.check.widget.OldPagerSlidingTabStrip;

public class ClockDynamicActivity extends FragmentActivity {
	//	private ImageView mLeftTitleImage,mRightTitleImage;
	//	private TextView mContentTitle;
	public SharedPreferences settings;
	public Editor mEditor;
	private Context mContext;

	ImageView ClockDynamic_title_back,ClockDynamic_title_point;
	OldPagerSlidingTabStrip ClockDynamic_tabs;
	ViewPager ClockDynamic_pager;

	/**
	 * 关注
	 */
	private AttentionFragment2 attentionFragment2;
	/**
	 * 消息
	 */
	private DynamicFragment dynamicFragment;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_clock_dynamic);
		mContext = this;
		initView();
		//		setDefaultFragment();
	}
	public void initView(){

		
		dm = getResources().getDisplayMetrics();
		settings = getSharedPreferences("clock", MODE_PRIVATE);
		mEditor = settings.edit();
		if(settings.getBoolean("isred", false)){
			mEditor.putBoolean("isred", false);
			mEditor.commit();
		}
		setOverflowShowingAlways();


		

		ClockDynamic_title_back = (ImageView) findViewById(R.id.ClockDynamic_title_back);
		ClockDynamic_tabs = (OldPagerSlidingTabStrip) findViewById(R.id.ClockDynamic_tabs);
		ClockDynamic_pager = (ViewPager) findViewById(R.id.ClockDynamic_pager);
		ClockDynamic_title_point = (ImageView) findViewById(R.id.ClockDynamic_title_point);

		ClockDynamic_pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		//		pager.setOffscreenPageLimit(4);


		ClockDynamic_tabs.setViewPager(ClockDynamic_pager);
		setTabsValue();


		ClockDynamic_title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		ClockDynamic_title_point.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, ClockSearchActivity.class);
				mContext.startActivity(in);
				overridePendingTransition(R.anim.fade, R.anim.hold);
			}
		});



		//		mContentTitle = (TextView) findViewById(R.id.title_item_content);
		//		mLeftTitleImage = (ImageView) findViewById(R.id.title_item_leftImage);
		//		mRightTitleImage = (ImageView) findViewById(R.id.title_item_rightImage);
		//		mRightTitleImage.setVisibility(View.VISIBLE);
		//		mRightTitleImage.setImageResource(R.drawable.xx_instruction);
		//		mContentTitle.setText("消息");
		//		mLeftTitleImage.setOnClickListener(new OnClickListener() {
		//			
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				finish();
		//			}
		//		});
		//		mRightTitleImage.setOnClickListener(new OnClickListener() {
		//			
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				Intent in = new Intent(mContext, PointExplainActivity.class);
		//				startActivity(in);
		//			}
		//		});
	}


	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		ClockDynamic_tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		ClockDynamic_tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		ClockDynamic_tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 0, dm));
		// 设置Tab Indicator的高度
		ClockDynamic_tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		ClockDynamic_tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		ClockDynamic_tabs.setSelectTextSice((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		ClockDynamic_tabs.setIndicatorColor(getResources().getColor(R.color.meibao_color_1));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		ClockDynamic_tabs.setSelectedTextColor(getResources().getColor(
				R.color.meibao_color_1));
		// 取消点击Tab时的背景色
		ClockDynamic_tabs.setTabBackground(0);
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

		private final String[] titles = {  "消息", "关注"};

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
				if (dynamicFragment == null) {
					dynamicFragment = new DynamicFragment();
				}
				return dynamicFragment;

			case 1:

				if (attentionFragment2 == null) {
					attentionFragment2 = new AttentionFragment2();;
				}
				return attentionFragment2;


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
}
