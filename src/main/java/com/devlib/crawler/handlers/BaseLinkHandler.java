package com.devlib.crawler.handlers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

import com.devlib.crawler.config.ConfigParams;
import com.devlib.crawler.config.Item;
import com.devlib.crawler.config.Url;
import com.devlib.crawler.crawlers.CrawlerResult;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 链接类型处理器的一个抽象基类实现，本系列的类用来处理rule > page > item >
 * url分支，即链接类型的模块。会从指定的元素中发现链接，作为新的seed url，最终会加入到任务队列中去。
 * 
 * @author wenjian.liang
 */
public abstract class BaseLinkHandler implements ItemHandler {
	protected ConfigParams params;

	@Override
	public boolean handle(final Item item, final HtmlPage htmlPage,
			final CrawlerResult jobResult) {
		// url的集合
		Set<Url> newUrls = new HashSet<Url>();

		// 依据item的配置参数，获取页面的特定对象
		final List<HtmlElement> eles = item.getElementList(htmlPage);
		if (eles == null || eles.isEmpty()) {
			return false;
		}

		// 针对这些对象，做定制化处理
		getLinksFromMatchedElements(item, htmlPage, newUrls, eles);

		// 加入结果集
		jobResult.addAllUrls(newUrls);

		return !newUrls.isEmpty();
	}

	@Override
	public void setConfigParams(ConfigParams cp) {
		// keep the config param instance;
		this.params = cp;
	}

	/**
	 * 获取链接地址列表
	 * 
	 * @param item
	 *            页面配置的item元素
	 * @param doc
	 *            Webkit抓取的页面对象
	 * @param newUrls
	 *            新url存储集合对象
	 * @param eles
	 *            链接地址元素集合
	 */
	protected void getLinksFromMatchedElements(Item item, Set<Url> newUrls,
			List<Element> elementList) {
		if (elementList == null || elementList.isEmpty()) {
			return;
		}

		for (Element element : elementList) {
			String link = getLinkFromMatchedElement(item, newUrls, element);
			if (link == null) {
				continue;
			}
			// 检验生成的link是否符合规定
			final String finalLink = validate(item, link);
			if (finalLink == null) {
				continue;
			}
			// 创建一个新的url对象，这个对象最终会添加到任务队列后面等待分析
			final Url newUrl = buildUrlLink(item, finalLink);
			newUrls.add(newUrl);
		}
	}

	/**
	 * 获取链接地址列表
	 * 
	 * @param item
	 *            页面配置的item元素
	 * @param doc
	 *            Webkit抓取的页面对象
	 * @param newUrls
	 *            新url存储集合对象
	 * @param ele
	 *            链接地址元素集合
	 * @return
	 */
	protected abstract String getLinkFromMatchedElement(Item item,
			Set<Url> newUrls, Element ele);

	/**
	 * 从指定的元素列表中解析出url来，放到urls里去
	 * 
	 * @param item
	 * @param htmlPage
	 * @param newUrls
	 * @param eles
	 */
	protected void getLinksFromMatchedElements(final Item item,
			final HtmlPage htmlPage, final Set<Url> newUrls,
			final List<HtmlElement> eles) {
		for (final HtmlElement ele : eles) {
			String link = getLinkFromMatchedElement(item, htmlPage, newUrls,
					ele);
			if (link == null) {
				continue;
			}

			// 检验生成的link是否符合规定
			final String finalLink = validate(item, link);
			if (finalLink == null) {
				continue;
			}

			// 创建一个新的url对象，这个对象最终会添加到任务队列后面等待分析
			try {
				Url newUrl = buildSeedUrl(item.getUrl(), finalLink);
				// 当配置了url > param,则对param进行获取如果为空则返回false

				newUrls.add(newUrl);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 从指定的元素列表中解析出url返回。 注：有时候ele并非直接是一个带链接的元素，或者根据需求， 需要取的是ele的子元素的链接列表
	 * 
	 * @param item
	 * @param htmlPage
	 * @param ele
	 * @return
	 */
	protected abstract String getLinkFromMatchedElement(final Item item,
			final HtmlPage htmlPage, Set<Url> newUrls, final HtmlElement ele);

	public static Url buildUrlLink(Item item, String urlStr) {
		item.getUrl().setValue(urlStr);
		return item.getUrl().clone();
	}

	protected Url buildSeedUrl(final Url url, final String finalLink)
			throws CloneNotSupportedException {
		final Url newUrl = new Url();
		newUrl.setValue(finalLink);
		newUrl.setPageId(url.getPageId());
		newUrl.setExecuteJs(url.getExecuteJs());
		newUrl.setAjaxCall(url.getAjaxCall());
		newUrl.setBrowserVersion(url.getBrowserVersion());
		newUrl.setCrawlerType(url.getCrawlerType().toString());
		newUrl.setWaitTime(url.getWaitTime());
		newUrl.setCookie(url.getCookie());

		return newUrl;
	}

	/**
	 * 验证新生成的link是否符合配置的regex
	 * 
	 * @param item
	 * @param link
	 * @return
	 */
	public static boolean checkRegex(final Item item, final String link) {
		final Url url = item.getUrl();
		final Pattern regex = url.getRegex();
		return regex == null || regex.matcher(link).matches();
	}

	/**
	 * 检验生成的link是否符合规定
	 * 
	 * @param item
	 * @param link
	 * @return
	 */
	public static String validate(final Item item, final String link) {
		if (!checkRegex(item, link)) {
			return null;
		}

		return link;
	}

	@Override
	public void handleElement(Item item, HtmlElement element,
			StringBuilder result) {
		// TODO Auto-generated method stub

	}
}
