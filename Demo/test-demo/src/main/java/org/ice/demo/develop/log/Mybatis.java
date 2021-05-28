package org.ice.demo.develop.log;

import org.apache.ibatis.logging.stdout.StdOutImpl;

import com.log.Log;
import com.log.LogFactory;

/**
 * @作者 ChengShi
 * @日期 2020-05-06 01:02:36
 * @版本 1.0
 * @描述 捕获mybatis输出的日志信息
 */
public final class Mybatis extends StdOutImpl{
	/** 使用自定义日志 */
	private Log log = LogFactory.getLog();
	public Mybatis(String clazz) {
		super(clazz); 
	}
	
	@Override
	public void debug(String s) {
		log.debug(s);
	}
	
	@Override
	public void error(String s) {
		log.error(s);
	}
	
	@Override
	public void error(String s, Throwable e) {
		log.error(s, e);
	}
	
	@Override
	public void warn(String s) {
		log.warn(s);
	}
	
	@Override
	public void trace(String s) {}
	
}
