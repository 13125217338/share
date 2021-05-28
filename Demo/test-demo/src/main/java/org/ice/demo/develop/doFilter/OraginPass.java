package org.ice.demo.develop.doFilter;

import com.http.core.DataCenter;
import com.http.core.HttpRequest;
import com.http.core.HttpResponse;
import com.icecream.annotation.DoFilter;
import com.icecream.spi.DoFilterIn;

/**
 * @作者 ChengShi
 * @日期 2021-08-18 14:06:02
 * @版本 1.0
 * @描述 来源通过（跨域放行）
 */
@DoFilter(level = -1, path = "")
public class OraginPass implements DoFilterIn{
	@Override
	public boolean doFilter(HttpRequest httpRequest, HttpResponse httpResponse) throws Throwable {
		httpResponse.setAllowHeaders("*");
		httpResponse.setAllowMethods("*");
		httpResponse.setAllowOrigin("*");
		/*设置直接返回*/
		if ("OPTIONS".equalsIgnoreCase(httpRequest.getRequestType())) {
			httpResponse.writeHead(DataCenter.AGREE_OK, false);
			return false;
		}
		return true;
	}
}
