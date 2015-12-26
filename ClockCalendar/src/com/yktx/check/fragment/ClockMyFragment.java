package com.yktx.check.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.haarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;
import com.yktx.check.ClockSetActivity;
import com.yktx.check.R;
import com.yktx.check.TaskInfoActivity;
import com.yktx.check.adapter.ClockMyAdapter;
import com.yktx.check.adapter.ClockMyAdapter.PointExplainClickListener;
import com.yktx.check.bean.MyTaskAllBean;
import com.yktx.check.bean.MyTaskBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.listview.ListViewForScrollView;
import com.yktx.check.service.Service;
import com.yktx.check.square.fragment.BaseFragment;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.LvHeightUtil;
import com.yktx.check.util.MyScrollView;
import com.yktx.check.util.Tools;

@SuppressLint({ "ValidFragment", "NewApi" })
public class ClockMyFragment extends BaseFragment implements ServiceListener{
	ImageView sizeNullImage;
	//	com.yktx.check.listview.ListViewForScrollView mNewListView, mEverListView;
	ListViewForScrollView mNewListView, mEverListView;
	RelativeLayout clock_my_everClockLayout, clock_my_nowClockLayout;
	private ClockMyAdapter mNewAdapter, mEverAdapter;
	private MyTaskAllBean mTaskAllBean;
	private List<MyTaskBean> mNewList = new ArrayList<MyTaskBean>(), mEverList = new ArrayList<MyTaskBean>();
	private String thisJobUserid, userID;
	SharedPreferences settings;
	MyScrollView myScrollView;
	private RelativeLayout layout;
//	private LinearLayout tabLayout;
//	ScrollView parentScrollView;
	private boolean isOther;
	private Activity mContext;

	public ClockMyFragment(){
	}
	@SuppressLint("ValidFragment")
	public ClockMyFragment(String userid) {//, ScrollView parentScrollView, LinearLayout tabLayout
		thisJobUserid = userid;
//		this.parentScrollView = parentScrollView;
//		this.tabLayout = tabLayout;
	}
	PointExplainFragmentClickListener pointExplainFragmentClickListener;
	public void setPointExplainFragmentClickListener(PointExplainFragmentClickListener pointExplainFragmentClickListener){
		this.pointExplainFragmentClickListener = pointExplainFragmentClickListener;
	}


	RelativeLayout loadingView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = ClockMyFragment.this.getActivity();
		layout = (RelativeLayout) inflater.inflate(R.layout.clock_my, null);
		// TODO Auto-generated method stub
		settings = mContext.getSharedPreferences("clock",
				mContext.MODE_PRIVATE);
		userID = settings.getString("userid", null);
		myScrollView = (MyScrollView) layout.findViewById(R.id.myScrollView);
//		myScrollView.setParentScrollView(parentScrollView);
//		myScrollView.setViewGetLocationOnScreen(tabLayout);
		sizeNullImage = (ImageView) layout.findViewById(R.id.sizeNullImage);

		mNewListView = (ListViewForScrollView) layout.findViewById(R.id.clock_my_nowClockListview);
		mEverListView = (ListViewForScrollView) layout.findViewById(R.id.clock_my_everClockListview);
		mNewListView.setParentScrollView(myScrollView);
		mEverListView.setParentScrollView(myScrollView);
		clock_my_everClockLayout = (RelativeLayout) layout.findViewById(R.id.clock_my_everClockLayout);
		clock_my_nowClockLayout = (RelativeLayout) layout.findViewById(R.id.clock_my_nowClockLayout);

		loadingView = (RelativeLayout) layout.findViewById(R.id.loadingView);
		clock_my_nowClockLayout.setVisibility(View.GONE);
		Conn();
		if(userID.equals(thisJobUserid)){
			isOther = false;
		}else{
			isOther = true;
		}
		sizeNullImage.setImageResource(R.drawable.zhanwei_taren);
		mNewAdapter = new ClockMyAdapter(mContext,isOther);
		mNewListView.setAdapter(mNewAdapter);
		mNewAdapter.setPointExplainClickListener(pointExplainClickListener);
		mEverAdapter = new ClockMyAdapter(mContext,isOther);// 记得换list
		mEverListView.setAdapter(mEverAdapter);
		mEverAdapter.setPointExplainClickListener(pointExplainClickListener);


		mNewListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, TaskInfoActivity.class);

				// if(isLeft){
				in.putExtra("taskid", mNewList.get(position).getTaskId());
				in.putExtra("userid", thisJobUserid);

				in.putExtra("totalCheckCount", mNewList.get(position)
						.getTotalCheckCount());// 打卡次数
				in.putExtra("totalDateCount", mNewList.get(position)
						.getTotalDateCount());
				in.putExtra("title", mNewList.get(position).getTitle());
				in.putExtra("description", mNewList.get(position)
						.getDescription());
				if (!thisJobUserid.equals(userID)) {
					in.putExtra("isother", true);
				} else {
					in.putExtra("isother", false);
				}
				startActivity(in);
				// }else{
				// in.putExtra("taskid", mEverList.get(position).getTaskId());
				// in.putExtra("totalCheckCount", mEverList.get(position)
				// .getTotalCheckCount());// 打卡次数
				// in.putExtra("totalDateCount", mEverList.get(position)
				// .getTotalDateCount());
				// in.putExtra("title", mEverList.get(position).getTitle());
				// in.putExtra("description", mEverList.get(position)
				// .getDescription());
				// }

			}
		});
		mNewListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (isMy)
					showClickLongDialog(position, true);
				return true;
			}
		});

		mEverListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (isMy)
					showClickLongDialog(position, false);
				return true;
			}
		});



		mEverListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if(position >= mEverList.size()){
					return;
				}
				Intent in = new Intent(mContext, TaskInfoActivity.class);

				// if(isLeft){
				in.putExtra("taskid", mEverList.get(position).getTaskId());
				in.putExtra("userid", thisJobUserid);

				in.putExtra("totalCheckCount", mEverList.get(position)
						.getTotalCheckCount());// 打卡次数
				in.putExtra("totalDateCount", mEverList.get(position)
						.getTotalDateCount());
				in.putExtra("title", mNewList.get(position).getTitle());
				in.putExtra("description", mEverList.get(position)
						.getDescription());
				in.putExtra("isCannotDaka", true);
				if (!thisJobUserid.equals(userID)) {
					in.putExtra("isother", true);
				} else {
					in.putExtra("isother", false);
				}
				startActivity(in);
			}
		});
		// bgLayout = (RelativeLayout)
		// LayoutInflater.from(mContext).inflate(R.layout.loading_view_anim,
		// null);

		// fl.addView(bgLayout);
		// getBuildingConn(type);
		//		initList();
		return layout;
	}

	PointExplainClickListener pointExplainClickListener = new PointExplainClickListener() {

		@Override
		public void setPointExplainClick() {
			// TODO Auto-generated method stub
			if (pointExplainFragmentClickListener != null)
				pointExplainFragmentClickListener.setPointExplainFragment();
		}
	};

	public void showClickLongDialog(final int position, final boolean isLeft) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(mContext,
						R.style.CustomDiaLog_by_SongHang));
		builder.setItems(new String[] { "删除", "设置", "取消" },
				new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					showDeleteDialog(position,
							isLeft ? mNewList.get(position).getTaskId()
									: mEverList.get(position)
									.getTaskId());
					break;
				case 1:
					Intent in = new Intent(mContext, ClockSetActivity.class);
					in.putExtra("taskid",
							isLeft ? mNewList.get(position).getTaskId()
									: mEverList.get(position)
									.getTaskId());
					startActivity(in);

					break;
				case 2:

					break;
				}
			}
		});
		builder.show();
	}

	public void showDeleteDialog(final int position, final String taskID) {
		new AlertDialog.Builder(mContext)
		.setTitle("确认删除此卡？").setMessage("删除后此卡所有数据将无法恢复")
		.setPositiveButton("删除", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				loadingView.setVisibility(View.VISIBLE);
				deleteTaskConn(position, taskID);
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		}).show();
	}

	private void deleteTaskConn(int position, String taskID) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("taskId", taskID));
			// params.add(new BasicNameValuePair("taskId", isLeft ?
			// mNewList.get(
			// position).getTaskId() : mEverList.get(position).getTaskId()));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_TASK_DELETE, null, null, this)
		.addList(params).request(UrlParams.POST);

	}

	private void initList() {
		boolean ishaveNewList;
		if (mNewList.size() == 0 && mEverList.size() == 0) {

			sizeNullImage.setVisibility(View.VISIBLE);
			clock_my_nowClockLayout.setVisibility(View.GONE);
			clock_my_everClockLayout.setVisibility(View.GONE);
		} else {

			//			sizeNullImage.setVisibility(View.GONE);
			if (mNewList.size() != 0) {
				clock_my_nowClockLayout.setVisibility(View.VISIBLE);
				ishaveNewList = true;
				if(isOther){
					sizeNullImage.setVisibility(View.GONE);
				}
			} else {
				ishaveNewList = false;
				clock_my_nowClockLayout.setVisibility(View.GONE);
				if(isOther){
					sizeNullImage.setVisibility(View.VISIBLE);
				}

			}

			if (mEverList.size() != 0) {
				clock_my_everClockLayout.setVisibility(View.VISIBLE);
				if(!isOther){
					sizeNullImage.setVisibility(View.GONE);
				}
				
			} else {
				clock_my_everClockLayout.setVisibility(View.GONE);
				if(!ishaveNewList){
					sizeNullImage.setVisibility(View.VISIBLE);
				}
			}

			mNewAdapter.setList(mNewList, true);
			mNewAdapter.notifyDataSetChanged();

			mEverAdapter.setList(mEverList, false);
			mEverAdapter.notifyDataSetChanged();
			//			
			LvHeightUtil.setListViewHeightBasedOnChildren(mNewListView);
			LvHeightUtil.setListViewHeightBasedOnChildren(mEverListView);
			setRightAdapter(mNewListView);
			if (!userID.equals(thisJobUserid)) {
				isMy = false;
				clock_my_nowClockLayout.setVisibility(View.GONE);
				clock_my_everClockLayout.setVisibility(View.GONE);
				mEverListView.setVisibility(View.GONE);
			} else {
				isMy = true;
			}
		}

	}

	boolean isMy;

	private void setRightAdapter(ListView listView) {
//		AnimationAdapter animAdapter = new SwingRightInAnimationAdapter(
//				mNewAdapter);
//		animAdapter.setAbsListView(listView);
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mNewAdapter);
		swingBottomInAnimationAdapter.setAbsListView(listView);
		listView.setAdapter(swingBottomInAnimationAdapter);
//		listView.setAdapter(animAdapter);
	}

	public interface clickLongItem {
		public void longItem(int position);
	}


	public interface PointExplainFragmentClickListener{
		public void setPointExplainFragment();
	}

	public void Conn() {
		//		Hashtable<String, String> param = new Hashtable<String, String>();
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(thisJobUserid);
		sb.append("&curUserId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_GETALL, null, sb.toString(),
				ClockMyFragment.this).addList(null).request(UrlParams.GET);
	}
	@Override
	public void getJOSNdataSuccess(Object bean, String sccmsg, int connType) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_OK;
		msg.obj = bean;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}

	@Override
	public void getJOSNdataFail(String errcode, String errmsg, int connType) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_FAIL;
		msg.obj = errmsg;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.GETALL:
					mTaskAllBean = (MyTaskAllBean) msg.obj;
					Tools.getLog(
							Tools.i,
							"aaa",
							"mTaskAllBean==================="
									+ mTaskAllBean.toString());
					mNewList = mTaskAllBean.getOngoingTasks();
					mEverList = mTaskAllBean.getSuspendTasks();
					// sizeNullImage.setVisibility(View.GONE);

					Tools.getLog(Tools.d, "bbb",
							"mTaskAllBean:" + mTaskAllBean.toString());
					Tools.getLog(Tools.d, "bbb",
							"mEverList:" + mEverList.size());
					initList();
					loadingView.setVisibility(View.GONE);
					break;
				case Contanst.TASK_DELETE:
					Toast.makeText(mContext, "删除成功！",
							Toast.LENGTH_SHORT).show();
					Conn();
					break;

				}
				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
				.show();
				break;
			}
		}
	};

}
