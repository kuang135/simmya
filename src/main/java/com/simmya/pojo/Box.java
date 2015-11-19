package com.simmya.pojo;

import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Box {
	@Id
	@GeneratedValue(generator = "UUID")
    private String id;

    private String name;

    private String title;

    private String detail;

    private String imageAddress;

    private BigDecimal boxPrice;

    private Integer collectCount;

    private Integer shareCount;
    
    private Integer discussCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress == null ? null : imageAddress.trim();
    }

    public BigDecimal getBoxPrice() {
        return boxPrice;
    }

    public void setBoxPrice(BigDecimal boxPrice) {
        this.boxPrice = boxPrice;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

	public Integer getDiscussCount() {
		return discussCount;
	}

	public void setDiscussCount(Integer discussCount) {
		this.discussCount = discussCount;
	}
}