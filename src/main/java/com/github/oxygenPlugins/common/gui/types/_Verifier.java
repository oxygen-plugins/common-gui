package com.github.oxygenPlugins.common.gui.types;

import java.awt.Container;

import javax.swing.JFormattedTextField;

public interface _Verifier {
	public void setVerifier(LabelField field, Container owner);
	public _Verifier getNewInstance();
	public void setVerifier(JFormattedTextField field, Container owner);
}
