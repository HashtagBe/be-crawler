package com.devlib.crawler.handlers;

import org.jsoup.nodes.Element;

import com.devlib.crawler.config.Item;
import com.devlib.util.Const;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * 对应于text item类型的处理器
 * 
 * 将指定元素的所有文本（包括子元素中的文本）输出到scd的Handler。 对应于TextItem
 * 
 * @author wenjian.liang
 */
public class AllTextHandler extends BaseDataHandler {
	/**
	 * 从单个节点元素中获取应该写到scd文件里的内容，拼接到sb上
	 * 
	 * @param sb
	 * @param elemenet
	 */
	@Override
	public void handleElement(final Item item, final HtmlElement elemenet,
			final StringBuilder sb) {
		String text = getElementText(elemenet);
		text = regexText(text, item);
		sb.append(text).append(Const.NEW_LINE);
	}

	/**
	 * 从单个节点元素中获取应该写到scd文件里的内容。 注：有些时候需要的内容不一定是getTextContent()，因此留这个方法在这里供子类扩展
	 * 
	 * @param element
	 * @return
	 */
	protected String getElementText(final DomNode element) {
		// return element.getTextContent().trim();
		return element.asText().trim();
	}

	@Override
	protected void handleElement(final Item item, final StringBuilder sb,
			final Element element) {
		String text = element.text();
		text = regexText(text, item);
		sb.append(text).append(Const.NEW_LINE);
	}
}
