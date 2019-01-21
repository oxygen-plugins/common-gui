package com.github.oxygenPlugins.common.gui.types.converter;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.oxygenPlugins.common.xml.xpath.XPathReader;


public class EnumTypeConverter extends TypeConverter {

	private final Object[] enumValues;
	private static XPathReader xpathreader = new XPathReader();

	private static String getNodeValue(Node node) {
		try {
			return xpathreader.getString(".", node);
		} catch (XPathExpressionException e) {
			return null;
		}
	}

	public EnumTypeConverter(String type, String[] values, int defaultValue) {
		super(type, TypeConverter.convertValue(values[defaultValue], type));
		// E N U M E R A T I O N
		enumValues = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			String enumVal = values[i];
			enumValues[i] = this.convertValue(enumVal);
		}

	}
	
	public EnumTypeConverter(String type, NodeList values, int defaultValue) {
		super(type, TypeConverter.convertValue(getNodeValue(values.item(defaultValue)), type));
		// E N U M E R A T I O N
		enumValues = new Object[values.getLength()];
		for (int i = 0; i < values.getLength(); i++) {
			Node enumVal = values.item(i);
			enumValues[i] = this.convertValue(getNodeValue(enumVal));
		}

	}

	public Object[] getEnumValues() {
		return enumValues;
	}

	public String[] getEnumValuesAsString() {
		String[] values = new String[enumValues.length];
		int i = 0;
		for (Object val : enumValues) {
			values[i++] = this.convertToString(val);
		}
		return values;
	}
}
