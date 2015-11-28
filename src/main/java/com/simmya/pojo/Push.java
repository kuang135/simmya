package com.simmya.pojo;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;



public class Push {
	@Id
	@GeneratedValue(generator = "UUID")
    private String id;
	private String status;
	@Transient
	private String createTimeShow;
    private String message;
    private Date createTime;

    public String getId() {
        return id;
    }

    public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getCreateTimeShow() {
		return createTimeShow;
	}


	public void setCreateTimeShow(String createTimeShow) {
		this.createTimeShow = createTimeShow;
	}


	public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }



    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    

}