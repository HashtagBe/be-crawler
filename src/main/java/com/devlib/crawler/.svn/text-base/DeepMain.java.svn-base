package com.devlib.crawler;

/**
 * 单机版Deep Crawler程序的入口
 * 
 * @author guangfeng
 * 
 */
public class DeepMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 1 && "-help".equalsIgnoreCase(args[0])) {
			printHelp();
			return;
		}

		if (args.length < 3) {
			System.out.println("输入参数出错！");
			printHelp();
			return;
		}

		String configXML = null;
		if ("-configXML".equalsIgnoreCase(args[0]))
			configXML = args[1];
		else {
			System.out.println("输入参数出错！");
			printHelp();
			return;
		}

		int threadNum = 1;
		if (args.length == 6 && "-threadNum".equalsIgnoreCase(args[2])) {
			try {
				int tmp = Integer.parseInt(args[3]);
				if (tmp > 0)
					threadNum = tmp;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("输入参数出错！");
				printHelp();
				return;
			}
		}

		final Processor processor = new Processor(configXML, threadNum);
		processor.setMultiThread(true);
		processor.start();
	}

	private static void printHelp() {
		System.out.println("Deep Crawler 用法：\n");
		System.out.println("java -jar DeepCrawler.jar [options]\n");
		System.out.println("[options]包含：\n");
		System.out.println("\t -help: 打印帮助文档\n");
		System.out.println("\t -configXML: 抓取配置文件的路径\n");
		System.out.println("\t -threadNum: 并发运行的线程个数，若不指定，则默认为1\n");
		System.out
				.println("用法示例：java -jar DeepCrawler.jar -configXML xxx.xml -threadNum 3\n");
	}
}
