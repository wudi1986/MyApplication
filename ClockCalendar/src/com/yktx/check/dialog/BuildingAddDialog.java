package com.yktx.check.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yktx.check.R;

public class BuildingAddDialog extends Dialog{
	private TextView mBuildingText;
	private ImageView mTakeClock,mInvite;
	private Context mContext;
	private Activity mActivity;
	public TakeClockDialog clockDialog;
	private String StrTitle;
	private LinearLayout building_dialog_OutSide;
	public BuildingAddDialog(Activity  activity,String title) {
		super(activity, R.style.NOmengceng_dialog);
		// TODO Auto-generated constructor stub
		mContext = activity;
		mActivity = activity;
		StrTitle = title;
	}
	onClickTwoButton mClickTwoButton;
	public void setOnClickTwoButton(onClickTwoButton clickTwoButton){
		mClickTwoButton = clickTwoButton;
	} 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.building_dialog);
		setOnShowListener(tener);
//		getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
		mBuildingText = (TextView) findViewById(R.id.building_dialog_Text);
		mTakeClock = (ImageView) findViewById(R.id.building_dialog_takeClock);
		mInvite = (ImageView) findViewById(R.id.building_dialog_inviteClock);
		building_dialog_OutSide = (LinearLayout) findViewById(R.id.building_dialog_OutSide);
		mBuildingText.setText(StrTitle);
		mTakeClock.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mClickTwoButton.onClickAdd();
			}
		});
		mInvite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mClickTwoButton.onClickInvite();
			}
		});
		building_dialog_OutSide.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				dismiss();
			}
		}, 5000);
	}
	OnShowListener tener = new OnShowListener() {
		@Override
		public void onShow(DialogInterface arg0) {
			// TODO Auto-generated method stub
			WindowManager windowManager = mActivity.getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.width = (int) (display.getWidth()); // 设置宽度
			getWindow().setAttributes(lp);
		}

	};
	public void setBuildText(String text){
		mBuildingText.setText(text);
	}
	public interface onClickTwoButton{
		/**
		 * 添加打卡
		 */
		public void onClickAdd();
		/**
		 * 邀请好友
		 */
		public void onClickInvite();
	}

}
