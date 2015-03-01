package com.devlib.crawler.config;

/**
 * 
 * html 页面组件
 * 
 * <p>
 * 主要用来表示配置一个页面元素,用来对特定元素的处理
 * </p>
 * 
 * @author yellowhat.huang
 * @version [版本号, 2013-3-5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Component {

	/**
	 * html css
	 */
	public static final String HTML_ATTR_CSS = "class";

	/**
	 * html style
	 */
	public static final String HTML_ATTR_STYLE = "style";

	/**
	 * html title
	 */
	public static final String HTML_ATTR_TITLE = "title";

	/**
	 * html value
	 */
	public static final String HTML_ATTR_VALUE = "value";

	/**
	 * html href
	 */
	public static final String HTML_ATTR_HREF = "href";

	/**
	 * html attr
	 */
	public static final String HTML_ATTR = "attr";

	/**
	 * html attr split
	 */
	public static final String HTML_ATTR_SLIPT = "=";

	/**
	 * 组件样式
	 */
	private String css;

	/**
	 * js function
	 */
	private String js;

	/**
	 * 链接地址
	 */
	private String link;

	/**
	 * 事件名称
	 */
	private String eventName;

	/**
	 * 　属性名称
	 */
	private String attr;

	/**
	 * 显示文本,如a的链接名字如 <a href="">link</a> link 就是显示名称　
	 */
	private String value;

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public String getJs() {
		return js;
	}

	public void setJs(String js) {
		this.js = js;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public static String getHtmlAttrStyle() {
		return HTML_ATTR_STYLE;
	}

	/**
	 * 判断是否启用css检测
	 * 
	 * @return true 启用,false 禁用
	 * @see [类、类#方法、类#成员]
	 */
	public boolean isEnableCss() {
		String css = this.getCss();
		if (null == css || css.trim().length() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否启用attr检测
	 * 
	 * @return true 启用,false 禁用
	 * @see [类、类#方法、类#成员]
	 */
	public boolean isEnableAttr() {
		String attr = this.getAttr();
		if (null == attr || attr.trim().length() == 0
				|| attr.indexOf(HTML_ATTR_SLIPT) == -1) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否启用js检测
	 * 
	 * @return true 启用,false 禁用
	 * @see [类、类#方法、类#成员]
	 */
	public boolean isEnableJs() {
		String js = this.getJs();
		if (null == js || js.trim().length() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否启用value检测
	 * 
	 * @return true 启用,false 禁用
	 * @see [类、类#方法、类#成员]
	 */
	public boolean isEnableValue() {
		String value = this.getValue();
		if (null == value || value.trim().length() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否启用link检测
	 * 
	 * @return true 启用,false 禁用
	 * @see [类、类#方法、类#成员]
	 */
	public boolean isEnableLink() {
		String link = this.getLink();
		if (null == link || link.trim().length() == 0) {
			return false;
		}
		return true;
	}

}
