package com.duguang.baseanimation.ui.listivew.deletelistview;

import com.yktx.check.ClockMainActivity;
import com.yktx.check.bean.SlideBean;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class ListViewCompat extends ListView {

	private static final String TAG = "ListViewCompat";

	private SlideView mFocusedItemView;

	public ListViewCompat(Context context) {
		super(context);
	}

	public ListViewCompat(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewCompat(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void shrinkListItem(int position) {
		View item = getChildAt(position);

		if (item != null) {
			try {
				((SlideView) item).shrink();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
	}

	float downX, downY;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(ClockMainActivity.isMove)
			return true;
		return super.onKeyDown(keyCode, event);
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
				int x = (int) event.getX();
				int y = (int) event.getY();
				downX = event.getX();
				downY = event.getY();
				ClockMainActivity.isMove = false;
				int position = pointToPosition(x, y);
				Log.e(TAG, "postion=" + position);
				if (position != INVALID_POSITION) {
					SlideBean data = (SlideBean) getItemAtPosition(position);
					mFocusedItemView = data.getSlideView();
					Log.e(TAG, "FocusedItemView=" + mFocusedItemView);
				}
			}
			break;
		case MotionEvent.ACTION_MOVE: {
	
				int x = (int) event.getX();
				int y = (int) event.getY();
				if (!ClockMainActivity.isMove && Math.abs(downX - x) > 50 && Math.abs(downY - y) < 50) {
					ClockMainActivity.isMove = true;
				}
			}
			break;
		}

		if (mFocusedItemView != null && ClockMainActivity.isMove) {
			mFocusedItemView.onRequireTouchEvent(event);
			return false;
		}

		return super.onTouchEvent(event);
	}
}
