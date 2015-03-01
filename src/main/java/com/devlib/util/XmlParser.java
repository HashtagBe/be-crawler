package com.devlib.util;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 一个简单的xml解析器。 按映射规则，为xml每个节点生成一个简单java bean，并把其子节点以及attribute 放到bean中。 a
 * simple xml parser named XmlParser,create a simple java bean(POJO?) for each
 * node of the xml file using a mapping, and put the children-nodes and
 * attributes of this node to the bean. 一些注意事项： note:
 * 1，如果一种标签可以作为某节点的子节点出现多次，则这个节点对应的java bean中需要有一个addXxx()方法。如： 1，if a type of
 * node can appear more than once as the child-node of a parent-node, then the
 * java bean of the parent-node should have method "addXxx()".e.g.: &lt;Page&gt;
 * &lt;Item&gt;...&lt;/Item&gt; &lt;Item&gt;...&lt;/Item&gt; ... &lt;/Page&gt;
 * 则对应的Page类需要有一个方法addItem(Item item) the class Page should have a method
 * "addItem(Item item)" 尚不支持标签体
 * 
 * @author wenjian.liang
 */
public class XmlParser {

	private final Map<String, String> nodeNameClassNameMap = new IgnoreCaseStringKeyMap<String>();
	private String packageName;

	public XmlParser() {
		super();
	}

	/**
	 * 设置一个基础包名。将在这个包下按xml标签名寻找对应的类
	 * 
	 * @param packageName
	 */
	public XmlParser(final String packageName) {
		super();
		setPackageName(packageName);
	}

	/**
	 * 设置一个映射关系map，key是xml中的标签名，value是类全名
	 * 
	 * @param nodeNameClassNameMap
	 */
	public XmlParser(final Map<String, String> nodeNameClassNameMap) {
		super();
		this.nodeNameClassNameMap.putAll(nodeNameClassNameMap);
	}

	/**
	 * 解析一个xml文件，按节点映射规则生成对应的节点对象，返回根节点的对象。 节点映射规则：
	 * 1，先从节点名-类名映射规则Map中寻找。这个映射Map可以通过构造时或者setMap方法添加进来。查找时对节点名不区分大小写。
	 * 2，如果没有配置相应的映射规则，则使用“默认包名”和节点名的组合作为该类的类名。默认包名可以通过构造时或者setPackageName设置。
	 * 
	 * @param xmlFile
	 * @return
	 * @throws IllegalArgumentException
	 * @throws ClassCastException
	 */
	public <T> T parse(final File xmlFile) throws IllegalArgumentException,
			ClassCastException {
		return parse(xmlFile, null);
	}

	/**
	 * 解析一个xml文件，按节点映射规则生成对应的节点对象，返回根节点的对象。 节点映射规则：
	 * 1，先从节点名-类名映射规则Map中寻找。这个映射Map可以通过构造时或者setMap方法添加进来。查找时对节点名不区分大小写。
	 * 2，如果没有配置相应的映射规则，则使用“默认包名”和节点名的组合作为该类的类名。默认包名可以通过构造时或者setPackageName设置。
	 * 
	 * @param xmlPath
	 * @return
	 * @throws IllegalArgumentException
	 * @throws ClassCastException
	 */
	public <T> T parse(final String xmlPath) throws IllegalArgumentException,
			ClassCastException {
		return parse(new File(xmlPath));
	}

	/**
	 * 解析一个xml文件，按节点映射规则生成对应的节点对象，返回根节点的对象。 节点映射规则：
	 * 1，先从节点名-类名映射规则Map中寻找。这个映射Map可以通过构造时或者setMap方法添加进来。查找时对节点名不区分大小写。
	 * 2，如果没有配置相应的映射规则，则使用“默认包名”和节点名的组合作为该类的类名。默认包名可以通过构造时或者setPackageName设置。
	 * 
	 * @param xmlPath
	 * @param xsdPath
	 * @return
	 * @throws IllegalArgumentException
	 * @throws ClassCastException
	 */
	public <T> T parse(final String xmlPath, final String xsdPath)
			throws IllegalArgumentException, ClassCastException {
		return parse(new File(xmlPath), new File(xsdPath));
	}

	/**
	 * 解析一个xml文件，按节点映射规则生成对应的节点对象，返回根节点的对象。 节点映射规则：
	 * 1，先从节点名-类名映射规则Map中寻找。这个映射Map可以通过构造时或者setMap方法添加进来。查找时对节点名不区分大小写。
	 * 2，如果没有配置相应的映射规则，则使用“默认包名”和节点名的组合作为该类的类名。默认包名可以通过构造时或者setPackageName设置。
	 * 
	 * @param xmlFile
	 * @param xsdFile
	 * @return
	 * @throws IllegalArgumentException
	 */
	public <T> T parse(final File xmlFile, final File xsdFile)
			throws IllegalArgumentException, ClassCastException {
		if (!xmlFile.exists()) {
			throw new IllegalArgumentException("file not exsist");
		}

		if (xsdFile != null && !XmlValidator.checkXmlFile(xsdFile, xmlFile)) {
			throw new IllegalArgumentException("invalide xml file:"
					+ xmlFile.getName());
		}

		return parse(parseDocument(xmlFile));
	}

	public <T> T parse(final InputStream inputStream) {
		return parse(parseDocument(inputStream));
	}

	public <T> T parse(final Reader reader) {
		return parse(parseDocument(reader));
	}

	@SuppressWarnings("unchecked")
	private <T> T parse(final Document doc) {
		// 这个map是用来存放每个节点和其对应的java类对象的.注意key是首字母大写，其余小写的，即类名风格
		final Map<Element, Object> elementObjectMap = new HashMap<Element, Object>();

		// 广度优先遍历xml的dom树
		final Queue<Element> elements = new LinkedList<Element>();

		final Element root = doc.getRootElement();
		elements.add(root);
		while (!elements.isEmpty()) {
			final Element element = elements.poll();
			parseNode(elementObjectMap, element);
			elements.addAll(element.elements());
		}

		return (T) elementObjectMap.get(root);
	}

	/**
	 * @param elementObjectMap
	 * @param node
	 */
	protected void parseNode(final Map<Element, Object> elementObjectMap,
			final Element node) {
		String nodeName = node.getName();
		if (nodeName == null) {
			// 根据dom4j的注释文档，CDATA元素和文本元素（比如注释）是没有nodeName的。暂时先不处理这些
		} else {
			nodeName = Character.toUpperCase(nodeName.charAt(0))
					+ nodeName.substring(1);
			// 根据配置中设定的class的名字反射一个节点对象出来。
			final Object object = createObjectForNode(nodeName);
			elementObjectMap.put(node, object);

			// 把节点的attribute放到节点对象里
			putAttributeToObject(node, object);

			// 把节点的内容放到节点对象里
			putContentToObject(node, object);

			// 放到父节点的对象里面去
			putObjectToParent(elementObjectMap, node, object);
		}
	}

	protected void putContentToObject(final Element node, final Object object) {
		final String content = node.getText();
		final Method method = ClassUtil
				.findMethodIgnoreCaseAndArgsTypesAssigned(object.getClass()
						.getMethods(), "set", String.class);//
		if (method != null) {
			ClassUtil.putFieldToObject(method, object, content);
		}
	}

	/**
	 * @param nodeName
	 * @return
	 */
	protected Object createObjectForNode(final String nodeName) {
		String className = nodeNameClassNameMap.get(nodeName);
		if (className == null) {// map里没有特殊指定映射类型，就用package
			className = packageName + Character.toUpperCase(nodeName.charAt(0))
					+ nodeName.substring(1);
		}
		return ClassUtil.newInstance(className);
	}

	/**
	 * @param node
	 * @param object
	 */
	protected void putAttributeToObject(final Element node, final Object object) {
		@SuppressWarnings("unchecked")
		final List<Attribute> attributes = node.attributes();
		for (final Attribute attribute : attributes) {
			final String attrName = attribute.getName();
			final String attrValue = attribute.getValue();
			// FIXME:没有根据attrValue进行类型自适应
			final Method method = ClassUtil
					.findMethodIgnoreCaseAndArgsTypesAssigned(object.getClass()
							.getMethods(), ClassUtil.toSetterName(attrName),
							String.class);//
			// .getSingleArgMethod(object.getClass(), String.class,
			// ClassUtil.toSetterName(attrName));
			if (method != null) {
				ClassUtil.putFieldToObject(method, object, attrValue);
			}
		}
	}

	/**
	 * @param elementObjectMap
	 * @param node
	 * @param object
	 */
	protected void putObjectToParent(
			final Map<Element, Object> elementObjectMap, final Element node,
			final Object object) {
		final Element parentElement = node.getParent();
		if (parentElement == null) {// root，也有可能是一些其他情况。详见dom4j文档
			// do nothing
		} else {
			final Object parent = elementObjectMap.get(parentElement);
			if (parent == null) {
				// 理论上不会到这里
			} else {
				ClassUtil.setField(parent, object);
			}
		}
	}

	protected Document parseDocument(final Reader reader)
			throws IllegalArgumentException {
		final SAXReader saxReader = new SAXReader();
		try {
			return saxReader.read(reader);
		} catch (final DocumentException e) {
			throw new IllegalArgumentException(e);
		}
	}

	protected Document parseDocument(final InputStream inputStream)
			throws IllegalArgumentException {
		final SAXReader saxReader = new SAXReader();
		try {
			return saxReader.read(inputStream);
		} catch (final DocumentException e) {
			throw new IllegalArgumentException(e);
		}
	}

	protected Document parseDocument(final File xmlFile)
			throws IllegalArgumentException {
		final SAXReader saxReader = new SAXReader();
		try {
			return saxReader.read(xmlFile);
		} catch (final DocumentException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(final String packageName) {
		this.packageName = packageName.endsWith(".") ? packageName
				: packageName + ".";
	}

	public Map<String, String> getNodeNameClassNameMap() {
		return nodeNameClassNameMap;
	}

	public void setNodeNameClassNameMap(final Map<String, String> map) {
		nodeNameClassNameMap.putAll(map);
	}

	public static void main(final String[] args) {

	}
}
