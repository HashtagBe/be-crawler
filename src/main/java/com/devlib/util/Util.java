package com.devlib.util;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.devlib.crawler.config.Url;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Util {
	

	/**
	 * 使用HtmlPage参数指定的页面，请求url所指向的图片（此url是在此HtmlPage页面上的），并将其保存为File参数指定的文件
	 * 
	 * @param htmlPage
	 * @param url
	 * @param imageFile
	 * @return 保存的图片的绝对路径
	 * @throws IOException
	 */
	public static String requestImgToSave(final HtmlPage htmlPage,
			final URL url, final File imageFile) throws IOException {
		// 发起一个新请求，请求的是这张图片
		final WebRequest request = new WebRequest(url);
		final WebResponse response = htmlPage.getWebClient().loadWebResponse(
				request);
		// 注：使用的是javax.imageio api
		final ImageInputStream iis = ImageIO.createImageInputStream(response
				.getContentAsStream());
		final Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
		if (!iter.hasNext()) {
			throw new IOException("No image detected in response, image url:"
					+ url);
		}
		final ImageReader imageReader = iter.next();
		imageReader.setInput(iis);
		// 保存这张图片。save the img
		final boolean b = ImageIO.write(imageReader.read(0),
				imageReader.getFormatName(), imageFile);
		if (!b) {
			return null;
		}

		return imageFile.getAbsolutePath();
	}

	/**
	 * Replace "<" or ">" symbols occurred in the string with "&lt;" or "&gt;"
	 * symbols.
	 * 
	 * @param s
	 *            The initial string
	 * @return The replaced string
	 */
	public static String replaceSpecialChars(final String s) {
		if (s == null) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		final int len = s.length();
		for (int i = 0; i < len; ++i) {
			final char c = s.charAt(i);
			if (c == '<') {
				sb.append("&lt;");
			} else if (c == '>') {
				sb.append("&gt;");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Read the buffered stream and put to a {@link String} reference
	 * 
	 * @param br
	 * @return
	 * @throws IOException
	 */
	public static String read(final Reader br) throws IOException {
		// read the file content as a string
		final StringBuilder sb = new StringBuilder();
		final char[] cbuf = new char[1024];
		int count = br.read(cbuf);
		while (count > 0) {
			sb.append(cbuf, 0, count);
			count = br.read(cbuf);
		}

		final String content = sb.toString();

		return content;
	}

	/**
	 * Encode the specified string to MD5 hexadecimal string representation
	 * 
	 * @param text
	 * @return The MD5 hex string
	 */
	public static String md5Encoding(final String text) {
		if (text == null) {
			return null;
		}
		try {
			final MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			final byte[] b = text.getBytes("utf-8");
			md.update(b);
			final byte[] rst = md.digest();
			final StringBuilder sb = new StringBuilder();
			for (final byte element : rst) {
				final String s = Integer.toHexString(element & 0Xff);
				if (s.length() == 1) {
					sb.append("0");
				}
				sb.append(s);
			}

			return sb.toString();
		} catch (final NoSuchAlgorithmException e) {
			// 理论上不会到达
			e.printStackTrace();
			assert false;
		} catch (final UnsupportedEncodingException e) {
			// 理论上不会到达
			e.printStackTrace();
			assert false;
		}
		return null;
	}

	/**
	 * @param node
	 * @return
	 */
	public static boolean isPureText(final DomNode node) {
		// 注：按照w3c的标准，纯文本的节点名字为“#text”
		return "#text".equals(node.getNodeName());
	}

	/**
	 * 查找此元素节点下第一个HtmlImage节点（即img标签）。如果它本身就是，返回它本身
	 * 
	 * @param domNode
	 * @return
	 */
	public static HtmlImage findFirstImageNode(final DomNode domNode) {
		if (domNode instanceof HtmlImage) {
			return (HtmlImage) domNode;
		} else {
			final DomNodeList<DomNode> eles = domNode.getChildNodes();
			for (final DomNode ele : eles) {
				final HtmlImage findFirstImageNode = findFirstImageNode(ele);
				if (findFirstImageNode != null) {
					return findFirstImageNode;
				}
			}
		}
		return null;
	}

	/**
	 * 查找此元素节点下第一个图片节点元素（即img标签）。如果它本身就是，返回它本身
	 * 
	 * @param element
	 * @return
	 */
	public static Element findFirstImageElement(final Element element) {
		if ("img".equalsIgnoreCase(element.tagName())) {
			return element;// 得到第一个图片节点
		} else {
			Elements children = element.children();
			Iterator<Element> it = children.iterator();
			while (it.hasNext()) {
				Element child = it.next();
				if ("img".equalsIgnoreCase(child.tagName())) {
					return child;
				} else {
					try {// Element类没有像hasChildren()这样的方法，child.child(0)又可能抛异常，所以这里捕捉一下
						return findFirstImageElement(child.child(0));
					} catch (Exception e) {
						return null;
					}
				}
			}
		}

		return null;
	}

	/**
	 * 使用图片的url地址生成img文件的名字
	 * 
	 * @param src
	 * @return
	 */
	public static String buildImgFileName(final String src) {
		final String md5Encoding = md5Encoding(src);
		assert md5Encoding != null;

		final String fileName = "Image-" + md5Encoding + ".pic";
		return fileName;
	}

	/**
	 * 根据scd中的名字创建对应的图片目录，然后创建图片文件。比如price是图片的，并且其在scd文件中就叫&lt;price&gt;，
	 * 就应该在根目录下创建一个叫price的目录用来保存price的图片
	 * 
	 * @param rootDir
	 * @param scdName
	 * @param fileName
	 * @return
	 */
	public static File buildImgFile(final File rootDir, final String scdName,
			final String fileName) {
		// 检查并创建目录
		try {
			final File dir = buildImgDir(rootDir, scdName);
			return new File(dir, fileName);
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据scd中的名字创建对应的图片目录。比如price是图片的，并且其在scd文件中就叫&lt;price&gt;，
	 * 就应该在根目录下创建一个叫price的目录用来保存price的图片
	 * 
	 * @param rootDir
	 * @param scdName
	 * @return
	 * @throws IOException
	 */
	protected static File buildImgDir(final File rootDir, final String scdName)
			throws IOException {
		return IOUtil.getDir(rootDir, scdName);
	}

	/**
	 * Get all url links in the node list <code>eles<code>
	 * 
	 * @param eles
	 * @return
	 * @throws MalformedURLException
	 */
	/**
	 * 将参数HtmlElement对象们所包含的所有a标签抽取出来,也包括参数中本身就是a标签的 Get all url links in the
	 * node list <code>eles<code>
	 * 
	 * @param eles
	 * @return
	 * @throws MalformedURLException
	 */
	public static List<HtmlAnchor> getTotalLinks(final List<HtmlElement> eles) {
		if (eles == null || eles.isEmpty()) {
			return Collections.emptyList();
		}
		final List<HtmlAnchor> totalLinks = new ArrayList<HtmlAnchor>();

		for (final HtmlElement ele : eles) {
			if (ele instanceof HtmlAnchor) {
				totalLinks.add((HtmlAnchor) ele);
			} else {
				final List<HtmlElement> links = ele.getElementsByTagName("a");
				if (links != null && !links.isEmpty()) {
					for (final HtmlElement link : links) {
						totalLinks.add((HtmlAnchor) link);
					}
				}
			}
		}
		return totalLinks;
	}

	public static Collection<Url> expandUrls(Collection<Url> urls) {
		if (urls == null || urls.isEmpty()) {
			return urls;
		}

		Collection<Url> results = new LinkedList<Url>();
		for (Url tempUrl : urls) {
			if (!StringUtils.isBlank(tempUrl.getReplaceValue())) {
				results.addAll(expandUrl(tempUrl));
			} else {
				results.add(tempUrl);
			}
		}

		return results;
	}

	public static Collection<Url> expandUrl(Url url) {
		Collection<Url> results = new LinkedList<Url>();

		String replaceValue = url.getReplaceValue();
		if (StringUtils.isBlank(replaceValue)) {
			results.add(url);
		} else {
			String value = url.getValue();
			String[] replaceFrags = replaceValue.split("\\s*-\\s*");
			int start = Integer.parseInt(StringUtils.trim(replaceFrags[0]));
			int end = Integer.parseInt(StringUtils.trim(replaceFrags[1]));

			for (int i = start; i <= end; i++) {
				Url newUrl = new Url();
				newUrl.setValue(MessageFormat.format(value, String.valueOf(i)));
				newUrl.setAjaxCall(url.getAjaxCall());
				newUrl.setExecuteJs(url.getExecuteJs());
				newUrl.setEncode(url.getEncode());
				newUrl.setPageId(url.getPageId());
				newUrl.setPriority(String.valueOf(url.getPriority()));
				newUrl.setWaitTime(url.getWaitTime());
				newUrl.setXpath(url.getXpath());
				newUrl.setCssQuery(url.getCssQuery());
				newUrl.setExecuteCss(url.getExecuteCss());
				newUrl.setFixMark(url.getFixMark());
				// set specificial properties
				newUrl.setCrawlerType(url.getCrawlerType().toString());
				newUrl.setBrowserVersion(url.getBrowserVersion().toString());
				if (null != url.getRegex()) {
					newUrl.setRegex(url.getRegex().toString());
				}
				results.add(newUrl);
			}
		}

		return results;
	}

}
