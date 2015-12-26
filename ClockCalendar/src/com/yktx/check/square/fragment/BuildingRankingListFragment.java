package com.yktx.check.square.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.yktx.check.ClockGroupInfoFragmentActivity;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.ClockThisTaskUserActivity;
import com.yktx.check.R;
import com.yktx.check.adapter.ThisTaskUserAdapter;
import com.yktx.check.adapter.ThisTaskUserAdapter.setFansTypeLisitener;
import com.yktx.check.bean.FansBean;
import com.yktx.check.bean.FansItemBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.listview.XListView;
import com.yktx.check.listview.XListView.IXListViewListener;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

/**
 * 达人榜单
 */
@SuppressLint("ValidFragment")
public class BuildingRankingListFragment extends BaseFragment implements ServiceListener{


	/** 第几页 */
	public int currentPage;
	/** 总数 */
	public int totalCount;
	/** 总页数 */	
	public int totalPage;
	/** 一页多少条数据 */
	public int pageLimit = 10;
	/** 数据集合 */
	public String listData;
	/** 当前时间 */
	public long reflashTime;

	String userName;//thisJobUserID
	//	SharedPreferences settings;
	boolean isConn, isReflush;

	private FansBean fansBean;
	private ArrayList<FansItemBean> fansItemBeans = new ArrayList<FansItemBean>();

	private ThisTaskUserAdapter adapter;


	private XListView listView;
	private ImageView imageListNull;//,mBack,mAdd
//	private TextView mTitle;
	private String buildingID;
	
	ClockGroupInfoFragmentActivity clockGroupInfoFragmentActivity;

	@SuppressLint("ValidFragment")
	public BuildingRankingListFragment(
			ClockGroupInfoFragmentActivity Activity) {
		super();
		this.clockGroupInfoFragmentActivity = Activity;
	}
	
	View view;
	String userID;

	private SharedPreferences settings;
	RelativeLayout title_item_layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = clockGroupInfoFragmentActivity.getLayoutInflater().inflate(R.layout.activity_clock_fans, null);
		settings = clockGroupInfoFragmentActivity.getSharedPreferences("clock", clockGroupInfoFragmentActivity.MODE_PRIVATE);
		userID = settings.getString("userid", null);
		findViews();
		init();
		setListeners();
		return view;
	}

	protected void findViews() {
		// TODO Auto-generated method stub
		title_item_layout = (RelativeLayout) view.findViewById(R.id.title_item_layout);
		imageListNull = (ImageView) view.findViewById(R.id.sizeNullImage);
//		thisJobUserID = clockGroupInfoFragmentActivity.getIntent().getStringExtra("thisjobuserid");
		buildingID = clockGroupInfoFragmentActivity.getIntent().getStringExtra("buildingId");

		if (!isConn) {
			Tools.getLog(Tools.d, "aaa", "===================22222====================");
			isConn = true;
			isReflush = true;
			conn(1);
		}
		listView = (XListView) view.findViewById(R.id.clock_my_fragment_listview);
		adapter = new ThisTaskUserAdapter(clockGroupInfoFragmentActivity);
//		mBack = (ImageView) view.findViewById(R.id.title_item_leftImage);
//		mAdd = (ImageView) view.findViewById(R.id.title_item_rightImage);
//		mTitle = (TextView) view.findViewById(R.id.title_item_content);

		
		
		//		if(isOther){
		//			imageListNull.setImageResource(R.drawable.zhanwei_fensi_taren);
		//		}else{
		//			imageListNull.setImageResource(R.drawable.zhanwei_fensi_taren);
		//		}
	}

	protected void init() {
		// TODO Auto-generated method stub
		title_item_layout.setVisibility(View.GONE);
		listView.setAdapter(adapter);
		listView.setIsShow(true);
		listView.setPullGoHome(false);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		listView.setXListViewListener(listener);
		adapter.setfansType(fansTypeLisitener);
//		mAdd.setVisibility(View.GONE);
//		mTitle.setText("此卡排行榜");
	}

	protected void setListeners() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				FansItemBean itemBean = fansItemBeans.get(position-1);
				if (userID.equals(itemBean.getUserId())) {
					Intent in = new Intent(clockGroupInfoFragmentActivity,
							ClockMyActivity.class);
					clockGroupInfoFragmentActivity.startActivity(in);
				} else {
					Intent in = new Intent(clockGroupInfoFragmentActivity,
							ClockOtherUserActivity.class);
					in.putExtra("userid", itemBean.getUserId());
					clockGroupInfoFragmentActivity.startActivity(in);
				}
			}
		});
//		mBack.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				finish();
//			}
//		});
	}
	String fansName,fansID;
	setFansTypeLisitener fansTypeLisitener = new setFansTypeLisitener() {

		@Override
		public void setFansType(int position, boolean isfan) {
			// TODO Auto-generated method stub
			FansItemBean itemBean = fansItemBeans.get(position);
			fansName = itemBean.getUserName();
			fansID = itemBean.getUserId();
			Tools.getLog(Tools.d, "aaa", "guanzhu:"+isfan);
			if(isfan){
				connUnFollow();
			}else{
				connFollow();
			}
		}
	};
	IXListViewListener listener = new IXListViewListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			if (isConn) {
				return;
			}
			conn(1);
			isReflush = true;
			isConn = true;

		}

		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			if (isConn) {
				return;
			}
			isReflush = false;

			if (currentPage * 10 >= totalCount) {
				// Toast.makeText(NewFragment.this.getActivity(), "亲，没有更多信息了",
				// Toast.LENGTH_SHORT).show();
				onLoad();
				return;
			}
			conn(currentPage + 1);
			Tools.getLog(Tools.i, "ccc","加载中的的currentPage：===="+currentPage);
			isConn = true;
		}
	};
	public void connFollow(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("followedUserId", fansID));
			params.add(new BasicNameValuePair("fansUserId", userID));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_USER_FOLLOW, null,
				null, BuildingRankingListFragment.this)
				.addList(params).request(UrlParams.POST);
	}
	public void connUnFollow(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("followedUserId", fansID));
			params.add(new BasicNameValuePair("fansUserId", userID));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_USER_UNFOLLOW, null,
				null, BuildingRankingListFragment.this)
				.addList(params).request(UrlParams.POST);
	} 
	private void conn(int currentPage){
		StringBuffer sb  = new StringBuffer();
		sb.append("?currentPage=");
		sb.append(currentPage);
		sb.append("&pageLimit=");
		sb.append(pageLimit);
		sb.append("&buildingId=");
		sb.append(buildingID);
		sb.append("&curUserId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_USER_GETUSERINBUILDING, null,
				sb.toString(), BuildingRankingListFragment.this)
				.addList(null).request(UrlParams.GET);
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
				case Contanst.USER_GETUSERINBUILDING:
					// 刷新附近列表
					if (isReflush) {
						currentPage = 1;
						fansItemBeans.clear();
						fansBean = (FansBean) msg.obj;
						currentPage = fansBean.getCurrentPage();
						totalCount = fansBean.getTotalCount();
						totalPage = fansBean.getTotalPage();
						fansItemBeans = fansBean.getListData();
						Tools.getLog(Tools.d, "aaa", "个人中心："+fansItemBeans.toString());
						if (fansItemBeans.size() == 0) {
							imageListNull.setVisibility(View.VISIBLE);
							onLoad();
							adapter.notifyDataSetChanged();
							return;
						}
						imageListNull.setVisibility(View.GONE);
						//						send_time = newList.get(newList.size()-1).getCheck_time();
						adapter.setList(fansItemBeans);
						//						adapter.setDistance(latitude, longitude);
						listView.setPullLoadEnable(true);
						adapter.notifyDataSetChanged();

					} else {
						currentPage++;
						Tools.getLog(Tools.i, "ccc","联网中的currentPage：===="+currentPage);
						fansBean = (FansBean) msg.obj;
						fansItemBeans.addAll(fansBean.getListData());
						adapter.setList(fansItemBeans);
						//						send_time = newList.get(newList.size()-1).getCheck_time ();
						//						adapter.setDistance(latitude, longitude);
						adapter.notifyDataSetChanged();
					}

					onLoad();
					if (totalCount <= 10 || currentPage * 10 >= totalCount) {
						listView.setIsShow(false);
					} else {
						listView.setIsShow(true);
					}
					//判断是否为本页点入building 页
					//					Tools.getLog(Tools.d, "aaa","type："+GroupMainFragmentActivity.ReflushItem+"，页数："+newItem);
					//					if(GroupMainFragmentActivity.ReflushItem == 1){
					//						xListView.setSelection(newItem);
					//					}
					break;
				case Contanst.USER_FOLLOW:
					Toast.makeText(clockGroupInfoFragmentActivity, "加入关注,"+fansName, Toast.LENGTH_SHORT).show();
					isConn = true;
					isReflush = true;
					conn(1);
					break;
				case Contanst.USER_UNFOLLOW:
					Toast.makeText(clockGroupInfoFragmentActivity, "取消关注,"+fansName, Toast.LENGTH_SHORT).show();
					isConn = true;
					isReflush = true;
					conn(1);
					break;

				}
				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				Toast.makeText(clockGroupInfoFragmentActivity, message, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	private void onLoad() {
		//		if(loadingView.getVisibility() != View.GONE){
		//			loadingView.setVisibility(View.GONE);
		//		}
		listView.stopRefresh();
		listView.stopLoadMore();
		isConn = false;
		isReflush = false;
	}



}
