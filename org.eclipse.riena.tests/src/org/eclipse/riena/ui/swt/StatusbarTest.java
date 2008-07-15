/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.swt;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link Statusbar}.
 */
public class StatusbarTest extends TestCase {

	private Shell shell;
	private Statusbar statusbar;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		statusbar = new Statusbar(shell, SWT.NONE, StatusbarSpacer.class);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.disposeWidget(shell);
		SwtUtilities.disposeWidget(statusbar);
	}

	/**
	 * Tests the method {@code addUIControl(Widget,String)}.
	 */
	public void testAddUIControl() {

		Label label = new Label(shell, SWT.NONE);

		ReflectionUtils.invokeHidden(statusbar, "addUIControl", label, "labelName");
		List<Object> controls = statusbar.getUIControls();
		assertNotNull(controls);
		assertFalse(controls.isEmpty());
		Label labelOfList = (Label) controls.get(controls.size() - 1);
		assertSame(label, labelOfList);
		assertEquals("labelName", labelOfList.getData(SWTBindingPropertyLocator.BINDING_PROPERTY));

		SwtUtilities.disposeWidget(label);

	}

	/**
	 * Tests the method {@code createSpacer(Composite)}.
	 */
	public void testCreateSpacer() {

		Control spacer = ReflectionUtils.invokeHidden(statusbar, "createSpacer", shell);
		assertNotNull(spacer);
		assertTrue(spacer instanceof StatusbarSpacer);
		assertSame(shell, spacer.getParent());

	}

	/**
	 * Tests the method {@code createContents()}.<br>
	 * <i>The method is already called by the constructor.</i>
	 */
	public void testCreateContents() {

		Control[] controls = statusbar.getChildren();
		boolean time = false;
		boolean date = false;
		boolean number = false;
		for (int i = 0; i < controls.length; i++) {
			if (controls[i].getClass() == StatusbarTime.class) {
				if (time) {
					fail("More than of time control!");
				}
				time = true;
			}
			if (controls[i].getClass() == StatusbarDate.class) {
				if (date) {
					fail("More than of date control!");
				}
				date = true;
			}
			if (controls[i].getClass() == StatusbarNumber.class) {
				if (number) {
					fail("More than of number control!");
				}
				number = true;
			}
		}
		assertTrue(time);
		assertTrue(date);
		assertTrue(number);
		assertNotNull(statusbar.getMessageComposite());

		number = false;
		List<Object> uicontrols = statusbar.getUIControls();
		for (Object uiControl : uicontrols) {
			if (uiControl.getClass() == StatusbarNumber.class) {
				if (number) {
					fail("More than of number UI-control!");
				}
				number = true;
				StatusbarNumber statusbarNumber = (StatusbarNumber) uiControl;
				assertEquals("statusBarNumberRidget", statusbarNumber
						.getData(SWTBindingPropertyLocator.BINDING_PROPERTY));
			}
		}
		assertTrue(number);

	}

}
