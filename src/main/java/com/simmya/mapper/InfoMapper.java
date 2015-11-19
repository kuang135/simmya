package com.simmya.mapper;

import org.springframework.stereotype.Repository;

import com.simmya.pojo.Info;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface InfoMapper extends Mapper<Info>{
	
}