package com.yktx.check.widget;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.yktx.check.util.Tools;

public class CustomViewPagerAdapter<V extends View> extends PagerAdapter {

	private V[] views;

	public CustomViewPagerAdapter(V[] views) {
		super();
		this.views = views;
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	private int mChildCount = 0;

	@Override
	public void notifyDataSetChanged() {
		mChildCount = getCount();
		super.notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		if (mChildCount > 0) {
			mChildCount--;
			return POSITION_NONE;
		}
		return super.getItemPosition(object);
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public Object instantiateItem(ViewGroup arg0, int arg1) {
		if (((ViewPager) arg0).getChildCount() == views.length) {
			((ViewPager) arg0).removeView(views[arg1 % views.length]);
		}
		Tools.getLog(Tools.i, "kkk",
				"instantiateItem instantiateItem instantiateItem instantiateItem ============ "
						+ arg1);
		((ViewPager) arg0).addView(views[arg1 % views.length]);
		return views[arg1 % views.length];
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startUpdate(View arg0) {
	}

	public V[] getAllItems() {
		return views;
	}
}
