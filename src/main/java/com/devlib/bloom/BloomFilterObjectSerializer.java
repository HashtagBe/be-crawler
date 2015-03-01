package com.devlib.bloom;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.devlib.util.Assert;

/**
 * @author quanzhao.chen
 */
class BloomFilterObjectSerializer implements BloomFilterSerializer {

	@Override
	public BloomFilter deserialize(InputStream in) throws IOException {
		Assert.notNull(in, "in must not be null");

		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
				in));
		try {
			return (BloomFilter) ois.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void serialize(BloomFilter obj, OutputStream out) throws IOException {
		Assert.notNull(out, "out must not be null");
		Assert.notNull(obj, "obj must not be null");

		ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(out));
		oos.writeObject(obj);
		oos.flush();
	}

}
