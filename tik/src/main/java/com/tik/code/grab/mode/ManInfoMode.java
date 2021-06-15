package com.tik.code.grab.mode;

import java.util.List;

/**
 * @作者 ChengShi
 * @日期 2021年1月10日
 * @版本 1.0
 * @描述 人物信息
 */
public class ManInfoMode {
	/*是否执行成功*/
	private boolean isExist;
	/*失败信息*/
	private String exMsg;
	/*人物信息集合*/
	private List<ManMode> normal_passengers;
	
	public List<ManMode> getNormal_passengers() {
		return normal_passengers;
	}
	public void setNormal_passengers(List<ManMode> normal_passengers) {
		this.normal_passengers = normal_passengers;
	}
	public boolean isExist() {
		return isExist;
	}
	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}
	public String getExMsg() {
		return exMsg;
	}
	public void setExMsg(String exMsg) {
		this.exMsg = exMsg;
	}

	/**
	 * @作者 ChengShi
	 * @日期 2021年1月10日
	 * @版本 1.0
	 * @描述 人物信息
	 */
	public static class ManMode{
		/*姓名*/
		private String passenger_name;
		/*性别*/
		private String sex_name;
		/*生日*/
		private String born_date;
		/*人物唯一ID类型（1=身份证）*/
		private String passenger_id_type_code;
		/*人物唯一ID类型对应名称*/
		private String passenger_id_type_name;
		/*人物唯一ID类型号码*/
		private String passenger_id_no;
		/*人物类型（1=成人，2=儿童）*/
		private String passenger_type;
		/*人物类型对应名称*/
		private String passenger_type_name;
		/*人物标志*/
		private String passenger_flag;
		/*电话号码*/
		private String mobile_no;
		/*定位ID，对于多个联系人的位置（从0开始）*/
		private String index_id;
		/*人物信息唯一标识*/
		private String allEncStr;
		
		public String getPassenger_name() {
			return passenger_name;
		}
		public void setPassenger_name(String passenger_name) {
			this.passenger_name = passenger_name;
		}
		public String getSex_name() {
			return sex_name;
		}
		public void setSex_name(String sex_name) {
			this.sex_name = sex_name;
		}
		public String getBorn_date() {
			return born_date;
		}
		public void setBorn_date(String born_date) {
			this.born_date = born_date;
		}
		public String getPassenger_id_type_code() {
			return passenger_id_type_code;
		}
		public void setPassenger_id_type_code(String passenger_id_type_code) {
			this.passenger_id_type_code = passenger_id_type_code;
		}
		public String getPassenger_id_type_name() {
			return passenger_id_type_name;
		}
		public void setPassenger_id_type_name(String passenger_id_type_name) {
			this.passenger_id_type_name = passenger_id_type_name;
		}
		public String getPassenger_id_no() {
			return passenger_id_no;
		}
		public void setPassenger_id_no(String passenger_id_no) {
			this.passenger_id_no = passenger_id_no;
		}
		public String getPassenger_type() {
			return passenger_type;
		}
		public void setPassenger_type(String passenger_type) {
			this.passenger_type = passenger_type;
		}
		public String getPassenger_type_name() {
			return passenger_type_name;
		}
		public void setPassenger_type_name(String passenger_type_name) {
			this.passenger_type_name = passenger_type_name;
		}
		public String getPassenger_flag() {
			return passenger_flag;
		}
		public void setPassenger_flag(String passenger_flag) {
			this.passenger_flag = passenger_flag;
		}
		public String getMobile_no() {
			return mobile_no;
		}
		public void setMobile_no(String mobile_no) {
			this.mobile_no = mobile_no;
		}
		public String getIndex_id() {
			return index_id;
		}
		public void setIndex_id(String index_id) {
			this.index_id = index_id;
		}
		public String getAllEncStr() {
			return allEncStr;
		}
		public void setAllEncStr(String allEncStr) {
			this.allEncStr = allEncStr;
		}
		
		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("名称：\"" + passenger_name + "\" ");
			buffer.append("性别：\"" + sex_name + "\" ");
			buffer.append("标志：\"" + passenger_id_type_name + "\" ");
			buffer.append("标志号码：\"" + passenger_id_no + "\" ");
			buffer.append("人物类型：\"" + passenger_type_name + "\" ");
			buffer.append("电话号码：\"" + mobile_no + "\" ");
			return buffer.toString();
		}
	}
}
