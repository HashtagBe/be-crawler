package com.hashtag.service;

import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.devlib.crawler.Processor;
import com.devlib.util.ExceptionHandler;
import com.hashtag.dao.ArticleDAO;
import com.hashtag.dao.TopicDAO;
import com.hashtag.dao.WebsiteDAO;
import com.hashtag.domain.Website;

public class CrawlerThread extends Thread {
	private static Logger logger = Logger.getLogger(CrawlerThread.class);

	public static final String siteSuffix = "_sites";
	public static final String articleSuffix = "_articles";
	private long reCrawlIntervalTime;
	private File configDir;
	private long checkInterval = 60000;
	private long lastCrawlTime;
	private long siteFileLastModifyTime;
	private HashSet<File> articleFiles;

	private ArticleDAO articleDao;
	private TopicDAO topicDao;
	private WebsiteDAO wsDao;
	private ArticleSaver crp;

	public CrawlerThread(long fullTimeInterval, long incrementalTimeInterval, File configDir) {
		this.setDaemon(true);
		this.reCrawlIntervalTime = fullTimeInterval;
		this.checkInterval = incrementalTimeInterval;
		this.configDir = configDir;
		this.articleFiles = new HashSet<File>();
	}

	@Override
	public void run() {
		this.crp = new ArticleSaver(this.articleDao, this.topicDao, this.wsDao);
		while (true) {
			crawlAll();
			crawlNew();
			try {
				Thread.sleep(this.checkInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * crawl new added xml configuration files
	 */
	private void crawlNew() {
		logger.info("begin incremental crawling process...");
		importSites();
		File[] files = this.configDir.listFiles();
		for (File f : files) {
			if (f.getName().indexOf(articleSuffix) > -1) {
				if (articleFiles.add(f)) {
					try {
						crawlSite(f);
					} catch (Exception e) {
						ExceptionHandler.handleException(e, logger);
					}
				}
			}
		}

		logger.info("incremental crawling process is finished...");
	}

	private void importSites() {
		File[] files = this.configDir.listFiles();
		File sitesFile = null;
		for (File f : files) {
			if (f.getName().indexOf(siteSuffix) > -1) {
				sitesFile = f;
				break;
			}
		}

		if (sitesFile != null) {
			long lastModifyTime = sitesFile.lastModified();
			if (lastModifyTime == this.siteFileLastModifyTime)
				return;

			this.siteFileLastModifyTime = lastModifyTime;
			final SAXReader saxReader = new SAXReader();
			try {
				Document doc = saxReader.read(new FileReader(sitesFile));
				Element root = doc.getRootElement();
				List<Element> siteNodes = root.elements("site");
				for (Element site : siteNodes) {
					String name = site.attributeValue("name");
					String domainUrl = site.attributeValue("domainUrl");
					Website ws = new Website();
					ws.setDomainUrl(domainUrl);
					ws.setName(name);
					try {
						int id = this.wsDao.isWebsiteExist(ws);
						if (id < 1)
							this.wsDao.insertWebsite(ws);
					} catch (Exception e) {
						ExceptionHandler.handleException(e, logger);
					}
				}
			} catch (Exception e) {
				ExceptionHandler.handleException(e, logger);
			}
		}
	}

	/**
	 * crawl all sites articles
	 */
	private void crawlAll() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - this.lastCrawlTime) < this.reCrawlIntervalTime)
			return;

		logger.info("begin full crawling process...");
		importSites();

		File[] files = this.configDir.listFiles();
		for (File f : files) {
			if (f.getName().indexOf(articleSuffix) > -1) {
				articleFiles.add(f);
				try {
					crawlSite(f);
				} catch (Exception e) {
					ExceptionHandler.handleException(e, logger);
				}
			}
		}

		this.lastCrawlTime = System.currentTimeMillis();

		logger.info("full crawling process is finished...");
	}

	private void crawlSite(File f) throws InterruptedException {
		logger.info("Begin crawling website config file: "
				+ f.getAbsolutePath());
		String fileName = f.getName();
		int i = fileName.indexOf(articleSuffix);
		String siteName = fileName.substring(0, i);
		int siteId = this.wsDao.getSiteIdByName(siteName);
		this.crp.setSiteId(siteId);

		Processor p = new Processor(f.getAbsolutePath(), 3);
		p.setCrawlRstProcessor(this.crp);
		p.start();

		while (!p.isCrawlFinished())
			Thread.sleep(2000);

		logger.info("Finish crawling website config file: "
				+ f.getAbsolutePath());
	}

	public void setSiteDAO(WebsiteDAO siteDao) {
		this.wsDao = siteDao;
	}

	public void setArticleDAO(ArticleDAO articleDao) {
		this.articleDao = articleDao;
	}

	public void setTopicDAO(TopicDAO topicDao) {
		this.topicDao = topicDao;
	}
}
