package com.hashtag.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.devlib.crawler.CrawlRstProcessor;
import com.devlib.crawler.crawlers.CrawlerResult;
import com.devlib.util.ExceptionHandler;
import com.hashtag.dao.ArticleDAO;
import com.hashtag.dao.TopicDAO;
import com.hashtag.dao.WebsiteDAO;
import com.hashtag.domain.Article;
import com.hashtag.domain.Topic;

/**
 * Save articles to DB
 * 
 * @author guangfeng jin
 *
 */
public class ArticleSaver implements CrawlRstProcessor {
	private static Logger logger = Logger.getLogger(ArticleSaver.class);

	public static final String url = "url";
	public static final String title = "title";
	public static final String subTitle = "subTitle";
	public static final String imageUrls = "imageUrls";
	public static final String authors = "authors";
	public static final String paragraphs = "paragraphs";
	public static final String topic = "topic";
	public static final String subTopic = "subTopic";
	public static final String rawtopic = "rawTopic";
	public static final String rawsubtopic = "rawSubTopic";
	public static final String rawtopic_fromurl = "rawTopicFromUrl";
	public static final String rawsubtopic_fromurl = "rawSubTopicFromUrl";
	public static final String date = "date";
	public static final String crawldate = "crawlDate";

	private ArticleDAO articleDao;
	private TopicDAO topicDao;
	private WebsiteDAO wsDao;
	private int siteId;

	private Pattern yearPatn, dayPatn;
	private HashMap<String, Integer> monthSet;

	public ArticleSaver(ArticleDAO ad, TopicDAO td, WebsiteDAO wd) {
		this.articleDao = ad;
		this.topicDao = td;
		this.wsDao = wd;
		initDatePatns();
	}

	private void initDatePatns() {
		yearPatn = Pattern.compile("\\d{4}");
		this.dayPatn = Pattern.compile("\\d{2}");
		this.monthSet = new HashMap<String, Integer>();
		monthSet.put("jan", 0);
		monthSet.put("feb", 1);
		monthSet.put("mar", 2);
		monthSet.put("apr", 3);
		monthSet.put("may", 4);
		monthSet.put("jun", 5);
		monthSet.put("jul", 6);
		monthSet.put("aug", 7);
		monthSet.put("sep", 8);
		monthSet.put("oct", 9);
		monthSet.put("nov", 10);
		monthSet.put("dec", 11);
	}

	@Override
	public void handleCrawlRst(CrawlerResult rst) {
		try {
			Map<String, String> scdMap = rst.getScdContent();
			if (scdMap.size() == 0)// link url result, ignore
				return;

			Article a = new Article();
			// url
			String s = rst.getUrlValue();
			a.setUrl(s);
			// title
			s = scdMap.get("title");
			if (s == null || s.trim().length() == 0)
				return;
			a.setTitle(s);
			// subtitle
			s = scdMap.get(subTitle);
			a.setSubTitle(s);
			// imageUrl
			s = scdMap.get(imageUrls);
			a.setImageUrls(s);
			// author
			s = scdMap.get(authors);
			a.setAuthors(s);
			// paragraphs
			s = scdMap.get(paragraphs);
			if (s == null || s.trim().length() == 0)
				return;
			a.setParagraphs(s);

			// rawTopic
			this.processRawTopic(a, scdMap);
			// date
			s = scdMap.get(date);
			if (s != null) {
				long time = parseDate(s);
				a.setDate(new Timestamp(time));
			}
			a.setCrawlDate(new Timestamp(System.currentTimeMillis()));

			a.setSiteId(this.siteId);

			this.articleDao.insertArticle(a);
		} catch (Exception e) {
			ExceptionHandler.handleException(e, logger);
		}
	}

	private void processRawTopic(Article a, Map<String, String> scdMap) {
		String rawTopic = scdMap.get(rawtopic);
		if (rawTopic != null) {
			String rawFromUrl = scdMap.get(rawtopic_fromurl);
			Topic t = new Topic();
			t.setName(rawTopic);
			t.setSiteId(this.siteId);
			t.setFromUrl(rawFromUrl);
			int topicId = this.topicDao.isTopicExist(t);
			if (topicId < 1) {
				// topic is not exist, insert
				topicId = this.topicDao.insertTopic(t);
			}

			String rawSubTopic = scdMap.get(rawsubtopic);
			if (rawSubTopic != null) {
				t.setName(rawSubTopic);
				t.setParentId(topicId);
				int subTopicId = this.topicDao.isTopicExist(t);
				if (subTopicId < 1) {
					// subTopic is not exist, insert
					subTopicId = this.topicDao.insertTopic(t);
				}
				a.setTopicId(subTopicId);
			} else {
				a.setTopicId(topicId);
			}
		}
	}

	@Override
	public boolean isUrlCrawled(String url) {
		try {
			Article a = new Article();
			a.setUrl(url);
			int i = this.articleDao.isArticleExist(a);
			if (i > 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			ExceptionHandler.handleException(e, logger);
			return false;
		}
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public long parseDate(String s) {
		long crawlTime = System.currentTimeMillis();
		try {
			long time = Long.parseLong(s);
			if (this.verifyDate(time))
				return time;
			else
				return crawlTime;
		} catch (Exception e) {
			try {
				int monthIdx = -1;
				s = s.toLowerCase();
				for (String month : this.monthSet.keySet()) {
					if (s.indexOf(month) > -1) {
						monthIdx = this.monthSet.get(month);
						Matcher m = this.yearPatn.matcher(s);
						if (m.find()) {
							String yearStr = m.group();
							int year = Integer.parseInt(yearStr);

							m = this.dayPatn.matcher(s);
							if (m.find()) {
								String dayStr = m.group();
								int day = Integer.parseInt(dayStr);
								if (day < 32) {
									Calendar c = new GregorianCalendar();
									c.set(Calendar.MONTH, monthIdx);
									c.set(Calendar.YEAR, year);
									c.set(Calendar.DAY_OF_MONTH, day);
									if (this.verifyDate(c.getTimeInMillis()))
										return c.getTimeInMillis();
								}
							}
						}
					}
				}
			} catch (Exception e1) {
			}
		}

		return crawlTime;
	}

	private boolean verifyDate(long time) {
		Calendar c1 = new GregorianCalendar();
		c1.setTimeInMillis(time);
		Calendar c2 = new GregorianCalendar();

		int year1 = c1.get(Calendar.YEAR);
		int year2 = c2.get(Calendar.YEAR);

		if (Math.abs(year1 - year2) > 3)
			return false;
		else
			return true;
	}
}
