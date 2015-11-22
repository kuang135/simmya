package com.simmya.pojo;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrdersCommit {

	//"createTime":"2015-10-10 10:10:10","addressId":"1324215334ee","totalPay":1233,"payBalance":80
	private Date createTime;
	private String addressId;
	private double totalPay;
	private double payBalance;
	private List<BoxCommit>  boxs;
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public double getTotalPay() {
		return totalPay;
	}
	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;
	}
	public double getPayBalance() {
		return payBalance;
	}
	public void setPayBalance(double payBalance) {
		this.payBalance = payBalance;
	}
	public List<BoxCommit> getBoxs() {
		return boxs;
	}
	public void setBoxs(List<BoxCommit> boxs) {
		this.boxs = boxs;
	}
	
	public static void main(String[] aaa){
		String jsons="{\"addressId\":\"1324215334ee\",\"totalPay\":1233,\"payBalance\":80,\"boxs\":[{\"boxid\":\"2143123\",\"orderWay\":\"一周一期\",\"orderCount\":3}]}";
		ObjectMapper mapper = new ObjectMapper();
		try {
			OrdersCommit order=mapper.readValue(jsons, OrdersCommit.class);
			
			for(BoxCommit box:order.getBoxs())
				System.out.println(box.getOrderWay());
				
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
