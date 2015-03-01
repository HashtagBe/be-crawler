package com.devlib.crawler.handlers;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.devlib.crawler.config.Item;
import com.devlib.util.Const;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

public class InnerHtmlHandler extends BaseDataHandler {

	@Override
	public void handleElement(Item item, HtmlElement element, StringBuilder sb) {
		String xpath = item.getXpath();
		List<HtmlElement> el = (List<HtmlElement>) element.getByXPath(xpath);
		if (null == el || el.size() == 0) {
			sb.append(Const.NEW_LINE);
			return;
		}
		String value = el.get(0).asXml().trim();

		if (StringUtils.isEmpty(value) || "".equals(value.trim())) {
			return;
		}

		sb.append(value).append(Const.NEW_LINE);
	}

}
