package com.simmya.service.impl;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simmya.mapper.BoxCollectionMapper;
import com.simmya.pojo.Box;
import com.simmya.pojo.BoxCollection;
import com.simmya.service.BaseService;
import com.simmya.util.DbUtil;

@Service
public class BoxService extends BaseService<Box>{
	
	@Autowired
	private BoxCollectionMapper boxCollectionMapper;
	
	public List<Map<String, Object>> listBox(int start, int size) throws SQLException {
		String sql = "select ID id,NAME NAME,TITLE TITLE,DETAIL detail,"
				+ "	IMAGE_ADDRESS imageAddress,SHARE_COUNT shareCount,"
				+ "	BOX_PRICE boxPrice,COLLECT_COUNT collectCount,DISCUSS_COUNT discussCount "
				+ "	from box limit ?,?";
		return DbUtil.getMapList(sql, start, size);
	}

	public List<Map<String, Object>> detail(String boxid) throws SQLException {
		String sql = "select ID id,NAME NAME,TITLE TITLE,DETAIL detail,"
				+ "	IMAGE_ADDRESS imageAddress,SHARE_COUNT shareCount,"
				+ "	BOX_PRICE boxPrice,COLLECT_COUNT collectCount,DISCUSS_COUNT discussCount "
				+ "	from box where id = ?";
		return DbUtil.getMapList(sql, boxid);
	}

	
	/*
	 * box 存在
	 * 检查该用户有没有收藏过该box
	 * box_collection表添加记录
	 * box更新collect_count
	 */
	public Map<String, Object> collect(String userid, String boxid) {
		Map<String, Object> map = new HashMap<String, Object>();
		Box box = super.selectByPrimaryKey(boxid);
		if (box == null) {
			map.put("code", "error");
			return map;
		}
		BoxCollection boxCollection = new BoxCollection();
		boxCollection.setUserId(userid);
		boxCollection.setBoxId(boxid);
		List<BoxCollection> list = boxCollectionMapper.select(boxCollection);
		if (list != null && list.size() > 0) {
			map.put("code", "error");
			map.put("desc", "你已收藏");
			return map;
		}
		boxCollectionMapper.insert(boxCollection);
		box.setCollectCount(box.getCollectCount() + 1); 
		super.updateSelective(box);
		map.put("code", "sucess");
		map.put("desc", "成功");
		return map;
	}

	/*
	 * box 存在
	 * box更新share_count(一个用户可以分享多次)
	 */
	public Map<String, Object> share(String userid, String boxid) {
		Map<String, Object> map = new HashMap<String, Object>();
		Box box = super.selectByPrimaryKey(boxid);
		if (box == null) {
			map.put("code", "error");
			return map;
		}
		box.setShareCount(box.getShareCount() + 1); 
		super.updateSelective(box);
		map.put("desc", "sucess");
		return map;
	}

	
}
