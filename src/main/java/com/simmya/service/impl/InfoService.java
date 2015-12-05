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
import com.simmya.mapper.BoxInfoRefMapper;
import com.simmya.mapper.DiscussMapper;
import com.simmya.mapper.InfoAgreeMapper;
import com.simmya.mapper.InfoCollectionMapper;
import com.simmya.mapper.InfoMapper;
import com.simmya.mapper.UserShareRefMapper;
import com.simmya.pojo.BoxInfoRef;
import com.simmya.pojo.Discuss;
import com.simmya.pojo.Info;
import com.simmya.pojo.InfoAgree;
import com.simmya.pojo.InfoCollection;
import com.simmya.pojo.UserShareRef;
import com.simmya.service.BaseService;
import com.simmya.util.DbUtil;
import com.simmya.util.StringUtil;

@Service
public class InfoService extends BaseService<Info>{
	
	@Autowired
	private InfoAgreeMapper infoAgreeMapper;
	@Autowired
	private DiscussMapper discussMapper;
	@Autowired
	private InfoCollectionMapper infoCollectionMapper;
	@Autowired
	private UserShareRefMapper shareMapper;
	@Autowired
	private InfoMapper infoMapper;
	@Autowired
	private BoxInfoRefMapper boxInfoMapper;
	
	public List<Map<String, Object>> getInfoList(int start, int limit, String url) throws SQLException {
		String sql = "SELECT ID id,NAME name,TITLE title,DETAIL detail, "
				+ " CASE WHEN COLLECT_COUNT IS NULL THEN 0 ELSE COLLECT_COUNT END collectCount,"
				+ " CASE WHEN AGREE_COUNT IS NULL THEN 0 ELSE AGREE_COUNT END agreeCount, "
				+ " CASE WHEN DISCUSS_COUNT IS NULL THEN 0 ELSE DISCUSS_COUNT END discussCount,"
				+ " CASE WHEN SHARE_COUNT IS NULL THEN 0 ELSE SHARE_COUNT END shareCount,"
				+ " CASE WHEN CLICK_COUNT IS NULL THEN 0 ELSE CLICK_COUNT END clickCount,"
				+ " CONCAT('" + url + "',CASE WHEN IMAGE_ADDRESS IS NOT NULL THEN REPLACE(IMAGE_ADDRESS,'\\\\','/') END) imageAddress, "
				+ " SOURCE source "
				+ " FROM info ORDER BY click_count DESC LIMIT ?,?";
		List<Map<String,Object>> mapList = DbUtil.getMapList(sql, start, limit);
		return mapList;
	}
	
	public List<Map<String, Object>> getInfoListByToken(String userid, int start, int limit, String url) throws SQLException {
		String sql = "SELECT ID id,NAME name,TITLE title,DETAIL detail, "
				+ " CASE WHEN COLLECT_COUNT IS NULL THEN 0 ELSE COLLECT_COUNT END collectCount,"
				+ " CASE WHEN AGREE_COUNT IS NULL THEN 0 ELSE AGREE_COUNT END agreeCount, "
				+ " CASE WHEN DISCUSS_COUNT IS NULL THEN 0 ELSE DISCUSS_COUNT END discussCount,"
				+ " CASE WHEN SHARE_COUNT IS NULL THEN 0 ELSE SHARE_COUNT END shareCount,"
				+ " CASE WHEN CLICK_COUNT IS NULL THEN 0 ELSE CLICK_COUNT END clickCount,"
				+ " CONCAT('" + url + "',CASE WHEN IMAGE_ADDRESS IS NOT NULL THEN REPLACE(IMAGE_ADDRESS,'\\\\','/') END) imageAddress, "
				+ " SOURCE source "
				+ " FROM info ORDER BY click_count DESC LIMIT ?,?";
		List<Map<String,Object>> mapList = DbUtil.getMapList(sql, start, limit);
		if (mapList != null && mapList.size() > 0) {
			for (Map<String, Object> map : mapList) {
				InfoCollection infoCollect = new InfoCollection();
				infoCollect.setInfoId((String)map.get("id"));
				infoCollect.setUserId(userid);
				InfoCollection selectOne = infoCollectionMapper.selectOne(infoCollect);
				if (selectOne == null) {
					map.put("collected", false);
				} else {
					map.put("collected", true);
				}
				InfoAgree infoAgree = new InfoAgree();
				infoAgree.setInfoId((String)map.get("id"));
				infoAgree.setUserId(userid);
				InfoAgree selectOne2 = infoAgreeMapper.selectOne(infoAgree);
				if (selectOne2 == null) {
					map.put("agreed", false);
				} else {
					map.put("agreed", true);
				}
			}
		}
		return mapList;
	}
	

	public Map<String, Object> getDetailById(String infoid, String url) throws SQLException {
		Info info = new Info();
		info.setClickCount(info.getClickCount() + 1);
		info.setId(infoid);
		super.updateSelective(info);
		String sql = "SELECT ID id,NAME NAME,TITLE TITLE,DETAIL detail,"
				+ " CASE WHEN COLLECT_COUNT IS NULL THEN 0 ELSE COLLECT_COUNT END collectCount,"
				+ " CASE WHEN AGREE_COUNT IS NULL THEN 0 ELSE AGREE_COUNT END agreeCount, "
				+ " CASE WHEN DISCUSS_COUNT IS NULL THEN 0 ELSE DISCUSS_COUNT END discussCount,"
				+ " CASE WHEN SHARE_COUNT IS NULL THEN 0 ELSE SHARE_COUNT END shareCount,"
				+ " CASE WHEN CLICK_COUNT IS NULL THEN 0 ELSE CLICK_COUNT END clickCount,"
				+ " CONCAT('" + url + "',CASE WHEN IMAGE_ADDRESS IS NOT NULL THEN REPLACE(IMAGE_ADDRESS,'\\\\','/') END) imageAddress, "
				+ " SOURCE source"
				+ " FROM info where ID = ?";
		Map<String, Object> map = DbUtil.getMap(sql, infoid);
		return map;
	}

	/*
	 * info存在
	 * 检查该用户有没有给该信息点过赞
	 * info_agree表添加记录
	 * info更新agree_count
	 */
	@Transactional
	public Map<String, Object> doAgree(String userId, String infoId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Info info = super.selectByPrimaryKey(infoId);
		if (info == null) {
			map.put("code", "error");
			return map;
		}
		InfoAgree infoAgree = new InfoAgree();
		infoAgree.setUserId(userId);
		infoAgree.setInfoId(infoId);
		List<InfoAgree> list = infoAgreeMapper.select(infoAgree);
		if (list != null && list.size() > 0) {
			//{code:error,desc:"你已点赞"}
			map.put("code", "error");
			map.put("desc", "你已点赞");
			return map;
		} 
		infoAgreeMapper.insert(infoAgree);
		info.setAgreeCount(info.getAgreeCount() + 1);
		super.updateSelective(info);
		//{code:success,desc:"成功"} 
		map.put("code", "success");
		map.put("desc", "成功");
		return map;
	}

	
	/*
	 * info存在
	 * 检查该用户有没有给该信息评论过
	 * discuss表添加记录
	 * info更新discuss_count
	 */
	@Transactional
	public Map<String, Object> doDiscuss(String userid, String infoid, String content) {
		Map<String, Object> map = new HashMap<String, Object>();
		Info info = super.selectByPrimaryKey(infoid);
		if (info == null) {
			map.put("code", "error");
			return map;
		}
		Discuss discuss = new Discuss();
		discuss.setUserId(userid);
		discuss.setInfoId(infoid);
		List<Discuss> list = discussMapper.select(discuss);
		if (list != null && list.size() > 0) {
			map.put("code", "error");
			map.put("desc", "你已评论");
			return map;
		}
		discuss.setContent(content);
		discuss.setCreateTime(new Date());
		discussMapper.insert(discuss);
		info.setDiscussCount(info.getDiscussCount() + 1); 
		super.updateSelective(info);
		map.put("code", "success");
		map.put("desc", "成功");
		return map;
	}

	/*
	 * info 存在
	 * 检查该用户有没有收藏过该咨询
	 * info_collection表添加记录
	 * info更新collect_count
	 */
	@Transactional
	public Map<String, Object> collectInfo(String userid, String infoid) {
		Map<String, Object> map = new HashMap<String, Object>();
		Info info = super.selectByPrimaryKey(infoid);
		if (info == null) {
			map.put("code", "error");
			return map;
		}
		InfoCollection infoCollection = new InfoCollection();
		infoCollection.setUserId(userid);
		infoCollection.setInfoId(infoid);
		List<InfoCollection> list = infoCollectionMapper.select(infoCollection);
		if (list != null && list.size() > 0) {
			map.put("code", "error");
			map.put("desc", "你已收藏");
			return map;
		}
		infoCollectionMapper.insert(infoCollection);
		info.setCollectCount(info.getCollectCount() + 1); 
		super.updateSelective(info);
		map.put("code", "success");
		map.put("desc", "成功");
		return map;
	}

	
	/*
	 * info 存在
	 * 检查该用户有没有分享该咨询
	 * user_share_ref表添加记录
	 * info更新share_count
	 */
	public Map<String, Object> shareInfo(String userId, String infoId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Info info = super.selectByPrimaryKey(infoId);
		if (info == null) {
			map.put("code", "error");
			return map;
		}
		UserShareRef share = new UserShareRef();
		share.setUserId(userId);
		share.setInfoId(infoId);
		List<UserShareRef> list = shareMapper.select(share);
		if (list != null && list.size() > 0) {
			map.put("code", "error");
			return map;
		}
		shareMapper.insert(share);
		info.setShareCount(info.getShareCount() + 1);
		super.updateSelective(info);
		map.put("code", "success");
		return map;
	}

	public DataGrid getDataGrid(int page, int rows, Info info) {
		PageHelper.startPage(page, rows);
		List<Info> list = infoMapper.selectByName(info.getName());
		PageInfo<Info> pageInfo=new PageInfo<Info>(list);
		DataGrid datagrid=new DataGrid();
        if(list != null) {
        	datagrid.setTotal((int)pageInfo.getTotal());
        	for (Info bean : list) {
				String newDetail = StringUtil.insertTagByDistance(bean.getDetail().replace("<br/>", ""), "<br/>", 32);
				bean.setDetail(newDetail);
			}
        	datagrid.setRows(list);
        }
        return datagrid;
	}
	
	public DataGrid getAllBoxDataGrid() {
		List<Info> list = super.selectAll();
		DataGrid datagrid=new DataGrid();
        if(list != null) {
        	datagrid.setTotal(list.size());
        	datagrid.setRows(list);
        }
        return datagrid;
	}
	
	@Transactional
	public int deleteByIds(String[] ids, String realPath) throws SimmyaException {
		int count = 0;
		String[] pic = new String[ids.length];
		for(int i = 0; i < ids.length; i++) {
			//box_info_ref表有在使用数据，不让删除
			BoxInfoRef boxInfo = new BoxInfoRef();
			boxInfo.setInfoId(ids[i]);
			List<BoxInfoRef> boxInfos = boxInfoMapper.select(boxInfo);
			if (boxInfos != null && boxInfos.size() > 0) {
				throw new SimmyaException(" -- BOX_INFO_REF表中还存在着某个INFO -- ");
			}
			Info info = super.selectByPrimaryKey(ids[i]);
			pic[i] = info.getImageAddress();
			super.deleteById(ids[i]);
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
