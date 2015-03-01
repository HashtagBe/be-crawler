package com.devlib.crawler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.devlib.crawler.config.ConfigParams;
import com.devlib.crawler.config.HopCountLimit;
import com.devlib.crawler.config.Page;
import com.devlib.crawler.config.Rule;
import com.devlib.crawler.config.Url;
import com.devlib.util.Const;
import com.devlib.util.ExceptionHandler;
import com.devlib.util.Util;
import com.devlib.util.XmlParser;

/**
 * 此类代表一次大的爬网页行动。通常一次这样的行动是针对同一个网站的，对应于一个rule.xml
 * 
 * @author wenjian.liang
 */
public class Processor {
	private Logger logger = Logger.getLogger(Processor.class);

	/**
	 * url池
	 */
	private final ConcurrentLinkedQueue<CrawlTask> unhandledUrls = new ConcurrentLinkedQueue<CrawlTask>();
	/**
	 * “正在搞”和“已搞”的url
	 */
	private final Set<String> handledUrls = new ConcurrentSkipListSet<String>();

	/**
	 * 多线程执行时的线程池
	 */
	private ThreadPoolExecutor scheduler;

	/**
	 * 已经处理的url的计数
	 */
	private final AtomicInteger handledUrlsCount = new AtomicInteger(0);

	/**
	 * rule.xml的路径
	 */
	private final String ruleFilePath;

	/**
	 * 解析rule.xml生成的Rule对象
	 */
	private Rule rule;

	private ConfigParams params;

	private CrawlRstProcessor rstProcessor;
	/**
	 * 任务是否使用多线程。默认是多线程
	 */
	private boolean multiThread = true;
	private final int maxThreadCount;

	private boolean isFinished;

	/**
	 * 准备一次任务流程，最大线程数使用默认值{@link Const.DEFUALT_MAX_THREAD_COUNT}
	 * 
	 * @param ruleFilePath
	 *            rule.xml路径
	 */
	public Processor(final String ruleFilePath) {
		this(ruleFilePath, Const.DEFUALT_MAX_THREAD_COUNT);//
	}

	/**
	 * 准备一次任务流程
	 * 
	 * @param ruleFilePath
	 *            rule.xml路径
	 * @param maxThreadCount
	 *            最大线程数
	 */
	public Processor(final String ruleFilePath, final int maxThreadCount) {
		this.ruleFilePath = ruleFilePath;
		multiThread = maxThreadCount > 1;
		this.maxThreadCount = maxThreadCount;
	}

	private boolean init() {
		// 解析rule.xxx.xml
		try {
			final XmlParser xmlParser = new XmlParser();
			xmlParser.setPackageName(Const.RULE_XML_NODE_CLASSES_PACKAGE_NAME);
			final Map<String, String> map = new HashMap<String, String>();
			xmlParser.setNodeNameClassNameMap(map);

			rule = xmlParser.parse(ruleFilePath);
			final Collection<Url> urls = rule.getUrls();
			assert !urls.isEmpty();// url若是没有，xml肯定有问题

			this.params = new ConfigParams();
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return false;
		}

		// 构建seedUrlMap
		final Collection<Url> urls = rule.getUrls();
		Collection<Url> actualUrls = Util.expandUrls(urls);
		for (final Url url : actualUrls) {
			unhandledUrls.add(new CrawlTask(url, this, this.rstProcessor));
		}

		scheduler = new ThreadPoolExecutor(0, maxThreadCount, 10,
				TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());// 旧版参数：0,
																		// 60,
																		// 30,
																		// TimeUnit.SECONDS

		return true;
	}

	public void start() {
		if (!init()) {
			this.isFinished = true;
			return;
		}

		try {
			// 任务池为空表示现在没有任务了，但是过一会儿可能还有线程会往里放新任务。
			// 当还有任务在执行时，scheduler.getActiveCount()肯定不为0.
			// 当所有任务执行完毕，unhandledUrls必为空, scheduler.getActiveCount()也变为0
			// 闲置时间即ThreadPoolExecutor构造方法的3、4参数决定
			while (this.unhandledUrls.size() > 0
					|| this.scheduler.getPoolSize() > 0) {// 对比已处理的url数和总共的url数
				final CrawlTask task = unhandledUrls.poll();
				if (task != null) {// 在多线程的情况下，虽然前面判断了!seedUrlMap.isEmpty()，这里依然要判断一下取到的东西是否为null
					if (handledUrls.add(task.getUrl().getValue())) {// 加入到urlDB中成功，说明还没有人开始搞这个任务
						if (multiThread) {
							try {
								// 开一个新线程去执行。这一句太渺小了，很容易就没看见。
								scheduler.submit(task);
							} catch (final RejectedExecutionException e) {
								// 如果任务被拒绝，把它重新放回任务列表中去，并且从“正在搞”里面移除
								putBack(task);
							}
						} else {
							// 单线程地执行
							task.run();
						}
					} else {
						// 进来这里是一种同步时的冲突情况或者某个物品在该网站有多个分类入口
						handledUrlsCount.incrementAndGet();
						logger.info("Pass duplicate item, someone is handling or has handled it.the url:"
								+ task.getUrl());
					}
				}
			}// while
			scheduler.shutdown();
			scheduler.awaitTermination(10, TimeUnit.SECONDS);
		} catch (final Throwable e) {
			ExceptionHandler.handleException(e, logger);
		}
		logger.info("job finished.");
		this.isFinished = true;
	}

	public void setCrawlRstProcessor(CrawlRstProcessor processor) {
		this.rstProcessor = processor;
	}

	public boolean isCrawlFinished() {
		return this.isFinished;
	}

	/**
	 * 将任务从“已搞”中删除，并放回任务池中，等待之后重新来过。
	 * 
	 * @param task
	 */
	void putBack(final CrawlTask task) {
		if (handledUrls.remove(task.getUrl().getValue())) {
			unhandledUrls.add(task);
		}
	}

	/**
	 * 获取当前是否多线程执行
	 * 
	 * @return
	 */
	public boolean getMultiThread() {
		return multiThread;
	}

	/**
	 * 设置此后是否多线程执行
	 * 
	 * @param multiThread
	 */
	public void setMultiThread(final boolean multiThread) {
		this.multiThread = multiThread;
	}

	int increaseHandledUrlsCount(int count) {
		return this.handledUrlsCount.addAndGet(count);
	}

	Page getPageById(String pageId) {
		return this.rule.getPageById(pageId);
	}

	ConfigParams getParams() {
		return this.params;
	}

	HopCountLimit getHopCountLimit() {
		return this.rule.getHopCountLimit();
	}

	boolean containsHandledUrl(Url newSeed) {
		return this.handledUrls.contains(newSeed.getValue());
	}

	void addNewTask(CrawlTask crawlTask) {
		this.unhandledUrls.add(crawlTask);
	}

	void logStatus() {
		logger.debug("handled urls count: " + this.handledUrlsCount
				+ ", unhandled url count: " + this.unhandledUrls.size());
	}
}
