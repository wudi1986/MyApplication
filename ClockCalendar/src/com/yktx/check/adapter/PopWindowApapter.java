package com.yktx.check.adapter;

import java.util.List;

import com.yktx.check.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PopWindowApapter extends BaseAdapter{
	private List<String> list;
	private Context mContext;
	private LayoutInflater inflater;
	

	public PopWindowApapter(List<String> list, Context mContext) {
		super();
		this.list = list;
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
	public View getView(int position, View 	convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = inflater.inflate(R.layout.popwindow_item, null);
		TextView title = (TextView) convertView.findViewById(R.id.popwindow_item_title);
		title.setText(list.get(position));
		return convertView;
	}

}
