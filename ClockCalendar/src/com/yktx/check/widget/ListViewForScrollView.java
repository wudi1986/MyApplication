package com.yktx.check.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.yktx.check.BaseActivity;
import com.yktx.check.ClockNewActivity;
import com.yktx.check.R;
import com.yktx.check.listview.XListViewHeader;
import com.yktx.check.util.Tools;

@SuppressLint("NewApi")
public class ListViewForScrollView extends ListView {
	ScrollView parentScrollView;
	int height;
	int statusBarHeight;
	RelativeLayout mHeaderViewContent;
	private XListViewHeader mHeaderView;

	public ListViewForScrollView(Context context, ScrollView parentScrollView) {
		super(context);
		this.parentScrollView = parentScrollView;
		init(context);
	}

	public void setScrollView(ScrollView parentScrollView) {
		this.parentScrollView = parentScrollView;
	}

	public ListViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

	}

	public ListViewForScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		statusBarHeight = (int) (getStatusBarHeight() + 48 * BaseActivity.DENSITY);
		mHeaderView = new XListViewHeader(context, null);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);
		addHeaderView(mHeaderView);
		mHeaderViewContent.setVisibility(View.GONE);
	}

	public int getStatusBarHeight() {
		return Resources.getSystem().getDimensionPixelSize(
				Resources.getSystem().getIdentifier("status_bar_height",
						"dimen", "android"));
	}

	// @Override
	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 */
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
	// MeasureSpec.AT_MOST);
	// super.onMeasure(widthMeasureSpec, expandSpec);
	// }

	float downY;
	int tabY;

	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	// // TODO Auto-generated method stub
	// Tools.getLog(Tools.i, "downY", "downY =============== "+downY);
	// switch (ev.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// int[] location = new int[2];
	// downY = ev.getY();
	// ClockNewActivity.new_tabs.getLocationOnScreen(location);
	// int x = location[0];
	// tabY = location[1];
	//
	// case MotionEvent.ACTION_MOVE:
	//
	// Tools.getLog(Tools.i, "downY", "downY =============== "+downY);
	// Tools.getLog(Tools.i, "aaa", "ev.getY() =============== "+ev.getY());
	// if(downY > ev.getY()){
	// //向上滑动
	// if(tabY <= statusBarHeight){
	// setParentScrollAble(false);
	// }
	// } else {
	// if(tabY <= statusBarHeight && getFirstVisiblePosition() != 0){
	// setParentScrollAble(false);
	// }
	// }
	// break;
	// case MotionEvent.ACTION_UP:
	// case MotionEvent.ACTION_CANCEL:
	// setParentScrollAble(true);// 当手指松开时，让父ScrollView重新拿到onTouch权限
	// break;
	// default:
	// break;
	//
	// }
	// return super.onInterceptTouchEvent(ev);
	//
	// }

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int[] location = new int[2];
			downY = ev.getY();
			ClockNewActivity.new_tabs.getLocationOnScreen(location);
			int x = location[0];
			tabY = location[1];
			setParentScrollAble(false);
			isParentScrollAble = false;
		case MotionEvent.ACTION_MOVE:

			// if(downY > ev.getY()){
			// //向上滑动
			// if(tabY <= statusBarHeight){
			// setParentScrollAble(false);
			// }
			// } else {
			// if(tabY <= statusBarHeight && getFirstVisiblePosition() != 0){
			// setParentScrollAble(false);
			// }
			// }
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			// setParentScrollAble(true);// 当手指松开时，让父ScrollView重新拿到onTouch权限
			break;
		default:
			break;

		}
		return super.onInterceptTouchEvent(ev);
	}

	boolean isParentScrollAble;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:

			Tools.getLog(Tools.i, "aaa", "downY =============== " + downY);
			Tools.getLog(Tools.i, "aaa",
					"ev.getY() =============== " + ev.getY());
			Tools.getLog(Tools.i, "aaa", "tabY =============== " + tabY);
			Tools.getLog(Tools.i, "aaa", "statusBarHeight =============== "
					+ statusBarHeight);
			Tools.getLog(Tools.i, "aaa", "getFirstVisiblePosition =============== "
					+ getFirstVisiblePosition());
			if (parentScrollView != null) {
				if (downY > ev.getY()) {
					// 向上滑动
					if (tabY <= statusBarHeight) {
						setParentScrollAble(false);
						isParentScrollAble = false;
					} else {
						setParentScrollAble(true);
						isParentScrollAble = true;
					}
				} else {
					if (tabY <= statusBarHeight
							&& getFirstVisiblePosition() != 0) {
						setParentScrollAble(false);
						isParentScrollAble = false;
					} else {
						setParentScrollAble(true);
						isParentScrollAble = true;
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
//		case MotionEvent.ACTION_CANCEL:
			setParentScrollAble(true);// 当手指松开时，让父ScrollView重新拿到onTouch权限
			break;
		default:
			break;

		}
		if (isParentScrollAble) {
			return true;
		} else {
			return super.onTouchEvent(ev);
		}
	}

	/**
	 * 是否把滚动事件交给父scrollview
	 * 
	 * @param flag
	 */
	private void setParentScrollAble(boolean flag) {
		parentScrollView.requestDisallowInterceptTouchEvent(!flag);
		// 这里的parentScrollView就是listview外面的那个scrollview
	}

}
