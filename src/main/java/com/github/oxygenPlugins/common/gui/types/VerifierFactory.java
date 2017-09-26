package com.github.oxygenPlugins.common.gui.types;

import java.awt.Container;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JFormattedTextField;

import com.github.oxygenPlugins.common.gui.types.converter.EnumTypeConverter;
import com.github.oxygenPlugins.common.gui.types.converter.TypeConverter;
import com.github.oxygenPlugins.common.gui.types.panels._EntryPanel;


public class VerifierFactory {
	private static final String DEFAULT_TYPE = "xs:string";
	
	public static _Verifier addVerifier(TypeConverter typeConverter, JFormattedTextField field, Container owner) {
		return addVerifier(typeConverter, field, owner, true);
	}
	
	public static _Verifier addVerifier(EnumTypeConverter typeConv, JFormattedTextField field, Container owner, boolean entryHelp) {
		MultiChoiceVerifier mcVerifier = new MultiChoiceVerifier(typeConv.getEnumValuesAsString(), false);
		mcVerifier.setVerifier(field, owner, entryHelp);
		return mcVerifier;
	}
	public static _Verifier addVerifier(TypeConverter typeConv, JFormattedTextField field, Container owner, boolean entryHelp) {
		if(typeConv instanceof EnumTypeConverter){
			return addVerifier((EnumTypeConverter) typeConv, field, owner, entryHelp);
		}
		
		String type = typeConv.getType();
		type = type.replaceFirst("[\\?\\*\\+]$", "");
		type = typeVerifierMap.containsKey(type) ? type : DEFAULT_TYPE;
		if(typeVerifierMap.containsKey(type)){
//			field.setEditable(false);
			_Verifier verifier = typeVerifierMap.get(type).getNewInstance();
			verifier.setVerifier(field, owner, entryHelp);
			return verifier;
		}
		return null;
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
	
	public static void fireEntryPanel(JFormattedTextField field){
		for (MouseListener ml : field.getMouseListeners()) {
			if(ml instanceof _EntryPanel){
				_EntryPanel entryPanel = (_EntryPanel) ml;
				entryPanel.activate();
				break;
			}
		}
	}
}
