package com.tik.code.grab.mode;

/**
 * @作者 ChengShi
 * @日期 2021年1月11日
 * @版本 1.0
 * @描述 预定
 */
public class SubmitOrderRequestMode {
	private String secretStr;
	private String train_date;
	private String back_train_date;
	/*旅行号，dc单程，fc返程*/
	private final String tour_flag = "dc";
	/*标记成人ADULT*/
	private final String purpose_codes = "ADULT";
	/*汉字，出站*/
	private String query_from_station_name;
	/*汉字，到站*/
	private String query_to_station_name;
	
	public String getSecretStr() {
		return secretStr;
	}
	public void setSecretStr(String secretStr) {
		this.secretStr = secretStr;
	}
	public String getTrain_date() {
		return train_date;
	}
	public void setTrain_date(String train_date) {
		this.train_date = train_date;
	}
	public String getBack_train_date() {
		return back_train_date;
	}
	public void setBack_train_date(String back_train_date) {
		this.back_train_date = back_train_date;
	}
	public String getTour_flag() {
		return tour_flag;
	}
	public String getPurpose_codes() {
		return purpose_codes;
	}
	public String getQuery_from_station_name() {
		return query_from_station_name;
	}
	public void setQuery_from_station_name(String query_from_station_name) {
		this.query_from_station_name = query_from_station_name;
	}
	public String getQuery_to_station_name() {
		return query_to_station_name;
	}
	public void setQuery_to_station_name(String query_to_station_name) {
		this.query_to_station_name = query_to_station_name;
	}
	
}
