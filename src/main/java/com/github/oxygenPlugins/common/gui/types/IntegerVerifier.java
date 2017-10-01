package com.github.oxygenPlugins.common.gui.types;


import java.awt.Container;

import javax.swing.JFormattedTextField;

import com.github.oxygenPlugins.common.gui.types.panels.StringPanel;

public class IntegerVerifier extends ValidCharVerifier {
	private final static String digits = "0123456789";
	public IntegerVerifier() {
		this("");
	}
	public IntegerVerifier(String vddValidStrings){
		this(digits + vddValidStrings, "");
	}
	public IntegerVerifier(String vddValidStrings, String startChars){
		super(digits + vddValidStrings, startChars);
	}
	
	@Override
	public void setVerifier(JFormattedTextField field, Container owner) {
		super.setVerifier(field, owner);
	}
	
	@Override
	public void setVerifier(LabelField field, Container owner) {
			field.addMouseListener(new StringPanel(field, this, owner));
	}
	
	@Override
	public _Verifier getNewInstance() {
		return new IntegerVerifier(String.copyValueOf(validChars), String.copyValueOf(startChars));
	}
}
