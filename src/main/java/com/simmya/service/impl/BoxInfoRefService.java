package com.simmya.service.impl;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simmya.pojo.BoxInfoRef;
import com.simmya.service.BaseService;

@Service
public class BoxInfoRefService extends BaseService<BoxInfoRef>{

	@Transactional
	public void update(String boxid, String[] infoIdsArr) {
		if (infoIdsArr == null || infoIdsArr.length == 0) {
			return;
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
