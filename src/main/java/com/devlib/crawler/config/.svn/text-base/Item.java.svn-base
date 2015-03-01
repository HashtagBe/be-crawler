package com.devlib.crawler.config;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.devlib.util.Const;
import com.devlib.util.Cssify;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 对应rule.xml中Rule > Page > Item
 * 
 * @author wenjian.liang
 */
public class Item {
	private static Logger logger = Logger.getLogger(Item.class);

	// item>property的是data，item>url的是link
	private Property property;

	/**
	 * 对应处理的类
	 */
	private String type;
	/**
	 * Item对象间相互区别的id。目前用于condition中的item和page中的item做对应
	 */
	private String itemId;

	/**
	 * 页面上节点元素的id属性，可以唯一确定一个节点元素
	 */
	private String id;

	/**
	 * 页面上节点元素的标签名字，与attrName、attrValue配合使用
	 */
	private String elementName;
	/**
	 * 页面上节点元素的某个属性，与elementName、attrValue配合使用。
	 */
	private String attrName;
	/**
	 * 页面上节点元素的某个属性的值，与elementName、attrName配合使用。
	 */
	private String attrValue;

	/**
	 * 页面上节点元素的xpath
	 */
	private String xpath;

	/**
	 * 页面上节点元素的cssQuery，称作“CSS选择器”
	 */
	private String cssQuery;

	/**
	 * 页面上节点元素的cssFinishOn
	 */
	private String cssFinishOn;

	/**
	 * item下使用property表示要处理的是数据，使用url表示要处理的是链接
	 */
	private Url url;

	/**
	 * 表示此 item 执行结果为必选项
	 * <p>
	 * 1. true / yes 必须满足条件,若失败 跳过后续item 处理 <br/>
	 * 2. false / no 非必选项
	 * </p>
	 */
	private boolean required = false;

	/**
	 * 在页面中寻找符合规则的元素列表。 寻找规则有如下几种：
	 * <p>
	 * 1，id属性具有指定值 <br/>
	 * 2，标签名是指定的标签名，并且具有某个指定的属性，并且该属性具有某个指定的值 3，符合某个xpath规则 <br/>
	 * 4，name属性具有指定值 <br/>
	 * </p>
	 * 
	 * @param htmlpage
	 *            页面对象
	 * @return 页面元素集合，若没找到，则集合为空
	 */
	@SuppressWarnings("unchecked")
	public List<HtmlElement> getElementList(final HtmlPage htmlpage) {
		// 1. by xpath
		List<HtmlElement> getEleList_xpath = getElementByXpath(htmlpage);
		if (getEleList_xpath != null && !getEleList_xpath.isEmpty()) {
			return getEleList_xpath;
		}

		// 2. by id
		List<HtmlElement> getEleList_id = getElementById(htmlpage);
		if (getEleList_id != null && !getEleList_id.isEmpty()) {
			return getEleList_id;
		}
		// 3. by ele and attr
		List<HtmlElement> getEleList_attr = getElementByElement(htmlpage);
		if (getEleList_attr != null && !getEleList_attr.isEmpty()) {
			return getEleList_attr;
		}

		return Collections.emptyList();
	}

	/**
	 * 通过 htmlElement　id 获取元素
	 * 
	 * @param htmlpage
	 * @return　List<HtmlElement>
	 * @see [类、类#方法、类#成员]
	 */
	private List<HtmlElement> getElementById(final HtmlPage htmlpage) {

		if (StringUtils.isBlank(id)) {
			return null;
		}

		DomElement domEle = htmlpage.getElementById(id);
		HtmlElement ele = null;
		if (null != domEle && domEle instanceof HtmlElement) {
			ele = (HtmlElement) domEle;
		}
		if (ele == null) {
			return null;
		} else {
			return Collections.singletonList(ele);
		}
	}

	/**
	 * 通过 htmlElement　xpath 获取元素
	 * 
	 * @param htmlpage
	 * @return　List<HtmlElement>
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	private List<HtmlElement> getElementByXpath(final HtmlPage htmlpage) {

		if (StringUtils.isBlank(xpath)) {
			return null;
		}
		return (List<HtmlElement>) htmlpage.getByXPath(xpath);
	}

	/**
	 * 通过 htmlElement　element attr and attr value 获取元素 <br/>
	 * 
	 * @param htmlpage
	 * @return　List<HtmlElement>
	 * @see [类、类#方法、类#成员]
	 */
	private List<HtmlElement> getElementByElement(final HtmlPage htmlpage) {

		if (StringUtils.isBlank(elementName) || StringUtils.isBlank(attrName)
				|| StringUtils.isBlank(attrValue)) {
			return null;
		}
		return htmlpage.getDocumentElement().getElementsByAttribute(
				elementName, attrName, attrValue);
	}

	public String getItemId() {
		// 注：itemId属性不能取消，因为and、or标签下的item标签只有一个itemId属性，用于对应前面一个Item。
		return itemId;
	}

	public Url getUrl() {
		return url;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public void setItemId(final String itemId) {
		this.itemId = itemId;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(final String elementName) {
		this.elementName = elementName;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(final String attrName) {
		this.attrName = attrName;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(final String attrValue) {
		this.attrValue = attrValue;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(final String xpath) {
		this.xpath = xpath;
	}

	public String getCssQuery() {
		return cssQuery;
	}

	public void setCssQuery(String cssQuery) {
		this.cssQuery = cssQuery;
	}

	public String getCssFinishOn() {
		return cssFinishOn;
	}

	public void setCssFinishOn(String cssFinishOn) {
		this.cssFinishOn = cssFinishOn;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(final Property property) {
		this.property = property;
	}

	public void setUrl(final Url url) {
		this.url = url;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = Boolean.valueOf(required)
				|| Const.YES.equalsIgnoreCase(required);
	}

	@Override
	public String toString() {
		return "Item [itemId=" + itemId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (itemId == null ? 0 : itemId.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Item other = (Item) obj;
		if (itemId == null) {
			if (other.itemId != null) {
				return false;
			}
		} else if (!itemId.equals(other.itemId)) {
			return false;
		}
		return true;
	}

	/**
	 * 在页面中寻找符合规则的元素列表。按优先级从高到底的顺序支持下列4种模式： 1.id 2.name 3.selector 4.xpath
	 * 其中xpath会转换成css selector再去寻找
	 * 
	 * @param doc
	 * @return
	 */
	public Elements getElementList(Document doc) {
		if (id != null && id.trim().length() > 0) {
			Elements elements = new Elements();
			Element element = doc.getElementById(id);
			if (element != null) {
				elements.add(element);
			}
			return elements;
		} else if (cssQuery != null) {
			Elements elements = doc.select(cssQuery);
			return elements;
		} else if (xpath != null) {
			try {
				String[] cssQueryArr = Cssify.xpath2CssArray(xpath);

				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < cssQueryArr.length; i++) {
					if (i > 0) {
						sb.append(",");
					}
					sb.append(cssQueryArr[i].trim());
				}

				Elements elements = doc.select(sb.toString());
				return elements;
			} catch (Exception e) {
				logger
						.error("Item.getElementList(Document doc)::xpath转换异常，异常的xpath为："
								+ xpath + " ，itemId为： " + this.itemId);
				return null;
			}
		}

		return new Elements();
	}
}
