package com.yktx.check.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.BaseActivity;
import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.ClockGroupInfoFragmentActivity;
import com.yktx.check.PointExplainActivity;
import com.yktx.check.R;
import com.yktx.check.bean.FansItemBean;
import com.yktx.check.bean.MedalBean;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;

public class FansFragmentAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<FansItemBean> fansItemBeans = new ArrayList<FansItemBean>(10);
	private LayoutInflater inflater;

	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.zw_touxiang)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(100))
	// 启用内存缓存
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	public DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.zw_image).showImageForEmptyUri(null)
	.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheOnDisk(true)
	// .displayer(new RoundedBitmapDisplayer(150))
	// .displayer(new RoundedBitmapDisplayer(6))
	// .cacheInMemory(false)
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();


	private boolean isFansList;
	setFansTypeLisitener fansTypeLisitener;
	OnFansFragmentItemClick onFansFragmentItemClick;
	public void setfansType(setFansTypeLisitener lisitener){
		fansTypeLisitener = lisitener;
	}
	public void setOnFansFragmentItemClick(OnFansFragmentItemClick onFansFragmentItemClick){
		this.onFansFragmentItemClick = onFansFragmentItemClick;
	}
	public FansFragmentAdapter(Context context,boolean bl) {
		super();
		this.mContext = context;
		isFansList = bl;
		inflater = LayoutInflater.from(context);
	}

	public ArrayList<FansItemBean> getList() {
		return fansItemBeans;
	}

	public void setList(ArrayList<FansItemBean> fansItemBeans) {
		this.fansItemBeans = fansItemBeans;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fansItemBeans.size();
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
		HolderView holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.fansfragment_listview_item, null);
			holder = new HolderView(convertView);
			convertView.setTag(holder);

		}else{
			holder = (HolderView) convertView.getTag();
		}
		FansItemBean bean = fansItemBeans.get(position);
		showView(position, bean, holder);
		return convertView;
	}
	public void showView(final int position,final FansItemBean itemBean,final HolderView holder){
		ImageLoader.getInstance().displayImage(Tools.getImagePath(itemBean.getImageSource())+ itemBean.getAvartarPath()
				+(itemBean.getImageSource() == 2?"?imageMogr2/thumbnail/55x55":""),
				holder.fansfragment_listview_item_headImage, headOptions);
		if(isFansList){
			holder.fansfragment_listview_item_name.setTextSize(14);//和关注一样，可能会有变动
			holder.fansfragment_listview_item_name.setTextColor(mContext.getResources().getColor(R.color.meibao_color_9));
			holder.fansfragment_listview_item_timeOrtaskname.setTextSize(12);
			holder.fansfragment_listview_item_timeOrtaskname.setTextColor(mContext.getResources().getColor(R.color.meibao_color_14));
		}
		holder.fansfragment_listview_item_name.setText(itemBean.getUserName());
		holder.fansfragment_listview_item_point.setText(itemBean.getPoint());
		String taskTitle = "#"+itemBean.getTitle()+"#";
		String timeOrtaskname = TimeUtil.getTimes(itemBean.getCheckTime())+"  打卡"+taskTitle;
		holder.fansfragment_listview_item_timeOrtaskname.setText("");
		SpannableString msp = new SpannableString(timeOrtaskname);

		msp.setSpan(new ClickableSpan() {
			@Override
			public void updateDrawState(TextPaint ds) {
				// TODO Auto-generated method stub
				super.updateDrawState(ds);
				//				ds.setColor(mContext.getResources().getColor(R.color.meibao_color_1));//设置文件颜色
				ds.setUnderlineText(false);      //设置下划线
				//				if(isFansList){
				ds.setColor(mContext.getResources().getColor(R.color.meibao_color_1));
				ds.setUnderlineText(false);      //设置下划线
				//				}else{
				//					ds.setColor(mContext.getResources().getColor(R.color.meibao_color_9));
				//					ds.setUnderlineText(false); 
				//				}
			}
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//					GroupMainFragmentActivity.isReflush = true;//让主activity 不刷新fragment
				//				if(userID.equals(bean.getUserId())){
				//					Intent in = new Intent(mContext,
				//							ClockMyActivity.class);
				//					mContext.startActivity(in);
				//				}else{
				//					Intent in = new Intent(mContext, ClockOtherUserActivity.class);
				//					in.putExtra("userid", bean.getUserId());
				//					mContext.startActivity(in);
				//				}
				Intent in = new Intent(mContext,
						ClockGroupInfoFragmentActivity.class);
				in.putExtra("buildingId", itemBean.getBuildingId());
				mContext.startActivity(in);

			}
		},timeOrtaskname.length()-taskTitle.length() ,timeOrtaskname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		holder.fansfragment_listview_item_timeOrtaskname.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
		holder.fansfragment_listview_item_timeOrtaskname.append(msp);
		holder.fansfragment_listview_item_timeOrtaskname.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件

		int relation = itemBean.getRelation();
		boolean isFans = false;
		if(relation == -1){
			holder.fansfragment_listview_item_fanstypeLayout.setVisibility(View.GONE);

		}else if(relation == 0 ||relation == 1){
			holder.fansfragment_listview_item_fanstypeLayout.setVisibility(View.VISIBLE);
			holder.fansfragment_listview_item_fanstypeImage.setImageResource(R.drawable.geren_guanzhu_weiguan);
			holder.fansfragment_listview_item_fanstypeText.setTextColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.fansfragment_listview_item_fanstypeText.setText("关注");
			isFans = false;

		}else if(relation == 2 || relation == 3){
			holder.fansfragment_listview_item_fanstypeLayout.setVisibility(View.VISIBLE);
			holder.fansfragment_listview_item_fanstypeImage.setImageResource(R.drawable.geren_guanzhu_yiguan);
			holder.fansfragment_listview_item_fanstypeText.setTextColor(mContext.getResources().getColor(R.color.meibao_color_11));
			holder.fansfragment_listview_item_fanstypeText.setText("已关注");
			isFans = true;
		}
		final boolean bl = isFans;
		holder.fansfragment_listview_item_fanstypeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Tools.getLog(Tools.d, "aaa", "adapterguanzhu:"+bl);
				fansTypeLisitener.setFansType(position, bl);
			}
		});
		//		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		//        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		//        holder.fansfragment_listview_item_medalLayout.measure(w, h);
		//        int width =holder.fansfragment_listview_item_medalLayout.getMeasuredWidth();
		//        int height =holder.fansfragment_listview_item_medalLayout.getMeasuredHeight();
		//        Tools.getLog(Tools.d, "aaa", "width:"+width);
		ViewTreeObserver vto = holder.fansfragment_listview_item_medalLayout.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				if(itemBean.getMedalwidth() == 0){
					holder.fansfragment_listview_item_medalLayout.removeAllViews();
					showMedalLayout(holder,itemBean,position);
				}


				return;
			}
		});
		holder.fansfragment_listview_item_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(onFansFragmentItemClick != null){
					onFansFragmentItemClick.itemClick(position);
				}
			}
		});
		holder.fansfragment_listview_item_medalLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, PointExplainActivity.class);
				mContext.startActivity(in);	
			}
		});

	}
	public void showMedalLayout(HolderView holder,final FansItemBean itemBean,int position){
		int width = holder.fansfragment_listview_item_medalLayout.getWidth();
		if(width != 0){
			fansItemBeans.get(position).setMedalwidth(width);
		}
		ArrayList<MedalBean> medalBeans = itemBean.getBadges();
		int itemNum = (int) (width / (Contanst.MEDAL_IMAGE_WIDTH
				* BaseActivity.DENSITY + 6 * BaseActivity.DENSITY));
		Tools.getLog(Tools.d, "aaa", "名称：" + itemBean.getUserName() + ",应该有几个："
				+ itemNum);
		for (int i = 0; i < itemNum; i++) {
			if (medalBeans != null) {
				if (i + 1 > medalBeans.size()) {
					break;
				}
				ImageView imageView = new ImageView(mContext);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						(int) (Contanst.MEDAL_IMAGE_WIDTH * BaseActivity.DENSITY),
						(int) (Contanst.MEDAL_IMAGE_HEIGHT * BaseActivity.DENSITY));
				params.setMargins(0, 0, 6, 0);
				imageView.setLayoutParams(params);
				holder.fansfragment_listview_item_medalLayout
				.addView(imageView);
				ImageLoader.getInstance().displayImage(
						UrlParams.QINIU_IP + medalBeans.get(i).getPicPath(),
						imageView, options);
			}

		}
	}
	class HolderView{
		private ImageView fansfragment_listview_item_headImage,fansfragment_listview_item_fanstypeImage;
		private TextView fansfragment_listview_item_name,fansfragment_listview_item_point,
		fansfragment_listview_item_timeOrtaskname,fansfragment_listview_item_fanstypeText;
		private LinearLayout fansfragment_listview_item_fanstypeLayout,fansfragment_listview_item_medalLayout;
		private RelativeLayout fansfragment_listview_item_Layout;
		public HolderView(View convertView){
			fansfragment_listview_item_medalLayout = (LinearLayout) convertView.findViewById(R.id.fansfragment_listview_item_medalLayout);
			fansfragment_listview_item_headImage = (ImageView) convertView.findViewById(R.id.fansfragment_listview_item_headImage);
			fansfragment_listview_item_fanstypeImage = (ImageView) convertView.findViewById(R.id.fansfragment_listview_item_fanstypeImage);

			fansfragment_listview_item_name = (TextView) convertView.findViewById(R.id.fansfragment_listview_item_name);
			fansfragment_listview_item_point = (TextView) convertView.findViewById(R.id.fansfragment_listview_item_point);
			fansfragment_listview_item_timeOrtaskname = (TextView) convertView.findViewById(R.id.fansfragment_listview_item_timeOrtaskname);
			fansfragment_listview_item_fanstypeText = (TextView) convertView.findViewById(R.id.fansfragment_listview_item_fanstypeText);
			fansfragment_listview_item_Layout = (RelativeLayout) convertView.findViewById(R.id.fansfragment_listview_item_Layout);
			fansfragment_listview_item_fanstypeLayout = (LinearLayout) convertView.findViewById(R.id.fansfragment_listview_item_fanstypeLayout);
		}
	}
	public interface setFansTypeLisitener{
		public void setFansType(int position,boolean isfan);
	}
	public interface OnFansFragmentItemClick{
		public void itemClick(int position);
	}
}
