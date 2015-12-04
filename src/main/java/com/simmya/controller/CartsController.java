package com.simmya.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.simmya.pojo.Carts;
import com.simmya.pojo.User;
import com.simmya.service.impl.CartsService;
import com.simmya.service.impl.UserService;


@Controller
public class CartsController {

	@Autowired
	private UserService userService;
	@Autowired
	private CartsService cartsService;
	
	
	@RequestMapping(value= "/carts/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addToCart(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "carts", required = true)String carts) throws Exception {
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
		ObjectMapper mapper = new ObjectMapper();
		CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Carts.class);
		List<Carts> list = mapper.readValue(carts, collectionType);
		return cartsService.addToCart(loginUser.getId(), list);
	}
	
	@RequestMapping(value= "/carts/list", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> listCart(@RequestHeader(value = "token",required = true)String token,
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
		return cartsService.listCarts(loginUser.getId(), url);
	}
	
	/*
	 * 从购物车中移除一个盒子
	 */
	@RequestMapping(value= "/carts/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteCart(@RequestHeader(value = "token",required = true)String token,
			 @RequestParam(value = "cartIds", required = true)String cartIds) {
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
		return cartsService.deleteCarts(loginUser.getId(), cartIds);
	}
	
}
