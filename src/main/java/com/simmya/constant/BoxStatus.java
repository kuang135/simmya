package com.simmya.constant;

public enum BoxStatus {
	
	NoPay("待付款"),
	Payed("已付款"),
	Back("退订");
	
	private String status;
	private BoxStatus(String status){
		this.status = status;
	}
	public String toString() {
		return status;
	}
	
}
