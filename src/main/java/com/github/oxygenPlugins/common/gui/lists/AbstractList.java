package com.github.oxygenPlugins.common.gui.lists;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.Border;

import com.github.oxygenPlugins.common.gui.lists.items.AbstractListItem;
import com.github.oxygenPlugins.common.gui.lists.items.ItemGroup;
import com.github.oxygenPlugins.common.gui.lists.items._ListGroupNode;
import com.github.oxygenPlugins.common.gui.lists.items._ListNode;
import com.github.oxygenPlugins.common.gui.swing.SwingUtil;

public abstract class AbstractList<ModelNodeType extends _ListNode, ListItemType extends AbstractListItem<ModelNodeType> & Comparable<ListItemType>>
		extends JPanel implements KeyListener, MouseListener {

	private Border border;
	private final GridBagLayout gbl_content = new GridBagLayout();
	protected final JPanel content = new JPanel(gbl_content);
	protected JPanel endPanel = new JPanel();
	protected JPanel emptyEndPanel = new JPanel();
	private final JPanel toolbarPanel = new JPanel();
	private final JPanel bottombarPanel = new JPanel();
	private final GridBagLayout gbl_toolbarPanel = new GridBagLayout();
	private final GridBagLayout gbl_bottombarPanel = new GridBagLayout();
	protected boolean isMultiSelectable = true;

	private ArrayList<ListItemType> itemList = new ArrayList<ListItemType>();
	private ArrayList<ItemGroup<ModelNodeType, ListItemType>> groups = new ArrayList<ItemGroup<ModelNodeType, ListItemType>>();

	private int messageIndex = 0;

	@SuppressWarnings("rawtypes")
	private HashMap<AbstractListItem, Integer> itemByPosition = new HashMap<AbstractListItem, Integer>();
	private ArrayList<ListItemType> selectedItem = new ArrayList<ListItemType>();
	private JScrollPane scrollPane;
	private final Theme theme;
	
	public AbstractList(Theme theme) {
		this.theme = theme;
		GridBagLayout gbl = new GridBagLayout();
		this.setLayout(gbl);

		toolbarPanel.setLayout(gbl_toolbarPanel);
		bottombarPanel.setLayout(gbl_bottombarPanel);
		
		this.content.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		
		scrollPane = new JScrollPane(this.content);
		ScrollPaneLayout spl = new ScrollPaneLayout();
		scrollPane.setLayout(spl);

		scrollPane.setBackground(theme.getDefaultBackgroundColor());
		scrollPane.setOpaque(true);

		JScrollBar vertScrollBar = new JScrollBar();
		vertScrollBar.setUnitIncrement(100);
		JScrollBar horScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
		horScrollBar.setUnitIncrement(100);

		scrollPane.setVerticalScrollBar(vertScrollBar);
		scrollPane.setHorizontalScrollBar(horScrollBar);

		endPanel.setBackground(theme.getDefaultBackgroundColor());
		emptyEndPanel.setBackground(theme.getDefaultBackgroundColor());

		SwingUtil.addComponent(this, gbl, toolbarPanel, 	0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH);
		SwingUtil.addComponent(this, gbl, scrollPane, 		0, 1, 1, 1, 0.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH);
		SwingUtil.addComponent(this, gbl, bottombarPanel, 	0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH);
		border = BorderFactory.createEmptyBorder();
		this.setBorder(border);
		
		// border = BorderFactory.createLineBorder(new Color(0, 100, 100, 70));
		this.content.setBackground(theme.getDefaultBackgroundColor());
		
		this.endPanel.addMouseListener(this);
		
		this.content.addMouseListener(this);
		this.content.addKeyListener(this);
		
	}
	
	public AbstractList() {
		this(new DefaultTheme());
	}
	public AbstractList(String title) {
		this(title, new DefaultTheme());
	}

	public AbstractList(String title, Theme theme) {
		this(theme);
		if(title != null){
			border = BorderFactory
					.createTitledBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
							Color.BLACK), title);
			this.setBorder(border);
		}
	}
	
//	protected void setTheme(Theme theme){
//		
//		setBgColor(theme.getDefaultBackgroundColor());
//	}
//	
//	private void setBgColor(Color bgc){
//		this.scrollPane.setBackground(bgc);
//		this.endPanel.setBackground(bgc);
//		this.content.setBackground(bgc);
//		this.emptyEndPanel.setBackground(bgc);
//		this.setBackground(bgc);
//		this.updateUI();
//	}
	
	protected void addComponentToToolbar(JComponent comp, int x, int y,
			int width, int height, double weightx, double weighty, int anchor,
			int fill){
		addComponentToToolbar(comp, x, y, width, height, weightx, weighty, anchor, fill, new Insets(0, 0, 0, 0));
	}
	protected void addComponentToToolbar(JComponent comp, int x, int y,
			int width, int height, double weightx, double weighty, int anchor,
			int fill, Insets insets) {
		SwingUtil.addComponent(this.toolbarPanel, gbl_toolbarPanel, comp, x, y,
				width, height, weightx, weighty, anchor, fill, insets);
	}
	protected void addComponentToBottombar(JComponent comp, int x, int y,
			int width, int height, double weightx, double weighty, int anchor,
			int fill) {
		addComponentToBottombar(comp, x, y, width, height, weightx, weighty, anchor, fill, new Insets(0, 0, 0, 0));
	}
	protected void addComponentToBottombar(JComponent comp, int x, int y,
			int width, int height, double weightx, double weighty, int anchor,
			int fill, Insets insets) {
		SwingUtil.addComponent(this.bottombarPanel, gbl_bottombarPanel, comp, x, y,
				width, height, weightx, weighty, anchor, fill, insets);
	}

	public void addToolbar(JToolBar toolbar) {
		this.toolbarPanel.add(toolbar);
	}
	
	private Insets itemInsets = new Insets(0, 1, 0, 1);

	public void addListItem(ListItemType item) {
		if (!this.content.isEnabled())
			this.content.setEnabled(true);
		
//		item.setTheme(this.theme);
		this.itemList.add(item);

		this.content.remove(this.endPanel);
		this.content.remove(emptyEndPanel);
		
		SwingUtil.addComponent(this.content, gbl_content, item, 0,
				messageIndex++, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, itemInsets);
		SwingUtil.addComponent(this.content, gbl_content, this.endPanel, 0,
				messageIndex + 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, itemInsets);

		this.itemByPosition.put(item, messageIndex);
	}

	public void addListItem(ListItemType[] items) {
		Arrays.sort(items);
		for (ListItemType item : items) {
			addListItem(item);
		}

	}

	public void addListItem(ArrayList<ListItemType> items) {
		Collections.sort(items);
		for (ListItemType item : items) {
			addListItem(item);
		}
	}
	
	protected ArrayList<ListItemType> getListItems(){
		return new ArrayList<ListItemType>(this.itemList);
	}
	
	public ItemGroup<ModelNodeType, ListItemType> createItemGroup(ArrayList<ListItemType> items, ArrayList<ItemGroup<ModelNodeType, ListItemType>> subGroups, _ListGroupNode node){
		Collections.sort(items);
		ItemGroup<ModelNodeType, ListItemType> group = new ItemGroup<ModelNodeType, ListItemType>(
				node, items, subGroups, 0, this, null, this.theme, false) {
			private static final long serialVersionUID = -420997520077674539L;

			@Override
			public void expand() {
				for (ListItemType item : this.items) {
					showItem(item);
				}
				super.expand();
			}

			@Override
			public void collapse() {
				for (ListItemType item : this.items) {
					hideItem(item);
				}
				super.collapse();
			}
			
		};
		
		return group;
		
	}
	
	protected ItemGroup<ModelNodeType,ListItemType> addListItemAsGroup(ArrayList<ListItemType> items,
			_ListGroupNode node) {
		return addListItemAsGroup(items, new ArrayList<ItemGroup<ModelNodeType, ListItemType>>(), node);
	}
	
	protected ItemGroup<ModelNodeType,ListItemType> addListItemAsGroup(ArrayList<ListItemType> items, ArrayList<ItemGroup<ModelNodeType, ListItemType>> subGroups,
			_ListGroupNode node) {
		ItemGroup<ModelNodeType, ListItemType> group = createItemGroup(items, subGroups, node);
		this.addListItem(group);
		return group;
	}

	@SuppressWarnings("rawtypes")
	private void showItem(AbstractListItem item) {
		item.setVisible(true);
	}

	@SuppressWarnings("rawtypes")
	private void hideItem(AbstractListItem item) {
		item.setVisible(false);
	}

	public void addListItem(ItemGroup<ModelNodeType, ListItemType> group) {
		if (!this.content.isEnabled())
			this.content.setEnabled(true);
		synchronized (group) {
			
			this.content.remove(this.endPanel);
			this.content.remove(this.emptyEndPanel);
			SwingUtil.addComponent(this.content, gbl_content, group, 0,
					messageIndex++, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, itemInsets);
			SwingUtil.addComponent(this.content, gbl_content, this.endPanel, 0,
					messageIndex + 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, itemInsets);
			
			this.itemByPosition.put(group, messageIndex);
			
			this.groups.add(group);
			
			for (ItemGroup<ModelNodeType, ListItemType> subGroup : group.getSubGroups()) {
				this.addListItem(subGroup);
			}
			
		}

		this.addListItem(group.getItems());

	}

	public void removeAllItems() {
		this.content.removeAll();
		this.messageIndex = 0;
		this.itemList = new ArrayList<ListItemType>();
		this.content.setEnabled(false);
		SwingUtil.addComponent(this.content, gbl_content, this.emptyEndPanel, 0,
				messageIndex + 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, itemInsets);
	}

	public boolean isSelectedItem(ListItemType item) {
		return getSelectedItem().contains(item);
	}

	public ArrayList<ListItemType> getSelectedItem() {
		return this.selectedItem;
	}

	public void unselectItems() {
		if (this.selectedItem != null) {
			for (ListItemType item : this.selectedItem) {
				item.setSelection(false);
			}
			this.selectedItem = new ArrayList<ListItemType>();
		}

	}

	private void unselectItem(ListItemType item) {
		selectedItem.remove(item);
		item.setSelection(false);
	}
	public void selectUnselectItem(ArrayList<ListItemType> items) {
		unselectItems();
		for (ListItemType item : items) {
			select(item);
		}
	}
	
	
	public void selectUnselectItem(InputEvent e, ArrayList<ListItemType> items) {
		if(this.isMultiSelectable){
			if (!e.isControlDown()) {
				unselectItems();
			}
			for (ListItemType item : items) {
				select(item);
			}
		}
	}
	
	private ListItemType getSelectAnchor(ListItemType item){
		return this.selectedItem.get(0);
	}

	public void selectUnselectItem(ListItemType item) {
		unselectItems();
		select(item);
	}
	public void selectUnselectItem(InputEvent e, ListItemType item) {
		if (e.isShiftDown() && this.isMultiSelectable) {
			shiftSelectUnselect(item);
		} else if (e.isControlDown() && this.isMultiSelectable) {
			ctrlSelectUnselect(item);
		} else {
			selectUnselectItem(item);
		}
	}

	private void shiftSelectUnselect(ListItemType item) {
		ListItemType lastSelectedItem = selectedItem.size() > 0 ? getSelectAnchor(item) : item;
		unselectItems();
		if (lastSelectedItem != null) {
			select(lastSelectedItem, item);
		} else {
			select(item);
		}
	}

	private void ctrlSelectUnselect(ListItemType item) {
		if (item.isSelected()) {
			unselectItem(item);
		} else {
			select(item);
		}
	}

	private void select(ListItemType item) {
		this.selectedItem.add(item);
		item.setSelection(true);
	}

	private void select(ListItemType from, ListItemType to) {
		boolean selecting = false;
		if(this.isMultiSelectable && from != to){
			boolean reverse = this.itemList.indexOf(from) > this.itemList.indexOf(to);
			
			ArrayList<ListItemType> reverseList = new ArrayList<ListItemType>(this.itemList);
			
			if(reverse){
				Collections.reverse(reverseList);
			}
			
			for (ListItemType item : reverseList) {
				if (item == from || item == to) {
					selecting = !selecting;
					select(item);
				} else if (selecting) {
					select(item);
				}
			}
		} else {
			select(from);
		}
	}

	public ListItemType getListItemByNode(_ListNode node) {
		for (ListItemType item : this.itemList) {
			if (item.getModelNode() == node)
				return item;
		}
		return null;
	}
	
	public ListItemType getNextItem(ListItemType item){
		int pos = this.itemByPosition.get(item);
		return getAllItems().get(pos + 1);
	}
	
	public ListItemType getBeforeItem(ListItemType item){
		int pos = this.itemByPosition.get(item);
		if(pos > 0)
			return getAllItems().get(pos - 1);
		return null;
	}

	public ArrayList<ListItemType> getAllItems() {
		return this.itemList;
	}

	private static final long serialVersionUID = 2271347445049935275L;

	//
	// Key Listener
	//

	public void implementShortCut(Container comp) {
		comp.removeKeyListener(this);
		comp.addKeyListener(this);
//		for (Component childComp : comp.getComponents()) {
//			this.implementShortCut(childComp);
//		}
	}

	public void implementShortCut(Component comp) {
		if (comp instanceof Container) {
			this.implementShortCut((Container) comp);
		} else {
			comp.removeKeyListener(this);
			comp.addKeyListener(this);
		}
	}

	@Override
	public Component add(Component comp) {
//		comp.removeKeyListener(this);
//		comp.addKeyListener(this);
		return super.add(comp);
	}

	@Override
	public void keyTyped(KeyEvent ke) {

	}

	@Override
	public void keyReleased(KeyEvent ke) {
//		if (KeyEvent.VK_CONTROL == ke.getExtendedKeyCode()) {
//			ctrlKeyPressed = false;
//		} else if (KeyEvent.VK_SHIFT == ke.getExtendedKeyCode()) {
//			shiftKeyPressed = false;
//		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
//		if (KeyEvent.VK_CONTROL == ke.getExtendedKeyCode()) {
//			ctrlKeyPressed = true;
//		} else if (KeyEvent.VK_SHIFT == ke.getExtendedKeyCode()) {
//			shiftKeyPressed = true;
//		}
	}
	
	protected void showHideItems(ArrayList<ListItemType> showItems,
			ArrayList<ListItemType> hideItems, boolean hideGroups) {
		
		HashSet<ListItemType> collapsedItems = new HashSet<ListItemType>();
		
		for (ItemGroup<ModelNodeType, ListItemType> group : this.groups) {
			if(!group.isExpanded()){
				collapsedItems.addAll(group.getItems());
			}
		}
		
		for (ListItemType showItem : showItems) {
			if(!collapsedItems.contains(showItem))
				showItem(showItem);
		}
		
		for (ListItemType hideItem : hideItems) {
			hideItem(hideItem);
		}
		
		if(hideGroups){
			for (ItemGroup<ModelNodeType, ListItemType> item : this.groups) {
				item.checkVisibility();
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	@Override
	public void mousePressed(MouseEvent arg0) {}
	
	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	
	@Override
	public void mouseClicked(MouseEvent me) {
		if(content.contains(me.getX(), me.getY())){
			content.requestFocus();
		}
	}
	
	public Theme getTheme() {
		return this.theme;
	}
	
//	public JPanel getEmptyPanel(){
//		return new JPanel();
//	}

	// public boolean isCtrlPressed(){
	// return ctrlKeyPressed;
	// }
	//
	// public boolean isShiftPressed(){
	// return shiftKeyPressed;
	// }
}
