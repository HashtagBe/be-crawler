package com.devlib.crawler.handlers;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;

import org.jsoup.nodes.Element;

import com.devlib.crawler.config.Item;
import com.devlib.crawler.config.Url;
import com.devlib.util.Const;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 此类为对应于encodeLink类型item的处理器。
 * 
 * 处理诸如URL中含有中文字符类型的URL，对新seed url进行编码。
 * 
 * @author wenjian.liang
 */
public class EncodeLinkHandler extends BaseLinkHandler {

	private final PlainLinkHandler plainLinkHandler = new PlainLinkHandler();

	@Override
	protected String getLinkFromMatchedElement(final Item item,
			final Set<Url> urls, final Element element) {
		String link = plainLinkHandler.getLinkFromMatchedElement(item, urls,
				element);

		return encodeUrl(link, item);
	}

	@Override
	protected String getLinkFromMatchedElement(final Item item,
			final HtmlPage htmlPage, final Set<Url> urls, final HtmlElement ele) {
		String link = plainLinkHandler.getLinkFromMatchedElement(item,
				htmlPage, urls, ele);

		return encodeUrl(link, item);
	}

	private String encodeUrl(String link, Item item) {
		try {
			// 解决参数中的中文编码问题。
			final URL url = new URL(link);
			String query = url.getQuery();
			if (query == null) {
				return link;
			} else {
				query = query.trim();
				if ("".equals(query)) {
					return link;
				}
			}

			// System.out.println("Before:" + query);

			String encode = item.getUrl().getEncode();
			if (encode == null) {
				encode = Const.ENCODING_UTF8;
			} else {
				encode = encode.trim();
				if ("".equals(encode)) {
					encode = Const.ENCODING_UTF8;
				}
			}
			String encodeQuery = URLEncoder.encode(query, encode);

			// 再将query中本来的&和=换回来
			// 那么#,-+这些要不要换呢？
			encodeQuery = encodeQuery.replaceAll("%3D", "=");
			encodeQuery = encodeQuery.replaceAll("%26", "&");

			link = link.replace(query, encodeQuery);
		} catch (final Exception e) {
		}
		return link;
	}
}
