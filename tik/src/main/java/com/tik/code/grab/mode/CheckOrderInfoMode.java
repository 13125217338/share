package com.tik.code.grab.mode;

/**
 * @作者 ChengShi
 * @日期 2021年1月11日
 * @版本 1.0
 * @描述 检票实体类
 */
public class CheckOrderInfoMode {
	/*常量*/
	private final String cancel_flag = "2";
	private final String bed_level_order_num = "000000000000000000000000000000";
	private final String tour_flag = "dc";
	private final String randCode = "";
	private final String whatsSelect = "1";
	private final String sessionId = "";
	private final String sig = "";
	private final String scene = "nc_login";
	private final String _json_att = "";
	/*变量*/
	private String passengerTicketStr;
	private String oldPassengerStr;
	private String REPEAT_SUBMIT_TOKEN;
	
	public String getCancel_flag() {
		return cancel_flag;
	}
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
	public String getREPEAT_SUBMIT_TOKEN() {
		return REPEAT_SUBMIT_TOKEN;
	}
	public void setREPEAT_SUBMIT_TOKEN(String rEPEAT_SUBMIT_TOKEN) {
		REPEAT_SUBMIT_TOKEN = rEPEAT_SUBMIT_TOKEN;
	}
	public String getBed_level_order_num() {
		return bed_level_order_num;
	}
	public String getTour_flag() {
		return tour_flag;
	}
	public String getRandCode() {
		return randCode;
	}
	public String getWhatsSelect() {
		return whatsSelect;
	}
	public String getSessionId() {
		return sessionId;
	}
	public String getSig() {
		return sig;
	}
	public String getScene() {
		return scene;
	}
	public String get_json_att() {
		return _json_att;
	}
	
	/**
	 * @作者 ChengShi
	 * @日期 2021年1月11日
	 * @版本 1.0
	 * @描述 检票返回值
	 */
	public static class CheckResult{
		/*能否选床位*/
		private String canChooseBeds;
		/*能否选座位*/
		private String canChooseSeats;
		/*选择的座位标志*/
		private String choose_Seats;
		private String isCanChooseMid;
		private String ifShowPassCodeTime;
		/*提交状态*/
		private boolean submitStatus;
		private String smokeStr;
		
		public String getCanChooseBeds() {
			return canChooseBeds;
		}
		public void setCanChooseBeds(String canChooseBeds) {
			this.canChooseBeds = canChooseBeds;
		}
		public String getCanChooseSeats() {
			return canChooseSeats;
		}
		public void setCanChooseSeats(String canChooseSeats) {
			this.canChooseSeats = canChooseSeats;
		}
		public String getChoose_Seats() {
			return choose_Seats;
		}
		public void setChoose_Seats(String choose_Seats) {
			this.choose_Seats = choose_Seats;
		}
		public String getIsCanChooseMid() {
			return isCanChooseMid;
		}
		public void setIsCanChooseMid(String isCanChooseMid) {
			this.isCanChooseMid = isCanChooseMid;
		}
		public String getIfShowPassCodeTime() {
			return ifShowPassCodeTime;
		}
		public void setIfShowPassCodeTime(String ifShowPassCodeTime) {
			this.ifShowPassCodeTime = ifShowPassCodeTime;
		}
		public boolean isSubmitStatus() {
			return submitStatus;
		}
		public void setSubmitStatus(boolean submitStatus) {
			this.submitStatus = submitStatus;
		}
		public String getSmokeStr() {
			return smokeStr;
		}
		public void setSmokeStr(String smokeStr) {
			this.smokeStr = smokeStr;
		}
		
	}
}
