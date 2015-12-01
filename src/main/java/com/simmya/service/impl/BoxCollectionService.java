package com.simmya.service.impl;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.simmya.pojo.BoxCollection;
import com.simmya.service.BaseService;
import com.simmya.util.DbUtil;

@Service
public class BoxCollectionService extends BaseService<BoxCollection>{
	
	
	/*
	 * [{'id':'2354234srte',NAME':'烧麦盒子','TITLE':'烧麦好吃',
	 * 'detail':'手工烧麦',imageAddress':'接口前缀+/image1.pig',
	 * 'shareCount':4,'boxPrice':100,'collectCount':'4'}
	 * */
	public List<Map<String, Object>> list(String id, String url) throws SQLException {
		String sql = "SELECT b.ID id,b.NAME NAME,b.TITLE TITLE,b.DETAIL detail,"
				+ " b.BOX_PRICE boxPrice,"
				+ " CONCAT('" + url +"',CASE WHEN b.IMAGE_ADDRESS IS NOT NULL THEN REPLACE(b.IMAGE_ADDRESS,'\\\\','/') END) imageAddress,"
				+ " COALESCE(b.SHARE_COUNT,0) shareCount,"
				+ " COALESCE(b.COLLECT_COUNT,0) collectCount "
				+ " FROM box_collection a "
				+ " LEFT JOIN box b ON a.BOX_ID = b.ID "
				+ " WHERE a.USER_ID = ?";
		return DbUtil.getMapList(sql, id);
	}
	
	
}
