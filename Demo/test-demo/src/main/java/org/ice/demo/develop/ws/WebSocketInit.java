package org.ice.demo.develop.ws;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.http.core.DataCenter;
import com.icecream.annotation.Compont;
import com.icecream.annotation.Init;
import com.icecream.annotation.Value;
import com.log.Log;
import com.log.LogFactory;
import com.ws.WS;
import com.ws.WSEvent;

/**
 * @作者 ChengShi
 * @日期 2021-12-01 14:49:34
 * @版本 1.0
 * @描述 ws初始化
 */
@Compont
public class WebSocketInit {
	/*日志*/
	private Log log = LogFactory.getLog();
	@Value("${Ws.port}")
	private int port = 9999;
	
	@Init
	private void init() throws IOException{
		/*初始化*/
		WS.init(port, null, new WSEvent() {
			/*存储数据*/
			private Map<String, ByteArrayOutputStream> DATAS = new TreeMap<>();
			@Override
			public String onOpen(String path, String parma) {
				DATAS.put(parma, new ByteArrayOutputStream());
				return parma;
			}
			
			@Override
			public void onMessage(byte[] datas, int len, String sessionId) throws Throwable {
				if (datas == null) {
					byte[] data = DATAS.get(sessionId).toByteArray();
					DATAS.get(sessionId).reset();
					/*打印传递信息*/
					log.debug(new String(data));
					/*写回前端*/
					byte[] msg = "已收到回复！".getBytes(DataCenter.UTF8);
					WS.write(port, msg, msg.length);
				}else{DATAS.get(sessionId).write(datas, 0, len);}
			}
			
			@Override
			public void onException(Throwable e, String sessionId) {log.error(String.format("会话(%s)出现异常！", sessionId), e);}
			@Override
			public void onClose(String sessionId) {log.info(String.format("会话(%s)已关闭！", sessionId));DATAS.remove(sessionId);}
		});
	}
}
