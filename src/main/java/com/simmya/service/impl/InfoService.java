package com.simmya.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simmya.mapper.DiscussMapper;
import com.simmya.mapper.InfoAgreeMapper;
import com.simmya.mapper.InfoCollectionMapper;
import com.simmya.mapper.UserShareRefMapper;
import com.simmya.pojo.Discuss;
import com.simmya.pojo.Info;
import com.simmya.pojo.InfoAgree;
import com.simmya.pojo.InfoCollection;
import com.simmya.pojo.UserShareRef;
import com.simmya.service.BaseService;
import com.simmya.util.DbUtil;

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
	
	public List<Map<String, Object>> getTop10(String limit) throws SQLException {
		String sql = "SELECT ID id,NAME NAME,TITLE TITLE,DETAIL detail,COLLECT_COUNT collectCount,"
					+ " AGREE_COUNT agreeCount, DISCUSS_COUNT discussCount,IMAGE_ADDRESS imageAddress,"
					+ " SHARE_COUNT shareCount,SOURCE source,CLICK_COUNT clickCount"
					+ " FROM info ORDER BY click_count DESC LIMIT ?";
		List<Map<String,Object>> mapList = DbUtil.getMapList(sql, Integer.parseInt(limit));
		return mapList;
	}

	public Map<String, Object> getDetailById(String infoid) throws SQLException {
		String sql = "SELECT ID id,NAME NAME,TITLE TITLE,DETAIL detail,COLLECT_COUNT collectCount,"
				+ " AGREE_COUNT agreeCount, DISCUSS_COUNT discussCount,IMAGE_ADDRESS imageAddress,"
				+ " SHARE_COUNT shareCount,SOURCE source,CLICK_COUNT clickCount"
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
		//{code:sucess,desc:"成功"} 
		map.put("code", "sucess");
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
		map.put("code", "sucess");
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
		map.put("code", "sucess");
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
		map.put("code", "sucess");
		return map;
	}
	
}
