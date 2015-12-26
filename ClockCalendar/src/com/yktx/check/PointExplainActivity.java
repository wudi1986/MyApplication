package com.yktx.check;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class PointExplainActivity extends BaseActivity {

	private int imageSource;
	private ImageView mImageView;
	int imageViewHeight;

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.point_explain_activity);
		mImageView = (ImageView) findViewById(R.id.image);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jifenshuoming);
		imageViewHeight = bitmap.getHeight()*BaseActivity.ScreenWidth/bitmap.getWidth();
		bitmap.recycle();
		bitmap = null;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(BaseActivity.ScreenWidth, imageViewHeight);
		mImageView.setLayoutParams(layoutParams);
		initTitle();
	}
	
	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub

	}

	private void initTitle(){
		ImageView title_item_leftImage = (ImageView)findViewById(R.id.title_item_leftImage);
		ImageView title_item_rightImage = (ImageView)findViewById(R.id.title_item_rightImage);
		TextView title_item_content = (TextView)findViewById(R.id.title_item_content);
		title_item_content.setText("积分说明");
		title_item_rightImage.setVisibility(View.GONE);
		title_item_leftImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
