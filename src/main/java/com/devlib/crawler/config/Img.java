package com.devlib.crawler.config;

import com.devlib.util.Const;

/**
 * 对应rule.xml中Rule > Page > Item > Property > Img 注意：是Property可能的子标签之一
 * 
 * @author wenjian.liang
 */
public class Img extends HasScdName {

	private String attrName;

	private String path;
	// 默认保留此图片的绝对url地址
	private boolean keepOriginal = true;

	public boolean getKeepOriginal() {
		return keepOriginal;
	}

	public void setKeepOriginal(final String keepOriginal) {
		this.keepOriginal = Boolean.valueOf(keepOriginal)
				|| !Const.NO.equalsIgnoreCase(keepOriginal);// 注：由于默认值是true，因此对解析false严校验而不是true严校验
	}

	public void setKeepOriginal(final boolean keepOriginal) {
		this.keepOriginal = keepOriginal;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(final String attrName) {
		this.attrName = attrName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
