package com.devlib.crawler.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * rule.xml的根元素
 * 
 * @author wenjian.liang
 */
public class Rule {

	private final Map<String, Page> idPageMap = new HashMap<String, Page>();

	private final List<Page> pages = new LinkedList<Page>();

	// seed url 集合
	private final Collection<Url> urls = new LinkedList<Url>();
	// hop count限制
	private HopCountLimit hopCountLimit;

	public Page getPageById(final String pageId) {
		return idPageMap.get(pageId);
	}

	public void addUrl(final Url url) {
		urls.add(url);
	}

	public Collection<Url> getUrls() {
		return urls;
	}

	public List<Page> getPages() {
		return pages;
	}

	public void addPage(final Page page) {
		pages.add(page);
		idPageMap.put(page.getId(), page);
	}

	public void addPages(List<Page> pages) {
		for (Page page : pages) {
			addPage(page);
		}
	}

	public HopCountLimit getHopCountLimit() {
		return hopCountLimit;
	}

	public void setHopCountLimit(final HopCountLimit hopCountLimit) {
		this.hopCountLimit = hopCountLimit;
	}
}
