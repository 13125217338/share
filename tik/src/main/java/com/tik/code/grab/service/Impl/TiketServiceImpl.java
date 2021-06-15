package com.tik.code.grab.service.Impl;

import com.icecream.annotation.Autowired;
import com.icecream.annotation.Service;
import com.tik.code.grab.mode.CheckOrderInfoMode;
import com.tik.code.grab.mode.CheckOrderInfoMode.CheckResult;
import com.tik.code.grab.mode.ConfirmSingleForQueueMode;
import com.tik.code.grab.mode.GetQueueCountMode;
import com.tik.code.grab.mode.GetQueueCountMode.QueueResult;
import com.tik.code.grab.mode.InitDC.DCData;
import com.tik.code.grab.mode.ManInfoMode.ManMode;
import com.tik.code.grab.mode.SubmitOrderRequestMode;
import com.tik.code.grab.mode.TiketQueryResultMode.TiketMode;
import com.tik.code.grab.remote.Ticket;
import com.tik.code.grab.service.TicketService;
import com.tik.code.grab.uitl.ParseResultUtil;
import com.tik.common.contant.Data;
import com.tik.common.contant.SeatType;
import com.tik.common.mode.RestObject;

/**
 * @作者 ChengShi
 * @日期 2021年1月11日
 * @版本 1.0
 * @描述 票服务实现类
 */
@Service
public class TiketServiceImpl implements TicketService{
	@Autowired
	private Ticket ticket;
	
	@Override
	public boolean submitOrderRequest(TiketMode tiketMode) throws Exception {
		SubmitOrderRequestMode submitOrderRequestMode = new SubmitOrderRequestMode();
		submitOrderRequestMode.setSecretStr(tiketMode.getSecretStr());
		submitOrderRequestMode.setTrain_date(ParseResultUtil.getTime(tiketMode.getTrain_date()));
		submitOrderRequestMode.setBack_train_date(submitOrderRequestMode.getTrain_date());
		submitOrderRequestMode.setQuery_from_station_name(tiketMode.getStart_name());
		submitOrderRequestMode.setQuery_to_station_name(tiketMode.getEnd_name());
		RestObject<String> submitOrderRequest = ticket.submitOrderRequest(submitOrderRequestMode);
		System.out.println("预定票》》》" + submitOrderRequest.getMessages());
		return submitOrderRequest.isStatus();
	}

	@Override
	public boolean checkOrderInfo(ManMode manMode, SeatType seatType, DCData dcData) throws Exception {
		CheckOrderInfoMode checkOrderInfoMode = new CheckOrderInfoMode();
		checkOrderInfoMode.setPassengerTicketStr(
				seatType.getCode() + "," + manMode.getIndex_id() + "," + manMode.getPassenger_type() + "," +
				manMode.getPassenger_name() + "," + manMode.getPassenger_id_type_code() + "," +
				manMode.getPassenger_id_no() + "," + manMode.getMobile_no() + "," + "N" + "," +
				manMode.getAllEncStr());
		checkOrderInfoMode.setOldPassengerStr(
				manMode.getPassenger_name() + "," + manMode.getPassenger_id_type_code() + "," +
				manMode.getPassenger_id_no() + "," + manMode.getPassenger_id_type_code() + "_");
		checkOrderInfoMode.setREPEAT_SUBMIT_TOKEN(dcData.getGlobalRepeatSubmitToken());
		RestObject<CheckResult> checkOrderInfo = ticket.checkOrderInfo(checkOrderInfoMode);
		System.out.println("检票流程》》》");
		return (checkOrderInfo.getHttpstatus() == Data.HTTP_STATE_OK && checkOrderInfo.getData().isSubmitStatus());
	}

	@Override
	public boolean getQueueCount(TiketMode tiketMode,SeatType seatType, DCData dcData) throws Exception {
		GetQueueCountMode getQueueCountMode = new GetQueueCountMode();
		getQueueCountMode.setTrain_date(ParseResultUtil.getGMTTime(tiketMode.getTrain_date()));
		getQueueCountMode.setTrain_no(tiketMode.getTrain_no());
		getQueueCountMode.setStationTrainCode(tiketMode.getTrain_name());
		getQueueCountMode.setSeatType(seatType.getCode());
		getQueueCountMode.setFromStationTelecode(tiketMode.getFrom_station_no());
		getQueueCountMode.setToStationTelecode(tiketMode.getTo_station_no());
		getQueueCountMode.setLeftTicket(tiketMode.getLeft_ticket());
		getQueueCountMode.setTrain_location(tiketMode.getTrain_location());
		getQueueCountMode.setREPEAT_SUBMIT_TOKEN(dcData.getGlobalRepeatSubmitToken());
		RestObject<QueueResult> queueCount = ticket.getQueueCount(getQueueCountMode);
		System.out.println("提交订单》》》 剩余票：" + queueCount.getData().getTicket());
		return queueCount.getHttpstatus() == Data.HTTP_STATE_OK;
	}

	@Override
	public boolean confirmSingleForQueue(ManMode manMode, TiketMode tiketMode, SeatType seatType, DCData dcData) throws Exception {
		ConfirmSingleForQueueMode confirmSingleForQueueMode = new ConfirmSingleForQueueMode();
		confirmSingleForQueueMode.setPassengerTicketStr(
				seatType.getCode() + "," + manMode.getIndex_id() + "," + manMode.getPassenger_type() + "," +
				manMode.getPassenger_name() + "," + manMode.getPassenger_id_type_code() + "," +
				manMode.getPassenger_id_no() + "," + manMode.getMobile_no() + "," + "N" + "," +
				manMode.getAllEncStr());
		confirmSingleForQueueMode.setOldPassengerStr(
				manMode.getPassenger_name() + "," + manMode.getPassenger_id_type_code() + "," +
				manMode.getPassenger_id_no() + "," + manMode.getPassenger_id_type_code() + "_");
		confirmSingleForQueueMode.setKey_check_isChange(dcData.getKey_check_isChange());
		confirmSingleForQueueMode.setLeftTicketStr(tiketMode.getLeft_ticket());
		confirmSingleForQueueMode.setTrain_location(tiketMode.getTrain_location());
		confirmSingleForQueueMode.setREPEAT_SUBMIT_TOKEN(dcData.getGlobalRepeatSubmitToken());
		confirmSingleForQueueMode.setEncryptedData("");
		RestObject<String> confirmSingleForQueue = ticket.confirmSingleForQueue(confirmSingleForQueueMode);
		System.out.println("确认订单》》》" + confirmSingleForQueue.getData());
		return confirmSingleForQueue.getHttpstatus() == Data.HTTP_STATE_OK;
	}

}
