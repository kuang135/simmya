package com.simmya.service.impl;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.simmya.pojo.Address;
import com.simmya.service.BaseService;
import com.simmya.util.DbUtil;

@Service
public class AddressService extends BaseService<Address>{
	
	
	public List<Map<String, Object>> listDiscussByInfoid(String infoid) throws SQLException {
		String sql = "select u.nickname nickName,u.head_pic headPic,d.content content,DATE_FORMAT(d.create_time,'%Y-%m-%d %H:%i:%s') time "
				+ " FROM discuss d "
				+ " LEFT JOIN USER u ON d.user_id = u.id "
				+ " WHERE d.info_id = ?";
		return DbUtil.getMapList(sql, infoid);
	}

	/*
	 * [{'id':'1234asdfgts','getName':姓名'address':'浙江嘉兴','zipCode':'314004','phone':'18818881888'}]
	 */
	public List<Map<String, Object>> listAddresss(String userid) throws SQLException {
		String sql = "SELECT ID id,ADDRESS_INFO address,GET_NAME getName,ZIPCODE zipCode,PHONE phone "
				+ " FROM address "
				+ " WHERE user_id = ?";
		return DbUtil.getMapList(sql, userid);
	}

	public Map<String, Object> addAddresss(Address addr) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			super.saveSelective(addr);
			map.put("code", "success");
		} catch (Exception e) {
			map.put("code", "error");
		}
		return map;
	}

	public Map<String, Object> delAddresss(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		int i = super.deleteById(id);
		if (i == 1) {
			map.put("code", "success");
		} else {
			map.put("code", "error");
		}
		return map;
	}

	public Map<String, Object> updateAddresss(Address addr) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			int i = super.updateSelective(addr);
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
