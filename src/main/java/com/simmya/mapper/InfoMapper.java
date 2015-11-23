package com.simmya.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.simmya.pojo.Info;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface InfoMapper extends Mapper<Info>{

	List<Info> selectByName(@Param("name")String name);
	
}