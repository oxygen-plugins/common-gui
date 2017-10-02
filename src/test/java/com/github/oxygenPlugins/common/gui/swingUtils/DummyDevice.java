package com.github.oxygenPlugins.common.gui.swingUtils;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;

public class DummyDevice extends GraphicsDevice {
	
	private Rectangle bounds;

	public DummyDevice(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getIDstring() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GraphicsConfiguration[] getConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GraphicsConfiguration getDefaultConfiguration() {
		// TODO Auto-generated method stub
		return new GraphicsConfiguration() {
			
			@Override
			public AffineTransform getNormalizingTransform() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public GraphicsDevice getDevice() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public AffineTransform getDefaultTransform() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ColorModel getColorModel(int transparency) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ColorModel getColorModel() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Rectangle getBounds() {
				// TODO Auto-generated method stub
				return bounds;
			}
		};
	}

}
