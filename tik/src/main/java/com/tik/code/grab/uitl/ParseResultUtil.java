package com.tik.code.grab.uitl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.tik.code.grab.mode.InitDC.DCData;
import com.tik.code.grab.mode.TiketQueryResultMode.TiketMode;

/**
 * @作者 ChengShi
 * @日期 2021年1月10日
 * @版本 1.0
 * @描述 
 */
public class ParseResultUtil {
	
	private final static String S = "[|]";
	
	/**
	 * @描述 解析结果
	 * @param results 结果集合
	 * @param map 映射名称
	 * @return 结果
	 */
	public static List<TiketMode> parseResult(List<String> results, Map<String, String> map) {
		List<TiketMode> modes = new ArrayList<>();
		for (String result : results) {
			TiketMode tiketMode = new TiketMode();
			String[] s = result.split(S);
			/*参数*/
			tiketMode.setSecretStr(s[0]);
			tiketMode.setTrain_no(s[2]);
			tiketMode.setTrain_name(s[3]);
			tiketMode.setStart_name(map.get(s[6]));
			tiketMode.setEnd_name(map.get(s[7]));
			tiketMode.setStart_time(s[8]);
			tiketMode.setEnd_time(s[9]);
			tiketMode.setAll_time(s[10]);
			tiketMode.setLeft_ticket(s[12]);
			tiketMode.setTrain_date(s[13]);
			tiketMode.setTrain_location(s[15]);
			tiketMode.setFrom_station_no(s[16]);
			tiketMode.setTo_station_no(s[17]);
			tiketMode.setSeat_types(s[35]);
			/*座位*/
			tiketMode.setHard_seat(s[29]);
			tiketMode.setHard_berth(s[28]);
			tiketMode.setTwo_seat(s[30]);
			modes.add(tiketMode);
		}
		return modes;
	}
	
	/**
	 * @描述 获取验证会话
	 * @param tokenStr 唯一提交会话
	 * @return 验证会话
	 */
	public static DCData getTOKEN(String tokenStr) {
		DCData data = new DCData();
		int tk_f = tokenStr.indexOf("globalRepeatSubmitToken");
		int tk_l = tokenStr.indexOf("'", tk_f) + 1;
		int tk_r = tokenStr.indexOf("'", tk_l);
		data.setGlobalRepeatSubmitToken(tokenStr.substring(tk_l, tk_r));
		int kc_f = tokenStr.indexOf("key_check_isChange", tk_r);
		int kc_l = tokenStr.indexOf(":'", kc_f) + 2;
		int kc_r = tokenStr.indexOf("'", kc_l);
		data.setKey_check_isChange(tokenStr.substring(kc_l, kc_r));
		return data;
	}
	
	/**
	 * @描述 获取火车的GMT时间
	 * @param time 20210101类型时间
	 * @return GMT时间
	 * @throws ParseException 
	 */
	public static String getGMTTime(String time) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMDD");
		Date parse = simpleDateFormat.parse(time);
		SimpleDateFormat sf1 = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss", Locale.US);
		return sf1.format(parse) + " GMT+0800 (中国标准时间)";
	}
	
	/**
	 * @描述 转换时间
	 * @param time 20210101类型时间
	 * @return 2021-01-01时间
	 * @throws ParseException 
	 */
	public static String getTime(String time) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMDD");
		Date parse = simpleDateFormat.parse(time);
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-DD");
		return sf1.format(parse);
	}
}
