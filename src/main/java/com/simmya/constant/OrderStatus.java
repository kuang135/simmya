package com.simmya.constant;

/*
 * “未付款”，“已付款”，“已完成”，“已退订
 */
public class OrderStatus {
	
	public static final String NotPayed = "未付款";
	public static final String Payed = "已付款";
	public static final String Sended = "待收货";//已发货，未收到
	public static final String Received = "待评价";//已收货，未评价
	public static final String Evaluated = "已评价";
	public static final String Completed = "已完成";//全部发送完
	public static final String Back = "已退订";
	
}
