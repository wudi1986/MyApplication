/**
 * 
 */
package com.yktx.check;




import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

/**  
 * @author 作者 :  wudi
 * @version 创建时间：2014年5月21日 下午6:56:13  
 * 类说明  */
/**
 * @author Administrator
 * 
 */
public class XieYiActivity extends BaseActivity {
	TextView back;

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.xieyi_activity);
		 back = (TextView)findViewById(R.id.xieyi_back);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				XieYiActivity.this.finish();
			}
		});
	}
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("SplashScreen"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	    MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}

}
