package com.tik.develop.diyparse;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.http.BufferRead;
import com.http.DataCenter;
import com.http.HttpRequest;
import com.http.HttpUtil;
import com.icecream.annotation.Autowired;
import com.icecream.annotation.RemoteParse;
import com.icecream.constant.RemoteParseIn;
import com.icecream.core.ControllerData;
import com.icecream.web.Util;
import com.tik.common.contant.Data;

/**
 * @作者 ChengShi
 * @日期 2020-05-16 01:39:08
 * @版本 1.0
 * @描述 
 */
@RemoteParse
public class SocketParse implements RemoteParseIn{
	@Autowired
	private HttpUtil httpUtil;
	
	/*存放IP*/
	public final static ThreadLocal<String> IPS = new ThreadLocal<>();
	
	@Override
	public Object parse(Socket socket, int type, String value, ControllerData controllerData, Map<Class<?>, Object> methodPamera, Type returnType) throws Throwable{
		switch (type) {
			case Data.HTTP_CODE: return parseHttp(socket, controllerData, methodPamera, returnType);
		}
		return null;
	}
	
	/*解析HTTP协议*/
	private Object parseHttp(Socket socket, ControllerData controllerData, Map<Class<?>, Object> methodPamera, Type returnType) throws Exception{
		Map<String, String> heads = new HashMap<>();
		heads.put("Host", Data.HOST_QUERY);
		heads.put("Origin", Data.Origin_QUERY);
		heads.put("Access-Control-Allow-Origin", Data.HOST_QUERY);
		heads.put("User-Agent", Data.USER_ARGENT_QUERY);
		heads.put("Cookie", Data.COOKIE_QUERY);
		heads.put(DataCenter.CONNECTION, DataCenter.CLOSE);
		if (controllerData.value().contains("initDc")) {
			heads.put("Referer", "https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc&fs=%E6%B7%B1%E5%9C%B3,SZQ&ts=%E5%8D%97%E6%98%8C,NCG&date=2021-01-11&flag=N,N,Y");
		}
		/*如果为Get*/
		if (controllerData.type() == 1) {
			String pamera = "";
			Object[] objs = methodPamera.values().toArray();
			/*只解析第一个参数*/
			if (objs.length > 0 && objs[0] != null) {
				pamera += (Util.objToUrlencoded(objs[0], DataCenter.UTF8.displayName()).replace("$", DataCenter.POINT));
			}
			HttpRequest data = httpUtil.getData(socket, heads, controllerData.value() + pamera);
			return parseHttpRep(data, controllerData.decode(), returnType);
		}else{
			ByteArrayInputStream arrayInputStream = null;
			Object[] objs = methodPamera.values().toArray();
			/*只解析第一个参数*/
			if (objs.length > 0 && objs[0] != null) {
				String objToUrlencoded = Util.objToUrlencoded(objs[0], DataCenter.UTF8.displayName());
//				System.out.println("post地址》》》" + controllerData.value() + "        post参数》》》" + objToUrlencoded);
				byte[] bs = objToUrlencoded.getBytes();
				heads.put(DataCenter.CONTENT_TYPE, DataCenter.URLENCODED + DataCenter.FH + DataCenter.CHARSET + DataCenter.EQUAL + DataCenter.UTF8.displayName());
				heads.put(DataCenter.CONTENT_LENGTH, DataCenter.NULLSTR + bs.length);
				arrayInputStream = new ByteArrayInputStream(bs);
			}
			HttpRequest postData = httpUtil.postData(socket, heads, controllerData.value(), arrayInputStream);
			return parseHttpRep(postData, controllerData.decode(), returnType);
		}
	}
	/*解析返回值，只解析JSON字符串*/
	private Object parseHttpRep(HttpRequest httpRequest, Charset charset, Type returnType) throws Exception{
		BufferRead bufferedReader = httpRequest.getBufferRead();
		IPS.set(httpRequest.getRemoteAddress());
		String contentType = httpRequest.getContentType();
		/*如果有编码，以contentType设置编码为主*/
		String decode = Util.getContentTypeCharset(contentType);
		charset = decode == null ? charset : Charset.forName(decode);
		if (contentType != null) {
			/*如果是JSON类型*/
			String lowerCase = contentType.toLowerCase();
			String size = httpRequest.getContentLength();
			bufferedReader.setMaxSize(size == null ? 0 : Long.parseLong(size));
			byte[] full = bufferedReader.readFull();
			if (lowerCase.contains(DataCenter.JSON)) {
				return JSONObject.parseObject(new String(full, charset), returnType);
			}else if(lowerCase.contains("text/html")) {
				return new String(full, charset);
			}
		}
		return null;
	}
}
