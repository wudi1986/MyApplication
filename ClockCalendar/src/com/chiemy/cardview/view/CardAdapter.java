package com.chiemy.cardview.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.ClockGroupInfoFragmentActivity;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.ImagePagerActivity;
import com.yktx.check.R;
import com.yktx.check.TaskInfoActivity;
import com.yktx.check.adapter.AttentionFragmentListViewAdapter.HoldView;
import com.yktx.check.adapter.ClockGroupInfoAdapter.OnClickAdd;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;

public class CardAdapter extends BaseAdapter {
	private final Context mContext;

	private ArrayList<TaskItemBean> mData;

	LayoutInflater inflater;
	String userID;

	public DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.anim.loading_image_animation)
			.showImageForEmptyUri(null).showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
			// .displayer(new RoundedBitmapDisplayer(150))
			// .displayer(new RoundedBitmapDisplayer(6))
			.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.zw_touxiang).showImageForEmptyUri(null)
			.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true).cacheInMemory(true)
			.displayer(new RoundedBitmapDisplayer(100))
			// 启用内存缓存
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	public DisplayImageOptions options1 = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.toumingimg).showImageForEmptyUri(null)
			.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
			// .displayer(new RoundedBitmapDisplayer(150))
			// .displayer(new RoundedBitmapDisplayer(6))
			.cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	public CardAdapter(Context context, String userID) {
		mContext = context;
		mData = new ArrayList<TaskItemBean>();
		inflater = LayoutInflater.from(context);
		this.userID = userID;
	}

	private int position;

	public int getPosition() {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		this.position = position;
		Tools.getLog(Tools.d,"aaa", "getView ==" + position);
		convertView = inflater.inflate(R.layout.readcard_cardinfo, null);
		holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder(convertView);
			convertView.setTag(convertView);
		}
		TaskItemBean itemBean = mData.get(position);
		showView(position, holder, itemBean);

		return convertView;

	}

	@SuppressLint("NewApi")
	private void showView(final int position, final ViewHolder holder, final TaskItemBean itemBean) {
		// if (readcard_fragment_item_daqiTopImage.getVisibility() ==
		// View.VISIBLE) {
		// isShowDaqi = settings.getBoolean("isshowdaqi", false);
		// if (isShowDaqi) {
		// readcard_fragment_item_daqiTopImage
		// .setVisibility(View.INVISIBLE);
		// } else {
		// readcard_fragment_item_daqiTopImage.setVisibility(View.VISIBLE);
		// }
		// }
		final String[] imagePathArray1;
		final String[] imagePathArray2;
		final String[] imagePathArray3;
		final String[] getAllSource;
		boolean isHaveSingnature, isHaveImage, isHaveQuantity;

		String isVote = itemBean.getVoted(); // 获得是否点赞
		if (isVote.equals("0")) {
			// readcard_fragment_item_daqiImage
			// .setBackgroundResource(R.drawable.daqi_select);
			holder.readcard_fragment_item_votesNumImage.setImageResource(R.drawable.building_vote_image);
		} else {
			// readcard_fragment_item_daqiImage
			// .setBackgroundResource(R.drawable.daqi_select2);
			holder.readcard_fragment_item_votesNumImage.setImageResource(R.drawable.building_vote_selectimage);
		}
		
		
//		ViewTreeObserver vto = holder.readcard_fragment_item_votesNumImage.getViewTreeObserver();
//		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//
//			@Override
//			public void onGlobalLayout() {
//				// TODO Auto-generated method stub
//				int X =  holder.readcard_fragment_item_votesNumImage.getScrollX();
//				int Y =  holder.readcard_fragment_item_votesNumImage.getScrollY();
//				Tools.getLog(Tools.d, "ccc", "position:====X:"+X+"===Y"+Y);
//			}
//		});
		
		
		// 是否关注
		int relation = itemBean.getRelation();
		 setRelationType(holder, relation);
		 holder.readcard_fragment_item_fanstypeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(onClickVoteOrComments != null)
					onClickVoteOrComments.clickFollow(itemBean.getUserId());
			}
		});

		holder.readcard_fragment_item_topLayout.setVisibility(View.VISIBLE);
		holder.readcard_fragment_item_yisi.setVisibility(View.GONE);
		holder.readcard_fragment_item_blankTopLayout.setVisibility(View.GONE);
//		ImageLoader.getInstance().displayImage(Tools.getImagePath(itemBean.getBadgeSource()) + itemBean.getBadgePath(),
//				holder.readcard_fragment_item_medalImage, options1);
		ImageLoader.getInstance().displayImage(Tools.getImagePath(itemBean.getAvatar_source()) + itemBean.getAvartar_path()+(itemBean.getAvatar_source()== 2?"?imageMogr2/thumbnail/85x85":""),
				holder.readcard_fragment_item_headImage, headOptions);
		holder.readcard_fragment_item_headLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (userID.equals(itemBean.getUserId())) {
					Intent in = new Intent(mContext, ClockMyActivity.class);
					mContext.startActivity(in);
				} else {
					Intent in = new Intent(mContext, ClockOtherUserActivity.class);
					in.putExtra("userid", itemBean.getUserId());
					mContext.startActivity(in);
				}
			}
		});
		final String allImagePath = itemBean.getAllPicPath();
		if (itemBean.getPicCount().equals("0")) {
			holder.readcard_fragment_item_ImageLayout.setVisibility(View.GONE);
			holder.readcard_fragment_item_clockContent.setMaxLines(13);// 纯文字是最多13行
			isHaveImage = false;
		} else if (itemBean.getPicCount().equals("1")) {
			isHaveImage = true;
			imagePathArray1 = new String[1];
			imagePathArray1[0] = allImagePath;
			getAllSource = new String[1];
			getAllSource[0] = itemBean.getAllSource();
			holder.readcard_fragment_item_ImageLayout_only1.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(Tools.getImagePath(Integer.parseInt(getAllSource[0])) + allImagePath+(Integer.parseInt(getAllSource[0])== 2?"?imageMogr2/thumbnail/310x310":""),
					holder.readcard_fragment_item_ImageLayout_only1, options);
			holder.readcard_fragment_item_clockContent.setMaxLines(1);// 一张图最多1行
			holder.readcard_fragment_item_ImageLayout_Image1.setVisibility(View.GONE);
			holder.readcard_fragment_item_ImageLayout_Image2.setVisibility(View.GONE);
			holder.readcard_fragment_item_ImageLayout_Image3.setVisibility(View.GONE);

			holder.readcard_fragment_item_ImageLayout.setVisibility(View.VISIBLE);
			holder.readcard_fragment_item_ImageLayout_only1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray1, getAllSource);
					Tools.getLog(Tools.i, "aaa", "imagePathArray1:" + imagePathArray1[0]);
				}
			});
		} else if (itemBean.getPicCount().equals("2")) {
			isHaveImage = true;
			imagePathArray2 = allImagePath.split(",");

			holder.readcard_fragment_item_ImageLayout_only1.setVisibility(View.GONE);
			getAllSource = itemBean.getAllSource().split(",");
			ImageLoader.getInstance().displayImage(Tools.getImagePath(Integer.parseInt(getAllSource[0])) + imagePathArray2[0]+(Integer.parseInt(getAllSource[0])== 2?"?imageMogr2/thumbnail/310x310":""),
					holder.readcard_fragment_item_ImageLayout_Image1, options);
			holder.readcard_fragment_item_clockContent.setMaxLines(7);// 2张图最多7行
			holder.readcard_fragment_item_ImageLayout_Image1.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(Tools.getImagePath(Integer.parseInt(getAllSource[1])) + imagePathArray2[1]+(Integer.parseInt(getAllSource[1])== 2?"?imageMogr2/thumbnail/310x310":""),
					holder.readcard_fragment_item_ImageLayout_Image2, options);
			holder.readcard_fragment_item_ImageLayout_Image2.setVisibility(View.VISIBLE);
			holder.readcard_fragment_item_ImageLayout_Image3.setVisibility(View.GONE);
			holder.readcard_fragment_item_ImageLayout.setVisibility(View.VISIBLE);
			holder.readcard_fragment_item_ImageLayout_Image1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray2, getAllSource);
				}
			});
			holder.readcard_fragment_item_ImageLayout_Image2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(1, imagePathArray2, getAllSource);
				}
			});
		} else {
			isHaveImage = true;
			holder.readcard_fragment_item_ImageLayout_only1.setVisibility(View.GONE);
			imagePathArray3 = allImagePath.split(",");
			getAllSource = itemBean.getAllSource().split(",");
			ImageLoader.getInstance().displayImage(Tools.getImagePath(Integer.parseInt(getAllSource[0])) + imagePathArray3[0]+(Integer.parseInt(getAllSource[0])== 2?"?imageMogr2/thumbnail/310x310":""),
					holder.readcard_fragment_item_ImageLayout_Image1, options);
			ImageLoader.getInstance().displayImage(Tools.getImagePath(Integer.parseInt(getAllSource[1])) + imagePathArray3[1]+(Integer.parseInt(getAllSource[1])== 2?"?imageMogr2/thumbnail/310x310":""),
					holder.readcard_fragment_item_ImageLayout_Image2, options);
			holder.readcard_fragment_item_clockContent.setMaxLines(7);// 3张图最多7行
			ImageLoader.getInstance().displayImage(Tools.getImagePath(Integer.parseInt(getAllSource[2])) + imagePathArray3[2]+(Integer.parseInt(getAllSource[2])== 2?"?imageMogr2/thumbnail/310x310":""),
					holder.readcard_fragment_item_ImageLayout_Image3, options);
			holder.readcard_fragment_item_ImageLayout_Image1.setVisibility(View.VISIBLE);
			holder.readcard_fragment_item_ImageLayout_Image2.setVisibility(View.VISIBLE);
			holder.readcard_fragment_item_ImageLayout_Image3.setVisibility(View.VISIBLE);
			holder.readcard_fragment_item_ImageLayout.setVisibility(View.VISIBLE);
			holder.readcard_fragment_item_ImageLayout_Image1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray3, getAllSource);
				}
			});
			holder.readcard_fragment_item_ImageLayout_Image2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(1, imagePathArray3, getAllSource);
				}
			});
			holder.readcard_fragment_item_ImageLayout_Image3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(2, imagePathArray3, getAllSource);
				}
			});
			holder.readcard_fragment_item_ImageLayout.setVisibility(View.VISIBLE);
		}
		holder.readcard_fragment_item_name.setText(itemBean.getName());
		holder.readcard_fragment_item_takeClockNumText.setText(itemBean.getTaskCount() + "");
		holder.readcard_fragment_item_clockName.setText(itemBean.getTitle());
		String singnature = itemBean.getSignature();
		if (singnature != null && singnature.length() != 0) {
			isHaveSingnature = true;
			holder.readcard_fragment_item_clockContent.setText(ToDBC(singnature));
			holder.readcard_fragment_item_clockContent.setVisibility(View.VISIBLE);
		} else {
			isHaveSingnature = false;
			holder.readcard_fragment_item_clockContent.setVisibility(View.GONE);
		}
		holder.readcard_fragment_item_clockContent.setText(itemBean.getSignature());

		String city = itemBean.getCity(); // 是否有城市信息

		if (city.equals("0")) {
			// readcard_fragment_item_daqiImage
			// .setBackgroundResource(R.drawable.daqi_select);
			holder.readcard_fragment_item_TimeText.setText(TimeUtil
					.getTimes(itemBean.getCheck_time())
					+ "  第"
					+ itemBean.getTotalDateCount() + "天");
		} else {
			holder.readcard_fragment_item_TimeText.setText(TimeUtil
					.getTimes(itemBean.getCheck_time())
					+ "  第"
					+ itemBean.getTotalDateCount() + "天   " + city);
		}
		holder.readcard_fragment_item_votesNumText.setText(itemBean
				.getVoteCount() + "");

		Tools.getLog(Tools.d,"aaa", "getVoteCount ==" + position);
		holder.readcard_fragment_item_commentsNumText.setText(itemBean.getCommentCount() + "");
		String quantity = itemBean.getQuantity();
		if (quantity != null && quantity.length() != 0) {
			isHaveQuantity = true;
			holder.readcard_fragment_item_clockQuantity.setText(quantity);
			holder.readcard_fragment_item_clockQuantity.setVisibility(View.VISIBLE);
			holder.readcard_fragment_item_clockQuantityLayout.setVisibility(View.VISIBLE);
		} else {
			isHaveQuantity = false;
			holder.readcard_fragment_item_clockQuantity.setVisibility(View.GONE);
			holder.readcard_fragment_item_clockQuantityLayout.setVisibility(View.GONE);
		}

		if (isHaveImage) {
			holder.readcard_fragment_item_clockContent_BottomBlackLayout.setVisibility(View.VISIBLE);
		} else {
			holder.readcard_fragment_item_clockContent_BottomBlackLayout.setVisibility(View.GONE);
		}
		if (!isHaveImage && !isHaveSingnature) {
			holder.readcard_fragment_item_clockContent.setText("打卡1次");
			holder.readcard_fragment_item_clockContent.setVisibility(View.VISIBLE);
		}

		holder.readcard_fragment_item_clockLevelLayout.setBackgroundResource(R.drawable.newest_fragment_item_center_shape);
		if (isHaveImage || isHaveQuantity || isHaveSingnature) {
			holder.readcard_fragment_item_IQSLayout.setVisibility(View.VISIBLE);

		} else {

		}

		holder.readcard_fragment_item_votesNumLayout.setOnClickListener(new OnClickListener() {// 点赞

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						if (onClickVoteOrComments != null) {
							onClickVoteOrComments.clickVote(position);
						}
					}
				});

		holder.readcard_fragment_item_commentsNumLayout.setOnClickListener(new OnClickListener() {// 点赞

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						if (onClickVoteOrComments != null) {
							onClickVoteOrComments.clickComments(position);
						}
					}
				});
		holder.readcard_fragment_item_clockLevelLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, ClockGroupInfoFragmentActivity.class);
				in.putExtra("buildingId", itemBean.getBuildingId());
				mContext.startActivity(in);
			}
		});
		holder.readcard_fragment_item_IQSLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, TaskInfoActivity.class);// 这个跳转不存在是不是自己，判断会在TaskInfoActivity有
				in.putExtra("taskid", itemBean.getTaskId());
				in.putExtra("userid", itemBean.getUserId());
				
				mContext.startActivity(in);
			}
		});

	}

	public void setRelationType(ViewHolder holder, int relation) {
		if (relation == -1) {
			holder.readcard_fragment_item_fanstypeLayout.setVisibility(View.GONE);

		} else if (relation == 0 || relation == 1) {
			holder.readcard_fragment_item_fanstypeLayout.setVisibility(View.VISIBLE);
			holder.readcard_fragment_item_fanstypeImage
					.setImageResource(R.drawable.geren_guanzhu_weiguan);
			holder.readcard_fragment_item_fanstypeText.setTextColor(mContext.getResources()
					.getColor(R.color.meibao_color_1));
			holder.readcard_fragment_item_fanstypeText.setText("关注");
		} else if (relation == 2 || relation == 3) {
			holder.readcard_fragment_item_fanstypeLayout.setVisibility(View.GONE);
//			readcard_fragment_item_fanstypeImage
//					.setImageResource(R.drawable.geren_guanzhu_yiguan);
//			readcard_fragment_item_fanstypeText.setTextColor(getResources()
//					.getColor(R.color.meibao_color_11));
//			readcard_fragment_item_fanstypeText.setText("已关注");
		}
	}
	
	public void setList(ArrayList<TaskItemBean> items) {
		mData = items;
	}

	@Override
	public TaskItemBean getItem(int position) {
		return mData.get(position);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	public Context getContext() {
		return mContext;
	}

	public void clear() {
		if (mData != null) {
			mData.clear();
		}
	}

	private void imageBrower(int position, String[] urls, String[] imageSource) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_SOURCE, imageSource);
		mContext.startActivity(intent);
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

	class ViewHolder {
		TextView readcard_fragment_item_name, readcard_fragment_item_clockName,
				readcard_fragment_item_clockContent;// 上部分的id

		TextView readcard_fragment_item_TimeText,
				readcard_fragment_item_votesNumText,
				readcard_fragment_item_commentsNumText;// 计数的id

		TextView readcard_fragment_item_clockQuantity;

		TextView readcard_fragment_item_typeAll,
				readcard_fragment_item_typeHaveImage,
				readcard_fragment_item_fanstypeText,
				readcard_fragment_item_takeClockNumText;

		ImageView readcard_fragment_item_headImage,
				readcard_fragment_item_ImageLayout_Image1,
				readcard_fragment_item_ImageLayout_Image2,
				readcard_fragment_item_ImageLayout_only1,
				readcard_fragment_item_ImageLayout_Image3,
				readcard_fragment_item_votesNumImage,
				readcard_fragment_item_yisi, readcard_fragment_item_medalImage,
				readcard_fragment_item_fanstypeImage,
				readcard_fragment_item_daqiTopImage;
		LinearLayout readcard_fragment_item_ImageLayout,
				readcard_fragment_item_votesNumLayout,
				readcard_fragment_item_commentsNumLayout,
				readcard_fragment_item_anim,
				readcard_fragment_item_blankTopLayout,
				readcard_fragment_item_fanstypeLayout;
		RelativeLayout readcard_fragment_item_IQSLayout,
				readcard_fragment_item_imagebottomblank,
				readcard_fragment_item_clockQuantityLayout,
				readcard_fragment_item_clockContent_BottomBlackLayout,
				readcard_fragment_item_topLayout,
				readcard_fragment_item_clockLevelLayout,
				readcard_fragment_item;

		RelativeLayout loadingView, readcard_fragment_item_headLayout;

		public ViewHolder(View v) {
			readcard_fragment_item_name = (TextView) v
					.findViewById(R.id.readcard_fragment_item_name);
			readcard_fragment_item_clockName = (TextView) v
					.findViewById(R.id.readcard_fragment_item_clockName);
			readcard_fragment_item_clockContent = (TextView) v
					.findViewById(R.id.readcard_fragment_item_clockContent);

			readcard_fragment_item_TimeText = (TextView) v
					.findViewById(R.id.readcard_fragment_item_TimeText);
			readcard_fragment_item_votesNumText = (TextView) v
					.findViewById(R.id.readcard_fragment_item_votesNumText);
			readcard_fragment_item_commentsNumText = (TextView) v
					.findViewById(R.id.readcard_fragment_item_commentsNumText);
			readcard_fragment_item_clockQuantity = (TextView) v
					.findViewById(R.id.readcard_fragment_item_clockQuantity);
			readcard_fragment_item_takeClockNumText = (TextView) v
					.findViewById(R.id.readcard_fragment_item_takeClockNumText);

			readcard_fragment_item_headImage = (ImageView) v
					.findViewById(R.id.readcard_fragment_item_headImage);
			readcard_fragment_item_ImageLayout_only1 = (ImageView) v
					.findViewById(R.id.readcard_fragment_item_ImageLayout_only1);

			readcard_fragment_item_ImageLayout_Image1 = (ImageView) v
					.findViewById(R.id.readcard_fragment_item_ImageLayout_Image1);
			readcard_fragment_item_ImageLayout_Image2 = (ImageView) v
					.findViewById(R.id.readcard_fragment_item_ImageLayout_Image2);
			readcard_fragment_item_ImageLayout_Image3 = (ImageView) v
					.findViewById(R.id.readcard_fragment_item_ImageLayout_Image3);
			readcard_fragment_item_votesNumImage = (ImageView) v
					.findViewById(R.id.readcard_fragment_item_votesNumImage);
			readcard_fragment_item_yisi = (ImageView) v
					.findViewById(R.id.readcard_fragment_item_yisi);
			readcard_fragment_item_medalImage = (ImageView) v
					.findViewById(R.id.readcard_fragment_item_medalImage);
			readcard_fragment_item_ImageLayout = (LinearLayout) v
					.findViewById(R.id.readcard_fragment_item_ImageLayout);
			readcard_fragment_item_votesNumLayout = (LinearLayout) v
					.findViewById(R.id.readcard_fragment_item_votesNumLayout);
			readcard_fragment_item_commentsNumLayout = (LinearLayout) v
					.findViewById(R.id.readcard_fragment_item_commentsNumLayout);
			readcard_fragment_item_anim = (LinearLayout) v
					.findViewById(R.id.readcard_fragment_item_anim);

			readcard_fragment_item_blankTopLayout = (LinearLayout) v
					.findViewById(R.id.readcard_fragment_item_blankTopLayout);

			readcard_fragment_item_IQSLayout = (RelativeLayout) v
					.findViewById(R.id.readcard_fragment_item_IQSLayout);
			readcard_fragment_item_imagebottomblank = (RelativeLayout) v
					.findViewById(R.id.readcard_fragment_item_imagebottomblank);
			readcard_fragment_item_clockQuantityLayout = (RelativeLayout) v
					.findViewById(R.id.readcard_fragment_item_clockQuantityLayout);
			readcard_fragment_item_clockContent_BottomBlackLayout = (RelativeLayout) v
					.findViewById(R.id.readcard_fragment_item_clockContent_BottomBlackLayout);
			readcard_fragment_item_topLayout = (RelativeLayout) v
					.findViewById(R.id.readcard_fragment_item_topLayout);
			readcard_fragment_item_clockLevelLayout = (RelativeLayout) v
					.findViewById(R.id.readcard_fragment_item_clockLevelLayout);
			readcard_fragment_item_headLayout = (RelativeLayout) v
					.findViewById(R.id.readcard_fragment_item_headLayout);
			readcard_fragment_item_fanstypeText = (TextView) v
					.findViewById(R.id.readcard_fragment_item_fanstypeText);
			readcard_fragment_item_fanstypeImage = (ImageView) v
					.findViewById(R.id.readcard_fragment_item_fanstypeImage);
			readcard_fragment_item_fanstypeLayout = (LinearLayout) v
					.findViewById(R.id.readcard_fragment_item_fanstypeLayout);
		}
	}

	OnClickVoteOrComments onClickVoteOrComments;

	public interface OnClickVoteOrComments {
		public void clickVote(int position);

		public void clickComments(int position);

		public void clickFollow(String userId);
		
	}

	public void setOnClickVoteOrComments(
			OnClickVoteOrComments onClickVoteOrComments) {
		this.onClickVoteOrComments = onClickVoteOrComments;
	}
}
