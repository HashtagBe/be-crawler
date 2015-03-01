package com.devlib.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.devlib.util.ProxyIPFetcher;
import com.devlib.util.ProxyIPFetcher.IP;

/**
 * 网页访问通用工具
 * 
 * @author jerome
 */
public class ClientUtils {

	/**
	 * post json
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	public static String post(String url, String json) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try {
			StringEntity entity = new StringEntity(json, "UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
			post.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT,
					5000);
			post.getParams().setIntParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);

			HttpResponse response;
			HttpEntity resEntity;
			try {
				response = client.execute(post);
				resEntity = response.getEntity();
			} catch (SocketTimeoutException e) {
				// DeepLogger.logger.debug("接口访问超时，休眠2s再次访问"+url+":"+json);
				Thread.sleep(2000);
				response = client.execute(post);
				resEntity = response.getEntity();
			}
			String content = EntityUtils.toString(resEntity, "UTF-8");
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			// DeepLogger.logger.error("",e);
			return null;
		}

	}

	public static String post(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try {
			StringEntity entity = new StringEntity("", "UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
			post.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT,
					5000);
			post.getParams().setIntParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);

			HttpResponse response;
			HttpEntity resEntity;
			try {
				response = client.execute(post);
				resEntity = response.getEntity();
			} catch (SocketTimeoutException e) {
				// DeepLogger.logger.debug("接口访问超时，休眠2s再次访问"+url+":"+json);
				Thread.sleep(2000);
				response = client.execute(post);
				resEntity = response.getEntity();
			}
			String content = EntityUtils.toString(resEntity, "UTF-8");
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			// DeepLogger.logger.error("",e);
			return null;
		}

	}

	public static void main(String[] args) {
		String url = "http://hotel.qunar.com/render/detailV2.jsp?fromDate=2013-12-27&toDate=2013-12-28&cityurl=enshi&HotelSEQ=enshi_2426&mixKey=0a8c69373c510d13879580470071fBuRlo6lyDaf1ligNuEG3LOwb2ZPh0zl";
		String content = post(url);
		System.out.println(content);
	}

	public static String getHttpContentUseProxy(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		IP ip = ProxyIPFetcher.getProxyIP();
		HttpPost post = new HttpPost(url);
		HttpHost proxy = new HttpHost(ip.getAddress(), ip.getPort());
		try {
			StringEntity entity = new StringEntity("UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
			post.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT,
					10000);
			post.getParams().setIntParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);

			HttpResponse response;
			HttpEntity resEntity;
			try {
				response = httpclient.execute(post);
				resEntity = response.getEntity();
			} catch (SocketTimeoutException e) {
				// DeepLogger.logger.debug("接口访问超时，休眠2s再次访问"+url+":"+json);
				Thread.sleep(2000);
				response = httpclient.execute(post);
				resEntity = response.getEntity();
			}
			String content = EntityUtils.toString(resEntity, "UTF-8");
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			// DeepLogger.logger.error("",e);
			return null;
		} finally {
			httpclient.clearResponseInterceptors();
		}

	}

	/**
	 * 去除两头空格，空则返回空
	 * 
	 * @param s
	 * @return
	 */
	public static String trim(String s) {
		if (null != s && !"".equals(s)) {
			return s.trim();
		}
		return null;
	}
}
