package com.twitter.download;

import javax.sql.DataSource;

import org.apache.commons.httpclient.methods.InputStreamRequestEntity;

import com.common.util.jdbc.JdbcAdapter;
import com.common.util.jdbc.MySQLBConnectionPoolForLocalHost_result;
import com.twitter.entry.FollowsInforBean;
import com.twitter.entry.InstagramInforBean;
import com.twitter.entry.TweetsWithRepliesInforBean;

public class download {
	
	private static download instance=null;
	public static download getInstance() {
		if (instance==null) {
			instance = new download();
		}
		return instance;
	}
	

	/*
	 * 连接数据库
	 */
	static DataSource ds = MySQLBConnectionPoolForLocalHost_result.dataSource;// 连接本地数据库
	static JdbcAdapter adapter = new JdbcAdapter();
	static {
		adapter.init(ds);// JDBC适配器初始化
	}
	
	public boolean SaveFollowsInfor(FollowsInforBean f){
		String sql="insert into followers (dataId,userLink,avatarImage,"
				+ "userName,userId,ProfileCard,insert_date) values('" + f.getDataId() + "','" + f.getUserLink() + "','"
				+ f.getAvatarImage() + "','" + f.getUserName() + "','" + f.getUserId() + "','" + f.getProfileCard()
				+ "',now())";
		
		return adapter.dbInsert(sql);
		
	}
	
	
	public boolean SaveTweetsInfor(TweetsWithRepliesInforBean twrib){
		String sql="insert into tweets (dataId,userLink,avatarImage,userName,"
				+ "userId,time,tweetText,img,card2Url,insert_date) values ('" + twrib.getDataId() + "','" + twrib.getUserLink()
				+ "','" + twrib.getAvatarImage() + "','" + twrib.getUserName() + "','" + twrib.getUserId() + "','"
				+ twrib.getTime() + "','" + twrib.getTweetText() + "','" + twrib.getImg() + "','" + twrib.getCard2Url()
				+ "',now())";
		return adapter.dbInsert(sql);
		
	}
	
	public boolean SaveInstagram(InstagramInforBean ins){
		String sql="insert into instagram_gra (caption,code,comments,date,"
				+ "display_src,id,likes,ownerId,ownerName,insert_date) values('"+ins.getCaption()+"','"+ins.getCode()
				+"','"+ins.getComments()+"','"+ins.getDate()+"','"+ins.getDisplay_src()+"','"+ins.getId()+"','"+
				ins.getLikes()+"','"+ins.getOwnerId()+"','"+ins.getOwnerName()+"',now());";
		return adapter.dbInsert(sql);
		
	}

}
