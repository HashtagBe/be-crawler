package com.devlib.bloom;

/**
 * @author quanzhao.chen
 */
public interface BloomFilter {

	/**
	 * 获取当前过滤器中字符串的数量
	 * 
	 * @return 当前过滤器中字符串的数量
	 */
	public int size();

	/**
	 * 检查给定字符串是否已存在当前过滤器中
	 * 
	 * @param s
	 *            给定字符串
	 * @return 如果存在，则返回true；否则返回false
	 */
	public boolean contains(final CharSequence s);

	/**
	 * 添加一个字符串到过滤器中
	 * 
	 * @param s
	 *            给定字符串
	 * @return 如果成功(前提是给定字符串当前不在过滤器中)，返回true
	 */
	public boolean add(final CharSequence s);

	/**
	 * 获得当前过滤器占用的内存字节数
	 * 
	 * @return 当前过滤器占用的内存字节数
	 */
	public long getSizeBytes();

	/**
	 * 获取期望添加到过滤器中的字符串数量
	 * 
	 * @return 期望添加到过滤器中的字符串数量
	 */
	public long getExpectedInserts();

	/**
	 * 获取Hash函数的个数
	 * 
	 * @return Hash函数的个数
	 */
	public long getHashCount();

	/**
	 * 检查给定索引是否设值
	 * 
	 * @param bitIndex
	 *            给定索引
	 * @return 如果设值，则返回true
	 */
	public boolean getBit(long bitIndex);

}
