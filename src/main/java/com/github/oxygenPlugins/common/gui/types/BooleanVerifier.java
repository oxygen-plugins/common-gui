package com.github.oxygenPlugins.common.gui.types;

public class BooleanVerifier extends MultiChoiceVerifier {
	public BooleanVerifier() {
		super(new String[]{"true", "false"},  false);
	}
}
