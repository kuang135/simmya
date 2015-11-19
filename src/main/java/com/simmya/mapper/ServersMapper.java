package com.simmya.mapper;

import org.springframework.stereotype.Repository;

import com.simmya.pojo.Servers;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ServersMapper extends Mapper<Servers> {
    
}