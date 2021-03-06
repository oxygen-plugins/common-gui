package com.github.oxygenPlugins.common.gui.lists;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class DarkTheme implements Theme {
	
	private static final Color DEFAULT_BG_COLOR = new Color(80, 80, 80);
	private static final Color[] DEFAULT_FG_COLOR = new Color[]{new Color(200, 200, 200), Color.GRAY, Color.BLUE, new Color(155, 155, 0, 255), new Color(0, 155, 155, 255)};
	private static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(1, 0, 1, 0);
	
	private static final Color HOVER_BG_COLOR = new Color(80, 80, 80);
	private static final Color[] HOVER_FG_COLOR = new Color[]{new Color(200, 200, 200), Color.GRAY, Color.BLUE, new Color(155, 155, 0, 255), new Color(0, 155, 155, 255)};
	private static final Border HOVER_BORDER = BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(0, 150, 255));

	private static final Color SELECTION_BG_COLOR = new Color(55, 55, 55);
	private static final Color[] SELECTION_FG_COLOR = new Color[]{new Color(200, 200, 200), Color.GRAY, Color.BLUE, new Color(155, 155, 0, 255), new Color(0, 155, 155, 255)};
	private static final Border SELECTION_BORDER = BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(200, 200, 200));

	private static final Color FOCUS_BG_COLOR = Color.WHITE;
	private static final Color[] FOCUS_FG_COLOR = new Color[]{Color.BLACK, Color.GRAY, Color.BLUE, new Color(155, 155, 0, 255), new Color(0, 155, 155, 255)};
	private static final Border FOCUS_BORDER = BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(0, 0, 0));

	@Override
	public Color getDefaultBackgroundColor() {
		// TODO Auto-generated method stub
		return DEFAULT_BG_COLOR;
	}

	@Override
	public Color getDefaultForegroundColor() {
		// TODO Auto-generated method stub
		return getDefaultForegroundColor(COLOR_TYPE_STANDARD);
	}

	@Override
	public Border getDefaultBorder() {
		// TODO Auto-generated method stub
		return DEFAULT_BORDER;
	}

	@Override
	public Color getHoverBackgroundColor() {
		// TODO Auto-generated method stub
		return HOVER_BG_COLOR;
	}

	@Override
	public Color getHoverForegroundColor() {
		// TODO Auto-generated method stub
		return getHoverForegroundColor(COLOR_TYPE_STANDARD);
	}

	@Override
	public Border getHoverBorder() {
		// TODO Auto-generated method stub
		return HOVER_BORDER;
	}

	@Override
	public Color getSelectionBackgroundColor() {
		// TODO Auto-generated method stub
		return SELECTION_BG_COLOR;
	}

	@Override
	public Color getSelectionForegroundColor() {
		// TODO Auto-generated method stub
		return getSelectionForegroundColor(COLOR_TYPE_STANDARD);
	}

	@Override
	public Border getSelectionBorder() {
		// TODO Auto-generated method stub
		return SELECTION_BORDER;
	}

	@Override
	public Color getDefaultForegroundColor(int type) {
		// TODO Auto-generated method stub
		return DEFAULT_FG_COLOR[type];
	}

	@Override
	public Color getHoverForegroundColor(int type) {
		// TODO Auto-generated method stub
		return HOVER_FG_COLOR[type];
	}

	@Override
	public Color getSelectionForegroundColor(int type) {
		// TODO Auto-generated method stub
		return SELECTION_FG_COLOR[type];
	}

	@Override
	public boolean isDark() {
		return true;
	}

	@Override
	public Color getFocusForegroundColor(int type) {
		return FOCUS_FG_COLOR[type];
	}

	@Override
	public Border getFocusBorder() {
		return FOCUS_BORDER;
	}

	@Override
	public Color getFocusForegroundColor() {
		return getFocusForegroundColor(COLOR_TYPE_STANDARD);
	}

	@Override
	public Color getFocusBackgroundColor() {
		return FOCUS_BG_COLOR;
	}

}
