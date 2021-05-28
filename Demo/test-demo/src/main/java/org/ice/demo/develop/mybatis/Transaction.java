package org.ice.demo.develop.mybatis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @作者 ChengShi
 * @日期 2020-04-30 15:40:05
 * @版本 1.0
 * @描述 事物开启，原子操作，有异常捕获请抛出异常才能事物管理（自动代理该实现的类，该注解只能注解接口方法，实现类要被框架管理，并且在自动注入该类时必须使用接口注入）
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transaction {}
