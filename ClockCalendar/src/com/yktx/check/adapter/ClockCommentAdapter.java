package com.yktx.check.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.R;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;

public class ClockCommentAdapter extends BaseAdapter {
	private Context mContext;
	ArrayList<CommentsBean> itemBeans = new ArrayList<CommentsBean>(10);
	private LayoutInflater inflater;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.zw_touxiang)
			.showImageForEmptyUri(null).showImageOnFail(null)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
			.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(100))
			// 启用内存缓存
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	BuildingInfoOnClick buildingInfoOnClick;

	public void setBuildingInfoOnClick(BuildingInfoOnClick buildingInfoOnClick) {
		this.buildingInfoOnClick = buildingInfoOnClick;
	}

	public ClockCommentAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public void setList(ArrayList<CommentsBean> list) {
		itemBeans = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemBeans.size();
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
		HoldView holder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.comment_info_item, null);
			holder = new HoldView(convertView);
			convertView.setTag(holder);
		} else {
			holder = (HoldView) convertView.getTag();
		}
		CommentsBean bean = itemBeans.get(position);
		ShowView(bean, holder, position);
		return convertView;
	}

	SpannableString msp = null;

	public void ShowView(final CommentsBean bean, HoldView holdView, final int position) {
		imageLoader.displayImage(Tools.getImagePath(bean.getImageSource()) + bean.getAvartar_path()+(bean.getImageSource() == 2?"?imageMogr2/thumbnail/70x70":""),
				holdView.comment_info_item_headImage, headOptions);
		holdView.comment_info_item_headImage
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent in = new Intent(mContext,
								ClockOtherUserActivity.class);
						in.putExtra("userid", bean.getUserId());
						mContext.startActivity(in);
					}
				});

		holdView.comment_info_item_username.setText(bean.getName());
		String text = bean.getText();
		if (bean.getCommentType() == 1) {
			text = bean.getText();
		} else {
			text = "回复" + bean.getRepliedUserName() + "：" + bean.getText();
		}

		String time = "    " + TimeUtil.getTimes(bean.getSendTime());
		String content = ToDBC(text + time);

		msp = new SpannableString(content);
		int subscriptend = content.length();
		int subscriptstart = content.length() - time.length();
		msp.setSpan(new AbsoluteSizeSpan(12, true), subscriptstart,
				subscriptend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// 设置字体前景色
		msp.setSpan(
				new ForegroundColorSpan(mContext.getResources().getColor(
						R.color.meibao_color_11)), subscriptstart,
				subscriptend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
		// 设置字体背景色
		holdView.comment_info_item_Content.setText(msp);
		holdView.comment_info_item_Content.setMovementMethod(LinkMovementMethod
				.getInstance());
		holdView.layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(buildingInfoOnClick != null){
					buildingInfoOnClick.clickComment(position);
				}
			}
		});
		holdView.comment_info_item_Content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(buildingInfoOnClick != null){
					buildingInfoOnClick.clickComment(position);
				}
			}
		});
	}

	class HoldView {
		ImageView comment_info_item_headImage;
		TextView comment_info_item_username, comment_info_item_Content;
		RelativeLayout layout;

		public HoldView(View convertView) {
			comment_info_item_headImage = (ImageView) convertView
					.findViewById(R.id.comment_info_item_headImage);
			comment_info_item_username = (TextView) convertView
					.findViewById(R.id.comment_info_item_username);
			comment_info_item_Content = (TextView) convertView
					.findViewById(R.id.comment_info_item_Content);

			layout = (RelativeLayout) convertView.findViewById(R.id.layout);
		}
	}

	/**
	 * 全角转换为半角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	public interface BuildingInfoOnClick {
		/**
		 * 点击评论
		 * */
		public void clickComment(int position);

	}
}
