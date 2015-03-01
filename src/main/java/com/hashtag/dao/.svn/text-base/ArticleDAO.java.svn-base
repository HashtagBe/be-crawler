package com.hashtag.dao;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hashtag.domain.Article;

@Repository
public class ArticleDAO {
	@Autowired
	private SqlSessionTemplate sqlST;

	public int insertArticle(Article a) {
		this.sqlST.insert("insertArticle", a);
		return this.isArticleExist(a);
	}

	public int isArticleExist(Article a) {
		Integer i = this.sqlST.selectOne("getArticleIdByUrl", a.getUrl());
		if (i == null)
			return -1;
		else
			return i.intValue();
	}

	public List<Article> getArticleList(long startTime, long endTime, int status) {
		Map<String, Object> map = new HashMap<String, Object>();
		Timestamp t1 = new Timestamp(startTime);
		Timestamp t2 = new Timestamp(endTime);
		map.put("startTime", t1);
		map.put("endTime", t2);
		map.put("status", status);

		return this.sqlST.selectList("getArticleList", map);
	}

	public int updateArticleStatus(int articleId, int status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("articleId", articleId);
		map.put("status", status);

		return this.sqlST.update("updateArticleStatus", map);
	}

	public int getArticleCount(long startTime, long endTime, int status) {
		Map<String, Object> map = new HashMap<String, Object>();
		Timestamp t1 = new Timestamp(startTime);
		Timestamp t2 = new Timestamp(endTime);
		map.put("startTime", t1);
		map.put("endTime", t2);
		map.put("status", status);

		return this.sqlST.selectOne("getArticleCount", map);
	}
}
