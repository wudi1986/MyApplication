package com.yktx.check.dialog;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.gallety.PhotoActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.analytics.MobclickAgent;
import com.yktx.check.ClockApplication;
import com.yktx.check.ClockMainActivity.OnSetDialogImage;
import com.yktx.check.R;
import com.yktx.check.adapter.TakeClockAdapter;
import com.yktx.check.bean.ImageListBean;
import com.yktx.check.dialog.TakeClockDialog.TaskClockDialogOnCLickClockSuccess;
import com.yktx.check.listview.HorizontalListView;
import com.yktx.check.util.FileURl;
import com.yktx.check.util.Tools;

public class TaskInfoDialog extends Dialog {
	private Context mContext;
	private Activity mActivity;
	private ImageView mTakeClockSuccess;
	private EditText mInputContent, mInputNum;
	public String cameraPhotoName;
	// .displayer(new RoundedBitmapDisplayer(400))
	public File cameraFile;
	private LinearLayout mOutSide;

	private ImageView sinaShare, friendCircleShare, qqZoneShare, inviteTask,
			setTask, deleteTask;
	private LinearLayout taskinfo_dialog_bottomLayout;
	public boolean isWeiboSY = true;
	private boolean isAloneItem;
	int index = 0;

	public TaskInfoDialog(Activity activity,boolean isAlone) {
		super(activity, R.style.dialog);
		// TODO Auto-generated constructor stub
		mContext = activity;
		mActivity = activity;
		isAloneItem = isAlone;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taskinfo_dialog);
		initView();
	}

	public void initView() {
		setOnShowListener(tener);
		setOnDismissListener(dismissListener);
		windowDeploy(0, 100);
		mOutSide = (LinearLayout) findViewById(R.id.takephoto_dialog_outSide);
		sinaShare = (ImageView) findViewById(R.id.sinaShare);
		friendCircleShare = (ImageView) findViewById(R.id.friendCircleShare);
		qqZoneShare = (ImageView) findViewById(R.id.qqZoneShare);
		inviteTask = (ImageView) findViewById(R.id.inviteTask);
		setTask = (ImageView) findViewById(R.id.setTask);
		deleteTask = (ImageView) findViewById(R.id.deleteTask);
		taskinfo_dialog_bottomLayout = (LinearLayout) findViewById(R.id.taskinfo_dialog_bottomLayout);

		sinaShare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index = 0;
				if(mCLickClockSuccess != null){
					mCLickClockSuccess.onClickSuccess(index);
				}
			}
		});
		friendCircleShare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index = 1;
				if(mCLickClockSuccess != null){
					mCLickClockSuccess.onClickSuccess(index);
				}
			}
		});
		qqZoneShare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index = 2;
				if(mCLickClockSuccess != null){
					mCLickClockSuccess.onClickSuccess(index);
				}
			}
		});
		inviteTask.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index = 3;
				if(mCLickClockSuccess != null){
					mCLickClockSuccess.onClickSuccess(index);
				}
			}
		});
		setTask.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index = 4;
				if(mCLickClockSuccess != null){
					mCLickClockSuccess.onClickSuccess(index);
				}
			}
		});
		deleteTask.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index = 5;
				if(mCLickClockSuccess != null){
					mCLickClockSuccess.onClickSuccess(index);
				}
			}
		});
		
		
		if(isAloneItem){
			taskinfo_dialog_bottomLayout.setVisibility(View.GONE);
		}else{
			taskinfo_dialog_bottomLayout.setVisibility(View.VISIBLE);
		}

		mOutSide.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(mContext, "clickblank");
				dismiss();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Tools.getLog(Tools.d, "aaa", "dialog的back");
			MobclickAgent.onEvent(mContext, "clickback");
			dismiss();
		}
		return super.onKeyDown(keyCode, event);
	}

	public int canshownum;
	OnSetDialogImage setDialogImage = new OnSetDialogImage() {

		@Override
		public void setImage(ArrayList<ImageListBean> beans,
				boolean isTakePicture) {
			// TODO Auto-generated method stub
			Tools.getLog(Tools.d, "aaa", beans.toString());
			Tools.getLog(Tools.d, "aaa", "beans" + beans.toString());
		}
	};

	OnDismissListener dismissListener = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface arg0) {
			// TODO Auto-generated method stub
			// if (filenames != null) {
			// for (int i = 0; i < filenames.size(); i++) {
			// filenames.get(i).setCheck(false);
			// }
			// }
		}
	};
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

	/** 获取聊天发送图片最后一张图 */

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Tools.getLog(Tools.d, "aaa", "查看相册！");
			ArrayList<ImageListBean> mPhotoList = (ArrayList<ImageListBean>) msg.obj;
			Tools.getLog(Tools.d, "aaa", "mPhotoList的数量：" + mPhotoList.size());
		}
	};


//	@Override
//	public void onClick(View view) {
//		// TODO Auto-generated method stub
//		int index = 0;
//		switch (view.getId()) {
//			case R.id.sinaShare:
//				index = 0;
//				break;
//			case R.id.friendCircleShare:
//				index = 1;
//				break;
//			case R.id.qqZoneShare:
//				index = 2;
//				break;
//			case R.id.inviteTask:
//				index = 3;
//				break;
//			case R.id.setTask:
//				index = 4;
//				break;
//			case R.id.deleteTask:
//				index = 5;
//				break;
//			}
//		if(mCLickClockSuccess != null){
//			mCLickClockSuccess.onClickSuccess(index);
//		}
//	}
	
	private onCLickClockSuccess mCLickClockSuccess;

	public void setOnClickClockSuccess(onCLickClockSuccess cLickClockSuccess) {
		mCLickClockSuccess = cLickClockSuccess;
	}

	public interface onCLickClockSuccess {
		public void onClickSuccess(int index);
	}

}
