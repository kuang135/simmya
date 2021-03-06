package com.simmya.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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
import com.simmya.mapper.BoxCollectionMapper;
import com.simmya.pojo.BoxCollection;
import com.simmya.pojo.User;
import com.simmya.service.impl.BoxService;
import com.simmya.service.impl.UserService;


@Controller
public class BoxController {

	@Autowired
	private BoxService boxService;
	@Autowired
	private UserService userService;
	@Autowired
	private BoxCollectionMapper boxCollectionMapper;
	/*
	 * start=1&limit=10
	 * {'id':'2354234srte',NAME':'烧麦盒子','TITLE':'烧麦好吃',
	 *  'detail':'手工烧麦',imageAddress':'接口前缀+/image1.pig',
	 *  'shareCount':4,'boxPrice':100,'collectCount':'4','discussCount':10}
	 */
	@RequestMapping(value= "/boxs/list", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> listBox(
				@RequestHeader(value = "token",required = false)String token,
				@RequestParam(value = "start", required = true)String start,
				@RequestParam(value = "limit", required = true)String limit,
				HttpServletRequest request) throws SQLException {
		int begin = Integer.parseInt(start);
		int size = Integer.parseInt(limit);
		if (begin < 1 || size < 0)
			return Collections.emptyList();
		int st = begin - 1;
		StringBuffer requestURL = request.getRequestURL();
		String servletPath = request.getServletPath();
		String url = StringUtils.substringBefore(requestURL.toString(), servletPath) + "/";
		if (StringUtils.isBlank(token)) {
			return boxService.listBox(st, size, url);
		} else {
			User loginUser = userService.checkLogin(token);
			if (loginUser == null) {
				return Collections.emptyList();
			}
			return boxService.getInfoListByToken(loginUser.getId(), st, size, url);
		}
	}
	
	/*
	 * {'id':'2354234srte',NAME':'烧麦盒子','TITLE':'烧麦好吃',
	 *  'detail':'手工烧麦',imageAddress':'接口前缀+/image1.pig',
	 *   'shareCount':4,'boxPrice':100,'collectCount':'4','discussCount':10}
	 */
	@RequestMapping(value= "/boxs/id", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> detail(@RequestParam(value = "boxid", required = true)String boxid,
			HttpServletRequest request) throws SQLException {
		StringBuffer requestURL = request.getRequestURL();
		String servletPath = request.getServletPath();
		String url = StringUtils.substringBefore(requestURL.toString(), servletPath) + "/";
		return boxService.detail(boxid, url);
	}
	
	@RequestMapping(value= "/boxs/idx", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> detailxx(@RequestParam(value = "boxid", required = true)String boxid,@RequestHeader(value = "token",required = true)String token,
			HttpServletRequest request) throws SQLException {
		List<Map<String, Object>> mapList=new ArrayList<Map<String, Object>>();
		if (StringUtils.isBlank(token)) {
			mapList.add(ReturnMap.BLANK);
			return mapList;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			mapList.add(ReturnMap.FAULT);
			return mapList;
		}
		
		
		StringBuffer requestURL = request.getRequestURL();
		String servletPath = request.getServletPath();
		String url = StringUtils.substringBefore(requestURL.toString(), servletPath) + "/";
		mapList=boxService.detail(boxid, url);
		if (mapList != null && mapList.size() > 0) {
			for (Map<String, Object> map : mapList) {
				BoxCollection bc = new BoxCollection();
				bc.setBoxId((String)map.get("id"));
				bc.setUserId(loginUser.getId());
				BoxCollection selectOne = boxCollectionMapper.selectOne(bc);
				if (selectOne == null) {
					map.put("collected", false);
				} else {
					map.put("collected", true);
				}
			}
		}
		return mapList;
	}
	
	
	
	@RequestMapping(value= "/boxs/collect", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> collect(@RequestHeader(value = "token",required = true)String token,
									   @RequestParam(value = "boxid", required = true)String boxid) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		return boxService.collect(loginUser.getId(), boxid);
	}
	
	@RequestMapping(value= "/boxs/share", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> share(@RequestHeader(value = "token",required = true)String token,
									 @RequestParam(value = "boxid", required = true)String boxid) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		return boxService.share(loginUser.getId(), boxid);
	}
	
	/*
	 * /user/box/discussAdd
	 */
	
	/*@RequestMapping(value= "/user/box/discussAdd", method = RequestMethod.POST)
	@ResponseBody*/
	public Map<String, Object> doDiscuss(@RequestHeader(value = "token",required = true)String token,
										 @RequestParam(value = "boxId", required = true)String boxId,
										 @RequestParam(value = "content", required = true)String content
										 ) {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		return boxService.doDiscuss(loginUser.getId(), boxId, content);
	}
	
}
