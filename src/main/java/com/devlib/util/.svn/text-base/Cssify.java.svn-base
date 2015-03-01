package com.devlib.util;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Cssify {

	private static Invocable invocableEngine;

	private final static Map<String, String> cache = new ConcurrentHashMap<String, String>();

	private final static Lock lock = new ReentrantLock();

	private final static int MAX_CACHE_SIZE = 20000;

	static {

		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("javascript");
			engine.eval(new InputStreamReader(Cssify.class
					.getResourceAsStream("/config/cssify.js")));
			invocableEngine = (Invocable) engine;
		} catch (ScriptException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param xpath
	 * @return css
	 * @throws Exception
	 */
	public static String xpath2Css(String xpath) throws Exception {
		String css = cache.get(xpath);

		if (css == null) {
			try {
				lock.lock();
				css = cache.get(xpath);
				if (css == null) {
					try {
						css = (String) invocableEngine.invokeFunction("cssify",
								xpath);
					} catch (Exception e) {
						throw new RuntimeException("can't convert xpath:"
								+ xpath, e);
					}
					cache.put(xpath, css);
				}
			} finally {
				lock.unlock();
			}
		}

		if (cache.size() > MAX_CACHE_SIZE) {
			cache.clear();
		}
		return css;
	}

	/**
	 * 
	 * @param xpath
	 * @return css array
	 * @throws Exception
	 */
	public static String[] xpath2CssArray(String xpath) throws Exception {
		List<String> xpaths = new ArrayList<String>();
		String[] array = xpath.split("\\|");
		for (String str : array) {
			int orIndex = str.indexOf(" or ");
			if (orIndex == -1) {
				xpaths.add(str);
				continue;
			}

			int rbIndex = str.lastIndexOf('[', orIndex);
			int lbIndex = str.indexOf(']', orIndex);

			if (rbIndex == -1 || lbIndex == -1) {
				throw new RuntimeException("xpath parse error");
			}
			String rbStr = str.substring(rbIndex, orIndex);
			String lbStr = str.substring(orIndex + 3, lbIndex + 1);
			String pre = str.substring(0, rbIndex);
			xpaths.add(pre + rbStr.trim() + "]");
			xpaths.add(pre + "[" + lbStr.trim());
		}

		List<String> ret = new ArrayList<String>();
		for (String _xpath : xpaths) {
			ret.add(xpath2Css(_xpath));
		}

		return ret.toArray(new String[0]);

	}

	public static void main(String[] args) throws Exception {

		long begin = System.currentTimeMillis();
		String[] array = Cssify
				.xpath2CssArray("html/head/meta[@name='description' or @name='Description']");
		for (String str : array) {
			System.out.println(str);
		}

		long end = System.currentTimeMillis();
		System.out.println(end - begin);

	}
}