package com.twitter.entry;

public class TweetsWithRepliesInforBean {
	private String dataId;
	private String userLink;
	private String avatarImage;
	private String userName;
	private String userId;
	private String time;
	private String tweetText;
	private String img;
	private String card2Url;
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTweetText() {
		return tweetText;
	}
	public void setTweetText(String tweetText) {
		this.tweetText = tweetText;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getCard2Url() {
		return card2Url;
	}
	public void setCard2Url(String card2Url) {
		this.card2Url = card2Url;
	}
	@Override
	public String toString() {
		return "TweetsWithRepliesInforBean [dataId=" + dataId + ", userLink=" + userLink + ", avatarImage="
				+ avatarImage + ", userName=" + userName + ", userId=" + userId + ", time=" + time + ", tweetText="
				+ tweetText + ", img=" + img + ", card2Url=" + card2Url + "]";
	}
	
	
}
