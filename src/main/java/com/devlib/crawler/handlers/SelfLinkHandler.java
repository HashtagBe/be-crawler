package com.devlib.crawler.handlers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;

import com.devlib.crawler.config.Item;
import com.devlib.crawler.config.Url;
import com.devlib.util.Const;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 取符合标准的&lt;a&gt;元素的href属性值作为新发现的seed。
 * 
 * 这是一个根据“算法”而不是“使用场景”实现的子类。其他的实现可以将本类对象作为一个字段来使用这个“算法”。
 * 
 * @author wenjian.liang
 */
public class SelfLinkHandler extends BaseLinkHandler {
	private static Logger logger = Logger.getLogger(SelfLinkHandler.class);
	private static String href = "href";
	private static final String http = "http";

	@Override
	protected String getLinkFromMatchedElement(final Item item,
			Set<Url> newUrls, final Element element) {
		String link = element.attr(href);
		if (link == null) {
			return null;
		}

		if (!StringUtils.startsWithIgnoreCase(link, http)) {
			link = getAbsUrl(element.baseUri(), link);
		}

		return link;
	}

	/**
	 * hack jsoup Element.absUrl(), if url start with "//", jsoup return wrong
	 * abs url
	 * 
	 * @param baseUri
	 * @param uri
	 * @return
	 */
	public String getAbsUrl(String baseUri, String uri) {
		if (uri == null) {
			return null;
		}
		if (uri.startsWith("//")) {
			uri = uri.substring(1);
		}
		URL base;
		try {
			try {
				base = new URL(baseUri);
			} catch (MalformedURLException e) {
				// the base is unsuitable, but the attribute may be abs on its
				// own, so try that
				URL abs = new URL(uri);
				return abs.toExternalForm();
			}
			// workaround: java resolves '//path/file + ?foo' to '//path/?foo',
			// not '//path/file?foo' as desired
			if (uri.startsWith("?")) {
				uri = base.getPath() + uri;
			}

			URL abs = new URL(base, uri);
			return abs.toExternalForm();
		} catch (MalformedURLException e) {
			return "";
		}
	}

	@Override
	protected String getLinkFromMatchedElement(final Item item,
			final HtmlPage htmlPage, Set<Url> newUrls, final HtmlElement ele) {
		// 逻辑是：先取a的href属性，得到的可能是绝对url也可能是相对url，然后转换成绝对url，
		// 如果转换中抛出异常，返回那个可能是绝对也可能是相对的url
		String link = ele.getAttribute(href);
		if (link == null || link.isEmpty()) {
			return null;
		}

		link = link.trim();
		try {
			return htmlPage.getFullyQualifiedUrl(link).toString();// 把相对路径转成绝对url
		} catch (final MalformedURLException e) {
			logger.error("Error occured while get fully qualified url: " + link
					+ Const.NEW_LINE + e.getMessage());
			return link;
		}
	}

	public static String getAbsoluteLinkUrl(HtmlPage htmlPage,
			HtmlElement urlEle) {
		// 逻辑是：先取a的href属性，得到的可能是绝对url也可能是相对url，然后转换成绝对url，
		// 如果转换中抛出异常，返回那个可能是绝对也可能是相对的url
		String link = urlEle.getAttribute(href);
		if (link == null) {
			return null;
		}

		link = link.trim();
		try {
			return htmlPage.getFullyQualifiedUrl(link).toString();// 把相对路径转成绝对url
		} catch (final MalformedURLException e) {
			logger.error("Error occured while get fully qualified url: " + link
					+ Const.NEW_LINE + e.getMessage());
			return link;
		}
	}

	public static String getAbsoluteLinkUrl(Element urlEle, Item item) {
		// 逻辑是：先取a的href属性，得到的可能是绝对url也可能是相对url，然后转换成绝对url，
		// 如果转换中抛出异常，返回那个可能是绝对也可能是相对的url
		String link = urlEle.attr(href);
		if (link == null) {
			return null;
		}

		link = link.trim();
		try {
			if (link.isEmpty() || !link.startsWith("http")) {
				link = urlEle.absUrl(href);// 取绝对地址
			}
		} catch (final Exception e) {
			logger.error("Error occured while get fully qualified url: " + link
					+ Const.NEW_LINE + e.getMessage());
			return null;
		}

		return link;
	}
}
