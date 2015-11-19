package com.simmya.pojo;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Codec {
	@Id
	@GeneratedValue(generator = "UUID")
    private String id;

    private String veriCode;

    private Date expiredTime;

    private String phone;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

	public String getVeriCode() {
		return veriCode;
	}

	public void setVeriCode(String veriCode) {
		this.veriCode = veriCode;
	}



	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

  
}