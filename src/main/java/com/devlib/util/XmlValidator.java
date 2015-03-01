package com.devlib.util;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;

/**
 * Validate the deep configuration file format according to associated XML
 * schema.
 * 
 * @author guangfeng
 * @author wenjian.liang
 */
public class XmlValidator {
	private static Logger logger = Logger.getLogger(XmlValidator.class);

	/**
	 * Check the configuration file.
	 * 
	 * @param schemaFile
	 * @param xmlFile
	 * @return True if the configuration file format is correct; otherwise
	 *         false. exceptions caught and logged as the caller did before.
	 */
	public static boolean checkXmlFile(final String schemaFile,
			final String xmlFile) {
		return checkXmlFile(schemaFile, new File(xmlFile));
	}

	/**
	 * 校验xml文件是否符合schema文件指定的格式
	 * 
	 * @param schemaFile
	 *            schema文件的路径
	 * @param xmlFile
	 *            xml文件
	 * @return
	 */
	public static boolean checkXmlFile(final String schemaFile,
			final File xmlFile) {
		return checkXmlFile(new File(schemaFile), xmlFile);
	}

	/**
	 * 校验xml文件是否符合schema文件指定的格式
	 * 
	 * @param schemaFile
	 *            要使用的schema文件
	 * @param xmlFile
	 *            要检验的xml文件
	 * @return
	 */
	public static boolean checkXmlFile(final File schemaFile, final File xmlFile) {
		try {
			final SchemaFactory schemaFactory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			final Schema schema = schemaFactory.newSchema(schemaFile);
			final Validator validator = schema.newValidator();

			validator.validate(new StreamSource(xmlFile));

			return true;
		} catch (Exception e) {
			ExceptionHandler.handleException(e, logger);
		}
		return false;
	}
}
