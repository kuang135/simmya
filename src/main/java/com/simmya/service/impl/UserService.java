package com.simmya.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.simmya.mapper.CodecMapper;
import com.simmya.pojo.User;
import com.simmya.service.BaseService;
import com.simmya.util.DbUtil;

@Service
public class UserService extends BaseService<User>{
	
	@Autowired
	public CodecMapper codecMapper;

	public User checkLogin(String token) {
		User where = new User();
		where.setToken(token);
		return super.selectOneByWhere(where);
	}

	@Transactional
	public String doLogin(String name, String password) {
		User where = new User();
		where.setUsername(name);
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
		where.setPassword(md5DigestAsHex);
		User user = super.selectOneByWhere(where);
		if (user == null)
			return null;
		String token = UUID.randomUUID().toString().replace("-", "");
		user.setToken(token);
		super.updateSelective(user);
		return token;
	}
	
	/*
	 * 前端处理
	 */
	public Map<String, Object> getBalance(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = super.selectByPrimaryKey(id);
		if (user.getBalance() == null ) {
			map.put("balance", 0.00);
		} else {
			map.put("balance", user.getBalance());
		}
		/*if (user.getBalance() == null) {
			map.put("balance", "0.00");
		} else {
			DecimalFormat df = new DecimalFormat("#,###.00");
			String value = df.format(user.getBalance().setScale(2, BigDecimal.ROUND_HALF_UP));
			if (value.startsWith(".")) {
				value = "0" + value;
			}
			map.put("balance", value);
		}*/
		return map;
	}

	/*
	 * {'nickName':'xxx','gender':'男','birth':'19800909',
	 * 'zodiac':'虎'，‘profession’:‘计算机应用’,
	 * 'headPic':'www.simmay.com/head.pic'}
	 */
	public Map<String, Object> getInfo(String id) throws SQLException {
		String sql = "SELECT a.NICKNAME nickName,a.GENDER gender,DATE_FORMAT(a.BIRTH,'%Y%m%d') birth, "
				+ " a.ZODIAC zodiac,a.PROFESSION profession,a.HEAD_PIC headPic "
				+ " FROM USER a "
				+ " WHERE a.ID = ?";
		return DbUtil.getMap(sql, id);
	}

	public Map<String, Object> updatePassword(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			int i = super.updateSelective(user);
			if (i == 1) {
				map.put("code", "success");
			} else {
				map.put("code", "error");
			}
		} catch (Exception e) {
			map.put("code", "error");
		}
		return map;
	}
	
}
