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

import com.simmya.constant.ReturnMap;
import com.simmya.mapper.InfoAgreeMapper;
import com.simmya.pojo.InfoAgree;
import com.simmya.pojo.InfoCollection;
import com.simmya.pojo.User;
import com.simmya.service.impl.InfoCollectionService;
import com.simmya.service.impl.InfoService;
import com.simmya.service.impl.UserService;

@Controller
public class InfoController {	
	
	@Autowired
	private InfoCollectionService infoCollectionService;
	@Autowired
	private InfoService infoService;
	@Autowired
	private UserService userService;
	@Autowired
	private InfoAgreeMapper infoAgreeMapper;
	
	@RequestMapping(value= "/infos/list", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getInfoList(
			@RequestHeader(value = "token",required = false)String token,
			@RequestParam(value = "page", required = true)int page,
			@RequestParam(value = "size", required = true)int size,
			HttpServletRequest request) throws SQLException {
		StringBuffer requestURL = request.getRequestURL();
		String servletPath = request.getServletPath();
		String url = StringUtils.substringBefore(requestURL.toString(), servletPath) + "/";
		int start = (page-1)*size;
		if (StringUtils.isBlank(token)) {
			return infoService.getInfoList(start, size, url);
		} else {
			User loginUser = userService.checkLogin(token);
			if (loginUser == null) {
				List<Map<String, Object>> listss=new ArrayList<Map<String, Object>>();
				listss.add(ReturnMap.FAULT);
				return listss;
				//return Collections.emptyList();
			}
			return infoService.getInfoListByToken(loginUser.getId(), start, size, url);
		}
	}
	
	@RequestMapping(value= "/infos/id", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getDetailById(
			@RequestParam(value = "infoid", required = true)String infoid,
			HttpServletRequest request) throws SQLException {
		StringBuffer requestURL = request.getRequestURL();
		String servletPath = request.getServletPath();
		String url = StringUtils.substringBefore(requestURL.toString(), servletPath) + "/";
		return infoService.getDetailById(infoid, url);
	}
	
	@RequestMapping(value= "/infos/idx", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getDetailByIdxx(
			@RequestParam(value = "infoid", required = true)String infoid,
			@RequestHeader(value = "token",required = true)String token,
			HttpServletRequest request) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		StringBuffer requestURL = request.getRequestURL();
		String servletPath = request.getServletPath();
		String url = StringUtils.substringBefore(requestURL.toString(), servletPath) + "/";

		Map<String, Object> maps= infoService.getDetailById(infoid, url);
		String idx=(String)maps.get("id");
		InfoCollection infoc=new InfoCollection();
		infoc.setInfoId(idx);
		infoc.setUserId(loginUser.getId());
		InfoCollection infocc=infoCollectionService.selectOneByWhere(infoc);
		if(infocc==null){
			maps.put("collected", "false");
		}else{
			maps.put("collected", "true");
		}
		InfoAgree infoAgree = new InfoAgree();
		infoAgree.setInfoId((String)maps.get("id"));
		infoAgree.setUserId(loginUser.getId());
		InfoAgree selectOne2 = infoAgreeMapper.selectOne(infoAgree);
		if (selectOne2 == null) {
			maps.put("agreed", "false");
		} else {
			maps.put("agreed", "true");
		}
		return maps;
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
	
	@RequestMapping(value= "/infos/delAgree", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteInfoAgree(
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
		return infoService.deleteAgree(loginUser.getId(), infoid);
	}
	
}
