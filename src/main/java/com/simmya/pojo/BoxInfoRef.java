package com.simmya.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class BoxInfoRef {
	@Id
	@GeneratedValue(generator = "UUID")
    private String id;

    private String infoId;

    private String boxId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId == null ? null : boxId.trim();
    }
}