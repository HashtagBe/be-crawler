package com.hashtag.domain;

import java.sql.Timestamp;

public class Article {
	public static final int STATUS_ACCEPTED = 1;
	public static final int STATUS_DISCARDED = -1;

	private int id, status = 1, siteId, topicId, rawTopicId;
	private String url, title, subTitle, authors, imageUrls, paragraphs;
	private Timestamp date, crawlDate;

	public Article() {
		this.crawlDate = new Timestamp(System.currentTimeMillis());
		this.date = new Timestamp(System.currentTimeMillis());
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Timestamp getCrawlDate() {
		return crawlDate;
	}

	public void setCrawlDate(Timestamp crawlDate) {
		this.crawlDate = crawlDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * The article status.
	 * 
	 * @return -1 indicates the article is reviewed and discarded; 1 indicates
	 *         the article is accepted. The default status value is 1.
	 */
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public int getRawTopicId() {
		return rawTopicId;
	}

	public void setRawTopicId(int rawTopicId) {
		this.rawTopicId = rawTopicId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(String imageUrls) {
		this.imageUrls = imageUrls;
	}

	public String getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(String paragraphs) {
		this.paragraphs = paragraphs;
	}
}
