package com.yktx.check.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
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
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.R;
import com.yktx.check.TaskInfoActivity;
import com.yktx.check.bean.MsgToUserListBean;
import com.yktx.check.square.fragment.NewFragment;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;

public class DynamicFragmentListViewAdapter extends BaseAdapter{
	private ImageLoader imageLoader = ImageLoader.getInstance();
	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.zw_touxiang)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(100))
	// 启用内存缓存
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	private Context mContext;
	ArrayList<MsgToUserListBean> msgToUserListBeans = new ArrayList<MsgToUserListBean>(10);
	private boolean isNullItem;
	private LayoutInflater inflater;
	public onMyItemClick thisMyItemClick;
	String userID;
	private SharedPreferences settings;
	public DynamicFragmentListViewAdapter(Context mContext,String userID) {
		super();
		this.mContext = mContext;
		this.userID = userID;
		inflater = LayoutInflater.from(mContext);
		settings = mContext.getSharedPreferences("clock", mContext.MODE_PRIVATE);
	}
	public void setMyItemClick(onMyItemClick myItemClick){
		thisMyItemClick = myItemClick;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(msgToUserListBeans.size() == 0){
			isNullItem = true;
			return 1;
		}
		isNullItem = false;
		return msgToUserListBeans.size();
	}
	public void setList(ArrayList<MsgToUserListBean> list){
		msgToUserListBeans = list;
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
		Tools.getLog(Tools.d, "aaa", "isNullItem=="+isNullItem);
		ViewHolder holder;
		if(isNullItem){
			View v = inflater.inflate(R.layout.image_null_item, null); 
			return v;
		}
		//			holder = (ViewHolder) convertView.getTag();
		if(convertView == null || convertView.getTag() == null){
			convertView = inflater.inflate(R.layout.dynamic_fragment_listview_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder  = (ViewHolder) convertView.getTag();
		}
		MsgToUserListBean bean = msgToUserListBeans.get(position);
		showView(holder, position, bean);
		return convertView;
	}
	private void showView(ViewHolder holder,final int position,final MsgToUserListBean bean){
		final String type = bean.getType();
		//		if(type.equals("2")||type.equals("3")||type.equals("8")||type.equals("6")||type.equals("12")||type.equals("13")){
		//			String ImageUrl = Tools.getImagePath(bean.getImageSource()) + settings.getString("userheadimage", null);
		//			imageLoader.displayImage(
		//					ImageUrl,
		//					holder.headImage,
		//					headOptions);
		//		}else{
		imageLoader.displayImage(
				Tools.getImagePath(bean.getImageSource())+ bean.getAvatarPath()+(bean.getImageSource() == 2?"?imageMogr2/thumbnail/50x50":""),
				holder.headImage,
				headOptions);

		//		}

		holder.headImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//				if(type.equals("2")||type.equals("3")||type.equals("8")||type.equals("6")){
				//					Intent in = new Intent(mContext,
				//							ClockMyActivity.class);
				//					mContext.startActivity(in);
				//				}else{
				Intent in = new Intent(mContext, ClockOtherUserActivity.class);
				in.putExtra("userid", bean.getUserId());
				mContext.startActivity(in);
				//				}
			}
		});
		String point = bean.getPoint();
		holder.DynamicFragment_listview_item_addNumText.setText("+"+bean.getPoint());//加分数
		if(point.equals("1")){
			holder.DynamicFragment_listview_item_addNumText.setTextColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.DynamicFragment_listview_item_addNumImage.setImageResource(R.drawable.xx_1fen);
		}else if(point.equals("2")){
			holder.DynamicFragment_listview_item_addNumText.setTextColor(mContext.getResources().getColor(R.color.meibao_color_4));
			holder.DynamicFragment_listview_item_addNumImage.setImageResource(R.drawable.xx_2fen);
		}else if(point.equals("3")){
			holder.DynamicFragment_listview_item_addNumText.setTextColor(mContext.getResources().getColor(R.color.meibao_color_3));
			holder.DynamicFragment_listview_item_addNumImage.setImageResource(R.drawable.xx_3fen);
		}else if(point.equals("10")){
			holder.DynamicFragment_listview_item_addNumText.setTextColor(mContext.getResources().getColor(R.color.meibao_color_16));
			holder.DynamicFragment_listview_item_addNumImage.setImageResource(R.drawable.xx_10fen);
		}else if(point.equals("30")){
			holder.DynamicFragment_listview_item_addNumText.setTextColor(mContext.getResources().getColor(R.color.meibao_color_17));
			holder.DynamicFragment_listview_item_addNumImage.setImageResource(R.drawable.xx_30fen);
		}

		holder.name.setText("");//为了刷新不重复，因为是append进去的
		holder.commentContent.setText("");
		holder.DynamicFragment_listview_item_commentExclusive.setText("");
		SpannableString msp;    	

		//		holder.qiqiuBg.setBackgroundResource(R.drawable.shape_login);
		if(type.equals("3")||type.equals("5")){
			//点赞
			String vTime = TimeUtil.getTimes(Long.parseLong(bean.getcTime()));
			String vName = "你";
			String vOtherName = bean.getUserName();
			String vTitle = "#"+bean.getTitle().trim()+"#";
			String vLeftTitle = "给";
			String vRightTitle = "打气了";
			String vBlank = " ";
			String content = "";
			String content2 = "";
			String v1 = "";
			if(type.equals("3")){
				//				content = vName+vLeftTitle+vBlank+vOtherName+vTitle+vRightTitle+vBlank+vBlank+vTime;
				content = vName+vLeftTitle+vBlank+vBlank+vOtherName+vBlank+vBlank+vRightTitle;
			}else if(type.equals("5")){
				//				content = vOtherName+vBlank+vLeftTitle+vName+vTitle+vRightTitle+vBlank+vBlank+vTime;
				content = vOtherName+vBlank+vBlank+vLeftTitle+vName+vRightTitle;
			}

			msp = new SpannableString(ToDBC(content));
			if(type.equals("5")){
				msp.setSpan(new ClickableSpan() {
					@Override
					public void updateDrawState(TextPaint ds) {
						// TODO Auto-generated method stub
						super.updateDrawState(ds);
						ds.setColor(mContext.getResources().getColor(R.color.meibao_color_14));//设置文件颜色
						ds.setUnderlineText(false);      //设置下划线
					}
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						//					GroupMainFragmentActivity.isReflush = true;//让主activity 不刷新fragment
						if(userID.equals(bean.getUserId())){
							Intent in = new Intent(mContext,
									ClockMyActivity.class);
							mContext.startActivity(in);
						}else{
							Intent in = new Intent(mContext, ClockOtherUserActivity.class);
							in.putExtra("userid", bean.getUserId());
							mContext.startActivity(in);
						}

					}
				}, 0, vOtherName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(type.equals("3")){
				v1 = vName+vLeftTitle+vBlank+vBlank;
				msp.setSpan(new ClickableSpan() {
					@Override
					public void updateDrawState(TextPaint ds) {
						// TODO Auto-generated method stub
						super.updateDrawState(ds);
						ds.setColor(mContext.getResources().getColor(R.color.meibao_color_14));//设置文件颜色
						ds.setUnderlineText(false);      //设置下划线
					}
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						//					GroupMainFragmentActivity.isReflush = true;//让主activity 不刷新fragment
						if(userID.equals(bean.getUserId())){
							Intent in = new Intent(mContext,
									ClockMyActivity.class);
							mContext.startActivity(in);
						}else{
							Intent in = new Intent(mContext, ClockOtherUserActivity.class);
							in.putExtra("userid", bean.getUserId());
							mContext.startActivity(in);
						}

					}
				}, v1.length(), v1.length()+vOtherName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			holder.name.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
			holder.name.append(msp);
			holder.name.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件

			content2 = vTitle+vBlank+vBlank+vTime;
			msp = new SpannableString(ToDBC(content2));
			msp.setSpan(new ClickableSpan() {
				@Override
				public void updateDrawState(TextPaint ds) {
					// TODO Auto-generated method stub
					super.updateDrawState(ds);
					ds.setColor(mContext.getResources().getColor(R.color.meibao_color_9));       //设置文件颜色
					ds.setUnderlineText(false);      //设置下划线
				}
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//					GroupMainFragmentActivity.ReflushItem = 0;  
					NewFragment.isNewLoadAgain = true;
					//					GroupMainFragmentActivity.isReflush = false;//让主activity 刷新fragment
					//					Intent in = new Intent(mContext, ClockGroupInfoActivity.class);
					//					in.putExtra("buildingId", bean.getBuildingId());
					//					mContext.startActivity(in);
					Intent in = new Intent(mContext, TaskInfoActivity.class);
					in.putExtra("taskid", bean.getTaskId());
					in.putExtra("userid", bean.getUserId());
					mContext.startActivity(in);
				}
			},0,  vTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			//设置时间的字体大小
			msp.setSpan(new AbsoluteSizeSpan(12,true), content2.length()-vTime.length(), content2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置时间的字体颜色
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_11))
			,content2.length()-vTime.length(), content2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色


			holder.commentContent.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
			holder.commentContent.append(msp);
			holder.commentContent.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件

			holder.commentContent.setVisibility(View.VISIBLE);
			holder.qiqiuBg.setVisibility(View.VISIBLE);
			holder.DynamicFragment_listview_item_commentExclusive.setVisibility(View.GONE);

		}else if(type.equals("6")||type.equals("7")){//关注

			String fName = "你";
			String fOtherName = bean.getUserName();
			String fLeftTitle = "关注了";
			String fBlank = " ";
			String fContent = "";
			String f1 = "";
			String fTime = TimeUtil.getTimes(Long.parseLong(bean.getcTime()));
			if(type.equals("6")){
				fContent = fName+fLeftTitle+fBlank+fBlank+fOtherName+fBlank+fBlank+fTime;
			}else{
				fContent = fOtherName+fBlank+fBlank+fLeftTitle+fName+fBlank+fBlank+fTime;
			}

			msp = new SpannableString(ToDBC(fContent));
			if(type.equals("7")){
				msp.setSpan(new ClickableSpan() {
					@Override
					public void updateDrawState(TextPaint ds) {
						// TODO Auto-generated method stub
						super.updateDrawState(ds);
						ds.setColor(mContext.getResources().getColor(R.color.meibao_color_9));       //设置文件颜色
						ds.setUnderlineText(false);      //设置下划线
					}
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent in = new Intent(mContext, ClockOtherUserActivity.class);
						in.putExtra("userid", bean.getUserId());
						mContext.startActivity(in);
					}
				}, 0, fOtherName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}else if(type.equals("6")){
				f1 = fName+fBlank+fBlank+fLeftTitle;
				msp.setSpan(new ClickableSpan() {
					@Override
					public void updateDrawState(TextPaint ds) {
						// TODO Auto-generated method stub
						super.updateDrawState(ds);
						ds.setColor(mContext.getResources().getColor(R.color.meibao_color_9));       //设置文件颜色
						ds.setUnderlineText(false);      //设置下划线
					}
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent in = new Intent(mContext, ClockOtherUserActivity.class);
						in.putExtra("userid", bean.getUserId());
						mContext.startActivity(in);
					}
				}, f1.length(), f1.length()+fOtherName.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			//设置时间的字体大小
			msp.setSpan(new AbsoluteSizeSpan(12,true), fContent.length()-fTime.length(), fContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置时间的字体颜色
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_11))
			,fContent.length()-fTime.length(), fContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色

			holder.name.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
			holder.name.append(msp);
			holder.name.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
			holder.commentContent.setVisibility(View.GONE);
			holder.qiqiuBg.setVisibility(View.VISIBLE);
			holder.DynamicFragment_listview_item_commentExclusive.setVisibility(View.GONE);

		}else if(type.equals("12")){//今日精品
			String Ttitle = "#"+bean.getTitle()+"#";
			String TTime = TimeUtil.getTimes(Long.parseLong(bean.getcTime()));
			String Tblack = " ";
			String Tcontent = "你的打卡被小编推荐为精品";
			String Tcontent2 = Ttitle+Tblack+Tblack+TTime;
			holder.name.append(Tcontent);
			msp = new SpannableString(ToDBC(Tcontent2));
			msp.setSpan(new ClickableSpan() {
				@Override
				public void updateDrawState(TextPaint ds) {
					// TODO Auto-generated method stub
					super.updateDrawState(ds);
					ds.setColor(mContext.getResources().getColor(R.color.meibao_color_9));       //设置文件颜色
					ds.setUnderlineText(false);      //设置下划线
				}
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent in = new Intent(mContext, TaskInfoActivity.class);
					in.putExtra("taskid", bean.getTaskId());
					in.putExtra("userid", bean.getUserId());
					mContext.startActivity(in);
				}
			}, 0, Ttitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置时间的字体大小
			msp.setSpan(new AbsoluteSizeSpan(12,true), Tcontent2.length()-TTime.length(), Tcontent2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置时间的字体颜色
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_11))
			,Tcontent2.length()-TTime.length(), Tcontent2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
			holder.commentContent.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
			holder.commentContent.append(msp);
			holder.commentContent.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
			holder.commentContent.setVisibility(View.VISIBLE);
			holder.qiqiuBg.setVisibility(View.VISIBLE);
			holder.DynamicFragment_listview_item_commentExclusive.setVisibility(View.GONE);

		}else if(type.equals("14")||type.equals("15")){
			String Aright1 = "提醒你要记得坚持哦,加油!"; 
			String Aright2 = "接受了你的提醒并打卡,你获得气球奖励!"; 
			String AotherName = bean.getUserName();
			String ABlank = " ";
			String Atitle = "#"+bean.getTitle()+"#";
			String ATime = TimeUtil.getTimes(Long.parseLong(bean.getcTime()));

			String content = "";
			StringBuilder content2 = new StringBuilder();
			if(type.equals("14")){
				content = AotherName+ABlank+ABlank+Aright1+ABlank+ABlank+ATime;
				holder.qiqiuBg.setVisibility(View.GONE);
			}else if(type.equals("15")){
				holder.qiqiuBg.setVisibility(View.VISIBLE);
				content = AotherName+ABlank+ABlank+Aright2+ABlank+ABlank+ATime;
			}

			msp = new SpannableString(ToDBC(content));
			msp.setSpan(new ClickableSpan() {
				@Override
				public void updateDrawState(TextPaint ds) {
					// TODO Auto-generated method stub
					super.updateDrawState(ds);
					ds.setColor(mContext.getResources().getColor(R.color.meibao_color_14));       //设置文件颜色
					ds.setUnderlineText(false);      //设置下划线
				}
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent in = new Intent(mContext, ClockOtherUserActivity.class);
					in.putExtra("userid", bean.getUserId());
					mContext.startActivity(in);
				}
			}, 0, AotherName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置时间的字体大小
			msp.setSpan(new AbsoluteSizeSpan(12,true), content.length()-ATime.length(), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置时间的字体颜色
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_11))
			,content.length()-ATime.length(), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
			holder.name.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
			holder.name.append(msp);
			holder.name.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
			if(type.equals("14")){
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.xx_qiqiu);
				ImageSpan imageSpan1 = new ImageSpan(mContext, bitmap);

				content2.append("( 如果你打卡  ");
				content2.append("Ta");
				content2.append("将获得 +10的奖励 )");
				msp = new SpannableString(ToDBC(content2.toString()));
				//				msp.setSpan(new ClickableSpan() {
				//					@Override
				//					public void updateDrawState(TextPaint ds) {
				//						// TODO Auto-generated method stub
				//						super.updateDrawState(ds);
				//						ds.setColor(mContext.getResources().getColor(R.color.meibao_color_9));       //设置文件颜色
				//						ds.setUnderlineText(false);      //设置下划线
				//					}
				//					@Override
				//					public void onClick(View arg0) {
				//						// TODO Auto-generated method stub
				//						Intent in = new Intent(mContext, TaskInfoActivity.class);
				//						in.putExtra("taskid", bean.getTaskId());
				//						mContext.startActivity(in);
				//					}
				//				}, 0, ATime.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				msp.setSpan(imageSpan1,content2.toString().indexOf("将获得")+2,
						content2.toString().indexOf("将获得")+3,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				//				设置时间的字体大小
				msp.setSpan(new AbsoluteSizeSpan(12,true), 0, content2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				//设置时间的字体颜色
				msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_9))
				,0, content2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
				holder.commentContent.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
				holder.commentContent.append(msp);
				holder.commentContent.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
				holder.commentContent.setVisibility(View.VISIBLE);
			}else if(type.equals("15")){
				holder.commentContent.setVisibility(View.GONE);
			}
			holder.DynamicFragment_listview_item_commentExclusive.setVisibility(View.GONE);


		}else if(type.equals("16")){//置顶
			String Stitle = " #"+bean.getTitle()+"# ";
			String Sblack = " ";
			String STime = TimeUtil.getTimes(Long.parseLong(bean.getcTime()));
			holder.name.append("你的打卡被小编推荐为   精品置顶");
			
			String Tcontent2 = Stitle+Sblack+Sblack+STime;
			msp = new SpannableString(ToDBC(Tcontent2));
			msp.setSpan(new ClickableSpan() {
				@Override
				public void updateDrawState(TextPaint ds) {
					// TODO Auto-generated method stub
					super.updateDrawState(ds);
					ds.setColor(mContext.getResources().getColor(R.color.meibao_color_9));       //设置文件颜色
					ds.setUnderlineText(false);      //设置下划线
				}
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent in = new Intent(mContext, TaskInfoActivity.class);
					in.putExtra("taskid", bean.getTaskId());
					in.putExtra("userid", bean.getUserId());
					mContext.startActivity(in);
				}
			}, 0, Stitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置时间的字体大小
			msp.setSpan(new AbsoluteSizeSpan(12,true), Tcontent2.length()-STime.length(), Tcontent2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置时间的字体颜色
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_11))
			,Tcontent2.length()-STime.length(), Tcontent2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
			holder.commentContent.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
			holder.commentContent.append(msp);
			holder.commentContent.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
			holder.commentContent.setVisibility(View.VISIBLE);
			holder.qiqiuBg.setVisibility(View.VISIBLE);
			holder.DynamicFragment_listview_item_commentExclusive.setVisibility(View.GONE);
			
			
		}else {
			//评论
			String cName = "你";
			String cOtherName =  bean.getUserName();
			String cOtherName1 =  bean.getJobOwnerName();
			String cBlank = " ";
			String cTitle = "#"+bean.getTitle().trim()+"#";
			String cLeftTitle = "评论了";
			String cRightTitle = "提到了";
			String cTidao = "@";

			String content = "";
			String content2 = "";

			String c1 =  cName+cLeftTitle;



			String cComment = bean.getText();
			String cTime = TimeUtil.getTimes(Long.parseLong(bean.getcTime()));
			String c2 = cComment+"    "+cTime;

			if(type.equals("4")||type.equals("2")||type.equals("11")){
				if(type.equals("2")){
					//					content = cName+cLeftTitle+cBlank+cOtherName+cTitle;
					content = cName+cLeftTitle+cBlank+cBlank+cOtherName;

				}else if(type.equals("4")){
					//					content = cOtherName+cBlank+cLeftTitle+cName+cTitle;
					content = cOtherName+cBlank+cBlank+cLeftTitle+cName;
				}else if(type.equals("11")){
					cOtherName = "有人";
					content = cOtherName+cLeftTitle+cName;
				}

				msp = new SpannableString(ToDBC(content));

				//第一个名字
				if(type.equals("4")){
					msp.setSpan(new ClickableSpan() {
						@Override
						public void updateDrawState(TextPaint ds) {
							// TODO Auto-generated method stub
							super.updateDrawState(ds);
							ds.setColor(mContext.getResources().getColor(R.color.meibao_color_14));       //设置文件颜色
							ds.setUnderlineText(false);      //设置下划线
						}
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent in = new Intent(mContext, ClockOtherUserActivity.class);
							in.putExtra("userid", bean.getUserId());
							mContext.startActivity(in);
						}
					}, 0, cOtherName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}

				//第二个名字

				if(type.equals("2")){
					c1 = cName+cLeftTitle+cBlank+cBlank;
					msp.setSpan(new ClickableSpan() {
						@Override
						public void updateDrawState(TextPaint ds) {
							// TODO Auto-generated method stub
							super.updateDrawState(ds);
							ds.setColor(mContext.getResources().getColor(R.color.meibao_color_14));       //设置文件颜色
							ds.setUnderlineText(false);      //设置下划线
						}
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent in = new Intent(mContext, ClockOtherUserActivity.class);
							in.putExtra("userid", bean.getUserId());
							mContext.startActivity(in);
						}
					}, c1.length(),c1.length()+cOtherName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}

				//title 名字可点绿色   从后往前
				//
				//				msp.setSpan(new ClickableSpan() {
				//					@Override
				//					public void updateDrawState(TextPaint ds) {
				//						// TODO Auto-generated method stub
				//
				//						super.updateDrawState(ds);
				//						ds.setColor(mContext.getResources().getColor(R.color.meibao_color_9));       //设置文件颜色
				//						ds.setUnderlineText(false);      //设置下划线
				//					}
				//					@Override
				//					public void onClick(View arg0) {
				//						// TODO Auto-generated method stub
				//						//					GroupMainFragmentActivity.ReflushItem = 0; 
				//						NewFragment.isNewLoadAgain = true;
				//						//					GroupMainFragmentActivity.isReflush = false;//让主activity 刷新fragment
				//						//					Intent in = new Intent(mContext, ClockGroupInfoActivity.class);
				//						//					in.putExtra("buildingId", bean.getBuildingId());
				//						//					mContext.startActivity(in);
				//						Intent in = new Intent(mContext, TaskInfoActivity.class);
				//						in.putExtra("taskid", bean.getTaskId());
				//						mContext.startActivity(in);
				//					}
				//				}, content.length()-cTitle.length()+2,  content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

				holder.name.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
				holder.name.append(msp);
				holder.name.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件

			}else if(type.equals("8")||type.equals("9")||type.equals("10")||type.equals("13")){
				if(type.equals("9")){
					//					content = cOtherName+cBlank+cLeftTitle+cName+cTitle+cRightTitle+cName;
					content = cOtherName+cBlank+cBlank+cTidao+cName;
				}else if(type.equals("8")){
					//					content = cName+cLeftTitle+cBlank+cOtherName1+cTitle+cRightTitle+cBlank+cOtherName;
					content = cName+cLeftTitle+cBlank+cBlank+cOtherName1+cBlank+cBlank+cTidao+cOtherName;
				}else if(type.equals("10")){
					//					content = cOtherName+cBlank+cLeftTitle+cOtherName1+cTitle+cRightTitle+cName;
					content = cOtherName+cBlank+cBlank+cLeftTitle+cBlank+cBlank+cOtherName1+cTidao+cName;
				}else if(type.equals("13")){
					content = cName +cBlank+cBlank+cTidao+cOtherName;
				}

				msp = new SpannableString(ToDBC(content));

				//判断第一个名字
				if(type.equals("9")||type.equals("10")){
					msp.setSpan(new ClickableSpan() {
						@Override
						public void updateDrawState(TextPaint ds) {
							// TODO Auto-generated method stub
							super.updateDrawState(ds);
							ds.setColor(mContext.getResources().getColor(R.color.meibao_color_14));       //设置文件颜色
							ds.setUnderlineText(false);      //设置下划线
						}
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							//					GroupMainFragmentActivity.isReflush = true;//让主activity 不刷新fragment
							Intent in = new Intent(mContext, ClockOtherUserActivity.class);
							in.putExtra("userid", bean.getUserId());
							mContext.startActivity(in);
						}
					}, 0, cOtherName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				//第二个名字
				if(type.equals("8")){
					c1 = cName+cLeftTitle+cBlank+cBlank;
					msp.setSpan(new ClickableSpan() {
						@Override
						public void updateDrawState(TextPaint ds) {
							// TODO Auto-generated method stub
							super.updateDrawState(ds);
							ds.setColor(mContext.getResources().getColor(R.color.meibao_color_14));       //设置文件颜色
							ds.setUnderlineText(false);      //设置下划线
						}
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							//					GroupMainFragmentActivity.isReflush = true;//让主activity 不刷新fragment
							Intent in = new Intent(mContext, ClockOtherUserActivity.class);
							in.putExtra("userid", bean.getUserId());
							mContext.startActivity(in);
						}
					}, c1.length(), c1.length()+cOtherName1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}else if(type.equals("10")){
					c1 = cName +cBlank+cBlank+cTidao;
					msp.setSpan(new ClickableSpan() {
						@Override
						public void updateDrawState(TextPaint ds) {
							// TODO Auto-generated method stub
							super.updateDrawState(ds);
							ds.setColor(mContext.getResources().getColor(R.color.meibao_color_14));       //设置文件颜色
							ds.setUnderlineText(false);      //设置下划线
						}
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							//					GroupMainFragmentActivity.isReflush = true;//让主activity 不刷新fragment
							Intent in = new Intent(mContext, ClockOtherUserActivity.class);
							in.putExtra("userid", bean.getUserId());
							mContext.startActivity(in);
						}
					}, c1.length(), c1.length()+cOtherName1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}else if(type.equals("13")){
					c1 = cName +cBlank+cBlank+cTidao;
					msp.setSpan(new ClickableSpan() {
						@Override
						public void updateDrawState(TextPaint ds) {
							// TODO Auto-generated method stub
							super.updateDrawState(ds);
							ds.setColor(mContext.getResources().getColor(R.color.meibao_color_14));       //设置文件颜色
							ds.setUnderlineText(false);      //设置下划线
						}
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							//					GroupMainFragmentActivity.isReflush = true;//让主activity 不刷新fragment
							Intent in = new Intent(mContext, ClockOtherUserActivity.class);
							in.putExtra("userid", bean.getUserId());
							mContext.startActivity(in);
						}
					}, c1.length(), c1.length()+cOtherName1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				//				//title 变绿，并且可以点击   从后 
				//				if(type.equals("9")||type.equals("10")){
				//					c1 = cTitle+cRightTitle+cName;
				//					msp.setSpan(new ClickableSpan() {
				//						@Override
				//						public void updateDrawState(TextPaint ds) {
				//							// TODO Auto-generated method stub
				//
				//							super.updateDrawState(ds);
				//							ds.setColor(mContext.getResources().getColor(R.color.meibao_color_9));       //设置文件颜色
				//							ds.setUnderlineText(false);      //设置下划线
				//						}
				//						@Override
				//						public void onClick(View arg0) {
				//							// TODO Auto-generated method stub
				//							//					GroupMainFragmentActivity.ReflushItem = 0; 
				//							NewFragment.isNewLoadAgain = true;
				//							//					GroupMainFragmentActivity.isReflush = false;//让主activity 刷新fragment
				//							//					Intent in = new Intent(mContext, ClockGroupInfoActivity.class);
				//							//					in.putExtra("buildingId", bean.getBuildingId());
				//							//					mContext.startActivity(in);
				//							Intent in = new Intent(mContext, TaskInfoActivity.class);
				//							in.putExtra("taskid", bean.getTaskId());
				//							mContext.startActivity(in);
				//						}
				//					}, content.length()-c1.length()+2,  content.length()-c1.length()+cTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				//				}else if(type.equals("8")){
				//					c1 = cTitle+cRightTitle+cBlank+cOtherName;
				//					msp.setSpan(new ClickableSpan() {
				//						@Override
				//						public void updateDrawState(TextPaint ds) {
				//							// TODO Auto-generated method stub
				//
				//							super.updateDrawState(ds);
				//							ds.setColor(mContext.getResources().getColor(R.color.meibao_color_9));       //设置文件颜色
				//							ds.setUnderlineText(false);      //设置下划线
				//						}
				//						@Override
				//						public void onClick(View arg0) {
				//							// TODO Auto-generated method stub
				//							//					GroupMainFragmentActivity.ReflushItem = 0; 
				//							NewFragment.isNewLoadAgain = true;
				//							//					GroupMainFragmentActivity.isReflush = false;//让主activity 刷新fragment
				//							//					Intent in = new Intent(mContext, ClockGroupInfoActivity.class);
				//							//					in.putExtra("buildingId", bean.getBuildingId());
				//							//					mContext.startActivity(in);
				//							Intent in = new Intent(mContext, TaskInfoActivity.class);
				//							in.putExtra("taskid", bean.getTaskId());
				//							mContext.startActivity(in);
				//						}
				//					}, content.length()-c1.length()+2,  content.length()-c1.length()+cTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				//				}


				//末尾的名字
				if(type.equals("8")){
					msp.setSpan(new ClickableSpan() {
						@Override
						public void updateDrawState(TextPaint ds) {
							// TODO Auto-generated method stub
							super.updateDrawState(ds);
							ds.setColor(mContext.getResources().getColor(R.color.meibao_color_14));       //设置文件颜色
							ds.setUnderlineText(false);      //设置下划线
						}
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							//					GroupMainFragmentActivity.isReflush = true;//让主activity 不刷新fragment
							Intent in = new Intent(mContext, ClockOtherUserActivity.class);
							in.putExtra("userid", bean.getUserId());
							mContext.startActivity(in);
						}
					}, content.length()-cOtherName.length(), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}


				holder.name.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
				holder.name.append(msp);
				holder.name.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
			}





			msp = new SpannableString(ToDBC(cTitle));
			msp.setSpan(new ClickableSpan() {
				@Override
				public void updateDrawState(TextPaint ds) {
					// TODO Auto-generated method stub

					super.updateDrawState(ds);
					ds.setColor(mContext.getResources().getColor(R.color.meibao_color_9));       //设置文件颜色
					ds.setUnderlineText(false);      //设置下划线
				}
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//					GroupMainFragmentActivity.ReflushItem = 0; 
					NewFragment.isNewLoadAgain = true;
					//					GroupMainFragmentActivity.isReflush = false;//让主activity 刷新fragment
					//					Intent in = new Intent(mContext, ClockGroupInfoActivity.class);
					//					in.putExtra("buildingId", bean.getBuildingId());
					//					mContext.startActivity(in);
					Intent in = new Intent(mContext, TaskInfoActivity.class);
					in.putExtra("taskid", bean.getTaskId());
					in.putExtra("userid", bean.getUserId());
					mContext.startActivity(in);
				}
			}, 0,  cTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.commentContent.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
			holder.commentContent.append(msp);
			holder.commentContent.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件

			content2 = cComment+cBlank+cBlank+cTime;
			msp = new SpannableString(ToDBC(content2));
			//设置时间的字体大小
			msp.setSpan(new AbsoluteSizeSpan(12,true), content2.length()-cTime.length(), content2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置时间的字体颜色
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_11))
			,content2.length()-cTime.length(), content2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色

			holder.DynamicFragment_listview_item_commentExclusive.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
			holder.DynamicFragment_listview_item_commentExclusive.append(msp);
			holder.DynamicFragment_listview_item_commentExclusive.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
			holder.commentContent.setVisibility(View.VISIBLE);
			holder.qiqiuBg.setVisibility(View.VISIBLE);
			holder.DynamicFragment_listview_item_commentExclusive.setVisibility(View.VISIBLE);
		}
		holder.DynamicFragment_listview_item_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				thisMyItemClick.itemClick(position);
			}
		});
	}
	class ViewHolder{
		private TextView name,commentContent,DynamicFragment_listview_item_addNumText
		,DynamicFragment_listview_item_commentExclusive;
		private ImageView headImage,DynamicFragment_listview_item_addNumImage;
		private RelativeLayout DynamicFragment_listview_item_layout;
		private LinearLayout qiqiuBg;

		public ViewHolder(View convertView){
			name = (TextView) convertView.findViewById(R.id.DynamicFragment_listview_item_name);
			commentContent = (TextView) convertView.findViewById(R.id.DynamicFragment_listview_item_commentContent);
			DynamicFragment_listview_item_addNumText = (TextView) convertView.findViewById(R.id.DynamicFragment_listview_item_addNumText);
			DynamicFragment_listview_item_commentExclusive = (TextView) convertView.findViewById(R.id.DynamicFragment_listview_item_commentExclusive);

			headImage = (ImageView) convertView.findViewById(R.id.DynamicFragment_listview_item_headImage);
			DynamicFragment_listview_item_addNumImage = (ImageView) convertView.findViewById(R.id.DynamicFragment_listview_item_addNumImage);
			qiqiuBg = (LinearLayout) convertView.findViewById(R.id.qiqiuBg);
			DynamicFragment_listview_item_layout = (RelativeLayout) convertView.findViewById(R.id.DynamicFragment_listview_item_layout);

		}
	}
	/** 点击某一条的点击事件  */
	public interface onMyItemClick{
		public void itemClick(int position);
	}
	/**   
	 * 全角转换为半角   
	 *    
	 * @param input   
	 * @return   
	 */   
	public String ToDBC(String input) {    
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
