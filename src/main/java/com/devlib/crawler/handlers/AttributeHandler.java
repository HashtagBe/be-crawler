package com.devlib.crawler.handlers;

import org.jsoup.nodes.Element;

import com.devlib.crawler.config.Item;
import com.devlib.util.Const;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * 将指定元素的某个属性的值输出到scd文件的Handler。
 * 
 * @author wenjian.liang
 */
public class AttributeHandler extends BaseDataHandler {
	@Override
	public void handleElement(final Item item, final HtmlElement elemenet,
			final StringBuilder sb) {
		sb.append(
				elemenet.getAttribute(item.getProperty().getAttr()
						.getAttrName())).append(Const.NEW_LINE);
	}

	@Override
	protected void handleElement(final Item item, final StringBuilder sb,
			final Element element) {
		sb.append(element.attr(item.getProperty().getAttr().getAttrName()))
				.append(Const.NEW_LINE);
	}
}
