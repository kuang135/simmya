package com.simmya.controller;

import java.sql.SQLException;
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
import com.simmya.service.impl.InfoService;
import com.simmya.service.impl.UserService;

@Controller
public class InfoController {	
	
	
	@Autowired
	private InfoService infoService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value= "/infos/list", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getTop10InfoOfClickcount(@RequestParam(value = "limit", required = true)String limit) throws SQLException {
		return infoService.getTop10(limit);
	}
	
	@RequestMapping(value= "/infos/id", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getDetailById(@RequestParam(value = "infoid", required = true)String infoid) throws SQLException {
		return infoService.getDetailById(infoid);
	}
	
	@RequestMapping(value= "/infos/agree", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doAgree(@RequestHeader(value = "token",required = true)String token,
				@RequestParam(value = "infoid", required = true)String infoid) {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		return infoService.doAgree(loginUser.getId(), infoid);
	}
	
	@RequestMapping(value= "/infos/discuss", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doDiscuss(@RequestHeader(value = "token",required = true)String token,
										 @RequestParam(value = "infoid", required = true)String infoid,
										 @RequestParam(value = "content", required = true)String content
										 ) {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		return infoService.doDiscuss(loginUser.getId(), infoid, content);
	}
	
	@RequestMapping(value= "/infos/collect", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> collectInfo(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "infoid", required = true)String infoid) {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		return infoService.collectInfo(loginUser.getId(), infoid);
	}

	@RequestMapping(value= "/infos/share", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> shareInfo(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "infoid", required = true)String infoid) {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		return infoService.shareInfo(loginUser.getId(), infoid);
	}
}
