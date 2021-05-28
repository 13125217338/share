package org.ice.demo.common.util;

import org.ice.demo.common.vo.TokenVo;

import com.http.core.Web;

/**
 * @作者 ChengShi
 * @日期 2021-08-16 17:32:15
 * @版本 1.0
 * @描述 
 */
public final class CommonUitl {
	private CommonUitl(){}
	
	/**
	 * @描述 获取线程缓存令牌数据
	 * @return 令牌数据
	 */
	public static TokenVo get(){return Web.getTv().get(TokenVo.class);}
	
	/**
	 * @描述 设置线程缓存令牌数据
	 * @param tokenMode 令牌数据
	 */
	public static void set(TokenVo tokenMode){Web.getTv().set(tokenMode);}
}
