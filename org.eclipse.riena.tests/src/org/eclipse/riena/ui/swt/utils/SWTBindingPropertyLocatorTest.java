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

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.tests.collect.UITestCase;
import org.eclipse.riena.ui.common.IComplexComponent;

/**
 * Tests the class {@link SWTBindingPropertyLocator}.
 */
@UITestCase
public class SWTBindingPropertyLocatorTest extends TestCase {

	/**
	 * Tests the <i>private</i> method {@code locateBindingProperty(Widget)}.
	 */
	public void testLocateBindingProperty() {

		SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();

		Shell shell = new Shell();
		String prop = ReflectionUtils.invokeHidden(locator, "locateBindingProperty", shell);
		assertEquals("", prop);

		Label label = new Label(shell, SWT.NONE);
		locator.setBindingProperty(label, "label1");
		prop = ReflectionUtils.invokeHidden(locator, "locateBindingProperty", label);
		assertEquals("label1", prop);

		TestComplexComponent complexComponent = new TestComplexComponent(shell, SWT.NONE);
		locator.setBindingProperty(complexComponent, "complex1");
		Text text = new Text(complexComponent, SWT.NONE);
		locator.setBindingProperty(text, "text1");
		prop = ReflectionUtils.invokeHidden(locator, "locateBindingProperty", text);
		assertEquals("complex1.text1", prop);

		SwtUtilities.disposeWidget(shell);

	}

	private static class TestComplexComponent extends Composite implements IComplexComponent {

		public TestComplexComponent(Composite parent, int style) {
			super(parent, style);
		}

		public List<Object> getUIControls() {
			return null;
		}

	}

}
