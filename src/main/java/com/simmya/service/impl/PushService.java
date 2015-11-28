package com.simmya.service.impl;






import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.simmya.constant.PushStatus;
import com.simmya.easyui.DataGrid;
import com.simmya.exception.SimmyaException;
import com.simmya.pojo.Push;
import com.simmya.service.BaseService;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;

@Service
public class PushService extends BaseService<Push>{
	
	protected static final Logger LOG = LoggerFactory.getLogger(PushService.class);
	private static final String appKey ="24af53b5ec9a37d0b4cb1ca4";
	private static final String masterSecret = "76ca1ca0a41e239336533b4f";
	
	public DataGrid getOrderDataGrid(int page, int rows) {
		PageInfo<Push> pageInfo= super.selectPage(page, rows, "CREATE_TIME DESC");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Push push : pageInfo.getList()) {
			push.setCreateTimeShow(sf.format(push.getCreateTime()));
		}
		DataGrid datagrid=new DataGrid();
    	datagrid.setTotal((int)pageInfo.getTotal());
    	datagrid.setRows(pageInfo.getList());
        return datagrid;
	}

	public void savePush(String message) throws SimmyaException {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
        PushPayload payload = PushPayload.alertAll(message);
        Push push = new Push();
        try {
            PushResult result = jpushClient.sendPush(payload);
            push.setStatus(PushStatus.SUCCESS);
            LOG.info("Got result - " + result);
        } catch (APIConnectionException e) {
        	push.setStatus(PushStatus.FAIL);
            LOG.error("Connection error. Should retry later. ", e);
            throw new SimmyaException("推送失败 ");
        } catch (APIRequestException e) {
        	push.setStatus(PushStatus.FAIL);
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
            throw new SimmyaException("推送失败 ");
        }
        push.setMessage(message);
        push.setCreateTime(new Date());
        super.save(push);
	}
	
	
}
