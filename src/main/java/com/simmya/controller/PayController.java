package com.simmya.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simmya.constant.OrderStatus;
import com.simmya.pojo.Orders;
import com.simmya.pojo.OrdersCommit;
import com.simmya.pojo.User;
import com.simmya.service.impl.OrdersService;
import com.simmya.service.impl.PayService;
import com.simmya.service.impl.UserService;
import com.simmya.vo.OrderV;


@Controller
public class PayController {

	@Autowired
	private UserService userService;
	@Autowired
	private PayService payService;
	@Autowired
	private OrdersService ordersService;
	
	/*
	 * 提交订单
	 */
	@RequestMapping(value= "/boxs/order", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> backOrderAdd(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "orders", required = true)String jsons) throws SQLException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isBlank(token)) {
			map.put("code", "error");
			map.put("desc", null);
			return map;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			map.put("code", "error");
			map.put("desc", null);
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			OrdersCommit orders=mapper.readValue(jsons, OrdersCommit.class);
			Orders orderx=payService.saveOrderRef(orders,loginUser);
			Map<String, String> sParaTemp = new HashMap<String, String>(); 
			sParaTemp.put("_input_charset", "utf-8");
			sParaTemp.put("it_b_pay", "30m"); 
			sParaTemp.put("notify_url", "http://115.159.42.41:6430/boxs/backZFB.do"); 
			sParaTemp.put("out_trade_no", orderx.getId()); 
			sParaTemp.put("partner", "2088511405850083"); 
			sParaTemp.put("payment_type", "1"); 
			//sParaTemp.put("return_url", "http://115.159.42.41:6430/boxs/returnZFB.do"); 
			sParaTemp.put("seller_id", "nuanuan1210@qq.com"); 
			sParaTemp.put("service", "mobile.securitypay.pay"); 
			sParaTemp.put("subject", "盒子购买"); 
			sParaTemp.put("total_fee", String.valueOf(orderx.getTotalPrice()));
			
			
			String sb="_input_charset="+sParaTemp.get("_input_charset");
			sb+="&it_b_pay="+sParaTemp.get("it_b_pay");
			sb+="&notify_url="+sParaTemp.get("notify_url");
			sb+="&out_trade_no="+sParaTemp.get("out_trade_no");			
			sb+="&partner="+sParaTemp.get("partner");
			sb+="&payment_type="+sParaTemp.get("payment_type");
			//sb+="&return_url="+sParaTemp.get("return_url");			
			sb+="&seller_id="+sParaTemp.get("seller_id");
			sb+="&service="+sParaTemp.get("service");
			sb+="&subject="+sParaTemp.get("subject");
			sb+="&total_fee="+sParaTemp.get("total_fee");		
			sb+="&sign=MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
			sb+="&sign_type=RSA";
			map.put("code", "sucess");
			map.put("desc", sb.toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return map;
	}
	
//	@RequestMapping(value= "/boxs/returnZFB", method = RequestMethod.GET)
//	@ResponseBody
//	public Map<String, Object> returnZFB() throws SQLException {
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("code", "sucess");
//		return map;
//	}
	
	
	@RequestMapping(value= "/boxs/backZFB", method = RequestMethod.POST)
	@ResponseBody
	public String backZFB(@RequestParam(value = "out_trade_no", required = true)String out_trade_no) throws SQLException {
		String returnString="error";
		Orders order=ordersService.selectByPrimaryKey(out_trade_no);
		order.setStatus(OrderStatus.Payed);
		ordersService.update(order);
		returnString = "success";
		return returnString;
	}
	
	@RequestMapping(value= "/user/ordersNoPay", method = RequestMethod.GET)
	@ResponseBody
	public List<OrderV> listOrders(@RequestHeader(value = "token",required = true)String token) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return null;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return null;
		}
		return payService.getOrderListNoPay(loginUser);
	}
	
	
}