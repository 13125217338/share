package com.tik.common.mode;

/**
 * @作者 ChengShi
 * @日期 2021年1月10日
 * @版本 1.0
 * @描述 票返回结果
 */
public class RestObject<T> {
	/*状态码*/
	private int httpstatus;
	/*数据*/
	private T data;
	/*信息*/
	private String messages;
	/*状态*/
	private boolean status;
	
	public int getHttpstatus() {
		return httpstatus;
	}
	public void setHttpstatus(int httpstatus) {
		this.httpstatus = httpstatus;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getMessages() {
		return messages;
	}
	public void setMessages(String messages) {
		this.messages = messages;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
