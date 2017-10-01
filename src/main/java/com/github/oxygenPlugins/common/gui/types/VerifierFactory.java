package com.github.oxygenPlugins.common.gui.types;

import java.awt.Container;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

import com.github.oxygenPlugins.common.gui.types.converter.EnumTypeConverter;
import com.github.oxygenPlugins.common.gui.types.converter.TypeConverter;
import com.github.oxygenPlugins.common.gui.types.panels._EntryPanel;


public class VerifierFactory {
	private static final String DEFAULT_TYPE = "xs:string";
	
	
	public static LabelField createEntryLabel(JComponent prevComponent, JComponent nextComponent, TypeConverter typeConv, Container owner){
		return new LabelField(prevComponent, nextComponent, typeConv, owner);
	}
	
	public static _Verifier addVerifier(EnumTypeConverter typeConv, LabelField field, Container owner) {
		MultiChoiceVerifier mcVerifier = new MultiChoiceVerifier(typeConv.getEnumValuesAsString(), false);
		mcVerifier.setVerifier(field, owner);
		return mcVerifier;
	}
	public static _Verifier addVerifier(TypeConverter typeConv, LabelField field, Container owner) {
		if(typeConv instanceof EnumTypeConverter){
			return addVerifier((EnumTypeConverter) typeConv, field, owner);
		}
		
		String type = typeConv.getType();
		type = type.replaceFirst("[\\?\\*\\+]$", "");
		type = typeVerifierMap.containsKey(type) ? type : DEFAULT_TYPE;
		if(typeVerifierMap.containsKey(type)){
//			field.setEditable(false);
			_Verifier verifier = getNewVerifier(type);
			verifier.setVerifier(field, owner);
			return verifier;
		}
		return null;
	}

	public static _Verifier getNewVerifier(String type) {
		return typeVerifierMap.get(type).getNewInstance();
	}
	
	

	@SuppressWarnings("unused")
	private static String[] types = new String[] { "float", "double",
			"boolean", "byte", "QName", "dateTime", "hexBinary",
			"base64Binary", "hexBinary",
			"unsignedByte", "time", "g", "anySimpleType", "duration",
			"NOTATION" };
	private static HashMap<String, _Verifier> typeVerifierMap = new HashMap<String, _Verifier>();
	static {
		typeVerifierMap.put(null, new StringVerifier());
		typeVerifierMap.put("xs:string", new StringVerifier());
		typeVerifierMap.put("xs:boolean", new BooleanVerifier());
		typeVerifierMap.put("xs:int", new IntegerVerifier("","+-"));
		typeVerifierMap.put("xs:integer", new IntegerVerifier("","+-"));
		typeVerifierMap.put("xs:short", new IntegerVerifier("","+-"));
		typeVerifierMap.put("xs:long", new IntegerVerifier("","+-"));
		typeVerifierMap.put("xs:decimal", new IntegerVerifier(".","+-"));
		typeVerifierMap.put("xs:unsignedInt", new IntegerVerifier("","+"));
		typeVerifierMap.put("xs:unsignedShort", new IntegerVerifier("","+"));
		typeVerifierMap.put("sqf:color", new ColorVerifier());
		typeVerifierMap.put("xs:date", new CalendarVerifier());
		typeVerifierMap.put("xs:dateTime", new CalendarTimeVerifier());
		typeVerifierMap.put("xs:time", new TimeVerifier());
	}
	
	public static void installVerifier(String type, _Verifier verifier){
		typeVerifierMap.put(type, verifier);
	}
	
}
