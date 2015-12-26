package com.yktx.check.service;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

public class TaskSuspendService extends Service{

	public TaskSuspendService(String requestType,
			Hashtable<String, String> params, String urlParams,
			ServiceListener serviceListener) {
		super(requestType, params, urlParams, serviceListener);
		// TODO Auto-generated constructor stub
		this.url=UrlParams.IP+Contanst.HTTP_TASK_SUSPEND;
		Tools.getLog(Tools.i,  "aaa", "url ===== "+url);
	}

	@Override
	void httpSuccess(String reponse) {
		// TODO Auto-generated method stub
		try {
			// ======================根据协议本地测试数据================================
			// reponse =
			// ========================联调接口时注释就可以============================
			JSONObject result = new JSONObject(reponse);
			String retcode = result.get("statusCode").toString();
			
//			HomeBestView.listCount = Integer.parseInt((String)result.get("tot_count")); 
			Tools.getLog(Tools.i,  "aaa", "retcode = "+retcode);
			if (Contanst.HTTP_SUCCESS.equals(retcode)) {//成功获取数据
				serviceListener.getJOSNdataSuccess("ok", retcode , Contanst.TASK_SUSPEND);
			} else {
				String errmsg = (String) result.get("message");
				serviceListener.getJOSNdataFail(erroCodeParse(retcode), errmsg, Contanst.TASK_SUSPEND);
			}
		} catch (JSONException e) {
			serviceListener.getJOSNdataFail("", "服务器异常", Contanst.TASK_SUSPEND);
			e.printStackTrace();
		}
	}

	@Override
	void httpFail(String erro) {
		// TODO Auto-generated method stub
		serviceListener.getJOSNdataFail("", "网络异常", Contanst.TASK_SUSPEND);
		Tools.getLog(Tools.i,  "aaa", "httpFailhttpFail");
//		LodingActivity.isJion = false;
	}

	@Override
	void parse(String reponse) {
		// TODO Auto-generated method stub
		
	}

}
