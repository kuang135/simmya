package com.simmya.service.impl;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simmya.pojo.Carts;
import com.simmya.service.BaseService;
import com.simmya.util.DbUtil;

@Service
public class CartsService extends BaseService<Carts>{
	
	
	/*
	 * 加入购物车
	 * 防止一个用户对同一个box进行两次加入购物操作
	 */
	@Transactional
	public Map<String, Object> addToCart(String userid, List<Carts> carts) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (carts != null && carts.size() > 0) {
				for (Carts cart : carts) {
					cart.setUserId(userid);
					super.saveSelective(cart);
				}
			}
			map.put("code", "success");
		} catch (Exception e) {
			map.put("code", "error");
		}
		return map;
	}

	/*
	 * [{'id':'2354234srte',NAME':'烧麦盒子','TITLE':'烧麦好吃',
	 *   'detail':'手工烧麦',imageAddress':'接口前缀+/image1.pig',
	 *   'shareCount':4,'boxPrice':100,'price':100,'collectCount':'4',
	 *   "orderCount" : 3,
		"orderWay" : "一周一期",
		"balance" : 10,
		"totalPrice" : 290}
	 * ]
	 */
	public List<Map<String, Object>> listCarts(String id, String url) throws SQLException {
		String sql = "SELECT c.id cartId,b.id boxId,b.NAME name,b.TITLE title,b.DETAIL detail,"
				+ " COALESCE(b.BOX_PRICE,0) boxPrice,"
				+ " CONCAT('" + url +"',CASE WHEN b.IMAGE_ADDRESS IS NOT NULL THEN REPLACE(b.IMAGE_ADDRESS,'\\\\','/') END) imageAddress,"
				+ " COALESCE(b.SHARE_COUNT,0) shareCount,"
				+ " COALESCE(b.COLLECT_COUNT,0) collectCount,"
				+ " COALESCE(b.DISCUSS_COUNT,0) discussCount,"
				+ " COALESCE(c.TOTAL_PRICE,0) totalPrice,"
				+ " COALESCE(c.BALANCE,0) balance,"
				+ " COALESCE(c.ORDER_COUNT,0) orderCount,"
				+ " c.ORDER_WAY orderWay "
				+ " FROM carts c"
				+ " LEFT JOIN box b ON c.box_id = b.id"
				+ " WHERE c.user_id = ?";
		return DbUtil.getMapList(sql, id);
	}

	@Transactional
	public Map<String, Object> deleteCarts(String userid, String cartIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isBlank(cartIds)) {
			map.put("code", "error");
			return map;
		}
		try {
			String[] ids = cartIds.split(",");
			for (String id : ids) {
				Carts carts = new Carts();
				carts.setId(id);
				super.deleteByWhere(carts);
			}
			map.put("code", "success");
		} catch (Exception e) {
			map.put("code", "error");
		}
		return map;
	}
	
	
}
