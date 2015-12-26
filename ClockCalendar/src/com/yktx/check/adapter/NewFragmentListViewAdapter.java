package com.yktx.check.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.BaseActivity;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.ImagePagerActivity;
import com.yktx.check.R;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.bean.VotesBean;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.listener.IntoUserCenterListener;
import com.yktx.check.square.fragment.GroupMainFragmentActivity;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;

/**
 * Created by Administrator on 2014/4/8.
 */
public class NewFragmentListViewAdapter extends BaseAdapter {
	Activity context;
	String latitude = "0.0";
	String longitude = "0.0";
	// BuildingOnClick buildingOnClick;
	OnNewFragmentItemClick fragmentItemClick;
	IntoUserCenterListener intoUserCenter;
	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.zw_touxiang)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(100))
	// 启用内存缓存
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	public DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.anim.loading_image_animation).showImageForEmptyUri(null)
	.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheOnDisk(true)
	// .displayer(new RoundedBitmapDisplayer(150))
	// .displayer(new RoundedBitmapDisplayer(6))
	// .cacheInMemory(false)
	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
	public DisplayImageOptions options1 = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.toumingimg)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	// .displayer(new RoundedBitmapDisplayer(150))
	// .displayer(new RoundedBitmapDisplayer(6))
	// .cacheInMemory(false)
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	ArrayList<TaskItemBean> list = new ArrayList<TaskItemBean>(10);

	public HashMap<String, Object> chatFacialMap = new HashMap<String, Object>();

	private boolean isMyorOther;
	int thisType = 0;// 0 位广场的最新 ；1广场的关注 ；2 为个人明细 ；3为他人明细 为了占位图
	String userID;

	public NewFragmentListViewAdapter(Activity context, boolean isMyorOther,
			String userID, int type) {
		this.context = context;
		this.isMyorOther = isMyorOther;
		this.userID = userID;
		thisType = type;
		// initFacialMap();
	}

	// public void setBuildingOnClick(BuildingOnClick buildingOnClick){
	// this.buildingOnClick = buildingOnClick;
	// }
	public void setOnNewFragmentItemClick(
			OnNewFragmentItemClick fragmentItemClick) {
		this.fragmentItemClick = fragmentItemClick;
	}

	public void setList(ArrayList<TaskItemBean> list) {
		this.list = list;
	}

	public void setIntoUserCenter(IntoUserCenterListener intoUserCenter) {
		this.intoUserCenter = intoUserCenter;
	}

	public void setDistance(String latitude, String longitude) {
		// TODO Auto-generated method stub
		this.latitude = latitude;
		this.longitude = longitude;
	}

	private boolean isNullItem;

	@Override
	public int getCount() {
		// return list.size();

		if (list.size() == 0) {
			isNullItem = true;
			return 1;
		}
		isNullItem = false;
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// @Override
	// public int getItemViewType(int position) {
	// // TODO Auto-generated method stub
	// TaskItemBean newMainBean = list.get(position);
	// Tools.getLog(Tools.i, "aaa",
	// "getItemViewTypegetItemViewTypegetItemViewTypegetItemViewType");
	// return 1;
	// }

	// /**
	// * 返回所有的layout的数量
	// *
	// * */
	// @Override
	// public int getViewTypeCount() {
	// return 4;
	// }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView;
		// int type = 1;
		// type = getItemViewType(position);
		if (isNullItem) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.image_null_item, null);
			ImageView image = (ImageView) v.findViewById(R.id.imageListNull);
			if (thisType == 0) {
				image.setImageResource(R.drawable.zhanwei_zuixin);
			} else if (thisType == 1) {
				image.setImageResource(R.drawable.zhanwei_guanzhu_ziji);
			} else if (thisType == 2) {
				image.setImageResource(R.drawable.zhanwei_dakamingxi_ziji);
			} else if (thisType == 3) {
				image.setImageResource(R.drawable.zhanwei_dakamingxi_taren);
			}
			image.setImageResource(R.drawable.zhanwei_dakamingxi_ziji);
			return v;
		}
		TaskItemBean newMainBean = (TaskItemBean) list.get(position);

		// holder = (
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.newest_fragment_item, null);
			holdView = new NewFragmentListViewAdapter.HoldView(convertView);

			convertView.setTag(holdView);
		} else {
			holdView = (HoldView) convertView.getTag();
		}
		show(holdView, newMainBean, position);

		return convertView;
	}

	SpannableString msp = null;

	private void show(final HoldView holdView, final TaskItemBean itemBean,
			final int position) {
		final String[] imagePathArray1;
		final String[] imagePathArray2;
		final String[] imagePathArray3;
		final String[] getAllSource;
		boolean isHaveSingnature, isHaveImage, isHaveQuantity;
		boolean isblankVisity;
		if (isMyorOther) {
			holdView.newset_fragment_item_topLayout.setVisibility(View.GONE);
			holdView.newset_fragment_item_blankTopLayout
			.setVisibility(View.VISIBLE);
			if (position == 0) {
				holdView.newset_fragment_item_blankTopLayout1
				.setVisibility(View.VISIBLE);
			} else {
				holdView.newset_fragment_item_blankTopLayout1
				.setVisibility(View.GONE);
			}
			holdView.newset_fragment_item.setBackgroundColor(context
					.getResources().getColor(R.color.white));
			holdView.newset_fragment_item_dayLayout.setVisibility(View.GONE);
			if (itemBean.getPrivateFlag().equals("1")) {
				holdView.newset_fragment_item_yisi.setVisibility(View.VISIBLE);
			} else {
				holdView.newset_fragment_item_yisi.setVisibility(View.GONE);
			}
		} else {
			holdView.newset_fragment_item.setBackgroundColor(context
					.getResources().getColor(R.color.meibao_color_15));
			holdView.newset_fragment_item_topLayout.setVisibility(View.VISIBLE);
			holdView.newset_fragment_item_dayLayout.setVisibility(View.VISIBLE);
			holdView.newset_fragment_item_yisi.setVisibility(View.GONE);
			holdView.newset_fragment_item_blankTopLayout
			.setVisibility(View.GONE);
			holdView.newset_fragment_item_blankTopLayout1
			.setVisibility(View.GONE);
		}
		ImageLoader.getInstance().displayImage(
				UrlParams.QINIU_IP + itemBean.getBadgePath()+"?imageMogr2/thumbnail/33x41",
				holdView.newset_fragment_item_medalImage, options1);
		ImageLoader.getInstance().displayImage(
				Tools.getImagePath(itemBean.getAvatar_source())
				+ itemBean.getAvartar_path()+(itemBean.getAvatar_source() == 2?"?imageMogr2/thumbnail/50x50":""),
				holdView.newset_fragment_item_headImage, headOptions);
		holdView.newset_fragment_item_headImage
		.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// intoUserCenter.getIntoUserCenter(itemBean.getTaskId());\
				// GroupMainFragmentActivity.isReflush =
				// true;//让主activity 不刷新fragment
				if (userID.equals(itemBean.getUserId())) {
					Intent in = new Intent(context,
							ClockMyActivity.class);
					context.startActivity(in);
				} else {
					Intent in = new Intent(context,
							ClockOtherUserActivity.class);
					in.putExtra("userid", itemBean.getUserId());
					context.startActivity(in);
				}
			}
		});
		final String allImagePath = itemBean.getAllPicPath();
		if (itemBean.getPicCount().equals("0")) {
			holdView.newset_fragment_item_ImageLayout.setVisibility(View.GONE);
			isHaveImage = false;
		} else if (itemBean.getPicCount().equals("1")) {
			isHaveImage = true;
			imagePathArray1 = new String[1];
			imagePathArray1[0] = allImagePath;
			getAllSource = new String[1];
			getAllSource[0] = itemBean.getAllSource();
			holdView.newset_fragment_item_ImageLayout_only1
			.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[0]))
					+ allImagePath+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/310x310":""),
					holdView.newset_fragment_item_ImageLayout_only1, options);
			holdView.newset_fragment_item_ImageLayout_Image1
			.setVisibility(View.GONE);
			holdView.newset_fragment_item_ImageLayout_Image2
			.setVisibility(View.GONE);
			holdView.newset_fragment_item_ImageLayout_Image3
			.setVisibility(View.GONE);
			holdView.newset_fragment_item_ImageLayout
			.setVisibility(View.VISIBLE);
			holdView.newset_fragment_item_ImageLayout_only1
			.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray1, getAllSource);
					Tools.getLog(Tools.i, "aaa", "imagePathArray1:"
							+ imagePathArray1[0]);
				}
			});
		} else if (itemBean.getPicCount().equals("2")) {
			isHaveImage = true;
			imagePathArray2 = allImagePath.split(",");

			holdView.newset_fragment_item_ImageLayout_only1
			.setVisibility(View.GONE);
			getAllSource = itemBean.getAllSource().split(",");
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[0]))
					+ imagePathArray2[0]+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.newset_fragment_item_ImageLayout_Image1, options);
			holdView.newset_fragment_item_ImageLayout_Image1
			.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[1]))
					+ imagePathArray2[1]+(Integer.parseInt(getAllSource[1]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.newset_fragment_item_ImageLayout_Image2, options);
			holdView.newset_fragment_item_ImageLayout_Image2
			.setVisibility(View.VISIBLE);
			holdView.newset_fragment_item_ImageLayout_Image3
			.setVisibility(View.GONE);
			holdView.newset_fragment_item_ImageLayout
			.setVisibility(View.VISIBLE);
			holdView.newset_fragment_item_ImageLayout_Image1
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray2, getAllSource);
				}
			});
			holdView.newset_fragment_item_ImageLayout_Image2
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(1, imagePathArray2, getAllSource);
				}
			});
		} else {
			isHaveImage = true;
			holdView.newset_fragment_item_ImageLayout_only1
			.setVisibility(View.GONE);
			imagePathArray3 = allImagePath.split(",");
			getAllSource = itemBean.getAllSource().split(",");
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[0]))
					+ imagePathArray3[0]+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.newset_fragment_item_ImageLayout_Image1, options);
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[1]))
					+ imagePathArray3[1]+(Integer.parseInt(getAllSource[1]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.newset_fragment_item_ImageLayout_Image2, options);
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[2]))
					+ imagePathArray3[2]+(Integer.parseInt(getAllSource[2]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.newset_fragment_item_ImageLayout_Image3, options);
			holdView.newset_fragment_item_ImageLayout_Image1
			.setVisibility(View.VISIBLE);
			holdView.newset_fragment_item_ImageLayout_Image2
			.setVisibility(View.VISIBLE);
			holdView.newset_fragment_item_ImageLayout_Image3
			.setVisibility(View.VISIBLE);
			holdView.newset_fragment_item_ImageLayout
			.setVisibility(View.VISIBLE);
			holdView.newset_fragment_item_ImageLayout_Image1
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray3, getAllSource);
				}
			});
			holdView.newset_fragment_item_ImageLayout_Image2
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(1, imagePathArray3, getAllSource);
				}
			});
			holdView.newset_fragment_item_ImageLayout_Image3
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(2, imagePathArray3, getAllSource);
				}
			});
			holdView.newset_fragment_item_ImageLayout
			.setVisibility(View.VISIBLE);
		}
		holdView.newset_fragment_item_name.setText(itemBean.getName());
		holdView.newset_fragment_item_currentStreak.setText(itemBean
				.getTotalDateCount());
		holdView.newset_fragment_item_clockName.setText(itemBean.getTitle());
		String singnature = itemBean.getSignature();
		if (singnature != null && singnature.length() != 0) {
			isHaveSingnature = true;
			holdView.newset_fragment_item_clockContent
			.setText(ToDBC(singnature));
			holdView.newset_fragment_item_clockContent
			.setVisibility(View.VISIBLE);
		} else {
			isHaveSingnature = false;
			holdView.newset_fragment_item_clockContent.setVisibility(View.GONE);
		}
		holdView.newset_fragment_item_clockContent.setText(itemBean
				.getSignature());

		holdView.newset_fragment_item_TimeText.setText(TimeUtil
				.getTimes(itemBean.getCheck_time())
				+ "  第"
				+ itemBean.getTotalDateCount() + "天");
		holdView.newset_fragment_item_votesNumText.setText(itemBean
				.getVoteCount() + "");
		holdView.newset_fragment_item_commentsNumText.setText(itemBean
				.getCommentCount() + "");
		String quantity = itemBean.getQuantity();
		if (quantity != null && quantity.length() != 0) {
			isHaveQuantity = true;
			String unit = itemBean.getUnit();
			if(unit.equals("0")){
				unit = "";
			}
			holdView.newset_fragment_item_clockQuantity.setText(quantity+unit);
			holdView.newset_fragment_item_clockQuantity
			.setVisibility(View.VISIBLE);
			holdView.newset_fragment_item_clockQuantityLayout
			.setVisibility(View.VISIBLE);
		} else {
			isHaveQuantity = false;
			holdView.newset_fragment_item_clockQuantity
			.setVisibility(View.GONE);
			holdView.newset_fragment_item_clockQuantityLayout
			.setVisibility(View.GONE);
		}

		// ArrayList<VotesBean> votesBeans = itemBean.getVotes();
		// int votesBeansSize = votesBeans.size();
		// StringBuffer sb = new StringBuffer();
		// for(int i = 0;i<votesBeansSize;i++){
		// sb.append(votesBeans.get(i).getName());
		// if(i != (votesBeansSize-1)){
		// sb.append(",");
		// }
		// }
		// holdView.newset_fragment_item_votesUsersContent.setText(sb.toString());
		// String CommentCount = itemBean.getCommentCount()+"";
		//
		// ArrayList<CommentsBean> commentsBeans = itemBean.getComments();
		// String username,text,time,contentText;
		// int usernameEnd,timeStart,timeEnd;
		// boolean isCommentVisity;
		// if(commentsBeans == null || commentsBeans.size() == 0){
		// isCommentVisity = true;
		// isblankVisity = false;
		// holdView.newset_fragment_item_commentsContentLayout1.setVisibility(View.GONE);
		// holdView.newset_fragment_item_commentsContentLayout2.setVisibility(View.GONE);
		// }else if(commentsBeans.size() == 1){
		// isCommentVisity = false;
		// isblankVisity = true;
		// username = commentsBeans.get(0).getName()+"：";
		// text = commentsBeans.get(0).getText();
		// time = "   "+TimeUtil.getTimes(commentsBeans.get(0).getSendTime());
		// contentText =username+text+time;
		// usernameEnd = username.length();
		// timeEnd = contentText.length();
		// timeStart = timeEnd - time.length();
		// msp = new SpannableString(ToDBC(contentText));
		// msp.setSpan(new AbsoluteSizeSpan(14,true), 0, usernameEnd,
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// //设置字体前景色
		// msp.setSpan(new
		// ForegroundColorSpan(context.getResources().getColor(R.color.meibao_color_9)),0,
		// usernameEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为洋红色
		//
		// msp.setSpan(new AbsoluteSizeSpan(12,true), timeStart, timeEnd,
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// //设置字体前景色
		// msp.setSpan(new
		// ForegroundColorSpan(context.getResources().getColor(R.color.meibao_color_11)),timeStart,
		// timeEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为洋红色
		//
		// holdView.newset_fragment_item_commentsContentText1.setText(msp);
		// holdView.newset_fragment_item_commentsContentText1.setMovementMethod(LinkMovementMethod.getInstance());
		//
		// //
		// holdView.newset_fragment_item_commentsContentUserbeforeimage2.setVisibility(View.GONE);
		// //
		// holdView.newset_fragment_item_commentsContentText2.setVisibility(View.GONE);
		// holdView.newset_fragment_item_commentsContentLayout2.setVisibility(View.GONE);
		// holdView.newset_fragment_item_commentsContentLayout1.setVisibility(View.VISIBLE);
		// }else {
		// isCommentVisity = false;
		// isblankVisity = true;
		// //第一条
		// username = commentsBeans.get(0).getName()+"：";
		// text = commentsBeans.get(0).getText();
		// time = "   "+TimeUtil.getTimes(commentsBeans.get(0).getSendTime());
		// contentText =username+text+time;
		// usernameEnd = username.length();
		// timeEnd = contentText.length();
		// timeStart = timeEnd - time.length();
		// msp = new SpannableString(ToDBC(contentText));
		// msp.setSpan(new AbsoluteSizeSpan(14,true), 0, usernameEnd,
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// //设置字体前景色
		// msp.setSpan(new
		// ForegroundColorSpan(context.getResources().getColor(R.color.meibao_color_9)),0,
		// usernameEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为洋红色
		//
		// msp.setSpan(new AbsoluteSizeSpan(12,true), timeStart, timeEnd,
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// //设置字体前景色
		// msp.setSpan(new
		// ForegroundColorSpan(context.getResources().getColor(R.color.meibao_color_11)),timeStart,
		// timeEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为洋红色
		//
		// holdView.newset_fragment_item_commentsContentText1.setText(msp);
		// holdView.newset_fragment_item_commentsContentText1.setMovementMethod(LinkMovementMethod.getInstance());
		//
		// //第二条
		// username = commentsBeans.get(1).getName()+"：";
		// text = commentsBeans.get(1).getText();
		// time = "   "+TimeUtil.getTimes(commentsBeans.get(1).getSendTime());
		// contentText =username+text+time;
		// usernameEnd = username.length();
		// timeEnd = contentText.length();
		// timeStart = timeEnd - time.length();
		// msp = new SpannableString(ToDBC(contentText));
		// msp.setSpan(new AbsoluteSizeSpan(14,true), 0, usernameEnd,
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// //设置字体前景色
		// msp.setSpan(new
		// ForegroundColorSpan(context.getResources().getColor(R.color.meibao_color_9)),0,
		// usernameEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为洋红色
		//
		// msp.setSpan(new AbsoluteSizeSpan(12,true), timeStart, timeEnd,
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// //设置字体前景色
		// msp.setSpan(new
		// ForegroundColorSpan(context.getResources().getColor(R.color.meibao_color_11)),timeStart,
		// timeEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为洋红色
		// //
		// holdView.newset_fragment_item_commentsContentUserText2.setText(commentsBeans.get(1).getName());
		// //
		// holdView.newset_fragment_item_commentsContentText2.setText(commentsBeans.get(1).getText()
		// +"    "+
		// //
		// TimeUtil.getTimes(Long.parseLong(commentsBeans.get(1).getSendTime())));
		// holdView.newset_fragment_item_commentsContentText2.setText(msp);
		// holdView.newset_fragment_item_commentsContentText2.setMovementMethod(LinkMovementMethod.getInstance());
		//
		// //
		// holdView.newset_fragment_item_commentsContentUserbeforeimage2.setVisibility(View.VISIBLE);
		// //
		// holdView.newset_fragment_item_commentsContentText2.setVisibility(View.VISIBLE);
		// holdView.newset_fragment_item_commentsContentLayout2.setVisibility(View.VISIBLE);
		// holdView.newset_fragment_item_commentsContentLayout2.setVisibility(View.VISIBLE);
		// holdView.newset_fragment_item_commentsContentLayout1.setVisibility(View.VISIBLE);
		// }
		// if(CommentCount!= null&&Integer.parseInt(CommentCount)>2){
		// holdView.newset_fragment_item_commentsContentMore.setVisibility(View.VISIBLE);
		// }else{
		// holdView.newset_fragment_item_commentsContentMore.setVisibility(View.GONE);
		// }
		// String voted = itemBean.getVoted();
		// if(voted.equals("1")){
		// holdView.newset_fragment_item_votesNumImage.setImageResource(R.drawable.building_vote_selectimage);
		// }else{
		// holdView.newset_fragment_item_votesNumImage.setImageResource(R.drawable.building_vote_image);
		// }
		if (itemBean.getVoteCount() != 0) {
			holdView.newset_fragment_item_votesUsersLayout
			.setVisibility(View.VISIBLE);
			holdView.newset_fragment_item_lineImage.setVisibility(View.VISIBLE);
			holdView.newset_fragment_item_CVMLLayout
			.setVisibility(View.VISIBLE);
			// if(isblankVisity){
			// holdView.newset_fragment_item_blank.setVisibility(View.VISIBLE);
			// }else{
			holdView.newset_fragment_item_blank.setVisibility(View.GONE);

			// }
		} else {
			// holdView.newset_fragment_item_votesNumText.setText("打气");
			// holdView.newset_fragment_item_dayLayout.setVisibility(View.VISIBLE);

			holdView.newset_fragment_item_votesUsersLayout
			.setVisibility(View.GONE);
			holdView.newset_fragment_item_blank.setVisibility(View.GONE);
			// if(isCommentVisity){
			holdView.newset_fragment_item_lineImage.setVisibility(View.GONE);
			holdView.newset_fragment_item_CVMLLayout.setVisibility(View.GONE);
			// }else{
			// holdView.newset_fragment_item_lineImage.setVisibility(View.VISIBLE);
			// holdView.newset_fragment_item_CVMLLayout.setVisibility(View.VISIBLE);
			// }
		}
		// String level = itemBean.getLevel();
		// if(level.equals("0")){
		// holdView.newset_fragment_item_clockLevelLayout.setBackgroundColor(context.getResources().getColor(R.color.meibao_color_5));
		// }else if(level.equals("1")){
		// holdView.newset_fragment_item_clockLevelLayout.setBackgroundColor(context.getResources().getColor(R.color.meibao_color_4));
		// }else{
		// holdView.newset_fragment_item_clockLevelLayout.setBackgroundColor(context.getResources().getColor(R.color.meibao_color_3));
		// }

		// if(isHaveImage){
		// holdView.newset_fragment_item_imagebottomblank.setVisibility(View.VISIBLE);
		// }else{
		// holdView.newset_fragment_item_imagebottomblank.setVisibility(View.GONE);
		// }图片下的Layout
		if (isHaveImage) {
			holdView.newset_fragment_item_clockContent_BottomBlackLayout
			.setVisibility(View.VISIBLE);
		} else {
			holdView.newset_fragment_item_clockContent_BottomBlackLayout
			.setVisibility(View.GONE);
		}
		if (!isHaveImage && !isHaveSingnature) {
			holdView.newset_fragment_item_clockContent.setText("打卡1次");
			holdView.newset_fragment_item_clockContent
			.setVisibility(View.VISIBLE);
		}

		holdView.newset_fragment_item_CVMLLayout.setVisibility(View.GONE);// 隐藏点赞和评论
		holdView.newset_fragment_item_clockLevelLayout
		.setBackgroundResource(R.drawable.newest_fragment_item_center_shape);
		if (isHaveImage || isHaveQuantity || isHaveSingnature) {
			holdView.newset_fragment_item_IQSLayout.setVisibility(View.VISIBLE);
			// if(level.equals("0")){
			// holdView.newset_fragment_item_clockLevelLayout.setBackgroundColor(context.getResources().getColor(R.color.meibao_color_5));
			// }else if(level.equals("1")){
			// holdView.newset_fragment_item_clockLevelLayout.setBackgroundColor(context.getResources().getColor(R.color.meibao_color_4));
			// }else{
			// holdView.newset_fragment_item_clockLevelLayout.setBackgroundColor(context.getResources().getColor(R.color.meibao_color_3));
			// }

		} else {
			// if(level.equals("0")){
			// holdView.newset_fragment_item_clockLevelLayout.setBackgroundResource(R.drawable.building_level1_shape);
			// }else if(level.equals("1")){
			// holdView.newset_fragment_item_clockLevelLayout.setBackgroundResource(R.drawable.building_level2_shape);
			// }else{
			// holdView.newset_fragment_item_clockLevelLayout.setBackgroundResource(R.drawable.building_level3_shape);
			// }
			// holdView.newset_fragment_item_clockLevelLayout.setBackgroundResource(R.drawable.building_levelall_shape);
		}
		//		holdView.newset_fragment_item_name
		//				.setOnClickListener(new OnClickListener() {
		//
		//					@Override
		//					public void onClick(View arg0) {
		//						// TODO Auto-generated method stub
		//						// GroupMainFragmentActivity.isReflush =
		//						// true;//让主activity 不刷新fragment
		//						if (userID.equals(itemBean.getUserId())) {
		//							Intent in = new Intent(context,
		//									ClockMyActivity.class);
		//							context.startActivity(in);
		//						} else {
		//							Intent in = new Intent(context,
		//									ClockOtherUserActivity.class);
		//							in.putExtra("userid", itemBean.getUserId());
		//							context.startActivity(in);
		//						}
		//					}
		//				});
		//
		//		holdView.newset_fragment_item_votesUsersContent
		//				.setOnClickListener(new OnClickListener() {
		//					// 点赞详情
		//					@Override
		//					public void onClick(View arg0) {
		//						// TODO Auto-generated method stub
		//						Intent in = new Intent(context, ClockVoteActivity.class);
		//						in.putExtra("jobid", itemBean.getJobId());
		//						in.putExtra("taskId", itemBean.getTaskId());
		//						context.startActivity(in);
		//					}
		//				});
		//		holdView.newset_fragment_item_commentsContentText1
		//				.setOnClickListener(new OnClickListener() {
		//
		//					@Override
		//					public void onClick(View arg0) {
		//						// TODO Auto-generated method stub
		//						Intent in = new Intent(context,
		//								ClockCommentActivity.class);
		//						in.putExtra("jobid", itemBean.getJobId());
		//						in.putExtra("createUserID", itemBean.getUserId());
		//						in.putExtra("taskId", itemBean.getTaskId());
		//						context.startActivity(in);
		//					}
		//				});
		//		holdView.newset_fragment_item_commentsContentText2
		//				.setOnClickListener(new OnClickListener() {
		//
		//					@Override
		//					public void onClick(View arg0) {
		//						// TODO Auto-generated method stub
		//						Intent in = new Intent(context,
		//								ClockCommentActivity.class);
		//						in.putExtra("jobid", itemBean.getJobId());
		//						in.putExtra("createUserID", itemBean.getUserId());
		//						in.putExtra("taskId", itemBean.getTaskId());
		//						context.startActivity(in);
		//					}
		//				});
		//		holdView.newset_fragment_item_commentsContentMore
		//				.setOnClickListener(new OnClickListener() {
		//					// 评论详情
		//					@Override
		//					public void onClick(View arg0) {
		//						// TODO Auto-generated method stub
		//						Intent in = new Intent(context,
		//								ClockCommentActivity.class);
		//						in.putExtra("jobid", itemBean.getJobId());
		//						in.putExtra("createUserID", itemBean.getUserId());
		//						in.putExtra("taskId", itemBean.getTaskId());
		//						context.startActivity(in);
		//					}
		//				});
		// holdView.newset_fragment_item_votesNumLayout.setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// buildingOnClick.clickVote(itemBean.getJobId(),position,itemBean.getVoted());
		// }
		// });
		// holdView.newset_fragment_item_commentsNumLayout.setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// buildingOnClick.clickComment(itemBean.getJobId(),position);
		// }
		// });
		holdView.newset_fragment_item_clockContentLayout
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (fragmentItemClick != null)
					fragmentItemClick.itemClick(position);
			}
		});
	}

	public class HoldView {
		// public ImageView newUserHead,
		// new_chat_photo1,new_chat_photo2,new_chat_photo3;
		// public TextView newUserName, newUserAge, newUserLevel,
		// newUserDistance,
		// new_chat_text, newGroupName, newGroupPeopleNum;
		TextView newset_fragment_item_name, newset_fragment_item_currentStreak,
		newset_fragment_item_clockName,
		newset_fragment_item_clockContent;// 上部分的id

		TextView newset_fragment_item_TimeText,
		newset_fragment_item_votesNumText,
		newset_fragment_item_commentsNumText;// 计数的id

		TextView newset_fragment_item_votesUsersContent;// 点赞的人的id

		TextView newset_fragment_item_commentsContentUserText1,
		newset_fragment_item_commentsContentText1;// 评论1
		TextView newset_fragment_item_commentsContentUserText2,
		newset_fragment_item_commentsContentText2;// 评论2
		TextView newset_fragment_item_commentsContentMore,
		newset_fragment_item_clockQuantity;

		ImageView newset_fragment_item_headImage,
		newset_fragment_item_ImageLayout_Image1,
		newset_fragment_item_ImageLayout_Image2,
		newset_fragment_item_ImageLayout_only1,
		newset_fragment_item_ImageLayout_Image3,
		newset_fragment_item_commentsContentUserbeforeimage2,
		newset_fragment_item_commentsContentUserbeforeimage1,
		newset_fragment_item_votesNumImage,
		newset_fragment_item_lineImage, newset_fragment_item_yisi,
		newset_fragment_item_medalImage;
		LinearLayout newset_fragment_item_ImageLayout,
		newset_fragment_item_commentsContentLayout1,
		newset_fragment_item_commentsContentLayout2,
		newset_fragment_item_votesNumLayout,
		newset_fragment_item_commentsNumLayout,
		newset_fragment_item_clockContentLayout,
		newset_fragment_item_blank,
		newset_fragment_item_blankTopLayout,
		newset_fragment_item_blankTopLayout1,
		newset_fragment_item_dayLayout;
		RelativeLayout newset_fragment_item_votesUsersLayout,
		newset_fragment_item_IQSLayout,
		newset_fragment_item_imagebottomblank,
		newset_fragment_item_CVMLLayout,
		newset_fragment_item_clockQuantityLayout,
		newset_fragment_item_clockContent_BottomBlackLayout,
		newset_fragment_item_topLayout,
		newset_fragment_item_clockLevelLayout, newset_fragment_item;

		public HoldView(View convertView) {
			newset_fragment_item_name = (TextView) convertView
					.findViewById(R.id.newset_fragment_item_name);
			newset_fragment_item_currentStreak = (TextView) convertView
					.findViewById(R.id.newset_fragment_item_currentStreak);
			newset_fragment_item_clockName = (TextView) convertView
					.findViewById(R.id.newset_fragment_item_clockName);
			newset_fragment_item_clockContent = (TextView) convertView
					.findViewById(R.id.newset_fragment_item_clockContent);

			newset_fragment_item_TimeText = (TextView) convertView
					.findViewById(R.id.newset_fragment_item_TimeText);
			newset_fragment_item_votesNumText = (TextView) convertView
					.findViewById(R.id.newset_fragment_item_votesNumText);
			newset_fragment_item_commentsNumText = (TextView) convertView
					.findViewById(R.id.newset_fragment_item_commentsNumText);

			newset_fragment_item_votesUsersContent = (TextView) convertView
					.findViewById(R.id.newset_fragment_item_votesUsersContent);

			newset_fragment_item_commentsContentUserText1 = (TextView) convertView
					.findViewById(R.id.newset_fragment_item_commentsContentUserText1);
			newset_fragment_item_commentsContentText1 = (TextView) convertView
					.findViewById(R.id.newset_fragment_item_commentsContentText1);
			newset_fragment_item_commentsContentUserText2 = (TextView) convertView
					.findViewById(R.id.newset_fragment_item_commentsContentUserText2);
			newset_fragment_item_commentsContentText2 = (TextView) convertView
					.findViewById(R.id.newset_fragment_item_commentsContentText2);

			newset_fragment_item_commentsContentMore = (TextView) convertView
					.findViewById(R.id.newset_fragment_item_commentsContentMore);
			newset_fragment_item_clockQuantity = (TextView) convertView
					.findViewById(R.id.newset_fragment_item_clockQuantity);

			newset_fragment_item_headImage = (ImageView) convertView
					.findViewById(R.id.newset_fragment_item_headImage);
			newset_fragment_item_ImageLayout_only1 = (ImageView) convertView
					.findViewById(R.id.newset_fragment_item_ImageLayout_only1);

			newset_fragment_item_ImageLayout_Image1 = (ImageView) convertView
					.findViewById(R.id.newset_fragment_item_ImageLayout_Image1);
			newset_fragment_item_ImageLayout_Image2 = (ImageView) convertView
					.findViewById(R.id.newset_fragment_item_ImageLayout_Image2);
			newset_fragment_item_ImageLayout_Image3 = (ImageView) convertView
					.findViewById(R.id.newset_fragment_item_ImageLayout_Image3);
			newset_fragment_item_commentsContentUserbeforeimage1 = (ImageView) convertView
					.findViewById(R.id.newset_fragment_item_commentsContentUserbeforeimage1);
			newset_fragment_item_commentsContentUserbeforeimage2 = (ImageView) convertView
					.findViewById(R.id.newset_fragment_item_commentsContentUserbeforeimage2);
			newset_fragment_item_votesNumImage = (ImageView) convertView
					.findViewById(R.id.newset_fragment_item_votesNumImage);
			newset_fragment_item_lineImage = (ImageView) convertView
					.findViewById(R.id.newset_fragment_item_lineImage);
			newset_fragment_item_yisi = (ImageView) convertView
					.findViewById(R.id.newset_fragment_item_yisi);
			newset_fragment_item_medalImage = (ImageView) convertView
					.findViewById(R.id.newset_fragment_item_medalImage);

			newset_fragment_item_ImageLayout = (LinearLayout) convertView
					.findViewById(R.id.newset_fragment_item_ImageLayout);
			newset_fragment_item_commentsContentLayout1 = (LinearLayout) convertView
					.findViewById(R.id.newset_fragment_item_commentsContentLayout1);
			newset_fragment_item_commentsContentLayout2 = (LinearLayout) convertView
					.findViewById(R.id.newset_fragment_item_commentsContentLayout2);
			newset_fragment_item_votesNumLayout = (LinearLayout) convertView
					.findViewById(R.id.newset_fragment_item_votesNumLayout);
			newset_fragment_item_commentsNumLayout = (LinearLayout) convertView
					.findViewById(R.id.newset_fragment_item_commentsNumLayout);
			newset_fragment_item_clockContentLayout = (LinearLayout) convertView
					.findViewById(R.id.newset_fragment_item_clockContentLayout);
			newset_fragment_item_blank = (LinearLayout) convertView
					.findViewById(R.id.newset_fragment_item_blank);
			newset_fragment_item_blankTopLayout = (LinearLayout) convertView
					.findViewById(R.id.newset_fragment_item_blankTopLayout);
			newset_fragment_item_blankTopLayout1 = (LinearLayout) convertView
					.findViewById(R.id.newset_fragment_item_blankTopLayout1);
			newset_fragment_item_dayLayout = (LinearLayout) convertView
					.findViewById(R.id.newset_fragment_item_dayLayout);

			newset_fragment_item_votesUsersLayout = (RelativeLayout) convertView
					.findViewById(R.id.newset_fragment_item_votesUsersLayout);
			newset_fragment_item_IQSLayout = (RelativeLayout) convertView
					.findViewById(R.id.newset_fragment_item_IQSLayout);
			newset_fragment_item_imagebottomblank = (RelativeLayout) convertView
					.findViewById(R.id.newset_fragment_item_imagebottomblank);
			newset_fragment_item_CVMLLayout = (RelativeLayout) convertView
					.findViewById(R.id.newset_fragment_item_CVMLLayout);
			newset_fragment_item_clockQuantityLayout = (RelativeLayout) convertView
					.findViewById(R.id.newset_fragment_item_clockQuantityLayout);
			newset_fragment_item_clockContent_BottomBlackLayout = (RelativeLayout) convertView
					.findViewById(R.id.newset_fragment_item_clockContent_BottomBlackLayout);
			newset_fragment_item_topLayout = (RelativeLayout) convertView
					.findViewById(R.id.newset_fragment_item_topLayout);
			newset_fragment_item_clockLevelLayout = (RelativeLayout) convertView
					.findViewById(R.id.newset_fragment_item_clockLevelLayout);
			newset_fragment_item = (RelativeLayout) convertView
					.findViewById(R.id.newset_fragment_item);
			// newUserHead = (ImageView) convertView
			// .findViewById(R.id.newUserHead);
			// new_chat_photo1 = (ImageView) convertView
			// .findViewById(R.id.new_chat_photo1);
			// new_chat_photo2 = (ImageView) convertView
			// .findViewById(R.id.new_chat_photo2);
			// new_chat_photo3 = (ImageView) convertView
			// .findViewById(R.id.new_chat_photo3);
			//
			// newUserName = (TextView)
			// convertView.findViewById(R.id.newUserName);
			// newUserAge = (TextView)
			// convertView.findViewById(R.id.newUserAge);
			// newUserLevel = (TextView) convertView
			// .findViewById(R.id.newUserLevel);
			// newUserDistance = (TextView) convertView
			// .findViewById(R.id.newUserDistance);
			// new_chat_text = (TextView) convertView
			// .findViewById(R.id.new_chat_text);
			// newGroupName = (TextView) convertView
			// .findViewById(R.id.newGroupName);
			// newGroupPeopleNum = (TextView) convertView
			// .findViewById(R.id.newGroupPeopleNum);
		}
	}

	public interface BuildingOnClick {
		/**
		 * 点击评论
		 * */
		public void clickComment(String jobid, int position);

		/**
		 * 点赞
		 */
		public void clickVote(String jobid, int position, String type);
	}

	public interface OnNewFragmentItemClick {
		public void itemClick(int position);
	}

	private void imageBrower(int position, String[] urls, String[] imageSource) {
		Intent intent = new Intent(context, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_SOURCE, imageSource);

		context.startActivity(intent);
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

}
