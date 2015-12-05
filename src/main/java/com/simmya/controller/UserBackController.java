package com.simmya.controller;

import java.util.HashMap;
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
import com.simmya.pojo.UserBack;
import com.simmya.service.impl.UserBackService;
import com.simmya.service.impl.UserService;


@Controller
public class UserBackController {

	@Autowired
	private UserBackService userBackService;
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value= "/user/back", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> back(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "content",required = true)String content) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isBlank(token) || StringUtils.isBlank(content)) {
			map.put("code", "error");
			return map;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			map.put("code", "error");
			return map;
		}
		UserBack userBack = new UserBack();
		userBack.setUserId(loginUser.getId());
		userBack.setContent(content);
		try {
			userBackService.save(userBack);
			map.put("code", "success");
		} catch (Exception e) {
			map.put("code", "error");
		}
		return map;
	}
	
}