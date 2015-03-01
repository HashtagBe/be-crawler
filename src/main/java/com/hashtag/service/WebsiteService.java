package com.hashtag.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.util.ExceptionHandler;
import com.hashtag.dao.WebsiteDAO;
import com.hashtag.domain.Website;

@Service
public class WebsiteService {
	private static Logger logger = Logger.getLogger(WebsiteService.class);
	@Autowired
	private WebsiteDAO siteDao;

	public int insertWebsite(Website site) {
		try {
			int id = this.siteDao.isWebsiteExist(site);
			if (id == 0)
				id = this.siteDao.insertWebsite(site);

			return id;
		} catch (Exception e) {
			ExceptionHandler.handleException(e, logger);
			return -1;
		}
	}
}
