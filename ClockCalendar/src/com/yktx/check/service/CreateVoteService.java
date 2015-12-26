package com.yktx.check.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.yktx.check.bean.VotesBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

/**
 * 点赞一次, 返回点赞后当前Job最新的点赞(5条)
 * @author Administrator
 *
 */

public class CreateVoteService extends Service{

	public CreateVoteService(String requestType,
			Hashtable<String, String> params, String urlParams,
			ServiceListener serviceListener) {
		super(requestType, params, urlParams, serviceListener);
		// TODO Auto-generated constructor stub
		this.url = UrlParams.IP + Contanst.HTTP_CREATEVOTE;
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
			List<VotesBean> beans = new ArrayList<VotesBean>(5);
			if (Contanst.HTTP_SUCCESS.equals(retcode)) {//成功获取数据
				JSONArray message = result.getJSONArray("message");
				for(int i = 0; i<message.length(); i++){
					String str = message.getString(i);
					Gson gson = new Gson();
					VotesBean bean =  gson.fromJson(str, VotesBean.class);
					beans.add(bean);
				}
				
				serviceListener.getJOSNdataSuccess(beans, retcode , Contanst.CREATEVOTE);
			} else {
				String errmsg = (String) result.get("message");
				serviceListener.getJOSNdataFail(erroCodeParse(retcode), errmsg, Contanst.CREATEVOTE);
			}
		} catch (JSONException e) {
			serviceListener.getJOSNdataFail("", "服务器异常", Contanst.CREATEVOTE);
			e.printStackTrace();
		}
	}

	@Override
	void httpFail(String erro) {
		// TODO Auto-generated method stub
		serviceListener.getJOSNdataFail("", "网络异常", Contanst.CREATEVOTE);
		Tools.getLog(Tools.i, "aaa", "httpFailhttpFail");
	}

	@Override
	void parse(String reponse) {
		// TODO Auto-generated method stub
		
	}

}
