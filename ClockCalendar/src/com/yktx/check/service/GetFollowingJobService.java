package com.yktx.check.service;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.yktx.check.bean.UserFollowingBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

public class GetFollowingJobService extends Service{

	public GetFollowingJobService(String requestType,
			Hashtable<String, String> params, String urlParams,
			ServiceListener serviceListener) {
		super(requestType, params, urlParams, serviceListener);
		// TODO Auto-generated constructor stub
		this.url = UrlParams.IP + Contanst.HTTP_USER_GETFOLLOWINGJOB+urlParams;
		Tools.getLog(Tools.i, "kkk", "url ===== " + url);
	}

	@Override
	void httpSuccess(String reponse) {
		// TODO Auto-generated method stub
		try {
			// ======================根据协议本地测试数据================================
			// reponse =
			// ========================联调接口时注释就可以============================
			Tools.getLog(Tools.i, "kkk", "reponse = "+reponse);
			JSONObject result = new JSONObject(reponse);
			String retcode =  result.getString("statusCode");

			Tools.getLog(Tools.i, "kkk", "retcode = "+retcode);
			if (Contanst.HTTP_SUCCESS.equals(retcode)) {//成功获取数据
				
				String message = result.getString("message");
				Gson gson = new Gson();
				UserFollowingBean byTaskIdBean = gson.fromJson(message, UserFollowingBean.class);
				serviceListener.getJOSNdataSuccess(byTaskIdBean, retcode , Contanst.USER_GETFOLLOWINGJOB);
			} else {
				String errmsg = (String) result.get("message");
				serviceListener.getJOSNdataFail(erroCodeParse(retcode), errmsg, Contanst.USER_GETFOLLOWINGJOB);
			}
		} catch (JSONException e) {
			serviceListener.getJOSNdataFail("", "服务器异常", Contanst.USER_GETFOLLOWINGJOB);
			e.printStackTrace();
		}
	}

	@Override
	void httpFail(String erro) {
		// TODO Auto-generated method stub
		serviceListener.getJOSNdataFail("", "网络异常", Contanst.USER_GETFOLLOWINGJOB);
		Tools.getLog(Tools.i, "aaa", "httpFailhttpFail");
	}

	@Override
	void parse(String reponse) {
		// TODO Auto-generated method stub
		
	}

}
