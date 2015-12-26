/**
 * 
 */
package com.yktx.check.square.fragment;

import java.lang.reflect.Field;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

/**  
 * @author 作者 :  wudi
 * @version 创建时间：2014年5月23日 下午1:59:20  
 * 类说明  */
/**
 * @author Administrator
 *
 */
public class BaseFragment extends Fragment{
	
	public ProgressDialog mDialog;
	
	/** 第几页 */
	public int currentPage = 1;
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
	
	public	String longitude = "0.0", latitude = "0.0";
		
	OnGoHomeListener onGoHomeListener;
	
	public interface OnGoHomeListener {
		public void goHome();
	}
	
	public void setOnGoHomeListener(OnGoHomeListener onGoHomeListener){
		this.onGoHomeListener = onGoHomeListener;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		try {
	        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);

	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }

		
		
	}
	
	
//	/* 打开相机 */
//	public void opencamera(String group_id,String group_name,String group_distance,String group_peopleCount) {
//
//		Intent cameraIntent = new Intent(BaseFragment.this.getActivity(),
//				CameraActivity.class);
//		cameraIntent.putExtra(CameraActivity.IsRegister, "0");
//		cameraIntent.putExtra(CameraActivity.IsIntoGroup, true);
//		cameraIntent.putExtra("longitude", longitude);
//		cameraIntent.putExtra("latitude", latitude);
//		cameraIntent.putExtra("group_id", group_id);
//		cameraIntent.putExtra("group_name", group_name);
//		cameraIntent.putExtra("group_distance", group_distance);
//		cameraIntent.putExtra("group_peopleCount", group_peopleCount);
//		startActivityForResult(cameraIntent, 444);
//	}
	
	
	
	public void showProgressDialog(String message) {
		mDialog = new ProgressDialog(BaseFragment.this.getActivity());
		mDialog.setMessage(message);
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(true);
		mDialog.show();
		
		
	}
}
