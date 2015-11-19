package com.simmya.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Carts {
	@Id
	@GeneratedValue(generator = "UUID")
    private String id;

    private String userId;

    private String boxIds;

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

    public String getBoxIds() {
        return boxIds;
    }

    public void setBoxIds(String boxIds) {
        this.boxIds = boxIds == null ? null : boxIds.trim();
    }
}