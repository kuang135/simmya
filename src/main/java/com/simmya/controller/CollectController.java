package com.simmya.controller;

import java.sql.SQLException;
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
	public List<Map<String, Object>> listCollectedInfo(@RequestHeader(value = "token",required = true)String token,
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
		return infoCollectService.list(loginUser.getId(), url);
	}
	
	@RequestMapping(value= "/user/collectionBox", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> listCollectedBox(@RequestHeader(value = "token",required = true)String token,
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
		return boxCollectService.list(loginUser.getId(), url);
	}
	
	
	@RequestMapping(value= "/infos/delCollect", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteInfoCollect(
			@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "infoid",required = true)String infoid) {
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
		return infoCollectService.deleteCollect(loginUser.getId(), infoid);
	}
	
	@RequestMapping(value= "/box/delCollect", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteBoxCollect(
			@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "boxid",required = true)String boxid) {
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
		return boxCollectService.deleteCollect(loginUser.getId(), boxid);
	}
	
}
