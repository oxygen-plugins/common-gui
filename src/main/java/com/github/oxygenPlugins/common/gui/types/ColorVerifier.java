package com.github.oxygenPlugins.common.gui.types;


import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyEvent;

import javax.swing.JFormattedTextField;

import com.github.oxygenPlugins.common.gui.types.panels.ColorPanel;

public class ColorVerifier extends ValidCharVerifier {

	public ColorVerifier() {
		super("0123456789abcdefABCDEF", "");
	}
	@Override
	public void setVerifier(LabelField field, Container owner) {
		field.setOpaque(true);
		field.addMouseListener(new ColorPanel(field, owner));
	}



	public void keyReleased(KeyEvent keyEvent) {
		super.keyTyped(keyEvent);
		JFormattedTextField field = this.getField();
		String code = field.getText();
		Color c = field.getBackground();
		if (code.replaceAll("[\\dabcdefABCDEF]", "").equals("")
				&& code.length() == 6) {
			c = Color.decode("#" + code);
		}
		field.setBackground(c);
		if(c.getRed()+ c.getGreen() * 1.5  < 334)
			field.setForeground(Color.WHITE);
		else
			field.setForeground(Color.BLACK);
		field.repaint();
	}
	@Override
	public _Verifier getNewInstance() {
		return new ColorVerifier();
	}
}
