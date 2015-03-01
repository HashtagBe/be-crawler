package com.devlib.crawler.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;

import com.devlib.util.ClassUtil;
import com.devlib.util.IOUtil;

/**
 * 加载item.property属性配置文件。此文件包含page页面中各种类型的item所对应的处理器类路径
 * 
 * @author guangfeng
 *
 */
public class ItemHandlerFactory {
	private static Logger logger = Logger.getLogger(ItemHandlerFactory.class);
	private final static String ITEM_FILE_PATH = "config" + File.separator
			+ "item.property";

	private final static Properties items;
	static {
		items = new Properties();
		FileInputStream inStream = null;
		try {
			inStream = new FileInputStream(
					Resources.getResourceAsFile(ITEM_FILE_PATH));
			items.load(inStream);
		} catch (final FileNotFoundException e) {
			logger.error("The item configuration file " + ITEM_FILE_PATH
					+ " is not found");
			e.printStackTrace();
		} catch (final IOException e) {
			logger.error("IOException while read the item configuration file "
					+ ITEM_FILE_PATH);
		} finally {
			IOUtil.close(inStream);
		}
	}

	private static final ConcurrentHashMap<String, ItemHandler> NAME_INSTANCE_MAP//
	= new ConcurrentHashMap<String, ItemHandler>();

	public static ItemHandler getItemHandler(String name) {
		ItemHandler handler = NAME_INSTANCE_MAP.get(name);
		if (handler == null) {
			try {
				handler = (ItemHandler) ClassUtil.newInstance(items
						.getProperty(name));
				if (handler == null) {
					// TODO do what?nothing to do
				}
				NAME_INSTANCE_MAP.putIfAbsent(name, handler);
			} catch (final IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		return handler;
	}
}
