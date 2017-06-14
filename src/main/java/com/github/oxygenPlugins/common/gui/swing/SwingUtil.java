package com.github.oxygenPlugins.common.gui.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;


public class SwingUtil {
	
	public static void addComponent(final Container cont, final GridBagLayout gbl, final Component c, final int x, final int y,
			final int width, final int height, final double weightx, final double weighty) {
		addComponent(cont, gbl, c, x, y, width, height, weightx, weighty, GridBagConstraints.NORTH, GridBagConstraints.BOTH);
	}

	public static void addComponent(final Container cont, final GridBagLayout gbl, final Component c, final int x, final int y,
			final int width, final int height, final double weightx, final double weighty, final int anchor, final int fill) {
		addComponent(cont, gbl, c, x, y, width, height, weightx, weighty, anchor, fill, new Insets(0, 0, 0, 0));
	}

	public static void addComponent(final Container container, final GridBagLayout gbl, final Component c, final int x, final int y,
			final int width, final int height, final double weightx, final double weighty, final int anchor, final int fill,
			final Insets insets) {
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = fill;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.anchor = anchor;
		gbc.insets = insets;
		gbl.setConstraints(c, gbc);
		container.add(c);
	}
	public static void centerFrame(final Window frame) {
		final Rectangle bounds = frame.getBounds();
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		bounds.x = (screen.width / 2) - (bounds.width / 2);
		bounds.y = (screen.height / 2) - (bounds.height / 2);
		frame.setBounds(bounds);
	}
	
//	public static void centerFrame(final Window frame, final Window parentFrame) {
//		final Rectangle bounds = frame.getBounds();
//		final Rectangle parentBounds = parentFrame.getBounds();
//		bounds.x = (parentBounds.width / 2) - (bounds.width / 2) + parentBounds.x;
//		bounds.y = (parentBounds.height / 2) - (bounds.height / 2) + parentBounds.y;
//		frame.setBounds(bounds);
//	}
//	
	public static void centerFrame(final Window  frame, Window owner){
		if(owner != null){
			Point ownerLoc	= owner.getLocation();
			frame.setLocation(ownerLoc.x + (owner.getWidth() / 2) - (frame.getWidth() / 2),
					ownerLoc.y + (owner.getHeight() / 2) - (frame.getHeight() / 2));
		} else {
			centerFrame(frame);
		}
	}
}
