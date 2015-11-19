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
import org.springframework.web.bind.annotation.ResponseBody;

import com.simmya.pojo.User;
import com.simmya.service.impl.BoxCollectionService;
import com.simmya.service.impl.InfoCollectionService;
import com.simmya.service.impl.UserService;


@Controller
public class CollectController {

	
	@Autowired
	private UserService userService;
	@Autowired
	private InfoCollectionService infoCollectService;
	@Autowired
	private BoxCollectionService boxCollectService;
	
	@RequestMapping(value= "/user/collectionInfo", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> listCollectedInfo(@RequestHeader(value = "token",required = true)String token) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return Collections.emptyList();
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return Collections.emptyList();
		}
		return infoCollectService.list(loginUser.getId());
	}
	
	@RequestMapping(value= "/user/collectionBox", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> listCollectedBox(@RequestHeader(value = "token",required = true)String token) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return Collections.emptyList();
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return Collections.emptyList();
		}
		return boxCollectService.list(loginUser.getId());
	}
	
	
}
