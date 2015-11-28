package com.simmya.controller;


import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simmya.easyui.DataGrid;
import com.simmya.pojo.Orders;
import com.simmya.service.impl.OrdersService;

@Controller
@RequestMapping(value= "/manage/order")
public class ManageOrderController {
	
	
	@Autowired
	private OrdersService orderService;
	
	@RequestMapping(value= "/list", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid list(@RequestParam(value="page")int page,
			@RequestParam(value="rows")int rows,
			@RequestParam(value="status",required = false)String status) {
		Orders orders = new Orders();
		if (StringUtils.isNotBlank(status)) {
			orders.setStatus(status);
		}
		return orderService.getOrderDataGrid(page, rows, orders);
	}
	
	@RequestMapping(value= "/box", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid box(@RequestParam(value="orderid",required = true)String orderid) throws SQLException {
		return orderService.getBoxDataGrid(orderid);
	}
	
}
