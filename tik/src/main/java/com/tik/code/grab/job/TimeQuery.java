package com.tik.code.grab.job;

import java.util.List;

import com.icecream.annotation.Autowired;
import com.icecream.annotation.Compont;
import com.icecream.annotation.Init;
import com.tik.code.grab.mode.InitDC;
import com.tik.code.grab.mode.InitDC.DCData;
import com.tik.code.grab.mode.ManInfoMode;
import com.tik.code.grab.mode.ManInfoMode.ManMode;
import com.tik.code.grab.mode.TiketQueryMode;
import com.tik.code.grab.mode.TiketQueryResultMode;
import com.tik.code.grab.mode.TiketQueryResultMode.TiketMode;
import com.tik.code.grab.remote.Ticket;
import com.tik.code.grab.service.TicketService;
import com.tik.code.grab.uitl.ParseResultUtil;
import com.tik.common.contant.Data;
import com.tik.common.contant.SeatType;
import com.tik.common.mode.RestObject;
import com.util.ListUtil;
import com.util.MyUtil;

/**
 * @作者 ChengShi
 * @日期 2021年1月10日
 * @版本 1.0
 * @描述 
 */
@Compont
public class TimeQuery {
	@Autowired
	private Ticket ticket;
	@Autowired
	private TicketService ticketService;
	
//	@Scalude(id = Data.QUERY_ID, timeout = 0)
	@Init
	private void query() {
		try {
			Thread.sleep((long)(Math.random() * 2000 + 1000));
			TiketQueryMode tiketQueryMode = new TiketQueryMode();
			tiketQueryMode.setLeftTicketDTO$train_date("2021-01-12");
			tiketQueryMode.setLeftTicketDTO$from_station("SZQ");
			tiketQueryMode.setLeftTicketDTO$to_station("NCG");
			tiketQueryMode.setPurpose_codes("ADULT");
			String trainName = "T398";
			/*开始刷票*/
			RestObject<TiketQueryResultMode> query = ticket.query(tiketQueryMode);
			if (query.getHttpstatus() == Data.HTTP_STATE_OK) {
				List<TiketMode> parseResult = ParseResultUtil.parseResult(query.getData().getResult(), query.getData().getMap());
				List<TiketMode> queryListData = ListUtil.getQueryListData(parseResult, "train_name", ListUtil.EQUAL, trainName, true);
				for (TiketMode tiketMode : queryListData) {
					System.out.println(tiketMode.toString());
					if (isHaveHardBerth(tiketMode.getHard_berth())) {downOrder(tiketMode, SeatType.硬卧);}
				}
			}else {System.out.println(query.getMessages());}
		} catch (Exception e) {e.printStackTrace();}
	}
	
	/*是否有硬卧*/
	private boolean isHaveHardBerth(String heardBerth) {
		try {
			heardBerth = heardBerth.trim();
			if (heardBerth.equals("有")) {return true;}
			int parseInt = Integer.parseInt(heardBerth);
			return parseInt > 0;
		} catch (Exception e) {return false;}
	}
	
	/*下单*/
	private void downOrder(TiketMode tiketMode, SeatType seatType) {
		try {
			if(ticketService.submitOrderRequest(tiketMode)) {
				/*获取提交会话*/
				DCData dcData = ParseResultUtil.getTOKEN(ticket.initDC(new InitDC()));
				System.out.println("提交会话Token》》》" + dcData.getGlobalRepeatSubmitToken());
				System.out.println("提交会话Key》》》" + dcData.getKey_check_isChange());
				RestObject<ManInfoMode> manInfo = ticket.getManInfo();
				if (manInfo.getHttpstatus() == 200) {
					if (manInfo.getData().isExist()) {
						List<ManMode> normal_passengers = manInfo.getData().getNormal_passengers();
						if (MyUtil.isNotBlank(normal_passengers)) {
							List<ManMode> queryListData = ListUtil.getQueryListData(normal_passengers, "passenger_name", ListUtil.EQUAL, "程仕", true);
							if (MyUtil.isNotBlank(queryListData)) {
								System.out.println("购票人员》》》" + queryListData.get(0).getPassenger_name());
								/*检票*/
								if(ticketService.checkOrderInfo(queryListData.get(0), seatType, dcData)) {
									/*提交订单*/
									if(ticketService.getQueueCount(tiketMode, seatType, dcData)) {
//									if (ticketService.confirmSingleForQueue(queryListData.get(0), tiketMode, seatType, dcData)) {
//										//发送至邮箱表示确认成功
//									}else {System.err.println("确认失败！");}
									}else {System.err.println("提交订单失败！");}
								}else {System.err.println("检票失败！");}
							}
						}
					}else {System.err.println(manInfo.getData().getExMsg());}
				}
			}else {System.out.println("预定失败！");}
		} catch (Exception e) {System.err.println(e.getMessage());}
	}
}
