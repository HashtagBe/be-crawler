package com.devlib.crawler.crawlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.devlib.crawler.config.Url;
import com.devlib.crawler.handlers.IHtmlPageHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 抓取器类。一个抓取器只抓取一个页面。
 * 
 * @author wenjian.liang
 */
public class HtmlUnitCrawler implements ICrawler {
	private static Logger logger = Logger.getLogger(HtmlUnitCrawler.class);

	int defaultCookieLength = 11;

	@Override
	public CrawlerResult getJobResult(Url seedUrl, IHtmlPageHandler pageHandler) {

		// 这里采用回调模式。本方法负责抓取页面以及最后不要忘了closeAllWindows，至于抓下来的页面如何处理，交由HtmlPageHandler负责。
		if (seedUrl == null || pageHandler == null) {
			return null;
		}

		WebClient webClient = null;
		HtmlPage htmlPage = null;
		try {
			webClient = new WebClient(seedUrl.getBrowserVersion()
					.getBrowserVersion());
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setAppletEnabled(false);
			webClient.getOptions().setTimeout(3000);

			final String urlValue = seedUrl.getValue();

			// 设置cookie，cookie指定则添加指定值，否则随机11位字符串
			// if (null != seedUrl.getCookie() && seedUrl.getCookie().length() >
			// 0) {
			// webClient.addRequestHeader("Cookie", seedUrl.getCookie());
			//
			// } else {
			// webClient.addRequestHeader("Cookie",
			// randomChars(defaultCookieLength));
			// }

			htmlPage = webClient.getPage(urlValue);
			if (htmlPage == null) {
				return null;
			}

			final CrawlerResult jobResult = new CrawlerResult(seedUrl);
			jobResult.setStatusCode(htmlPage.getWebResponse().getStatusCode());

			// 非200状态记录下来
			if (jobResult.getStatusCode() != 200) {
				logger.warn(jobResult.getStatusCode() + ":"
						+ seedUrl.getValue());
			} else {
				logger.debug(jobResult.getStatusCode() + ":"
						+ seedUrl.getValue());
			}

			pageHandler.handle(htmlPage, jobResult);

			// 清理htmlPage占用的资源
			htmlPage.cleanUp();

			return jobResult;
		} catch (OutOfMemoryError e) {
			// 内存溢出的严重错误，程序直接退出！
			logger.error("爬取:" + seedUrl.getValue() + " 页面时内存溢出！"
					+ " \nCause: " + e.getMessage());
			System.err.println(e.getMessage());
			e.printStackTrace();
			logger.error("Error while get the HtmlPage.", e);
			System.exit(-1);
			return null;
		} catch (final Throwable e) {
			logger.error("exception while get page "
					+ seedUrl.getValue() + " \nCause: " + e.getMessage());
			System.err.println(e.getMessage());
			e.printStackTrace();
			logger.error("Error while get the HtmlPage.", e);
			return null;
		} finally {
			// 清理htmlPage占用的资源
			if (htmlPage != null) {
				htmlPage.cleanUp();
				htmlPage = null;
			}

			if (webClient != null) {
				webClient.closeAllWindows();
				webClient = null;
			}
		}

	}

	/**
	 * 取指定位数的a-zA-Z0-9的数字
	 * 
	 * @param n
	 * @return
	 */
	private static String randomChars(int n) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < n; i++) {
			int id = random.nextInt(charBytes.size() - 1);
			sb.append(charBytes.get(id));
		}

		return sb.toString();
	}

	// 随机字符串获取列表
	static List<Character> charBytes = new ArrayList<Character>();
	static {
		/**
		 * 字符列表中添加A-Za-z0-9的字符
		 */
		// a-z A-Z
		for (Integer i = 65; i <= 90; i++) {
			Integer j = i + 32;
			charBytes.add((char) i.byteValue());
			charBytes.add((char) j.byteValue());
		}
		// 0-9
		for (Integer i = 48; i <= 57; i++) {
			charBytes.add((char) i.byteValue());
		}
	}
}
