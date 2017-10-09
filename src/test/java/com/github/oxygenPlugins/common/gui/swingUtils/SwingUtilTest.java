package com.github.oxygenPlugins.common.gui.swingUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Rectangle;

import org.junit.Test;

import com.github.oxygenPlugins.common.gui.swing.SwingUtil;

public class SwingUtilTest {

	static {
		SwingUtil.setDevices(new GraphicsDevice[] {new DummyDevice(new Rectangle(0, 0, 1024, 768))});
	}

	@Test
	public void testIsLocationInScreenBounds() {
		assertTrue(SwingUtil.isLocationInScreenBounds(new Point(0, 0)));
		assertTrue(SwingUtil.isLocationInScreenBounds(new Point(1023, 767)));
		assertTrue(SwingUtil.isLocationInScreenBounds(new Point(500, 0)));
		assertTrue(SwingUtil.isLocationInScreenBounds(new Point(0, 500)));
		assertTrue(SwingUtil.isLocationInScreenBounds(new Point(500, 500)));
		assertFalse(SwingUtil.isLocationInScreenBounds(new Point(-1, 0)));
		assertFalse(SwingUtil.isLocationInScreenBounds(new Point(1024, 0)));
		assertFalse(SwingUtil.isLocationInScreenBounds(new Point(1023, 768)));
		assertFalse(SwingUtil.isLocationInScreenBounds(new Point(2024, 768)));
		assertFalse(SwingUtil.isLocationInScreenBounds(new Point(500, -1)));
	}

	@Test
	public void testmoveOnScreen() {
		
		Point point = SwingUtil.moveOnScreen(new Point(-10, -10), 30, 30);
		assertEquals(0, point.x);
		assertEquals(0, point.y);
		
		point = SwingUtil.moveOnScreen(new Point(500, -10), 200, 50);
		assertEquals(500, point.x);
		assertEquals(0, point.y);
		
		point = SwingUtil.moveOnScreen(new Point(900, -10), 200, 50);
		assertEquals(823, point.x);
		assertEquals(0, point.y);
		
		point = SwingUtil.moveOnScreen(new Point(900, 500), 200, 50);
		assertEquals(823, point.x);
		assertEquals(500, point.y);
		

		point = SwingUtil.moveOnScreen(new Point(900, 750), 200, 50);
		assertEquals(823, point.x);
		assertEquals(717, point.y);

		point = SwingUtil.moveOnScreen(new Point(500, 750), 200, 50);
		assertEquals(500, point.x);
		assertEquals(717, point.y);

		point = SwingUtil.moveOnScreen(new Point(-10, 750), 200, 50);
		assertEquals(0, point.x);
		assertEquals(717, point.y);

		point = SwingUtil.moveOnScreen(new Point(-10, 500), 200, 50);
		assertEquals(0, point.x);
		assertEquals(500, point.y);
	}

}
