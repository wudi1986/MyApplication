package com.yktx.check.service;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.yktx.check.bean.ByIdDetailBean;
import com.yktx.check.bean.MedalBean;
import com.yktx.check.bean.MoreAlertTimeBean;
import com.yktx.check.bean.RecommendFollowBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

public class UserGetRecommendFollowService extends Service{

	public UserGetRecommendFollowService(String requestType,
			Hashtable<String, String> params, String urlParams,
			ServiceListener serviceListener) {
		super(requestType, params, urlParams, serviceListener);
		// TODO Auto-generated constructor stub
		this.url = UrlParams.IP + Contanst.HTTP_USER_GETRECOMMENDFOLLOW+urlParams;
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
				ArrayList<RecommendFollowBean> list = new ArrayList<RecommendFollowBean>();
				JSONArray message = result.getJSONArray("message");
				for(int j = 0; j < message.length(); j++){
					String str = message.getString(j);
					Gson gson = new Gson();
					RecommendFollowBean bean = gson.fromJson(str, RecommendFollowBean.class);
					list.add(bean);
				}
				serviceListener.getJOSNdataSuccess(list, retcode , Contanst.USER_GETRECOMMENDFOLLOW);
			} else {
				String errmsg = (String) result.get("message");
				Tools.getLog(Tools.i, "aaa", "errmsg = "+errmsg);
				serviceListener.getJOSNdataFail(erroCodeParse(retcode), errmsg, Contanst.USER_GETRECOMMENDFOLLOW);
			}
		} catch (JSONException e) {
			serviceListener.getJOSNdataFail("", "服务器异常", Contanst.USER_GETRECOMMENDFOLLOW);
			e.printStackTrace();
		}
	}

	@Override
	void httpFail(String erro) {
		// TODO Auto-generated method stub
		serviceListener.getJOSNdataFail("", "网络异常", Contanst.USER_GETRECOMMENDFOLLOW);
		Tools.getLog(Tools.i, "aaa", "httpFailhttpFail");
	}

	@Override
	void parse(String reponse) {
		// TODO Auto-generated method stub
		
	}

}
