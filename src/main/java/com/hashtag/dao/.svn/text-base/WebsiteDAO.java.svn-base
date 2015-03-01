package com.hashtag.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hashtag.domain.Website;

@Repository
public class WebsiteDAO {
	@Autowired
	private SqlSessionTemplate sqlST;

	public int insertWebsite(Website site) {
		return this.sqlST.insert("insertSite", site);
	}

	public int isWebsiteExist(Website site) {
		Integer i = this.sqlST.selectOne("getSiteIdByDomainUrl",
				site.getDomainUrl());
		if (i == null)
			return -1;
		else
			return i.intValue();
	}

	public int getSiteIdByName(String siteName) {
		Integer i = this.sqlST.selectOne("getSiteIdByName", siteName);
		if (i != null)
			return i.intValue();
		else
			return -1;
	}
}
