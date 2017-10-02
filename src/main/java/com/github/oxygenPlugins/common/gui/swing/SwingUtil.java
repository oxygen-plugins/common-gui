package com.github.oxygenPlugins.common.gui.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;

import javax.swing.JPanel;

public class SwingUtil {

	public static void addComponent(final Container cont, final GridBagLayout gbl, final Component c, final int x,
			final int y, final int width, final int height, final double weightx, final double weighty) {
		addComponent(cont, gbl, c, x, y, width, height, weightx, weighty, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH);
	}

	public static void addComponent(final Container cont, final GridBagLayout gbl, final Component c, final int x,
			final int y, final int width, final int height, final double weightx, final double weighty,
			final int anchor, final int fill) {
		addComponent(cont, gbl, c, x, y, width, height, weightx, weighty, anchor, fill, new Insets(0, 0, 0, 0));
	}

	public static void addComponent(final Container container, final GridBagLayout gbl, final Component c, final int x,
			final int y, final int width, final int height, final double weightx, final double weighty,
			final int anchor, final int fill, final Insets insets) {
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

	// public static void centerFrame(final Window frame, final Window
	// parentFrame) {
	// final Rectangle bounds = frame.getBounds();
	// final Rectangle parentBounds = parentFrame.getBounds();
	// bounds.x = (parentBounds.width / 2) - (bounds.width / 2) +
	// parentBounds.x;
	// bounds.y = (parentBounds.height / 2) - (bounds.height / 2) +
	// parentBounds.y;
	// frame.setBounds(bounds);
	// }
	//
	public static void centerFrame(final Window frame, Window owner) {
		if (owner != null) {
			Point ownerLoc = owner.getLocation();
			frame.setLocation(ownerLoc.x + (owner.getWidth() / 2) - (frame.getWidth() / 2),
					ownerLoc.y + (owner.getHeight() / 2) - (frame.getHeight() / 2));
		} else {
			centerFrame(frame);
		}
	}

	/**
	 * This class contains a collection of static utility methods for Swing.
	 * 
	 * @author Heidi Rakels.
	 */
	/**
	 * Verifies if the given point is visible on the screen.
	 * 
	 * @param location
	 *            The given location on the screen.
	 * @return True if the location is on the screen, false otherwise.
	 */
	
	public static void setDevices(GraphicsDevice[] devices){
		graphicsDevices = devices;
	}
	
	private static GraphicsDevice[] graphicsDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
	
	public static boolean isLocationInScreenBounds(Point location) {

		// Check if the location is in the bounds of one of the graphics
		// devices.
		
		Rectangle graphicsConfigurationBounds = new Rectangle();

		// Iterate over the graphics devices.
		for (int j = 0; j < graphicsDevices.length; j++) {

			// Get the bounds of the device.
			GraphicsDevice graphicsDevice = graphicsDevices[j];
			graphicsConfigurationBounds.setRect(graphicsDevice.getDefaultConfiguration().getBounds());

			// Is the location in this bounds?
			graphicsConfigurationBounds.setRect(graphicsConfigurationBounds.x, graphicsConfigurationBounds.y,
					graphicsConfigurationBounds.width, graphicsConfigurationBounds.height);
			if (graphicsConfigurationBounds.contains(location.x, location.y)) {

				// The location is in this screengraphics.
				return true;

			}

		}

		// We could not find a device that contains the given point.
		return false;

	}

	public static Point moveOnScreen(Point topLeft, int width, int height) {
		Point topRight = new Point(topLeft.x + width, topLeft.y);
		Point botLeft = new Point(topLeft.x, topLeft.y + height);
		Point botRight = new Point(topLeft.x + width, topLeft.y + height);

		Point onScreenP = new Point(topLeft.x, topLeft.y);

		ArrayList<Point> visiblePoints = new ArrayList<Point>();
		ArrayList<Point> hiddenPoints = new ArrayList<Point>();

		for (Point p : new Point[] { topLeft, topRight, botLeft, botRight }) {
			if (isLocationInScreenBounds(p)) {
				visiblePoints.add(p);
			} else {
				hiddenPoints.add(p);
			}
		}

		// Check move on x axis
		boolean moveX = false;
		Point vMoveX = null;
		Point hMoveX = null;
		for (Point pointV : visiblePoints) {

			for (Point pointH : hiddenPoints) {
				if (pointV.y == pointH.y) {
					vMoveX = pointV;
					hMoveX = pointH;
					moveX = true;
					break;
				}
			}
			if (moveX)
				break;
		}

		// Check move on y axis
		boolean moveY = false;
		Point vMoveY = null;
		Point hMoveY = null;
		for (Point pointV : visiblePoints) {

			for (Point pointH : hiddenPoints) {
				if (pointV.x == pointH.x) {
					vMoveY = pointV;
					hMoveY = pointH;
					moveY = true;
					break;
				}
			}
			if (moveY)
				break;
		}
		
		int xDiff = 0;
		if(moveX){
			Point middleX = getMove(vMoveX, hMoveX);
			xDiff = hMoveX.x - middleX.x;
		}
		
		int yDiff = 0;
		if(moveY){
			Point middleY = getMove(vMoveY, hMoveY);
			yDiff = hMoveY.y - middleY.y;
		}
		
		return new Point(topLeft.x - xDiff, topLeft.y - yDiff);
	}
	
	private static Point getMove(Point visible, Point hidden){
		boolean moveX = visible.y == hidden.y;
		
		Point middle = new Point(visible.x, visible.y);
		
		int vVal = moveX ? visible.x : visible.y;
		int hVal = moveX ? hidden.x : hidden.y;
		
		int mVal = ((vVal - hVal) / 2) + hVal; 
		
		if(moveX){
			middle.x = mVal;
		} else {
			middle.y = mVal;
		}
		
		if(middle.x == hidden.x && middle.y == hidden.y){
			return visible;
		}
		if(middle.x == visible.x && middle.y == visible.y){
			return visible;
		}
		
		
		boolean isMiddleV = isLocationInScreenBounds(middle);
		visible = isMiddleV ? middle : visible;
		hidden = isMiddleV ? hidden : middle;
		
		return getMove(visible, hidden);
		
		
	}
}
