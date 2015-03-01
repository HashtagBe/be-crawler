package com.devlib.crawler;

import java.util.Set;

import org.apache.log4j.Logger;

import com.devlib.crawler.config.HopCountLimit;
import com.devlib.crawler.config.Page;
import com.devlib.crawler.config.Url;
import com.devlib.crawler.crawlers.CrawlerFactory;
import com.devlib.crawler.crawlers.CrawlerResult;
import com.devlib.crawler.crawlers.ICrawler;
import com.devlib.crawler.handlers.HtmlPageHandlerImpl;
import com.devlib.crawler.handlers.IHtmlPageHandler;
import com.devlib.util.Const;
import com.devlib.util.ExceptionHandler;

/**
 * 任务类。一个对象代表一个url（也就是rule.xml中的一个url标签，包括在item标签下的url标签或者最原始的url标签）、一个页面、
 * 一次页面抓取行动。 此类是Processor类的成员内部类，依赖于一次Processor
 * 
 * @author MaGiCalL
 */
public class CrawlTask implements Runnable {
	private Logger logger = Logger.getLogger(CrawlTask.class);
	/**
	 * 此任务的来源。hopCount=0的来源通常是来自于rule.xml中的url标签，hopCount!=0的通常来自于rule.
	 * xml的item下的url标签
	 */
	private final Url url;

	/**
	 * 抓取的深度，从0开始，由rule.xml里的hotCountLimit指定一个限制。rule根下的url是第0层，url → page →
	 * item里面如果还是url，则递增为1，以此类推。 The hop-count for this URL。
	 */
	private final int hopCount;

	/**
	 * 已重试的次数 The retried times when failed to crawl.
	 */
	private int retry;

	private Processor processor;

	private CrawlRstProcessor crawlRstProcessor;

	public CrawlTask(final Url url, Processor p, CrawlRstProcessor rstProc) {
		this(url, p, rstProc, 0);
	}

	public CrawlTask(final Url url, Processor p, CrawlRstProcessor rstProc,
			final int hopCount) {
		super();
		this.url = url;
		this.processor = p;
		this.hopCount = hopCount;
		this.crawlRstProcessor = rstProc;
	}

	@Override
	public void run() {
		// 抓取
		final CrawlerResult jobResult = crawl();

		if (jobResult == null) {// job result为null，原因很多,应该具体搞一下
			logger.error("URL extracted nothing: " + url.getValue());
			if (retry < Const.DEFAULT_RETRY_TIMES) {
				retry++;
				logger.info("Recrawling of " + url + ", " + retry);
				this.processor.putBack(this);// 抓取失败，再放回去
			} else {
				// 已经失败，放弃，同时标示为已经处理
				this.processor.increaseHandledUrlsCount(1);
			}
			return;
		}

		// 处理抓取结果
		handleResult(jobResult);

		// 记录日志
		this.processor.logStatus();
	}

	private CrawlerResult crawl() {
		// 抓取
		try {
			// 抓取的主要逻辑入口
			return extractDoc(url);// deep.extractDoc(url);
		} catch (final Exception e) {
			ExceptionHandler.handleException(e, logger); 
			return null;
		}
	}

	/**
	 * 去抓取这个页面、分析、检查
	 * 
	 * @param seedUrl
	 *            The url object which we want to crawl from
	 * @return The crawl result
	 * @throws Exception
	 */
	private CrawlerResult extractDoc(final Url seedUrl) {
		final ICrawler crawler = CrawlerFactory.getCrawlerInstance(seedUrl
				.getCrawlerType());
		if (crawler == null)
			throw new RuntimeException("获取crawler类型失败！配置的crawler类型为："
					+ seedUrl.getCrawlerType().toString());

		final Page page = this.processor.getPageById(seedUrl.getPageId());
		IHtmlPageHandler handler = new HtmlPageHandlerImpl(page);
		handler.setConfigParams(this.processor.getParams());
		final CrawlerResult jobResult = crawler.getJobResult(seedUrl, handler);

		if (jobResult == null) {
			return null;
		}

		return jobResult;
	}

	private void handleResult(final CrawlerResult jobResult) {
		// 存入DB
		this.crawlRstProcessor.handleCrawlRst(jobResult);
		// writeSCD(jobResult, urlValue);

		// 追加新找到的url
		addNewUrls(jobResult);

		// 表示完成本url的任务
		this.processor.increaseHandledUrlsCount(1);
	}

	/**
	 * 把这次任务新发现的url作为新的seed放到任务集合里去
	 * 
	 * @param jobResult
	 */
	private void addNewUrls(final CrawlerResult jobResult) {
		int hopCountLimit = Integer.MAX_VALUE;
		HopCountLimit hcl = this.processor.getHopCountLimit();
		if (hcl != null)
			hopCountLimit = hcl.get();
		final Set<Url> urls = jobResult.getURLs();
		if (urls != null && !urls.isEmpty()) {
			try {
				for (final Url newSeed : urls) {
					if (this.processor.containsHandledUrl(newSeed)) {
						this.processor.increaseHandledUrlsCount(1);
						logger.info("Pass duplicate item, discoverd when crawl.the url:"
								+ newSeed);
					} else {
						// check if the url is already crawled
						String url = newSeed.getValue();
						if (this.crawlRstProcessor.isUrlCrawled(url)) {
							continue;
						}

						final int newHopCount = hopCount + 1;
						if (newHopCount <= hopCountLimit) {
							this.processor.addNewTask(new CrawlTask(newSeed,
									this.processor, this.crawlRstProcessor,
									newHopCount));
						}
					}
				}
			} catch (final Exception e) {
				ExceptionHandler.handleException(e, logger);
			}
		}
	}

	public Url getUrl() {
		return url;
	}
}
