package com.simmya.controller;

import java.sql.SQLException;
import java.util.Collections;
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

import com.simmya.constant.ReturnMap;
import com.simmya.pojo.User;
import com.simmya.service.impl.CartsService;
import com.simmya.service.impl.UserService;


@Controller
public class CartsController {

	@Autowired
	private UserService userService;
	@Autowired
	private CartsService cartsService;
	
	
	@RequestMapping(value= "/boxs/order", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> addToCart(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "boxid", required = true)String boxid) {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		return cartsService.addToCart(loginUser.getId(), boxid);
	}
	
	@RequestMapping(value= "/boxs/cart", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> listCart(@RequestHeader(value = "token",required = true)String token) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return Collections.emptyList();
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return Collections.emptyList();
		}
		return cartsService.listCarts(loginUser.getId());
	}
	
	@RequestMapping(value= "/boxs/cart/boxDel", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteCart(@RequestHeader(value = "token",required = true)String token,
			 @RequestParam(value = "boxid", required = true)String boxid) {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		return cartsService.deleteCarts(loginUser.getId(), boxid);
	}
}
