package com.simmya.service.impl;


import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simmya.easyui.DataGrid;
import com.simmya.exception.SimmyaException;
import com.simmya.mapper.BoxCollectionMapper;
import com.simmya.mapper.BoxDiscussMapper;
import com.simmya.mapper.BoxInfoRefMapper;
import com.simmya.mapper.BoxMapper;
import com.simmya.mapper.OrderBoxRefMapper;
import com.simmya.pojo.Box;
import com.simmya.pojo.BoxCollection;
import com.simmya.pojo.BoxDiscuss;
import com.simmya.pojo.BoxInfoRef;
import com.simmya.pojo.OrderBoxRef;
import com.simmya.service.BaseService;
import com.simmya.util.DbUtil;
import com.simmya.util.StringUtil;

@Service
public class BoxService extends BaseService<Box>{
	
	@Autowired
	private BoxCollectionMapper boxCollectionMapper;
	@Autowired
	private BoxDiscussMapper boxDiscussMapper;
	@Autowired
	private BoxMapper boxMapper;
	@Autowired
	private OrderBoxRefMapper orderBoxMapper;
	@Autowired
	private BoxInfoRefMapper boxInfoMapper;
	
	public List<Map<String, Object>> listBox(int start, int size) throws SQLException {
		String sql = "select ID id,NAME NAME,TITLE TITLE,DETAIL detail,"
				+ "	IMAGE_ADDRESS imageAddress,SHARE_COUNT shareCount,"
				+ "	BOX_PRICE boxPrice,COLLECT_COUNT collectCount,DISCUSS_COUNT discussCount "
				+ "	from box limit ?,?";
		return DbUtil.getMapList(sql, start, size);
	}

	public List<Map<String, Object>> detail(String boxid) throws SQLException {
		String sql = "select ID id,NAME NAME,TITLE TITLE,DETAIL detail,"
				+ "	IMAGE_ADDRESS imageAddress,SHARE_COUNT shareCount,"
				+ "	BOX_PRICE boxPrice,COLLECT_COUNT collectCount,DISCUSS_COUNT discussCount "
				+ "	from box where id = ?";
		return DbUtil.getMapList(sql, boxid);
	}

	
	/*
	 * box 存在
	 * 检查该用户有没有收藏过该box
	 * box_collection表添加记录
	 * box更新collect_count
	 */
	public Map<String, Object> collect(String userid, String boxid) {
		Map<String, Object> map = new HashMap<String, Object>();
		Box box = super.selectByPrimaryKey(boxid);
		if (box == null) {
			map.put("code", "error");
			return map;
		}
		BoxCollection boxCollection = new BoxCollection();
		boxCollection.setUserId(userid);
		boxCollection.setBoxId(boxid);
		List<BoxCollection> list = boxCollectionMapper.select(boxCollection);
		if (list != null && list.size() > 0) {
			map.put("code", "error");
			map.put("desc", "你已收藏");
			return map;
		}
		boxCollectionMapper.insert(boxCollection);
		box.setCollectCount(box.getCollectCount() + 1); 
		super.updateSelective(box);
		map.put("code", "sucess");
		map.put("desc", "成功");
		return map;
	}

	/*
	 * box 存在
	 * box更新share_count(一个用户可以分享多次)
	 */
	public Map<String, Object> share(String userid, String boxid) {
		Map<String, Object> map = new HashMap<String, Object>();
		Box box = super.selectByPrimaryKey(boxid);
		if (box == null) {
			map.put("code", "error");
			return map;
		}
		box.setShareCount(box.getShareCount() + 1); 
		super.updateSelective(box);
		map.put("desc", "sucess");
		return map;
	}

	public Map<String, Object> doDiscuss(String userid, String boxId, String content) {
		Map<String, Object> map = new HashMap<String, Object>();
		Box box = super.selectByPrimaryKey(boxId);
		if (box == null) {
			map.put("code", "error");
			return map;
		}
		BoxDiscuss boxDiscuss = new BoxDiscuss();
		boxDiscuss.setUserId(userid);
		boxDiscuss.setBoxId(boxId);;
		List<BoxDiscuss> list = boxDiscussMapper.select(boxDiscuss);
		if (list != null && list.size() > 0) {
			map.put("code", "error");
			map.put("desc", "你已评论");
			return map;
		}
		boxDiscuss.setContent(content);
		boxDiscuss.setCreateTime(new Date());
		boxDiscussMapper.insert(boxDiscuss);
		box.setDiscussCount(box.getDiscussCount() + 1); 
		super.updateSelective(box);
		map.put("code", "sucess");
		map.put("desc", "成功");
		return map;
	}

	public DataGrid getDataGrid(int page, int rows, Box box) {
		PageHelper.startPage(page, rows);
		List<Box> boxes = boxMapper.selectByName(box.getName());
		PageInfo<Box> pageInfo=new PageInfo<Box>(boxes);
		DataGrid datagrid=new DataGrid();
        if(boxes != null){
        	datagrid.setTotal((int)pageInfo.getTotal());
        	for (Box bean : boxes) {
				String newDetail = StringUtil.insertTagByDistance(bean.getDetail().replace("<br/>", ""), "<br/>", 32);
				bean.setDetail(newDetail);
			}
        	datagrid.setRows(boxes);
        }
        return datagrid;
	}

	@Transactional
	public int deleteByIds(String[] ids, String realPath) throws SimmyaException {
		int count = 0;
		String[] pic = new String[ids.length];
		for(int i = 0; i < ids.length; i++) {
			//order_box_ref表有在使用数据，不让删除
			OrderBoxRef orderBox = new OrderBoxRef();
			orderBox.setBoxId(ids[i]);
			List<OrderBoxRef> orderBoxs = orderBoxMapper.select(orderBox);
			if (orderBoxs != null && orderBoxs.size() > 0) {
				throw new SimmyaException(" -- ORDER_BOX_REF表中还存在着某个BOX -- ");
			}
		}
		for(int i = 0; i < ids.length; i++) {
			Box box = super.selectByPrimaryKey(ids[i]);
			pic[i] = box.getImageAddress();
			super.deleteById(ids[i]);
			BoxInfoRef boxInfo = new BoxInfoRef();
			boxInfo.setBoxId(ids[i]);
			boxInfoMapper.delete(boxInfo);
		}
		for(int i = 0; i < pic.length; i++) {
			File file = new File(realPath, pic[i]);
			if (file.exists()) {
				file.delete();
			}
			count ++;
		}
		return count;
	}

	
}
