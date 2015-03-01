package com.devlib.crawler.config;

import java.util.HashMap;

/**
 * 配置参数的集合类。任何配置参数都可以放入此类，用于维护程序运行时的配置参数。
 * 
 * @author guangfeng
 * 
 */
public class ConfigParams {
	private HashMap<String, Object> params = new HashMap<String, Object>();

	public Object getParam(String key) {
		return this.params.get(key);
	}

	public Object removeParam(String key) {
		return this.params.remove(key);
	}

	public void addParam(String key, Object value) {
		this.params.put(key, value);
	}
}
