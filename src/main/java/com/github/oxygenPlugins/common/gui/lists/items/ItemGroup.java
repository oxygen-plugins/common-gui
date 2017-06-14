package com.github.oxygenPlugins.common.gui.lists.items;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Icon;

import com.github.oxygenPlugins.common.gui.lists.AbstractList;
import com.github.oxygenPlugins.common.gui.lists.Theme;


public abstract class ItemGroup<SubNodeType extends _ListNode, SubItemType extends AbstractListItem<SubNodeType> & Comparable<SubItemType>>
		extends AbstractListItem<_ListGroupNode> {

	private static final long serialVersionUID = -3830254020006810672L;

	protected final ArrayList<SubItemType> items;

	private boolean isExpanded = true;

	private final AbstractList<SubNodeType, SubItemType> parentList;
	private final ArrayList<ItemGroup<SubNodeType, SubItemType>> subGroups;

	private CollapseListener collapseListener;

	public ItemGroup(_ListGroupNode node, ArrayList<SubItemType> items,
			ArrayList<ItemGroup<SubNodeType, SubItemType>> subGroups,
			int level, AbstractList<SubNodeType, SubItemType> parentList, Icon defaultIcon, Theme theme, boolean collapse) {
		super(node, level, defaultIcon, theme);

		this.items = items;
		this.subGroups = subGroups;
		this.parentList = parentList;

		setLevel(level);

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectSubItems(e);
				super.mouseClicked(e);
			}
		});
		this.addIconMouseAdapter(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (isExpanded) {
					collapse();
				} else {
					expand();
				}
				super.mouseClicked(me);
			}
		});
		if(collapse){
			this.isExpanded = false;
			collapse();
		}
	}

	@Override
	public void setLevel(int level) {
		super.setLevel(level);

		for (ItemGroup<SubNodeType, SubItemType> subGroup : subGroups) {
			subGroup.setLevel(level + 1);
		}
		for (SubItemType item : items) {
			item.setLevel(level + 1);
		}
	}

	protected void selectSubItems(MouseEvent e) {
		ArrayList<SubItemType> descItems = getDescandentItems();

		this.parentList.selectUnselectItem(e, descItems);
	}

	private ArrayList<SubItemType> getDescandentItems() {
		ArrayList<SubItemType> descItems = new ArrayList<SubItemType>();

		for (ItemGroup<SubNodeType, SubItemType> group : this.getSubGroups()) {
			descItems.addAll(group.getDescandentItems());
		}

		descItems.addAll(getItems());

		return descItems;
	}

	// @Override
	// public Icon getDefaultIcon() {
	// // if(this.node.getMessageCount() > 0){
	// // return ema.getIcon(IconMap.MESSAGE_GROUP_FAILED);
	// // } else {
	// // return ema.getIcon(IconMap.MESSAGE_GROUP_SUCCEDED);
	// // }
	// return null;
	// }

	public ArrayList<SubItemType> getItems() {
		ArrayList<SubItemType> subItems = new ArrayList<SubItemType>();
		// for (ItemGroup<SubNodeType, SubItemType> group : subGroups) {
		// subItems.addAll(group.getItems());
		// }
		subItems.addAll(items);
		return subItems;
	}

	public void expand() {
		for (ItemGroup<SubNodeType, SubItemType> group : subGroups) {
			group.setVisible(true);
			group.expand();
		}
		for (SubItemType subItem : this.items) {
			subItem.setVisible(true);
		}
		if(collapseListener != null)
			collapseListener.expand();
		this.isExpanded = true;
	};

	public void collapse() {
		for (ItemGroup<SubNodeType, SubItemType> group : subGroups) {
			group.setVisible(false);
			group.collapse();
		}
		for (SubItemType subItem : this.items) {
			subItem.setVisible(false);
		}
		if(collapseListener != null)
			collapseListener.collapse();
		this.isExpanded = false;
	}
	
	public static interface CollapseListener {
		public void collapse();
		public void expand();
	}
	
	public void setCollapseListener(CollapseListener listener){
		this.collapseListener = listener;
	}
	public void removeCollapseListener(){
		this.collapseListener = null;
	}

	public ArrayList<ItemGroup<SubNodeType, SubItemType>> getSubGroups() {
		return this.subGroups;
	}
	
	@Override
	public void checkVisibility() {
		if(!this.isExpanded){
			this.setVisible(true);
		} else {
			boolean isOneVisible = false;
			for (SubItemType subItem : this.getItems()) {
				if(subItem.isVisible()){
					isOneVisible = true;
				}
			}
			this.setVisible(isOneVisible);
		}
	}

	public boolean isExpanded() {
		return this.isExpanded;
	}

}
