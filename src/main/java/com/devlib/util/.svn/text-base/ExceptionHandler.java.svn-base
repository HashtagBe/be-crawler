package com.devlib.util;

import org.apache.log4j.Logger;

/**
 * 处理异常的通用类。将异常写入日至文件，异常包含如下信息：
 * <ul>
 * <li>抛出异常的类名字
 * <li>异常信息
 * <li>异常栈轨迹
 * </ul>
 * 
 * @author guangfeng
 * 
 */
public class ExceptionHandler {
	/**
	 * 记录异常信息到error类型的日志文件中
	 * 
	 * @param e
	 *            异常类型
	 * @param logger
	 *            Logger 类
	 */
	public static void handleException(Throwable e, Logger logger) {
		if (logger == null)
			return;

		logger.error(" ");

		logger.error("Exception type: " + e.getClass().getName());
		logger.error("Exception message: " + e.getMessage());
		logger.error("Stack traces: ");
		StackTraceElement[] traces = e.getStackTrace();
		for (int j = 0; j < traces.length; j++)
			logger.error(traces[j].toString());
		Throwable cause = e.getCause();
		if (cause != null) {
			logger.error("Caused by: " + cause.getClass().getName());
			logger.error("Caused message: " + cause.getMessage());
			logger.error("Stack traces: ");
			traces = cause.getStackTrace();
			for (int j = 0; j < traces.length; j++)
				logger.error(traces[j].toString());
		}

		logger.error(" ");

		// TODO
		e.printStackTrace();
	}

	/**
	 * 记录异常信息到error类型的日志文件中
	 * 
	 * @param uID
	 *            日志信息ID，用于唯一标示日志记录
	 * @param e
	 *            异常类型
	 * @param logger
	 *            Logger for this exception
	 */
	public static void handleException(String uID, Throwable e, Logger logger) {
		if (logger == null)
			return;
		logger.error("uID: " + uID);
		logger.error("Exception type: " + e.getClass().getName());
		logger.error("Eexeption message: " + e.getMessage());
		logger.error("Stack traces: ");
		StackTraceElement[] traces = e.getStackTrace();
		for (int j = 0; j < traces.length; j++)
			logger.error(traces[j].toString());

		Throwable cause = e.getCause();
		if (cause != null) {
			logger.error("Caused by: " + cause.getClass().getName());
			logger.error("Caused message: " + cause.getMessage());
			logger.error("Stack traces: ");
			traces = cause.getStackTrace();
			for (int j = 0; j < traces.length; j++)
				logger.error(traces[j].toString());
		}
	}
}
