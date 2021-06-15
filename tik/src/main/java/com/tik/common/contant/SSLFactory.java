package com.tik.common.contant;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

/**
 * @作者 ChengShi
 * @日期 2021年1月10日
 * @版本 1.0
 * @描述 
 */
public class SSLFactory implements com.http.SSLFactory{

	@Override
	public SSLContext getSSLContext() {
		try {
			SSLContext ctx;
			KeyManagerFactory kmf;
			KeyStore ks;
			char[] passphrase = "changeit".toCharArray();
			
			ctx = SSLContext.getInstance("TLS");
			kmf = KeyManagerFactory.getInstance("SunX509");
			ks = KeyStore.getInstance("JKS");
			
			ks.load(new FileInputStream("C:\\Program Files (x86)\\Java\\jdk1.8.0_161" + "/jre/lib/security/cacerts"), passphrase);
			
			kmf.init(ks, passphrase);
			ctx.init(kmf.getKeyManagers(), null, null);
			return ctx;
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}

}
