package com.devlib.crawler.handlers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.devlib.crawler.config.Img;
import com.devlib.crawler.config.Item;
import com.devlib.crawler.crawlers.CrawlerResult;
import com.devlib.util.Const;
import com.devlib.util.IOUtil;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 用于下载保存图片的处理器。图片下载保存到本地。
 * 
 * @author guangfeng jin
 *
 */
public class ImgsSaveHandler extends BaseDataHandler {
	private static Logger logger = Logger.getLogger(ImgsSaveHandler.class);
	private static final String SCD_NAME_PREFIX_ORIGINAL = "Original";
	private static final String SCD_NAME_PREFIX_LOCAL = "Local";
	private static final String SEPARATOR_ORIGINAL_LOCAL = "_|korea|_";
	private static final String RELATIVE_PATH_ROOT = "SCDs";

	// 取到由item配置规则指定的实际html元素(s)
	// <item itemId="detailImgs" type="koreaxingDetailImgs"
	// xpath="//div[@id='textarea_content']
	// required="true">
	@Override
	public boolean handle(final Item item, final HtmlPage htmlPage,
			final CrawlerResult jobResult) {
		final List<HtmlElement> elements = item.getElementList(htmlPage);// 配置的节点(s)

		if (elements == null || elements.size() == 0) {
			logger.error("TextItem got "
					+ item.getProperty().getScd().getScdName()
					+ " none content in " + jobResult.getUrlValue());
			return false;
		}

		// 依次获取元素下的各个图片节点，包括其各级子孙下的图片节点
		List<HtmlImage> imageList = new ArrayList<HtmlImage>();
		for (HtmlElement element : elements) {
			imageList.addAll(findAllImageNode(element));
		}
		if (imageList == null || imageList.isEmpty()) {
			return false;
		}

		try {
			final File directory = (File) this.params
					.getParam(Const.SCD_DIR_NAME);
			final String scdName = item.getProperty().getScd().getScdName();
			final File imgDir = IOUtil.getDir(directory, scdName);
			for (HtmlImage image : imageList) {
				final String src = image.getSrcAttribute();
				String fullSrc = "";
				try {
					fullSrc = htmlPage.getFullyQualifiedUrl(src).toString();
				} catch (final MalformedURLException e) {
					fullSrc = src;
				}

				// String localImagePath = ImageDownloader2.download(fullSrc,
				// imgDir);
				String localImagePath = null;
				if (localImagePath == null
						|| !localImagePath.contains(RELATIVE_PATH_ROOT)) {
					continue;
				} else {
					int relativeRootIndex = localImagePath
							.indexOf(RELATIVE_PATH_ROOT);
					localImagePath = localImagePath.substring(
							relativeRootIndex, localImagePath.length());// 只存图片在本地的相对地址
					jobResult
							.addScdContent(SCD_NAME_PREFIX_LOCAL + scdName,
									fullSrc + SEPARATOR_ORIGINAL_LOCAL
											+ localImagePath);// 保持图片网络地址与本地地址的对应关系
				}
				// 保留图片的原始URL地址到SCD中
				final Img img = item.getProperty().getImg();
				if (img.getKeepOriginal()) {
					jobResult.addScdContent(SCD_NAME_PREFIX_ORIGINAL + scdName,
							fullSrc);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public static List<HtmlImage> findAllImageNode(final DomNode domNode) {
		List<HtmlImage> list = new ArrayList<HtmlImage>();
		if (domNode instanceof HtmlImage) {
			list.add((HtmlImage) domNode);
		} else {
			final DomNodeList<DomNode> eles = domNode.getChildNodes();
			for (final DomNode ele : eles) {
				list.addAll(findAllImageNode(ele));
			}
		}

		return list;
	}
}