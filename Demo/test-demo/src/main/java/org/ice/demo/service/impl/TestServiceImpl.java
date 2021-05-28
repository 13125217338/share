package org.ice.demo.service.impl;

import org.ice.demo.common.util.UserTokenUtil;
import org.ice.demo.common.vo.TokenVo;
import org.ice.demo.remote.IPInfoRemote;
import org.ice.demo.service.TestService;
import org.ice.demo.vo.IPVo;

import com.http.core.HttpRequest;
import com.icecream.annotation.Autowired;
import com.icecream.annotation.Service;
import com.icecream.auth.IceToken.Token;
import com.log.Log;
import com.log.LogFactory;

/**
 * @作者 ChengShi
 * @日期 2021-12-01 15:43:47
 * @版本 1.0
 * @描述 测试服务实现
 */
@Service
public class TestServiceImpl implements TestService{
	/*日志*/
	private Log log = LogFactory.getLog();
	@Autowired
	private UserTokenUtil userTokenUtil;
	@Autowired
	private IPInfoRemote ipInfoRemote;

	@Override
	public String test(String name) {
		log.debug("测试名称》》 " + name);
		return name;
	}

	@Override
	public String getTK(HttpRequest httpRequest) {
		TokenVo tokenVo = new TokenVo("cs", "测试", httpRequest.getRemoteAddress());
		return userTokenUtil.getToken(new Token<TokenVo>("SHA-256", tokenVo));
	}

	@Override
	public TokenVo parseTK(HttpRequest httpRequest) {
		String tk = httpRequest.getHead("Token");
		return userTokenUtil.getData(tk, "验证不通过，请检查TK值是否正确！");
	}
	
	@Override
	public String getIpName(String ip) {
		return ipInfoRemote.getIpAddress(new IPVo("json", ip));
	}
}
