package com.yktx.check.service;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

public class MessageRemindService extends Service{

	public MessageRemindService(String requestType,
			Hashtable<String, String> params, String urlParams,
			ServiceListener serviceListener) {
		super(requestType, params, urlParams, serviceListener);
		// TODO Auto-generated constructor stub
		this.url=UrlParams.IP+Contanst.HTTP_MESSAGE_REMIND;
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

			Tools.getLog(Tools.i,  "aaa", "retcode = "+retcode);
			if (Contanst.HTTP_SUCCESS.equals(retcode)) {//成功获取数据
				serviceListener.getJOSNdataSuccess("ok", retcode , Contanst.MESSAGE_REMIND);
			} else {
				String errmsg = (String) result.get("message");
				serviceListener.getJOSNdataFail(erroCodeParse(retcode), errmsg, Contanst.MESSAGE_REMIND);
			}
		} catch (JSONException e) {
			serviceListener.getJOSNdataFail("", "服务器异常", Contanst.MESSAGE_REMIND);
			e.printStackTrace();
		}
	}

	@Override
	void httpFail(String erro) {
		// TODO Auto-generated method stub
		serviceListener.getJOSNdataFail("", "网络异常", Contanst.MESSAGE_REMIND);
		Tools.getLog(Tools.i,  "aaa", "httpFailhttpFail");
	}

	@Override
	void parse(String reponse) {
		// TODO Auto-generated method stub

	}

}
