package com.simmya.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.common.Mapper;

public class BaseService<T> {

	@Autowired
    protected Mapper<T> mapper;

	public T selectByPrimaryKey(Object id) {
		return mapper.selectByPrimaryKey(id);
	}
	
	public List<T> selectListByWhere(T t) {
		return mapper.select(t);
	}
	
	public T selectOneByWhere(T t) {
		List<T> list = mapper.select(t);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public List<T> selectAll() {
		return mapper.selectAll();
	}
	
	public PageInfo<T> selectPage(int pageNum,int pageSize){
		PageHelper.startPage(pageNum, pageSize);
		List<T> list = mapper.select(null);
		PageInfo<T> pageInfo=new PageInfo<T>(list);
		return pageInfo;
	}
	
	public PageInfo<T> selectPage(int pageNum,int pageSize, String order){
		PageHelper.startPage(pageNum, pageSize, order);
		List<T> list = mapper.select(null);
		PageInfo<T> pageInfo=new PageInfo<T>(list);
		return pageInfo;
	}
	
	public PageInfo<T> selectPageByWhere(int pageNum,int pageSize, T t){
		PageHelper.startPage(pageNum, pageSize);
		List<T> list = mapper.select(t);
		PageInfo<T> pageInfo=new PageInfo<T>(list);
		return pageInfo;
	}
	
	/*
	 * 新增全部字段
	 */
    public int save(T entity){
        return mapper.insert(entity);
    }
    
    /*
     * 新增不为null的字段
     */
    public int saveSelective(T t) {
    	return mapper.insertSelective(t);
    }

    
    /*
     * 根据id删除
     */
    public int deleteById(Object id) {
    	return mapper.deleteByPrimaryKey(id);
    }
    
    /*
     * 根据条件删除
     */
    public int deleteByWhere(T entity){
        return mapper.delete(entity);
    }
    
    /*
     * 根据id更新，全部字段
     */
    public int update(T t) {
    	return mapper.updateByPrimaryKey(t);
    }

     /*
      *  根据id更新，不为null的字段
     */
    public int updateSelective(T t) {
    	return mapper.updateByPrimaryKeySelective(t);
    }
	
}
