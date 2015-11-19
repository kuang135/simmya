package com.simmya.service.impl;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.simmya.pojo.Codec;
import com.simmya.service.BaseService;
import com.simmya.util.DbUtil;

@Service
public class CodecService extends BaseService<Codec> {

	public void saveCodec(String phone, String veriCode) {
		Codec codecx = new Codec();
		codecx.setPhone(phone);
		codecx.setVeriCode(veriCode);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // 设置当前日期
		c.add(Calendar.MINUTE, 1); // 日期分钟加1,Calendar.DATE(天),Calendar.HOUR(小时)
		Date date = c.getTime(); // 结果
		codecx.setExpiredTime(date);
		super.save(codecx);
	}
	
	
	public Map<String, Object> vailCode(String name) throws SQLException {
		String sql = " select VERI_CODE veriCode, EXPIRED_TIME expiredTime, PHONE "
				+ " from codec where PHONE = ? "
				+ " order by EXPIRED_TIME desc";
		List<Map<String, Object>> list = DbUtil.getMapList(sql, name);
		if (list != null) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
}
