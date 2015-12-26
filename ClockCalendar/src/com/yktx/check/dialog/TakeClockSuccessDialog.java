package com.yktx.check.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.ClockGroupInfoFragmentActivity;
import com.yktx.check.PointExplainActivity;
import com.yktx.check.R;

public class TakeClockSuccessDialog extends Dialog {
	int jifenIndex;
	int peopleNum;
	int checkDateCountNum;
	OnCLickSuccessShare onCLickSuccessShare;
	Context context;
	String buildingId,clockTaskName;
	private ImageView takecolck_successdialog_Image;
//	private ImageView daka_qiqiu1, daka_qiqiu2;
	private ImageView sinaShare, daka_circle, daka_qq,closeDialog;
	RelativeLayout  layout;
	TextView  jifen, userNum,taskName,checkDateCount;
	LinearLayout finishLayout;
//	/** 今日获得分数 */
//	int pointToday;
//	/** 排名 */
//	int rank;
	

	public TakeClockSuccessDialog(Context context, int jifenIndex,
			int peopleNum, String buildingId,String clockTaskName,int checkDateCountNum) {

//		  super(context, android.R.style.Theme);
			super(context, R.style.Dialog_Fullscreen);
			// TODO Auto-generated constructor stub
			this.jifenIndex = jifenIndex;
			this.context = context;
			this.peopleNum = peopleNum;
			this.buildingId = buildingId;
			this.clockTaskName = clockTaskName;
			this.checkDateCountNum = checkDateCountNum;
//			this.pointToday = pointToday;
//			this.rank = rank;
		
	}

	int index = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.takeclock_successdialog2);
		windowDeploy(0, 0);
		layout = (RelativeLayout) findViewById(R.id.layout);

//		qiqiuNum = (TextView) findViewById(R.id.qiqiuNum);
//		rankingNum = (TextView) findViewById(R.id.rankingNum);
		taskName = (TextView) findViewById(R.id.taskName);
		checkDateCount = (TextView) findViewById(R.id.checkDateCount);
		jifen = (TextView) findViewById(R.id.jifen);
		userNum = (TextView) findViewById(R.id.userNum);
		finishLayout = (LinearLayout) findViewById(R.id.finishLayout);
		takecolck_successdialog_Image = (ImageView) findViewById(R.id.takecolck_successdialog_Image);
//		daka_qiqiu1 = (ImageView) findViewById(R.id.daka_qiqiu1);
//		daka_qiqiu2 = (ImageView) findViewById(R.id.daka_qiqiu2);
		sinaShare = (ImageView) findViewById(R.id.sinaShare);
		daka_circle = (ImageView) findViewById(R.id.daka_circle);
		daka_qq = (ImageView) findViewById(R.id.daka_qq);
		closeDialog = (ImageView) findViewById(R.id.closeDialog);
		Typeface customFont = Typeface.createFromAsset(context.getAssets(), "font/Elementary_Heavy_SF_Bold.ttf");
		checkDateCount.setTypeface(customFont);

		userNum.setText(peopleNum+"");
		taskName.setText(clockTaskName);
		checkDateCount.setText(checkDateCountNum+"");
//		qiqiuNum.setText(pointToday+"");
//		rankingNum.setText(rank+"");
		
		
//		jifen.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
//		jifen.getPaint().setAntiAlias(true);// 抗锯齿
//		jifen.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		
		closeDialog.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		jifen.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(context, PointExplainActivity.class);
				context.startActivity(in);
				dismiss();
			}
		});

		finishLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(context, ClockGroupInfoFragmentActivity.class);
				in.putExtra("buildingId", buildingId);
				context.startActivity(in);
				TakeClockSuccessDialog.this.dismiss();
			}
		});

		sinaShare.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index = 0;
				if (onCLickSuccessShare != null) {
					onCLickSuccessShare.onClickSuccess(index);
				}
			}
		});
		daka_circle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index = 1;
				if (onCLickSuccessShare != null) {
					onCLickSuccessShare.onClickSuccess(index);
				}
			}
		});
		daka_qq.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index = 2;
				if (onCLickSuccessShare != null) {
					onCLickSuccessShare.onClickSuccess(index);
				}
			}
		});

		switch (jifenIndex) {
			case 0:
				takecolck_successdialog_Image
				.setImageResource(R.drawable.dh_dkcg_2fen);
				break;
			case 1:
				takecolck_successdialog_Image
						.setImageResource(R.drawable.daka_3fen);
				break;
			case 2:
				takecolck_successdialog_Image
						.setImageResource(R.drawable.daka_5fen);
				break;

		}

		layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
//		daka_qiqiu1.setAnimation(new Animation() {
//		});
		Animation shake = AnimationUtils.loadAnimation(context,
				R.anim.doudong_animation);
		shake.reset();
		shake.setFillAfter(true);
		// daka_qiqiu1.startAnimation(shake);
		// daka_qiqiu2.startAnimation(shake);

	}

	public void setOnCLickSuccessShare(OnCLickSuccessShare onCLickSuccessShare) {
		this.onCLickSuccessShare = onCLickSuccessShare;
	}

	private Window window = null;

	public void windowDeploy(int x, int y) {
		window = getWindow(); //
		window.setWindowAnimations(R.style.dialogWindowAnimSmall); //
		WindowManager.LayoutParams wl = window.getAttributes();
		window.setAttributes(wl);
	}

	android.view.View.OnClickListener click = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			// // TODO Auto-generated method stub
			// int index = -1;
			// switch(view.getId()){
			// case R.id.sinaShare:
			// index = 0;
			// break;
			//
			// case R.id.daka_circle:
			// index = 1;
			// break;
			//
			// case R.id.daka_qq:
			// index = 2;
			// break;
			//
			// }
			// if(onCLickSuccessShare != null){
			// onCLickSuccessShare.onClickSuccess(index);
			// }
		}
	};

	public interface OnCLickSuccessShare {
		public void onClickSuccess(int shareID);
	}

}
