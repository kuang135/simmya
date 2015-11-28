package com.simmya.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simmya.easyui.AjaxResult;
import com.simmya.easyui.DataGrid;
import com.simmya.service.impl.PushService;


@Controller
@RequestMapping(value= "/manage/push")
public class ManagePushController {
	
	@Autowired
	private PushService pushService;
	
	@RequestMapping(value= "/list", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid list(@RequestParam(value="page")int page,
			@RequestParam(value="rows")int rows) {
		return pushService.getOrderDataGrid(page, rows);
	}
	
	@RequestMapping(value= "/add", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult add(@RequestParam(value="message",required = true)String message) {
		System.out.println(message);
		AjaxResult ajaxResult;
		try {
			pushService.savePush(message.trim());
			ajaxResult = new AjaxResult(200, "推送成功。");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxResult = new AjaxResult(400, "推送失败。");
		}
		return ajaxResult;
	}
	
}
