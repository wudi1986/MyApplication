package com.yktx.check.service;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.yktx.check.bean.ByTaskIdBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

public class GetByTaskIdService extends Service{

	public GetByTaskIdService(String requestType,
			Hashtable<String, String> params, String urlParams,
			ServiceListener serviceListener) {
		super(requestType, params, urlParams, serviceListener);
		// TODO Auto-generated constructor stub
		this.url = UrlParams.IP + Contanst.HTTP_GETBYTASKID+urlParams;
		Tools.getLog(Tools.i, "aaa", "url ===== " + url);
	}

	@Override
	void httpSuccess(String reponse) {
		// TODO Auto-generated method stub
		try {
			// ======================根据协议本地测试数据================================
			// reponse =
			// ========================联调接口时注释就可以============================
			Tools.getLog(Tools.i, "bbb", "reponse = "+reponse);
			JSONObject result = new JSONObject(reponse);
			String retcode =  result.getString("statusCode");

			Tools.getLog(Tools.i, "bbb", "retcode = "+retcode);
			Tools.getLog(Tools.i, "bbb", "getJOSNdataSuccessgetJOSNdataSuccess");
			if (Contanst.HTTP_SUCCESS.equals(retcode)) {//成功获取数据
//				ByTaskIdBean byTaskIdBean = new ByTaskIdBean();
//				JSONObject message = result.getJSONObject("message");
//				byTaskIdBean.setCurrentPage(message.getString("currentPage"));
//				byTaskIdBean.setTotalCount(message.getString("totalCount"));
//				byTaskIdBean.setTotalPage(message.getString("totalPage"));
//				byTaskIdBean.setPageLimit(message.getString("pageLimit"));
//				ArrayList<TaskItemBean> itemBeans = new ArrayList<TaskItemBean>();
//				JSONArray listdata = message.getJSONArray("listData");
//				for(int i = 0;i<listdata.length();i++){
//					String str = listdata.getString(i);
//					Gson gson = new Gson();
//					TaskItemBean taskItemBean = gson.fromJson(str, TaskItemBean.class);
//					itemBeans.add(taskItemBean);
//				}
//				byTaskIdBean.setListData(itemBeans);
				String message = result.getString("message");
				Gson gson = new Gson();
				ByTaskIdBean byTaskIdBean = gson.fromJson(message, ByTaskIdBean.class);
				serviceListener.getJOSNdataSuccess(byTaskIdBean, retcode , Contanst.GETBYTASKID);
			} else {
				String errmsg = (String) result.get("message");
				serviceListener.getJOSNdataFail(erroCodeParse(retcode), errmsg, Contanst.GETBYTASKID);
			}
		} catch (JSONException e) {
			serviceListener.getJOSNdataFail("", "服务器异常", Contanst.GETBYTASKID);
			e.printStackTrace();
		}
	}

	@Override
	void httpFail(String erro) {
		// TODO Auto-generated method stub
		serviceListener.getJOSNdataFail("", "网络异常", Contanst.GETBYTASKID);
		Tools.getLog(Tools.i, "aaa", "httpFailhttpFail");
	}

	@Override
	void parse(String reponse) {
		// TODO Auto-generated method stub
		
	}

}
