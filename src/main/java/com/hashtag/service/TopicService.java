package com.hashtag.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.util.ExceptionHandler;
import com.hashtag.dao.TopicDAO;
import com.hashtag.domain.Topic;

@Service
public class TopicService {
	private static Logger logger = Logger.getLogger(TopicService.class);

	@Autowired
	private TopicDAO topicDao;

	public int insertTopic(Topic t) {
		try {
			int topicId = this.topicDao.isTopicExist(t);
			if (topicId > 0)
				return topicId;

			return this.topicDao.insertTopic(t);
		} catch (Exception e) {
			ExceptionHandler.handleException(e, logger);
			return -1;
		}
	}
}
