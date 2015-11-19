package com.simmya.service.impl;



import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simmya.constant.BoxStatus;
import com.simmya.constant.OrderStatus;
import com.simmya.mapper.BackBoxMapper;
import com.simmya.mapper.BackOrderMapper;
import com.simmya.mapper.OrderBoxRefMapper;
import com.simmya.mapper.OrdersMapper;
import com.simmya.pojo.BackBox;
import com.simmya.pojo.BackOrder;
import com.simmya.pojo.OrderBoxRef;
import com.simmya.pojo.Orders;
import com.simmya.service.BaseService;
import com.simmya.util.DbUtil;
import com.simmya.vo.OrderV;

@Service
public class OrdersService extends BaseService<Orders>{

	@Autowired
	private OrdersMapper orderMapper;
	@Autowired
	private BackOrderMapper backOrderMapper;
	@Autowired
	private OrderBoxRefMapper orderBoxRefMapper;
	@Autowired
	private BackBoxMapper backBoxMapper;
	

	/*
	 * [{'id':'134sdrgtwe43','createTime':'20150805 10:10:10',‘address’:'嘉兴桐乡',
	 * 'boxs':[{'id':'2354234srte','name':'烧麦','TITLE':'烧麦好吃',
	 * 			'detail':'手工烧麦',imageAddress':'接口前缀+/image1.pig',
	 * 			'price':'100','orderWay':'一周一次'，‘orderCount（订阅期限）’：‘5’,‘sendCount(已发期数)’:'3'}]
	 */
	public List<OrderV> listOrders(String id) throws SQLException {
		List<OrderV> list = orderMapper.getOrderListByUserid(id);
		return list;
	}


	/*
	 * [{'id':'2354234srte',NAME':'烧麦盒子','TITLE':'烧麦好吃',
	 * 'detail':'手工烧麦',imageAddress':'接口前缀+/image1.pig',
	 * 'shareCount':4,'boxPrice':100,'collectCount':'4'}]
	 */
	public List<Map<String, Object>> listOrderBoxes(String userid, String orderid) throws SQLException {
		String sql = "SELECT c.ID id,c.NAME NAME,c.TITLE TITLE,c.DETAIL detail,c.IMAGE_ADDRESS imageAddress, "
				+ " c.SHARE_COUNT shareCount,c.BOX_PRICE boxPrice,c.COLLECT_COUNT collectCount "
				+ " FROM orders a "
				+ " LEFT JOIN ORDER_BOX_REF b ON b.ORDER_ID = a.ID "
				+ " LEFT JOIN box c ON c.ID = b.BOX_ID "
				+ " WHERE a.USER_ID = ? AND a.ID = ?";
		return DbUtil.getMapList(sql, userid, orderid);
	}


	/*
	 * 取消整个订单
	 * 修改订单的状态为： 退订
	 * 修改订单所有的box的状态为退订
	 * 订单退订表添加记录
	 */
	@Transactional
	public Map<String, Object> backOrderAdd(String id, String orderId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Orders od = super.selectByPrimaryKey(orderId);
		if (od == null) {
			map.put("code", "error");
			return map;
		}
		try {
			od.setStatus(OrderStatus.Back.toString());
			super.updateSelective(od);
			String sql = "UPDATE order_box_ref SET STATUS = ? WHERE order_id = ?";
			DbUtil.update(sql, OrderStatus.Back, orderId);
			BackOrder backOrder = new BackOrder();
			backOrder.setOrdersId(orderId);
			backOrder.setUserId(id);
			backOrder.setCreateTime(new Date());
			backOrderMapper.insertSelective(backOrder);
			map.put("code", "success");
		} catch (Exception e) {
			map.put("code", "error");
		}
		return map;
	}

	
	/*
	 * 已付款订单退订某个盒子
	 * 1.确定订单的状态为已付款
	 * 2.将改订单的某个盒子的状态设置为退订
	 * 3.back_box表中插入数据
	 * 
	 */
	public Map<String, Object> orderBoxBack(String userId, String orderId, String boxId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "error");
		Orders order = super.selectByPrimaryKey(orderId);
		if (order != null && order.getStatus().equals(OrderStatus.Payed.toString())) {
			OrderBoxRef orderBoxRef = new OrderBoxRef();
			orderBoxRef.setOrderId(orderId);
			orderBoxRef.setBoxId(boxId);
			List<OrderBoxRef> list = orderBoxRefMapper.select(orderBoxRef);
			if (list != null && list.size() > 0) {
				OrderBoxRef bean = list.get(0);
				bean.setStatus(BoxStatus.Back.toString());
				orderBoxRefMapper.updateByPrimaryKeySelective(bean);
				BackBox backBox = new BackBox();
				backBox.setBoxId(boxId);
				backBox.setOrderId(orderId);
				backBox.setUserId(userId);
				backBox.setCreateTime(new Date());
				backBoxMapper.insert(backBox);
				map.put("code", "success");
				return map;
			}
		}
		return map;
	}

}
