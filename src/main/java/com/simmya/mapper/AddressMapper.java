package com.simmya.mapper;

import org.springframework.stereotype.Repository;

import com.simmya.pojo.Address;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface AddressMapper extends Mapper<Address> {

}