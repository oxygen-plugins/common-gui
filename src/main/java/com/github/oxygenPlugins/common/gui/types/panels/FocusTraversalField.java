package com.github.oxygenPlugins.common.gui.types.panels;

import java.awt.event.FocusListener;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class FocusTraversalField extends JLabel {
	
	private static final long serialVersionUID = 1L;
	private JComponent nextComponent = null;
	private JComponent prevComponent = null;
	private JComponent parentComponent;
	
	public FocusTraversalField(){}
	
	public FocusTraversalField(JComponent prevComponent, JComponent nextComponent){
		this.prevComponent = prevComponent;
		this.nextComponent = nextComponent;
		
	}
	
	public void myNextFocus(){
		if(nextComponent != null){
			myFocus(nextComponent);
		} else {
			this.transferFocus();
		}
	}
	
	public void setNextComponent(JComponent nextComp) {
		this.nextComponent = nextComp;
	}
	
	public void prevFocus(){
		if(prevComponent != null){
			myFocus(prevComponent);
		} else {
			this.transferFocusBackward();
		}
	}

	public void setPrevComponent(JComponent prevComp) {
		this.prevComponent = prevComp;
		if(prevComp instanceof FocusTraversalField){
			((FocusTraversalField) prevComp).setNextComponent(this);
		}
	}

	public void parentFocus(){
		myFocus(parentComponent);
	}

	public void setParentComponent(JComponent parentComp) {
		this.parentComponent = parentComp;
	}
	
	public void removeAllFocusListener(){
		
		FocusListener[] listeners = this.getFocusListeners();
		for (FocusListener listener : listeners) {
			this.removeFocusListener(listener);
		}
	}
	
	protected void myFocus(JComponent comp){
		if(comp != null){
			comp.requestFocus();
		}
	}
}
