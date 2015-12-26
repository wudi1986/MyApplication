package com.yktx.check.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.R;
import com.yktx.check.bean.RecommendBean;

public class ClockNewBuildingAdapter extends BaseAdapter{
	private List<RecommendBean> beans = new ArrayList<RecommendBean>();
	private Context mContext;
	private LayoutInflater inflater;
	private addBuilding building;
	private OnClickItem onClickItem;
	public ClockNewBuildingAdapter(Context context){
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		
	}
	public void setAddBuilding(addBuilding building){
		this.building = building;
	}
	
	
	public void setOnClickItem(OnClickItem onClickItem){
		this.onClickItem = onClickItem;
	}
	
	
	public void setBeans(List<RecommendBean> beans2){
		beans = beans2;
		notifyDataSetChanged();
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beans.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		convertView = inflater.inflate(R.layout.clock_newbuilding_listview_item, null);
		holder = (ViewHolder) convertView.getTag();
		if(holder == null){
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		showView(position,holder);
		return convertView;
	}
	public void showView(int position,ViewHolder holder){
		final RecommendBean recommendBean = beans.get(position);
		holder.new_listview_item_Title.setText(recommendBean.getTitle());
//		holder.new_listview_item_Content.setText("有"+recommendBean.getAttCount()+"在坚持");
		String content = recommendBean.getAttCount()+"人  "+recommendBean.getDescription();
		holder.new_listview_item_Content.setText(content);
		holder.new_listview_item_AddContent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				building.add(recommendBean);
			}
		});
//		holder.new_listview_item_Content.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		holder.layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(onClickItem != null){
					onClickItem.click(recommendBean.getId());
				}
			}
		});
	}
	class ViewHolder{
		private TextView new_listview_item_Title,new_listview_item_Content;
		private ImageView new_listview_item_AddContent;
		private RelativeLayout layout ;
		public ViewHolder(View v){
			new_listview_item_Title = (TextView) v.findViewById(R.id.new_listview_item_Title);
			new_listview_item_Content = (TextView) v.findViewById(R.id.new_listview_item_Content);
			new_listview_item_AddContent = (ImageView) v.findViewById(R.id.new_listview_item_AddContent);
			layout =  (RelativeLayout) v.findViewById(R.id.layout);
		}
		
	}
	public interface addBuilding{
		public void add(RecommendBean recommendBean);
	};
	
	public interface OnClickItem{
		public void click(String Id);
	};

}
