package com.github.oxygenPlugins.common.gui.types;

import static org.junit.Assert.*;

import org.junit.Test;

public class ValidCharVerifierTest {

	@Test
	public void testCheckValueValidChars() {
		
		// with leading char
		ValidCharVerifier vcv = new ValidCharVerifier("123", "-", true);
		
		assertTrue(vcv.checkValue("-123"));
		assertTrue(vcv.checkValue("123"));
		assertTrue(vcv.checkValue("111"));
		
		assertFalse(vcv.checkValue("-1-23"));
		assertFalse(vcv.checkValue("1234"));
		assertFalse(vcv.checkValue(""));
		
		// without leading char
		vcv = new ValidCharVerifier("123", "", true);
		
		assertFalse(vcv.checkValue("-123"));
		assertTrue(vcv.checkValue("123"));
		assertTrue(vcv.checkValue("111"));
		
		assertFalse(vcv.checkValue("1234"));
		assertFalse(vcv.checkValue(""));
	}
	
	@Test
	public void testCheckValueInvalidChars() {
//		without leading char
		ValidCharVerifier vcv = new ValidCharVerifier("123", "", false);
		
		assertTrue(vcv.checkValue("55"));
		assertTrue(vcv.checkValue("abc"));
		assertTrue(vcv.checkValue("99"));
		assertTrue(vcv.checkValue(""));
		
		assertFalse(vcv.checkValue("91"));
		

//		without leading char
		vcv = new ValidCharVerifier("123", "-", false);
		
		assertTrue(vcv.checkValue("5-5"));
		assertTrue(vcv.checkValue("abc-"));
		assertTrue(vcv.checkValue("99"));
		assertTrue(vcv.checkValue(""));
		
		assertFalse(vcv.checkValue("91"));
		assertFalse(vcv.checkValue("-99"));
		
	}

}
