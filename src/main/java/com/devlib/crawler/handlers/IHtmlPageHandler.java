package com.devlib.crawler.handlers;

import com.devlib.crawler.config.ConfigParams;
import com.devlib.crawler.config.Page;
import com.devlib.crawler.crawlers.CrawlerResult;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 对爬下来的HtmlPage进行处理的处理器。
 * 
 * @author wenjian.liang
 */
public interface IHtmlPageHandler {

	/**
	 * 处理以HtmlUnit方式爬下来的页面
	 * 
	 * @param htmlPage
	 *            页面对象
	 * @param jobResult
	 *            处理结果保存对象
	 */
	void handle(HtmlPage htmlPage, CrawlerResult jobResult);

	/**
	 * 设置处理页面时需要用到的配置参数
	 * 
	 * @param cp
	 *            配置参数集合对象
	 */
	void setConfigParams(ConfigParams cp);

	/**
	 * 获取页面处理的配置参数
	 * 
	 * @return 配置参数集合对象
	 */
	ConfigParams getConfigParams();

	/**
	 * 获取配置的Page
	 * 
	 * @return
	 */
	Page getPage();
}
