package com.github.oxygenPlugins.common.gui.types;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.github.oxygenPlugins.common.gui.types.converter.TypeConverter;
import com.github.oxygenPlugins.common.gui.types.panels.FocusTraversalField;
import com.github.oxygenPlugins.common.gui.types.panels.StringPanel;

public class LabelField extends FocusTraversalField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4827605903105996871L;
	public static final String NULL_LABEL = "(no value)";

	private final TypeConverter type;
	private Object value;

	LabelField(TypeConverter type, Container owner) {
		this(null, null, type, owner);
	}
	LabelField(JComponent prevComponent, JComponent nextComponent, TypeConverter type, Container owner) {
		super(prevComponent, nextComponent);
		this.setOpaque(true);
		this.type = type;
		this.value = type.getDefault();
		this.setText(type.convertToString(value));

		VerifierFactory.addVerifier(type, this, owner);

	}

	public TypeConverter getType() {
		return type;
	}
	
	public String getValueAsString(String defaultValue){
		String val = type.convertToString(this.value);
		if(val == null){
			return defaultValue;
		}
		return val;
	}
	

	@Override
	public void setText(String text) {
		if (this.type != null) {
			value = this.type.convertValue(text);
			
			if (text == null || text.equals("")) {
				text = NULL_LABEL;
				this.setStyle(Font.ITALIC);
				this.setBackground(Color.WHITE);
			} else if (value == null) {
				this.setStyle(Font.PLAIN);
				this.setBackground(Color.RED);
			} else {
				this.setStyle(Font.PLAIN);
				this.setBackground(Color.WHITE);
			}
		}

		super.setText(text);
		
		checkMinimumSize();
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

}
