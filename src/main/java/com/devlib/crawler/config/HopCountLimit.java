package com.devlib.crawler.config;

/**
 * 对应rule.xml中rule > hopCountLimit 节点
 * 
 * @author guangfeng
 * 
 */
public class HopCountLimit {
	/**
	 * hop count限制数目
	 */
	private int limit = Integer.MAX_VALUE;

	public void set(final String content) {
		limit = Integer.parseInt(content);
	}

	public int get() {
		return limit;
	}
}
