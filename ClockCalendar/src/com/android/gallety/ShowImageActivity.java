package com.android.gallety;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yktx.check.BaseActivity;
import com.yktx.check.R;
import com.yktx.check.util.FileURl;


public class ShowImageActivity extends BaseActivity {
	private GridView mGridView;
	private List<String> list;
	private ChildAdapter adapter;
	public static final String SELECTIAMGE = "selectimage";

	private ImageView back;
	private ImageView finish;
	TextView complete;


	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem complete = menu.add("完成");
		complete.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// switch (item.getTitle().toString()){
		// case "完成":

		ArrayList<String> listreturn = new ArrayList<String>();
		ArrayList<Integer> integers = (ArrayList<Integer>) adapter
				.getSelectItems();
		for (Integer postion : integers) {
			listreturn.add((String) adapter.getItem(postion));
		}
		Intent intentdata = new Intent();
		intentdata.putExtra(SELECTIAMGE, (java.io.Serializable) listreturn);
		setResult(222, intentdata);
		finish();
		// break;
		// }
		return super.onOptionsItemSelected(item);
	}
	//	private MenuItem successMenu;
	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		getMenuInflater().inflate(R.menu.people_mainactivity, menu);
	//		successMenu = menu.findItem(R.id.action_contact);
	//		successMenu.setTitle("完成");
	//		return super.onCreateOptionsMenu(menu);
	//	}

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.show_image_activity);
		//		getActionBar().setDisplayHomeAsUpEnabled(true);
		//	    getActionBar().setDisplayShowHomeEnabled(false); 
		mGridView = (GridView) findViewById(R.id.child_grid);
		list = getIntent().getStringArrayListExtra("data");


		//
		//		back = (ImageView) findViewById(R.id.back);
		//		back.setOnClickListener(new View.OnClickListener() {
		//
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				ShowImageActivity.this.finish();
		//			}
		//		});
		complete = (TextView)findViewById(R.id.delete);

		finish = (ImageView) findViewById(R.id.back);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		adapter = new ChildAdapter(this, list, mGridView);
		mGridView.setAdapter(adapter);
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		complete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ArrayList<String> listreturn = new ArrayList<String>();
				ArrayList<Integer> integers = (ArrayList<Integer>) adapter
						.getSelectItems();
				for (Integer postion : integers) {
					listreturn.add((String) adapter.getItem(postion));
				}
				Intent intentdata = new Intent();
				intentdata.putExtra(SELECTIAMGE, (java.io.Serializable) listreturn);
				setResult(222, intentdata);
				ShowImageActivity.this.finish();
			}
		});
		finish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//		ArrayList<String> listreturn = new ArrayList<String>();
				//		ArrayList<Integer> integers = (ArrayList<Integer>) adapter
				//				.getSelectItems();
				//		for (Integer postion : integers) {
				//			listreturn.add((String) adapter.getItem(postion));
				//		}
				//		Intent intentdata = new Intent();
				//		intentdata.putExtra(SELECTIAMGE, (java.io.Serializable) listreturn);
				//		setResult(222, intentdata);
				finish();
			}
		});
	}

	//	@Override
	//	public boolean onOptionsItemSelected(MenuItem item) {
	//		// switch (item.getTitle().toString()){
	//		// case "完成":
	//		int id = item.getItemId();
	//		switch (id) {
	//		case android.R.id.home:
	//			this.finish();
	//			break;
	//		case R.id.action_contact:
	//			ArrayList<String> listreturn = new ArrayList<String>();
	//			ArrayList<Integer> integers = (ArrayList<Integer>) adapter
	//					.getSelectItems();
	//			for (Integer postion : integers) {
	//				listreturn.add((String) adapter.getItem(postion));
	//			}
	//			Intent intentdata = new Intent();
	//			intentdata.putExtra(SELECTIAMGE, (java.io.Serializable) listreturn);
	//			setResult(222, intentdata);
	//			finish();
	//			break;
	//		}

	//		ArrayList<String> listreturn = new ArrayList<String>();
	//		ArrayList<Integer> integers = (ArrayList<Integer>) adapter
	//				.getSelectItems();
	//		for (Integer postion : integers) {
	//			listreturn.add((String) adapter.getItem(postion));
	//		}
	//		Intent intentdata = new Intent();
	//		intentdata.putExtra(SELECTIAMGE, (java.io.Serializable) listreturn);
	//		setResult(222, intentdata);
	//		finish();
	//		// break;
	//		// }
	//		return super.onOptionsItemSelected(item);
	//	}

}
