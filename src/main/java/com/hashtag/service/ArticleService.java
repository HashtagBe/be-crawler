package com.hashtag.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.util.ExceptionHandler;
import com.hashtag.dao.ArticleDAO;
import com.hashtag.dao.TopicDAO;
import com.hashtag.domain.Article;
import com.hashtag.domain.Topic;
import org.apache.commons.lang3.StringEscapeUtils;

@Service
public class ArticleService {
	private static Logger logger = Logger.getLogger(ArticleService.class);

	@Autowired
	private ArticleDAO articleDao;
	@Autowired
	private TopicDAO topicDao;

	public int insertArticle(Article a) {
		try {
			int id = this.articleDao.isArticleExist(a);
			if (id > 0)
				return id;

			return this.articleDao.insertArticle(a);
		} catch (Exception e) {
			ExceptionHandler.handleException(e, logger);
			return -1;
		}
	}

	public List<Article> getArticleList(long startTime, long endTime, int status) {
		if (startTime >= endTime)
			return new LinkedList<Article>();
		try {
			return this.articleDao.getArticleList(startTime, endTime, status);
		} catch (Exception e) {
			ExceptionHandler.handleException(e, logger);
			return null;
		}
	}

	public int getArticleCount(long startTime, long endTime, int status) {
		if (startTime >= endTime)
			return 0;

		try {
			return this.articleDao.getArticleCount(startTime, endTime, status);
		} catch (Exception e) {
			ExceptionHandler.handleException(e, logger);
			return -1;
		}
	}

	/**
	 * Convert the DB records of article instance to the json object list
	 * 
	 * @param articleList
	 * @return
	 */
	public List<String> convert(List<Article> articleList) {
		List<String> rst = new LinkedList<String>();
		for (Article a : articleList) {
			rst.add(this.convert(a));
		}

		return rst;
	}

        private String escape(String input) {
            return StringEscapeUtils.escapeJson(input);
        }
        
	public String convert(Article a) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");

		sb.append(this.assembleNameValue("s", a.getUrl())).append(",");
		sb.append(this.assembleNameValue("t", escape(a.getTitle()))).append(",");
		sb.append(this.assembleNameValue("st", escape(a.getSubTitle()))).append(",");
		// image url list
		String s = a.getImageUrls();
		if (s != null) {
			String[] urls = s.split(",");
			List<String> urlList = new LinkedList<String>();
			for (String url : urls) {
				if (url.startsWith("http"))
					;
				urlList.add(url);
			}
			sb.append(this.assembleNameValue("i", urlList));
		} else {
			sb.append(this.assembleNameValue("i", null));
		}
		sb.append(",");

		// author
		s = a.getAuthors();
		if (s != null) {
			String[] authors = s.split("\\n");
			List<String> authorList = new LinkedList<String>();
			for (String author : authors) {
				if (author.trim().length() > 0)
					authorList.add(escape(author));
			}
			sb.append(this.assembleNameValue("a", authorList));
		} else {
			sb.append(this.assembleNameValue("a", null));
		}
		sb.append(",");

		// paragraph
		s = a.getParagraphs();
		if (s != null) {
			String[] paragraphs = s.split("\\n");
			List<String> paraList = new LinkedList<String>();
			for (String para : paragraphs) {
				if (para.trim().length() > 0)
					paraList.add(escape(para));
			}
			sb.append(this.assembleNameValue("p", paraList));
		} else {
			sb.append(this.assembleNameValue("p", null));
		}
		sb.append(",");

		// date
		if (a.getDate() != null)
			sb.append(this.assembleNameValue("D", a.getDate().getTime()));
		else
			sb.append(this.assembleNameValue("D", null));
		sb.append(",");

		// crawl date
		sb.append(this.assembleNameValue("CD", a.getCrawlDate().getTime()));
		sb.append(",");

		// topic
		int tId = a.getTopicId();
		if (tId > 0) {
			Topic t1 = this.topicDao.getTopic(tId);
			if (t1 != null) {
				String n1 = t1.getName();
				if (t1.getParentId() > 0) {
					sb.append(this.assembleNameValue("S", n1));

					Topic t2 = this.topicDao.getTopic(t1.getParentId());
					if (t2 != null)
						sb.append(this.assembleNameValue("T", t2.getName()))
								.append(",");
					else
						sb.append(this.assembleNameValue("T", null))
								.append(",");
				} else {
					sb.append(this.assembleNameValue("T", n1)).append(",");
					sb.append(this.assembleNameValue("S", null)).append(",");
				}
			}
		} else {
			sb.append(this.assembleNameValue("T", null)).append(",");
			sb.append(this.assembleNameValue("S", null)).append(",");
		}

		// raw topic
		int rtId = a.getRawTopicId();
		if (rtId > 0) {
			Topic t1 = this.topicDao.getTopic(rtId);
			if (t1 != null) {
				String n1 = t1.getName();
				String fu1 = t1.getFromUrl();
				if (t1.getParentId() > 0) {
					sb.append(this.assembleNameValue("RS", n1)).append(",");
					sb.append(this.assembleNameValue("SU", fu1)).append(",");
					Topic t2 = this.topicDao.getTopic(t1.getParentId());
					if (t2 != null) {
						sb.append(this.assembleNameValue("RT", t2.getName()))
								.append(",");
						sb.append(this.assembleNameValue("TU", t2.getFromUrl()))
								.append(",");
					}
				} else {
					sb.append(this.assembleNameValue("RT", n1)).append(",");
					sb.append(this.assembleNameValue("TU", fu1)).append(",");
					sb.append(this.assembleNameValue("RS", null)).append(",");
					sb.append(this.assembleNameValue("SU", null)).append(",");
				}
			}
		} else {
			sb.append(this.assembleNameValue("RT", null)).append(",");
			sb.append(this.assembleNameValue("TU", null)).append(",");
			sb.append(this.assembleNameValue("RS", null)).append(",");
			sb.append(this.assembleNameValue("SU", null)).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("}");

		return sb.toString();
	}

	public String assembleNameValue(String name, Object value) {
		StringBuilder sb = new StringBuilder();
		sb.append("\"").append(name).append("\"").append(":");
		if (value == null) {
			sb.append("\"\"");
			return sb.toString();
		}

		if (value instanceof String) {
			String s = ((String) value).replaceAll("\\\"", "\\\\\"");
			sb.append("\"").append(s).append("\"");
			return sb.toString();
		}

		if (value instanceof Number) {
			sb.append(value.toString());
			return sb.toString();
		}

		if (value instanceof List) {
			List list = (List) value;
			if (list.size() == 0) {
				sb.append("null");
				return sb.toString();
			}

			sb.append("[");
			for (int i = 0; i < list.size(); i++) {
				Object o = list.get(i);
				if (o != null) {
					sb.append("\"");
					String s = (o.toString()).replaceAll("\\\"", "\\\\\"");
					sb.append(s).append("\",");
				}
			}
			if (sb.length() > 1)
				sb.deleteCharAt(sb.length() - 1);
			sb.append("]");

			return sb.toString();
		}

		return "";
	}

	public int exportAllArticleList(OutputStream fileStream) throws IOException {
		List<Article> articles = this.articleDao.getArticleList(0,
				System.currentTimeMillis(), Article.STATUS_ACCEPTED);
		byte[] bytes = ExcelExporter.generateExcelStream(articles);
		fileStream.write(bytes);
		return bytes.length;
	}
}
