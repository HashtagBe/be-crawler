package com.devlib.bloom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

/**
 * 维护一个全局的PersistableBloomFilter实例
 * 
 * @author quanzhao.chen
 */
public abstract class PersistableBloomFilterHolder {

	private static PersistableBloomFilter bloomFilter;

	private static String imagePath = "";

	public static void init(String path) throws IOException {
		imagePath = path;

		if (StringUtils.isBlank(imagePath) || !(new File(imagePath).exists())) {
			bloomFilter = new PersistableBloomFilter(125000000, 22);
		} else {
			FileInputStream fin = new FileInputStream(imagePath);
			bloomFilter = new PersistableBloomFilter().load(fin);
			fin.close();
		}

		Runtime.getRuntime().addShutdownHook(
				new PersistableBloomFilterStoreThread());
	}

	public static PersistableBloomFilter get() {
		return bloomFilter;
	}

	public static boolean store() throws IOException {
		if (StringUtils.isBlank(imagePath)) {
			return false;
		}

		FileOutputStream fout = new FileOutputStream(imagePath);
		bloomFilter.store(fout);
		fout.close();
		return true;
	}

}

class PersistableBloomFilterStoreThread extends Thread {

	@Override
	public void run() {
		boolean success = false;
		try {
			success = PersistableBloomFilterHolder.store();
		} catch (Exception e) {
		}
		// System.out.println("Store bloom filter image:" + success);
	}

}
