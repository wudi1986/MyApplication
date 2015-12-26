package com.duguang.baseanimation.ui.listivew.deletelistview;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.tt.balloonperformerlibrary.Balloon;
import com.yktx.check.BaseActivity;
import com.yktx.check.R;
import com.yktx.check.util.Tools;

public class QiQiuUtils {

	FrameLayout mFrameLayout;
	Context mContext;
	private ArrayList<Balloon> mBalloons;
	int clickX;
	int clickY;
	static int imageArray[] = { R.drawable.dianzan_1, R.drawable.dianzan_2, R.drawable.dianzan_3, R.drawable.dianzan_4, R.drawable.dianzan_5 };

	public QiQiuUtils(FrameLayout mFrameLayout, Context mContext) {
		this.mFrameLayout = mFrameLayout;
		this.mContext = mContext;

		mBalloons = new ArrayList<Balloon>();
	}

	/**
	 * 释放气球
	 */
	public void startFly(int x, int y) {
		// 添加若干气球
		clickX = x;
		clickY = y;
		addContent();
	}

	Random r;

	/**
	 * 添加气球
	 */
	private void addContent() {
		// for (int i = 0; i < PreferenceHelper.balloonCount(mContext); i++) {
		if (r == null) {
			r = new Random();
		}
		int balloonNum = r.nextInt(2) + 3;
		for (int i = 0; i < balloonNum; i++) {
			final int index = i;
			new Thread(new Runnable() {

				public void run() {

					try {
						Thread.sleep((200 * index));
						Message msg = new Message();
						handler.sendMessage(msg); // 告诉主线程执行任务
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}).start();
		}
		// 执行动画

		// }
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == 1) {
				onEnd((Balloon) msg.obj);
			} else {
				Tools.getLog(Tools.i,"aaa", "handlerhandlerhandlerhandler");
				performAnim(addBallonByPosition(clickX, clickY, getRandomColor(), (int) (22 * BaseActivity.DENSITY),
						(int) (27 * BaseActivity.DENSITY)));
			}
		}
	};

	/**
	 * 从一个列表中取出一个颜色
	 * 
	 * @return
	 */
	public static int getRandomColor() {
		int size = imageArray.length;
		int index = (int) (Math.random() * size);
		return imageArray[index];
	}

	/**
	 * 随机获取气球大小
	 * 
	 * @param manager
	 * @return
	 */
	public static int[] getBallonSizeByRandom(Context context) {
		int minWidth = getMinWidth(context);
		int width = (int) (minWidth + Math.random() * (minWidth * FLOAT_W));
		int[] size = new int[2];
		size[0] = width;
		size[1] = width * 4;
		return size;
	}

	private static final float FLOAT_W = 0.3f;

	private static int getMinWidth(Context context) {
		int minWidth = (int) (BaseActivity.ScreenWidth / PreferenceHelper.balloonCount(context) * (1 + FLOAT_W));
		return minWidth;
	}

	/**
	 * 获取气球随机可能的最大高度
	 * 
	 * @return
	 */
	public static int getMaxHeight(Context context) {
		int minWidth = getMinWidth(context);
		int width = (int) (minWidth + (minWidth * FLOAT_W));
		return 4 * width;
	}

	/**
	 * 添加一个气球
	 * 
	 * @param x
	 * @param y
	 * @param drawable
	 * @param width
	 * @param height
	 */
	private Balloon addBallonByPosition(int x, int y, int drawable, int width, int height) {
		Balloon balloon = new Balloon(mContext);
		balloon.setParams(new Balloon.Builder().setDrawable(drawable).build());
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
		// 固定位置
		lp.setMargins(x, y, 0, 0);
		// 如果view没有被加入到某个父组件中，则加入WindowManager中
		if (balloon.getParent() == null) {
			mFrameLayout.addView(balloon, lp);
			mBalloons.add(balloon);
			// balloon.startFly();
			return balloon;
		}
		return null;
	}

	/**
	 * 获取随机的时间
	 */
	public static long getRandomFlyDuration(Context context) {
		long duration = PreferenceHelper.balloonCount(context);
		return (long) (duration + Math.random() * duration);
	}

	private void performAnim(final Balloon balloon) {
		int offset = getMaxHeight(mContext);
		int transY = -(BaseActivity.ScreenHeight + offset);
		AnimationSet bouncer = new AnimationSet(true);
		ScaleAnimation c = new ScaleAnimation(0, 1, 0, 1);
		c.setDuration(500L);
		AlphaAnimation a = new AlphaAnimation(1, 0);
		a.setDuration(6000L);
		TranslateAnimation b = new TranslateAnimation(0, 0, 0, transY);
		b.setDuration(8000L);

		bouncer.addAnimation(a);
		bouncer.addAnimation(b);
		bouncer.addAnimation(c);
		bouncer.setFillAfter(true);
		bouncer.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				Tools.getLog(Tools.i,"kkk", "onAnimationRepeat");
				onEnd(balloon);
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				Tools.getLog(Tools.i,"kkk", "onAnimationEnd");

				Message msg = new Message();
				msg.arg1 = 1;
				msg.obj = balloon;
				handler.sendMessage(msg);
			}
		});
		LinearInterpolator linearInterpolator = new LinearInterpolator();
		bouncer.setInterpolator(linearInterpolator);
		balloon.startAnimation(bouncer);
	}

	/**
	 * 动画结束释放资源
	 * 
	 * @param balloon
	 */
	private void onEnd(Balloon balloon) {
		if (balloon != null && balloon.getParent() != null) {
			mFrameLayout.removeView(balloon);
		}
	}

	/**
	 * 释放全部资源
	 */
	public void release() {
		for (Balloon balloon : mBalloons) {
			onEnd(balloon);
		}
		mBalloons.clear();
	}

}
