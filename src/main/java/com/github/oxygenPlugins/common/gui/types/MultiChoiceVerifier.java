package com.github.oxygenPlugins.common.gui.types;

import java.awt.Container;

import javax.swing.JFormattedTextField;

import com.github.oxygenPlugins.common.gui.types.panels.MultiChoicePanel;

public class MultiChoiceVerifier extends ValidCharVerifier {
	private final String[] values;
	private final boolean isNullSelectable;
	public MultiChoiceVerifier(String[] values, boolean isNullSelectable){
		super("", "", false);
		this.values = values;
		this.isNullSelectable = isNullSelectable;
	}
	
	@Override
	public void setVerifier(JFormattedTextField field, Container owner) {
	}
	@Override
	public _Verifier getNewInstance() {
		return new MultiChoiceVerifier(this.values, this.isNullSelectable);
	}
	
	@Override
	public void setVerifier(LabelField field, Container owner) {
		MultiChoicePanel panel = new MultiChoicePanel(field, owner, values);
		field.addMouseListener(panel);
	}
}
