package org.ice.demo.vo;

/**
 * @作者 ChengShi
 * @日期 2021-09-06 10:53:57
 * @版本 1.0
 * @描述 
 */
public class IPVo {
	/*类型*/
	private String type;
	/*地址*/
	private String ip;
	
	public IPVo(String type, String ip) {this.type = type;this.ip = ip;}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}
