package com.devlib.crawler.crawlers;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.devlib.crawler.config.Url;
import com.devlib.util.Const;

/**
 * The crawl result object, contains the new urls and scd contents
 * 
 * @author zhouxiang
 */
public class CrawlerResult {
	private final Set<Url> newUrls = new HashSet<Url>();
	/**
	 * 处理数据产生的结果，最终会写入scd文件
	 */
	private final Map<String, String> scdContent = new HashMap<String, String>();

	// 页面返回的http response code
	private int statusCode;
	/**
	 * 经过item们的处理之后，保存的item的处理结果, key为item ID，value为是否处理成功
	 */
	private final Map<String, Boolean> itemResult = new HashMap<String, Boolean>();

	/**
	 * 这个jobResult对象的源url，即它是哪个页面工作的结果
	 */
	private final Url url;

	/**
	 * link正则表达式
	 */
	private Set<Pattern> patterns = new HashSet<Pattern>(3);

	public CrawlerResult(final Url url) throws CloneNotSupportedException {
		this.url = url;
	}

	public Url getUrl() {
		return url;
	}

	public String getUrlValue() {
		return url.getValue();
	}

	public void addUrl(final Url seedUrl) {
		newUrls.add(seedUrl);
	}

	public void addAllUrls(final Set<Url> seeds) {
		newUrls.addAll(seeds);
	}

	public void addScdContent(final String key, String value) {
		if (null == value)
			value = "";
		if (scdContent.containsKey(key)) {
			String line = value.length() > 0
					&& scdContent.get(key).length() > 0 ? Const.NEW_LINE : "";
			value = scdContent.get(key) + line + value;
		}
		scdContent.put(key, value);
	}

	/**
	 * return the original scd content
	 * 
	 * @return
	 */
	public Map<String, String> getScdContent() {
		return scdContent;
	}

	public Set<Url> getURLs() {
		return newUrls;
	}

	/**
	 * Set the JobResult status code, should be a http response code
	 * 
	 * @param code
	 */
	public void setStatusCode(final int code) {
		statusCode = code;
	}

	/**
	 * Return the JobResult status code, it's the http response code
	 * 
	 * @return
	 */
	public int getStatusCode() {
		return statusCode;
	}

	public void setItemResult(final String itemId, final boolean isNull) {
		itemResult.put(itemId, isNull);
	}

	public Map<String, Boolean> getItemResult() {
		final Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.putAll(itemResult);
		return result;
	}

	public boolean getItemResult(final String itemId) {
		final Boolean result = itemResult.get(itemId);
		if (result == null) {
			return false;
		}
		return result;
	}

	public Set<Pattern> getPatterns() {
		return Collections.unmodifiableSet(patterns);
	}

	public void addPattern(Pattern pattern) {
		this.patterns.add(pattern);
	}
}
