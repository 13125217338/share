package com.tik.code.grab.mode;

/**
 * @作者 ChengShi
 * @日期 2021年1月11日
 * @版本 1.0
 * @描述 确认订单
 */
public class ConfirmSingleForQueueMode {
	/*常量*/
	private final String randCode = "";
	private final String purpose_codes = "00";
	private final String seatDetailType = "000";
	private final String whatsSelect = "1";
	private final String roomType = "00";
	private final String dwAll = "N";
	private final String is_jy = "N";
	private final String _json_att = "";
	/*变量*/
	private String passengerTicketStr;
	private String oldPassengerStr;
	private String key_check_isChange;
	private String leftTicketStr;
	private String train_location;
	private String choose_seats;
	private String encryptedData;
	private String REPEAT_SUBMIT_TOKEN;
	
	public String getPassengerTicketStr() {
		return passengerTicketStr;
	}
	public void setPassengerTicketStr(String passengerTicketStr) {
		this.passengerTicketStr = passengerTicketStr;
	}
	public String getOldPassengerStr() {
		return oldPassengerStr;
	}
	public void setOldPassengerStr(String oldPassengerStr) {
		this.oldPassengerStr = oldPassengerStr;
	}
	public String getKey_check_isChange() {
		return key_check_isChange;
	}
	public void setKey_check_isChange(String key_check_isChange) {
		this.key_check_isChange = key_check_isChange;
	}
	public String getLeftTicketStr() {
		return leftTicketStr;
	}
	public void setLeftTicketStr(String leftTicketStr) {
		this.leftTicketStr = leftTicketStr;
	}
	public String getTrain_location() {
		return train_location;
	}
	public void setTrain_location(String train_location) {
		this.train_location = train_location;
	}
	public String getChoose_seats() {
		return choose_seats;
	}
	public void setChoose_seats(String choose_seats) {
		this.choose_seats = choose_seats;
	}
	public String getEncryptedData() {
		return encryptedData;
	}
	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}
	public String getREPEAT_SUBMIT_TOKEN() {
		return REPEAT_SUBMIT_TOKEN;
	}
	public void setREPEAT_SUBMIT_TOKEN(String rEPEAT_SUBMIT_TOKEN) {
		REPEAT_SUBMIT_TOKEN = rEPEAT_SUBMIT_TOKEN;
	}
	public String getRandCode() {
		return randCode;
	}
	public String getPurpose_codes() {
		return purpose_codes;
	}
	public String getSeatDetailType() {
		return seatDetailType;
	}
	public String getWhatsSelect() {
		return whatsSelect;
	}
	public String getRoomType() {
		return roomType;
	}
	public String getDwAll() {
		return dwAll;
	}
	public String getIs_jy() {
		return is_jy;
	}
	public String get_json_att() {
		return _json_att;
	}
	
}
