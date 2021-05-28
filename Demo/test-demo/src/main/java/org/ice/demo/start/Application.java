package org.ice.demo.start;

import com.icecream.annotation.ConfigPath;
import com.icecream.annotation.EnableApi;
import com.icecream.annotation.EnableResource;
import com.icecream.core.IceApplication;

/**
 * @作者 ChengShi
 * @日期 2021-11-26 15:59:28
 * @版本 1.0
 * @描述 启动入口
 */
@ConfigPath(value = "config.properties")
@EnableApi("/api")
@EnableResource("/include")
public class Application {
	public static void main(String[] args) {
		IceApplication.scan();
	}
}
