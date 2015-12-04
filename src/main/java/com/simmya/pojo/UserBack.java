package com.simmya.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class UserBack {
	@Id
	@GeneratedValue(generator = "UUID")
    private String id;

    private String userId;

    private String content;

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


    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}