package com.hashtag.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hashtag.domain.Topic;

@Repository
public class TopicDAO {
	@Autowired
	private SqlSessionTemplate sqlST;

	public int isTopicExist(Topic t) {
		Integer i = this.sqlST.selectOne("getTopicId", t);
		if (i == null)
			return -1;
		else
			return i.intValue();
	}

	public int insertTopic(Topic t) {
		this.sqlST.insert("insertTopic", t);
		return this.isTopicExist(t);
	}

	public List<Topic> getTopicListBySiteId(int siteId) {
		return this.sqlST.selectList("getTopicListBySiteId", siteId);
	}

	public Topic getTopic(int tId) {
		return this.sqlST.selectOne("getTopicById", tId);
	}
}
