package com.devlib.crawler.handlers;

import org.jsoup.nodes.Element;

import com.devlib.crawler.config.Item;
import com.devlib.util.Const;
import com.devlib.util.Util;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * 将标签下的文本（不包含子节点的文本）输出到scd文件的handler
 * 
 * @author wenjian.liang
 */
public class DirectTextHandler extends BaseDataHandler {
	@Override
	public void handleElement(final Item item, final HtmlElement ele,
			final StringBuilder sb) {
		// 注：HtmlUnit是按照w3c的标准，某标签下的文本在dom树上也是此标签节点的子元素，节点名字为“#text”
		for (final DomNode h : ele.getChildNodes()) {// 所有子元素
			if (Util.isPureText(h)) {// 纯文本子元素
				final String text = h.asText().trim();
				if (!"".equals(text)) {
					sb.append(regexText(text, item)).append(Const.NEW_LINE);
				}
			}
		}
	}

	@Override
	protected void handleElement(final Item item, final StringBuilder sb,
			final Element element) {
		// 不包括子节点中的文本内容
		String text = element.ownText();
		text = regexText(text, item);
		sb.append(text).append(Const.NEW_LINE);
	}

}
