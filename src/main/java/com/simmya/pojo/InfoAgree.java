package com.simmya.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class InfoAgree {
	@Id
	@GeneratedValue(generator = "UUID")
    private String id;

    private String userId;

    private String infoId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId == null ? null : infoId.trim();
    }
}