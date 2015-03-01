package com.devlib.crawler.crawlers;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * 一个枚举，用来对rule.xml中的browserVersion属性和真正要使用的BrowserVersion对象做一个映射关系
 * 
 * @author wenjian.liang
 */
public enum CrawlBrowserVersion {
	Firefox_24("ff24", BrowserVersion.FIREFOX_24), //
	IE11("ie7", BrowserVersion.INTERNET_EXPLORER_11);

	public static CrawlBrowserVersion getCrawlBrowser(final String stringValue) {
		final CrawlBrowserVersion[] bs = CrawlBrowserVersion.values();
		for (final CrawlBrowserVersion b : bs) {
			if (b.stringValue.equalsIgnoreCase(stringValue)) {
				return b;
			}
		}
		return getDefault();
	}

	public static CrawlBrowserVersion getDefault() {
		return IE11;// ie11 for default
	}

	private final String stringValue;
	private final BrowserVersion browserVersion;

	private CrawlBrowserVersion(final String stringValue,
			final BrowserVersion browserVersion) {
		this.stringValue = stringValue;
		this.browserVersion = browserVersion;
	}

	public String getStringValue() {
		return stringValue;
	}

	public BrowserVersion getBrowserVersion() {
		return browserVersion;
	}

}
