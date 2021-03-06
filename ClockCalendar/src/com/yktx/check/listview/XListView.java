/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * 		Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package com.yktx.check.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.yktx.check.BaseActivity;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockNewActivity;
import com.yktx.check.R;
import com.yktx.check.util.Tools;

public class XListView extends ListView implements OnScrollListener {

	/** 手在屏幕上滑动的位置 */
	private float mLastY = -1; // save event y

	ScrollView parentScrollView;

	private Scroller mScroller; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener

	// the interface to trigger refresh and load more.
	private IXListViewListener mListViewListener;

	private IXListViewOnScroll iXListViewOnScroll;
	private IXListViewOnGoHome iXListViewOnGoHome;

	// -- header view
	private XListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;
	// private TextView mHeaderTimeView;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;
	private boolean mEnablePullGoHome = false;
	private boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	private XListViewFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;

	float downY;
	/** 距离布局上方可移动的偏移量 */
	int tabY;

	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;

	private final static int SCROLL_DURATION = 400; // scroll back duration
	private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
														// at bottom, trigger
														// load more.
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
													// feature.

	public void setBackGround(int color) {
		mHeaderView.setBackGround(color);
		mFooterView.setBackGround(color);
	}

	public void setIsShow(boolean isShow) {
		mFooterView.setIsShow(isShow);
	}

	int statusBarHeight;

	public void setParentScrollView(ScrollView parentScrollView) {
		this.parentScrollView = parentScrollView;
		statusBarHeight = (int) (getStatusBarHeight() + 48 * BaseActivity.DENSITY);
	}

	/**
	 * @param context
	 */
	public XListView(Context context) {
		super(context);
		initWithContext(context, null);
	}

	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context, attrs);
	}

	public XListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context, attrs);
	}

	public int getStatusBarHeight() {
		return Resources.getSystem().getDimensionPixelSize(
				Resources.getSystem().getIdentifier("status_bar_height",
						"dimen", "android"));
	}

	boolean isMyProgressBar;

	@SuppressLint("Recycle")
	private void initWithContext(Context context, AttributeSet attrs) {

		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.XListView);
			isMyProgressBar = a.getBoolean(
					R.styleable.XListView_isMyProgressBar, false);
			a.recycle();
		}
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);
		// init header view
		mHeaderView = new XListViewHeader(context, isMyProgressBar);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);
		// mHeaderTimeView = (TextView) mHeaderView
		// .findViewById(R.id.xlistview_header_time);
		addHeaderView(mHeaderView);

		// init footer view
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

	View offView;

	/**
	 * 根据外面scrollView 移动view的距离
	 */
	public void setViewGetLocationOnScreen(View offView) {
		this.offView = offView;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			downY = ev.getY();
			if (offView != null) {
				int[] location = new int[2];
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
	public void setAdapter(ListAdapter adapter) {
		// make sure XListViewFooter is the last footer view, and only add once.
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
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

	public void setPullGoHome(boolean enable) {
		mEnablePullGoHome = enable;
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
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
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

	/**
	 * set last refresh time
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
		// mHeaderTimeView.setText(time);
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头

			if (mEnablePullGoHome) {
				if (mHeaderView.getVisiableHeight() > mHeaderViewHeight
						&& mHeaderView.getVisiableHeight() < mHeaderViewHeight * 2) {
					mHeaderView.setState(XListViewHeader.STATE_READY);
				} else if (mHeaderView.getVisiableHeight() > mHeaderViewHeight * 2) {
					mHeaderView.setState(XListViewHeader.STATE_GOHOME);
				} else {
					mHeaderView.setState(XListViewHeader.STATE_NORMAL);
				}
			} else {
				if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					mHeaderView.setState(XListViewHeader.STATE_READY);
				} else {
					mHeaderView.setState(XListViewHeader.STATE_NORMAL);
				}
			}
		}
		setSelection(0); // scroll to top each time
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

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	// public boolean isNotTouch = false;
	boolean isParentScrollAble;
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
			downY = ev.getY();
			if (offView != null) {
				int[] location = new int[2];
				offView.getLocationOnScreen(location);
				int x = location[0];
				tabY = location[1];
			}

			break;
		case MotionEvent.ACTION_MOVE:
			Tools.getLog(Tools.i, "aaa", "downY =============== " + downY);
			Tools.getLog(Tools.i, "aaa",
					"ev.getY() =============== " + ev.getY());
			Tools.getLog(Tools.i, "aaa", "tabY =============== " + tabY);
			Tools.getLog(Tools.i, "aaa", "getFirstVisiblePosition =============== "+getFirstVisiblePosition());
			if (parentScrollView != null && offView != null) {
				if (downY > ev.getY()) {
					// 向上滑动
					if (tabY <= statusBarHeight) {
						isParentScrollAble = false;
						setParentScrollAble(false);
					} else {
						setParentScrollAble(true);
						isParentScrollAble = true;
						return true;
					}
				} else {
					if (tabY <= statusBarHeight
							&& getFirstVisiblePosition() != 0) {
						isParentScrollAble = false;
						setParentScrollAble(false);
					}else {
						setParentScrollAble(true);
						isParentScrollAble = true;
						return true;
					}
				}
			}

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
			if (mEnablePullGoHome
					&& mHeaderView.getVisiableHeight() > mHeaderViewHeight * 2) {
				if (iXListViewOnGoHome != null) {

				}
			}
			setParentScrollAble(true);// 当手指松开时，让父ScrollView重新拿到onTouch权限
			break;
		default:
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh

				if (mEnablePullGoHome
						&& mHeaderView.getVisiableHeight() > mHeaderViewHeight * 2) {
					if (iXListViewOnGoHome != null) {
						mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
						iXListViewOnGoHome.onGoHome();
					}
				} else if (mEnablePullRefresh
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
		if(isParentScrollAble){
			return true;
		} else {
			return super.onTouchEvent(ev);
		}
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

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
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
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	public void setIXListViewOnScroll(IXListViewOnScroll l) {
		iXListViewOnScroll = l;
	}

	public void setIXListViewOnGoHome(IXListViewOnGoHome l) {
		iXListViewOnGoHome = l;
	}

	public void setFooterBackground(int rgb) {
		mFooterView.mContentView.setBackgroundColor(rgb);
	}

	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();

	}

	public interface IXListViewOnScroll {

		public void onScroll();
	}

	public interface IXListViewOnGoHome {

		public void onGoHome();
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
