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
			String orderid=payService.saveOrderRef(orders,loginUser);
			map.put("code", "sucess");
			map.put("desc", orderid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return map;
	}
	
	@RequestMapping(value= "/boxs/backZFB", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> backZFB(@RequestParam(value = "out_trade_no", required = true)String out_trade_no) throws SQLException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("code", "error");
		Orders order=ordersService.selectByPrimaryKey(out_trade_no);
		order.setStatus(OrderStatus.Payed);
		ordersService.update(order);
		map.put("code", "sucess");
		return map;
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