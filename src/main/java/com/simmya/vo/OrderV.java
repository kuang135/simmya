package com.simmya.vo;

import java.util.List;



public class OrderV {

	private String id;
	private String createTime;
	private String address;
	List<BoxV> boxs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<BoxV> getBoxs() {
		return boxs;
	}

	public void setBoxs(List<BoxV> boxs) {
		this.boxs = boxs;
	}
	
	
	
}
