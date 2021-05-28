package org.ice.demo.common.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.ice.demo.common.vo.TokenVo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.http.core.DataCenter;
import com.icecream.annotation.Compont;
import com.icecream.annotation.Init;
import com.icecream.annotation.Value;
import com.icecream.auth.DomainIceToken;
import com.icecream.auth.IceToken.Serialize;
import com.icecream.auth.IceToken.Token;
import com.log.Log;
import com.log.LogFactory;

/**
 * @作者 ChengShi
 * @日期 2021-08-18 15:49:27
 * @版本 1.0
 * @描述 用户验证工具
 */
@Compont
public class UserTokenUtil extends DomainIceToken<TokenVo>{
	@Value("${TK.flushTime}")
	private int flushTime;
	/*日志*/
	private final Log log = LogFactory.getLog();
	/*aes密钥*/
	private byte[] password = null;private Cipher ENCODE = null;private Cipher DECODE = null;
	/*序列化方式*/
	private Serialize<TokenVo> serialize = new Serialize<TokenVo>() {
		@Override
		public byte[] xlh(Token<TokenVo> data, byte[] token) {
			try {
				pd(token);
				return ENCODE.doFinal(JSONObject.toJSONBytes(data));
			} catch (Exception e) {log.error(e);return null;}
		}
		@Override
		public Token<TokenVo> fxl(byte[] vals, byte[] token) {
			try {
				pd(token);
				return JSON.parseObject(new String(DECODE.doFinal(vals), DataCenter.UTF8),
										new TypeReference<Token<TokenVo>>(){});
			} catch (Exception e) {log.error(e);return null;}
		}
	};
	/*获取加密对象*/
	private Cipher getCipher(boolean isEn){
		try {
			KeyGenerator instance = KeyGenerator.getInstance("AES");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(password);
			instance.init(128, random);
			SecretKeySpec keySpec = new SecretKeySpec(instance.generateKey().getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(isEn ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec);
			return cipher;
		} catch (Exception e) {log.error(e);return null;}
	}
	/*判断并设置*/
	private void pd(byte[] token){
		if (!Arrays.equals(token, password)) {
			password = token;
			ENCODE = getCipher(true);
			DECODE = getCipher(false);
		}
	}
	
	@Init
	private void init() throws Exception{
		super.init(UUID.randomUUID().toString().getBytes(), serialize, flushTime);
		log.debug("TK刷新时间》》》 " + super.getTokenON().getTimeout());
	}
}
