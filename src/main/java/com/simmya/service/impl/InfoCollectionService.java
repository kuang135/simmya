package com.simmya.service.impl;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.simmya.pojo.InfoCollection;
import com.simmya.service.BaseService;
import com.simmya.util.DbUtil;

@Service
public class InfoCollectionService extends BaseService<InfoCollection>{
	
	
	/*
	 * [{'id':'2354234srte',NAME':'烧麦','TITLE':'烧麦好吃',
	 * 'detail':'手工烧麦','collectCount':4,'agreeCount':4,
	 * 'discussCount':4,'imageAddress':'接口前缀+/image1.pig',
	 * 'shareCount':4,'source':'www.baidu.com\xx','clickCount':'4'}]
	 */
	public List<Map<String, Object>> list(String id) throws SQLException {
		String sql = "SELECT b.ID id,b.NAME NAME,b.TITLE TITLE,b.DETAIL detail,"
				+ " b.COLLECT_COUNT collectCount,b.AGREE_COUNT agreeCount,"
				+ " b.DISCUSS_COUNT discussCount,b.IMAGE_ADDRESS imageAddress,"
				+ " b.SHARE_COUNT shareCount,b.SOURCE source,b.CLICK_COUNT clickCount "
				+ " FROM info_collection a "
				+ " LEFT JOIN info b ON a.INFO_ID = b.ID "
				+ " WHERE a.USER_ID = ?";
		return DbUtil.getMapList(sql, id);
	}
	
	
}
