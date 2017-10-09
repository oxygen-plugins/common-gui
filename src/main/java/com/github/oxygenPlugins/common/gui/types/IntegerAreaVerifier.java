package com.github.oxygenPlugins.common.gui.types;


import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFormattedTextField;

import com.github.oxygenPlugins.common.gui.types.panels.StringPanel;

public class IntegerAreaVerifier implements _Verifier, KeyListener {
	
	protected final char[] validChars = "0123456789".toCharArray();
	private final int start;
	private final int end;
	
	private int errorValue;
	
	protected JFormattedTextField field = new JFormattedTextField();
	public IntegerAreaVerifier(int end) {
		this(0, end);
	}
	public IntegerAreaVerifier(int start, int end) {
		this.start = start;
		this.end = end;
		
		
		
		this.errorValue = start;
	}
	
	public void setErrorValue(int errorValue){
		this.errorValue = errorValue;
	}
	
	public boolean hasErrorValue(){
		return checkValue(this.errorValue);
	}
	
	private boolean checkValue(int value){
		return value >= start && value <= end;
	}
	
	
	@Override
	public void setVerifier(JFormattedTextField field, Container owner) {
		this.field = field;
		field.addKeyListener(this);
	}
	protected JFormattedTextField getField(){
		return this.field;
	}
	
	public void keyReleased(KeyEvent ke) {}
	public void keyTyped(KeyEvent keyEvent) {
		// TODO Auto-generated method stub
//		int pos = field.getCaret().getMark();
		int selStart = field.getSelectionStart();
		int selEnd = field.getSelectionEnd();
		char insert = keyEvent.getKeyChar();
		
		boolean consume = true;
		String newValue = field.getText();
		newValue = newValue.substring(0, selStart) + keyEvent.getKeyChar() + newValue.substring(selEnd);
		
		for (char valChar : this.validChars) {
			if(valChar == insert){
				consume = false;
				break;
			}
		}
		if(consume){
			keyEvent.consume();
 		} else {
 			int newValInt = Integer.parseInt(newValue);
 			if(newValInt > end || newValInt < start){
 				keyEvent.consume();
 			}
 		}
		boolean isValid = false;
		try {
			isValid = checkValue(Integer.parseInt(field.getText()));
		} catch (Exception e){
			System.err.println(e.getMessage());
		}
		
		if(!isValid){
			field.setText(errorValue + "");
		}
		
//		System.out.println("pos: " + pos);
//		System.out.println("selStart: " + selStart);
//		System.out.println("selEnd: " + selEnd);
		
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public _Verifier getNewInstance() {
//		if(this instanceof IntegerVerifier)
//			return new IntegerVerifier(String.copyValueOf(validChars), String.copyValueOf(startChars));
		return new IntegerAreaVerifier(start, end);
	}

	@Override
	public void setVerifier(LabelField field, Container owner) {
		field.addMouseListener(new StringPanel(field, this, owner));
	}

}
