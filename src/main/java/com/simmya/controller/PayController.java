package com.simmya.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simmya.alipay.config.AlipayConfig;
import com.simmya.alipay.sign.RSA;
import com.simmya.alipay.util.AlipayCore;
import com.simmya.alipay.util.AlipayNotify;
import com.simmya.constant.OrderStatus;
import com.simmya.pojo.Orders;
import com.simmya.pojo.OrdersCommit;
import com.simmya.pojo.User;
import com.simmya.service.impl.OrdersService;
import com.simmya.service.impl.PayService;
import com.simmya.service.impl.UserService;
import com.simmya.vo.OrderV;



@Controller
public class PayController {

	@Autowired
	private UserService userService;
	@Autowired
	private PayService payService;
	@Autowired
	private OrdersService ordersService;
	
	/*
	 * 提交订单
	 */
	@RequestMapping(value= "/boxs/order", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> backOrderAdd(@RequestHeader(value = "token",required = true)String token,
			@RequestParam(value = "orders", required = true)String jsons) throws SQLException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isBlank(token)) {
			map.put("code", "error");
			map.put("desc", null);
			return map;
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			map.put("code", "error");
			map.put("desc", null);
		}
		map.put("code", "error");
		map.put("desc", null);
		ObjectMapper mapper = new ObjectMapper();
		try {
			OrdersCommit orders=mapper.readValue(jsons, OrdersCommit.class);
			Orders orderx=payService.saveOrderRef(orders,loginUser);
			Map<String, String> sParaTemp = new HashMap<String, String>(); 
			sParaTemp.put("_input_charset", "\"utf-8\"");
			sParaTemp.put("it_b_pay", "\"30m\""); 
			sParaTemp.put("notify_url", "\"http://115.159.42.41:6430/boxs/backZFB.do\""); 
			sParaTemp.put("out_trade_no", "\""+orderx.getId()+"\""); 
			sParaTemp.put("partner", "\"2088511405850083\""); 
			sParaTemp.put("payment_type", "\"1\""); 
			//sParaTemp.put("return_url", "http://115.159.42.41:6430/boxs/returnZFB.do"); 
			sParaTemp.put("seller_id", "\"nuanuan1210@qq.com\""); 
			sParaTemp.put("service", "\"mobile.securitypay.pay\""); 
			sParaTemp.put("subject", "\"盒子购买\""); 
			sParaTemp.put("total_fee", "\""+String.valueOf(orderx.getTotalPrice())+"\"");

			String aliStr=AlipayCore.createLinkString(AlipayCore.paraFilter(sParaTemp));
			String rsaStr=RSA.sign(aliStr, AlipayConfig.private_key, AlipayConfig.input_charset);
			String sb=aliStr+"&sign=\""+URLEncoder.encode(rsaStr)+"\"&sign_type=\"RSA\"";
			map.put("code", "success");
			map.put("desc", sb.toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return map;
	}
	
//	@RequestMapping(value= "/boxs/returnZFB", method = RequestMethod.GET)
//	@ResponseBody
//	public Map<String, Object> returnZFB() throws SQLException {
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("code", "success");
//		return map;
//	}
	
	
	@RequestMapping(value= "/boxs/backZFB", method = RequestMethod.POST)
	@ResponseBody
	public String backZFB(HttpServletRequest request) throws SQLException, UnsupportedEncodingException {
		System.out.println("进入回调");
		String returnString="error";
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号

		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//支付宝交易号

		//String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		//String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		if(AlipayNotify.verify(params)){//验证成功
			System.out.println("验证成功");
			Orders order=ordersService.selectByPrimaryKey(out_trade_no);
			if(order!=null){
				System.out.println("数据操作");
				order.setStatus(OrderStatus.Payed);
				ordersService.update(order);
				returnString = "success";
			}
		}
		System.out.println("结束验证");
		return returnString;
	}
	
	@RequestMapping(value= "/user/ordersNoPay", method = RequestMethod.GET)
	@ResponseBody
	public List<OrderV> listOrders(@RequestHeader(value = "token",required = true)String token) throws SQLException {
		if (StringUtils.isBlank(token)) {
			return null; 
		}
		User loginUser = userService.checkLogin(token);
		if (loginUser == null) {
			return null;
		}
		return payService.getOrderListNoPay(loginUser);
	}
	
	
}