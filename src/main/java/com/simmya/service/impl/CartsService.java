package com.simmya.service.impl;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simmya.mapper.BoxMapper;
import com.simmya.pojo.Box;
import com.simmya.pojo.Carts;
import com.simmya.service.BaseService;
import com.simmya.util.DbUtil;

@Service
public class CartsService extends BaseService<Carts>{
	
	@Autowired
	private BoxMapper boxMapper;
	
	/*
	 * 加入购物车
	 * 防止一个用户对同一个box进行两次加入购物操作
	 */
	public Map<String, Object> addToCart(String userid, String boxid) {
		Map<String, Object> map = new HashMap<String, Object>();
		Box box = boxMapper.selectByPrimaryKey(boxid);
		if (box == null) {
			map.put("code", "error");
			return map;
		}
		Carts carts = new Carts();
		carts.setUserId(userid);
		carts.setBoxIds(boxid);
		Carts existCart = super.selectOneByWhere(carts);
		if (existCart != null) {
			map.put("code", "error");
			return map;
		}
		super.saveSelective(carts);
		map.put("code", "sucess");
		return map;
	}

	/*
	 * [{'id':'2354234srte',NAME':'烧麦盒子','TITLE':'烧麦好吃',
	 *   'detail':'手工烧麦',imageAddress':'接口前缀+/image1.pig',
	 *   'shareCount':4,'boxPrice':100,'collectCount':'4'}
	 * ]
	 */
	public List<Map<String, Object>> listCarts(String id) throws SQLException {
		String sql = "SELECT b.id id,b.NAME NAME,b.TITLE TITLE,b.DETAIL detail,"
				+ " b.IMAGE_ADDRESS imageAddress,b.SHARE_COUNT shareCount,"
				+ " b.BOX_PRICE boxPrice,b.COLLECT_COUNT collectCount,b.DISCUSS_COUNT discussCount "
				+ " FROM carts c"
				+ " LEFT JOIN box b ON c.box_ids = b.id"
				+ " WHERE c.user_id = ?";
		return DbUtil.getMapList(sql, id);
	}

	public Map<String, Object> deleteCarts(String userid, String boxid) {
		Map<String, Object> map = new HashMap<String, Object>();
		Carts carts = new Carts();
		carts.setUserId(userid);
		carts.setBoxIds(boxid);
		int r = super.deleteByWhere(carts);
		if (r == 1) {
			map.put("code", "sucess");
		} else {
			map.put("code", "error");
		}
		return map;
	}
	
	
}
