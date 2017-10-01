package com.github.oxygenPlugins.common.gui.types.panels;

import java.awt.Container;

import javax.swing.JFormattedTextField;

import com.github.oxygenPlugins.common.gui.types.LabelField;

public class BooleanPanel2 extends MultiChoicePanel {

	public BooleanPanel2(LabelField field, Container owner) {
		super(field, owner, new String[]{"true", "false"});
	}

}
