/**
The MIT License (MIT)

Copyright (c) 2014 singwhatiwanna
https://github.com/singwhatiwanna
http://blog.csdn.net/singwhatiwanna

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.yktx.check.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.yktx.check.R;
import com.yktx.check.listview.XListView.OnXScrollListener;
import com.yktx.check.util.Tools;

public class PinnedHeaderExpandableListView extends ExpandableListView
		implements OnScrollListener {
	private static final String TAG = "PinnedHeaderExpandableListView";
	private static final boolean DEBUG = true;

	public interface OnHeaderUpdateListener {
		/**
		 * 返回一个view对象即可 注意：view必须要有LayoutParams
		 */
		public View getPinnedHeader();

		public void updatePinnedHeader(View headerView, int firstVisibleGroupPos);
	}

	/**
	 * 卡位的header
	 */
	private View mStuckHeaderView;
	private int mHeaderWidth;
	private int mHeaderHeight;

	private View mTouchTarget;

	private OnScrollListener mScrollListener;
	private OnHeaderUpdateListener mHeaderUpdateListener;

	private boolean mActionDownHappened = false;
	protected boolean mIsHeaderGroupClickable = true;
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.
	private int mHeaderViewHeight; // header view's height

	// ============================添加下拉加载=====================
	/** 手在屏幕上滑动的位置 */
	private float mLastY = -1; // save event y
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;

	private XListViewHeader mHeaderView;
	private XListViewFooter mFooterView;
	private RelativeLayout mHeaderViewContent;
	private Scroller mScroller; // used for scroll back
	private int mScrollBack;
	private LinearLayout mContainer;
	private int mTotalItemCount;
	boolean isMyProgressBar = true;
	private IXListViewOnScroll iXListViewOnScroll;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;
	private IXListViewListener mListViewListener;

	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	private final static int SCROLL_DURATION = 400; // scroll back duration
	private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
														// at bottom, trigger
														// load more.
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
													// feature.

	public void setIsShow(boolean isShow) {
		mFooterView.setIsShow(isShow);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	@SuppressLint("Recycle")
	private void initWithContext(Context context, AttributeSet attrs) {

		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.XListView);
			isMyProgressBar = a.getBoolean(
					R.styleable.XListView_isMyProgressBar, false);
			a.recycle();
		}
		isMyProgressBar = true;
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);
		mHeaderView = new XListViewHeader(context, isMyProgressBar);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);
		addHeaderView(mHeaderView);
		mFooterView = new XListViewFooter(context);
		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});

	}

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		// TODO Auto-generated method stub
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}

	// @Override
	// public void setAdapter(ListAdapter adapter) {
	// // make sure XListViewFooter is the last footer view, and only add once.
	// if (mIsFooterReady == false) {
	// mIsFooterReady = true;
	// addFooterView(mFooterView);
	// }
	// super.setAdapter(adapter);
	// }
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		// if(isNotTouch){
		// return false;
		// }
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			// System.out.println("数据监测：" + getFirstVisiblePosition() + "---->"
			// + getLastVisiblePosition() + "mLastY = ev.getRawY()");
			if (iXListViewOnScroll != null) {
				iXListViewOnScroll.onScroll();
			}
			// mEnablePullRefresh 取消上拉

			if (mEnablePullRefresh
					&& (getFirstVisiblePosition() == 0 && (mHeaderView
							.getVisiableHeight() > 0 || deltaY > 0))) {
				// the first item is showing, header has shown or pull down.
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				invokeOnScrolling();
			}
			if (getLastVisiblePosition() == mTotalItemCount - 1
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
				// last item, already pulled up or want to pull up.

				updateFooterHeight(-deltaY / OFFSET_RADIO);

			}
			break;
		case MotionEvent.ACTION_CANCEL:
			// if (mEnablePullGoHome
			// && mHeaderView.getVisiableHeight() > mHeaderViewHeight * 2) {
			// if (iXListViewOnGoHome != null) {
			//
			// }
			// }
			break;
		default:
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh

				if (mEnablePullRefresh
						&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					mPullRefreshing = true;
					mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) {
						mListViewListener.onRefresh();
					}
				}

				resetHeaderHeight();
			}
			if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// invoke load more.

				if (mEnablePullLoad
						&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
					startLoadMore();
				}
				resetFooterHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头

			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); // scroll to top each time
	}

	public void setIXListViewOnScroll(IXListViewOnScroll l) {
		iXListViewOnScroll = l;
	}

	public interface IXListViewOnScroll {

		public void onScroll();
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
													// more.
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();

	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	/**
	 * enable or disable pull down refresh feature.
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
		mHeaderViewContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// startLoadMore();
			}
		});
	}
	/**
	 * enable or disable pull up load more feature.
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);

			mFooterView.setVisibility(View.GONE);
		} else {
			mPullLoading = false;
			mFooterView.setVisibility(View.VISIBLE);
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	// ================= end =========================
	public PinnedHeaderExpandableListView(Context context) {
		super(context);
		initView(context, null);
	}

	public PinnedHeaderExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
	}

	public PinnedHeaderExpandableListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initView(context, attrs);
	}

	private void initView(Context context, AttributeSet attrs) {
		setFadingEdgeLength(0);
		setOnScrollListener(this);
		initWithContext(context, attrs);
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		if (l != this) {
			mScrollListener = l;
		} else {
			mScrollListener = null;
		}
		super.setOnScrollListener(this);
	}

	/**
	 * 给group添加点击事件监听
	 * 
	 * @param onGroupClickListener
	 *            监听
	 * @param isHeaderGroupClickable
	 *            表示header是否可点击<br/>
	 *            note :
	 *            当不想group可点击的时候，需要在OnGroupClickListener#onGroupClick中返回true，
	 *            并将isHeaderGroupClickable设为false即可
	 */
	public void setOnGroupClickListener(
			OnGroupClickListener onGroupClickListener,
			boolean isHeaderGroupClickable) {
		mIsHeaderGroupClickable = isHeaderGroupClickable;
		super.setOnGroupClickListener(onGroupClickListener);
	}

	public void setOnHeaderUpdateListener(OnHeaderUpdateListener listener) {
		mHeaderUpdateListener = listener;
		if (listener == null) {
			mStuckHeaderView = null;
			mHeaderWidth = mHeaderHeight = 0;
			return;
		}
		mStuckHeaderView = listener.getPinnedHeader();
		int firstVisiblePos = getFirstVisiblePosition();
		final int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
		listener.updatePinnedHeader(mStuckHeaderView, firstVisibleGroupPos);
		mStuckHeaderView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickHeadView.clickHead(firstVisibleGroupPos);
				return;
			}
		});
		// mHeaderView.setFocusable(false);
		// mHeaderView.setFocusableInTouchMode(false);
		// mHeaderView.requestFocus();
		requestLayout();
		postInvalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mStuckHeaderView == null) {
			return;
		}
		measureChild(mStuckHeaderView, widthMeasureSpec, heightMeasureSpec);
		mHeaderWidth = mStuckHeaderView.getMeasuredWidth();
		mHeaderHeight = mStuckHeaderView.getMeasuredHeight();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (mStuckHeaderView == null) {
			return;
		}
		int delta = mStuckHeaderView.getTop();
		mStuckHeaderView.layout(0, delta, mHeaderWidth, mHeaderHeight + delta);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mStuckHeaderView != null) {
			drawChild(canvas, mStuckHeaderView, getDrawingTime());
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		int pos = pointToPosition(x, y);
		if (mStuckHeaderView != null && y >= mStuckHeaderView.getTop()
				&& y <= mStuckHeaderView.getBottom()) {
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				mTouchTarget = getTouchTarget(mStuckHeaderView, x, y);
				mActionDownHappened = true;
			} else if (ev.getAction() == MotionEvent.ACTION_UP) {
				View touchTarget = getTouchTarget(mStuckHeaderView, x, y);
				if (touchTarget == mTouchTarget && mTouchTarget.isClickable()) {
					mTouchTarget.performClick();
					invalidate(new Rect(0, 0, mHeaderWidth, mHeaderHeight));
				} else if (mIsHeaderGroupClickable) {
					int groupPosition = getPackedPositionGroup(getExpandableListPosition(pos));
					if (groupPosition != INVALID_POSITION
							&& mActionDownHappened) {
						if (isGroupExpanded(groupPosition)) {
							collapseGroup(groupPosition);
						} else {
							expandGroup(groupPosition);
						}
					}
				}
				mActionDownHappened = false;
			}
			return true;
		}

		return super.dispatchTouchEvent(ev);
	}

	private View getTouchTarget(View view, int x, int y) {
		if (!(view instanceof ViewGroup)) {
			return view;
		}

		ViewGroup parent = (ViewGroup) view;
		int childrenCount = parent.getChildCount();
		final boolean customOrder = isChildrenDrawingOrderEnabled();
		View target = null;
		for (int i = childrenCount - 1; i >= 0; i--) {
			final int childIndex = customOrder ? getChildDrawingOrder(
					childrenCount, i) : i;
			final View child = parent.getChildAt(childIndex);
			if (isTouchPointInView(child, x, y)) {
				target = child;
				break;
			}
		}
		if (target == null) {
			target = parent;
		}

		return target;
	}

	private boolean isTouchPointInView(View view, int x, int y) {
		if (view.isClickable() && y >= view.getTop() && y <= view.getBottom()
				&& x >= view.getLeft() && x <= view.getRight()) {
			return true;
		}
		return false;
	}

	public void requestRefreshHeader() {
		refreshHeader();
		invalidate(new Rect(0, 0, mHeaderWidth, mHeaderHeight));
	}

	protected void refreshHeader() {
		if (mStuckHeaderView == null) {
			return;
		}
		int firstVisiblePos = getFirstVisiblePosition();
		int pos = firstVisiblePos + 1;
		int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
		int group = getPackedPositionGroup(getExpandableListPosition(pos));
		if (DEBUG) {
			Tools.getLog(Tools.d, TAG, "refreshHeader firstVisibleGroupPos="
					+ firstVisibleGroupPos);
		}

		if (group == firstVisibleGroupPos + 1) {
			View view = getChildAt(1);
			if (view == null) {
				Log.w(TAG, "Warning : refreshHeader getChildAt(1)=null");
				if (mHeaderUpdateListener != null) {
					mHeaderUpdateListener.updatePinnedHeader(mStuckHeaderView,
							firstVisibleGroupPos);
				}
				return;
			}
			if (view.getTop() <= mHeaderHeight) {
				int delta = mHeaderHeight - view.getTop();
				mStuckHeaderView.layout(0, -delta, mHeaderWidth, mHeaderHeight
						- delta);
			} else {
				// TODO : note it, when cause bug, remove it
				mStuckHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
			}
		} else {
			mStuckHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
		}
		// if (firstVisibleGroupPos <= 0) {
		// mHeaderView.setVisibility(View.GONE);
		// } else {
		if (mHeaderUpdateListener != null) {
			mHeaderUpdateListener.updatePinnedHeader(mStuckHeaderView,
					firstVisibleGroupPos);
		}
		// }
	}

	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mTotalItemCount = totalItemCount;
		if (totalItemCount > 0) {
			refreshHeader();
		}
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	setClickHeadViewlistener clickHeadView;

	public void setClickHeadView(setClickHeadViewlistener headView) {
		clickHeadView = headView;
	}
	public void setmFooterViewBackground(int color){
		mFooterView.background.setBackgroundColor(color);
	}

	public interface setClickHeadViewlistener {
		public void clickHead(int position);
	}
	

}