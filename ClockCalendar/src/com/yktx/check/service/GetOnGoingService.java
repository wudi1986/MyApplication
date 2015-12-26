//package com.yktx.check.service;
//
//import java.util.ArrayList;
//import java.util.Hashtable;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.util.Log;
//
//import com.google.gson.Gson;
//import com.yktx.check.bean.ByDateBean;
//import com.yktx.check.conn.ServiceListener;
//import com.yktx.check.conn.UrlParams;
//import com.yktx.check.util.Contanst;
//import com.yktx.check.util.Tools;
//
///**
// * 获取用户当前所有未暂停的Task, 并给出在一段时间内用户打过卡的所有日期
// * 
// * @author Administrator
// * 
// */
//public class GetOnGoingService extends Service {
//
//	public GetOnGoingService(String requestType,
//			Hashtable<String, String> params, String urlParams,
//			ServiceListener serviceListener) {
//		super(requestType, params, urlParams, serviceListener);
//		// TODO Auto-generated constructor stub
//		this.url = UrlParams.IP + Contanst.HTTP_GETONGOING + urlParams;
//		Tools.getLog(Tools.i, "aaa", "url ===== " + url);
//	}
//
//	@Override
//	void httpSuccess(String reponse) {
//		// TODO Auto-generated method stub
//		try {
//			// ======================根据协议本地测试数据================================
//			// reponse =
//			// ========================联调接口时注释就可以============================
//			Tools.getLog(Tools.i, "bbb", "reponse = " + reponse);
//			JSONObject result = new JSONObject(reponse);
//			String retcode = result.getString("statusCode");
//
//			Tools.getLog(Tools.i, "bbb", "retcode = " + retcode);
//			Tools.getLog(Tools.i, "bbb", "getJOSNdataSuccessgetJOSNdataSuccess");
//			if (Contanst.HTTP_SUCCESS.equals(retcode)) {// 成功获取数据
//				ArrayList<ByDateBean> list = new ArrayList<ByDateBean>();
//				JSONArray message = result.getJSONArray("message");
//				for (int j = 0; j < message.length(); j++) {
//					String str = message.getString(j);
//					Gson gson = new Gson();
//					ByDateBean bean = gson.fromJson(str, ByDateBean.class);
//					list.add(bean);
//				}
//				serviceListener.getJOSNdataSuccess(list, retcode,
//						Contanst.GETONGOING);
//			} else {
//				String errmsg = (String) result.get("message");
//				serviceListener.getJOSNdataFail(erroCodeParse(retcode), errmsg,
//						Contanst.GETONGOING);
//			}
//		} catch (JSONException e) {
//			serviceListener.getJOSNdataFail("", "服务器异常", Contanst.GETONGOING);
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	void httpFail(String erro) {
//		// TODO Auto-generated method stub
//		serviceListener.getJOSNdataFail("", "网络异常", Contanst.GETONGOING);
//		Tools.getLog(Tools.i, "aaa", "httpFailhttpFail");
//	}
//
//	@Override
//	void parse(String reponse) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
