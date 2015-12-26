package com.yktx.check.dialog;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.analytics.MobclickAgent;
import com.yktx.check.DailycamShowActivity;
import com.yktx.check.R;
import com.yktx.check.bean.UserAdBean;
import com.yktx.check.util.Tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnShowListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ADDialog extends Dialog{
	private Context mContext;
	private Activity mActivity;

	public TextView ad_dialog_title,ad_dialog_content,ad_dialog_skip;

	public ImageView ad_dialog_Image;

	public RelativeLayout ad_dialog_Layout,ad_dialog_contentLayout;

	private SharedPreferences settings;
	private Editor mEditor;

	public DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageForEmptyUri(R.drawable.zw_image)
	.showImageForEmptyUri(R.drawable.zw_image)
	.showImageOnFail(R.anim.loading_image_animation)
	.showImageOnLoading(R.anim.loading_image_animation).showImageForEmptyUri(null)
	.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheOnDisk(true)
	.cacheInMemory(false)
	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
	.build();

	private UserAdBean userAdBean;
	public ADDialog(Activity context,UserAdBean adBean) {
		super(context,R.style.dialog);
		// TODO Auto-generated constructor stub
		mActivity = context;
		mContext = context;
		userAdBean = adBean;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_dialog);
		settings = mActivity.getSharedPreferences("clock", mActivity.MODE_PRIVATE);
		mEditor = settings.edit();
		ad_dialog_title = (TextView) findViewById(R.id.ad_dialog_title);
		ad_dialog_content = (TextView) findViewById(R.id.ad_dialog_content);
		ad_dialog_skip = (TextView) findViewById(R.id.ad_dialog_skip);

		ad_dialog_Image = (ImageView) findViewById(R.id.ad_dialog_Image);
		

		ad_dialog_Layout = (RelativeLayout) findViewById(R.id.ad_dialog_Layout);
		ad_dialog_contentLayout = (RelativeLayout) findViewById(R.id.ad_dialog_contentLayout);

		ad_dialog_title.setText(userAdBean.getText());
		ad_dialog_content.setText(userAdBean.getTitle());

		ImageLoader.getInstance().displayImage(Tools.getImagePath(0)+userAdBean.getImagePath()+"?imageMogr2/thumbnail/347x260"
				, ad_dialog_Image, options);

		ad_dialog_Image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(mContext, "adclik");
				mEditor.putString("adurl", userAdBean.getLink());
				mEditor.commit();
				Intent in = new Intent(mContext, DailycamShowActivity.class);
				in.putExtra("title", userAdBean.getTitle());
				in.putExtra("url", userAdBean.getLink());
				in.putExtra("imageUrl", userAdBean.getImagePath());
				mActivity.startActivity(in);
				mActivity.overridePendingTransition(R.anim.my_scale_action,
						R.anim.my_scale_action1);
				//				bannerLayout.setVisibility(View.GONE);
			}
		});
		ad_dialog_contentLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(mContext, "adclik");
				mEditor.putString("adurl", userAdBean.getLink());
				mEditor.commit();
				Intent in = new Intent(mContext, DailycamShowActivity.class);
				in.putExtra("title", userAdBean.getTitle());
				in.putExtra("url", userAdBean.getLink());
				mActivity.startActivity(in);
				mActivity.overridePendingTransition(R.anim.my_scale_action,
						R.anim.my_scale_action1);
				
				//				bannerLayout.setVisibility(View.GONE);
			}
		});
		ad_dialog_skip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(mContext, "adtiaoguoclick");
				mEditor.putString("adurl", userAdBean.getLink());
				mEditor.commit();
				dismiss();
				
			}
		}); 
		setOnShowListener(tener);
	}
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
}
