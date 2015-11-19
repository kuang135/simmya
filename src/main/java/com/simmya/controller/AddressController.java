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
import com.simmya.pojo.Address;
import com.simmya.pojo.User;
import com.simmya.service.impl.AddressService;
import com.simmya.service.impl.UserService;


@Controller
public class AddressController {

	@Autowired
	private UserService userService;
	@Autowired
	private AddressService addressService;
	
	
	
	@RequestMapping(value= "/user/address", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> listAddress(@RequestHeader(value = "token",required = true)String token) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return Collections.emptyList();
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return Collections.emptyList();
		}
		return addressService.listAddresss(loginUser.getId());
	}
	
	
	/*
	 * address=浙江嘉兴3&
	 * zipCode=314202&
	 * getName=小张&
	 * phone=12313423423
	 */
	@RequestMapping(value= "/user/addressAdd", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addAddress(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "address", required = true)String address,
			@RequestParam(value = "zipCode", required = true)String zipCode,
			@RequestParam(value = "getName", required = true)String getName,
			@RequestParam(value = "phone", required = true)String phone) {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		Address addr = new Address();
		addr.setAddressInfo(address);
		addr.setZipcode(zipCode);
		addr.setGetName(getName);
		addr.setPhone(phone);
		addr.setUserId(loginUser.getId());
		return addressService.addAddresss(addr);
	}
	
	@RequestMapping(value= "/user/addressDel", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delAddress(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "id", required = true)String id) {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		return addressService.delAddresss(id);
	}
	
	/*
	 * id = 
	 * address=浙江嘉兴3&
	 * zipCode=314202&
	 * getName=小张&
	 * phone=12313423423
	 */
	@RequestMapping(value= "/user/addressUpd", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateAddress(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "id", required = true)String id,
			@RequestParam(value = "address", required = true)String address,
			@RequestParam(value = "zipCode", required = true)String zipCode,
			@RequestParam(value = "getName", required = true)String getName,
			@RequestParam(value = "phone", required = true)String phone) {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		Address addr = new Address();
		addr.setId(id);
		addr.setAddressInfo(address);
		addr.setZipcode(zipCode);
		addr.setGetName(getName);
		addr.setPhone(phone);
		addr.setUserId(loginUser.getId());
		return addressService.updateAddresss(addr);
	}
}
