package com.github.oxygenPlugins.common.gui.types.converter;

import java.util.HashMap;

import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.ValidationException;
import net.sf.saxon.value.DateTimeValue;
import net.sf.saxon.value.DateValue;
import net.sf.saxon.value.TimeValue;

@SuppressWarnings("rawtypes")
public class TypeConverter {
	private String type;
	private Object defaultValue;

	public TypeConverter(String type, Object defaultValue) {
		this.type = type;
		this.defaultValue = defaultValue;
	}

	public TypeConverter(String type, String defaultValue) {
		this(type, TypeConverter.convertValue(defaultValue, type));
	}

	public Object convertValue(String value) {
		return TypeConverter.convertValue(value, this.type);
	}

	protected static Object convertValue(String value, String type) {
		Object result;
		try {
			if (value == null) {
				result = null;
			} else if (type.equals("xs:dateTime")) {
				result = getClass(type).cast(getDate(value));
			} else if (type.equals("xs:time")) {
				result = getClass(type).cast(getTime(value));
			} else if (type.equals("xs:date")) {
				result = getClass(type).cast(getDate(value));
			} else if (type.equals("xs:dateTime")) {
				result = getClass(type).cast(getDateTime(value));
			} else if (type.equals("xs:short")){
				short val = Short.parseShort(value);
				result = val;
			} else if (type.equals("xs:long")){
				long val = Long.parseLong(value);
				result = val;
			} else if (type.equals("xs:integer") || type.equals("xs:short")
					|| type.equals("xs:unsignedInt")) {
				int valInt = value.equals("") ? 0 : Integer.valueOf(value);
				result = getClass(type).cast(valInt);
			} else if (type.equals("xs:int")){
				int val = Integer.valueOf(value);
				result = val;
			} else if ( type.equals("xs:decimal")
					|| type.equals("xs:unsignedShort") || type.equals("xs:double")) {
				double valDouble = value.equals("") ? 0.0 : Double.parseDouble(value);
				result = getClass(type).cast(valDouble);
			} else {
				result = getClass(type).cast(value);
			}
			return result;
		} catch (Exception e) {
			return null;
		}

	}

	private static Class getClass(String type) {
		if (typeVerifierMap.containsKey(type)) {
			return typeVerifierMap.get(type);
		} else {
			return String.class;
		}
	}

	private static Object getTime(String value) {
		return TimeValue.makeTimeValue(value);
	}

	private static DateTimeValue getDateTime(String value) {
		if (value.contains("T")) {
			String[] split = value.split("T");
			try {
				return DateTimeValue.makeDateTimeValue(getDate(split[0]), (TimeValue) getTime(split[1]));
			} catch (XPathException e) {
			}
		}
		return null;
	}

	private static DateValue getDate(String value) {
		try {
			return new DateValue(value);
		} catch (ValidationException e) {
			return null;
		}
	}

	private static HashMap<String, Class> typeVerifierMap = new HashMap<String, Class>();
	static {
		typeVerifierMap.put(null, String.class);
		typeVerifierMap.put("xs:string", String.class);
		typeVerifierMap.put("xs:int", int.class);
		typeVerifierMap.put("xs:integer", Integer.class);
		typeVerifierMap.put("xs:short", Integer.class);
		typeVerifierMap.put("xs:long", Long.class);
		typeVerifierMap.put("xs:decimal", Double.class);
		typeVerifierMap.put("xs:double", Double.class);
		typeVerifierMap.put("xs:unsignedInt", Integer.class);
		typeVerifierMap.put("xs:unsignedShort", Double.class);
		typeVerifierMap.put("sqf:color", String.class);
		typeVerifierMap.put("xs:date", DateValue.class);
		typeVerifierMap.put("xs:time", TimeValue.class);
		typeVerifierMap.put("xs:dateTime", DateTimeValue.class);
	}

	public String convertToString(Object value) {
		if (value == null)
			return null;

		if (value instanceof DateValue) {
			DateValue dv = (DateValue) value;
			return dv.getPrimitiveStringValue().toString();
		}
		if (value instanceof TimeValue) {
			TimeValue time = (TimeValue) value;
			return time.getPrimitiveStringValue().toString();
		}
		if (value instanceof DateTimeValue) {
			DateTimeValue dateTime = (DateTimeValue) value;
			return dateTime.getPrimitiveStringValue().toString();
		}
		return value.toString();
	}

	public String getType() {
		return this.type;
	}


	public boolean hasDefault() {
		// TODO Auto-generated method stub
		return this.defaultValue != null;
	}

	public Object getDefault() {
		// TODO Auto-generated method stub
		return this.defaultValue;
	}

}
