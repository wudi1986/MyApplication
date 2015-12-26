/**
 * 
 */
package com.yktx.check.util;

import com.yktx.check.BaseActivity;
import com.yktx.check.ClockMyActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**  
 * @author 作者 :  
 * @version 创建时间：2014年5月13日 下午2:04:28  
 * 类说明  */
/**
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class MyScrollView extends ScrollView {
	
	public MyScrollView(Context context) {
		super(context);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	float downY;
	/** 距离布局上方可移动的偏移量 */
	int tabY;
	ScrollView parentScrollView;

	int statusBarHeight;

	public void setParentScrollView(ScrollView parentScrollView) {
		this.parentScrollView = parentScrollView;
		statusBarHeight = (int) (getStatusBarHeight() + 48 * BaseActivity.DENSITY);
	}

	public int getStatusBarHeight() {
		return Resources.getSystem().getDimensionPixelSize(
				Resources.getSystem().getIdentifier("status_bar_height",
						"dimen", "android"));
	}

	View offView;

	/**
	 * 根据外面scrollView 移动view的距离
	 */
	public void setViewGetLocationOnScreen(View offView) {
		this.offView = offView;
	}

	boolean isParentScrollAble;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY();
			int[] location = new int[2];
			if (offView != null) {
				offView.getLocationOnScreen(location);
				int x = location[0];
				tabY = location[1];
			}
			 setParentScrollAble(false);//
			// 当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview
			// 停住不能滚动
			 break;
		case MotionEvent.ACTION_MOVE:
			
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			setParentScrollAble(true);// 当手指松开时，让父ScrollView重新拿到onTouch权限
			break;
		default:
			break;

		}
		return super.onInterceptTouchEvent(ev);

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
			// setParentScrollAble(false);//
			// 当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview
			// 停住不能滚动
		case MotionEvent.ACTION_MOVE:
			
			
			Tools.getLog(Tools.i, "aaa", "downY =============== " + downY);
			Tools.getLog(Tools.i, "aaa",
					"ev.getY() =============== " + ev.getY());
			Tools.getLog(Tools.i, "aaa", "tabY =============== "
					+ tabY);
			Tools.getLog(Tools.i, "aaa", "statusBarHeight =============== "
					+ statusBarHeight);

			Tools.getLog(Tools.i, "aaa", "getScrollY() =============== "
					+ getScrollY());
			
			
			if (parentScrollView != null && offView != null ) {
				if (downY > ev.getY()) {
					// 向上滑动
					if (tabY <= statusBarHeight) {
						setParentScrollAble(false);
						isParentScrollAble = false;
					}else {
						setParentScrollAble(true);
						isParentScrollAble = true;
					}
				} else {
					if (tabY <= statusBarHeight && getScrollY() != 0) {
						setParentScrollAble(false);
						isParentScrollAble = false;
					}else {
						setParentScrollAble(true);
						isParentScrollAble = true;
					}
				}
			}
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
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
		if (parentScrollView != null)
			parentScrollView.requestDisallowInterceptTouchEvent(!flag);
		// 这里的parentScrollView就是listview外面的那个scrollview
	}
}
