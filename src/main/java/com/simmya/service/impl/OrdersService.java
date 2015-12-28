package com.simmya.service.impl;



import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simmya.constant.BoxStatus;
import com.simmya.constant.OrderStatus;
import com.simmya.constant.SendStatus;
import com.simmya.easyui.AjaxResult;
import com.simmya.easyui.DataGrid;
import com.simmya.exception.SimmyaException;
import com.simmya.mapper.BackBoxMapper;
import com.simmya.mapper.BackOrderMapper;
import com.simmya.mapper.BoxMapper;
import com.simmya.mapper.OrderBoxRefMapper;
import com.simmya.mapper.OrderSendMapper;
import com.simmya.mapper.OrdersMapper;
import com.simmya.pojo.BackBox;
import com.simmya.pojo.BackOrder;
import com.simmya.pojo.Box;
import com.simmya.pojo.OrderBoxRef;
import com.simmya.pojo.OrderSend;
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
	@Autowired
	private OrderSendMapper orderSendMapper;
	@Autowired
	private BoxMapper boxMapper;
	/*
	 * [{'id':'134sdrgtwe43','createTime':'20150805 10:10:10',‘address’:'嘉兴桐乡',
	 * 'boxs':[{'id':'2354234srte','name':'烧麦','TITLE':'烧麦好吃',
	 * 			'detail':'手工烧麦',imageAddress':'接口前缀+/image1.pig',
	 * 			'price':'100','orderWay':'一周一次'，‘orderCount（订阅期限）’：‘5’,‘sendCount(已发期数)’:'3'}]
	 */
	public List<OrderV> listOrders(String id, String url, String status) throws SQLException {
		List<OrderV> list = orderMapper.getOrderListByUserid(id, url, status);
		return list;
	}
	
	public List<OrderV> getOrder(String userid, String url, String orderid) throws SQLException {
		List<OrderV> list = orderMapper.getOrderByid(userid, url, orderid);
		return list;
	}
	


	/*
	 * [{'id':'2354234srte',NAME':'烧麦盒子','TITLE':'烧麦好吃',
	 * 'detail':'手工烧麦',imageAddress':'接口前缀+/image1.pig',
	 * 'shareCount':4,'boxPrice':100,'collectCount':'4'}]
	 */
	public List<Map<String, Object>> listOrderBoxes(String userid, String orderid, String url) throws SQLException {
		String sql = "SELECT c.ID id,c.NAME NAME,c.TITLE TITLE,c.DETAIL detail,"
				+ " c.BOX_PRICE boxPrice,"
				+ " CONCAT('"+url+"',CASE WHEN c.IMAGE_ADDRESS IS NOT NULL THEN REPLACE(c.IMAGE_ADDRESS,'\\\\','/') END) imageAddress, "
				+ " COALESCE(c.SHARE_COUNT,0) shareCount, "
				+ " COALESCE(c.COLLECT_COUNT,0) collectCount " 
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
			od.setStatus(OrderStatus.Back);
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
	 * 4.如果某个orderid下所有的box为已退订，就修改order 的状态为退订
	 */
	public Map<String, Object> orderBoxBack(String userId, String orderId, String boxId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "error");
		Orders order = super.selectByPrimaryKey(orderId);
		if (order == null || !order.getStatus().equals(OrderStatus.NotPayed))
			return map;
		OrderBoxRef orderBoxRef = new OrderBoxRef();
		orderBoxRef.setOrderId(orderId);
		orderBoxRef.setBoxId(boxId);
		List<OrderBoxRef> list = orderBoxRefMapper.select(orderBoxRef);
		if (list != null && list.size() > 0) {
			OrderBoxRef bean = list.get(0);
			bean.setStatus(BoxStatus.Back);
			orderBoxRefMapper.updateByPrimaryKeySelective(bean);
			BackBox backBox = new BackBox();
			backBox.setBoxId(boxId);
			backBox.setOrderId(orderId);
			backBox.setUserId(userId);
			backBox.setCreateTime(new Date());
			backBoxMapper.insert(backBox);
		}
		OrderBoxRef orderBoxRef2 = new OrderBoxRef();
		orderBoxRef2.setOrderId(orderId);
		List<OrderBoxRef> lists = orderBoxRefMapper.select(orderBoxRef2);
		boolean flag = true;
		if (lists != null && lists.size() > 0) {
			for (OrderBoxRef obf : lists) {
				if (!boxId.equals(obf.getBoxId())) {
					String status = obf.getStatus();
					if (!status.equals(BoxStatus.Back)) {
						flag = false;
						break;
					}
				}
			}
		}
		if (flag) {
			Orders ods = super.selectByPrimaryKey(orderId);
			ods.setStatus(OrderStatus.Back);
			super.updateSelective(ods);
		}
		map.put("code", "success");
		return map;
	}


	public DataGrid getOrderDataGrid(int page, int rows, Orders orders) {
		PageHelper.startPage(page, rows, "CREATE_TIME DESC");
		List<Orders> list = orderMapper.selectByStatusAndId(orders.getStatus(), orders.getId());
		PageInfo<Orders> pageInfo=new PageInfo<Orders>(list);
		DataGrid datagrid=new DataGrid();
        if(list != null) {
        	datagrid.setTotal((int)pageInfo.getTotal());
        	datagrid.setRows(list);
        }
        return datagrid;
	}


	public DataGrid getBoxDataGrid(String orderid) throws SQLException {
		String sql = "SELECT a.BOX_ID id,b.NAME name,b.BOX_PRICE boxPrice,a.ORDER_COUNT orderCount,a.ORDER_WAY orderWay,a.STATUS status,"
				+ " a.SEND_COUNT sendCount,DATE_FORMAT(a.UPDATE_TIME,'%Y-%m-%d %H:%i:%s') updateTime,"
				+ " a.SEND_STATUS sendStatus "
				+ " FROM order_box_ref a "
				+ " LEFT JOIN box b ON a.BOX_ID = b.ID "
				+ " WHERE a.ORDER_ID = ? "
				+ " ORDER BY a.UPDATE_TIME DESC";
		List<Map<String, Object>> list = DbUtil.getMapList(sql, orderid);
		DataGrid datagrid=new DataGrid();
        if(list != null) {
        	datagrid.setTotal(list.size());
        	datagrid.setRows(list);
        }
        return datagrid;
	}



	@Transactional
	public Map<String, Object> receiveOrderBox(String id, String orderid, String boxid, int count) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			OrderBoxRef orderBoxRef = new OrderBoxRef();
			orderBoxRef.setOrderId(orderid);
			orderBoxRef.setBoxId(boxid);
			OrderBoxRef selectOne = orderBoxRefMapper.selectOne(orderBoxRef);
			if (selectOne != null) {
				selectOne.setSendStatus(SendStatus.Received);
				orderBoxRefMapper.updateByPrimaryKeySelective(selectOne);
			} else {
				throw new SimmyaException(" -- 记录缺失 -- ");
			}
			
			OrderSend orderSend = new OrderSend();
			orderSend.setOrderId(orderid);
			orderSend.setBoxId(boxid);
			orderSend.setCount(count);
			OrderSend selectOne2 = orderSendMapper.selectOne(orderSend);
			if (selectOne2 != null) {
				selectOne2.setReceived(true);
				orderSendMapper.updateByPrimaryKeySelective(selectOne2);
			} else {
				throw new SimmyaException(" -- 记录缺失 -- ");
			}
			
			map.put("code", "success");
		} catch (Exception e) {
			map.put("code", "error");
		}
		return map;
	}



	@Transactional
	public Map<String, Object> discussOrderBox(String id, String orderid, String boxid, int count, String discuss) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			OrderBoxRef orderBoxRef = new OrderBoxRef();
			orderBoxRef.setOrderId(orderid);
			orderBoxRef.setBoxId(boxid);
			OrderBoxRef selectOne = orderBoxRefMapper.selectOne(orderBoxRef);
			if (selectOne != null) {
				selectOne.setSendStatus(SendStatus.Evaluated);
				orderBoxRefMapper.updateByPrimaryKeySelective(selectOne);
			} else {
				throw new SimmyaException(" -- 记录缺失 -- ");
			}
			
			OrderSend orderSend = new OrderSend();
			orderSend.setOrderId(orderid);
			orderSend.setBoxId(boxid);
			orderSend.setCount(count);
			OrderSend selectOne2 = orderSendMapper.selectOne(orderSend);
			if (selectOne2 != null) {
				selectOne2.setDiscuss(discuss);
				orderSendMapper.updateByPrimaryKeySelective(selectOne2);
			} else {
				throw new SimmyaException(" -- 记录缺失 -- ");
			}
			Box box = boxMapper.selectByPrimaryKey(boxid);
			if (box != null) {
				box.setDiscussCount(box.getDiscussCount() + 1); 
				boxMapper.updateByPrimaryKeySelective(box);
			} else {
				throw new SimmyaException(" -- 记录缺失 -- ");
			}
			map.put("code", "success");
		} catch (Exception e) {
			map.put("code", "error");
		}
		return map;
	}



	/*
	 *  order_box_ref 表更改
	 *  	count +1, sendStauts 修改， updatetime 修改
	 *  	box 已完成，order 已完成
	 *   orders-send 表添加记录
	 */
	@Transactional
	public AjaxResult sendBox(String orderid, String boxid) {
		Orders od = super.selectByPrimaryKey(orderid);
		if (OrderStatus.NotPayed.equals(od.getStatus())) {
			return new AjaxResult(400, "该订单还未付款，不能发送");
		}
		if (OrderStatus.Back.equals(od.getStatus())) {
			return new AjaxResult(400, "该订单已退订，不能发送");
		}
		if (OrderStatus.Completed.equals(od.getStatus())) {
			return new AjaxResult(400, "该订单已完成，不能发送");
		}
		OrderBoxRef orderBoxRef = new OrderBoxRef();
		orderBoxRef.setOrderId(orderid);
		orderBoxRef.setBoxId(boxid);
		OrderBoxRef selectOne = orderBoxRefMapper.selectOne(orderBoxRef);
		Integer sendCount = null;
		if (selectOne != null) {
			sendCount = selectOne.getSendCount();
			if (sendCount == null) {
				sendCount = 0;
			}
			Integer orderCount = selectOne.getOrderCount();
			if (orderCount == null) {
				orderCount = 0;
			}
			if (BoxStatus.Back.equals(selectOne.getStatus())) {
				return new AjaxResult(400, "该盒子已经退订，不能发送。");
			}
			if (sendCount >= orderCount) {
				return new AjaxResult(400, "该盒子已经全部发送完，不能再发送。");
			}
			selectOne.setSendCount(sendCount + 1);
			if ((sendCount + 1) == selectOne.getOrderCount()) {
				selectOne.setStatus(BoxStatus.Completed);
			}
			selectOne.setSendStatus(SendStatus.Sended);
			selectOne.setUpdateTime(new Date());
			orderBoxRefMapper.updateByPrimaryKeySelective(selectOne);
		} else {
			return new AjaxResult(400, "发送操作失败。");
		}
		if (sendCount != null) {
			OrderSend orderSend = new OrderSend();
			orderSend.setOrderId(orderid);
			orderSend.setBoxId(boxid);
			orderSend.setReceived(false);
			orderSend.setCount(sendCount + 1);
			orderSend.setDiscuss("");
			orderSendMapper.insertSelective(orderSend);
		}
		/*	修改order 的status
		 * 		所有box的status为已完成，order的status就为已完成
		 * 		所有box的status为已退订，order的status就为已退订
		 * 		box的status为已完成，或已退订，order的status就为已完成
		 */
		OrderBoxRef orderBoxRef2 = new OrderBoxRef();
		orderBoxRef2.setOrderId(orderid);
		List<OrderBoxRef> lists = orderBoxRefMapper.select(orderBoxRef2);
		boolean flag = true;
		if (lists != null && lists.size() > 0) {
			for (OrderBoxRef obf : lists) {
				String status = obf.getStatus();
				if (status.equals(BoxStatus.NotCompleted)) {
					flag = false;
					break;
				}
			}
		}
		if (flag) {
			Orders ods = super.selectByPrimaryKey(orderid);
			ods.setStatus(OrderStatus.Completed);
			super.updateSelective(ods);
		}
		return new AjaxResult(200, "发送操作成功。");
	}

}
