package com.tik.code.grab.mode;

import java.util.List;
import java.util.Map;

import com.tik.develop.diyparse.SocketParse;
import com.util.MyUtil;

/**
 * @作者 ChengShi
 * @日期 2021年1月10日
 * @版本 1.0
 * @描述 票查询结果实体类
 */
public class TiketQueryResultMode {
	private List<String> result;
	private String flag;
	private Map<String, String> map;

	public List<String> getResult() {
		return result;
	}
	public void setResult(List<String> result) {
		this.result = result;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap( Map<String, String> map) {
		this.map = map;
	}
	
	/**
	 * @作者 ChengShi
	 * @日期 2021年1月10日
	 * @版本 1.0
	 * @描述 票信息
	 */
	public static class TiketMode{
		/*车预定号*/
		private String secretStr;
		/*火车票号*/
		private String train_no;
		/*起始站点*/
		private String from_station_no;
		/*到达站点*/
		private String to_station_no;
		/*seat_types*/
		private String seat_types;
		/*出发日期*/
		private String train_date;
		/*火车名*/
		private String train_name;
		/*起始名称*/
		private String start_name;
		/*结束名称*/
		private String end_name;
		/*起始时间*/
		private String start_time;
		/*结束时间*/
		private String end_time;
		/*总时长*/
		private String all_time;
		/*票码*/
		private String left_ticket;
		/*车序列号*/
		private String train_location;
		/*硬座*/
		private String hard_seat;
		/*二等座（动车高铁专用）*/
		private String two_seat;
		/*硬卧*/
		private String hard_berth;
		
		public String getSecretStr() {
			return secretStr;
		}
		public void setSecretStr(String secretStr) {
			this.secretStr = secretStr;
		}
		public String getTrain_no() {
			return train_no;
		}
		public void setTrain_no(String train_no) {
			this.train_no = train_no;
		}
		public String getFrom_station_no() {
			return from_station_no;
		}
		public void setFrom_station_no(String from_station_no) {
			this.from_station_no = from_station_no;
		}
		public String getTo_station_no() {
			return to_station_no;
		}
		public void setTo_station_no(String to_station_no) {
			this.to_station_no = to_station_no;
		}
		public String getSeat_types() {
			return seat_types;
		}
		public void setSeat_types(String seat_types) {
			this.seat_types = seat_types;
		}
		public String getTrain_date() {
			return train_date;
		}
		public void setTrain_date(String train_date) {
			this.train_date = train_date;
		}
		public String getTrain_name() {
			return train_name;
		}
		public void setTrain_name(String train_name) {
			this.train_name = train_name;
		}
		public String getStart_name() {
			return start_name;
		}
		public void setStart_name(String start_name) {
			this.start_name = start_name;
		}
		public String getEnd_name() {
			return end_name;
		}
		public void setEnd_name(String end_name) {
			this.end_name = end_name;
		}
		public String getStart_time() {
			return start_time;
		}
		public void setStart_time(String start_time) {
			this.start_time = start_time;
		}
		public String getEnd_time() {
			return end_time;
		}
		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}
		public String getAll_time() {
			return all_time;
		}
		public void setAll_time(String all_time) {
			this.all_time = all_time;
		}
		public String getLeft_ticket() {
			return left_ticket;
		}
		public void setLeft_ticket(String left_ticket) {
			this.left_ticket = left_ticket;
		}
		public String getTrain_location() {
			return train_location;
		}
		public void setTrain_location(String train_location) {
			this.train_location = train_location;
		}
		public String getHard_seat() {
			return hard_seat;
		}
		public void setHard_seat(String hard_seat) {
			this.hard_seat = hard_seat;
		}
		public String getTwo_seat() {
			return two_seat;
		}
		public void setTwo_seat(String two_seat) {
			this.two_seat = two_seat;
		}
		public String getHard_berth() {
			return hard_berth;
		}
		public void setHard_berth(String hard_berth) {
			this.hard_berth = hard_berth;
		}
		
		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("车次：\"" + train_name + "\" ");
			buffer.append("乘车日期：\"" + train_date + "\" ");
			buffer.append("起始地点：\"" + start_name + "\" ");
			buffer.append("结束地点：\"" + end_name + "\" ");
			buffer.append("起始时间：\"" + start_time + "\" ");
			buffer.append("结束时间：\"" + end_time + "\" ");
			buffer.append("总时长：\"" + all_time + "\" ");
			if (MyUtil.isNotBlank(hard_seat)) {
				buffer.append("硬座：\"" + hard_seat + "\" ");
				buffer.append("硬卧：\"" + hard_berth + "\" ");
			}else {
				buffer.append("二等座：\"" + two_seat + "\" ");
			}
			buffer.append("IP：\"" + SocketParse.IPS.get() + "\"");
			return buffer.toString();
		}
	}
}
