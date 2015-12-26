package com.yktx.check;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yktx.check.bean.ByDateBean;
import com.yktx.check.listview.DragView;
import com.yktx.check.util.Tools;


public class SetTaskSortActivity extends BaseActivity{
	
	private DragListAdapter adapter = null;
	static ArrayList<ByDateBean> list;

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_task_sort);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
		initTitle();
		list = (ArrayList<ByDateBean>) getIntent().getSerializableExtra("LIST");
		DragView dragView = (DragView) this.findViewById(R.id.dragView);
		adapter = new DragListAdapter(this, list);
		dragView.setAdapter(adapter);
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		
	}
	private void initTitle(){
		ImageView title_item_leftImage = (ImageView)findViewById(R.id.title_item_leftImage);
		TextView title_item_content = (TextView)findViewById(R.id.title_item_content);
		ImageView title_item_rightImage = (ImageView)findViewById(R.id.title_item_rightImage);
		TextView title_item_rightText = (TextView)findViewById(R.id.title_item_rightText);
		TextView title_item_leftText = (TextView)findViewById(R.id.title_item_leftText);
		title_item_leftImage.setVisibility(View.VISIBLE);
		title_item_rightImage.setVisibility(View.GONE);
		title_item_rightText.setVisibility(View.VISIBLE);
		title_item_leftText.setVisibility(View.GONE);
		title_item_content.setVisibility(View.GONE);
		title_item_rightText.setText("完成");

		title_item_leftImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SetTaskSortActivity.this.finish();

	           
			}
		});
		title_item_rightText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	            Intent intent=new Intent();
	            intent.putExtra("LIST", (java.io.Serializable) list);
	            setResult(RESULT_OK, intent);
				SetTaskSortActivity.this.finish();
	           
			}
		});
		
	}
	
	//渲染不同的view
	public class DragListAdapter extends ArrayAdapter<ByDateBean>{
		public DragListAdapter(Context context,ArrayList<ByDateBean> objects) {
			super(context, 0, objects);
		}
		public ArrayList<ByDateBean> getList(){
			return list;
		}
		
		//利用模板布局不同的listview
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			view = LayoutInflater.from(getContext()).inflate(R.layout.textandimage, null);
			TextView textView = (TextView) view.findViewById(R.id.headtext);
			textView.setText(list.get(position).getTitle());
			Tools.getLog(Tools.i, "aaa", "list.get(position).getTitle() =========== "+list.get(position).getTitle());
			return view;
		}
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
