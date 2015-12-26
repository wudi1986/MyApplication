package com.yktx.check.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.yktx.check.R;

public class GiveUpJobDialog extends Dialog{
	private Context mContext;
	public TextView mSuccess,mClose,mTitle,giveUpJob_dialog_content;
	public EditText mInput;
	public GiveUpJobDialog(Context context) {
		super(context,R.style.dialog);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.giveupjob_dialog);
		initView();
	}
	public void initView(){
		mTitle = (TextView) findViewById(R.id.giveUpJob_dialog_title);
		mSuccess = (TextView) findViewById(R.id.giveUpJob_dialog_success);
		mInput = (EditText) findViewById(R.id.giveUpJob_dialog_input);
		mClose = (TextView) findViewById(R.id.giveUpJob_dialog_close);
		giveUpJob_dialog_content = (TextView) findViewById(R.id.giveUpJob_dialog_content);
	}
	

}
