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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.gui.swing.SwingUtil;
import com.github.oxygenPlugins.common.gui.types.LabelField;
import com.github.oxygenPlugins.common.gui.types._Verifier;

//
public class StringPanel extends JPanel implements MouseListener, FocusListener, _EntryPanel {
	private static final long serialVersionUID = 2661956887580911488L;
	private final GridBagLayout gbl;
	private final int minWidth = 75;
	private final int minHeight = 0;
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

	private String value;
	private final String initialValue;

	private JFormattedTextField entryField;
	private final Container owner;

	private final JPanel buttonPanel = new JPanel();
	private final JButton okBtn;
	@SuppressWarnings("unused")
	private final JButton cancelBtn;
	private final JButton clearBtn;

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

	public StringPanel(final LabelField field, _Verifier verifier, Container owner) {
		this(field, owner);
		if(verifier != null)
			verifier.setVerifier(entryField, owner);
	}

	public StringPanel(final LabelField field, Container owner) {
		this.owner = owner;
		this.textField = field;
		
		initialValue = field.getValueAsString();
		value = initialValue;

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

		
		entryField = new JFormattedTextField();
		if (value == null) {
			entryField.setText("");
		} else {
			entryField.setText(value);
		}
		KeyListener[] kls = textField.getKeyListeners();

		for (int i = 0; i < kls.length; i++) {
			entryField.addKeyListener(kls[i]);
		}
		entryField.addKeyListener(new KeyListener() {
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
				} else if (e.getKeyCode() == KeyEvent.VK_UP){
					dispose();
					textField.prevFocus();
				} else if(e.getKeyCode() == KeyEvent.VK_DOWN){
					dispose();
					textField.myNextFocus();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		textField.setFocusable(true);
		entryField.setFocusTraversalKeysEnabled(false);
		textField.removeAllFocusListener();
		textField.addFocusListener(this);

		SwingUtil.addComponent(this, gbl, entryField, 0, 0, 1, 3, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 0));

		SwingUtil.addComponent(this, gbl, okBtn, 1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.VERTICAL, new Insets(3, 0, 0, 3));

		// SwingUtil.addComponent(this, gbl, cancelBtn,
		// 1, 1, 1, 1, 0.0, 1.0,
		// GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0,
		// 0, 0, 3));

		SwingUtil.addComponent(this, gbl, clearBtn, 1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.SOUTHEAST,
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
		this.textField.setText(this.entryField.getText());
		textField.repaint();
	}

	private void getText() {
		value = textField.getValueAsString("");
		entryField.setText(value);

		if (!value.equals("")) {
			entryField.setSelectionStart(0);
			entryField.setSelectionEnd(value.length());
		}
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
	
	public synchronized void activate() {
		synchronized (this) {
			
			this.getText();
			// if (dialog != null) {
			// dispose();
			// }
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
				public void windowLostFocus(WindowEvent we) {
					if (dialog != null){
						dispose();
					} else {
					}
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
			// set minimum height
			finalHeight = finalHeight < 40 ? 40 : finalHeight;

			Point dialogLocLT = new Point((int) (tfLoc.x - finalWidth * 0.25), (int) (tfLoc.y - finalHeight * 0.25));

			dialogLocLT = SwingUtil.moveOnScreen(dialogLocLT, finalWidth, finalHeight);

			dialog.setSize(finalWidth, finalHeight);
			dialog.setLocation(dialogLocLT);
			dialog.add(this);
			dialog.setModal(false);
			dialog.setVisible(true);
		}
		// if (textField.isEnabled()) {
		// }
	}


	protected void dispose() {
		this.dispose(false);
	}

	private void dispose(boolean unsetText) {
		if (unsetText) {
			this.textField.setText(initialValue);
			this.textField.repaint();
		} else {
			this.setText();
		}
		if (dialog != null) {
			this.dialog.dispose();
			this.dialog = null;
		}
		this.textField.parentFocus();
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
