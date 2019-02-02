package com.github.oxygenPlugins.common.gui.types.panels;


import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.gui.swing.SwingUtil;
import com.github.oxygenPlugins.common.gui.types.LabelField;
import com.github.oxygenPlugins.common.gui.types._Verifier;

import static com.github.oxygenPlugins.common.gui.types.converter.TypeConverter.*;

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
	private String lastValue;
	private final String initialValue;

	private JFormattedTextField entryField;
	private final Container owner;

	private final JPanel buttonPanel = new JPanel();
	private final JButton okBtn;
	private final JButton expBtn;
	@SuppressWarnings("unused")
	private final JButton cancelBtn;
	private final JButton clearBtn;
	private boolean isExpanded = false;

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
			this.expBtn = new PanelButton(icons.getIcon(0, 11));
		} else {
			this.okBtn = new PanelButton("ok");
			this.clearBtn = new PanelButton("x");
			this.cancelBtn = new PanelButton("c");
			this.expBtn = new PanelButton("+");
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
				dispose(DisposeOptions.RESET);
			}
		});

		this.cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose(DisposeOptions.CANCEL);
			}
		});

		this.expBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				expand();
			}
		});

		// this.okBtn.setVisible(false);
		// this.clearBtn.setVisible(false);
		// this.cancelBtn.setVisible(false);

		
		entryField = new JFormattedTextField();
		entryField.setFont(field.getFont());
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
					if(e.isShiftDown()){
						dispose(DisposeOptions.RESET);
					} else {
						dispose(DisposeOptions.CANCEL);
					}
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
				} else if(e.getKeyCode() == KeyEvent.VK_F2){
					expand();
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

		if("xs:string".equals(field.getType().getType())){

			SwingUtil.addComponent(this, gbl, cancelBtn, 2, 0, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHEAST,
					GridBagConstraints.VERTICAL, new Insets(3, 0, 0, 3));


			SwingUtil.addComponent(this, gbl, expBtn, 2, 1, 1, 1, 0.0, 1.0, GridBagConstraints.SOUTHEAST,
					GridBagConstraints.VERTICAL, new Insets(0, 0, 3, 3));

		}

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
			this.lastValue =  textField.getValueAsString();
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

	private enum DisposeOptions {
		RESET, OK, CANCEL
	}

	protected void dispose() {
		this.dispose(DisposeOptions.OK);
	}

	private void dispose(DisposeOptions unsetText) {
		switch (unsetText){
			case RESET:
				this.textField.setText(initialValue);
				this.textField.repaint();
			break;

			case CANCEL:
				this.textField.setText(lastValue);
				this.textField.repaint();
			break;
			default:
				this.setText();
		}
		if (dialog != null) {
			if(!this.isExpanded){
				this.dialog.dispose();
				this.dialog = null;
			}
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

	public void expand(){

		ExpandedPanel expanded = new ExpandedPanel(this.dialog, entryField);

		expanded.expand();
		
	}




	private class ExpandedPanel extends JDialog {

		private final JFormattedTextField entryField;
		private final JTextArea textArea = new JTextArea();
		public ExpandedPanel(JDialog parent, JFormattedTextField entryField){
			super(parent);
			this.entryField = entryField;
			init();
		}

		private void init(){

			textArea.setFont( SwingUtil.decreceFontSize( entryField.getFont(), 1.2));

			this.setTitle(textField.getTitle());

			textArea.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {

				}

				@Override
				public void keyPressed(KeyEvent e) {

				}

				@Override
				public void keyReleased(KeyEvent e) {
					switch (e.getKeyChar()){
						case '\n':
							if(e.isControlDown()){
								disposeMe();
							}
							break;
						case KeyEvent.VK_ESCAPE:
							if(e.isShiftDown()){
								disposeMe(DisposeOptions.RESET);
							} else {
								disposeMe(DisposeOptions.CANCEL);
							}
							break;
					}
				}
			});

			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					disposeMe(DisposeOptions.CANCEL);
				}
			});

			this.setSize(800, 300);
			this.setMinimumSize(new Dimension(800, 300));
			this.setPreferredSize(new Dimension(800, 300));
			this.setModal(true);
			this.getContentPane().add(textArea);


			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		}

		private void expand(){

			isExpanded = true;
			textArea.setText(makeMultipleLines(this.entryField.getText()));

			this.pack();
			SwingUtil.centerFrame(this);
			this.setVisible(true);
		}

		private void disposeMe(){
			disposeMe(DisposeOptions.OK);
		}

		private void disposeMe(DisposeOptions option){
			if(option == DisposeOptions.OK){
				entryField.setText(makeOneLine(textArea.getText()));
			}
			isExpanded = false;
			StringPanel.this.dispose(option);
		}

	}
}
