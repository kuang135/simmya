package com.simmya.service.impl;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.simmya.pojo.Discuss;
import com.simmya.service.BaseService;
import com.simmya.util.DbUtil;

@Service
public class DiscussService extends BaseService<Discuss>{
	
	
	public List<Map<String, Object>> listDiscussByInfoid(String infoid) throws SQLException {
		String sql = "select u.nickname nickName,u.head_pic headPic,d.content content,DATE_FORMAT(d.create_time,'%Y-%m-%d %H:%i:%s') time "
				+ " FROM discuss d "
				+ " LEFT JOIN USER u ON d.user_id = u.id "
				+ " WHERE d.info_id = ?";
		return DbUtil.getMapList(sql, infoid);
	}
	
	
}
