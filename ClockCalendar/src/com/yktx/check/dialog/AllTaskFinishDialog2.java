package com.yktx.check.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yktx.check.R;
import com.yktx.check.dialog.TakeClockSuccessDialog.OnCLickSuccessShare;

public class AllTaskFinishDialog2 extends Dialog {
	OnCLickSuccessShare onCLickSuccessShare;
	Context context;
	private ImageView closeDialog;
	public ImageView shareImage;
//	private ImageView sinaShare, daka_circle, daka_qq;

	public AllTaskFinishDialog2(Context context) {
		super(context, R.style.dialog);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	int index = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_task_finish_dialog2);
		windowDeploy(0, 0);
//		takecolck_successdialog_result = (ImageView) findViewById(R.id.takecolck_successdialog_result);
		shareImage = (ImageView) findViewById(R.id.shareImage);
		closeDialog = (ImageView) findViewById(R.id.closeDialog);
		closeDialog.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
//		sinaShare = (ImageView) findViewById(R.id.sinaShare);
//		daka_circle = (ImageView) findViewById(R.id.daka_circle);
//		daka_qq = (ImageView) findViewById(R.id.daka_qq);
//		sinaShare.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				index = 0;
//				if (onCLickSuccessShare != null) {
//					onCLickSuccessShare.onClickSuccess(index);
//				}
//			}
//		});
//		daka_circle.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				index = 1;
//				if (onCLickSuccessShare != null) {
//					onCLickSuccessShare.onClickSuccess(index);
//				}
//			}
//		});
//		daka_qq.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				index = 2;
//				if (onCLickSuccessShare != null) {
//					onCLickSuccessShare.onClickSuccess(index);
//				}
//			}
//		});

//		takecolck_successdialog_result
//				.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						dismiss();
//					}
//				});
		// daka_qiqiu1.setAnimation(new Animation() {
		// });
		// Animation shake = AnimationUtils.loadAnimation(context ,
		// R.anim.doudong_animation);
		// shake.reset();
		// shake.setFillAfter(true);
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

}
