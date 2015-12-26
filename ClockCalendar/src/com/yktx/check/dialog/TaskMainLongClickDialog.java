package com.yktx.check.dialog;

import com.yktx.check.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TaskMainLongClickDialog extends Dialog{
	private Activity mActivity;
	public ImageView taskmainlongclick_dialog_details,taskmainlongclick_dialog_setting,taskmainlongclick_dialog_delete;
	private LinearLayout taskmainlongclick_dialog_blank;
	public TaskMainLongClickDialog(Activity context) {
		super(context, R.style.dialog);
		// TODO Auto-generated constructor stub
		mActivity = context;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taskmainlongclick_dialog);
		setOnShowListener(tener);
		taskmainlongclick_dialog_details = (ImageView) findViewById(R.id.taskmainlongclick_dialog_details);
		taskmainlongclick_dialog_setting = (ImageView) findViewById(R.id.taskmainlongclick_dialog_setting);
		taskmainlongclick_dialog_delete = (ImageView) findViewById(R.id.taskmainlongclick_dialog_delete);
		
		taskmainlongclick_dialog_blank = (LinearLayout) findViewById(R.id.taskmainlongclick_dialog_blank);
		taskmainlongclick_dialog_blank.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		 windowDeploy(0, 100);
	}
	// 设置窗口显示
		private Window window = null;

		public void windowDeploy(int x, int y) {
			window = getWindow(); // 得到对话框
			window.setWindowAnimations(R.style.dialogWindowAnim); // 设置窗口弹出动画
			WindowManager.LayoutParams wl = window.getAttributes();
			// 根据x，y坐标设置窗口需要显示的位置w
			wl.x = x; // x小于0左移，大于0右移
			wl.y = y; // y小于0上移，大于0下移
			// wl.alpha = 0.6f; //设置透明度
			// wl.gravity = Gravity.BOTTOM; //设置重力
			window.setAttributes(wl);
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
}
