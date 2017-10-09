package com.github.oxygenPlugins.common.gui.types;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFormattedTextField;

import com.github.oxygenPlugins.common.gui.types.panels.StringPanel;

public class ValidCharVerifier implements _Verifier, KeyListener {
	protected final char[] validChars;
	protected final char[] startChars;
	protected JFormattedTextField field = new JFormattedTextField();
	private final boolean charsAreValid;
	private String errorValue;
	

	public ValidCharVerifier(String validStrings, String validStart) {
		this(validStrings, validStart, true);
	}

	public ValidCharVerifier(String validStrings, String validStart, boolean charsAreValid) {
		validChars = validStrings.toCharArray();
		this.startChars = validStart.toCharArray();
		this.charsAreValid = charsAreValid;
		this.errorValue = (charsAreValid ? validChars[0] : "") + "";
	}

	@Override
	public void setVerifier(JFormattedTextField field, Container owner) {
		this.field = field;
		if(!checkValue(field.getText())){
			field.setText(errorValue);
		}
		field.addKeyListener(this);
	}

	protected JFormattedTextField getField() {
		return this.field;
	}

	public void keyReleased(KeyEvent ke) {
		if(!checkValue(field.getText())){
			field.setText(errorValue);
		}
	}

	public void keyTyped(KeyEvent keyEvent) {
		// TODO Auto-generated method stub
		int selStart = field.getSelectionStart();
		int selEnd = field.getSelectionEnd();
		
		String newValue = field.getText();
		newValue = newValue.substring(0, selStart) + keyEvent.getKeyChar() + newValue.substring(selEnd);
		
		if (!checkValue(newValue)){
			keyEvent.consume();
		}
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	public boolean checkValue(String text) {
		char[] chars = text.toCharArray();
		
		if(chars.length == 0){
			return !charsAreValid;
		}

		for (int ci = 0; ci < chars.length; ci++) {
			boolean isValid = !charsAreValid;
			char c = chars[ci];
			char[] allStartChars = (new String(startChars) + new String(validChars)).toCharArray();
			for (char sc : (ci == 0 ? allStartChars : this.validChars)) {
				if (sc == c) {
					isValid = charsAreValid;
					break;
				}
			}
			if (!isValid) {
				return false;
			}

		}

		return true;
	}

	@Override
	public _Verifier getNewInstance() {
		// if(this instanceof IntegerVerifier)
		// return new IntegerVerifier(String.copyValueOf(validChars),
		// String.copyValueOf(startChars));
		return new ValidCharVerifier(String.copyValueOf(validChars), String.copyValueOf(startChars), charsAreValid);
	}

	@Override
	public void setVerifier(LabelField field, Container owner) {
		if(field.getValueAsString() == null){
			field.setText(this.errorValue);
		}
		
		field.addMouseListener(new StringPanel(field, this, owner));
	}

}
