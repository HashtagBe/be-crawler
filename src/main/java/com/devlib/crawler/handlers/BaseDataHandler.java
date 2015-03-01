package com.devlib.crawler.handlers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;

import com.devlib.crawler.config.ConfigParams;
import com.devlib.crawler.config.Content;
import com.devlib.crawler.config.Item;
import com.devlib.crawler.crawlers.CrawlerResult;
import com.devlib.util.Const;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * ItemHandler的一个基类处理器，本系列的类用来处理rule > page > item >
 * property分支，即数据类型的模块。会从指定的元素中抓取符合条件的数据，最终写入到scd文件中。
 * 
 * @author wenjian.liang
 */
public abstract class BaseDataHandler implements ItemHandler {
	private static Logger logger = Logger.getLogger(BaseDataHandler.class);
	protected ConfigParams params;

	@Override
	public boolean handle(final Item item, final HtmlPage htmlPage,
			final CrawlerResult jobResult) {
		// 取到指定的html元素。其实这一步应该在调用者处做。
		final List<HtmlElement> elements = item.getElementList(htmlPage);
		// item > property > xx
		final String scdName = item.getProperty().getScd().getScdName();

		if (elements == null || elements.size() == 0) {
			// 如果这些东西为空，没取到结果，返回
			jobResult.addScdContent(scdName, null);
			logger.error("TextItem got " + scdName
					+ " none content in " + jobResult.getUrlValue());
			return false;
		} else {
			// 从指定的节点元素中获取应该写到scd文件里的内容
			String scdContent = getContent(item, htmlPage, elements, jobResult);
			if (scdContent == null) {
				// 如果这些东西为空，没取到结果，返回
				jobResult.addScdContent(scdName, null);
				return false;
			}
			scdContent = scdContent.trim();
			if (scdContent.equals("")) {
				// 如果这些东西为空，没取到结果，返回
				jobResult.addScdContent(scdName, null);
				return false;
			}

			jobResult.addScdContent(scdName, scdContent);

			return true;
		}
	}

	@Override
	public void setConfigParams(ConfigParams cp) {
		// keep the config param instance;
		this.params = cp;
	}

	/**
	 * 从指定的节点元素中获取应该写到scd文件里的内容
	 * 
	 * @param item
	 * @param eles
	 * @return
	 */
	protected String getContent(final Item item, final HtmlPage htmlPage,
			final List<HtmlElement> eles, final CrawlerResult jobResult) {
		final StringBuilder sb = new StringBuilder();
		for (final HtmlElement ele : eles) {
			handleElement(item, ele, sb);
		}
		return sb.toString();
	}

	@Override
	public void handleElement(Item item, HtmlElement element, StringBuilder sb) {
		String scdName = item.getProperty().getScd().getScdName();
		sb.append("<").append(scdName).append(">");
		String xpath = item.getXpath();
		List<HtmlElement> el = (List<HtmlElement>) element.getByXPath(xpath);
		if (null == el || el.size() == 0) {
			sb.append(Const.NEW_LINE);
			return;
		}
		String value = el.get(0).asText().trim();

		if (StringUtils.isEmpty(value) || "".equals(value.trim())) {
			return;
		}

		sb.append(value).append(Const.NEW_LINE);
	}

	/**
	 * 从指定的节点元素中获取应该写到scd文件里的内容
	 * 
	 * @param item
	 * @param qtHtmlPage
	 * @param elements
	 * @param jobResult
	 * @return
	 */
	protected String getContent(final Item item,
			final List<Element> elementList, final CrawlerResult jobResult) {
		final StringBuilder sb = new StringBuilder();

		if (elementList != null && !elementList.isEmpty()) {
			for (Element element : elementList) {
				handleElement(item, sb, element);
			}
		}

		return sb.toString();
	}

	/**
	 * 从单个节点元素中获取应该写到scd文件里的内容，拼接到sb上
	 * 
	 * @param item
	 * @param sb
	 * @param elemenet
	 */
	protected void handleElement(final Item item, final StringBuilder sb,
			final Element elemenet) {
		// default do nothing
	}

	/**
	 * 对文本内容进行匹配和抽取
	 * 
	 * @param text
	 * @param item
	 * @return
	 */
	protected String regexText(String text, Item item) {
		try {
			Content content = item.getProperty().getContent();
			Pattern pattern = content.getPattern();
			Matcher matcher = pattern.matcher(text);
			if (matcher.find()) {
				return matcher.group();
			} else {
				// return text;
				return "";
			}
		} catch (Exception e) {
			return text;
		}
	}

}