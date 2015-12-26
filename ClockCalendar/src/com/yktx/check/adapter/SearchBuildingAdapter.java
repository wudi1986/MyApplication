package com.yktx.check.adapter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yktx.check.R;
import com.yktx.check.bean.SearchBean;

public class SearchBuildingAdapter extends BaseAdapter {
	/**
	 * 用来存储图片的选中情况
	 */

	String searchStr;
	private ArrayList<SearchBean> curList = new ArrayList<SearchBean>(3);
	protected LayoutInflater mInflater;
	Activity mContext;

	public SearchBuildingAdapter(Activity context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		
	}

	public void setList(ArrayList<SearchBean> list) {
		curList.clear();
		curList.addAll(list);
	};

	public void setSearchStr(String searchStr){
		this.searchStr = searchStr;
	}
	
	@Override
	public int getCount() {
		return curList.size();
	}

	@Override
	public Object getItem(int position) {
		return curList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final HoldView viewHolder;
		final SearchBean bean = curList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.search_building_item, null);
			viewHolder = new HoldView(convertView, position);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (HoldView) convertView.getTag();
		}
		//关键字变色
		if (searchStr != null) {
			SpannableString s = new SpannableString(bean.getTitle());

			Pattern p = Pattern.compile(searchStr);

			Matcher m = p.matcher(s);

			while (m.find()) {
				int start = m.start();
				int end = m.end();
				s.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_1)), start, end,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}

			viewHolder.searchTitle.setText(s);
		} else {
			viewHolder.searchTitle.setText(bean.getTitle());
		}
		viewHolder.searchNum.setText("有"+bean.getTaskCount()+"人坚持");
		return convertView;
	}
	
	public static class HoldView {
		TextView searchTitle,searchNum;

		public HoldView(View convertView, int position) {
			searchTitle = (TextView) convertView.findViewById(R.id.searchTitle);
			searchNum = (TextView) convertView
					.findViewById(R.id.searchNum);
		}

	}

}
