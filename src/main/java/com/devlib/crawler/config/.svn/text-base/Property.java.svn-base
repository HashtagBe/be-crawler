package com.devlib.crawler.config;

/**
 * 对应rule.xml中Rule > Page > Item > Property
 * 注意：其子标签未作规定，目前有Url、Img、Meta、Content、Attr、Chops 6个
 * 
 * @author wenjian.liang
 */
public class Property {

	// 注：xsd中并没有规定Property标签下可以有什么子标签，也没有规定个数，这是不好的，但是现在也只能按着旧版来。
	// 如果Property下需要出现另一个标签，都是要改代码的。
	// 目前有Img、Meta、Content、Attr，因此先列这几个
	// 以后如果Property下需要使用另一种标签，并且新建该新标签的处理类。

	private Img img;
	private Meta meta;
	private Content content;
	private Attr attr;
	private Docs docs;// 新增，便于一个页面多个文档数据的获取

	// 以上4种子节点的父类，代表其中的某个子节点
	private HasScdName scd;

	// property元素在页面中的xpath
	private String xpath;
	// property元素在页面中的cssQuery
	private String cssQuery;

	public Img getImg() {
		return img;
	}

	public void setImg(final Img img) {
		this.img = img;
		scd = img;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(final Meta meta) {
		this.meta = meta;
		scd = meta;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(final Content content) {
		this.content = content;
		scd = content;
	}

	public Attr getAttr() {
		return attr;
	}

	public void setAttr(final Attr attr) {
		this.attr = attr;
		scd = attr;
	}

	public HasScdName getScd() {
		return scd;
	}

	public void setScd(final HasScdName scd) {
		this.scd = scd;
	}

	public Docs getDocs() {
		return docs;
	}

	public void setDocs(Docs docs) {
		this.docs = docs;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getCssQuery() {
		return cssQuery;
	}

	public void setCssQuery(String cssQuery) {
		this.cssQuery = cssQuery;
	}

}
