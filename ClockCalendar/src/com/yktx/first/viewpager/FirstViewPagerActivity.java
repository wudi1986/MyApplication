package com.yktx.first.viewpager;

import java.util.ArrayList;
import java.util.List;

import com.yktx.check.BaseActivity;
import com.yktx.check.ClockMainActivity;
import com.yktx.check.LoginActivity;
import com.yktx.check.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FirstViewPagerActivity extends BaseActivity implements OnClickListener, OnPageChangeListener{
	
	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	/** 跳过 */
	ImageView skip;
	
	//引导图片资源
	private static final int[] pics = { R.drawable.xsyd_1,
			R.drawable.xsyd_2, R.drawable.xsyd_3,
			R.drawable.xsyd_4 };
	
	//底部小店图片
	private ImageView[] dots ;
	
	//记录当前选中位置
	private int currentIndex;
	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		  setContentView(R.layout.activity_first_pager);
	        
	        views = new ArrayList<View>();
	       
	        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
	        		LinearLayout.LayoutParams.WRAP_CONTENT);
	        
	        //初始化引导图片列表
	        for(int i=0; i<pics.length; i++) {
	        	ImageView iv = new ImageView(this);
	        	iv.setLayoutParams(mParams);
	        	iv.setImageResource(pics[i]);
	        	views.add(iv);
	        }
	        vp = (ViewPager) findViewById(R.id.viewpager);
	        skip = (ImageView) findViewById(R.id.skip);
	        //初始化Adapter
	        vpAdapter = new ViewPagerAdapter(views, FirstViewPagerActivity.this);
	        vp.setAdapter(vpAdapter);
	        //绑定回调
	        vp.setOnPageChangeListener(this);
	        
	        //初始化底部小点
	        initDots();
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		skip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent();
				if (isLogin) {
					in = new Intent(mContext, ClockMainActivity.class);
				} else {
					in = new Intent(mContext, LoginActivity.class);
				}
				mEditor.putBoolean("isFirstOpen", false);
				mEditor.commit();
				startActivity(in);
				// PendingTransition(anim_enter, anim_exit);
				finish();
				overridePendingTransition(R.anim.slide_alpha_in_right, R.anim.slide_alpha_out_left);
			}
		});
	}
    
    private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[pics.length];

		//循环取得小点图片
		for (int i = 0; i < pics.length; i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);//都设为灰色
			dots[i].setOnClickListener(this);
			dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);//设置为白色，即选中状态
    }
    
    /**
     *设置当前的引导页 
     */
    private void setCurView(int position)
    {
		if (position < 0 || position >= pics.length) {
			return;
		}

		vp.setCurrentItem(position);
    }

    /**
     *这只当前引导小点的选中 
     */
    private void setCurDot(int positon)
    {
		if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
			return;
		}

		dots[positon].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = positon;
    }

    //当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	//当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	//当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		//设置底部小点选中状态
		setCurDot(arg0);
	}

	@Override
	public void onClick(View v) {
		int position = (Integer)v.getTag();
		setCurView(position);
		setCurDot(position);
	}


}