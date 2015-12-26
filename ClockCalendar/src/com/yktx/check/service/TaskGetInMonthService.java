package com.yktx.check.service;

import java.util.Hashtable;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yktx.check.bean.GetByTaskIdCameraBean;
import com.yktx.check.bean.TaskIdCameraBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

/**
 * @author Administrator
 * 
 */
public class TaskGetInMonthService extends Service {

	String urlParams;

	public TaskGetInMonthService(String requestType,
			Hashtable<String, String> params, String urlParams,
			ServiceListener serviceListener) {
		super(requestType, params, urlParams, serviceListener);
		this.url = UrlParams.IP + Contanst.HTTP_TASK_INMONTH
				+ urlParams;
		this.urlParams = urlParams;
		Tools.getLog(Tools.i,"aaa", "url ======= " + url);
	}

	@Override
	void httpFail(String erro) {
		serviceListener.getJOSNdataFail("", "网络异常",
				Contanst.TASK_INMONTH);
		Tools.getLog(Tools.i,"aaa", "httpFailhttpFail");
		// LodingActivity.isJion = false;
	}

	@Override
	void httpSuccess(String reponse) {

		try {
			// ======================根据协议本地测试数据================================
			// reponse =
			// ========================联调接口时注释就可以============================
			JSONObject result = new JSONObject(reponse);
			String retcode = result.getString("statusCode").toString();
			Tools.getLog(Tools.i,"aaa", "retcode = " + retcode);
			if (Contanst.HTTP_SUCCESS.equals(retcode)) {// 成功获取数据
//				String messageStr = result.getString("message");

				TaskIdCameraBean tBean = new TaskIdCameraBean();

				LinkedHashMap<String, GetByTaskIdCameraBean> curMap = new LinkedHashMap<String, GetByTaskIdCameraBean>(
						10);
//				JSONObject message = new JSONObject(messageStr);
//				long createDate = message.getLong("taskCTime");
//				int manCountToday = message.getInt("manCountToday");
//				String buildingId = message.getString("buildingId");
				JSONArray listData = new JSONArray(
						result.getString("message"));
				Tools.getLog(Tools.i,"aaa", "listData ============= " + listData.length());
				for (int j = 0; j < listData.length(); j++) {
					JSONObject jsonObject = listData.getJSONObject(j);
					GetByTaskIdCameraBean bean = new GetByTaskIdCameraBean();
					String checkDate = jsonObject.getString("checkDate");
					Tools.getLog(Tools.i,"kkk", "checkDate ======== "+checkDate);
					bean.setCheckDate(checkDate);
					bean.setImageCount(jsonObject.getInt("imageCount"));
					if(jsonObject.getString("lastImageCTime") == null || jsonObject.getString("lastImageCTime").equals("null")){
						bean.setLastImageCTime(0);
					} else {
						bean.setLastImageCTime(jsonObject.getLong("lastImageCTime"));
					}
					
					if(jsonObject.getString("lastImagePath") == null || jsonObject.getString("lastImagePath").equals("null")){
						bean.setLastImagePath(null);
					} else {
						bean.setLastImagePath(jsonObject.getString("lastImagePath"));
					}
					
					if(jsonObject.getString("lastImageSource") == null || jsonObject.getString("lastImageSource").equals("null")){
						bean.setLastImageSource(2);
					} else {
						bean.setLastImageSource(jsonObject.getInt("lastImageSource"));
					}
					bean.setPosition(j);
					curMap.put(checkDate, bean);
					if(j == listData.length()-1){
						tBean.setLastKey(checkDate);
					}
				}
//				tBean.setTaskCTime(createDate);
				tBean.setMapData(curMap);
//				tBean.setManCountToday(manCountToday);
//				tBean.setBuildingId(buildingId);
				
				serviceListener.getJOSNdataSuccess(tBean, retcode,
						Contanst.TASK_INMONTH);

				// 解析
				// FenYeBean<NewCameraBean> bean = (FenYeBean<NewCameraBean>)
				// msg.obj;
				// currentPage = bean.getCurrentPage();
				// totalCount = bean.getTotalCount();
				// totalPage = bean.getTotalPage();
				//
				// getAttentionList = bean.getListData();

			} else {
				String errmsg = (String) result.get("mess");
				serviceListener.getJOSNdataFail(erroCodeParse(retcode), errmsg,
						Contanst.TASK_INMONTH);
			}
		} catch (JSONException e) {
			serviceListener.getJOSNdataFail("", "服务器异常",
					Contanst.TASK_INMONTH);
			e.printStackTrace();
		}
	}

	@Override
	void parse(String reponse) {
		// TODO Auto-generated method stub

	}

}
