package com.instagram.spider;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.util.jdbc.Logit;
import com.twitter.download.download;
import com.twitter.entry.InstagramInforBean;
import com.twitter.util.UtilTool;
import http.HttpClientUtil;


/**
 * hizb.ut.tahrir.info
 * instagram帖子信息
 * @author Administrator
 */
public class graphql {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logit log = Logit.getInstance(graphql.class);
	private static HttpClientUtil http = HttpClientUtil.getInstance();
	private static UtilTool utilTool = UtilTool.getInstace();
	private static download dl = download.getInstance();
	private static String after="QVFCRDRmU2RnYTJaRjF2VDBJUWtDenUxVVZDTUYxRlRhdFVHUGZrZmt3M19sLW5sWGFacUtyTHRoQ0QzRXFPVWhSb1F1Mm1BVk9yQ1I2OW5RcjZ4NGFIRw==";
	private static String cookies;
	private static String has_next_page="true";
	public static void main(String[] args) {
		
		while (!has_next_page.equals("false")) {
			try {
				getInfor(new JSONObject(), new InstagramInforBean());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 解析帖子
	 */
	public static String getHtml(String httpUrl){
		HashMap<String, String> headMaps = new HashMap<>();
		headMaps.put("Host", "www.instagram.com");
		headMaps.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
		headMaps.put("Accept", "*/*");
		headMaps.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		headMaps.put("Accept-Encoding", "gzip, deflate, br");
		headMaps.put("Referer", "https://www.instagram.com/hizb.ut.tahrir.info/");
		headMaps.put("X-Instagram-GIS", "02628f110b98a2a740eaf36c7c298106");
		headMaps.put("X-Requested-With", "XMLHttpRequest");
		headMaps.put("Connection", "keep-alive");
		headMaps.put("Cookie", "mid=W3Yw6gAEAAFoBTCgoO0Q-L1VF841; mcd=3; ig_cb=1; csrftoken=vI9KRyIxTDQlhaElzfNpAjtFe0RsQHkJ; sessionid=5045223140%3ArGVnBd0UEQPB01%3A20; rur=FTW; ds_user_id=5045223140; urlgen=\"{\"65.49.68.183\": 6939}:1gbcQn:s10R0DyF2ujshHJ1XocvoYS8QrM\"; csrftoken=vI9KRyIxTDQlhaElzfNpAjtFe0RsQHkJ");
		headMaps.put("TE", "Trailers");
	
		String html=http.sendHttpsGet(httpUrl, headMaps, 5);
		return html;
	}
	
	/**
	 * 获取帖子数据
	 * @param variables
	 * @param insta
	 * @throws Throwable
	 */
	public static void getInfor(JSONObject variables,InstagramInforBean insta) throws Throwable{
		variables.put("id", "8669875419");
		variables.put("first", "12");
		variables.put("after", after);
		String httpUrl="https://www.instagram.com/graphql/query/?query_hash=66eb9403e44cc12e5b5ecda48b667d41&variables="+URLEncoder.encode(variables.toString(), "utf-8");
//		log.info(httpUrl);
		String html=getHtml(httpUrl);
//		log.info(html);
		JSONObject ht = JSONObject.parseObject(html);
		if (ht.containsKey("data")) {
			JSONObject data = ht.getJSONObject("data");
			if (data.containsKey("user")) {
				JSONObject user=data.getJSONObject("user");
				if (user.containsKey("edge_owner_to_timeline_media")) {
					JSONObject edge_owner = user.getJSONObject("edge_owner_to_timeline_media");
					if (edge_owner.containsKey("edges")) {
						JSONArray edges = edge_owner.getJSONArray("edges");
						for (int i = 0; i < edges.size(); i++) {
							JSONObject edg = edges.getJSONObject(i);
							if (edg.containsKey("node")) {
								JSONObject node = edg.getJSONObject("node");
								if (node.containsKey("edge_media_to_caption")) {
									JSONObject edge_media_to_caption = node.getJSONObject("edge_media_to_caption");
									if (edge_media_to_caption.containsKey("edges")) {
										JSONArray caption_edges=edge_media_to_caption.getJSONArray("edges");
										for (int j = 0; j < caption_edges.size(); j++) {
											JSONObject cap_edge = caption_edges.getJSONObject(j);
											if (cap_edge.containsKey("node")) {
												JSONObject cap_node = cap_edge.getJSONObject("node");
												if (cap_node.containsKey("text")) {
													insta.setCaption(cap_node.getString("text").replace("\"", "\\\"")
															.replace("'", "&acute;").replace("\n", "")
															.replace("\r", "").trim());
												}
											}
										}
									}
									
								}
								
								if (node.containsKey("shortcode")) {
									insta.setCode(node.getString("shortcode").trim());
								}
								if (node.containsKey("edge_media_to_comment")) {
									JSONObject edge_media_to_comment = node.getJSONObject("edge_media_to_comment");
									if (edge_media_to_comment.containsKey("count")) {
										insta.setComments(edge_media_to_comment.getString("count").trim());
									}
								}
								if (node.containsKey("taken_at_timestamp")) {
									String date = node.getString("taken_at_timestamp");
									Date dd = new Date(Long.valueOf(date)*1000);
									insta.setDate(sdf.format(dd));
								}
								
								if (node.containsKey("display_url")) {
									insta.setDisplay_src(node.getString("display_url").trim());
								}
								
								if (node.containsKey("id")) {
									insta.setId(node.getString("id").trim());
								}
								if (node.containsKey("edge_media_preview_like")) {
									JSONObject edge_media_preview_like = node.getJSONObject("edge_media_preview_like");
									insta.setLikes(edge_media_preview_like.getString("count"));
								}
								if (node.containsKey("owner")) {
									JSONObject ownerj=node.getJSONObject("owner");
									insta.setOwnerId(ownerj.getString("id").trim());
									insta.setOwnerName(ownerj.getString("username").trim());
								}
							}
							
							log.info(insta);
							boolean sqlb=dl.SaveInstagram(insta);
							if (!sqlb) {
								log.error("入库失败");
							}
						}
						
					}
				}
			}
		}
		
		pageInfor(html);
	
	}
	
	/**
	 * 修改翻页状态
	 * @param html
	 */
	public static void pageInfor(String html){
		JSONObject ht = JSONObject.parseObject(html);
		if (ht.containsKey("data")) {
			JSONObject data= ht.getJSONObject("data");
			if (data.containsKey("user")) {
				JSONObject user=data.getJSONObject("user");
				if (user.containsKey("edge_owner_to_timeline_media")) {
					JSONObject edge_owner = user.getJSONObject("edge_owner_to_timeline_media");
					if (edge_owner.containsKey("page_info")) {
						JSONObject page_info=edge_owner.getJSONObject("page_info");
						after=page_info.getString("end_cursor");
						log.info(">>>>>>>>>>"+after+"<<<<<<<<<<<<<");
						has_next_page=page_info.getString("has_next_page");
					}
				}
			}
		}
		
	}

}
