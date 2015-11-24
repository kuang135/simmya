package com.simmya.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.simmya.pojo.Box;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface BoxMapper extends Mapper<Box> {

	List<Box> selectByName(@Param("name")String name);
    
}