package com.simmya.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class BoxCollection {
	@Id
	@GeneratedValue(generator = "UUID")
    private String id;

    private String userId;

    private String boxId;

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

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId == null ? null : boxId.trim();
    }
}