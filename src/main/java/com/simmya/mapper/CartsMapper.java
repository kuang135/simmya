package com.simmya.mapper;

import org.springframework.stereotype.Repository;

import com.simmya.pojo.Carts;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface CartsMapper extends Mapper<Carts> {
    
}