package com.devlib.crawler.handlers;

import java.util.List;

import com.devlib.crawler.config.ConfigParams;
import com.devlib.crawler.config.Item;
import com.devlib.crawler.config.Page;
import com.devlib.crawler.crawlers.CrawlerResult;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 其实对应于rule.xml中的一个page节点的处理部分。Page对应于page节点的数据部分
 * 
 * @author wenjian.liang
 */
public class HtmlPageHandlerImpl implements IHtmlPageHandler {

	private ConfigParams cp;
	private final Page page;

	public HtmlPageHandlerImpl(final Page page) {
		super();
		this.page = page;
	}

	@Override
	public void handle(final HtmlPage htmlPage, final CrawlerResult jobResult) {
		final List<Item> items = page.getItems();
		if (items == null || items.isEmpty()) {
			return;
		}
		for (final Item item : items) {
			final ItemHandler handler = getItemHandler(item);
			handler.setConfigParams(this.cp);
			final boolean isNotNull = handler.handle(item, htmlPage, jobResult);
			jobResult.setItemResult(item.getItemId(), isNotNull);
		}
	}

	private ItemHandler getItemHandler(final Item item) {
		return ItemHandlerFactory.getItemHandler(item.getType());
	}

	@Override
	public void setConfigParams(ConfigParams cp) {
		this.cp = cp;
	}

	@Override
	public ConfigParams getConfigParams() {
		return this.cp;
	}

	@Override
	public Page getPage() {
		return page;
	}
}