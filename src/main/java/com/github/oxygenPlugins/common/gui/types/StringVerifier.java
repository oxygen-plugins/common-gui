package com.github.oxygenPlugins.common.gui.types;

import java.awt.Container;

import javax.swing.JFormattedTextField;

import com.github.oxygenPlugins.common.gui.types.panels.StringPanel;

public class StringVerifier extends ValidCharVerifier {
	public StringVerifier() {
		this("");
	}
	public StringVerifier(String invalidStrings){
		this(invalidStrings, "");
	}
	public StringVerifier(String invalidStrings, String invalidStarts){
		super(invalidStrings, invalidStarts, false);
	}
	
	@Override
	public void setVerifier(JFormattedTextField field, Container owner) {
		StringPanel sp = new StringPanel(field, owner);
//		field.removeFocusListener(sp);
		field.addMouseListener(sp);
//		field.removeFocusListener(sp);
//		field.addFocusListener(sp);
		super.setVerifier(field, owner);
	}
	@Override
	public _Verifier getNewInstance() {
		return new StringVerifier();
	}
}
