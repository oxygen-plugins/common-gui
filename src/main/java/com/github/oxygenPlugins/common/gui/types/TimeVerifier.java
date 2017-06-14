package com.github.oxygenPlugins.common.gui.types;

import java.awt.Container;

import javax.swing.JFormattedTextField;

import com.github.oxygenPlugins.common.gui.types.panels.TimePanel;

public class TimeVerifier implements _Verifier {


	@Override
	public void setVerifier(JFormattedTextField field, Container owner) {
		field.addMouseListener(new TimePanel(field, 11, owner));
	}

	@Override
	public _Verifier getNewInstance() {
		// TODO Auto-generated method stub
		return new TimeVerifier();
	}

	@Override
	public void setVerifier(JFormattedTextField field, Container owner, boolean entryHelp) {
		setVerifier(field, owner);
	}

}
