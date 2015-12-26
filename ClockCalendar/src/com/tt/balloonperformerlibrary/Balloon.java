package com.tt.balloonperformerlibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.yktx.check.BaseActivity;
import com.yktx.check.R;

/**
 * 气球view
 * 
 * @author Kyson
 */
@SuppressLint("DrawAllocation")
public class Balloon extends ImageView {
	// 默认颜色
	public static final int COLOR = R.drawable.dianzan_1;
	// 默认振幅
	public static final int AMPLITUDE = 15;
	// 默认振动时间
	public static final long DURATION = 350;
	// 气球线宽度
	public static final int LINE_WIDTH = 3;
	// 三角形的宽度占比
	private static final float TRIANGLE_FLOAT_W = 0.1F;
	// 三角形的高度占比
	private static final float TRIANGLE_FLOAT_H = 0.025F;
	// 刷新间隔
	private int REFRESH_DURATION = 35;

	private Paint mPaint;
	// 外部设置的参数
	private Params mParams;

	private int mCurrentAmplitude;

	private RectF mOval;

	private Path mPath;

	public Balloon(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Balloon(Context context) {
		super(context);
		init();
	}

	private void init() {
		mParams = new Params();
		mPaint = new Paint();
		mPaint.setStrokeWidth(LINE_WIDTH);
		mPaint.setStyle(Style.STROKE);
		mPaint.setAntiAlias(true);
		mCurrentAmplitude = mParams.amplitude;
		mOval = new RectF();
		mPath = new Path();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Rect rect = new Rect(0, 0, (int) (22 * BaseActivity.DENSITY), (int) (27 * BaseActivity.DENSITY));
		Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), mParams.color);
		Rect rect2 = new Rect(0, 0, bmp1.getWidth(), bmp1.getHeight());
		canvas.drawBitmap(bmp1, rect2, rect, mPaint);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(ViewUtil.measureSize(widthMeasureSpec, AMPLITUDE * 2), ViewUtil.measureSize(heightMeasureSpec, AMPLITUDE * 8));
	}
	
	private boolean mIsFlying = false;

	/**
	 * 气球线摆动
	 */
//	public void startFly() {
//		mIsFlying = true;
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				int t = 0;
//				while (mIsFlying) {
//					if (t <= mParams.duration) {
//						double d = Math.cos((2 * Math.PI * t) / mParams.duration);
//						mCurrentAmplitude = (int) (d * ((2 * mParams.amplitude) > getWidth() ? (getWidth() / 2) : mParams.amplitude));
//						postInvalidate();
//						t = t + REFRESH_DURATION;
//						try {
//							Thread.sleep(REFRESH_DURATION);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					} else {
//						t = 0;
//					}
//				}
//			}
//		}).start();
//	}

	public void stopFly() {
		mIsFlying = false;
	}

	public void setParams(Params p) {
		this.mParams = p;
	}

	/**
	 * 外部设置参数
	 */
	public static class Params {
		// 气球颜色
		public int color;
		// 摆动幅度
		public int amplitude;
		// 摆动一周的时间
		public long duration;

		public Params() {
			color = COLOR;
			amplitude = AMPLITUDE;
			duration = DURATION;
		}
	}

	public static class Builder {
		private Params mParams;

		public Builder() {
			mParams = new Params();
		}

		public Builder setDrawable(int color) {
			mParams.color = color;
			return this;
		}

		public Builder setAmplitude(int amplitude) {
			mParams.amplitude = amplitude;
			return this;
		}

		public Builder setDuration(long duration) {
			mParams.duration = duration;
			return this;
		}

		public Params build() {
			return mParams;
		}
	}

}
