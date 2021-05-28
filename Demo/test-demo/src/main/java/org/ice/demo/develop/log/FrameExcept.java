package org.ice.demo.develop.log;


import org.ice.demo.common.constant.RestException;
import org.ice.demo.common.vo.RestObject;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.http.core.DataCenter;
import com.http.core.HttpResponse;
import com.http.core.Web;
import com.icecream.annotation.Compont;
import com.icecream.annotation.Init;
import com.icecream.core.IceApplication.IceConfig;
import com.icecream.spi.IceThrowable;
import com.log.Log;
import com.log.LogFactory;
import com.util.MyUtil;

/**
 * @作者 ChengShi
 * @日期 2020-05-07 09:34:05
 * @版本 1.0
 * @描述 全局框架异常捕获
 */
@Compont(level = -1)
public class FrameExcept implements IceThrowable{
	/** 使用自定义日志 */
	private Log log = LogFactory.getLog();
	
	@Init
	private void init() {IceConfig.setIceThrowable(this);}
	
	@Override
	public void throwable(Throwable e) {
		try {
			HttpResponse httpResponse = Web.getTv().get(HttpResponse.class);
			if (httpResponse != null) {
				String msg = e.getMessage();
				if (MyUtil.isNotBlank(msg) &&  msg.length() > 100) {msg = RestException.OTHER_EXP.exp().getMessage();}
				/*设置返回消息*/
				RestObject<String> result = RestObject.newERRO(msg);
				byte[] datas = JSONObject.toJSONBytes(result, SerializerFeature.WriteEnumUsingToString);
				httpResponse.setContentType(DataCenter.JSON_UTF8);
				httpResponse.setContentLength(datas.length);
				httpResponse.writeHead(DataCenter.AGREE_OK, false);
				httpResponse.getHttpWrite().write(datas);
			}
			log.error(e);
		} catch (Exception e2) {log.error(e2);}
	}
}
