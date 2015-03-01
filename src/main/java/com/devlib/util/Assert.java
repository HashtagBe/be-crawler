package com.devlib.util;

/**
 * @author quanzhao.chen
 */
public abstract class Assert {

	public static void notNull(Object obj, String message) {
		if (obj == null) {
			throw new NullPointerException(message);
		}
	}

}
