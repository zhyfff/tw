package com.twitter.spider;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.common.util.jdbc.Logit;
import com.twitter.download.download;
import com.twitter.entry.FollowsInforBean;
import com.twitter.util.UtilTool;

import http.HttpClientUtil;

/**
 * twitter@mediaa粉丝信息
 * @author Administrator
 *
 */
public class follows {
	
	private static Logit log = Logit.getInstance(follows.class);
	private static HttpClientUtil http = HttpClientUtil.getInstance();
	private static UtilTool utilTool = UtilTool.getInstace();
	private static download dl = download.getInstance();
	private static String min_position=utilTool.getMin_position();//起始信息位置
	private static String has_more_items="true";//是否有下一页
	private static String cookies=utilTool.getCookiesTool();
	public static void main(String[] args) {
		FollowsInforBean f = new FollowsInforBean();
//		getFirstPageFollowsList(f);
	
		while (!has_more_items.equals("false")) {
			try {
				getFollowsList(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			sleep();
		}
		
	}
	
	/**
	 * 解析更多粉丝
	 */
	public static JSONObject getFollowsListHTML(String httpUrl){
		
		HashMap<String,String> headMaps = new HashMap<>();
		headMaps.put("Host", "twitter.com");
		headMaps.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
		headMaps.put("Accept", "application/json, text/javascript, */*; q=0.01");
		headMaps.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		headMaps.put("Accept-Encoding", "gzip, deflate, br");
		headMaps.put("Referer", "https://twitter.com/mediaa/followers");
		headMaps.put("X-Twitter-Active-User", "yes");
		headMaps.put("X-Requested-With", "XMLHttpRequest");
		headMaps.put("Connection", "keep-alive");
//		headMaps.put("Cookie", "_twitter_sess=BAh7DCIKZmxhc2hJQzonQWN0aW9uQ29udHJvbGxlcjo6Rmxhc2g6OkZsYXNo%250ASGFzaHsABjoKQHVzZWR7ADoPY3JlYXRlZF9hdGwrCBAug89nAToMY3NyZl9p%250AZCIlZTA3MTUxNjRmMGRiM2UxZTA3ZDE3M2E4MDRiMTYzMDY6B2lkIiU4MTFi%250AZjgwMmExMzljZDk0Y2M0MGQzN2M2ODk1M2E1NjofbG9naW5fdmVyaWZpY2F0%250AaW9uX3VzZXJfaWRsKwfeEp5zOiJsb2dpbl92ZXJpZmljYXRpb25fcmVxdWVz%250AdF9pZCIrQlNmSllCM0tLNVZnNmNtUFg0N1hnbnZ3RzVicnBsMm9Ic0pNU3Y6%250ACXVzZXJsKwkA8BSg6sDuDg%253D%253D--b4ad2172925107dc02cf89508e80a667bccf8178; ct0=d8d234eabe0da12d128e65fcce515445; lang=en; _ga=GA1.2.1361297271.1545374751; _gid=GA1.2.774265373.1545374751; gt=1076008511803248640; kdt=TNhdwoRzQqqSZwF45uBJAjY8XVP25hAWBU18RwSg; dnt=1; csrf_same_site_set=1; csrf_same_site=1; personalization_id=\"v1_+Rh+5lk/k+KYmSSbFxKgpQ==\"; guest_id=v1%3A154537712870493001; ads_prefs=\"HBISAAA=\"; remember_checked_on=1; twid=\"u=1076009474928734208\"; auth_token=e2a95533e2f2dbe38d65933f735e4d83fc3d0758; external_referer=ZLhHHTiegr%2FqUbefnT%2BrTHolXI%2FpGJl%2Bgfgog7JFqjlmCGbTP08olQ%3D%3D|0|8e8t2xd8A2w%3D; _gat=1");
		headMaps.put("Cookie", cookies);
		headMaps.put("TE", "Trailers");
		String html = http.sendHttpsGet(httpUrl, headMaps, 5);
//		log.info(html);
		JSONObject folist= JSONObject.parseObject(html);
		
		return folist;
	}
	
	/**
	 * 解析第一页粉丝
	 */
	public static String getFirstPageFollowsListHTML(String httpfist){
		HashMap<String,String> headMaps = new HashMap<>();
		headMaps.put("Host", "twitter.com");
		headMaps.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
		headMaps.put("Accept", "application/json, text/javascript, */*; q=0.01");
		headMaps.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		headMaps.put("Accept-Encoding", "gzip, deflate, br");
		headMaps.put("Referer", "https://twitter.com/mediaa/following");
		headMaps.put("X-Push-State-Request", "true");
		headMaps.put("X-Asset-Version", "98d693");
		headMaps.put("X-Twitter-Active-User", "yes");
		headMaps.put("X-Requested-With", "XMLHttpRequest");
		headMaps.put("Connection", "keep-alive");
//		headMaps.put("Cookie", "_twitter_sess=BAh7DCIKZmxhc2hJQzonQWN0aW9uQ29udHJvbGxlcjo6Rmxhc2g6OkZsYXNo%250ASGFzaHsABjoKQHVzZWR7ADoPY3JlYXRlZF9hdGwrCBAug89nAToMY3NyZl9p%250AZCIlZTA3MTUxNjRmMGRiM2UxZTA3ZDE3M2E4MDRiMTYzMDY6B2lkIiU4MTFi%250AZjgwMmExMzljZDk0Y2M0MGQzN2M2ODk1M2E1NjofbG9naW5fdmVyaWZpY2F0%250AaW9uX3VzZXJfaWRsKwfeEp5zOiJsb2dpbl92ZXJpZmljYXRpb25fcmVxdWVz%250AdF9pZCIrQlNmSllCM0tLNVZnNmNtUFg0N1hnbnZ3RzVicnBsMm9Ic0pNU3Y6%250ACXVzZXJsKwkA8BSg6sDuDg%253D%253D--b4ad2172925107dc02cf89508e80a667bccf8178; ct0=d8d234eabe0da12d128e65fcce515445; lang=en; _ga=GA1.2.1361297271.1545374751; _gid=GA1.2.774265373.1545374751; gt=1076008511803248640; kdt=TNhdwoRzQqqSZwF45uBJAjY8XVP25hAWBU18RwSg; dnt=1; csrf_same_site_set=1; csrf_same_site=1; personalization_id=\"v1_+Rh+5lk/k+KYmSSbFxKgpQ==\"; guest_id=v1%3A154537712870493001; ads_prefs=\"HBISAAA=\"; remember_checked_on=1; twid=\"u=1076009474928734208\"; auth_token=e2a95533e2f2dbe38d65933f735e4d83fc3d0758; external_referer=ZLhHHTiegr%2FqUbefnT%2BrTHolXI%2FpGJl%2Bgfgog7JFqjlmCGbTP08olQ%3D%3D|0|8e8t2xd8A2w%3D; _gat=1");
		headMaps.put("Cookie", cookies);
		headMaps.put("TE", "Trailers");
		String html = http.sendHttpsGet(httpfist, headMaps, 5);
//		log.info(html);
		JSONObject folist= JSONObject.parseObject(html);
		if (folist.containsKey("page")) {
			return folist.getString("page");
		}
		return null;
	}
	
	/**
	 * 修改翻页状态信息
	 * @param folist
	 */
	public static void getPositionAndmore(JSONObject folist){
		
		if (folist.containsKey("min_position")) {
			min_position=folist.getString("min_position").trim();
			log.info(">>>>>>>>>>>>>>>min_position:"+min_position+"<<<<<<<<<<<<<<<<<<<<<<");
		}
		if (folist.containsKey("has_more_items")) {
			has_more_items=folist.getString("has_more_items").trim();
		}
	} 
	
	
	/**
	 * 第一页粉丝
	 * @param f
	 */
	public static void getFirstPageFollowsList(FollowsInforBean f){
		String httpfist = "https://twitter.com/mediaa/followers";
		String first_html=getFirstPageFollowsListHTML(httpfist);
		Document doc = Jsoup.parse(first_html);
//		log.info(doc);
		Elements ele = doc.select("[class=Grid Grid--withGutter]").select("[class~=Grid-cell]");
		for (Element element : ele) {
			Elements e=element.select("[id~=stream-item-user]").select("[class~=ProfileCard]").select("[class=ProfileCard-content]");
			f.setDataId(element.select("[id~=stream-item-user]").select("[class~=ProfileCard]").attr("data-user-id").trim());
			f.setUserLink("https://twitter.com"+e.select("a").attr("href").trim().replaceAll("'", "’"));
			f.setAvatarImage(e.select("a").select("img").attr("src").trim().replaceAll("'", "’"));
			f.setUserName(filterEmoji(filterEmoji(e.select("[class=ProfileCard-userFields]").select("[class=ProfileNameTruncated account-group]").select("[class=u-textTruncate u-inlineBlock]").select("[class~=fullname]").text().trim().replaceAll("'", "’"))));
			f.setUserId(e.select("[class=ProfileCard-userFields]").select("[class=ProfileCard-screenname]").text().trim().replaceAll("'", "’").replace(" ‏", ""));
			f.setProfileCard(filterEmoji(filterEmoji(e.select("[class=ProfileCard-userFields]").select("p").text().trim().replaceAll("'", "’"))));
			log.info(f);
			dl.SaveFollowsInfor(f);
			log.info(">>>>>>>>>>>>>>>>>>>>>>");
		}

	}
	
	/**
	 * 更多粉丝
	 * @param f
	 * @return 
	 * @throws Exception 
	 */
	public static String getFollowsList(FollowsInforBean f) throws Exception{
		String httpUrl="https://twitter.com/mediaa/followers/users?include_available_features=1&include_entities=1&max_position="+min_position+"&reset_error_state=false";
		JSONObject folist=getFollowsListHTML(httpUrl);
		String message=null;
		if (folist.containsKey("message")) {
			message=folist.getString("folist").trim();
			if (message.equals("对不起，你受频率限制了。")) {
				log.error("对不起，你受频率限制了。");
				Thread.sleep(1000*20);
				return getFollowsList(f);
			}else {
				String items_html=null;
				if (folist.containsKey("items_html")) {
					items_html=folist.getString("items_html");
				}
				Document doc = Jsoup.parse(items_html);
//				log.info(doc);
				Elements ele = doc.select("[class=Grid Grid--withGutter]").select("[class~=Grid-cell]");
				for (Element element : ele) {
					Elements e=element.select("[id~=stream-item-user]").select("[class~=ProfileCard]").select("[class=ProfileCard-content]");
					f.setDataId(element.select("[id~=stream-item-user]").select("[class~=ProfileCard]").attr("data-user-id").trim());
					f.setUserLink("https://twitter.com"+e.select("a").attr("href").trim().replaceAll("'", "’"));
					f.setAvatarImage(e.select("a").select("img").attr("src").trim().replaceAll("'", "’"));
					f.setUserName(filterEmoji(e.select("[class=ProfileCard-userFields]").select("[class=ProfileNameTruncated account-group]").select("[class=u-textTruncate u-inlineBlock]").select("[class~=fullname]").text().trim().replaceAll("'", "’")));
					f.setUserId(e.select("[class=ProfileCard-userFields]").select("[class=ProfileCard-screenname]").text().trim().replaceAll("'", "’").replace(" ‏", ""));
					f.setProfileCard(filterEmoji(e.select("[class=ProfileCard-userFields]").select("p").text().trim().replaceAll("'", "’")));
					log.info(f);
					dl.SaveFollowsInfor(f);
					log.info(">>>>>>>>>>>>>>>>>>>>>>");
				}
				getPositionAndmore(folist);
			}
		}
		return null;
		
	}
	
	public static void sleep(){
		try {

			int[] inpool = { 1000, 2000, 3000 ,4000,5000};
			int s=inpool[(int) (Math.random() * inpool.length)];
			Thread.sleep(s);
			log.info("休眠："+s/1000+"秒");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean containsEmoji(String source) {
		if (StringUtils.isBlank(source)) {
			return false;
		}

		int len = source.length();

		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);

			if (isEmojiCharacter(codePoint)) {
				// do nothing，判断到了这里表明，确认有表情字符
				return true;
			}
		}

		return false;
	}

	private static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * 
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {

		if (!containsEmoji(source)) {
			return source;// 如果不包含，直接返回
		}
		// 到这里铁定包含
		StringBuilder buf = null;
		int len = source.length();

		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);

			if (isEmojiCharacter(codePoint)) {
				if (buf == null) {
					buf = new StringBuilder(source.length());
				}

				buf.append(codePoint);
			} else {
			}
		}

		if (buf == null) {
			return source;// 如果没有找到 emoji表情，则返回源字符串
		} else {
			if (buf.length() == len) {// 这里的意义在于尽可能少的toString，因为会重新生成字符串
				buf = null;
				return source;
			} else {
				return buf.toString();
			}
		}
	}
}
