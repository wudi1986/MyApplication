package com.yktx.check.conn;

/**
 * 联网服务反馈监听�?
 * 基本联网流程：ui设置此监听器到Service，service设置监听器到HttpConnectinWrapper
 * 				�?��数据连续回调到ui�?
 * 
 * @author wudi
 */
public interface ServiceListener {

	/**
	 * 成功返回数据封装�?
	 * @param bean 数据解析后封装数据类
	 */
	public void getJOSNdataSuccess(Object bean,String sccmsg, int connType );
	
	/**
	 * 请求失败
	 * @param errcode 错误�?
	 * @param errmsg 错误信息
	 */
	public void getJOSNdataFail(String errcode,String errmsg, int connType);
	
}
