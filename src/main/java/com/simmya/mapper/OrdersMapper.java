package com.simmya.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import tk.mybatis.mapper.common.Mapper;

import com.simmya.pojo.Orders;
import com.simmya.vo.OrderV;

@Repository
public interface OrdersMapper extends Mapper<Orders> {
   
	List<OrderV> getOrderListByUserid(@Param("userid")String userid);
	
}