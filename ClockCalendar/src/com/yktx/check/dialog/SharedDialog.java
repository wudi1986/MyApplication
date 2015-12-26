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
import com.yktx.check.R;
import com.yktx.check.util.MyUMSDK;


public class SharedDialog extends AlertDialog{
	private LinearLayout marginLayout;
	private Context mContext;
	private String id,phone;
	final String appID = "wx290ed9227b7e261c";
	final String appSecret  = "3fcbe2d594089668466342c06687f647";
	final String QQID = "1101696438";
	final String QQappSecret  = "pADnTXUzOV8LU2Qk";
	String Strtitle = "打卡7";
	String Strcontent = "优化生活，就从每日打卡开始";
//	final String StrMycontent = "我发布一个不错的闲置物品正在出售，也许是你打算买的哦，快进来看看吧~";
	private String mImageUrl = null;
	boolean isInvite;
	private TextView title;
	private Activity mActivity;
	public UMSocialService mController;
	private MyUMSDK myShare;
	private String headline;
	private boolean isShareImage;
	Bitmap imageBitmap;
	private String jobid;
	final String contentUrl = "http://123.57.5.108:8087/architect/share?jobId=";
	StringBuffer sb;
	public SharedDialog(Activity context, String jobId,
			Bitmap imageBitmaps,String Signature,String Quantity,String Title
			,String CurrentStreak) {
		super(context,R.style.dialog);
		// TODO Auto-generated constructor stub
		mActivity = context;
		mContext = context;
		jobid = jobId;
		imageBitmap = imageBitmaps;
		sb = new StringBuffer();
//		boolean isContentHave = false;
		sb.append("#"+Title+"#");
		
		if(Signature!= null && Signature.length()!=0){
			sb.append(Signature);
//			isContentHave = true;
		}
		if(Quantity!= null && Quantity.length()!=0){
//			if(isContentHave){
//				sb.append("&");
//			}
			sb.append("【"+Quantity+"】");
			
		}
		sb.append("，已坚持"+CurrentStreak+"天。");
		sb.append(" #打卡7#");
		
//		if (imageBitmap != null) {
//			isShareImage = true;
//		}
	}

	private ImageView qq,weixin,friendster,Qzero,Sina;
	private TextView cancle;	
	private Window window = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_type_new); 
		MobclickAgent.onEvent(mContext, "fenxiang");
		marginLayout = (LinearLayout) findViewById(R.id.share_margin);
		qq = (ImageView) findViewById(R.id.share_type_new_qq);
		weixin = (ImageView) findViewById(R.id.share_type_new_weixin);
		friendster = (ImageView) findViewById(R.id.share_type_new_friendster);
		Qzero = (ImageView) findViewById(R.id.share_type_new_qzeon);
		Sina = (ImageView) findViewById(R.id.share_type_new_sina);
		cancle = (TextView) findViewById(R.id.share_type_new_cancel);
		title = (TextView) findViewById(R.id.share_type_new__title);
		
		if(isInvite){
			title.setText(headline);
		}

		marginLayout.setOnClickListener(listener);
		qq.setOnClickListener(listener);
		weixin.setOnClickListener(listener);
		friendster.setOnClickListener(listener);
		Qzero.setOnClickListener(listener);
		cancle.setOnClickListener(listener);
		Sina.setOnClickListener(listener);
		myShare = new MyUMSDK(mContext); 
		setOnShowListener(tener);
		windowDeploy(0,100);
	}

	View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
//			switch (id) {
//			case R.id.share_type_new_qq:
//				myShare.qqUMShared( mActivity,Strcontent, Strtitle, contentUrl+jobid,
//						imageBitmap,isShareImage);
//				dismiss();
//				break;
//			case R.id.share_type_new_qzeon:
//				myShare.qzeroUMShared(mActivity, Strcontent, Strtitle, contentUrl+jobid,
//						imageBitmap);
//				dismiss();
//				break;
//			case R.id.share_type_new_weixin:
//				myShare.weixinUMShared(Strcontent, Strtitle, contentUrl+jobid,
//						imageBitmap,isShareImage);
//				dismiss();
//				break;
//			case R.id.share_type_new_friendster:
//				myShare.friendsterUMShared( Strtitle,Strcontent, contentUrl+jobid,
//						imageBitmap,isShareImage);
//				dismiss();
//				break;
//
//			case R.id.share_type_new_cancel:
//				MobclickAgent.onEvent(mContext, "quxiao");
//				SharedDialog.this.dismiss();
//				break;
//			case R.id.share_margin:
//				dismiss();
//				break;
//			case R.id.share_type_new_sina:
//				myShare.sinaUMShared(sb.toString(),contentUrl+jobid,imageBitmap,true);
//				dismiss();
//				break;
//			}
		}
	};
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

	//设置窗口显示

	public void windowDeploy(int x, int y){

		window = getWindow(); //得到对话框

		window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
		WindowManager.LayoutParams wl = window.getAttributes();
		//根据x，y坐标设置窗口需要显示的位置
		wl.x = x; //x小于0左移，大于0右移
		wl.y = y; //y小于0上移，大于0下移  
		//         wl.alpha = 0.6f; //设置透明度
		//         wl.gravity = Gravity.BOTTOM; //设置重力
		window.setAttributes(wl);

	}
}
