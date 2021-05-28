package org.ice.demo.develop.diyparse;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.ice.demo.common.constant.Data;

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

/**
 * @作者 ChengShi
 * @日期 2020-05-16 01:39:08
 * @版本 1.0
 * @描述 自定义解析remote接口socket请求
 */
@RemoteParse
public class SocketParse implements RemoteParseIn{
	@Autowired
	private HttpUtil httpUtil;
	
	@Override
	public Object parse(Socket socket, int type, String value, ControllerData controllerData, Map<Class<?>, Object> methodPamera, Type returnType) throws Throwable{
		switch (type) {
			case Data.HTTP: return parseHttp(socket, controllerData, methodPamera, returnType, value);
		}
		return null;
	}
	
	/*解析HTTP协议*/
	private Object parseHttp(Socket socket, ControllerData controllerData, Map<Class<?>, Object> methodPamera, Type returnType, String value) throws Exception{
		/*如果为Get*/
		if (controllerData.type() == 1) {
			Map<String, String> heads = new HashMap<>();
			String val = DataCenter.NULLSTR;
			Object[] objs = methodPamera.values().toArray();
			if (objs.length > 0 && objs[0] != null) {
				heads.put("Host", socket.getInetAddress().getHostName());
				val = Util.objToUrlencoded(objs[0], DataCenter.UTF8.displayName());
			}
			HttpRequest getData = httpUtil.getData(socket, heads, value + controllerData.value() + DataCenter.WHY + val);
			return parseHttpRep(getData, controllerData.decode(), returnType);
		}else{
			Map<String, String> heads = new HashMap<>();
			byte[] datas = null;
			Object[] objs = methodPamera.values().toArray();
			/*只解析第一个参数*/
			if (objs.length > 0 && objs[0] != null) {
				heads.put("Host", socket.getInetAddress().getHostName());
				String jsonString = JSONObject.toJSONString(objs[0]);
				datas = jsonString.getBytes(Data.JSON_UTF8.getEncode());
				heads.put(DataCenter.CONTENT_TYPE, Data.JSON_UTF8.getContentType());
				heads.put(DataCenter.CONTENT_LENGTH, DataCenter.NULLSTR + datas.length);
			}
			HttpRequest postData = httpUtil.postData(socket, heads, value + controllerData.value(), null, datas);
			return parseHttpRep(postData, controllerData.decode(), returnType);
		}
	}
	/*解析返回值，只解析JSON字符串*/
	private Object parseHttpRep(HttpRequest httpRequest, Charset charset, Type returnType) throws Exception{
		BufferRead bufferedReader = httpRequest.getBufferRead();
		String contentType = httpRequest.getContentType();
		/*如果有编码，以contentType设置编码为主*/
		String decode = Util.getContentTypeCharset(contentType);
		charset = decode == null ? charset : Charset.forName(decode);
		if (contentType != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			String size = httpRequest.getContentLength();
			if (size == null) {
				byte[] cacheBytes = bufferedReader.getCacheBytes();int len = -1;
				out.write(cacheBytes);
				while((len = bufferedReader.getHttpRead().read(cacheBytes)) != -1){out.write(cacheBytes, 0, len);}
			}else{
				bufferedReader.setMaxSize(Long.parseLong(size));
				out.write(bufferedReader.readFull());
			}
			/*如果是JSON类型*/
			if (contentType.toLowerCase().contains(DataCenter.JSON)) {
				return JSONObject.parseObject(out.toString(charset.displayName()), returnType);
			}
			/*如果返回String且类型是text*/
			if (String.class.getName().equals(returnType.getTypeName()) && contentType.toLowerCase().contains("text")) {
				return out.toString(charset.displayName());
			}
		}
		return null;
	}
}
