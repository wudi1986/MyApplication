package com.yktx.check.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMSocialService;
import com.yktx.check.BaseActivity;
import com.yktx.check.R;
import com.yktx.check.util.MyUMSDK;

public class AllTaskFinishDialog extends AlertDialog {
	// private LinearLayout marginLayout;
	private Context mContext;

	// final String StrMycontent = "我发布一个不错的闲置物品正在出售，也许是你打算买的哦，快进来看看吧~";
	public AllTaskFinishDialog(Activity context) {
		super(context, R.style.dialog);
		// TODO Auto-generated constructor stub
		mContext = context;

	}

	private ImageView allTaskFinishButton;
	private Window window = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_task_finish_dialog);
		// marginLayout = (LinearLayout) findViewById(R.id.share_margin);
		allTaskFinishButton = (ImageView) findViewById(R.id.allTaskFinishButton);

		// marginLayout.setOnClickListener(listener);
		allTaskFinishButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		windowDeploy(0, (int) (-50 * BaseActivity.DENSITY));
	}

//	View.OnClickListener listener = new View.OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			int id = v.getId();
//			switch (id) {
//			case R.id.allTaskFinishButton:
//				dismiss();
//				break;
//			}
//		}
//	};

	// 设置窗口显示

	public void windowDeploy(int x, int y) {

		window = getWindow(); // 得到对话框

		window.setWindowAnimations(R.style.dialogWindowAnimSmall); // 设置窗口弹出动画
		WindowManager.LayoutParams wl = window.getAttributes();
		// 根据x，y坐标设置窗口需要显示的位置
		// wl.x = x; //x小于0左移，大于0右移
		wl.y = y; // y小于0上移，大于0下移
		// wl.alpha = 0.6f; //设置透明度
		// wl.gravity = Gravity.BOTTOM; //设置重力
		window.setAttributes(wl);

	}
}
