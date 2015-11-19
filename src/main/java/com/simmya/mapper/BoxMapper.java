package com.simmya.mapper;

import org.springframework.stereotype.Repository;

import com.simmya.pojo.Box;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface BoxMapper extends Mapper<Box> {
    
}