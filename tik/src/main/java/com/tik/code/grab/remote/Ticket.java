package com.tik.code.grab.remote;

import com.icecream.annotation.GetMapping;
import com.icecream.annotation.PostMapping;
import com.icecream.annotation.Remote;
import com.tik.code.grab.mode.CheckOrderInfoMode;
import com.tik.code.grab.mode.CheckOrderInfoMode.CheckResult;
import com.tik.code.grab.mode.ConfirmSingleForQueueMode;
import com.tik.code.grab.mode.GetQueueCountMode;
import com.tik.code.grab.mode.InitDC;
import com.tik.code.grab.mode.GetQueueCountMode.QueueResult;
import com.tik.code.grab.mode.ManInfoMode;
import com.tik.code.grab.mode.SubmitOrderRequestMode;
import com.tik.code.grab.mode.TiketQueryMode;
import com.tik.code.grab.mode.TiketQueryResultMode;
import com.tik.common.contant.Data;
import com.tik.common.contant.SSLFactory;
import com.tik.common.mode.RestObject;

/**
 * @作者 ChengShi
 * @日期 2021年1月10日
 * @版本 1.0
 * @描述 票远程
 */
@Remote(host = "kyfw.12306.cn", port = "443", type = Data.HTTP_CODE, useSSL = SSLFactory.class)
public interface Ticket{
	
	@GetMapping(value = "/otn/leftTicket/queryT?")
	public RestObject<TiketQueryResultMode> query(TiketQueryMode tiketQueryMode);
	
	@GetMapping(value = "/otn/confirmPassenger/getPassengerDTOs")
	public RestObject<ManInfoMode> getManInfo();
	
	@PostMapping(value = "/otn/leftTicket/submitOrderRequest")
	public RestObject<String> submitOrderRequest(SubmitOrderRequestMode submitOrderRequestMode);
	
	@PostMapping(value = "/otn/confirmPassenger/initDc")
	public String initDC(InitDC initDC);
	
	@PostMapping(value = "/otn/confirmPassenger/checkOrderInfo")
	public RestObject<CheckResult> checkOrderInfo(CheckOrderInfoMode checkOrderInfoMode);
	
	@PostMapping(value = "/otn/confirmPassenger/getQueueCount")
	public RestObject<QueueResult> getQueueCount(GetQueueCountMode getQueueCountMode);
	
	@PostMapping(value = "/otn/confirmPassenger/confirmSingleForQueue")
	public RestObject<String> confirmSingleForQueue(ConfirmSingleForQueueMode confirmSingleForQueueMode);
}
