package com.devlib.crawler.config;

/**
 * 一个父类，为property标签下的数据类型的子元素：meta、content、img、attr。这些元素都要有内容要写到scd文件中去，
 * 而写进去时的key就是这个scdName
 * 
 * @author wenjian.liang
 */
public class HasScdName {

	private String scdName;

	public HasScdName() {
		super();
	}

	public String getScdName() {
		return scdName;
	}

	public void setScdName(final String scdName) {
		this.scdName = scdName;
	}

}