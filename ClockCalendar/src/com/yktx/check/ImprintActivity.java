package com.yktx.check;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ImprintActivity extends BaseActivity {

	private ImageView mLeftTitleImage,mRightTitleImage;
	private TextView mContentTitle;
	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_imprint);
		mContentTitle = (TextView) findViewById(R.id.title_item_content);
		mLeftTitleImage = (ImageView) findViewById(R.id.title_item_leftImage);
		mRightTitleImage = (ImageView) findViewById(R.id.title_item_rightImage);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

		mRightTitleImage.setVisibility(View.GONE);
		mContentTitle.setText("版权说明");


	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		mLeftTitleImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}


}
