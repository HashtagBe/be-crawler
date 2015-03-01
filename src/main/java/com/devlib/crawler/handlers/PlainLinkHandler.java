package com.devlib.crawler.handlers;

import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.devlib.crawler.config.Item;
import com.devlib.crawler.config.Url;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 此类为对应于link类型item的处理器
 * <p>
 * 获取指定元素及其所包含元素的所有a标签（如果它本身就是a标签，包含它本身）的链接作为新发现的任务url。
 * 适用于&lt;a&gt;标签直接是一个普通href跳转的情况。
 * </p>
 * 
 * @author guangfeng.jin
 */
public class PlainLinkHandler extends BaseLinkHandler {

	private final SelfLinkHandler selfLinkHandler = new SelfLinkHandler();

	@Override
	protected String getLinkFromMatchedElement(final Item item, Set<Url> urls,
			final Element element) {
		if ("a".equalsIgnoreCase(element.tagName())) {// 本元素本身是一个a标签
			return selfLinkHandler.getLinkFromMatchedElement(item, urls,
					element);
		} else {// 本元素不是a标签，要去取其子元素中的a标签
			Elements descendantsLink = element.getElementsByTag("a");
			if (descendantsLink != null && !descendantsLink.isEmpty()) {
				selfLinkHandler.getLinksFromMatchedElements(item, urls,
						descendantsLink);
			}

			return null;
		}
	}

	@Override
	protected String getLinkFromMatchedElement(final Item item,
			final HtmlPage htmlPage, Set<Url> urls, final HtmlElement ele) {
		if (ele instanceof HtmlAnchor) {// 本元素本身是一个a标签
			return selfLinkHandler.getLinkFromMatchedElement(item, htmlPage,
					urls, ele);
		} else {// 本元素不是a标签，要去取其子元素中的a标签
			final List<HtmlElement> links = ele.getElementsByTagName("a");

			if (links != null && !links.isEmpty()) {
				selfLinkHandler.getLinksFromMatchedElements(item, htmlPage,
						urls, links);
			}

			return null;
		}
	}
}
