/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *    Florian Pirchner - FontDescriptor
 *******************************************************************************/
package org.eclipse.riena.ui.swt.lnf.rienadefault;

import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.swt.lnf.ILnfResource;
import org.eclipse.riena.ui.swt.lnf.ILnfTheme;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;

/**
 * Tests of the class <code>RienaDefaultLnf</code>.
 */
@NonUITestCase
public class RienaDefaultLnfTest extends TestCase {

	private static final boolean BOOLEAN_VALUE = true;
	private static final Integer INTEGER_VALUE = 4;
	private static final String BOOLEAN_KEY = "boolean";
	private static final String INTEGER_KEY = "integer";

	private RienaDefaultLnf lnf;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		lnf = new RienaDefaultLnf();
		lnf.initialize();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		lnf.setTheme(null);
		lnf.uninitialize();
		lnf = null;
	}

	/**
	 * Test of the method <code>initialize()</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testInitialize() throws Exception {

		lnf.uninitialize();

		assertNull(lnf.getRenderer(LnfKeyConstants.SUB_MODULE_VIEW_BORDER_RENDERER));
		assertNull(lnf.getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_FOREGROUND));

		lnf.initialize();

		assertNotNull(lnf.getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_FOREGROUND));

	}

	/**
	 * Test of the method <code>uninitialize()</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testUninitialize() throws Exception {

		Color color = lnf.getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_FOREGROUND);
		assertNotNull(color);

		lnf.uninitialize();

		assertTrue(color.isDisposed());
		assertNull(lnf.getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_FOREGROUND));

	}

	/**
	 * Test of the method <code>getColor(String)</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testGetColor() throws Exception {

		lnf.initialize();
		assertNotNull(lnf.getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_FOREGROUND));
		assertNull(lnf.getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_FONT));
		assertNull(lnf.getColor("dummy"));

	}

	/**
	 * Test of the method <code>getFont(String)</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testGetFont() throws Exception {

		lnf.initialize();
		assertNull(lnf.getFont(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_FOREGROUND));
		assertNotNull(lnf.getFont(LnfKeyConstants.EMBEDDED_TITLEBAR_FONT));
		assertNull(lnf.getFont("dummy"));

	}

	/**
	 * Test of the method <code>getFont(String, int, int)</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testGetFontWithProps() throws Exception {

		lnf.initialize();
		Font font = lnf.getFont(LnfKeyConstants.EMBEDDED_TITLEBAR_FONT, 10, SWT.BOLD | SWT.ITALIC);
		FontData data = font.getFontData()[0];
		assertEquals(SWT.BOLD | SWT.ITALIC, data.getStyle());

		Font font1 = lnf.getFont(LnfKeyConstants.EMBEDDED_TITLEBAR_FONT, 12, SWT.BOLD);
		data = font1.getFontData()[0];
		assertEquals(SWT.BOLD, data.getStyle());

		Font font2 = lnf.getFont(LnfKeyConstants.EMBEDDED_TITLEBAR_FONT, 12, SWT.BOLD);
		assertSame(font1, font2);

		Font fontNull = lnf.getFont(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_FOREGROUND, 12, SWT.BOLD);
		assertNull(fontNull);
	}

	/**
	 * Test of the method <code>getTheme()</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testGetTheme() throws Exception {

		assertEquals(RienaDefaultTheme.class, lnf.getTheme().getClass());

		lnf.setTheme(new DummyTheme());
		assertEquals(DummyTheme.class, lnf.getTheme().getClass());

	}

	/**
	 * Test of the method <code>setTheme()</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testSetTheme() throws Exception {

		Color color = lnf.getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_FOREGROUND);
		assertNotNull(color);

		lnf.setTheme(new DummyTheme());
		lnf.initialize();
		assertTrue(color.isDisposed());

	}

	/**
	 * Test of the method {@code getIntegerSetting(String, Integer)}.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testGetIntegerSetting() throws Exception {

		Integer value = lnf.getIntegerSetting(INTEGER_KEY, 300);
		assertEquals(300, value.intValue());

		lnf.setTheme(new DummyTheme());
		lnf.initialize();
		value = lnf.getIntegerSetting(INTEGER_KEY, 300);
		assertEquals(INTEGER_VALUE.intValue(), value.intValue());

	}

	/**
	 * Test of the method {@code getBooleanSetting(String, boolean)}.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testGetBooleanSetting() throws Exception {

		boolean value = lnf.getBooleanSetting(BOOLEAN_KEY, true);
		assertTrue(value);
		value = lnf.getBooleanSetting(BOOLEAN_KEY, false);
		assertFalse(value);

		lnf.setTheme(new DummyTheme());
		lnf.initialize();
		value = lnf.getBooleanSetting(BOOLEAN_KEY, false);
		assertEquals(BOOLEAN_VALUE, value);

	}

	private static class DummyTheme implements ILnfTheme {

		/**
		 * @see org.eclipse.riena.ui.swt.lnf.ILnfTheme#addCustomColors(java.util.Map)
		 */
		public void addCustomColors(Map<String, ILnfResource> table) {
		}

		/**
		 * @see org.eclipse.riena.ui.swt.lnf.ILnfTheme#addCustomFonts(java.util.Map)
		 */
		public void addCustomFonts(Map<String, ILnfResource> table) {
		}

		/**
		 * @see org.eclipse.riena.ui.swt.lnf.ILnfTheme#addCustomImages(java.util.Map)
		 */
		public void addCustomImages(Map<String, ILnfResource> table) {
		}

		/**
		 * @see org.eclipse.riena.ui.swt.lnf.ILnfTheme#addCustomSettings(java.util.Map)
		 */
		public void addCustomSettings(Map<String, Object> table) {
			table.put(INTEGER_KEY, INTEGER_VALUE);
			table.put(BOOLEAN_KEY, BOOLEAN_VALUE);
		}

	}

}
