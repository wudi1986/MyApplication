package com.yktx.check.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.R;
import com.yktx.check.adapter.FansFragmentAdapter.setFansTypeLisitener;
import com.yktx.check.bean.FansItemBean;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ThisTaskUserAdapter extends BaseAdapter{
	private ArrayList<FansItemBean> fansItemBeans = new ArrayList<FansItemBean>(10);
	private Context mContext;
	private LayoutInflater inflater;
	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.zw_touxiang)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(100))
	// 启用内存缓存
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	public DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.toumingimg).showImageForEmptyUri(null)
	.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheOnDisk(true)
	// .displayer(new RoundedBitmapDisplayer(150))
	// .displayer(new RoundedBitmapDisplayer(6))
	// .cacheInMemory(false)
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();



	public ThisTaskUserAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
	}
	public void setList(ArrayList<FansItemBean> fansItemBeans) {
		this.fansItemBeans = fansItemBeans;
	}
	setFansTypeLisitener fansTypeLisitener;
	public void setfansType(setFansTypeLisitener lisitener){
		fansTypeLisitener = lisitener;
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
		HolderView holderView;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.thistaskuser_item, null);
			holderView = new HolderView(convertView);
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
		FansItemBean bean = fansItemBeans.get(position);
		showView(position,bean,holderView);
		return convertView;
	}
	public void showView(final int position,FansItemBean itemBean,HolderView holder){
		ImageLoader.getInstance().displayImage(Tools.getImagePath(itemBean.getImageSource()) + itemBean.getAvartarPath()+(itemBean.getImageSource() == 2?"?imageMogr2/thumbnail/55x55":""),
				holder.thistaskuser_item_headImage, headOptions);
		holder.thistaskuser_item_name.setText(itemBean.getUserName());
		holder.thistaskuser_item_point.setText(itemBean.getPoint());
		holder.thistaskuser_item_leiji.setText("累计打卡"+itemBean.getJobCount()+"次");
		ImageLoader.getInstance().displayImage(UrlParams.QINIU_IP +itemBean.getBadgePath(), //+"?imageMogr2/thumbnail/39x48"
				holder.thistaskuser_item_fanstypeImage_medalImage, options);
		holder.thistaskuser_item_fanstypeLayout.setVisibility(View.GONE);
		int relation = itemBean.getRelation();
		boolean isFans = false;
		if(relation == -1){
			holder.thistaskuser_item_fanstypeLayout.setVisibility(View.GONE);

		}else if(relation == 0 ||relation == 1){
			//			holder.thistaskuser_item_fanstypeLayout.setVisibility(View.VISIBLE);
			holder.thistaskuser_item_fanstypeImage.setImageResource(R.drawable.geren_guanzhu_weiguan);
			holder.thistaskuser_item_fanstypeText.setTextColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_fanstypeText.setText("关注");
			isFans = false;

		}else if(relation == 2 || relation == 3){
			//			holder.thistaskuser_item_fanstypeLayout.setVisibility(View.VISIBLE);
			holder.thistaskuser_item_fanstypeImage.setImageResource(R.drawable.geren_guanzhu_yiguan);
			holder.thistaskuser_item_fanstypeText.setTextColor(mContext.getResources().getColor(R.color.meibao_color_11));
			holder.thistaskuser_item_fanstypeText.setText("已关注");
			isFans = true;
		}
		final boolean bl = isFans;
		holder.thistaskuser_item_fanstypeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Tools.getLog(Tools.d, "aaa", "adapterguanzhu:"+bl);
				fansTypeLisitener.setFansType(position, bl);
			}
		});
		
		int progress = itemBean.getProgress();
		switch (progress) {
		case 0:
			holder.thistaskuser_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 1:
			holder.thistaskuser_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 2:
			holder.thistaskuser_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 3:
			holder.thistaskuser_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 4:
			holder.thistaskuser_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 5:
			holder.thistaskuser_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holder.thistaskuser_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 6:
			holder.thistaskuser_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.thistaskuser_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		}
	}
	class HolderView{
		ImageView thistaskuser_item_headImage,thistaskuser_item_fanstypeImage,thistaskuser_item_fanstypeImage_medalImage;
		TextView thistaskuser_item_name,thistaskuser_item_point,thistaskuser_item_leiji,
		thistaskuser_item_fanstypeText;
		ImageView thistaskuser_item_progress1,thistaskuser_item_progress2,thistaskuser_item_progress3,
		thistaskuser_item_progress4,thistaskuser_item_progress5,thistaskuser_item_progress6,
		thistaskuser_item_progress7;
		LinearLayout thistaskuser_item_fanstypeLayout;
		public HolderView(View convertView){
			thistaskuser_item_headImage = (ImageView) convertView.findViewById(R.id.thistaskuser_item_headImage);
			thistaskuser_item_fanstypeImage_medalImage = (ImageView) convertView.findViewById(R.id.thistaskuser_item_fanstypeImage_medalImage);
			thistaskuser_item_fanstypeImage = (ImageView) convertView.findViewById(R.id.thistaskuser_item_fanstypeImage);
			thistaskuser_item_name = (TextView) convertView.findViewById(R.id.thistaskuser_item_name);
			thistaskuser_item_point = (TextView) convertView.findViewById(R.id.thistaskuser_item_point);
			thistaskuser_item_leiji = (TextView) convertView.findViewById(R.id.thistaskuser_item_leiji);
			thistaskuser_item_fanstypeText = (TextView) convertView.findViewById(R.id.thistaskuser_item_fanstypeText);
			thistaskuser_item_fanstypeLayout = (LinearLayout) convertView.findViewById(R.id.thistaskuser_item_fanstypeLayout);
			thistaskuser_item_progress1 = (ImageView) convertView.findViewById(R.id.thistaskuser_item_progress1);
			thistaskuser_item_progress2 = (ImageView) convertView.findViewById(R.id.thistaskuser_item_progress2);
			thistaskuser_item_progress3 = (ImageView) convertView.findViewById(R.id.thistaskuser_item_progress3);
			thistaskuser_item_progress4 = (ImageView) convertView.findViewById(R.id.thistaskuser_item_progress4);
			thistaskuser_item_progress5 = (ImageView) convertView.findViewById(R.id.thistaskuser_item_progress5);
			thistaskuser_item_progress6 = (ImageView) convertView.findViewById(R.id.thistaskuser_item_progress6);
			thistaskuser_item_progress7 = (ImageView) convertView.findViewById(R.id.thistaskuser_item_progress7);
		}
	}
	public interface setFansTypeLisitener{
		public void setFansType(int position,boolean isfan);
	}
}
