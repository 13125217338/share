package org.ice.demo.common.vo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @作者 ChengShi
 * @日期 2020-05-20 14:35:07
 * @版本 1.0
 * @描述 返回给前端的数据
 */
public final class RestObject<T> {
	/** 状态 */
	private int state;
	/** 信息 */
	private String msg;
	/** 数据 */
	private T data;
	
	/** 成功 */
	public final static String SUCCESS = "success";
	public final static int OK = 200;
	
	/** 失败 */
	public final static int ERRO = 400;
	
	public int getState() {return state;}
	public void setState(int state) {this.state = state;}
	public String getMsg() {return msg;}
	public void setMsg(String msg) {this.msg = msg;}
	public T getData() {return data;}
	public void setData(T data) {this.data = data;}

	/**
	 * @描述 自定义返回结果
	 * @param state 状态（0表示成功，400表示失败）
	 * @param msg 信息
	 * @param data 数据
	 */
	public RestObject(int state, String msg, T data) {
		this.state = state;
		this.msg = msg;
		this.data = data;
	}
	/** 无参数初始化 */
	public RestObject() {}
	
	/**
	 * @描述 返回成功信息
	 * @return 没有数据
	 */
	public static <T> RestObject<T> newOK(){return new RestObject<T>(OK, SUCCESS, null);}
	
	/**
	 * @描述 返回成功信息与数据
	 * @param data 数据
	 * @return 成功后的数据
	 */
	public static <T> RestObject<T> newOK(T data){return new RestObject<T>(OK, SUCCESS, data);}
	
	/**
	 * @描述 返回失败信息
	 * @param msg 失败信息
	 * @return 没有数据
	 */
	public static <T> RestObject<T> newERRO(String msg){return new RestObject<T>(ERRO, msg, null);}
	
	/**
	 * @描述 重新修改为错误
	 * @param msg 错误信息
	 */
	public void ERRO(String msg){this.state = ERRO;this.msg = msg;this.data = null;}
	
	/**
	 * @描述 重新修改为成功
	 * @param data 成功数据
	 */
	public void OK(T data){this.state = OK;this.msg = SUCCESS;this.data = data;}
	
	/**
	 * @描述 判断是否成功（状态ok，数据不为NULL）
	 * @return true为成功
	 */
	public boolean isSuccess(){return this.state == OK && this.data != null ? true : false;}
	
	@Override
	public String toString() {return JSONObject.toJSONString(this, SerializerFeature.WriteEnumUsingToString);}
}
