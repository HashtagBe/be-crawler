package com.hashtag.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hashtag.domain.Article;
import com.hashtag.service.ArticleService;
import com.hashtag.service.CrawlerService;
import com.hashtag.util.JsonRespEntity;

@Controller
@RequestMapping("/article")
public class ArticleController {
	@Autowired
	private ArticleService articleSvc;
	@Autowired
	private CrawlerService crawlerSvc;

	@RequestMapping(value = "/getArticleList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getArticleList(long startTime, long endTime) {
		JsonRespEntity jre = new JsonRespEntity();
		List<Article> articleList = this.articleSvc.getArticleList(startTime,
				endTime, Article.STATUS_ACCEPTED);
		if (articleList == null) {
			jre.setMsgCode(-1);
			jre.setMsg("Failed to get articles");
			return jre;
		}

		List<String> rst = this.articleSvc.convert(articleList);
		StringBuilder sb = new StringBuilder("[");
		for (String s : rst) {
			sb.append(s).append(",");
		}

		if (sb.length() > 1)
			sb.deleteCharAt(sb.length() - 1);

		sb.append("]");

		return sb.toString();
	}

	@RequestMapping(value = "/getArticleCount", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getArticleCount(long startTime, long endTime) {
		JsonRespEntity jre = new JsonRespEntity();
		int count = this.articleSvc.getArticleCount(startTime, endTime,
				Article.STATUS_ACCEPTED);
		if (count < 0) {
			jre.setMsgCode(-1);
			jre.setMsg("Failed to get article count");
			return jre;
		}

		StringBuilder sb = new StringBuilder("{");
		sb.append("\"count\":").append(count).append("}");

		return sb.toString();
	}

	@RequestMapping(value = "/startArticleCrawler", method = RequestMethod.GET)
	public void startCrawlerThread() {
		this.crawlerSvc.startDaemonThread();
	}

	@RequestMapping(value = "/exportToExcel", method = RequestMethod.GET)
	public void exportArticleList(HttpServletResponse response) {
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename="
				+ new String("article.xls".getBytes()));

		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		try {
			OutputStream fileStream = new BufferedOutputStream(
					response.getOutputStream());
			int len = this.articleSvc.exportAllArticleList(fileStream);
			response.setContentLength(len);
			fileStream.flush();
			fileStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
