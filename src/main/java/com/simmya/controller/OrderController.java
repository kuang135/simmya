package com.simmya.controller;

import java.sql.SQLException;
import java.util.Collections;
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

import com.simmya.pojo.User;
import com.simmya.service.impl.OrdersService;
import com.simmya.service.impl.UserService;
import com.simmya.vo.OrderV;


@Controller
public class OrderController {

	@Autowired
	private UserService userService;
	@Autowired
	private OrdersService orderService;
	
	
	
	@RequestMapping(value= "/user/orders", method = RequestMethod.GET)
	@ResponseBody
	public List<OrderV> listOrders(@RequestHeader(value = "token",required = true)String token) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return null;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return null;
		}
		return orderService.listOrders(loginUser.getId());
	}
	
	@RequestMapping(value= "/user/orderBoxes", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> listOrders(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "orderId", required = true)String orderId) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return Collections.emptyList();
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return Collections.emptyList();
		}
		return orderService.listOrderBoxes(loginUser.getId(), orderId);
	}

	/*
	 * 退订整个订单
	 */
	@RequestMapping(value= "/user/orderbackAdd", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> backOrderAdd(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "orderId", required = true)String orderId) throws SQLException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isBlank(token)) {
			map.put("code", "error");
			return map;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			map.put("code", "error");
			return map;
		}
		return orderService.backOrderAdd(loginUser.getId(), orderId);
	}
	
	/*
	 * 从我的订单发起对盒子退订
	 */
	@RequestMapping(value= "/user/orderbackDel", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> orderbackDel(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "orderId", required = true)String orderId,
			@RequestParam(value = "boxId", required = true)String boxId) throws SQLException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isBlank(token)) {
			map.put("code", "error");
			return map;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			map.put("code", "error");
			return map;
		}
		return orderService.orderBoxBack(loginUser.getId(), orderId, boxId);
	}
}