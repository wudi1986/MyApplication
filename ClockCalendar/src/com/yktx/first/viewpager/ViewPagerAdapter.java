package com.yktx.first.viewpager;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.yktx.check.BaseActivity;
import com.yktx.check.ClockMainActivity;
import com.yktx.check.LoginActivity;
import com.yktx.check.R;
import com.yktx.check.util.Tools;

public class ViewPagerAdapter extends PagerAdapter{
	
	//界面列表
	private List<View> views;
	FirstViewPagerActivity mContext;
	
	public ViewPagerAdapter (List<View> views, FirstViewPagerActivity mContext){
		this.views = views;
		this.mContext = mContext;
	}

	//销毁arg1位置的界面
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));		
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub
		
	}

	//获得当前界面数
	@Override
	public int getCount() {
		if (views != null)
		{
			return views.size();
		}
		
		return 0;
	}
	

	//初始化arg1位置的界面
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		Tools.getLog(Tools.i,"aaa", "arg1 =========== "+arg1);
		
		View view = views.get(arg1);
		if(arg1 == views.size()-1){
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent in = new Intent();
					if (BaseActivity.isLogin) {
						in = new Intent(mContext, ClockMainActivity.class);
					} else {
						in = new Intent(mContext, LoginActivity.class);
					}
					mContext.mEditor.putBoolean("isFirstOpen", false);
					mContext.mEditor.commit();
					mContext.startActivity(in);
					// PendingTransition(anim_enter, anim_exit);
					mContext.finish();
					mContext.overridePendingTransition(R.anim.slide_alpha_in_right, R.anim.slide_alpha_out_left);
				}
			});
		}
		((ViewPager) arg0).addView(view, 0);
		return views.get(arg1);
	}

	//判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
