package com.simmya.mapper;

import org.springframework.stereotype.Repository;

import com.simmya.pojo.Discuss;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface DiscussMapper extends Mapper<Discuss> {
    
}