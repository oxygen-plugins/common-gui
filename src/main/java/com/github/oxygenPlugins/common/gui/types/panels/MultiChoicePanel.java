package com.github.oxygenPlugins.common.gui.types.panels;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.gui.swing.SwingUtil;
import com.github.oxygenPlugins.common.gui.types.LabelField;

//
public class MultiChoicePanel extends JPanel implements MouseListener, FocusListener, _EntryPanel {
	private static final long serialVersionUID = 2661956887580911488L;
	private final GridBagLayout gbl;

	private final int minWidth;
	private final int minHeight = 30;

	private final int maxHeight = 30;
	private final int maxWidth = 0;

	private JDialog dialog;
	// private final Border defaultBorder = BorderFactory.createBevelBorder(
	// BevelBorder.RAISED, new Color(240, 240, 240), new Color(150, 150,
	// 150));
	private final Border outlineBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED, new Color(110, 110, 110),
			new Color(200, 200, 200));
	// private final Border selectBorder = BorderFactory.createBevelBorder(
	// BevelBorder.RAISED, new Color(30, 20, 120),
	// new Color(110, 100, 200));
	private LabelField textField;

	private final ArrayList<String> valueList;

	private int value;
	private final int initialValue;

	private JComboBox<String> entryBox;
	private final Container owner;

	private final JPanel buttonPanel = new JPanel();
	private final JButton okBtn;
	@SuppressWarnings("unused")
	private final JButton cancelBtn;
	private final JButton clearBtn;
	private final boolean hasNull;

	private class PanelButton extends JButton {
		private static final long serialVersionUID = 1699184718806511284L;

		public PanelButton(Icon i) {
			this.setIcon(i);
			Dimension dim = new Dimension(i.getIconWidth() + 1, i.getIconHeight() + 1);
			this.setMinimumSize(dim);
			this.setSize(dim);
		}

		public PanelButton(String text) {
			super(text);
		}
	}

	public MultiChoicePanel(final LabelField field, Container owner, String[] values) {
		this(field, owner, values, false);
	}

	private int getMaxValueLength(String[] values){
		int length = 15;
		for (String val: values){
			length = val.length() > length ? val.length() : length;
		}

		return length;
	}

	public MultiChoicePanel(final LabelField field, Container owner, String[] values, boolean isNullSelectable) {
		this.owner = owner;
		this.textField = field;
		textField.setHorizontalAlignment(JTextField.CENTER);
		this.minWidth = getMaxValueLength(values) * 12;
		this.valueList = new ArrayList<String>();
		this.hasNull = !field.getType().hasDefault();

		if (isNullSelectable && hasNull) {
			valueList.add(LabelField.NULL_LABEL);
		}
		Collections.addAll(this.valueList, values);

		initialValue = this.valueList.indexOf(field.getText());
		value = initialValue;

		// if(field.isEnabled()){
		//
		// } else {
		// initialValue = VALUE_NULL;
		// value = VALUE_NULL;
		// }
		setBorder(outlineBorder);
		setBackground(Color.WHITE);
		gbl = new GridBagLayout();
		this.setLayout(gbl);

		GridBagLayout buttonGbl = new GridBagLayout();
		buttonPanel.setLayout(buttonGbl);
		buttonPanel.setVisible(false);

		if (IconMap.ICONS == null) {
			try {
				IconMap.ICONS = new IconMap();
			} catch (IOException e) {
			}
		}

		if (IconMap.ICONS != null) {
			IconMap icons = IconMap.ICONS;
			this.okBtn = new PanelButton(icons.getIcon(2, 10));
			this.clearBtn = new PanelButton(icons.getIcon(10, 11));
			this.cancelBtn = new PanelButton(icons.getIcon(0, 10));
		} else {
			this.okBtn = new PanelButton("ok");
			this.clearBtn = new PanelButton("x");
			this.cancelBtn = new PanelButton("c");
		}

		this.okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		this.clearBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose(true);
			}
		});

		// this.okBtn.setVisible(false);
		// this.clearBtn.setVisible(false);
		// this.cancelBtn.setVisible(false);

		entryBox = new JComboBox<String>(this.valueList.toArray(new String[this.valueList.size()]));
		entryBox.setSelectedIndex(value);
		entryBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent ie) {
				if (dialog == null)
					return;
				if (dialog.isVisible()) {
					// dispose();
				}
			}
		});
		

		entryBox.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					dispose();
				} else if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
					dispose(true);
					textField.parentFocus();
				} else if (e.getKeyChar() == KeyEvent.VK_TAB) {

					dispose();

					if (e.isShiftDown()) {
						textField.prevFocus();
					} else {
						textField.myNextFocus();
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		textField.setFocusable(true);
		entryBox.setFocusTraversalKeysEnabled(false);
		textField.removeAllFocusListener();
		textField.addFocusListener(this);

		SwingUtil.addComponent(this, gbl, entryBox, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 0));

		// SwingUtil.addComponent(this, gbl, okBtn,
		// 1, 0, 1, 1, 0.0, 1.0,
		// GridBagConstraints.NORTHEAST, GridBagConstraints.VERTICAL, new
		// Insets(3, 0, 0, 3));

		// SwingUtil.addComponent(this, gbl, cancelBtn,
		// 1, 1, 1, 1, 0.0, 1.0,
		// GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0,
		// 0, 0, 3));

		SwingUtil.addComponent(this, gbl, okBtn, 1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.VERTICAL, new Insets(0, 0, 3, 3));
		
		SwingUtil.addComponent(this, gbl, clearBtn, 2, 0, 1, 1, 0.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.VERTICAL, new Insets(0, 0, 3, 3));

		MouseListener ml = new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				// switchToField();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

				// switchToButtons();
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				dispose();
			}
		};

		addMouseListener(ml);
	}

	protected void setText() {
		// int value = this.entryBox.getSelectedIndex();
		// boolean isNull = isNull(value);
		// this.textField.setEnabled(!isNull);
		this.textField.setText(convert(this.entryBox.getSelectedIndex()));
		textField.repaint();
	}

	private void getText() {
		String text = textField.getText();
		value = convert(text);
		if (value < 0) {
			value = 0;
			text = LabelField.NULL_LABEL;
		}
		entryBox.setSelectedItem(text);
	}

	private boolean isNull(int value) {
		if (value >= valueList.size())
			return true;
		if (entryBox.getItemAt(value) == LabelField.NULL_LABEL)
			return true;
		if (value < 0)
			return true;
		return false;
	}

	private String convert(int value) {
		if (isNull(value))
			return null;
		return valueList.get(value);
	}

	private int convert(String valueString) {
		int i = 0;
		for (String label : valueList) {
			if (valueString.equals(label))
				return i;
			i++;
		}
		return -1;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		activate();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void activate() {
		// if (textField.isEnabled()) {
		// if (dialog != null) {
		// dispose();
		// }
		this.getText();

		if (dialog == null) {
			if (owner instanceof Dialog) {
				dialog = new JDialog((Dialog) owner);
			} else if (owner instanceof Frame) {
				dialog = new JDialog((Frame) owner);
			} else if (owner instanceof Window) {
				dialog = new JDialog((Window) owner);
			} else {
				dialog = new JDialog(new JFrame());
			}
		}
		dialog.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent arg0) {
				if (dialog != null)
					dispose();
			}

			@Override
			public void windowGainedFocus(WindowEvent arg0) {
			}
		});
		dialog.setUndecorated(true);
		dialog.setMinimumSize(new Dimension(minWidth, minHeight));
		Point tfLoc = textField.getLocationOnScreen();
		int finalWidth = textField.getWidth() * 2;
		int finalHeight = textField.getHeight() * 2;

		finalHeight = this.maxHeight > 0 && this.maxHeight < finalHeight ? this.maxHeight : finalHeight;

		finalHeight = this.minHeight > 0 && this.minHeight > finalHeight ? this.minHeight : finalHeight;

		finalWidth = this.maxWidth > 0 && this.maxWidth < finalWidth ? this.maxWidth : finalWidth;

		finalWidth = this.minWidth > 0 && this.minWidth > finalWidth ? this.minWidth : finalWidth;

		dialog.setMaximumSize(new Dimension(finalWidth, finalHeight));

		dialog.setSize(finalWidth, finalHeight);
		Point finalLoc = new Point((int) (tfLoc.x - finalWidth * 0.25), (int) (tfLoc.y - finalHeight * 0.25));
		finalLoc = SwingUtil.moveOnScreen(finalLoc, finalWidth, finalHeight);
		dialog.setLocation(finalLoc);
		dialog.add(this);
		dialog.setModal(false);
		dialog.setVisible(true);
		// }
	}

	private void dispose() {
		dispose(false);
	}

	private void dispose(boolean unsetText) {
		if (unsetText) {
			this.textField.setText(convert(initialValue));
			this.textField.repaint();
		} else {
			this.setText();
		}
		if (dialog != null) {
			this.dialog.dispose();
			this.dialog = null;
		}
		textField.parentFocus();
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		if (dialog == null) {
			// activate();
		}
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// dispose();
	}
}
