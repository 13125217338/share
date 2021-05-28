package org.ice.demo.common.vo;

import java.io.Serializable;

import com.util.MyUtil;

/**
 * @作者 ChengShi
 * @日期 2021-08-16 17:52:10
 * @版本 1.0
 * @描述 存于前端
 */
public class TokenVo implements Serializable{
	private static final long serialVersionUID = 1L;
	/*用户登录ip*/
	private String ip;
	/*用户登录账号*/
	private String account;
	/*用户名*/
	private String name;
	public TokenVo() {}
	
	public TokenVo(String account, String name, String ip) {
		this.ip = ip;
		this.account = account;
		this.name = MyUtil.isNotBlank(name) ? name : String.valueOf(System.currentTimeMillis());
	}
	
	public String getIp() {
		return ip;
	}
	public String getAccount() {
		return account;
	}
	public String getName() {
		return name;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
