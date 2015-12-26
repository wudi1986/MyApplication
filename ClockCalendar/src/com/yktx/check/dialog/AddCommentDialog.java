package com.yktx.check.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yktx.check.ClockApplication;
import com.yktx.check.R;

public class AddCommentDialog extends Dialog{
	private Activity mActivity;
	private TextView mComfirm;
	private EditText mInput;
	private onCLickCommentSuccess cLickCommentSuccess;
	private LinearLayout addComment_dialog_OutSide;
	/**
	 * 回复人名
	 */
	private String name;
	public AddCommentDialog(Activity activity, String name) {
		super(activity,R.style.dialog);
		// TODO Auto-generated constructor stub
		mActivity = activity;
		this.name = name;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addcomment_dialog);
		initView();
	}
	public void initView(){
		//		windowDeploy(0,100);
		mInput = (EditText) findViewById(R.id.addComment_dialog_input);
		if(name != null){
			mInput.setHint("回复"+name +"：");
		} else {
			mInput.setHint("评论：");
		}
		mComfirm = (TextView) findViewById(R.id.addComment_dialog_confirm);
		addComment_dialog_OutSide = (LinearLayout) findViewById(R.id.addComment_dialog_OutSide);
		ClockApplication.openKeybord(mInput, mActivity);
		mComfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(cLickCommentSuccess != null)
					cLickCommentSuccess.onClickSuccess(mInput.getText().toString());
				ClockApplication.closeKeybord(mInput, mActivity);
				dismiss();
			}
		});
		addComment_dialog_OutSide.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ClockApplication.closeKeybord(mInput, mActivity);
				dismiss();
			}
		});
		setOnShowListener(tener);
		setOnKeyListener(keyListener);
	}
	public void setOnClickCommentSuccess(onCLickCommentSuccess cLickCommentSuccess){
		this.cLickCommentSuccess = cLickCommentSuccess;
	}
	OnKeyListener keyListener = new OnKeyListener() {

		@Override
		public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				dismiss();
				return true;
			}
			return false;
		}
	};
	//	//设置窗口显示
	//	private Window window = null;
	//	public void windowDeploy(int x, int y){
	//		window = getWindow(); //得到对话框
	//		window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
	//		WindowManager.LayoutParams wl = window.getAttributes();
	//		//根据x，y坐标设置窗口需要显示的位置w
	//		wl.x = x; //x小于0左移，大于0右移
	//		wl.y = y; //y小于0上移，大于0下移  
	//		//         wl.alpha = 0.6f; //设置透明度
	//		//         wl.gravity = Gravity.BOTTOM; //设置重力
	//		window.setAttributes(wl);
	//	}
	OnShowListener tener = new OnShowListener() {
		@Override
		public void onShow(DialogInterface arg0) {
			// TODO Auto-generated method stub
			WindowManager windowManager = mActivity.getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.width = (int)(display.getWidth()); //设置宽度
			getWindow().setAttributes(lp);
		}

	};
	public interface onCLickCommentSuccess{
		public void onClickSuccess(String content);
	}

}
