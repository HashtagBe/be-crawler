package com.devlib.crawler.config;

import java.util.regex.Pattern;

import com.devlib.crawler.crawlers.CrawlBrowserVersion;
import com.devlib.util.Const;

/**
 * 对应rule.xml中Rule > Seeds > Url
 * 
 * @author wenjian.liang
 */
public class Url {
	public static enum CrawlerType {
		HtmlUnit("htmlUnit");

		private String type;

		private CrawlerType(String type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return this.type;
		}
	}

	// 对应page.id
	private String pageId;
	// 真正的url地址
	private String value;

	// 下面这4个是请求页面时候的参数
	private boolean executeJs;
	private boolean executeCss;
	private boolean ajaxCall;
	private int waitTime;
	private String cookie;

	private CrawlBrowserVersion browserVersion = CrawlBrowserVersion
			.getDefault();
	private CrawlerType crawlerType = CrawlerType.HtmlUnit;

	// 下面的是用来检验抓到的url是否符合条件的（还有chops）
	private Pattern regex;

	// 下面的是给一些专门的ItemHandler的参数。
	// private String ajaxButton;// 目前只有在property里面的url标签有这个属性
	private String encode;// 目前只有在property里面的url标签有这个属性

	// 页面配置的xpath表达式，为可选项
	private String xpath;

	// 页面配置的cssQuery表达式，为可选项
	private String cssQuery;

	// 有些url需要修正一下，比如苏宁网站列表页面的下一页按钮搞出来的url，就多了一点东西，这是苏宁的bug
	private String fixMark;

	// 指示此URL是否优先处理
	private int priority = 0;

	// 描述多个占位符的赋值属性
	private String replaceValue;

	public void setWaitTime(final String waitTime) {
		this.waitTime = Integer.parseInt(waitTime);
	}

	public void setExecuteJs(final String executeJs) {
		this.executeJs = Boolean.valueOf(executeJs)
				|| Const.YES.equalsIgnoreCase(executeJs);
	}

	public void setExecuteCss(final String executeCss) {
		this.executeCss = Boolean.valueOf(executeCss)
				|| Const.YES.equalsIgnoreCase(executeCss);
	}

	public void setAjaxCall(final String ajaxCall) {
		this.ajaxCall = Boolean.valueOf(ajaxCall)
				|| Const.YES.equalsIgnoreCase(ajaxCall);
	}

	public void setRegex(final String regex) {
		this.regex = Pattern.compile(regex);
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		boolean b = (Boolean.valueOf(priority) || Const.YES
				.equalsIgnoreCase(priority));
		if (!b) {
			try {
				int i = Integer.parseInt(priority);
				if (i >= 2)
					this.priority = 2;
				else if (i == 1)
					this.priority = 1;
			} catch (Exception e) {
			}
		} else {
			this.priority = 1;
		}
	}

	public String getReplaceValue() {
		return replaceValue;
	}

	public void setReplaceValue(String replaceValue) {
		this.replaceValue = replaceValue;
	}

	// ----------------------------------
	public String serialize() {
		final StringBuilder sb = new StringBuilder("<url ")
				//
				.append("pageId=").append("\"").append(pageId).append("\"")
				.append("value=").append("\"").append(value)
				.append("\"")
				.append("executeJs=")
				.append("\"")
				.append(executeJs)
				.append("\"")
				//
				.append("ajaxCall=").append("\"").append(ajaxCall).append("\"")
				.append("waitTime=").append("\"").append(waitTime).append("\"")
				.append("browserVersion=").append("\"").append(browserVersion)
				.append("\"")
				//
				.append("regex=").append("\"").append(regex.pattern())
				.append("\"")
				//
				.append("encode=").append("\"").append(encode).append("\"")//
				.append(" >");

		return sb.append("</url>").toString();
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(final String pageId) {
		this.pageId = pageId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public boolean getExecuteJs() {
		return executeJs;
	}

	public void setExecuteJs(final boolean executeJs) {
		this.executeJs = executeJs;
	}

	public boolean getExecuteCss() {
		return executeCss;
	}

	public void setExecuteCss(final boolean executeCss) {
		this.executeCss = executeCss;
	}

	public boolean getAjaxCall() {
		return ajaxCall;
	}

	public void setAjaxCall(final boolean ajaxCall) {
		this.ajaxCall = ajaxCall;
	}

	public Pattern getRegex() {
		return regex;
	}

	public void setRegex(final Pattern regex) {
		this.regex = regex;
	}

	public int getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(final int waitTime) {
		this.waitTime = waitTime;
	}

	public CrawlBrowserVersion getBrowserVersion() {
		return browserVersion == null ? CrawlBrowserVersion.getDefault()
				: browserVersion;
	}

	public void setBrowserVersion(final String browserVersion) {
		this.browserVersion = CrawlBrowserVersion
				.getCrawlBrowser(browserVersion);
	}

	public boolean isExecuteJs() {
		return getExecuteJs();
	}

	public boolean isExecuteCss() {
		return getExecuteCss();
	}

	public boolean isAjaxCall() {
		return getAjaxCall();
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(final String encode) {
		this.encode = encode;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	@Override
	public String toString() {
		return "Url [value=" + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (value == null ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Url other = (Url) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	public void setBrowserVersion(final CrawlBrowserVersion browserVersion) {
		this.browserVersion = browserVersion;
	}

	public void setCrawlerType(String type) {
		CrawlerType[] types = CrawlerType.values();
		for (CrawlerType ct : types) {
			if (ct.toString().equalsIgnoreCase(type)) {
				this.crawlerType = ct;
				break;
			}
		}
	}

	public CrawlerType getCrawlerType() {
		return this.crawlerType;
	}

	public String getCssQuery() {
		return cssQuery;
	}

	public void setCssQuery(String cssQuery) {
		this.cssQuery = cssQuery;
	}

	public String getFixMark() {
		return fixMark;
	}

	public void setFixMark(String fixMark) {
		this.fixMark = fixMark;
	}

	public Url clone() {
		Url cloneUrl = null;
		try {
			cloneUrl = (Url) super.clone();
		} catch (CloneNotSupportedException cnse) {
			cloneUrl = new Url();
			cloneUrl.setPageId(this.getPageId());
			cloneUrl.setExecuteJs(this.executeJs);
			cloneUrl.setExecuteCss(this.executeCss);
			cloneUrl.setAjaxCall(this.ajaxCall);
			cloneUrl.setXpath(this.xpath);
			cloneUrl.setCssQuery(this.cssQuery);
			cloneUrl.setFixMark(this.fixMark);
			cloneUrl.setValue(this.value);
			cloneUrl.setWaitTime(this.getWaitTime());
			cloneUrl.setPriority(String.valueOf(this.getPriority()));
			cloneUrl.setReplaceValue(this.replaceValue);
		}
		// set crawler type
		cloneUrl.setCrawlerType(this.getCrawlerType().toString());
		cloneUrl.setBrowserVersion(this.getBrowserVersion().toString());
		if (null != this.getRegex()) {
			cloneUrl.setRegex(this.getRegex().toString());
		}
		return cloneUrl;
	}

}
