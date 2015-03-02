package com.hashtag.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.util.ExceptionHandler;
import com.hashtag.dao.ArticleDAO;
import com.hashtag.dao.TopicDAO;
import com.hashtag.dao.WebsiteDAO;

@Service
public class CrawlerService {
	private static Logger logger = Logger.getLogger(CrawlerService.class);
	/**
	 * xml configuration file directory to store xml crawling configuration
	 * files
	 */
	private File configFileDir;

	private CrawlerThread crawlerThread;

	@Autowired
	private WebsiteDAO siteDao;
	@Autowired
	private ArticleDAO articleDao;
	@Autowired
	private TopicDAO topicDao;

	/**
	 * the default re-crawling time interval, 12 hours
	 */
	private long recrawlIntervalTime = 12 * 3600000;
	
	/**
	 * the default incremental check interval time
	 */
	private long checkIntervalTime = 60000;

	public synchronized void startDaemonThread() {
		if (this.configFileDir == null) {
			Properties prpt = new Properties();
			try {
				prpt.load(new FileReader(Resources
						.getResourceAsFile("config/config.properties")));
				String s = prpt.getProperty("xmlFileDirectory");
				this.configFileDir = new File(s);

				int hours = Integer.parseInt(prpt.getProperty("reCrawlInterval"));
				this.recrawlIntervalTime = hours * 3600000;
				hours = Integer.parseInt(prpt.getProperty("increCrawlInterval"));
				this.checkIntervalTime = hours * 3600000;
			} catch (IOException e) {
				ExceptionHandler.handleException(e, logger);
			}
		}

		if (crawlerThread == null) {
			this.crawlerThread = new CrawlerThread(this.recrawlIntervalTime,
					this.checkIntervalTime, this.configFileDir);
			this.crawlerThread.setSiteDAO(this.siteDao);
			this.crawlerThread.setArticleDAO(this.articleDao);
			this.crawlerThread.setTopicDAO(this.topicDao);
			this.crawlerThread.start();
		}
	}
}
