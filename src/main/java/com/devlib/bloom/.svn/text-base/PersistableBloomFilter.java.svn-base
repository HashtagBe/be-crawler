package com.devlib.bloom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Random;

import com.devlib.io.Persistable;
import com.devlib.util.Assert;

/**
 * @author quanzhao.chen
 */
public class PersistableBloomFilter implements BloomFilter,
		Persistable<PersistableBloomFilter> {

	private static final long serialVersionUID = 2352385130338738147L;

	private BloomFilterSerializer serializer = new BloomFilterObjectSerializer();

	private BloomFilter bloomFilter;

	PersistableBloomFilter() {
	}

	public PersistableBloomFilter(long expectedInserts, int expectedHashs) {
		this(expectedInserts, expectedHashs, new SecureRandom(), false);
	}

	public PersistableBloomFilter(long expectedInserts, int expectedHashs,
			boolean roundUp) {
		this(expectedInserts, expectedHashs, new SecureRandom(), roundUp);
	}

	public PersistableBloomFilter(long expectedInserts, int expectedHashs,
			Random weightGenerator, boolean roundUp) {
		this.bloomFilter = new StandardBloomFilter(expectedInserts,
				expectedHashs, weightGenerator, roundUp);
	}

	@Override
	public int size() {
		return this.bloomFilter.size();
	}

	@Override
	public boolean contains(CharSequence s) {
		return this.bloomFilter.contains(s);
	}

	@Override
	public boolean add(CharSequence s) {
		return this.bloomFilter.add(s);
	}

	@Override
	public long getSizeBytes() {
		return this.bloomFilter.getSizeBytes();
	}

	@Override
	public long getExpectedInserts() {
		return this.bloomFilter.getExpectedInserts();
	}

	@Override
	public long getHashCount() {
		return this.bloomFilter.getHashCount();
	}

	@Override
	public boolean getBit(long bitIndex) {
		return this.bloomFilter.getBit(bitIndex);
	}

	public void setBloomFilterSerializer(BloomFilterSerializer serializer) {
		this.serializer = serializer;
	}

	@Override
	public synchronized PersistableBloomFilter load(InputStream in)
			throws IOException {
		Assert.notNull(in, "in must not be null");

		this.bloomFilter = serializer.deserialize(in);
		return this;
	}

	@Override
	public synchronized void store(OutputStream out) throws IOException {
		Assert.notNull(out, "out must not be null");

		serializer.serialize(this.bloomFilter, out);
	}

}
