package com.simmya.controller;

import java.io.UnsupportedEncodingException;
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
import com.simmya.alipay.config.AlipayConfig;
import com.simmya.alipay.sign.RSA;
import com.simmya.alipay.util.AlipayCore;
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
		map.put("code", "error");
		map.put("desc", null);
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

			String aliStr=AlipayCore.createLinkString(AlipayCore.paraFilter(sParaTemp));
			String rsaStr=RSA.sign(aliStr, AlipayConfig.private_key, AlipayConfig.input_charset);
			String sb=aliStr+"&sign="+rsaStr+"&sign_type=RSA";
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
	public String backZFB(@RequestParam(value = "out_trade_no", required = true)String out_trade_no) throws SQLException, UnsupportedEncodingException {
		String returnString="error";
		String out_trade_no_ = new String(out_trade_no.getBytes("ISO-8859-1"),"UTF-8");
		Orders order=ordersService.selectByPrimaryKey(out_trade_no_);
		if(order!=null){
			order.setStatus(OrderStatus.Payed);
			ordersService.update(order);
			returnString = "success";
		}
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