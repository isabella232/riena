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
package org.eclipse.riena.ui.swt.utils;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.common.IComplexComponent;

/**
 * Tests the class {@link SWTBindingPropertyLocator}.
 */
@UITestCase
public class SWTBindingPropertyLocatorTest extends TestCase {

	private Shell shell;

	protected void setUp() {
		shell = new Shell();
	}

	@Override
	protected void tearDown() throws Exception {
		shell.dispose();
	}

	/**
	 * Tests the <i>private</i> method {@code locateBindingProperty(Widget)}.
	 */
	public void testLocateBindingProperty() {
		SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();

		String prop = locator.locateBindingProperty(shell);

		assertEquals("", prop);

		Label label = new Label(shell, SWT.NONE);
		locator.setBindingProperty(label, "label1");
		prop = locator.locateBindingProperty(label);

		assertEquals("label1", prop);

		label.dispose();
		prop = locator.locateBindingProperty(label);

		assertEquals(null, prop);

		prop = locator.locateBindingProperty(null);

		assertEquals(null, prop);

		prop = locator.locateBindingProperty(new Object());

		assertEquals(null, prop);
	}

	public void testLocateBindingPropertyInComplexComponent() {
		SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		TestComplexComponent complexComponent = new TestComplexComponent(shell, SWT.NONE);
		Text text = new Text(complexComponent, SWT.NONE);

		locator.setBindingProperty(complexComponent, "complex1");
		String prop = locator.locateBindingProperty(text);

		assertEquals("", prop);

		locator.setBindingProperty(text, "text1");
		prop = locator.locateBindingProperty(text);

		assertEquals("complex1.text1", prop);

		locator.setBindingProperty(complexComponent, "");
		prop = locator.locateBindingProperty(text);

		assertEquals("text1", prop);
	}

	public void testGetControlsWithBindingProperty() {
		SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		Composite composite1 = new Composite(shell, SWT.NONE);
		Label label1 = new Label(composite1, SWT.NONE);
		Label label2 = new Label(composite1, SWT.NONE);

		locator.setBindingProperty(label1, "label1");
		locator.setBindingProperty(label2, "label2");

		List<Object> result1 = SWTBindingPropertyLocator.getControlsWithBindingProperty(composite1);

		assertEquals(2, result1.size());
		assertTrue(result1.contains(label1));
		assertTrue(result1.contains(label2));
	}

	public void testGetControlsWithBindingPropertyExcludeFirstComposite() {
		SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		Composite composite1 = new Composite(shell, SWT.NONE);
		Label label1 = new Label(composite1, SWT.NONE);

		locator.setBindingProperty(composite1, "complex1");
		locator.setBindingProperty(label1, "label1");

		List<Object> result1 = SWTBindingPropertyLocator.getControlsWithBindingProperty(composite1);

		assertEquals(1, result1.size());
		assertTrue(result1.contains(label1));
	}

	public void testGetControlWithBindingPropertyRecursive() {
		SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();

		Composite composite1 = new Composite(shell, SWT.NONE);
		locator.setBindingProperty(composite1, "composite1");
		Composite composite2 = new Composite(composite1, SWT.NONE);
		locator.setBindingProperty(composite2, "composite2");
		Label c2Label1 = new Label(composite2, SWT.NONE);
		locator.setBindingProperty(c2Label1, "label1");
		Label c2Label2 = new Label(composite2, SWT.NONE);
		locator.setBindingProperty(c2Label2, "label2");

		List<Object> result = SWTBindingPropertyLocator.getControlsWithBindingProperty(composite1);

		assertEquals(3, result.size());
		assertTrue(result.contains(composite2));
		assertTrue(result.contains(c2Label1));
		assertTrue(result.contains(c2Label2));
	}

	// helping classes
	//////////////////

	private static class TestComplexComponent extends Composite implements IComplexComponent {
		public TestComplexComponent(Composite parent, int style) {
			super(parent, style);
		}

		public List<Object> getUIControls() {
			return null;
		}
	}

}
