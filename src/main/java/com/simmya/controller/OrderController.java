package com.simmya.controller;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private OrdersService orderService;
	
	
	
	@RequestMapping(value= "/user/orders", method = RequestMethod.POST)
	@ResponseBody
	public List<OrderV> listOrdersByStatus(
			@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "status",required = false)String status,
			HttpServletRequest request) throws SQLException {
		try {
			if (StringUtils.isBlank(token)) {
				return Collections.emptyList();
			}
			User loginUser = userService.checkLogin(token);
			if (loginUser == null) {
				return Collections.emptyList();
			}
			StringBuffer requestURL = request.getRequestURL();
			String servletPath = request.getServletPath();
			String url = StringUtils.substringBefore(requestURL.toString(), servletPath) + "/";
			return orderService.listOrders(loginUser.getId(), url, status);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}
	
	@RequestMapping(value= "/orderBox/receive", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> receiveOrderBox(
			@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "orderid",required = true)String orderid,
			@RequestParam(value = "boxid",required = true)String boxid,
			@RequestParam(value = "sendCount",required = true)int sendCount) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isBlank(token)) {
			map.put("code", "error");
			return map;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			map.put("code", "error");
			return map;
		}
		return orderService.receiveOrderBox(loginUser.getId(), orderid, boxid, sendCount);
	}
	
	@RequestMapping(value= "/orderBox/discuss", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> discussOrderBox(
			@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "orderid",required = true)String orderid,
			@RequestParam(value = "boxid",required = true)String boxid,
			@RequestParam(value = "sendCount",required = true)int sendCount,
			@RequestParam(value = "discuss",required = true)String discuss) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isBlank(token)) {
			map.put("code", "error");
			return map;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			map.put("code", "error");
			return map;
		}
		return orderService.discussOrderBox(loginUser.getId(), orderid, boxid, sendCount, discuss);
	}
	
	
	
	@RequestMapping(value= "/user/orderBoxes", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> listOrders(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "orderId", required = true)String orderId,
			HttpServletRequest request) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return Collections.emptyList();
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return Collections.emptyList();
		}
		StringBuffer requestURL = request.getRequestURL();
		String servletPath = request.getServletPath();
		String url = StringUtils.substringBefore(requestURL.toString(), servletPath) + "/";
		return orderService.listOrderBoxes(loginUser.getId(), orderId, url);
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