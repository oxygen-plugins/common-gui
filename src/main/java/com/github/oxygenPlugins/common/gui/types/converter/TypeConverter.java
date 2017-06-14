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
	public TypeConverter(String type) {
		this.type = type;
		
	}

	public Object convertValue(String value){
		Object result;
		try{
			if(value == null){
				result = null;
			} else if (type.equals("xs:dateTime")) {
				result = getClass(type).cast(getDate(value));
			} else if (type.equals("xs:time")) {
				result = getClass(type).cast(getTime(value));
			} else if (type.equals("xs:date")) {
				result = getClass(type).cast(getDate(value));
			} else if (type.equals("xs:dateTime")) {
				result = getClass(type).cast(getDateTime(value));
			} else if (type.equals("xs:integer")) {
				int valInt = value.equals("") ? 0 : Integer.valueOf(value);
				result = getClass(type).cast(valInt);
			} else {
				result = getClass(type).cast(value);
			}
			return result;
		} catch(ClassCastException e){
			return null;
		}
		
	}
	
	
	private Class getClass(String type){
		if(typeVerifierMap.containsKey(type)){
			return typeVerifierMap.get(type);
		} else {
			return String.class;
		}
	}
	
	private static Object getTime(String value){
		return TimeValue.makeTimeValue(value);
	}
	private static DateTimeValue getDateTime(String value){
		if(value.contains("T")){
			String[] split = value.split("T");
			try {
				return DateTimeValue.makeDateTimeValue(getDate(split[0]), (TimeValue) getTime(split[1]));
			} catch (XPathException e) {
			}
		}
		return null;
	}
	
	private static DateValue getDate(String value){
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
		typeVerifierMap.put("xs:int", Integer.class);
		typeVerifierMap.put("xs:integer", Integer.class);
		typeVerifierMap.put("xs:short", Double.class);
		typeVerifierMap.put("xs:long", Double.class);
		typeVerifierMap.put("xs:decimal", Double.class);
		typeVerifierMap.put("xs:unsignedInt", Double.class);
		typeVerifierMap.put("xs:unsignedShort", Double.class);
		typeVerifierMap.put("sqf:color", String.class);
		typeVerifierMap.put("xs:date", DateValue.class);
		typeVerifierMap.put("xs:time", TimeValue.class);
		typeVerifierMap.put("xs:dateTime", DateTimeValue.class);
	}
	public String convertToString(Object value) {
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
	
	public String getType(){
		return this.type;
	}
	
}
