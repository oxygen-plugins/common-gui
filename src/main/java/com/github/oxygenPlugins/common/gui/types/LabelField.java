package com.github.oxygenPlugins.common.gui.types;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.github.oxygenPlugins.common.gui.types.converter.TypeConverter;
import com.github.oxygenPlugins.common.gui.types.panels.FocusTraversalField;
import com.github.oxygenPlugins.common.gui.types.panels._EntryPanel;

public class LabelField extends FocusTraversalField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4827605903105996871L;
	public static final String NULL_LABEL = "(no value)";

	private final TypeConverter type;
	private Object value;
	private final ArrayList<_ValueListener> valueListener = new ArrayList<_ValueListener>();
	
	private _EntryPanel entryPanel = null;

	LabelField(TypeConverter type, Container owner) {
		this(null, null, type, owner);
	}

	LabelField(JComponent prevComponent, JComponent nextComponent, TypeConverter type, Container owner) {
		super(prevComponent, nextComponent);
		this.setOpaque(true);
		this.type = type;
		this.setText(type.convertToString(type.getDefault()));
		this.setHorizontalAlignment(JTextField.CENTER);
		VerifierFactory.addVerifier(type, this, owner);
		
		removeAllFocusListener();

	}

	public TypeConverter getType() {
		return type;
	}

	public String getValueAsString(){
		return getValueAsString(type.convertToString(type.getDefault()));
	}
	public String getValueAsString(String defaultValue) {
		String val = type.convertToString(this.value);
		if (val == null) {
			return defaultValue;
		}
		return val;
	}

	@Override
	public void setText(String text) {
		if (this.type != null) {
			setValue(this.type.convertValue(text));
			
			this.setStyle(Font.PLAIN);
			this.setBackground(Color.WHITE);
			this.setForeground(Color.BLACK);
			
			if(value == null){
				this.setForeground(Color.RED);
			} else if (isValueDefault()) {
				this.setForeground(Color.GRAY);
			}
			
			if(text == null){
				text = NULL_LABEL;
				this.setStyle(Font.ITALIC);
			}
			
			
		}
		this.setToolTipText(text);
		super.setText(text);

		checkMinimumSize();
	}

	private boolean isValueDefault() {
//		string compare as date / time comparison does not work
		
		if(!type.hasDefault()){
			return false;
		}
		String defAsString = type.convertToString(type.getDefault());
		String valAsString = type.convertToString(this.value);
		return defAsString.equals(valAsString);
	}

	private void setValue(Object value) {
		Object oldVal = this.value;
		this.value = value;

		for (_ValueListener vl : valueListener) {
			vl.valueChanged(this.value, oldVal);
		}
	}

	@Override
	public void setMinimumSize(Dimension minimumSize) {
		// TODO Auto-generated method stub
		super.setMinimumSize(minimumSize);

		checkMinimumSize();
	}

	private void checkMinimumSize() {
		int w = getWidth();
		int h = getHeight();
		Dimension minD = getMinimumSize();
		w = minD.width > w ? minD.width : w;
		h = minD.height > h ? minD.height : h;

		this.setPreferredSize(new Dimension(w, h));

	}

	private void setStyle(int style) {
		Font FONT = new JLabel().getFont();
		if (FONT != null) {
			Font font = new Font(FONT.getName(), style, FONT.getSize());
			this.setFont(font);
		}
	}

	public void addValueListener(_ValueListener listener) {
		this.valueListener.add(listener);
	}

	public void removeValueListener(_ValueListener listener) {
		this.valueListener.remove(listener);
	}
	
	
//	Focus
	
	public void activateVerifier(){
		if(this.entryPanel != null){
			entryPanel.activate();
		}
	}
	
	@Override
	protected void myFocus(JComponent comp) {
		if(comp instanceof LabelField){
			LabelField compLabelField = (LabelField) comp;
			compLabelField.activateVerifier();
		} else {
			super.myFocus(comp);
		}
	}
	
	@Override
	public synchronized void addMouseListener(MouseListener l) {
		if(l instanceof _EntryPanel){
			this.entryPanel = (_EntryPanel) l;
		}
		super.addMouseListener(l);
		
		
	}

}
