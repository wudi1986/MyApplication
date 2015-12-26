package com.yktx.check.listview;


import com.yktx.check.BaseActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

public class ListViewForScrollView extends ListView {
    public ListViewForScrollView(Context context) {
        super(context);
    }
    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListViewForScrollView(Context context, AttributeSet attrs,
        int defStyle) {
        super(context, attrs, defStyle);
    }
    ScrollView parentScrollView;

	public void setParentScrollView(ScrollView parentScrollView) {
		this.parentScrollView = parentScrollView;
	}

//    @Override
//    /**
//     * 重写该方法，达到使ListView适应ScrollView的效果
//     */
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//        MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, expandSpec);
//    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			 setParentScrollAble(true);//
			// 当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview
			// 停住不能滚动
			 break;
		case MotionEvent.ACTION_MOVE:
			 setParentScrollAble(true);//
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
		if (parentScrollView != null)
			parentScrollView.requestDisallowInterceptTouchEvent(!flag);
		// 这里的parentScrollView就是listview外面的那个scrollview
	}
}
