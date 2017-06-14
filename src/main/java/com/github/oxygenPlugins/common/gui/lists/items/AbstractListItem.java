package com.github.oxygenPlugins.common.gui.lists.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import com.github.oxygenPlugins.common.gui.lists.DefaultTheme;
import com.github.oxygenPlugins.common.gui.lists.Theme;
import com.github.oxygenPlugins.common.gui.swing.SwingUtil;



public abstract class AbstractListItem<ModelNodeType extends _ListNode> extends
		JPanel {

	// private static final Color HOVER_COLOR = Color.WHITE;
	// // private static final Color DEFAULT_COLOR = Color.WHITE;
	// private static final Color DEFAULT_COLOR = new Color(50, 50, 50);
	// private static final Color DEFAULT_FONT_COLOR = Color.BLACK;
	// private static final Color HOVER_FONT_COLOR = Color.BLACK;
	// private static final Color SELECTION_COLOR = new Color(0, 255, 255);
	// private static final Color SELECTION_FONT_COLOR = Color.BLACK;
	// private static final Border DEFAULT_BORDER =
	// BorderFactory.createEmptyBorder(1, 0, 1, 0);
	// private static final Border HOVER_SELECT_BORDER =
	// BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(0, 150, 255));

	private static final long serialVersionUID = 159899019060744554L;

	private GridBagLayout gbl = new GridBagLayout();

	private Theme theme = new DefaultTheme();

	private Color usedBgColor = theme.getDefaultBackgroundColor();
	private Color usedFontColor = theme.getDefaultForegroundColor();

	private Color defaultFontColor = this.theme.getDefaultForegroundColor();
	private Color selectFontColor = this.theme.getSelectionForegroundColor();
	private Color howerFontColor = theme.getHoverForegroundColor();

	private JLabel titleLabel;
	private boolean isSelected;
	protected final ModelNodeType node;
	protected final ControlPanel controlPanel = new ControlPanel(new Mouse());
	private final JLabel iconLabel;
	private MouseListener selcetionListener;
	private int paddingLabel;
	// private JPopupMenu menu = null;
	private HashMap<Integer, JPopupMenu> menuByModifier = new HashMap<Integer, JPopupMenu>();

	private Border usedBorder;

	public AbstractListItem(ModelNodeType node, Icon defaultIcon) {
		this(node, 0, defaultIcon, new DefaultTheme());
	}
	public AbstractListItem(ModelNodeType node, int level, Icon defaultIcon) {
		this(node, level, defaultIcon, new DefaultTheme());
	}

	public AbstractListItem(ModelNodeType node, Icon defaultIcon, Theme theme) {
		this(node, 0, defaultIcon, theme);
	}

	public AbstractListItem(ModelNodeType node, int level, Icon defaultIcon,
			Theme theme) {
		this.node = node;
		this.theme = theme;
		this.setLayout(gbl);
		titleLabel = new JLabel(node.toString());

		Icon icon = null;
		if (node.hasIcon()) {
			try {
				icon = node.getIcon();
			} catch (IOException e) {
				icon = defaultIcon;
			}
		} else {
			icon = defaultIcon;
		}
		iconLabel = new JLabel(icon);

		int iconW = icon != null ? icon.getIconWidth() : 0;
		paddingLabel = 2;
		if (iconW > 16) {
			paddingLabel += iconW - 16;
		}

		SwingUtil.addComponent(this, gbl, iconLabel, 0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						level * 10, 0, paddingLabel));
		SwingUtil.addComponent(this, gbl, titleLabel, 1, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH);
		SwingUtil.addComponent(this, gbl, this.controlPanel, 2, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 5));

		titleLabel.setForeground(theme
				.getDefaultForegroundColor(Theme.COLOR_TYPE_STANDARD));

		this.setBackground(theme.getDefaultBackgroundColor());
		this.setForeground(theme.getDefaultForegroundColor());
		this.setBorder(theme.getDefaultBorder());
		// setTheme(new DefaultTheme());

		usedBgColor = this.theme.getDefaultBackgroundColor();
		usedFontColor = this.theme.getDefaultForegroundColor();
		usedBorder = this.theme.getDefaultBorder();

		defaultFontColor = this.theme.getDefaultForegroundColor();
		selectFontColor = this.theme.getSelectionForegroundColor();
		howerFontColor = this.theme.getHoverForegroundColor();

		this.addMouseListener(new Mouse());
		this.setFocusable(true);
		this.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				setBorder(usedBorder);
			}

			@Override
			public void focusGained(FocusEvent e) {
				usedBorder = getBorder();
				setBorder(AbstractListItem.this.theme.getFocusBorder());
			}
		});
	}

	// public void setTheme(Theme theme){
	// this.theme = theme;
	//

	// this.repaint();
	// }

	public void addSelectionListener(MouseListener ml) {
		// this.addMouseListener(ml);
		this.selcetionListener = ml;
	}

	public void setLevel(int level) {
		GridBagConstraints gbc = gbl.getConstraints(iconLabel);
		gbc.insets = new Insets(0, level * 10, 0, paddingLabel);
		gbl.setConstraints(iconLabel, gbc);
		this.repaint();
	}

	protected void addIconMouseAdapter(MouseAdapter mouse) {
		this.iconLabel.addMouseListener(mouse);
		this.iconLabel.addMouseMotionListener(mouse);
		this.iconLabel.addMouseWheelListener(mouse);
	}

	public void implementPopupMenu(JPopupMenu menu, int modifier) {
		this.menuByModifier.put(modifier, menu);
	}

	public void implementPopupMenu(JPopupMenu[] menus) {
		this.implementPopupMenu(menus, new int[] { NO_MODIFIER, SHIFT_DOWN,
				CTRL_DOWN });
	}

	public void implementPopupMenu(JPopupMenu[] menus, int[] modifiers) {
		int i = 0;
		for (int mod : modifiers) {
			if (i < menus.length) {
				this.menuByModifier.put(mod, menus[i]);
			}
			i++;
		}
	}

	public final static int NO_MODIFIER = 0;

	public final static int ALT_DOWN = MouseEvent.ALT_DOWN_MASK;
	public final static int CTRL_DOWN = MouseEvent.CTRL_DOWN_MASK;
	public final static int SHIFT_DOWN = MouseEvent.SHIFT_DOWN_MASK;

	public void implementPopupMenu(JPopupMenu menu) {
		this.implementPopupMenu(menu, NO_MODIFIER);
	}

	public class Mouse extends MouseAdapter {
		private Border usedBorder = theme.getDefaultBorder();

		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			if (e.isPopupTrigger()) {
				doPop(e);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);
			if (e.isPopupTrigger()) {
				doPop(e);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			usedBorder = getBorder();
			if (isSelected) {
				setBorder(theme.getSelectionBorder());
			} else {
				setBackground(theme.getHoverBackgroundColor());
				titleLabel.setForeground(howerFontColor);
			}
			updateUI();
			super.mouseEntered(e);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			if (selcetionListener != null && !e.isPopupTrigger()) {
				selcetionListener.mouseClicked(e);
			}
			// if(AbstractListItem.this.contains(e.getX(), e.getY())){
			// AbstractListItem.this.requestFocus();
			// }
		}

		private void doPop(MouseEvent e) {
			if ((!isSelected) && selcetionListener != null) {
				selcetionListener.mouseClicked(e);
			}
			int modifier = NO_MODIFIER;
			if (e.isShiftDown()) {
				modifier = SHIFT_DOWN;
			} else if (e.isControlDown()) {
				modifier = CTRL_DOWN;
			}

			if (!menuByModifier.containsKey(modifier)) {
				modifier = NO_MODIFIER;
			}

			JPopupMenu menu = null;
			if (menuByModifier.containsKey(modifier)) {
				menu = menuByModifier.get(modifier);
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setBorder(usedBorder);
			setBackground(usedBgColor);
			titleLabel.setForeground(usedFontColor);
			updateUI();
			super.mouseExited(e);
		}
	}

	// public abstract Icon getDefaultIcon();

	public void setSelection(boolean isSelected) {
		this.isSelected = isSelected;
		if (isSelected) {
			this.setBackground(theme.getSelectionBackgroundColor());
			titleLabel.setForeground(this.selectFontColor);
			this.usedBgColor = theme.getSelectionBackgroundColor();
			this.usedFontColor = this.selectFontColor;
		} else {
			this.setBackground(theme.getDefaultBackgroundColor());
			titleLabel.setForeground(this.defaultFontColor);
			this.usedBgColor = theme.getDefaultBackgroundColor();
			this.usedFontColor = this.defaultFontColor;
		}
		repaint();
	}

	protected void updateText() {
		titleLabel.setText(node.toString());
		this.repaint();
	}

	protected void setLabelStyle(int style) {
		Font f = titleLabel.getFont();
		f = new Font(f.getName(), style, f.getSize());
		titleLabel.setFont(f);
		titleLabel.repaint();
	}

	protected void setLabelColorType(int type) {
		titleLabel.setForeground(theme.getDefaultForegroundColor(type));
		this.usedFontColor = theme.getDefaultForegroundColor(type);
		this.defaultFontColor = theme.getDefaultForegroundColor(type);
		this.selectFontColor = theme.getSelectionForegroundColor(type);
		this.howerFontColor = theme.getHoverForegroundColor(type);
	}

	public boolean isSelected() {
		return this.isSelected;
	}

	public ModelNodeType getModelNode() {
		return this.node;
	}

	@Override
	public String toString() {
		return node.toString();
	}

	public boolean isOnControllIcon(Point point) {
		return this.controlPanel.contains(point);
	}

	public void checkVisibility() {

	}

}
