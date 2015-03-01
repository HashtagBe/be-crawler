package com.devlib.crawler.handlers;

import com.devlib.crawler.config.ConfigParams;
import com.devlib.crawler.config.Item;
import com.devlib.crawler.crawlers.CrawlerResult;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 本接口的实现类对抓回来的页面进行处理。
 * 
 * @author wenjian.liang
 */
public interface ItemHandler {

	/**
	 * 对抓回来的HtmlUnit类型的htmlPage页面和相应的item进行处理，将结果写到jobResult里去。
	 * 
	 * @param item
	 *            所处理页面的item节点，提供了处理此item的配置信息
	 * @param htmlPage
	 *            抓取的某个具体的html页面对象，被item对象用于提取页面元素
	 * @param jobResult
	 *            保存处理结果的对象
	 * @return 返回处理结果是否为“空”。若抓取成功，则返回true；否则，则返回false
	 */
	boolean handle(Item item, HtmlPage htmlPage, CrawlerResult jobResult);

	/**
	 * htmlunit 获取的页面中，有多个文档数据，对其中一个文档数据用相应的Item进行处理，将结果添加到StringBuffer中去
	 * 
	 * @param item
	 *            item节点处理，提供处理此item的配置信息
	 * @param element
	 *            页面中的某个节点，用于相应的item进行二次处理
	 * @param result
	 *            保存处理结果的对象
	 * @return 返回处理结果是否为“空”。若抓取成功，则返回true；否则，返回false
	 */
	void handleElement(Item item, HtmlElement element, StringBuilder result);

	/**
	 * 设置配置参数对象，此配置参数可能会被处理器所使用
	 * 
	 * @param cp
	 *            参数集合
	 */
	public void setConfigParams(ConfigParams cp);
}
