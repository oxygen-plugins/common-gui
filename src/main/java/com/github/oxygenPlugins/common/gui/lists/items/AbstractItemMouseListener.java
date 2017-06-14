package com.github.oxygenPlugins.common.gui.lists.items;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.github.oxygenPlugins.common.gui.lists.AbstractList;


public abstract class AbstractItemMouseListener<ListNodeType extends _ListNode, ListItemType extends AbstractListItem<ListNodeType> & Comparable<ListItemType>>
		extends MouseAdapter {

	protected final ListItemType item;
	protected final AbstractList<ListNodeType, ListItemType> list;
	@SuppressWarnings("unused")
	private boolean wasDoubleClick;
//	private Timer timer;

	public AbstractItemMouseListener(ListItemType item,
			AbstractList<ListNodeType, ListItemType> list) {
		this.item = item;
		this.list = list;
	}

	public void mouseClicked(final MouseEvent e) {
		if(e.isPopupTrigger()) {
			return;
		} else if (e.getClickCount() == 2) {
			doubleClick(e);
			wasDoubleClick = true;
		} else {

			list.selectUnselectItem(e, item);
			oneClick(e, item.isSelected());
			
//			if (wasDoubleClick) {
//				wasDoubleClick = false; // reset flag
//			} else {
//			}

			// Integer timerinterval = (Integer)
			// Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
			// timer = new Timer(timerinterval.intValue(), new ActionListener()
			// {
			// public void actionPerformed(ActionEvent evt) {
			//
			// }
			// });
			// timer.setRepeats(false);
			// timer.start();
		}
	}

	public abstract void oneClick(MouseEvent e, boolean isSelected);

	public abstract void doubleClick(MouseEvent e);
}
