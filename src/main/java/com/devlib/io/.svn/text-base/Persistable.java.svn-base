package com.devlib.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * @author quanzhao.chen
 */
public interface Persistable<T> extends Serializable {

	/**
	 * 从给定输入流中加载
	 * 
	 * @param in
	 *            给定输入流
	 */
	public T load(InputStream in) throws IOException;

	/**
	 * 输出到给定输出流
	 * 
	 * @param out
	 *            给定输出流
	 */
	public void store(OutputStream out) throws IOException;

}
