package com.tik.code.grab.service;

import com.tik.code.grab.mode.InitDC.DCData;
import com.tik.code.grab.mode.ManInfoMode.ManMode;
import com.tik.code.grab.mode.TiketQueryResultMode.TiketMode;
import com.tik.common.contant.SeatType;

/**
 * @作者 ChengShi
 * @日期 2021年1月11日
 * @版本 1.0
 * @描述 票服务
 */
public interface TicketService {
	/**
	 * @描述 预定
	 * @param tiketMode 票实体类
	 * @return 预定结果
	 */
	public boolean submitOrderRequest(TiketMode tiketMode) throws Exception;
	/**
	 * @描述 检票
	 * @param manMode 人物信息
	 * @param seatType 座位类型
	 * @param dcData 提交会话
	 * @return 是否检票成功
	 */
	public boolean checkOrderInfo(ManMode manMode, SeatType seatType, DCData dcData) throws Exception;
	/**
	 * @描述 提交订单
	 * @param tiketMode 票实体类
	 * @param seatType 座位类型
	 * @param dcData 提交会话
	 * @return 是否提交成功
	 */
	public boolean getQueueCount(TiketMode tiketMode,SeatType seatType, DCData dcData) throws Exception;
	/**
	 * @描述 确认订单
	 * @param manMode 人物信息
	 * @param tiketMode 票实体类
	 * @param seatType 座位类型
	 * @param dcData 提交会话
	 * @return 是否确认成功
	 */
	public boolean confirmSingleForQueue(ManMode manMode, TiketMode tiketMode,SeatType seatType, DCData dcData) throws Exception;
}
