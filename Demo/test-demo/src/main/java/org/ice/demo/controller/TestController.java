package org.ice.demo.controller;

import org.ice.demo.common.vo.RestObject;
import org.ice.demo.common.vo.TokenVo;
import org.ice.demo.service.TestService;

import com.http.core.HttpRequest;
import com.icecream.annotation.Autowired;
import com.icecream.annotation.Controller;
import com.icecream.annotation.PameraName;
import com.icecream.annotation.PostMapping;

/**
 * @作者 ChengShi
 * @日期 2021-12-01 14:23:48
 * @版本 1.0
 * @描述 测试接口
 */
@Controller(api = "测试接口")
public class TestController{
	@Autowired
	private TestService testService;
	
	@PostMapping(value = "/test", api = "测试")
	public RestObject<String> test(@PameraName("name") String name){
		return RestObject.newOK(testService.test(name));
	}
	
	@PostMapping(value = "/getTK", api = "获取动态令牌")
	public RestObject<String> getTK(HttpRequest httpRequest){
		return RestObject.newOK(testService.getTK(httpRequest));
	}
	
	@PostMapping(value = "/parseTK", api = "解析TK内的值（请将TK值放入头的Token键中）")
	public RestObject<TokenVo> parseTK(HttpRequest httpRequest){
		return RestObject.newOK(testService.parseTK(httpRequest));
	}
	
	@PostMapping(value = "/getIpName", api = "通过IP查询其所在地")
	public RestObject<String> getIpName(@PameraName("ip") String ip){
		return RestObject.newOK(testService.getIpName(ip));
	}
}
