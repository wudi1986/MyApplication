package com.yktx.check.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yktx.check.ClockMainActivity;
import com.yktx.check.bean.PerformanceBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;
/**
 * 查询用户在一个时间段内的任务执行状况
 * @author Administrator
 *
 */
public class GetPerformance extends Service{

	public GetPerformance(String requestType, Hashtable<String, String> params,
			String urlParams, ServiceListener serviceListener) {
		super(requestType, params, urlParams, serviceListener);
		// TODO Auto-generated constructor stub
		this.url = UrlParams.IP + Contanst.HTTP_GETPERFORMANCE+urlParams;
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
				 HashMap<String, String> curMap = new HashMap<String, String>(10);
				JSONArray message = result.getJSONArray("message");
				ArrayList<PerformanceBean> list = new ArrayList<PerformanceBean>(10);
				for(int j = 0; j < message.length(); j++){
					JSONObject jsonObject = message.getJSONObject(j);
					PerformanceBean bean = new PerformanceBean();
					bean.setPerformance(jsonObject.getInt("performance"));
					bean.setRecord_date(jsonObject.getString("record_date"));
					list.add(bean);
					ClockMainActivity.curMap.put(jsonObject.getString("record_date"), jsonObject.getString("performance"));
				}
				serviceListener.getJOSNdataSuccess(list, retcode , Contanst.GETPERFORMANCE);
			} else {
				String errmsg = (String) result.get("message");
				serviceListener.getJOSNdataFail(erroCodeParse(retcode), errmsg, Contanst.GETPERFORMANCE);
			}
		} catch (JSONException e) {
			serviceListener.getJOSNdataFail("", "服务器异常", Contanst.GETPERFORMANCE);
			e.printStackTrace();
		}
	}

	@Override
	void httpFail(String erro) {
		// TODO Auto-generated method stub
		serviceListener.getJOSNdataFail("", "网络异常", Contanst.GETPERFORMANCE);
		Tools.getLog(Tools.i, "aaa", "httpFailhttpFail");
	}

	@Override
	void parse(String reponse) {
		// TODO Auto-generated method stub
		
	}

}
