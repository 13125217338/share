package com.tik.code.grab.mode;

/**
 * @作者 ChengShi
 * @日期 2021年1月11日
 * @版本 1.0
 * @描述 提交订单实体类
 */
public class GetQueueCountMode {
	private final String _json_att = "";
	private final String purpose_codes = "00";
	private String train_date;
	private String train_no;
	private String stationTrainCode;
	private String seatType;
	private String fromStationTelecode;
	private String toStationTelecode;
	private String leftTicket;
	private String train_location;
	private String REPEAT_SUBMIT_TOKEN;
	
	public String getTrain_date() {
		return train_date;
	}
	public void setTrain_date(String train_date) {
		this.train_date = train_date;
	}
	public String getTrain_no() {
		return train_no;
	}
	public void setTrain_no(String train_no) {
		this.train_no = train_no;
	}
	public String getStationTrainCode() {
		return stationTrainCode;
	}
	public void setStationTrainCode(String stationTrainCode) {
		this.stationTrainCode = stationTrainCode;
	}
	public String getSeatType() {
		return seatType;
	}
	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}
	public String getFromStationTelecode() {
		return fromStationTelecode;
	}
	public void setFromStationTelecode(String fromStationTelecode) {
		this.fromStationTelecode = fromStationTelecode;
	}
	public String getToStationTelecode() {
		return toStationTelecode;
	}
	public void setToStationTelecode(String toStationTelecode) {
		this.toStationTelecode = toStationTelecode;
	}
	public String getLeftTicket() {
		return leftTicket;
	}
	public void setLeftTicket(String leftTicket) {
		this.leftTicket = leftTicket;
	}
	public String getPurpose_codes() {
		return purpose_codes;
	}
	public String getTrain_location() {
		return train_location;
	}
	public void setTrain_location(String train_location) {
		this.train_location = train_location;
	}
	public String getREPEAT_SUBMIT_TOKEN() {
		return REPEAT_SUBMIT_TOKEN;
	}
	public void setREPEAT_SUBMIT_TOKEN(String rEPEAT_SUBMIT_TOKEN) {
		REPEAT_SUBMIT_TOKEN = rEPEAT_SUBMIT_TOKEN;
	}
	public String get_json_att() {
		return _json_att;
	}
	
	/**
	 * @作者 ChengShi
	 * @日期 2021年1月11日
	 * @版本 1.0
	 * @描述 提交返回信息
	 */
	public static class QueueResult{
		/*统计*/
		private String count;
		/*剩余票数*/
		private String ticket;
		private String op_2;
		private String countT;
		private String op_1;
		
		public String getCount() {
			return count;
		}
		public void setCount(String count) {
			this.count = count;
		}
		public String getTicket() {
			return ticket;
		}
		public void setTicket(String ticket) {
			this.ticket = ticket;
		}
		public String getOp_2() {
			return op_2;
		}
		public void setOp_2(String op_2) {
			this.op_2 = op_2;
		}
		public String getCountT() {
			return countT;
		}
		public void setCountT(String countT) {
			this.countT = countT;
		}
		public String getOp_1() {
			return op_1;
		}
		public void setOp_1(String op_1) {
			this.op_1 = op_1;
		}
		
	}
}
