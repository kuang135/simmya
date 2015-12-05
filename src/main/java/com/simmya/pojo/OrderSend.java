package com.simmya.pojo;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class OrderSend {
	@Id
	@GeneratedValue(generator = "UUID")
	private String id;
	private String orderId;
	private String boxId;
	private Integer count;
	private Boolean received;
	private String discuss;
	
	
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	public Boolean getReceived() {
		return received;
	}
	public void setReceived(Boolean received) {
		this.received = received;
	}
	public String getDiscuss() {
		return discuss;
	}
	public void setDiscuss(String discuss) {
		this.discuss = discuss;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getBoxId() {
		return boxId;
	}
	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}
	
	
}
