package com.devlib.crawler.config;

import java.util.regex.Pattern;

/**
 * 对应rule.xml中Rule > Page > Item > Property > Content 注意：是Property可能的子标签之一
 * 
 * @author wenjian.liang
 */
public class Content extends HasScdName {
	private Pattern pattern;

	private String replaceStr = "";

	public void setRegex(final String regex) {
		if (regex == null || regex.length() == 0) {
			pattern = null;
			return;
		}
		this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setReplaceStr(String replaceStr) {
		if (replaceStr == null || replaceStr.length() == 0) {
			this.replaceStr = "";
		} else {
			this.replaceStr = replaceStr;
		}
	}

	public String getReplaceStr() {
		return this.replaceStr;
	}
}
