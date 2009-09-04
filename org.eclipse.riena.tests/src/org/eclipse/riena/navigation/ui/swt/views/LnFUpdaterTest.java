/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.views;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.lnf.ColorLnfResource;
import org.eclipse.riena.ui.swt.lnf.ILnfResource;
import org.eclipse.riena.ui.swt.lnf.ILnfTheme;
import org.eclipse.riena.ui.swt.lnf.LnFUpdater;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Tests of the class {@link LnFUpdater}.
 */
@UITestCase
public class LnFUpdaterTest extends RienaTestCase {

	private Shell shell;
	private LnFUpdater lnFUpdater;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		lnFUpdater = new LnFUpdater();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		SwtUtilities.disposeWidget(shell);
		lnFUpdater = null;
	}

	/**
	 * Tests the <i>private</i> method {@code getErrorMessage}.
	 * 
	 * @throws IntrospectionException
	 */
	public void testGetErrorMessage() throws IntrospectionException {

		PropertyDescriptor property = getForegroundProperty();
		Label label = new Label(shell, SWT.NONE);
		String message = ReflectionUtils.invokeHidden(lnFUpdater, "getErrorMessage", label, property);
		assertNotNull(message);
		assertTrue(message.indexOf(Label.class.getSimpleName()) > 0);
		assertTrue(message.indexOf(property.getName()) > 0);
		SwtUtilities.disposeWidget(label);

	}

	/**
	 * Tests the <i>private</i> method {@code getLnfValue}.
	 */
	public void testGetLnfValue() throws IntrospectionException {
		RienaDefaultLnf oldLnf = LnfManager.getLnf();
		RienaDefaultLnf lnf = new RienaDefaultLnf();
		LnfManager.setLnf(lnf);

		try {
			lnf.setTheme(new MyTheme());
			Label label = new Label(shell, SWT.NONE);
			PropertyDescriptor property = getForegroundProperty();
			Object value = ReflectionUtils.invokeHidden(lnFUpdater, "getLnfValue", label, property);
			SwtUtilities.disposeWidget(label);

			assertNotNull(value);
			assertTrue(value instanceof Color);
			Color color = (Color) value;
			assertEquals(1, color.getRed());
			assertEquals(2, color.getGreen());
			assertEquals(3, color.getBlue());

			lnf.setTheme(new MyTheme());
			Text text = new Text(shell, SWT.NONE);
			value = ReflectionUtils.invokeHidden(lnFUpdater, "getLnfValue", text, property);
			SwtUtilities.disposeWidget(text);

			assertNull(value);
		} finally {
			LnfManager.setLnf(oldLnf);
		}

	}

	/**
	 * Tests the <i>private</i> method {@code getLnfStyleValue}.
	 */
	public void testGetLnfStyleValue() throws IntrospectionException {
		RienaDefaultLnf oldLnf = LnfManager.getLnf();
		RienaDefaultLnf lnf = new RienaDefaultLnf();
		LnfManager.setLnf(lnf);

		try {
			// Label widget with style (existing) "section"
			lnf.setTheme(new MyTheme());
			Label label = new Label(shell, SWT.NONE);
			label.setData(UIControlsFactory.KEY_LNF_STYLE, "section");
			PropertyDescriptor property = getForegroundProperty();
			Object value = ReflectionUtils.invokeHidden(lnFUpdater, "getLnfStyleValue", label, property);
			SwtUtilities.disposeWidget(label);

			assertNotNull(value);
			assertTrue(value instanceof Color);
			Color color = (Color) value;
			assertEquals(111, color.getRed());
			assertEquals(22, color.getGreen());
			assertEquals(3, color.getBlue());

			// Label widget with style (not existing) "dummy"
			lnf.setTheme(new MyTheme());
			label = new Label(shell, SWT.NONE);
			label.setData(UIControlsFactory.KEY_LNF_STYLE, "dummy");
			value = ReflectionUtils.invokeHidden(lnFUpdater, "getLnfStyleValue", label, property);
			SwtUtilities.disposeWidget(label);

			assertNull(value);

			// Text widget with style (existing) "section"
			// (It also works for other widgets.)
			lnf.setTheme(new MyTheme());
			Text text = new Text(shell, SWT.NONE);
			text.setData(UIControlsFactory.KEY_LNF_STYLE, "section");
			value = ReflectionUtils.invokeHidden(lnFUpdater, "getLnfStyleValue", text, property);
			SwtUtilities.disposeWidget(label);

			assertNotNull(value);
			assertTrue(value instanceof Color);
			color = (Color) value;
			assertEquals(111, color.getRed());
			assertEquals(22, color.getGreen());
			assertEquals(3, color.getBlue());
		} finally {
			LnfManager.setLnf(oldLnf);
		}
	}

	/**
	 * Tests the <i>private</i> method {@code checkUpDateAfterBind}.
	 */
	public void testCheckUpDateAfterBind() {

		Composite comp1 = new Composite(shell, SWT.NONE);
		Label label1 = new Label(comp1, SWT.NONE);
		boolean retValue = ReflectionUtils.invokeHidden(lnFUpdater, "checkUpDateAfterBind", label1);
		assertFalse(retValue);

		ChoiceComposite choiceComp1 = new ChoiceComposite(comp1, SWT.NONE, false);
		retValue = ReflectionUtils.invokeHidden(lnFUpdater, "checkUpDateAfterBind", choiceComp1);
		assertTrue(retValue);
		Label label2 = new Label(choiceComp1, SWT.NONE);
		retValue = ReflectionUtils.invokeHidden(lnFUpdater, "checkUpDateAfterBind", label2);
		assertTrue(retValue);

	}

	/**
	 * Tests the <i>private</i> method {@code checkPropertyUpdateView}.
	 */
	public void testCheckPropertyUpdateView() {

		boolean retValue = ReflectionUtils.invokeHidden(lnFUpdater, "checkPropertyUpdateView");
		assertFalse(retValue);

		System.setProperty("riena.lnf.update.view", "abc");
		retValue = ReflectionUtils.invokeHidden(lnFUpdater, "checkPropertyUpdateView");
		assertFalse(retValue);

		System.setProperty("riena.lnf.update.view", "true");
		retValue = ReflectionUtils.invokeHidden(lnFUpdater, "checkPropertyUpdateView");
		assertTrue(retValue);

		System.setProperty("riena.lnf.update.view", "false");
		retValue = ReflectionUtils.invokeHidden(lnFUpdater, "checkPropertyUpdateView");
		assertFalse(retValue);

	}

	/**
	 * Tests the <i>private</i> method {@code updateUIControl}.
	 */
	public void testUpdateUIControl() {
		RienaDefaultLnf oldLnf = LnfManager.getLnf();
		try {
			RienaDefaultLnf lnf = new RienaDefaultLnf();
			LnfManager.setLnf(lnf);
			lnf.setTheme(new MyTheme());
			Label label = new Label(shell, SWT.NONE);
			ReflectionUtils.invokeHidden(lnFUpdater, "updateUIControl", label);
			Color labelColor = label.getForeground();
			Color themeColor = lnf.getColor("Label.foreground");
			assertEquals(themeColor, labelColor);
		} finally {
			LnfManager.setLnf(oldLnf);
		}
	}

	/**
	 * Tests the <i>private</i> method {@code getDefaultPropertyValue}.
	 * 
	 * @throws IntrospectionException
	 *             - handled by jUnit
	 */
	public void testGetDefaultPropertyValue() throws IntrospectionException {

		Label label = new Label(shell, SWT.NONE);
		PropertyDescriptor foregroundProperty = new PropertyDescriptor("foreground", Label.class);
		Object value = ReflectionUtils.invokeHidden(lnFUpdater, "getDefaultPropertyValue", label, foregroundProperty);
		assertEquals(label.getForeground(), value);

	}

	/**
	 * Tests the <i>private</i> method {@code getPropertyValue}.
	 * 
	 * @throws IntrospectionException
	 *             - handled by jUnit
	 */
	public void testGetPropertyValue() throws IntrospectionException {

		Label label = new Label(shell, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		PropertyDescriptor property = new PropertyDescriptor("alignment", Label.class);
		Object value = ReflectionUtils.invokeHidden(lnFUpdater, "getPropertyValue", label, property);
		assertEquals(SWT.RIGHT, value);

	}

	/**
	 * Tests the <i>private</i> method {@code hasNoDefaultValue}.
	 * 
	 * @throws IntrospectionException
	 *             - handled by jUnit
	 */
	public void testHasNoDefaultValue() throws IntrospectionException {

		Label label = new Label(shell, SWT.NONE);
		PropertyDescriptor property = new PropertyDescriptor("text", Label.class);
		Boolean ret = ReflectionUtils.invokeHidden(lnFUpdater, "hasNoDefaultValue", label, property);
		assertFalse(ret);

		label.setText("Hello!");
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "hasNoDefaultValue", label, property);
		assertTrue(ret);

	}

	/**
	 * Tests the <i>private</i> method {@code getSimpleClassName}.
	 */
	public void testGetSimpleClassName() {

		Class<? extends Control> controlClass = Label.class;
		String className = ReflectionUtils.invokeHidden(lnFUpdater, "getSimpleClassName", controlClass);
		assertEquals(Label.class.getSimpleName(), className);

		Control innerControl = new Composite(shell, SWT.NONE) {
			@Override
			public boolean setFocus() {
				return true;
			}
		};

		controlClass = innerControl.getClass();
		className = ReflectionUtils.invokeHidden(lnFUpdater, "getSimpleClassName", controlClass);
		assertEquals(Composite.class.getSimpleName(), className);

	}

	/**
	 * Tests the <i>private</i> method {@code checkLnfKeys}.
	 */
	public void testCheckLnfKeys() {
		RienaDefaultLnf oldLnf = LnfManager.getLnf();
		try {
			RienaDefaultLnf lnf = new RienaDefaultLnf();
			LnfManager.setLnf(lnf);
			ILnfTheme oldTheme = lnf.getTheme();
			MyTheme myTheme = new MyTheme();
			lnf.setTheme(myTheme);
			Label label = new Label(shell, SWT.NONE);
			boolean ret = ReflectionUtils.invokeHidden(lnFUpdater, "checkLnfKeys", label);
			lnf.setTheme(oldTheme);
			SwtUtilities.disposeWidget(label);

			assertTrue(ret);

			lnf.setTheme(myTheme);
			Text text = new Text(shell, SWT.NONE);
			ret = ReflectionUtils.invokeHidden(lnFUpdater, "checkLnfKeys", text);
			lnf.setTheme(oldTheme);
			SwtUtilities.disposeWidget(text);

			assertFalse(ret);
		} finally {
			LnfManager.setLnf(oldLnf);
		}
	}

	private PropertyDescriptor getForegroundProperty() throws IntrospectionException {

		BeanInfo beanInfo = Introspector.getBeanInfo(Label.class);
		PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : properties) {
			if (property.getName().equals("foreground")) {
				return property;
			}
		}

		return null;

	}

	private static class MyTheme implements ILnfTheme {

		public void addCustomColors(Map<String, ILnfResource> table) {
			table.put("Label.foreground", new ColorLnfResource(1, 2, 3));
			table.put("section.foreground", new ColorLnfResource(111, 22, 3));
		}

		public void addCustomFonts(Map<String, ILnfResource> table) {
		}

		public void addCustomImages(Map<String, ILnfResource> table) {
		}

		public void addCustomSettings(Map<String, Object> table) {
		}

	}

}
