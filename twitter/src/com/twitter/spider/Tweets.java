package com.twitter.spider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.common.util.jdbc.Logit;
import com.twitter.download.download;
import com.twitter.entry.TweetsWithRepliesInforBean;
import com.twitter.util.UtilTool;

import http.HttpClientUtil;

/**
 * twitter@mediaa博文信息
 * @author Administrator
 *
 */
public class Tweets {
	private static Logit log = Logit.getInstance(Tweets.class);
	private static HttpClientUtil http = HttpClientUtil.getInstance();
	private static UtilTool utilTool = UtilTool.getInstace();
	private static download dl = download.getInstance();
	private static String cookies=utilTool.getCookiesTool();
	private static String min_position=utilTool.getTweetsMin_position();
	private static String has_more_items="true";
	public static void main(String[] args) {
		getFirstTweetsWithReplies(new TweetsWithRepliesInforBean());
		while (!has_more_items.equals("false")) {
			try {
				getMoreTweetsWithReplies(new TweetsWithRepliesInforBean());
			} catch (Exception e) {
				e.printStackTrace();
			}
			sleep();
		}
		
		
//		System.out.println(stampToDate(1538560035*1000l));
	}
	
	/**
	 *
	 *解析第一页推文
	 */
	public static String getTweetsWithRepliesFirstHtml(String httpUrl){
		HashMap<String, String> headMaps = new HashMap<>();
		headMaps.put("Host", "twitter.com");
		headMaps.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
		headMaps.put("Accept", "application/json, text/javascript, */*; q=0.01");
		headMaps.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		headMaps.put("Accept-Encoding", "gzip, deflate, br");
		headMaps.put("Referer", "https://twitter.com/mediaa");
		headMaps.put("X-Push-State-Request", "true");
		headMaps.put("X-Asset-Version", "98d693");
		headMaps.put("X-Twitter-Active-User", "yes");
		headMaps.put("X-Requested-With", "XMLHttpRequest");
		headMaps.put("Connection", "keep-alive");
		headMaps.put("Cookie", cookies);
		String html=http.sendHttpsGet(httpUrl, headMaps, 5);
		return html;
	}
	
	/**
	 * 解析更多推文
	 */
	public static String getTweetsWithRepliesMoreHtml(String httpUrl){
		HashMap<String, String> headMaps = new HashMap<>();
		headMaps.put("Host", "twitter.com");
		headMaps.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
		headMaps.put("Accept", "application/json, text/javascript, */*; q=0.01");
		headMaps.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		headMaps.put("Accept-Encoding", "gzip, deflate, br");
		headMaps.put("Referer", "https://twitter.com/mediaa");
		headMaps.put("X-Push-State-Request", "true");
		headMaps.put("X-Asset-Version", "98d693");
		headMaps.put("X-Twitter-Active-User", "yes");
		headMaps.put("X-Requested-With", "XMLHttpRequest");
		headMaps.put("Connection", "keep-alive");
		headMaps.put("Cookie", cookies);
		String html=http.sendHttpsGet(httpUrl, headMaps, 5);
//		log.info(html);
		return html;
	}
	
	/**
	 * 获取第一页推文信息
	 * @return
	 */
	public static String getFirstTweetsWithReplies(TweetsWithRepliesInforBean twrib){
		String httpUrl="https://twitter.com/mediaa/with_replies";
		String html=getTweetsWithRepliesFirstHtml(httpUrl);
		JSONObject twr = JSONObject.parseObject(html);
		if (twr.containsKey("page")) {
			String pageHtml=twr.getString("page");
			Document doc = Jsoup.parse(pageHtml);
			Elements ele = doc.select("[class=stream]").select("[id=stream-items-id]").select("li[id~=stream-item-tweet]");
//			log.info(ele);
			for (Element element : ele) {
				Elements tw = element.select("div[class~=tweet]").select("[class=content]");
				Elements hd = tw.select("[class=stream-item-header]").select("a[class~=account-group]");
				twrib.setDataId(hd.attr("data-user-id").trim());
				twrib.setUserLink("https://twitter.com"+hd.attr("href").trim());
				twrib.setAvatarImage(hd.select("img").attr("src").trim());
				twrib.setUserName(filterEmoji(hd.select("[class=FullNameGroup]").text().replace("认证账号", "").replaceAll("‏ ", "").replaceAll("'", "’")).trim());
				twrib.setUserId(hd.select("[class~=username]").text().trim());
				twrib.setTime(stampToDate(Long.parseLong(tw.select("[class=stream-item-header]").select("[class=time]").select("a").select("span").attr("data-time").trim())*1000l));
				twrib.setTweetText(filterEmoji(tw.select("[class=js-tweet-text-container]").text().replaceAll("'", "’")).trim());				
				String card2url=tw.select("[class~=card2]").select("[class~=js-macaw-cards-iframe-container]").attr("data-src").trim();
				if (!card2url.equals("")) {
					twrib.setCard2Url("https://twitter.com"+card2url);	
				}else {
					twrib.setCard2Url("");
				}				
				twrib.setImg(tw.select("[class=AdaptiveMediaOuterContainer]").select("img").attr("src").trim());
				log.info(twrib);
				dl.SaveTweetsInfor(twrib);
			}
		}
		return null;
	}
	
	/**
	 * 获取更多推文信息
	 * @param twrib
	 * @return
	 * @throws Exception 
	 */
	public static String  getMoreTweetsWithReplies(TweetsWithRepliesInforBean twrib) throws Exception{
		String httpUrl="https://twitter.com/i/profiles/show/mediaa/timeline/with_replies?include_available_features=1&include_entities=1&max_position="+min_position+"&reset_error_state=false";
		String html=getTweetsWithRepliesMoreHtml(httpUrl);
		JSONObject twr = JSONObject.parseObject(html);
		if (html.contains("对不起，你受频率限制了。")) {
			log.info("对不起，你受频率限制了。");
			Thread.sleep(1000*20);
		}else {
			if (twr.containsKey("items_html")) {
				String items_html=twr.getString("items_html");
				Document doc = Jsoup.parse(items_html);
				Elements ele = doc.select("li[id~=stream-item-tweet]");
//				log.info(ele);
				for (Element element : ele) {
					Elements tw = element.select("div[class~=tweet]").select("[class=content]");
					Elements hd = tw.select("[class=stream-item-header]").select("a[class~=account-group]");
					twrib.setDataId(hd.attr("data-user-id").trim());
					twrib.setUserLink("https://twitter.com"+hd.attr("href").trim());
					twrib.setAvatarImage(hd.select("img").attr("src").trim());
					twrib.setUserName(filterEmoji(hd.select("[class=FullNameGroup]").text().replace("认证账号", "").replaceAll("‏ ", "").replaceAll("'", "’")).trim());
					twrib.setUserId(hd.select("[class~=username]").text().trim());
					try {
						twrib.setTime(stampToDate(Long.parseLong(tw.select("[class=stream-item-header]").select("[class=time]").select("a").select("span").attr("data-time").trim())*1000l));
					} catch (NumberFormatException e) {
						log.error(e.getMessage());
					}
					twrib.setTweetText(filterEmoji(tw.select("[class=js-tweet-text-container]").text().replaceAll("'", "’")).trim());
					String card2url=tw.select("[class~=card2]").select("[class~=js-macaw-cards-iframe-container]").attr("data-src").trim();
					if (!card2url.equals("")) {
						twrib.setCard2Url("https://twitter.com"+card2url);	
					}else {
						twrib.setCard2Url("");
					}
					twrib.setImg(tw.select("[class=AdaptiveMediaOuterContainer]").select("img").attr("src").trim());
					log.info(twrib);
					dl.SaveTweetsInfor(twrib);
				}
			}
			getPositionAndmore(html);
		}
		
		return null;
	}
	/**
	 * 修改翻页状态信息
	 * @param html
	 */
	public static void getPositionAndmore(String html){
		JSONObject twr = JSONObject.parseObject(html);
		if (twr.containsKey("min_position")) {
			min_position=twr.getString("min_position").trim();
			log.info(">>>>>>>>>>>>>>>min_position:"+min_position+"<<<<<<<<<<<<<<<<<<<<<<");
		}
		if (twr.containsKey("has_more_items")) {
			has_more_items=twr.getString("has_more_items").trim();
		}
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
	
	 /* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(long l){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(l);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
