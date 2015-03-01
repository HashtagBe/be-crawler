package com.devlib.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class IOUtil {

	/**
	 * 关闭“能关闭的”，比如流
	 * 
	 * @param closeable
	 * @return
	 */
	public static IOException close(final Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (final IOException e) {
				e.printStackTrace();
				return e;
			}
		}
		return null;
	}

	/**
	 * 取得一个目录的File对象。如果它不存在，将会创建
	 * 
	 * @param parent
	 * @param dirName
	 * @return
	 * @throws IOException
	 */
	public static File getDir(final String parent, final String dirName)
			throws IOException {
		final File parentDir = getDir(parent);
		return getDir(parentDir, dirName);
	}

	/**
	 * 取得一个目录的File对象。如果它不存在，将会创建
	 * 
	 * @param parent
	 * @param dirName
	 * @return
	 * @throws IOException
	 */
	public static File getDir(final File parent, final String dirName)
			throws IOException {
		final File dir = new File(parent, dirName);
		return tryMakeDir(dir);

	}

	/**
	 * 取得一个目录的File对象。如果它不存在，将会创建
	 * 
	 * @return
	 * @throws IOException
	 */
	public static File getDir(final String dirPath) throws IOException {
		final File dir = new File(dirPath);
		return tryMakeDir(dir);
	}

	/**
	 * 尝试创建一个目录。
	 * 
	 * @param dir
	 * @return
	 * @throws IOException
	 *             如果此File对象已存在并且所指向的是一个文件而非目录；或无法创建目录
	 */
	protected static File tryMakeDir(final File dir) throws IOException {
		if (dir.exists()) {
			if (dir.isDirectory()) {
				return dir;
			}
			throw new IOException("the path " + dir.getAbsolutePath()
					+ " exists but it is a file");
		}
		synchronized (dir.getAbsolutePath().intern()) {
			if (dir.exists()) {
				return dir;
			}
			if (dir.mkdirs()) {
				return dir;
			} else {
				throw new IOException("Failed to make directory: "
						+ dir.getAbsolutePath());
			}
		}
	}

	/**
	 * 取得一个文件的File对象，如果它不存在，将会创建
	 * 
	 * @param dir
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static File getFile(final File dir, final String fileName)
			throws IOException {
		return checkFile(new File(dir, fileName));
	}

	/**
	 * 取得一个文件的File对象，如果它不存在，将会创建
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static File getFile(final String path) throws IOException {
		return checkFile(new File(path));
	}

	/**
	 * 检验参数File对象，如果它指向的文件不存在，则创建它。 check the argument File object,if the file
	 * pointed not exist, it will be created
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 *             create file fail
	 */
	public static File checkFile(final File file) throws IOException {
		final File f = file;
		if (f.exists()) {
			return f;
		}
		synchronized (f.getAbsolutePath().intern()) {
			if (f.exists()) {
				return f;
			}
			if (f.createNewFile()) {
				return f;
			} else {
				throw new IOException("Failed to create file: "
						+ f.getAbsolutePath());
			}
		}
	}
}
