package com.devlib.util;

import java.io.File;
import java.io.FileFilter;

/**
 * 系统常量
 * 
 * @author wenjian.liang
 */
public interface Const {

	/**
	 * 每个线程在最后一次任务完成之后，闲置状态下继续存活的时间。以秒为单位
	 */
	public static final int THREAD_KEEP_ALIVE_SECONDS = 30;
	/**
	 * 多线程抓取时默认最大线程数
	 */
	public static final int DEFUALT_MAX_THREAD_COUNT = 10;
	/**
	 * 一个scd文件最大的尺寸，具体数值是照搬旧版代码。 不是严格标准，多线程情况下允许略大。只表示一个大致的数量级
	 */
	public static final long SCD_FILE_SIZE_LIMIT = 104857600L;// 100M。这个常量以后做到配置里面去。

	/**
	 * 在rule.xml一些属性中表示布尔值的真
	 */
	public static final String YES = "yes";
	/**
	 * 在rule.xml一些属性中表示布尔值的假
	 */
	public static final String NO = "no";

	public static final String ENCODING_UTF8 = "UTF-8";
	public static final String NEW_LINE = "\n";

	/**
	 * 对某url抓取失败时重试次数。
	 */
	public static final int DEFAULT_RETRY_TIMES = 3;

	/**
	 * 爬取深度。深度的定义是这样的：rule.xml中初始的url深度为0，从这些url爬取到的新url深度为1，以此类推递增。 -1表示无限深度
	 */
	public static final int DEFAULT_HOP_COUNT_LIMIT = -1;

	/**
	 * rule.xml的标签映射到的默认的java bean类的包名
	 */
	public static final String RULE_XML_NODE_CLASSES_PACKAGE_NAME = "com.devlib.crawler.config";

	/**
	 * 一个FileFilter，过滤掉文件（即只取目录）。
	 */
	public static final FileFilter DIRECTORY_FILTER = new FileFilter() {
		@Override
		public boolean accept(final File file) {
			return file.isDirectory();
		}
	};

	/**
	 * SCD 文件目录名称
	 */
	public static final String SCD_DIR_NAME = "scdDirName";

	public static final String Selenium_WebDriver_Url = "webDriverUrl";

	public static final String COMMA = ",";

	public static final String FENHAO = ";";

}
