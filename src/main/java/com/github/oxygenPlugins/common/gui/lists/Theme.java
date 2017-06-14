package com.github.oxygenPlugins.common.gui.lists;

import java.awt.Color;

import javax.swing.border.Border;

public interface Theme {
	

	public static final int COLOR_TYPE_STANDARD = 0;
	public static final int COLOR_TYPE_INACTIVE = 1;
	public static final int COLOR_TYPE_HIGHLIGHT = 2;
	public static final int COLOR_TYPE_HTTP = 3;
	public static final int COLOR_TYPE_ARCH = 4;
	
	Color getDefaultBackgroundColor();
	Color getDefaultForegroundColor();
	Color getDefaultForegroundColor(int type);
	Border getDefaultBorder();
	
	Color getHoverBackgroundColor();
	Color getHoverForegroundColor();
	Color getHoverForegroundColor(int type);
	Border getHoverBorder();
	
	Color getSelectionBackgroundColor();
	Color getSelectionForegroundColor();
	Color getSelectionForegroundColor(int type);
	Border getSelectionBorder();
	
	boolean isDark();
	Color getFocusForegroundColor(int type);
	Border getFocusBorder();
	Color getFocusForegroundColor();
	Color getFocusBackgroundColor();
}
