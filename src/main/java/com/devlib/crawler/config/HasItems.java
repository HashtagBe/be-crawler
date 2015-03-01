package com.devlib.crawler.config;

import java.util.LinkedList;
import java.util.List;

/**
 * 给有Item子标签的标签提供方便编码支持的父类
 * 
 * @author wenjian.liang
 */
class HasItems {

	protected final List<Item> items = new LinkedList<Item>();

	public HasItems() {
		super();
	}

	public void addItem(final Item item) {
		items.add(item);
	}

	public List<Item> getItems() {
		return items;
	}

}