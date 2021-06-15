package com.tik.common.contant;

/**
 * @作者 ChengShi
 * @日期 2021年1月11日
 * @版本 1.0
 * @描述 
 */
public enum SeatType {
	硬卧("3");
	private final String code;
	public String getCode() {return this.code;}
	private SeatType(String code) {this.code = code;}
}
