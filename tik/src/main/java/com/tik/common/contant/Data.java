package com.tik.common.contant;

import com.http.DataCenter;
import com.icecream.core.Msg;

/**
 * @作者 ChengShi
 * @日期 2020-05-17 17:28:06
 * @版本 1.0
 * @描述 数据接口
 */
public interface Data {
	/** 响应JSON编码UTF-8类型Msg */
	public final static Msg JSON_UTF8 = new Msg(DataCenter.JSON + DataCenter.FH + DataCenter.CHARSET + DataCenter.EQUAL + "UTF-8");
	
	/** http类型 */
	public final static int HTTP_CODE = 1;
	
	/** 状态成功 */
	public final static int HTTP_STATE_OK = 200;
	
	/** 查询ID */
	public final static String QUERY_ID = "QUERY_ID";
	
	/** 查询参数 */
	public final static String USER_ARGENT_QUERY = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";
	/** 查询HOST */
	public final static String HOST_QUERY = "kyfw.12306.cn";
	public final static String Origin_QUERY = "https://kyfw.12306.cn";
	/** 查询COOKIE */
	public final static String COOKIE_QUERY_ = "";
	public final static String COOKIE_QUERY = "_uab_collina=161036266205892939453545; JSESSIONID=933E29FE06CED6D2C449B9E5678DC6D1; tk=5_sCdhBhxGBwTbsXN0lc_17T6iB3DIfj7uBgWSpAlCshuc1c0; RAIL_EXPIRATION=1610654032137; RAIL_DEVICEID=B6aPRbV-ZiRIksSQjLgyO-QlevRffo8mVm_ckwelwZ2bben_4NQ95ATD3p4ifq4uLZEUCVdqZbE1cddTHMIQ4GxNcmIgee2uOz4-sDwV0Q8-jVASoPwxEzBnieeV5tx9KdkTnWLCuBxrVr-j8abQfLbpzhYjxWCP; _jc_save_fromStation=%u6DF1%u5733%2CSZQ; _jc_save_toStation=%u5357%u660C%2CNCG; _jc_save_wfdc_flag=dc; BIGipServerpassport=786956554.50215.0000; current_captcha_type=Z; _jc_save_toDate=2021-01-11; route=9036359bb8a8a461c164a04f8f50b252; BIGipServerpool_passport=149160458.50215.0000; _jc_save_showIns=true; _jc_save_fromDate=2021-01-12; BIGipServerotn=3755409674.64545.0000; uKey=9a1e7a25b92d4f7146197a50d20d2628264a4341ad2efd167fc7a36984401218";
}
