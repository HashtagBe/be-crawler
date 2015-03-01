package com.devlib.crawler.crawlers;

import com.devlib.crawler.config.Url.CrawlerType;

/**
 * Crawler的工厂类，根据crawler类型获取相应的crawler 对象
 * 
 * @author guangfeng
 * 
 */
public class CrawlerFactory {
	/**
	 * 获取crawler对象
	 * 
	 * @param type
	 *            crawler 类型
	 * @return 若类型正确，则返回相应的类型；否则返回null
	 */
	public static ICrawler getCrawlerInstance(CrawlerType type) {
		if (type == CrawlerType.HtmlUnit)
			return new HtmlUnitCrawler();
		else
			return null;
	}
}
