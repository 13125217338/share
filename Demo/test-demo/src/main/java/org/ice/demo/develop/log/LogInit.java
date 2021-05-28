package org.ice.demo.develop.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.icecream.annotation.Autowired;
import com.icecream.annotation.Init;
import com.icecream.annotation.Start;
import com.icecream.core.IceApplication;
import com.log.Log;
import com.log.LogConfigValue;
import com.log.LogFactory;
import com.util.MyUtil;

/**
 * @作者 ChengShi
 * @日期 2020-05-06 01:03:34
 * @版本 1.0
 * @描述 初始日志，之后可以直接通过LogFactory获取日志对象
 */
@Start(level = 0)
public final class LogInit{
	@Autowired
	private Properties properties;
	
	@Init
	private void init() throws Exception{
		/** 初始化日志工厂，第二个参数是回调接口，可以通过接口将信息捕捉发送给邮箱之类的 */
		LogFactory.init(properties).setLogMsg(new DoLog());;
		Log log = LogFactory.getLog();
		/*打印自定义信息*/
		printStart(log);
		log.info("当前日志路径：" + properties.getProperty(LogConfigValue.PATH));
	}
	
	private void printStart(Log log) throws IOException{
		try(InputStream resourceAsStream = IceApplication.getResourceClass().getResourceAsStream("/start.txt");) {
			log.echo(new String(MyUtil.getBytes(resourceAsStream, 256), "UTF-8"));
		}
	}
}
