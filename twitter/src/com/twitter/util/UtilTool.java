package com.twitter.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.twitter.entry.IpBean;

public class UtilTool {
	private String cookies;
	private  String min_position;//起始信息位置
	private static UtilTool instance=null;
	public static UtilTool getInstace(){
		if (instance==null) {
			instance = new UtilTool();
		}
		return instance;
	}
	private static Properties prop = new Properties();
	private static InputStream is = null;
	private static String configPath="./";
	private static String propertyFileName = "configuration.properties";
	private static File file = new File( configPath + "/" + propertyFileName  );
	
	/**
	 * 粉丝钩子
	 * @return
	 */
	public String getMin_position(){
		try {
			is = new FileInputStream( file );
			prop.load(is);
			min_position = prop.getProperty("min_position");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return min_position;
	}
	
	/**
	 * 推文钩子
	 * @return
	 */
	public String getTweetsMin_position(){
		try {
			is = new FileInputStream( file );
			prop.load(is);
			min_position = prop.getProperty("Tweetsmin_position");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return min_position;
	}
	
	/**
	 * 获取使用的cookies
	 * @return
	 */
	public String getCookiesTool(){
	
		try {
			is = new FileInputStream( file );
			prop.load(is);
			cookies = prop.getProperty("cookies");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cookies;
	}
	
	
	/**
	 * 获取代理ip
	 * @return
	 */
	public IpBean getIpTool(IpBean ipBean){
		try {
			is = new FileInputStream( file );
			prop.load(is);
			ipBean.setIpaddress( prop.getProperty("ipaddress"));
			ipBean.setIport(Integer.parseInt(prop.getProperty("iport")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ipBean;
	}
	
}
