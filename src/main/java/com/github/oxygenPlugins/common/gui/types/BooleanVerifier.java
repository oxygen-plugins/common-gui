package com.github.oxygenPlugins.common.gui.types;

import java.awt.Container;

import javax.swing.JFormattedTextField;

import com.github.oxygenPlugins.common.gui.types.panels.BooleanPanel;
import com.github.oxygenPlugins.common.gui.types.panels.BooleanPanel2;
import com.github.oxygenPlugins.common.gui.types.panels.StringPanel;

public class BooleanVerifier extends ValidCharVerifier {
	public BooleanVerifier() {
		this("");
	}
	public BooleanVerifier(String invalidStrings){
		this(invalidStrings, "");
	}
	public BooleanVerifier(String invalidStrings, String invalidStarts){
		super(invalidStrings, invalidStarts, false);
	}
	
	@Override
	public void setVerifier(JFormattedTextField field, Container owner) {
		BooleanPanel2 sp = new BooleanPanel2(field, owner);
//		field.removeFocusListener(sp);
		field.addMouseListener(sp);
//		field.removeFocusListener(sp);
//		field.addFocusListener(sp);
		super.setVerifier(field, owner);
	}
	@Override
	public _Verifier getNewInstance() {
		return new BooleanVerifier();
	}
}
