package com.simmya.controller;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.simmya.constant.ReturnMap;
import com.simmya.pojo.User;
import com.simmya.service.impl.CodecService;
import com.simmya.service.impl.UserService;
import com.simmya.util.FilePathUtil;
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
		String pswd = "Tch666777";// 密码
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
	public Map<String, Object> getInfo(@RequestHeader(value = "token",required = true)String token,
			HttpServletRequest request) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return Collections.emptyMap();
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return Collections.emptyMap();
		}
		StringBuffer requestURL = request.getRequestURL();
		String servletPath = request.getServletPath();
		String url = StringUtils.substringBefore(requestURL.toString(), servletPath) + "/";
		Map<String, Object> map = userService.getInfo(loginUser.getId(), url);
		return map;
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
	
	/*
	 * gender=男&birth=19800909&zodiac=虎&profession=计算机应用
	 */
	@RequestMapping(value= "/user/personalInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> completeInfo(@RequestHeader(value = "token",required = true)String token,
			  @RequestParam(value = "gender", required = true)String gender,
			  @RequestParam(value = "birth", required = true)String birth,
			  @RequestParam(value = "zodiac", required = true)String zodiac,
			  @RequestParam(value = "nickName", required = true)String nickName,
			  @RequestParam(value = "profession", required = true)String profession) throws Exception {
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
		loginUser.setBirth(new SimpleDateFormat("yyyyMMdd").parse(birth));
		loginUser.setGender(gender);
		loginUser.setProfession(profession);
		loginUser.setZodiac(zodiac);
		loginUser.setNickname(nickName);
		return userService.completeInfo(loginUser);
	}
	
	@RequestMapping(value= "/user/headUpload", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> headUpload(@RequestHeader(value = "token",required = true)String token,
			@RequestParam("in") MultipartFile multipartFile,
			HttpServletRequest request) throws Exception {
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
		String originalFilename = multipartFile.getOriginalFilename();
		String suffix = StringUtils.substringAfter(originalFilename, ".");
		if (!suffix.equalsIgnoreCase("PNG") && !suffix.equalsIgnoreCase("JPG") 
				&& !suffix.equalsIgnoreCase("BMP") && !suffix.equalsIgnoreCase("GIF")) {
			map.put("code", "error");
			return map;
		}
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String realPath = request.getSession().getServletContext().getRealPath("/pic/head");
		String dirPath = realPath + FilePathUtil.createPath();
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String name = dirPath + File.separator + uuid + "." + suffix;
		File file = new File(name);
		multipartFile.transferTo(file);
		loginUser.setHeadPic("pic" + File.separator + "head" + FilePathUtil.createPath() + File.separator + uuid + "." + suffix);
		userService.updateSelective(loginUser);
		map.put("code", "sucess");
		return map;
	}
}