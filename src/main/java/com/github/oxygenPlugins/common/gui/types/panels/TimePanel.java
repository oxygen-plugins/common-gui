package com.github.oxygenPlugins.common.gui.types.panels;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.gui.swing.SwingUtil;
import com.github.oxygenPlugins.common.gui.types.IntegerAreaVerifier;
import com.github.oxygenPlugins.common.gui.types.LabelField;

public class TimePanel extends JPanel implements MouseListener, _EntryPanel {
	private static final long serialVersionUID = -4261661006435142610L;
	private final JDialog dialog;
	private final LabelField textField;

	private final Font defFont;

	@SuppressWarnings("unused")
	private final int padding;
	private final GridBagLayout gbl = new GridBagLayout();

	// private final int minWidth;
	// private final int minHeight;
	private JPanel contentPanel = new JPanel();

	JFormattedTextField hour = new JFormattedTextField();
	JFormattedTextField minutes = new JFormattedTextField();
	JFormattedTextField sec = new JFormattedTextField();

	private final Border defaultBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(240, 240, 240),
			new Color(150, 150, 150));
//	private final Border greenBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED, new Color(0, 110, 0),
//			new Color(0, 200, 0));
	private final Border selectBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(30, 20, 120),
			new Color(110, 100, 200));
	private String initialValue;
	private DateView okBtn;

	public TimePanel(LabelField field, int fontSize, Container owner) {

		initialValue = field.getValueAsString(null);
		if(initialValue != null){
			setTime(convert(initialValue));
		}

		if (owner instanceof Dialog) {
			dialog = new JDialog((Dialog) owner);
		} else if (owner instanceof Frame) {
			dialog = new JDialog((Frame) owner);
		} else if (owner instanceof Window) {
			dialog = new JDialog((Window) owner);
		} else {
			dialog = new JDialog(new JFrame());
		}
		this.setLayout(gbl);
		this.textField = field;
		Font defFont = new JLabel().getFont();
		this.defFont = new Font(defFont.getName(), defFont.getStyle(), fontSize);

		// this.minWidth = 25 * fontSize;
		// this.minHeight = 25 * fontSize;
		this.padding = (int) Math.round(fontSize * 0.1);
		this.setLayout(gbl);

		String text = this.textField.getText();
		DateTime actuellDat = convert(text);
		buildCalendar(actuellDat, actuellDat);
		Dimension minDim = new Dimension(fontSize * 7 / 4, fontSize * 2);
		hour.setMinimumSize(minDim);
		hour.setPreferredSize(minDim);
		minutes.setMinimumSize(minDim);
		minutes.setPreferredSize(minDim);
		sec.setMinimumSize(minDim);
		sec.setPreferredSize(minDim);
		dialog.setFocusTraversalKeysEnabled(false);
		dialog.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent arg0) {
				dispose();
			}

			@Override
			public void windowGainedFocus(WindowEvent arg0) {
			}
		});
		dialog.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				dispose();
			}
		});
		// dialog.setMinimumSize(new Dimension(minWidth, minHeight));
		dialog.setUndecorated(true);
		dialog.add(this);
	}

	private void dispose() {
		this.dialog.dispose();
		this.textField.parentFocus();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		activate();
	}

	@Override
	public void activate() {

		String text = this.textField.getText();
		DateTime actuellDat = convert(text);
		buildCalendar(actuellDat, actuellDat);

		// dialog.setSize(textField.getWidth(), textField.getWidth());
		int finalWidth = textField.getWidth() * 2;
		int finalHeight = textField.getHeight() * 2;
		dialog.setSize(finalWidth, finalHeight);

		dialog.pack();

		dialog.setLocation(getDialogBounds());
		// System.out.println(tfLoc.y);
		dialog.setModal(false);
		dialog.setVisible(true);

		okBtn.requestFocus();
	}

	private Point getDialogBounds() {

		Point tfLoc = textField.getLocationOnScreen();
		Point prefLoc = new Point(tfLoc.x, tfLoc.y);

		prefLoc.y = tfLoc.y + textField.getHeight();
		prefLoc.x = tfLoc.x + textField.getWidth() - dialog.getWidth();

		Point onScreenLoc = SwingUtil.moveOnScreen(prefLoc, dialog.getWidth(), dialog.getHeight());

		onScreenLoc.y = prefLoc.y > onScreenLoc.y ? tfLoc.y - dialog.getHeight() : onScreenLoc.y;

		return onScreenLoc;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	private void clear() {
		this.remove(contentPanel);
	}

	private void buildCalendar(final DateTime actuell, final DateTime selectedDate) {
		clear();

		IconMap icons = null;
		;
		try {
			icons = new IconMap();
		} catch (IOException e) {
		}
		GridBagLayout gblContent = new GridBagLayout();

		this.setBorder(defaultBorder);
		contentPanel = new JPanel();
		contentPanel.setLayout(gblContent);
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setOpaque(true);

		SwingUtil.addComponent(this, gbl, contentPanel, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH);

		// JPanel head = buildHeader(actuell, selectedDate);
		// SwingUtil.addComponent(contentPanel, gbl, head, 0, 0, 1, 1, 0.0, 0.0,
		// GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH);

		IntegerAreaVerifier iavHour = new IntegerAreaVerifier(0, 23);
		IntegerAreaVerifier iavMinutes = new IntegerAreaVerifier(0, 59);
		IntegerAreaVerifier iavSec = new IntegerAreaVerifier(0, 59);

		iavHour.setVerifier(hour, dialog);

		iavMinutes.setVerifier(minutes, dialog);

		iavSec.setVerifier(sec, dialog);

		setTime(actuell);

		final ArrayList<DateView> dateViewList = new ArrayList<DateView>();

		DateView hourP = new DateView(icons.getIcon(6, 13)) {

			{
				dateViewList.add(this);
			}

			private static final long serialVersionUID = 1L;

			@Override
			public void performAction() {
				setTime(getTime().plusHours(1));
			}

			@Override
			void up() {
				performAction();
			}

			@Override
			void down() {
				dateViewList.get(1).requestFocus();
			}

			@Override
			void right() {
				dateViewList.get(2).requestFocus();
			}

			@Override
			void left() {
				dateViewList.get(6).requestFocus();
			}
		};

		DateView hourM = new DateView(icons.getIcon(4, 13)) {
			private static final long serialVersionUID = 1L;
			{
				dateViewList.add(this);
			}

			@Override
			public void performAction() {
				setTime(getTime().minusHours(1));
			}

			@Override
			void up() {
				dateViewList.get(0).requestFocus();
			}

			@Override
			void down() {
				performAction();
			}

			@Override
			void right() {
				dateViewList.get(3).requestFocus();
			}

			@Override
			void left() {
				dateViewList.get(7).requestFocus();
			}
		};
		final DateView minP = new DateView(icons.getIcon(6, 13)) {
			private static final long serialVersionUID = 1L;
			{
				dateViewList.add(this);
			}

			@Override
			public void performAction() {
				setTime(getTime().plusMinutes(1));
			}

			@Override
			void up() {
				performAction();
			}

			@Override
			void down() {
				dateViewList.get(3).requestFocus();
			}

			@Override
			void right() {
				dateViewList.get(4).requestFocus();
			}

			@Override
			void left() {
				dateViewList.get(0).requestFocus();
			}
		};

		final DateView minM = new DateView(icons.getIcon(4, 13)) {
			private static final long serialVersionUID = 1L;
			{
				dateViewList.add(this);
			}

			@Override
			public void performAction() {
				setTime(getTime().minusMinutes(1));
			}

			void down() {
				performAction();
			}

			void up() {
				dateViewList.get(2).requestFocus();
			}

			@Override
			void right() {
				dateViewList.get(5).requestFocus();
			}

			@Override
			void left() {
				dateViewList.get(1).requestFocus();
			}
		};
		DateView secP = new DateView(icons.getIcon(6, 13)) {
			private static final long serialVersionUID = 1L;
			{
				dateViewList.add(this);
			}

			@Override
			public void performAction() {
				setTime(getTime().plusSeconds(1));
			}

			void down() {
				dateViewList.get(5).requestFocus();
			}

			void up() {
				performAction();
			}

			@Override
			void right() {
				dateViewList.get(6).requestFocus();
			}

			@Override
			void left() {
				dateViewList.get(2).requestFocus();
			}
		};
		DateView secM = new DateView(icons.getIcon(4, 13)) {
			private static final long serialVersionUID = 1L;
			{
				dateViewList.add(this);
			}

			@Override
			public void performAction() {
				setTime(getTime().minusSeconds(1));
			}

			void down() {
				performAction();
			}

			void up() {
				dateViewList.get(4).requestFocus();
			}

			@Override
			void right() {
				dateViewList.get(7).requestFocus();
			}

			@Override
			void left() {
				dateViewList.get(3).requestFocus();
			}
		};
		this.okBtn = new DateView(icons.getIcon(2, 10)) {
			private static final long serialVersionUID = 1L;
			{
				dateViewList.add(this);
				this.setFocusTraversalKeysEnabled(false);
			}

			@Override
			public void performAction() {
				confirmDate(getTime());
			}

			@Override
			void up() {
				confirmDate(getTime());
				textField.prevFocus();
			}

			void down() {
				dateViewList.get(7).requestFocus();
			}

			@Override
			void right() {
				dateViewList.get(0).requestFocus();
			}

			@Override
			void left() {
				dateViewList.get(4).requestFocus();
			}

			@Override
			void tabForward() {
				down();
			}

			@Override
			void tabBackward() {
				confirmDate(getTime());
				textField.prevFocus();
			}
		};
		DateView clearBtn = new DateView(icons.getIcon(10, 11)) {
			private static final long serialVersionUID = 1L;
			{
				dateViewList.add(this);
				this.setFocusTraversalKeysEnabled(false);
			}

			@Override
			public void performAction() {
				unsetTextAndDispose();
			}

			void up() {
				dateViewList.get(6).requestFocus();
			}

			@Override
			void right() {
				confirmDate(getTime());
				textField.myNextFocus();
			}

			@Override
			void down() {
				confirmDate(getTime());
				textField.myNextFocus();
			}

			@Override
			void left() {
				dateViewList.get(5).requestFocus();
			}

			@Override
			void tabBackward() {
				up();
			}

			@Override
			void tabForward() {
				confirmDate(getTime());
				textField.myNextFocus();
			}
		};

		SwingUtil.addComponent(contentPanel, gblContent, hour, 0, 0, 1, 2, 0.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH);
		SwingUtil.addComponent(contentPanel, gblContent, hourP, 1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE);
		SwingUtil.addComponent(contentPanel, gblContent, hourM, 1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST,
				GridBagConstraints.NONE);
		SwingUtil.addComponent(contentPanel, gblContent, minutes, 2, 0, 1, 2, 0.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH);
		SwingUtil.addComponent(contentPanel, gblContent, minP, 3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.NONE);
		SwingUtil.addComponent(contentPanel, gblContent, minM, 3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH,
				GridBagConstraints.NONE);
		SwingUtil.addComponent(contentPanel, gblContent, sec, 4, 0, 1, 2, 0.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH);
		SwingUtil.addComponent(contentPanel, gblContent, secP, 5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE);
		SwingUtil.addComponent(contentPanel, gblContent, secM, 5, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST,
				GridBagConstraints.NONE);

		SwingUtil.addComponent(contentPanel, gblContent, okBtn, 6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST,
				GridBagConstraints.NONE);
		SwingUtil.addComponent(contentPanel, gblContent, clearBtn, 6, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST,
				GridBagConstraints.NONE);
		okBtn.requestFocus();
		this.contentPanel.repaint();
		this.repaint();
		this.updateUI();
	}

	protected void unsetTextAndDispose() {
		this.textField.setText(initialValue);
		this.textField.repaint();

		this.dialog.dispose();
	}

	private DateTime getTime() {
		String timeString = "T" + hour.getText() + ":" + minutes.getText() + ":" + sec.getText();
		DateTime time = DateTime.parse(timeString);
		return time;
	}

	private void setTime(DateTime time) {
		setIntToField(hour, time.getHourOfDay());
		setIntToField(minutes, time.getMinuteOfHour());
		setIntToField(sec, time.getSecondOfMinute());
	}

	private void setIntToField(JFormattedTextField field, int value) {
		String valueString = "" + value;
		if (valueString.length() == 1) {
			valueString = "0" + valueString;
		}
		field.setText(valueString);
	}

	private abstract class DateView extends JLabel {
		private static final long serialVersionUID = 1L;
		private final JLabel label;
		private Color foreground;
		private Color background = Color.WHITE;
		private Border defaultBorder = TimePanel.this.defaultBorder;

		private DateView(int padding) {
			this.label = new JLabel();
			this.foreground = Color.BLACK;
			// this.label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			// this.label.setAlignmentY(JLabel.CENTER_ALIGNMENT);

			setSelect(false);
			// this.setBackground(Color.RED);
			// this.label.setBackground(Color.BLUE);
			// this.label.setOpaque(true);

			this.setFont(defFont);
			this.setFocusable(true);
			// Insets insets = new Insets(padding, padding, padding, padding);
			// SwingUtil.addComponent(this, gbl, label, 0, 0, 1, 1, 1.0, 1.0,
			// GridBagConstraints.CENTER, GridBagConstraints.NONE, insets);
			addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent arg0) {
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					DateView.this.setSelect(false);
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					DateView.this.setSelect(true);
				}

				@Override
				public void mouseClicked(MouseEvent arg0) {
					// DateView.this.setSelect(false);
					performAction();
				}
			});

			addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					setSelect(false);
				}

				@Override
				public void focusGained(FocusEvent e) {
					setSelect(true);
				}
			});

			addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {

				}

				@Override
				public void keyReleased(KeyEvent e) {
					switch (e.getKeyChar()){
					case KeyEvent.VK_TAB:
						e.consume();
						break;
					}
					switch (e.getKeyCode()) {

					case KeyEvent.VK_ENTER:
					case KeyEvent.VK_SPACE:
						performAction();
						break;
					case KeyEvent.VK_ESCAPE:
						dispose();
						break;
					case KeyEvent.VK_DOWN:
						DateView.this.down();
						break;
					case KeyEvent.VK_UP:
						DateView.this.up();
						break;
					case KeyEvent.VK_RIGHT:
						DateView.this.right();
						break;
					case KeyEvent.VK_LEFT:
						DateView.this.left();
						break;
					case KeyEvent.VK_TAB:
						
						if (e.isShiftDown()) {
							DateView.this.tabBackward();
						} else {
							DateView.this.tabForward();
						}
						e.consume();
						break;
					case KeyEvent.VK_PAGE_DOWN:
					case KeyEvent.VK_PAGE_UP:
					default:
						break;
					}
				}

				@Override
				public void keyPressed(KeyEvent e) {

				}
			});

		}

		private DateView(Icon icon) {
			this(0);
			this.setIcon(icon);
			Dimension dim = new Dimension(icon.getIconWidth() + 1, icon.getIconHeight() + 1);
			this.setMinimumSize(dim);
			this.setSize(dim);
			// this.label.setBackground(Color.YELLOW);
			// this.label.setOpaque(true);
		}

		private DateView(Icon icon, int size) {
			this(icon);
			Dimension dim = new Dimension(size, size);
			this.setMaximumSize(dim);
			setSize(dim);
			setPreferredSize(dim);
		}

		public abstract void performAction();

		void up() {
		}

		void down() {
		}

		void right() {
		}

		void left() {
		}

		void tabForward() {
			this.transferFocus();
		}

		void tabBackward() {
			this.transferFocusBackward();
		}

		private void setSelect(boolean isSelect) {
			if (isSelect) {
				this.setBorder(selectBorder);
				setBackground(new Color(205, 225, 225));
				// this.label.setForeground(Color.WHITE);
			} else {
				this.setBorder(this.defaultBorder);
				setBackground(this.background);
				this.label.setForeground(this.foreground);
			}
			this.setOpaque(true);
		}
	}

	private void confirmDate(DateTime date) {
		this.textField.setText(convert(date));
		this.textField.repaint();
		dispose();
	}

	static DateTimeFormatter FMT = DateTimeFormat.forPattern("HH:mm:ss");

	private static String convert(DateTime date) {
		return date.toString(FMT);
	}

	private static DateTime convert(String string) {
		try {
			return DateTime.parse(string, FMT);
		} catch (IllegalArgumentException e) {
			return new DateTime();
		}
	}

}
