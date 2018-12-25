package com.twitter.entry;

public class FollowsInforBean {
	private String dataId;
	private String userLink;
	private String avatarImage;
	private String userName;
	private String userId;
	private String ProfileCard;
	
	
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getUserLink() {
		return userLink;
	}
	public void setUserLink(String userLink) {
		this.userLink = userLink;
	}
	public String getAvatarImage() {
		return avatarImage;
	}
	public void setAvatarImage(String avatarImage) {
		this.avatarImage = avatarImage;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getProfileCard() {
		return ProfileCard;
	}
	public void setProfileCard(String profileCard) {
		ProfileCard = profileCard;
	}
	@Override
	public String toString() {
		return "FollowsInforBean [dataId=" + dataId + ", userLink=" + userLink + ", avatarImage=" + avatarImage
				+ ", userName=" + userName + ", userId=" + userId + ", ProfileCard=" + ProfileCard + "]";
	}
	
	
	
	

}
