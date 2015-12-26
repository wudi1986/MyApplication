package com.yktx.check.adapter;

import java.util.ArrayList;

import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.ClockGroupInfoFragmentActivity;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.R;
import com.yktx.check.bean.HotTestBean;
import com.yktx.check.bean.LatestBean;
import com.yktx.check.bean.MostDatesTaskBean;
import com.yktx.check.bean.TopPointUserBean;
import com.yktx.check.listview.TwoWayView;
import com.yktx.check.listview.TwoWayView.OnScrollListener;
import com.yktx.check.util.LvHeightUtil;
import com.yktx.check.util.Tools;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RecommendFragmentAdapter extends BaseAdapter implements OnScrollListener{
	ArrayList<HotTestBean> hotList = new ArrayList<HotTestBean>(10);
	ArrayList<MostDatesTaskBean> todayBoutiqueJobList = new ArrayList<MostDatesTaskBean>(10);
	ArrayList<TopPointUserBean> topPointUserBeans = new ArrayList<TopPointUserBean>(10);
	ArrayList<LatestBean> latestBeans = new ArrayList<LatestBean>(10);
	String userID;
	private LayoutInflater inflater;
	private int thisAdapterType;
	Activity context;
	int hotListSize = 0;

	public RecommendFragmentAdapter(Activity context, String userID) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.userID = userID;
	}

	public void setHotList(ArrayList<HotTestBean> hotList) {
		this.hotList = hotList;
		if (hotList != null)
			hotListSize = hotList.size();
	}

	public void setTodayBoutiqueJobList(ArrayList<MostDatesTaskBean> list) {
		todayBoutiqueJobList = list;
	}

	public void setTopPointUserBeans(
			ArrayList<TopPointUserBean> topPointUserBeans) {
		this.topPointUserBeans = topPointUserBeans;
	}


	public void setLatestBeans(ArrayList<LatestBean> latestBeans) {
		this.latestBeans = latestBeans;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
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
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if(position == 0){
			thisAdapterType = 0;//今日牛人榜
		}else if(position == 1){
			thisAdapterType = 3;//热卡
		}else if(position == 2){
			thisAdapterType = 2;//坚持最久榜
		}else if(position == 3){
			thisAdapterType = 1;//新建的卡
		}
		return thisAdapterType;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null|| holder == null){
			convertView = inflater.inflate(R.layout.recommendfragment_listviewadapter_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}



		final int AdapterType = getItemViewType(position);
		Tools.getLog(Tools.d, "aaa", topPointUserBeans.toString());
		RecommendFragmentListViewAdapter adapter = new RecommendFragmentListViewAdapter(context, userID, AdapterType);
		if(AdapterType == 0){
//			holder.recommendfragment_listviewadapter_item_topLayoutText.setText("今天我最牛");
//			adapter.setTopPointUserBeans(topPointUserBeans);
//			adapter.notifyDataSetChanged();
			convertView = inflater.inflate(
					R.layout.recommendfragment_listview_fristitem, null);
			TwoWayView horizontalListView = (TwoWayView) convertView
					.findViewById(R.id.recommendfragment_listview_fristitem_HorizontalListView);
			RecommendFragmentHorizontalListViewAdapter fragmentHorizontalListViewAdapter = new RecommendFragmentHorizontalListViewAdapter(
					context);
			fragmentHorizontalListViewAdapter
					.setTopPointUserBeans(topPointUserBeans);
			horizontalListView.setAdapter(fragmentHorizontalListViewAdapter);
			// setting the exact state of ListView as scrolled previously
			horizontalListView.setSelectionFromOffset(indexr, offSet);
			horizontalListView
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							TopPointUserBean bean = topPointUserBeans.get(arg2);
							// Toast.makeText(context, "点击头像！！！！！！",
							// Toast.LENGTH_SHORT).show();
							if (userID.equals(bean.getUserId())) {
								Intent in = new Intent(context,
										ClockMyActivity.class);
								context.startActivity(in);
							} else {
								Intent in = new Intent(context,
										ClockOtherUserActivity.class);
								in.putExtra("userid", bean.getUserId());
								context.startActivity(in);
							}
						}
					});
			return convertView;
			
			
			
		}else if(AdapterType == 1){
			holder.recommendfragment_listviewadapter_item_topLayoutText.setText("热卡");
			holder.recommendfragment_listviewadapter_item_topLayoutImage.setImageResource(R.drawable.guangchang_gc_reka);
			adapter.setHotList(hotList);
			adapter.notifyDataSetChanged();
		}else if(AdapterType == 2){
			holder.recommendfragment_listviewadapter_item_topLayoutText.setText("坚持最久榜");
			holder.recommendfragment_listviewadapter_item_topLayoutImage.setImageResource(R.drawable.guangchang_gc_jingpin);
			adapter.setTodayBoutiqueJobList(todayBoutiqueJobList);
			adapter.notifyDataSetChanged();
		}else if(AdapterType == 3){
			holder.recommendfragment_listviewadapter_item_topLayoutText.setText("新建的卡");
			holder.recommendfragment_listviewadapter_item_topLayoutImage.setImageResource(R.drawable.guangchang_gc_chuangjian);
			adapter.setLatestBeans(latestBeans);
			adapter.notifyDataSetChanged();
		}


		holder.recommendfragment_listviewadapter_item_ListView.setAdapter(adapter);
		holder.recommendfragment_listviewadapter_item_ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(AdapterType == 0){
					TopPointUserBean topPointUserBean = topPointUserBeans.get(arg2);
					if (userID.equals(topPointUserBean.getUserId())) {
						Intent in = new Intent(context,
								ClockMyActivity.class);
						context.startActivity(in);
					} else {
						Intent in = new Intent(context,
								ClockOtherUserActivity.class);
						in.putExtra("userid", topPointUserBean.getUserId());
						context.startActivity(in);
					}
				}else if(AdapterType == 1){
					Intent in = new Intent(context, ClockGroupInfoFragmentActivity.class);
					HotTestBean hotTestBean= hotList.get(arg2);
					Tools.getLog(Tools.d, "aaa", "hotTestBean数据"+hotTestBean.toString());
					in.putExtra("buildingId",hotTestBean.getBuildingId());
					context.startActivity(in);
				}else if(AdapterType == 2){
					Intent in = new Intent(context, ClockGroupInfoFragmentActivity.class);
					MostDatesTaskBean mostDatesTaskBean= todayBoutiqueJobList.get(arg2);
					Tools.getLog(Tools.d, "aaa", "todayBoutiqueJobList数据"+mostDatesTaskBean.toString());
					in.putExtra("buildingId",mostDatesTaskBean.getBuildingId());
					context.startActivity(in);
				}else if(AdapterType == 3){
					Intent in = new Intent(context, ClockGroupInfoFragmentActivity.class);
					LatestBean latestBean= latestBeans.get(arg2);
					Tools.getLog(Tools.d, "aaa", "latestBeans数据"+latestBean.toString());
					in.putExtra("buildingId",latestBean.getBuildingId());
					context.startActivity(in);
				}
			}
		});
		LvHeightUtil.setListViewHeightBasedOnChildren(holder.
				recommendfragment_listviewadapter_item_ListView);//动态设置ListView的高度
		return convertView;
	}
	class ViewHolder{
		ImageView recommendfragment_listviewadapter_item_topLayoutImage;
		TextView recommendfragment_listviewadapter_item_topLayoutText;
		ListView recommendfragment_listviewadapter_item_ListView;
		public ViewHolder(View convertView){
			recommendfragment_listviewadapter_item_topLayoutImage = (ImageView) convertView.
					findViewById(R.id.recommendfragment_listviewadapter_item_topLayoutImage);
			recommendfragment_listviewadapter_item_topLayoutText = (TextView) convertView.
					findViewById(R.id.recommendfragment_listviewadapter_item_topLayoutText);
			recommendfragment_listviewadapter_item_ListView = (ListView) convertView.
					findViewById(R.id.recommendfragment_listviewadapter_item_ListView);
		}
	} 
	int indexr = 0;
	int offSet = 0;

	@Override
	public void onScrollStateChanged(TwoWayView view, int scrollState) {

		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
			// do nothing
		} else {
			// set the previous state of ListView as scrolled
			int index = view.getFirstVisiblePosition();
			View v = view.getChildAt(0);
			int left = (v == null) ? 0 : v.getLeft();

			indexr = index;
			offSet = left;
			// mVerticalList.get((Integer)view.getTag()).setIndex(index);
			// mVerticalList.get((Integer)view.getTag()).setOffSet(left);
		}
	}

	@Override
	public void onScroll(TwoWayView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

}
