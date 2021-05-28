package org.ice.demo.develop.diyparse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.http.core.DataCenter;
import com.http.core.HttpRequest;
import com.http.core.HttpResponse;
import com.http.io.BufferRead;
import com.icecream.annotation.DoFilter;
import com.icecream.annotation.Init;
import com.icecream.annotation.Value;
import com.icecream.core.IceApplication;
import com.icecream.spi.DoFilterIn;

/**
 * @作者 ChengShi
 * @日期 2020-05-18 13:07:21
 * @版本 1.0
 * @描述 解析html使用公共模板
 */
@DoFilter(level = 5, path = "/")
public class ParseHtml implements DoFilterIn{
	@Value(value = "${" + com.icecream.constant.WebConfigValue.MAIN + "}")
	private String serverMain;
	@Value(value = "${Server.template}")
	private String templateUri;
	
	/** 是否缓存 */
	private final boolean isCache = false;
	private final Map<String, byte[]> cache = new HashMap<>();
	
	/** 头html信息 */
	private final String HTML_PATH_LEFT = "<!-- 模板文件路径：";
	private final String HTML_PATH_RIGHT = " -->\r\n";
	private final String PREP = "<html>\r\n<head><meta name='viewport' content='width=device-width, initial-scale=1.0 user-scalable=yes' charset='";
	private final String MIDDEN = "'/><title>";
	private String HIND = null;
	private final String FOOT = "</body>\r\n</html>\r\n";
	
	@Init
	private void init() throws IOException{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byteArrayOutputStream.write("</title></head>\r\n<body>\r\n".getBytes(DataCenter.UTF8));
		try(InputStream resourceTemp = IceApplication.getResourceClass().getResourceAsStream(templateUri)){
			if (resourceTemp != null) {
				byte[] bs = new byte[512];
				int len = -1;
				while((len = resourceTemp.read(bs)) != -1){
					byteArrayOutputStream.write(bs, 0, len);
				}
			}
		}
		HIND = byteArrayOutputStream.toString(DataCenter.UTF8.displayName());
	}
	/** 解析需要的一些值 */
	private final String HTML = ".html";
	private final String TEMPLET = "<mode";
	private final String TITLE = "title";
	private final String ENCODE_ = "encode";
	
	@Override
	public boolean doFilter(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
		String uri = httpRequest.getURI();
		/*解析主html*/
		if (uri.equals(DataCenter.PATH_SPLITE)) {
			if (serverMain != null) {
				if(!parseHtml(httpRequest, httpResponse, serverMain)){return false;};
			}
		}
		if(!parseHtml(httpRequest, httpResponse, uri)){return false;};
		return true;
	}
	
	private boolean parseHtml(HttpRequest httpRequest, HttpResponse httpResponse, String resourceUri) throws Exception {
		byte[] bs = cache.get(resourceUri);
		if (bs != null) {
			httpResponse.writeHead(DataCenter.AGREE_OK, true);
			httpResponse.getHttpWrite().write(bs);
			return false;
		}
		/*只解析.html*/
		if (resourceUri.toLowerCase().endsWith(HTML)) {
			try(InputStream asStream = IceApplication.getResourceClass().getResourceAsStream(resourceUri)) {
				if (asStream != null) {
					BufferRead bufferRead = new BufferRead(asStream, httpResponse.getBuffSize());
					String mode = getMode(bufferRead);
					if (mode != null) {
						String enc = getValue(mode, ENCODE_);
						Charset encodeHtml = (enc == null ? DataCenter.UTF8 : Charset.forName(enc));
						httpResponse.writeHead(DataCenter.AGREE_OK, true);
						/*直接读取所有字节*/
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						/*打印模板路径*/
						byteArrayOutputStream.write(HTML_PATH_LEFT.getBytes(encodeHtml));
						byteArrayOutputStream.write(resourceUri.getBytes(encodeHtml));
						byteArrayOutputStream.write(HTML_PATH_RIGHT.getBytes(encodeHtml));
						/*写入模板*/
						byteArrayOutputStream.write(PREP.getBytes(encodeHtml));
						byteArrayOutputStream.write((encodeHtml.displayName()+ MIDDEN).getBytes(encodeHtml));						
						String value = getValue(mode, TITLE);
						if (value != null) {byteArrayOutputStream.write(new String(value.getBytes(bufferRead.getCharset()), DataCenter.UTF8).getBytes(encodeHtml));}
						byteArrayOutputStream.write(HIND.getBytes(encodeHtml));
						/*因为读了一行有缓存数据，所以先读取缓存数据*/
						byteArrayOutputStream.write(bufferRead.getCacheBytes());
						int len = -1;byte[] datas = new byte[bufferRead.getBuffSize()];
						while((len = bufferRead.getHttpRead().read(datas)) != -1){
							byteArrayOutputStream.write(datas, 0, len);
						}
						byteArrayOutputStream.write(FOOT.getBytes(encodeHtml));
						byte[] data = byteArrayOutputStream.toByteArray();
						/*是否缓存*/
						if (isCache) {cache.put(resourceUri, data);}
						httpResponse.getHttpWrite().write(data);
						/*自定义解析就不用框架解析，返回false*/
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/** 获取前端标志，只能第一行 
	 * @throws Exception */
	private String getMode(BufferRead bufferedReader) throws Exception{
		String readLine = bufferedReader.readLine(false);
		if (readLine.contains(TEMPLET)) {
			return readLine;
		}
		return null;
	}
	
	/** 获取标志中的值 */
	private String getValue(String mode, String name){
		int indexOf = mode.indexOf(name);
		if (indexOf != -1) {
			int i1 = mode.indexOf("\"", indexOf);
			int i2 = mode.indexOf("\"", i1 + 1);
			if (i1 != -1 && i2 != -1) {
				return mode.substring(i1 + 1, i2);
			}
		}
		return null;
	}

}
