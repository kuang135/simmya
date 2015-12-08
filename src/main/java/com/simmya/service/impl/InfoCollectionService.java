package com.simmya.service.impl;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simmya.mapper.InfoMapper;
import com.simmya.pojo.Info;
import com.simmya.pojo.InfoCollection;
import com.simmya.service.BaseService;
import com.simmya.util.DbUtil;

@Service
public class InfoCollectionService extends BaseService<InfoCollection>{
	
	
	@Autowired
	private InfoMapper infoMapper;
	
	/*
	 * [{'id':'2354234srte',NAME':'烧麦','TITLE':'烧麦好吃',
	 * 'detail':'手工烧麦','collectCount':4,'agreeCount':4,
	 * 'discussCount':4,'imageAddress':'接口前缀+/image1.pig',
	 * 'shareCount':4,'source':'www.baidu.com\xx','clickCount':'4'}]
	 */
	public List<Map<String, Object>> list(String id, String url) throws SQLException {
		String sql = "SELECT b.ID id,b.NAME NAME,b.TITLE TITLE,b.DETAIL detail,"
				+ " COALESCE(b.COLLECT_COUNT,0) collectCount,"
				+ " COALESCE(b.AGREE_COUNT,0) agreeCount,"
				+ " COALESCE(b.DISCUSS_COUNT,0) discussCount,"
				+ " CONCAT('" + url +"',CASE WHEN b.IMAGE_ADDRESS IS NOT NULL THEN REPLACE(b.IMAGE_ADDRESS,'\\\\','/') END) imageAddress,"
				+ " COALESCE(b.SHARE_COUNT,0) shareCount,"
				+ " COALESCE(b.CLICK_COUNT,0) clickCount, "
				+ " b.SOURCE source "
				+ " FROM info_collection a "
				+ " LEFT JOIN info b ON a.INFO_ID = b.ID "
				+ " WHERE a.USER_ID = ?";
		return DbUtil.getMapList(sql, id);
	}

	@Transactional
	public Map<String, Object> deleteCollect(String id, String infoid) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			InfoCollection ic = new InfoCollection();
			ic.setUserId(id);
			ic.setInfoId(infoid);
			int c = super.deleteByWhere(ic);
			if (c > 0) {
				Info info = infoMapper.selectByPrimaryKey(infoid);
				info.setCollectCount(info.getCollectCount() -1);
				infoMapper.updateByPrimaryKeySelective(info);
				map.put("code", "success");
			} else {
				map.put("code", "error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "error");
		}
		return map;
	}
	
	
}
