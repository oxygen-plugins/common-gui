package com.github.oxygenPlugins.common.gui.lists.items;

import java.io.IOException;

import javax.swing.Icon;

public interface _ListNode {
	boolean hasIcon();
	Icon getIcon() throws IOException;
	void setIcon(Icon icon);
}
