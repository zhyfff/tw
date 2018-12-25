package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;  
import java.util.HashMap;
import java.util.List;  
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;  
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;  
import org.apache.http.client.config.RequestConfig;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.CloseableHttpResponse;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.StringEntity;  
import org.apache.http.impl.client.CloseableHttpClient;  
import org.apache.http.impl.client.HttpClients;  
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.util.EntityUtils;

import com.caiji.cn.ProxyIp;
import com.twitter.entry.IpBean;
import com.twitter.util.UtilTool;


public class HttpClientUtil { 
	public static Log log = LogFactory.getLog(HttpClientUtil.class);
	private static UtilTool utilTool = UtilTool.getInstace();
	private static String ipaddress;
	private static int iport=0;
	static {
		IpBean ipBean=new IpBean();
		ipaddress=utilTool.getIpTool(ipBean).getIpaddress();
		iport=utilTool.getIpTool(ipBean).getIport();
	}
	private RequestConfig requestConfig = RequestConfig.custom()  
			.setSocketTimeout(15000)  //读取超时
			.setConnectTimeout(15000)  //连接超时
			.setConnectionRequestTimeout(15000) //从连接池获取连接的timeout
//			.setProxy(new HttpHost("127.0.0.1",8580))
			.setProxy(new HttpHost(ipaddress,iport))
			.build();  

	private static HttpClientUtil instance = null;  
	private HttpClientUtil(){}  
	public static HttpClientUtil getInstance(){  
		if (instance == null) {  
			instance = new HttpClientUtil();  
		}  
		return instance;  
	}
	
	private static HashMap<String, String> MAP = new HashMap<>();
	
	public void randomSleep(){
		int ss[] = {10000,11000,12000,13000,14000,15000,16000,17000,18000,19000,20000 };
		int index = (int) (Math.random() * ss.length);
//		log.info("休眠" + ss[index] + "毫秒");
		try {
			Thread.sleep(ss[index]);
		} catch (InterruptedException e) {
//			e.printStackTrace();
			log.error(e);
		}
	}
	/**
	 * 清除ip
	 */
	public static void clearGIP() {
		MAP.clear();
//		ipList.add(getwuyouip.getIp());
	}
	
	
	/**
	 * 
	 * 存储获取ip
	 * @return
	 */
	public RequestConfig getRequestConfig() {
		if (MAP.isEmpty()) {
			log.info("取ip啦！！！！");
			randomSleep();
			MAP = getIp();
		}
		
		RequestConfig requestConfig;
		requestConfig = RequestConfig.custom().setSocketTimeout(15000) // 读取超时
				.setConnectTimeout(25000) // 连接超时
				.setConnectionRequestTimeout(15000) // 从连接池获取连接的timeout
				// .setProxy(new HttpHost(ipaddress,iport))
				.setProxy(new HttpHost(MAP.get("ipadress"), Integer.parseInt(MAP.get("prot")))).build();

		return requestConfig;
	}
	
	/**
	 * 获取ip
	 * @return
	 */
	public static HashMap<String, String> getIp() {

		HashMap<String, String> map = new HashMap<>();
		String ipadress = null;
		int prot = 0;
		try {
			String ipAndprot[] = ProxyIp.getInternce().getip().split(":");// 将获取到的IP放到ipAndprot对象数组中以“:”号进行分割
			ipadress = ipAndprot[0];// IP地址
			prot = Integer.parseInt(ipAndprot[1]);// IP端口
		} catch (Exception e) {
			log.info("没有获取到IP");
		}
		map.put("ipadress", ipadress);
		map.put("prot", prot + "");

		log.info("代理IP地址：" + ipadress + "所对应的端口prot：" + prot);// 你的业务逻辑
		return map;
	}
	
	/**
	 * 发送PUT请求
	 * @param httpUrl 地址
	 * @param req 请求参数
	 * @param headmaps 请求头
	 * */
	public String sendHttpPut(String httpUrl,String req,Map<String,String> headMaps)
	{
		HttpPut httpPut=new HttpPut(httpUrl);	
		for(String key:headMaps.keySet())
		{
			httpPut.setHeader(key, headMaps.get(key));
		}
		try {
			StringEntity reqEntity=new StringEntity(req);
			httpPut.setEntity(reqEntity);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return	sendHttpPut(httpPut);
	}
	

	
	/** 
	 * 发送Put请求 
	 * @param httpPost 
	 * @return 
	 */  
	private String sendHttpPut(HttpPut httpPut) {  
		CloseableHttpClient httpClient = null;  
		CloseableHttpResponse response = null;  
		HttpEntity entity = null;  
		String responseContent = null;  
		try {  
			// 创建默认的httpClient实例.  
			httpClient = HttpClients.createDefault();  
			httpPut.setConfig(requestConfig);  
			// 执行请求  
			response = httpClient.execute(httpPut);  
			entity = response.getEntity();  
			responseContent = EntityUtils.toString(entity, "UTF-8");  
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {  
			try {  
				// 关闭连接,释放资源  
				if (response != null) {  
					response.close();  
				}  
				if (httpClient != null) {  
					httpClient.close();  
				}  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
		return responseContent;  
	}  
	
	

	
	
	
	
	

	/** 
	 * 发送 post请求 
	 * @param httpUrl 地址 
	 */  
	public String sendHttpPost(String httpUrl) {  
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost    
		return sendHttpPost(httpPost);  
	}  

	/** 
	 * 发送 post请求 
	 * @param httpUrl 地址 
	 * @param params 参数(格式:key1=value1&key2=value2) 
	 */  
	public String sendHttpPost(String httpUrl, String params) {  
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost    
		try {  
			//设置参数  
			StringEntity stringEntity = new StringEntity(params, "UTF-8");  
			stringEntity.setContentType("application/x-www-form-urlencoded");  
			httpPost.setEntity(stringEntity);  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return sendHttpPost(httpPost);  
	}  
	
	/**
	 * 发送post请求
	 * @param httpsUrl 地址
	 * @param req 请求参数
	 * @param headmaps 请求头
	 * */
	public String sendHttpsPost(String httpUrl,String req,Map<String,String> headMaps)
	{
		HttpPost httpPost=new HttpPost(httpUrl);	
		for(String key:headMaps.keySet())
		{
			httpPost.setHeader(key, headMaps.get(key));
		}
		try {
			StringEntity reqEntity=new StringEntity(req);
			httpPost.setEntity(reqEntity);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return	sendHttpsPost(httpPost);
	}
	
	
	
	/**
	 * 发送post请求
	 * @param httpUrl 地址
	 * @param req 请求参数
	 * @param headmaps 请求头
	 * */
	public String sendHttpPost(String httpUrl,String req,Map<String,String> headMaps)
	{
		HttpPost httpPost=new HttpPost(httpUrl);	
		for(String key:headMaps.keySet())
		{
			httpPost.setHeader(key, headMaps.get(key));
		}
		try {
			StringEntity reqEntity=new StringEntity(req);
			httpPost.setEntity(reqEntity);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return	sendHttpPost(httpPost);
	}
	/**
	 * 发送post请求
	 * @param httpUrl 地址
	 * @param headMaps 请求头
	 * @param maps 请求参数
	 * */
	public String sendHttpPost(String httpUrl,Map<String,String> headMaps,Map<String,String> maps)
	{
		HttpPost httpPost=new HttpPost(httpUrl);	
		for(String key:headMaps.keySet())
		{
			httpPost.setHeader(key, headMaps.get(key));
		}
		// 创建参数队列    
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
		for (String key : maps.keySet()) {  //获取map中的key
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));  
		}   
		try {  
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8")); 
		} catch (Exception e) {
			e.printStackTrace();  
		}  
		return	sendHttpPost(httpPost);
	}
	
	/** 
	 * 发送 post请求 
	 * @param httpUrl 地址 
	 * @param maps 参数 
	 */  
	public String sendHttpPost(String httpUrl, Map<String, String> maps) {  
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost    
		// 创建参数队列    
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
		for (String key : maps.keySet()) {  //获取map中的key
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));  
		}  
		try {  
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return sendHttpPost(httpPost);  
	}  
	/** 
	 * 发送 post请求（带文件） 
	 * @param httpUrl 地址 
	 * @param maps 参数 
	 * @param fileLists 附件 
	 */  
//	public String sendHttpPost(String httpUrl, Map<String, String> maps, List<File> fileLists) {  
//		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost    
//		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();  
//		for (String key : maps.keySet()) {  
//			meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));  
//		}  
//		for(File file : fileLists) {  
//			FileBody fileBody = new FileBody(file);  
//			meBuilder.addPart("files", fileBody);  
//		}  
//		HttpEntity reqEntity = meBuilder.build();  
//		httpPost.setEntity(reqEntity);  
//		return sendHttpPost(httpPost);  
//	}  

	/** 
	 * 发送Post请求 
	 * @param httpPost 
	 * @return 
	 */  
	private String sendHttpPost(HttpPost httpPost) {  
		CloseableHttpClient httpClient = null;  
		CloseableHttpResponse response = null;  
		HttpEntity entity = null;  
		String responseContent = null;  
		try {  
			// 创建默认的httpClient实例.  
			httpClient = HttpClients.createDefault();  
			httpPost.setConfig(requestConfig);  
			// 执行请求  
			response = httpClient.execute(httpPost);  
			entity = response.getEntity();  
			responseContent = EntityUtils.toString(entity, "UTF-8");  
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {  
			try {  
				// 关闭连接,释放资源  
				if (response != null) {  
					response.close();  
				}  
				if (httpClient != null) {  
					httpClient.close();  
				}  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
		return responseContent;  
	}  
	
	
	/** 
	 * 发送Post请求 
	 * @param httpsPost 
	 * @return 
	 */  
	private String sendHttpsPost(HttpPost httpPost) {  
		CloseableHttpClient httpClient = null;  
		CloseableHttpResponse response = null;  
		HttpEntity entity = null;  
		String responseContent = null;  
		try {  
			// 创建默认的httpClient实例.  
			
			httpClient = HttpClients.createDefault();   
//			httpClient = new SSLClient();
			httpPost.setConfig(requestConfig);  
			// 执行请求  
			response = httpClient.execute(httpPost);  
			entity = response.getEntity();  
			responseContent = EntityUtils.toString(entity, "UTF-8");  
		}catch (ConnectException e) {
		    log.info("连接超时 重新连接中------------");
		    sendHttpsPost(httpPost);
		}catch (ConnectTimeoutException e) {
		    log.info("连接超时 重新连接中------------");
		    sendHttpsPost(httpPost);
		}catch (UnknownHostException e) {
		    log.info("请求出错 重新连接中------------");
		    sendHttpsPost(httpPost);
		}catch (SocketTimeoutException e) {
		    log.info("运行超时 重新连接中------------");
		    sendHttpsPost(httpPost);
		}catch (SocketException e) {
		    log.info("连接异常 重新连接中------------");
		    sendHttpsPost(httpPost);
		}catch (StackOverflowError e) {
		    log.info("估计是没有网了 或者网速极度不好------------");
		    sendHttpsPost(httpPost);
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {  
			try {  
				// 关闭连接,释放资源  
				if (response != null) {  
					response.close();  
				}  
				if (httpClient != null) {  
					httpClient.close();  
				}  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
		return responseContent;  
	}  
	
	
	

	/** 
	 * 发送 get请求 
	 * @param httpUrl 
	 */  
	public String sendHttpGet(String httpUrl,Map<String, String> headMaps) {  
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求  
		
		for(String key:headMaps.keySet())
		{
			httpGet.setHeader(key, headMaps.get(key));
		}
		
		return sendHttpGet(httpGet);  
	}  

	/** 
	 * 发送 get请求Https 
	 * @param httpUrl 
	 */  
	public String sendHttpsGet(String httpUrl,Map<String, String> headMaps,int count) {  
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求  
		for(String key:headMaps.keySet())
		{
			httpGet.setHeader(key, headMaps.get(key));
		}
		
		return sendHttpsGet(httpGet,count);  
	}  

	/** 
	 * 发送Get请求 
	 * @param httpPost 
	 * @return 
	 */  
	private String sendHttpGet(HttpGet httpGet) {  
		CloseableHttpClient httpClient = null;  
		CloseableHttpResponse response = null;  
		HttpEntity entity = null;  
		String responseContent = null;  
		try {  
			// 创建默认的httpClient实例.  
			httpClient = HttpClients.createDefault();  
			httpGet.setConfig(requestConfig);  
			// 执行请求  
			response = httpClient.execute(httpGet);  
			entity = response.getEntity();  
			responseContent = EntityUtils.toString(entity, "UTF-8");  
		}catch (ConnectException e) {
		    log.info("连接超时 重新连接中------------");
		    sendHttpGet(httpGet);
		}catch (UnknownHostException e) {
		    log.info("请求出错 重新连接中------------");
		    sendHttpGet(httpGet);
		}catch (SocketTimeoutException e) {
		    log.info("运行超时 重新连接中------------");
		    sendHttpGet(httpGet);
		}catch (SocketException e) {
		    log.info("连接异常 重新连接中------------");
		    sendHttpGet(httpGet);
		}catch (StackOverflowError e) {
		    log.info("估计是没有网了 或者网速极度不好------------");
		    sendHttpGet(httpGet);
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {  
			try {  
				// 关闭连接,释放资源  
				if (response != null) {  
					response.close();  
				}  
				if (httpClient != null) {  
					httpClient.close();  
				}  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
		return responseContent;  
	}  

	/** 
	 * 发送Get请求Https 
	 * @param httpGet 
	 * @return 
	 */  
	private String sendHttpsGet(HttpGet httpGet ,int count) {  
		CloseableHttpClient httpClient = null;  
		CloseableHttpResponse response = null;  
		HttpEntity entity = null;  
		String responseContent = null; 
		
		try {  
			//TODO
//			httpGet.setConfig(getRequestConfig());
			httpGet.setConfig(requestConfig);
			// 创建默认的httpClient实例.  
//			PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));  
//			DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);  
//			httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
			httpClient = HttpClients.createDefault(); 			
			// 执行请求  
			response = httpClient.execute(httpGet);  
			entity = response.getEntity();  
			responseContent = EntityUtils.toString(entity, "utf-8");  
			
		}catch (ConnectException e) {
		    log.info("连接超时 重新连接中------------");
		    count--;
		    if (count<1) {
		    	 clearGIP();
			}
		    sleep(2000);
		    return sendHttpsGet(httpGet,count);
		}catch (UnknownHostException e) {
		    log.info("请求出错 重新连接中------------");
		    count--;
		    if (count<1) {
		    	 clearGIP();
			}
		    sleep(2000);
		    return sendHttpsGet(httpGet,count);
		}catch (SocketTimeoutException e) {
		    log.info("运行超时 重新连接中------------");
		    count--;
		    if (count<1) {
		    	 clearGIP();
			}
		    sleep(2000);
		    return sendHttpsGet(httpGet,count);
		}catch (SocketException e) {
		    log.info("连接异常 重新连接中------------");
		    count--;
		    if (count<1) {
		    	 clearGIP();
			}
		    sleep(2000);
		    return sendHttpsGet(httpGet,count);
		}catch (StackOverflowError e) {
		    log.info("估计是没有网了 或者网速极度不好------------");
		    count--;
		    if (count<1) {
		    	 clearGIP();
			}
		    sleep(2000);
		    return sendHttpsGet(httpGet,count);
		}catch (Exception e) {  
			e.printStackTrace();  
		} finally {  
			try {  
				// 关闭连接,释放资源  
				if (response != null) {  
					response.close();  
				}  
				if (httpClient != null) {  
					httpClient.close();  
				}  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
		return responseContent;  
	}
	
	
	
	
	
	
	
	
	public static void sleep(int s){
		try {
			Thread.sleep(s);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
}  