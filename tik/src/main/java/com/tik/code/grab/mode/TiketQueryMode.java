package com.tik.code.grab.mode;

/**
 * @作者 ChengShi
 * @日期 2021年1月10日
 * @版本 1.0
 * @描述 票查询实体类
 */
public class TiketQueryMode {
	/*出发日期*/
	private String leftTicketDTO$train_date;
	/*出发站点代号*/
	private String leftTicketDTO$from_station;
	/*到达站点代号*/
	private String leftTicketDTO$to_station;
	/*票类型*/
	private String purpose_codes;
	
	public String getLeftTicketDTO$train_date() {
		return leftTicketDTO$train_date;
	}
	public void setLeftTicketDTO$train_date(String leftTicketDTO$train_date) {
		this.leftTicketDTO$train_date = leftTicketDTO$train_date;
	}
	public String getLeftTicketDTO$from_station() {
		return leftTicketDTO$from_station;
	}
	public void setLeftTicketDTO$from_station(String leftTicketDTO$from_station) {
		this.leftTicketDTO$from_station = leftTicketDTO$from_station;
	}
	public String getLeftTicketDTO$to_station() {
		return leftTicketDTO$to_station;
	}
	public void setLeftTicketDTO$to_station(String leftTicketDTO$to_station) {
		this.leftTicketDTO$to_station = leftTicketDTO$to_station;
	}
	public String getPurpose_codes() {
		return purpose_codes;
	}
	public void setPurpose_codes(String purpose_codes) {
		this.purpose_codes = purpose_codes;
	}
}
