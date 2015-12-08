package com.simmya.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simmya.constant.BoxStatus;
import com.simmya.constant.OrderStatus;
import com.simmya.constant.SendStatus;
import com.simmya.mapper.PayMapper;
import com.simmya.pojo.BoxCommit;
import com.simmya.pojo.OrderBoxRef;
import com.simmya.pojo.Orders;
import com.simmya.pojo.OrdersCommit;
import com.simmya.pojo.User;
import com.simmya.service.BaseService;
import com.simmya.vo.OrderV;

@Service
public class PayService extends BaseService<OrderBoxRef> {

	@Autowired
	private PayMapper payMapper;
	@Autowired
	private CartsService cartsService;

	@Transactional(rollbackFor = Exception.class)
	public Orders saveOrderRef(OrdersCommit order, User loginUser) {
		Orders order_ = new Orders();
		order_.setUserId(loginUser.getId());
		order_.setStatus(OrderStatus.NotPayed);
		order_.setAddressId(order.getAddressId());
		order_.setCreateTime(new Date());
		order_.setTotalPrice(new BigDecimal(order.getTotalPay()));
		order_.setBalancePrice(new BigDecimal(order.getPayBalance()));
		payMapper.insertOrders(order_);
		
		System.out.println(order_.getId());
		for(BoxCommit box:order.getBoxs()){
			OrderBoxRef boxRef=new OrderBoxRef();
			boxRef.setBoxId(box.getBoxid());
			boxRef.setOrderCount(box.getOrderCount());
			boxRef.setOrderId(order_.getId());
			boxRef.setOrderWay(box.getOrderWay());
			boxRef.setStatus(BoxStatus.NotCompleted);
			boxRef.setSendStatus(OrderStatus.NotPayed);
			super.save(boxRef);
			cartsService.deleteCarts(loginUser.getId(), boxRef.getBoxId());
		}
		
		return order_;
	}
	
	public List<OrderV> getOrderListNoPay(User loginUser){
		return payMapper.getOrderListNoPay(loginUser.getId());
	}

}
