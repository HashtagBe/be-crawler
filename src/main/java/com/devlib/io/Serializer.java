package com.devlib.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author quanzhao.chen
 */
public interface Serializer<T> {

	/**
	 * 序列化
	 * 
	 * @param obj
	 *            序列化对象
	 * @param out
	 *            输出流
	 */
	public void serialize(T obj, OutputStream out) throws IOException;

}
