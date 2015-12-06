package com.simmya.pojo;

public class BoxCommit {
//"boxid":"2143123","orderWay"="一周一期/半年一期","orderCount"=3
	private String boxid;
	private String orderWay;
	private int orderCount;
	private double payBalance;
	
	public String getBoxid() {
		return boxid;
	}
	public void setBoxid(String boxid) {
		this.boxid = boxid;
	}
	public String getOrderWay() {
		return orderWay;
	}
	public void setOrderWay(String orderWay) {
		this.orderWay = orderWay;
	}
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	public double getPayBalance() {
		return payBalance;
	}
	public void setPayBalance(double payBalance) {
		this.payBalance = payBalance;
	}
	
	
}
