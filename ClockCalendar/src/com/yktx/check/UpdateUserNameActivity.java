package com.yktx.check;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

public class UpdateUserNameActivity extends BaseActivity  implements ServiceListener{
	private ImageView title_leftImage,title_rightImage;
	private TextView title_content,title_rightText;
	private RelativeLayout titleLayout;
	private EditText update_user_name_input;
	private String oldName;

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_update_user_name);
		title_leftImage = (ImageView) findViewById(R.id.title_item_leftImage);
		title_rightImage = (ImageView) findViewById(R.id.title_item_rightImage);
		title_content = (TextView) findViewById(R.id.title_item_content);
		title_rightText = (TextView) findViewById(R.id.title_item_rightText);
		titleLayout = (RelativeLayout) findViewById(R.id.title_item_layout);
		update_user_name_input = (EditText) findViewById(R.id.update_user_name_input);
		
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		title_rightImage.setVisibility(View.GONE);
		title_content.setText("更改名字");
		title_rightText.setText("完成");
		title_rightText.setVisibility(View.VISIBLE);
		oldName = settings.getString("username", null);
		if(oldName != null){
			update_user_name_input.setText(oldName);
			update_user_name_input.setSelection(oldName.length());
		}
		
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		title_leftImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		title_rightText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String name = update_user_name_input.getText().toString();
				if(name != null){
					if(!name.equals(oldName)){
						Tools.getLog(Tools.d,"aaa", "名字可修改！");
						updateUserName(name);
						mEditor.putString("username", name);
						mEditor.commit();
					}else{
						finish();
					}
				}else{
					Toast.makeText(mContext, "名称不可为空！", Toast.LENGTH_SHORT).show();
				}
				
				
				
			}
		});
		
	}
	private void updateUserName(String name){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("userId",
					userID));
			params.add(new BasicNameValuePair("name",
					name));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		com.yktx.check.service.Service
		.getService(Contanst.HTTP_UPDATE_USERNAME, null,
				null, UpdateUserNameActivity.this)
				.addList(params).request(UrlParams.POST);
	} 

	@Override
	public void getJOSNdataSuccess(Object bean, String sccmsg, int connType) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_OK;
		msg.obj = bean;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}

	@Override
	public void getJOSNdataFail(String errcode, String errmsg, int connType) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_FAIL;
		msg.obj = errmsg;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.UPDATE_USERNAME:
					Toast.makeText(mContext, "名称修改成功", Toast.LENGTH_SHORT).show();
					finish();
					break;
				}
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

}
