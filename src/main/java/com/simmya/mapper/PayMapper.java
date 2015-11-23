package com.simmya.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.simmya.pojo.Orders;
import com.simmya.vo.OrderV;

@Repository
public interface PayMapper{
   
	void insertOrders(Orders order);
	
	List<OrderV> getOrderListNoPay(@Param("userid")String userid);
}