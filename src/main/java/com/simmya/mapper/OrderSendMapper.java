package com.simmya.mapper;


import org.springframework.stereotype.Repository;

import com.simmya.pojo.OrderSend;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface OrderSendMapper extends Mapper<OrderSend> {

}