package com.devlib.crawler.crawlers;

import com.devlib.crawler.config.Url;
import com.devlib.crawler.handlers.IHtmlPageHandler;

public interface ICrawler {

	/**
	 * 爬取指定的HTML页面并获取结果
	 * 
	 * @param seedUrl
	 *            要爬取的HTML页面信息的entity对象
	 * @param pageHandler
	 *            针对此页面的处理器
	 * @return 爬取的结果对象，若爬取失败，则返回null
	 */
	CrawlerResult getJobResult(Url seedUrl, IHtmlPageHandler pageHandler);
}