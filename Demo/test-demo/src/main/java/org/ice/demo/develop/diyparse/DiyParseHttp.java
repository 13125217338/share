package org.ice.demo.develop.diyparse;

import com.alibaba.fastjson.JSONObject;
import com.http.DataCenter;
import com.icecream.annotation.HtmlParse;
import com.icecream.constant.HtmlParseIn;
import com.util.MyUtil;

/**
 * @作者 ChengShi
 * @日期 2020-05-15 13:05:25
 * @版本 1.0
 * @描述 自定义解析前端传递类型值
 */
@HtmlParse
public class DiyParseHttp implements HtmlParseIn{

	@Override
	public Object parse(Class<?> type, String pamera, String pameraName, String contentType) {
		/*contentType不会为NULL*/
		if (contentType.toLowerCase().contains(DataCenter.JSON)) {
			return parseJson(type, pamera, pameraName);
		}
		return pamera;
	}
	
	/** 解析JSON */
	private Object parseJson(Class<?> type, Object pamera, String pameraName){
		if (MyUtil.isNotBlank(pameraName)) {return JSONObject.parseObject(pamera.toString()).getObject(pameraName, type);}
		else{return JSONObject.parseObject(pamera.toString(), type);}
	}
}
