package com.simmya.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

public class Info {
	@Id
	@GeneratedValue(generator = "UUID")
    private String id;

    private String name;

    private String title;

    private String detail;

    private Integer collectCount;

    private Integer shareCount;

    private Integer agreeCount;

    private String imageAddress;

    private String source;

    private Integer clickCount;

    private Integer discussCount;
    
    @Transient
    private boolean selected;

    
    public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

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

    public Integer getCollectCount() {
        return collectCount==null?0:collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getShareCount() {
        return shareCount==null?0:shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getAgreeCount() {
        return agreeCount==null?0:agreeCount;
    }

    public void setAgreeCount(Integer agreeCount) {
        this.agreeCount = agreeCount;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress == null ? null : imageAddress.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public Integer getClickCount() {
        return clickCount==null?0:clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public Integer getDiscussCount() {
        return discussCount==null?0:discussCount;
    }

    public void setDiscussCount(Integer discussCount) {
        this.discussCount = discussCount;
    }
}