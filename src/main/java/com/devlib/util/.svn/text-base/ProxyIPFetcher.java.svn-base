package com.devlib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 从代理服务商处获取代理列表
 * 
 * @author guangfeng
 * 
 */
public class ProxyIPFetcher {
	public static class IP {
		String ipAddress;
		int port;

		public String getAddress() {
			return ipAddress;
		}

		public void setAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}
	}

	private static List<IP> blankIPs = new ArrayList<IP>();
	private static String proxyServer2 = "http://aws00.daili8888.com/do_extract?num=50&order_id=457450876519215&avoid_dup=yes&ports=&pro=&format=plaintext2";
	private static String proxyServer1 = "http://www.xinxinproxy.com/httpip/text?orderId=460041981039215&count=50&isDetailFormat=2&isNew=1";
	private static String proxyServer = proxyServer1;
	private static String charset = "UTF-8";

	public static void main(String[] args) {
		List<IP> rst = getProxyIPs();

		rst = getProxyIPs();

		for (IP ip : rst) {
			System.out.println(ip.getAddress() + ":" + ip.getPort());
		}
	}

	private static Iterator<IP> iter;

	/**
	 * 每次获得一个IP
	 * 
	 * @return
	 */
	public static IP getProxyIP() {

		if (iter != null && iter.hasNext()) {
			return iter.next();
		} else {
			iter = getProxyIPs().iterator();
			return getProxyIP();
		}
	}

	public static List<IP> getProxyIPs() {
		GetThread gt = new GetThread();
		gt.start();
		try {
			gt.join(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<IP> rst = gt.getIPs();
		if (rst != null)
			return rst;
		else
			return blankIPs;
	}

	private static List<IP> parseIP(String s, String urlStr) {
		ArrayList<IP> rst = new ArrayList<IP>();
		if (urlStr == proxyServer1) {
			s = s.trim();
			StringTokenizer st = new StringTokenizer(s, "|");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				int idx = token.indexOf(":");
				String addr = token.substring(0, idx);
				String port = token.substring(idx + 1);
				IP ip = new IP();
				ip.setAddress(addr);
				ip.setPort(Integer.parseInt(port));

				rst.add(ip);
			}
		} else {
			s = s.trim();
			int idx = s.indexOf(":");
			String addr = s.substring(0, idx);
			String port = s.substring(idx + 1);
			IP ip = new IP();
			ip.setAddress(addr);
			ip.setPort(Integer.parseInt(port));

			rst.add(ip);
		}

		return rst;
	}

	private static class GetThread extends Thread {
		List<IP> ips = null;

		public void run() {
			this.ips = getProxyIPs();
		}

		public List<IP> getIPs() {
			return ips;
		}

		public static List<IP> getProxyIPs() {
			List<IP> rst = new ArrayList<IP>(10);
			String urlStr = proxyServer;
			if (proxyServer == proxyServer1) {
				proxyServer = proxyServer2;
			} else {
				proxyServer = proxyServer1;
			}

			HttpURLConnection con = null;
			try {
				URL url = new URL(urlStr);
				con = (HttpURLConnection) url.openConnection();
				HttpURLConnection.setFollowRedirects(true);
				con.setRequestMethod("GET");
				con.setRequestProperty("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");

				con.connect();
				InputStream is = con.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, charset));
				String s = br.readLine();
				while (s != null) {
					List<IP> ips = parseIP(s, urlStr);
					rst.addAll(ips);
					s = br.readLine();
				}
				is.close();

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (con != null)
					con.disconnect();
			}

			return rst;
		}
	};

}
