package com.simmya.service.impl;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simmya.constant.BoxStatus;
import com.simmya.exception.SimmyaException;
import com.simmya.mapper.OrderBoxRefMapper;
import com.simmya.pojo.BoxInfoRef;
import com.simmya.pojo.OrderBoxRef;
import com.simmya.service.BaseService;

@Service
public class BoxInfoRefService extends BaseService<BoxInfoRef>{
	
	@Autowired
	private OrderBoxRefMapper orderBoxMapper;

	@Transactional
	public void update(String boxid, String[] infoIdsArr) throws SimmyaException {
		if (infoIdsArr == null) {
			return;
		}
		OrderBoxRef orderBoxRef = new OrderBoxRef();
		orderBoxRef.setBoxId(boxid);
		List<OrderBoxRef> orderBoxRefs = orderBoxMapper.select(orderBoxRef);
		if (orderBoxRefs != null) {
			for (OrderBoxRef obr : orderBoxRefs) {
				if (BoxStatus.NotCompleted.equals(obr.getStatus())) {
					throw new SimmyaException(" -- 有用户仍旧订阅了该盒子中的咨询 -- ");
				}
			}
		}
		BoxInfoRef boxInfoRef = new BoxInfoRef();
		boxInfoRef.setBoxId(boxid);
		super.deleteByWhere(boxInfoRef);
		for (int i = 0; i < infoIdsArr.length; i++) {
			BoxInfoRef bif = new BoxInfoRef();
			bif.setBoxId(boxid);
			bif.setInfoId(infoIdsArr[i]);
			super.save(bif);
		}
	}
	
	
	
	
}
