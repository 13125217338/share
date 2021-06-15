package com.tik.start;

import com.icecream.annotation.ConfigPath;
import com.icecream.core.IceApplication;

/**
 * @作者 ChengShi
 * @日期 2020-05-07 23:30:38
 * @版本 1.0
 * @描述 主入口
 */
@ConfigPath("config.properties")
public class Application {
	public static void main(String[] args) throws Exception {
		/*开始运行*/
		IceApplication.scan();
	}
}
