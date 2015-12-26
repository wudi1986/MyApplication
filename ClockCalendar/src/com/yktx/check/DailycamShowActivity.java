package com.yktx.check;

import java.lang.reflect.InvocationTargetException;

import com.yktx.check.dialog.TaskInfoDialog;
import com.yktx.check.dialog.TaskInfoDialog.onCLickClockSuccess;
import com.yktx.check.util.MyUMSDK;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class DailycamShowActivity extends BaseActivity {
	ImageView leftTitleImage,rightTitleImage;
	TextView ContentTitleText;
	String title,thisUrl,imageUrl;
	WebView activity_dailycam_show_webView;
	private MyUMSDK myShare;

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_dailycam_show);
		myShare = new MyUMSDK(mContext);
		leftTitleImage = (ImageView) findViewById(R.id.title_item_leftImage);
		rightTitleImage = (ImageView) findViewById(R.id.title_item_rightImage);
		ContentTitleText = (TextView) findViewById(R.id.title_item_content);
		title = getIntent().getStringExtra("title");
		thisUrl = getIntent().getStringExtra("url");
		imageUrl = getIntent().getStringExtra("imageUrl");
		activity_dailycam_show_webView = (WebView) findViewById(R.id.activity_dailycam_show_webView);


	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		//		activity_dailycam_show_webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		rightTitleImage.setImageResource(R.drawable.xq_share);
		ContentTitleText.setText(title);
		leftTitleImage.setImageResource(R.drawable.close);
		//		WebSettings settings = activity_dailycam_show_webView.getSettings();  
		////		settings.setPluginState(PluginState.ON);
		//		settings.setLoadWithOverviewMode(true);
		//		settings.setUseWideViewPort(true);
		//		//设置WebView属性，能够执行Javascript脚本  
		//		settings.setJavaScriptEnabled(true);  
		setWebView();
		activity_dailycam_show_webView.loadUrl(thisUrl);

	}
	public void setWebView(){
		WebSettings ws = activity_dailycam_show_webView.getSettings();
		/**
		 * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
		 * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
		 * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
		 * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
		 * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
		 * setSupportZoom 设置是否支持变焦
		 * */
		ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
		ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
		ws.setUseWideViewPort(true);// 可任意比例缩放
		// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
		ws.setLoadWithOverviewMode(true);
		//	        ws.setSavePassword(true);
		//	        ws.setSaveFormData(true);// 保存表单数据
		ws.setJavaScriptEnabled(true);
		//	        ws.setGeolocationEnabled(true);// 启用地理定位
		//	        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
		ws.setDomStorageEnabled(true);
		//	        xwebchromeclient = new xWebChromeClient();
		//	        videowebview.setWebChromeClient(xwebchromeclient);
		//	        videowebview.setWebViewClient(new xWebViewClientent());
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			activity_dailycam_show_webView.getClass().getMethod("onResume").invoke(activity_dailycam_show_webView,(Object[])null);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			activity_dailycam_show_webView.getClass().getMethod("onPause").invoke(activity_dailycam_show_webView,(Object[])null);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		rightTitleImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				myShare.friendsterUMShared1("打卡7", title,
						thisUrl, imageUrl, false,5);
				
			}
		});

		leftTitleImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.my_scale_action1,
						R.anim.my_scale_action);
			}
		});
		activity_dailycam_show_webView.setWebChromeClient(new WebChromeClient() {//判断页面加载过程
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				if (newProgress == 100) {
					// 网页加载完成

				} else {
					// 加载中

				}
			}
		});
		activity_dailycam_show_webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{ //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});
	}
	//改写物理按键——返回的逻辑
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(activity_dailycam_show_webView.canGoBack())
			{
				activity_dailycam_show_webView.goBack();//返回上一页面
				return true;
			}
			else
			{
				//                System.exit(0);//退出程序
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
