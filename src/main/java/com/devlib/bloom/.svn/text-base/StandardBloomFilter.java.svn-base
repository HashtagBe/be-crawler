package com.devlib.bloom;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 标准布隆过滤器的一种实现。<br>
 * 测试了下<125000000,22>情况下，重复虑大概是千万分之一
 * 
 * @author quanzhao.chen
 */
public class StandardBloomFilter implements BloomFilter, Serializable {

	private static final long serialVersionUID = 274102337234329739L;

	protected long[][] bits;

	/** The random integers used to generate the hash functions */
	protected long[][] weight;

	protected long totalBit;

	protected long expectedInserts;

	protected int expectedHashs;

	protected int power = -1;

	protected int size;

	public StandardBloomFilter(long expectedInserts, int expectedHashs) {
		this(expectedInserts, expectedHashs, new SecureRandom(), false);
	}

	public StandardBloomFilter(long expectedInserts, int expectedHashs,
			boolean roundUp) {
		this(expectedInserts, expectedHashs, new SecureRandom(), roundUp);
	}

	public StandardBloomFilter(long expectedInserts, int expectedHashs,
			Random weightGenerator, boolean roundUp) {
		this.expectedInserts = expectedInserts;
		this.expectedHashs = expectedHashs;
		long lenInLongs = (long) Math.ceil(((long) expectedInserts
				* (long) expectedHashs / NATURAL_LOG_OF_2) / 64L);
		if (lenInLongs > (1L << 48)) {
			throw new IllegalArgumentException("This filter would require "
					+ lenInLongs + " longs, "
					+ "greater than this classes maximum of 2^48 longs (2PiB).");
		}
		long lenInBits = lenInLongs * 64L;

		if (roundUp) {
			int pow = 0;
			while ((1L << pow) < lenInBits) {
				pow++;
			}
			this.power = pow;
			this.totalBit = 1L << pow;
			lenInLongs = totalBit / 64L;
		} else {
			this.totalBit = lenInBits;
		}

		int arrayLength = (int) ((lenInLongs + SUBARRAY_LENGTH_IN_LONGS - 1) / SUBARRAY_LENGTH_IN_LONGS);
		bits = new long[(int) (arrayLength)][];
		// ensure last subarray is no longer than necessary
		long lenInLongsRemain = lenInLongs;
		for (int i = 0; i < bits.length; i++) {
			bits[i] = new long[(int) Math.min(lenInLongsRemain,
					SUBARRAY_LENGTH_IN_LONGS)];
			lenInLongsRemain -= bits[i].length;
		}

		this.weight = new long[expectedHashs][];
		for (int i = 0; i < expectedHashs; i++) {
			this.weight[i] = new long[NUMBER_OF_WEIGHTS];
			for (int j = 0; j < NUMBER_OF_WEIGHTS; j++) {
				this.weight[i][j] = weightGenerator.nextLong();
			}
		}
	}

	public int size() {
		return size;
	}

	public boolean contains(final CharSequence s) {
		int i = expectedHashs, l = s.length();
		while (i-- != 0) {
			if (!getBit(hash(s, l, i))) {
				return false;
			}
		}
		return true;
	}

	public boolean add(final CharSequence s) {
		boolean result = false;
		int i = expectedHashs, l = s.length();
		long h;
		while (i-- != 0) {
			h = hash(s, l, i);
			if (!getAndSetBit(h)) {
				result = true;
			}
		}
		if (result) {
			size++;
		}
		return result;
	}

	private long hash(final CharSequence s, final int l, final int k) {
		final long[] w = weight[k];
		long h = 0;
		int i = l;
		while (i-- != 0) {
			h ^= s.charAt(i) * w[i % NUMBER_OF_WEIGHTS];
		}

		if (power > 0) {
			return h >>> (64 - power);
		}
		return (h & 0x7FFFFFFFFFFFFFFFL) % totalBit;
	}

	public boolean getBit(long bitIndex) {
		long longIndex = bitIndex >>> ADDRESS_BITS_PER_UNIT;
		int arrayIndex = (int) (longIndex >>> SUBARRAY_POWER_OF_TWO);
		int subarrayIndex = (int) (longIndex & SUBARRAY_MASK);
		return ((bits[arrayIndex][subarrayIndex] & (1L << (bitIndex & BIT_INDEX_MASK))) != 0);
	}

	private boolean getAndSetBit(long bitIndex) {
		long longIndex = bitIndex >>> ADDRESS_BITS_PER_UNIT;
		long mask = 1L << (bitIndex & BIT_INDEX_MASK);
		int arrayIndex = (int) (longIndex >>> SUBARRAY_POWER_OF_TWO);
		int subarrayIndex = (int) (longIndex & SUBARRAY_MASK);
		boolean ret = (bits[arrayIndex][subarrayIndex] & mask) != 0;
		bits[arrayIndex][subarrayIndex] |= mask;
		return ret;
	}

	public long getSizeBytes() {
		return 8 * (((bits.length - 1) * bits[0].length) + bits[bits.length - 1].length);
	}

	public long getExpectedInserts() {
		return expectedInserts;
	}

	public long getHashCount() {
		return expectedHashs;
	}

	private static final long ADDRESS_BITS_PER_UNIT = 6; // 64=2^6
	private static final long BIT_INDEX_MASK = (1 << 6) - 1; // 63 =
																// 2^BITS_PER_UNIT
																// - 1;
	private static final double NATURAL_LOG_OF_2 = Math.log(2);
	/** The number of weights used to create hash functions */
	private final static int NUMBER_OF_WEIGHTS = 2083;
	/** power-of-two to use as maximum size of bitfield subarrays */
	private static final int SUBARRAY_POWER_OF_TWO = 26; // 512MB
	/** number of longs in one subarray */
	private static final int SUBARRAY_LENGTH_IN_LONGS = 1 << SUBARRAY_POWER_OF_TWO;// 512MB
	/** mask for lowest SUBARRAY_POWER_OF_TWO bits */
	private static final int SUBARRAY_MASK = SUBARRAY_LENGTH_IN_LONGS - 1; // 0x0FFFFFFF

}