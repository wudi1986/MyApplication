package com.yktx.check.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

public class ListViewAndScrollView extends ListView {
	ScrollView parentScrollView;
	int height;
	int statusBarHeight;

	public ListViewAndScrollView(Context context, ScrollView parentScrollView) {
		super(context);
		this.parentScrollView = parentScrollView;
		statusBarHeight = getStatusBarHeight();
	}

	public void setScrollView(ScrollView parentScrollView) {
		this.parentScrollView = parentScrollView;
	}

	public ListViewAndScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewAndScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * @return 状态栏高度
	 */
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

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

		case MotionEvent.ACTION_MOVE:

			setParentScrollAble(false);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			setParentScrollAble(true);// 当手指松开时，让父ScrollView重新拿到onTouch权限
			break;
		default:
			break;

		}
		return super.onTouchEvent(ev);
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
