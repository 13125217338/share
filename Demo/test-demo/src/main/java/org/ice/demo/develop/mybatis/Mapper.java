package org.ice.demo.develop.mybatis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @作者 ChengShi
 * @日期 2020-08-03 11:00:38
 * @版本 1.0
 * @描述 标记使用工厂会话
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mapper {
	/**
	 * @描述 使用哪个会话工厂产生数据库会话（看配置）
	 */
	public Class<?> factory();
}
