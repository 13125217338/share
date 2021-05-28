package org.ice.demo.common.constant;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import com.http.adapter.SSLAdapter;

/**
 * @作者 ChengShi
 * @日期 2021-09-06 10:55:38
 * @版本 1.0
 * @描述 https请求
 */
public class HttpsRequest implements SSLAdapter{
	@Override
	public SSLContext getSSLContext() {
		try {return SSLContext.getDefault();}
		catch (NoSuchAlgorithmException e) {return null;}
	}
}
