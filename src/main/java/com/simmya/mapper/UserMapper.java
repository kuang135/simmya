package com.simmya.mapper;

import org.springframework.stereotype.Repository;

import com.simmya.pojo.User;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserMapper extends Mapper<User>{
    
}