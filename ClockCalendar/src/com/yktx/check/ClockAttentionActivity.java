//package com.yktx.check;
//
//import com.yktx.check.fragment.ClockFollowFragment;
//import com.yktx.check.square.fragment.DynamicFragment;
//
//import android.os.Build;
//import android.os.Bundle;
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.support.v4.app.FragmentActivity;
//import android.view.Menu;
//import android.view.View;
//import android.view.Window;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class ClockAttentionActivity extends FragmentActivity {
//
//	private ImageView mLeftTitleImage,mRightTitleImage;
//	private TextView mContentTitle;
//	public SharedPreferences settings;
//	public Editor mEditor;
//	private Context mContext;
//	private String thisJobUserId;
//	private boolean isOther;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature( Window.FEATURE_NO_TITLE );//不显示程序的标题栏
//		setContentView(R.layout.activity_clock_attention);
//		mContext = this;
//		thisJobUserId = getIntent().getStringExtra("userid");
//		isOther = getIntent().getBooleanExtra("isother", false);
//		initView();
//		setDefaultFragment();
//	}
//	public void initView(){
//		settings = getSharedPreferences("clock", MODE_PRIVATE);
//		mEditor = settings.edit();
//		mContentTitle = (TextView) findViewById(R.id.title_item_content);
//		mLeftTitleImage = (ImageView) findViewById(R.id.title_item_leftImage);
//		mRightTitleImage = (ImageView) findViewById(R.id.title_item_rightImage);
//		mRightTitleImage.setVisibility(View.GONE);
//		if(isOther){
//			mContentTitle.setText("Ta的关注");
//		}else{
//			mContentTitle.setText("我的关注");
//		}
//		mLeftTitleImage.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				finish();
//			}
//		});
//	}
//	//
//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	@SuppressLint("NewApi")
//	private void setDefaultFragment() {
//		FragmentManager fm = getFragmentManager();
//		FragmentTransaction transaction = fm.beginTransaction();
//		ClockFollowFragment followFragment= new ClockFollowFragment(mContext, thisJobUserId, isOther);
//		transaction.replace(R.id.clock_attention_LinearLayout, followFragment);
//		transaction.commit();
//	}
//}
