package org.ice.demo.service;

import org.ice.demo.common.vo.TokenVo;

import com.http.core.HttpRequest;

/**
 * @作者 ChengShi
 * @日期 2021-12-01 15:35:15
 * @版本 1.0
 * @描述 测试服务
 */
public interface TestService {
	/**
	 * @描述 测试
	 * @param name 测试值
	 * @return 测试结果
	 */
	public String test(String name);
	
	/**
	 * @描述 获取令牌码
	 * @param httpRequest 请求
	 * @return 令牌码
	 */
	public String getTK(HttpRequest httpRequest);
	
	/**
	 * @描述 解析令牌信息
	 * @param httpRequest 请求
	 * @return 令牌内的信息
	 */
	public TokenVo parseTK(HttpRequest httpRequest);
	
	/**
	 * @描述 通过IP查询所在地
	 * @param ip 待查询的IP
	 * @return ip所在地
	 */
	public String getIpName(String ip);
}
