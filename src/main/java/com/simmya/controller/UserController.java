package com.simmya.controller;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simmya.constant.ReturnMap;
import com.simmya.pojo.User;
import com.simmya.service.impl.CodecService;
import com.simmya.service.impl.UserService;
import com.simmya.util.HttpSender;
import com.simmya.util.MathUtil;


@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private CodecService codecService;
	
	@RequestMapping(value= "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doLogin(@RequestParam(value = "name", required = true)String name, @RequestParam(value = "password", required = true)String password) {
		String token = userService.doLogin(name, password);
		Map<String, Object> map = new HashMap<String, Object>();
		if (token == null) {
			//{code:error,desc:"用户名或密码错误",token:null} 
			map.put("code", "error");
			map.put("desc", "用户名或密码错误");
		} else {
			//{code:sucess,desc:"成功",token:"1341234123423"}
			map.put("code", "sucess");
			map.put("desc", "成功");
		}
		map.put("token", token);
		return map;
	}
	
	@RequestMapping(value= "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doRegister(@RequestParam(value = "name", required = true)String name,
										  @RequestParam(value = "code", required = true)String code,
										  @RequestParam(value = "password", required = true)String password,
										  @RequestParam(value = "nickname", required = true)String nickname) throws SQLException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		//验证短信码是否正确:
		//1.通过手机号来验证短信码
		Map<String, Object> codec = codecService.vailCode(name);
		map.put("code", "error");
		if(!code.equals(codec.get("veriCode"))){
			map.put("desc", "验证码出错");
			return map;
		}
		if(((Date)codec.get("expiredTime")).compareTo(new Date())<0){
			map.put("desc", "验证码过期");
			return map;
		}
		
		User user=new User();
		user.setUsername(name);
		if(!userService.selectListByWhere(user).isEmpty()){
			map.put("desc", "此手机号已被注册");
			return map;
		}
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
		user.setPassword(md5DigestAsHex);
		user.setNickname(nickname);
		userService.save(user);
		map.put("code", "sucess");
		map.put("desc", "成功");
		return map;
	}
	
	@RequestMapping(value= "/user/code", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doCode(@RequestParam(value = "name", required = true)String name) {
		
		String url = "http://222.73.117.158/msg/";// 应用地址
		String account = "jiekou-clcs-10";// 账号
		String pswd = "Tch123123";// 密码
		String mobile = name;// 手机号码，多个号码使用","分割
		int randsu=MathUtil.nextInt(100000, 999999);
		String msg = "您好，您的验证码是"+String.valueOf(randsu);// 短信内容
		boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
		String product = null;// 产品ID
		String extno = null;// 扩展码

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String returnString = HttpSender.batchSend(url, account, pswd, mobile, msg, needstatus, product, extno);
			System.out.println(returnString);
			codecService.saveCodec(mobile, String.valueOf(randsu));
			map.put("code", "sucess");
			map.put("desc", "成功");
		} catch (Exception e) {
			map.put("code", "error");
			map.put("desc", e.getMessage());
		}
		return map;
	}
	
	@RequestMapping(value= "/user/balance", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getBalance(@RequestHeader(value = "token",required = true)String token) {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = userService.getBalance(loginUser.getId());
		} catch (Exception e) {
			map.put("balance", null);
		}
		return map;
	}
	
	/*
	 * {'nickName':'xxx','gender':'男','birth':'19800909',
	 * 'zodiac':'虎'，‘profession’:‘计算机应用’,
	 * 'headPic':'www.simmay.com/head.pic'}
	 */
	@RequestMapping(value= "/user/personalInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getInfo(@RequestHeader(value = "token",required = true)String token) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return Collections.emptyMap();
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return Collections.emptyMap();
		}
		return userService.getInfo(loginUser.getId());
	}

	@RequestMapping(value= "/user/password", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updatePassword(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "password", required = true)String password) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return ReturnMap.BLANK;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return ReturnMap.FAULT;
		}
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
		loginUser.setPassword(md5DigestAsHex);
		return userService.updatePassword(loginUser);
	}
}