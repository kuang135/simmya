package com.simmya.mapper;

import org.springframework.stereotype.Repository;

import com.simmya.pojo.BackOrder;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface BackOrderMapper extends Mapper<BackOrder> {
    
}