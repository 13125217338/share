package org.ice.demo.remote;

import org.ice.demo.common.constant.Data;
import org.ice.demo.vo.IPVo;

import com.icecream.annotation.GetMapping;
import com.icecream.annotation.Remote;

/**
 * @作者 ChengShi
 * @日期 2021-09-06 10:50:12
 * @版本 1.0
 * @描述 ip地址查询
 */
@Remote(host = "ip.ws.126.net", port = "80", type = Data.HTTP)
public interface IPInfoRemote{

	@GetMapping("/ipquery")
	public String getIpAddress(IPVo ipVo);
}
