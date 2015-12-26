package com.yktx.check.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.BaseActivity;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.R;
import com.yktx.check.bean.TopPointUserBean;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecommendFragmentHorizontalListViewAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater inflater;
	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.zw_touxiang)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(100))
	// 启用内存缓存
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	ArrayList<TopPointUserBean> topPointUserBeans = new ArrayList<TopPointUserBean>(10);

	public ArrayList<TopPointUserBean> getTopPointUserBeans() {
		return topPointUserBeans;
	}

	public void setTopPointUserBeans(ArrayList<TopPointUserBean> topPointUserBeans) {
		this.topPointUserBeans = topPointUserBeans;
	}

	public RecommendFragmentHorizontalListViewAdapter(Context Context) {
		super();
		this.mContext = Context;
		inflater = LayoutInflater.from(Context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return topPointUserBeans.size();
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
		HolderView holderView = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.recommendfragment_listview_horizontallistview_item, null);
			holderView = new HolderView(convertView);
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
		final TopPointUserBean bean = topPointUserBeans.get(position);
		ImageLoader.getInstance().displayImage(Tools.getImagePath(bean.getImageSource())+bean.getAvartarPath()+(bean.getImageSource() == 2?"?imageMogr2/thumbnail/85x85":"")
				, holderView.recommendfragment_listview_horizontallistview_item_headImage, headOptions);
		holderView.recommendfragment_listview_horizontallistview_item_Name.setText(bean.getName());
		holderView.recommendfragment_listview_horizontallistview_item_qiqiuNum.setText(bean.getPoint());
		if(position == 0){
			holderView.recommendfragment_listview_horizontallistview_item_headImageRanking.setImageResource(R.drawable.guangchang_gc_first);
//			holderView.recommendfragment_listview_horizontallistview_item_headImageRanking.setVisibility(View.VISIBLE);
		}else if(position == 1){
			holderView.recommendfragment_listview_horizontallistview_item_headImageRanking.setImageResource(R.drawable.guangchang_gc_second);
//			holderView.recommendfragment_listview_horizontallistview_item_headImageRanking.setVisibility(View.VISIBLE);
		}else if(position == 2){
			holderView.recommendfragment_listview_horizontallistview_item_headImageRanking.setImageResource(R.drawable.guangchang_gc_third);
//			holderView.recommendfragment_listview_horizontallistview_item_headImageRanking.setVisibility(View.VISIBLE);
		}else{
			holderView.recommendfragment_listview_horizontallistview_item_headImageRanking.setImageResource(R.drawable.guangchang_gc_other);
//			holderView.recommendfragment_listview_horizontallistview_item_headImageRanking.setVisibility(View.VISIBLE);
		}
//		holderView.recommendfragment_listview_horizontallistview_Layout.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		return convertView;
	}
	
	class HolderView{
		ImageView recommendfragment_listview_horizontallistview_item_headImage,recommendfragment_listview_horizontallistview_item_headImageRanking;
		TextView recommendfragment_listview_horizontallistview_item_Name,recommendfragment_listview_horizontallistview_item_qiqiuNum;
		RelativeLayout recommendfragment_listview_horizontallistview_Layout;
		public HolderView(View convertView){
			recommendfragment_listview_horizontallistview_item_headImage = (ImageView) convertView.findViewById(R.id.recommendfragment_listview_horizontallistview_item_headImage);
			recommendfragment_listview_horizontallistview_item_headImageRanking = (ImageView) convertView.findViewById(R.id.recommendfragment_listview_horizontallistview_item_headImageRanking);
			recommendfragment_listview_horizontallistview_item_Name = (TextView) convertView.findViewById(R.id.recommendfragment_listview_horizontallistview_item_Name);
			recommendfragment_listview_horizontallistview_item_qiqiuNum = (TextView) convertView.findViewById(R.id.recommendfragment_listview_horizontallistview_item_qiqiuNum);
			recommendfragment_listview_horizontallistview_Layout = (RelativeLayout) convertView.findViewById(R.id.recommendfragment_listview_horizontallistview_Layout);
		}
	}

}
