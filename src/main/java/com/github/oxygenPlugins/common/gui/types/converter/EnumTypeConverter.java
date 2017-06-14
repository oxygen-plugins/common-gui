package com.github.oxygenPlugins.common.gui.types.converter;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nkutsche.common.xml.xpath.XPathReader;

public class EnumTypeConverter extends TypeConverter {

	private final Object[] enumValues;

	public EnumTypeConverter(String type, NodeList values) {
		super(type);
		XPathReader xpathreader = new XPathReader();
		// E N U M E R A T I O N
		enumValues = new Object[values.getLength()];
		for (int i = 0; i < values.getLength(); i++) {
			Node enumVal = values.item(i);
			try {
				enumValues[i] = this.convertValue(xpathreader.getString(".", enumVal));
			} catch (XPathExpressionException e) {
				enumValues[i] = null;
			}
		}

	}

	public Object[] getEnumValues() {
		return enumValues;
	}
	
	public String[] getEnumValuesAsString(){
		String[] values = new String[enumValues.length];
		int i = 0;
		for (Object val : enumValues) {
			values[i++] = this.convertToString(val);
		}
		return values;
	}
}
