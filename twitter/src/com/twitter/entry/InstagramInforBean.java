package com.twitter.entry;

public class InstagramInforBean {
	private String caption = "";//发表内容
	private String code = "";//code码
	private String comments = "";//评论数
	private String date = "";//注册时间
	private String display_src = "";//图片
	private String id = "";//状态ID
	private String likes = "";//点赞数
	private String ownerId = "";//用户ID
	private String ownerName="";//用户名称
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getDisplay_src() {
		return display_src;
	}
	public void setDisplay_src(String display_src) {
		this.display_src = display_src;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLikes() {
		return likes;
	}
	public void setLikes(String likes) {
		this.likes = likes;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	@Override
	public String toString() {
		return "InstagramInforBean [caption=" + caption + ", code=" + code + ", comments=" + comments + ", date=" + date
				+ ", display_src=" + display_src + ", id=" + id + ", likes=" + likes + ", ownerId=" + ownerId
				+ ", ownerName=" + ownerName + "]";
	}
	
	
	
	
}
