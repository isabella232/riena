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

import junit.framework.TestCase;

import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link StatusbarNumber}.
 */
public class StatusbarNumberTest extends TestCase {

	private Shell shell;
	private StatusbarNumber statusNumber;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		statusNumber = new StatusbarNumber(shell, SWT.NONE);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.disposeWidget(shell);
		SwtUtilities.disposeWidget(statusNumber);
	}

	/**
	 * Tests the method {@code createContents()}.<br>
	 * <i>The method is already called by the constructor.</i>
	 */
	public void testCreateContents() {

		Control[] controls = statusNumber.getChildren();
		assertEquals(1, controls.length);
		assertTrue(controls[0] instanceof Label);
		Label label = (Label) controls[0];
		assertEquals("0000000", label.getText());

	}

	/**
	 * Tests the method {@code setNumber(String)}.
	 */
	public void testSetNumberString() {

		statusNumber.setNumber("4711-a");

		Control[] controls = statusNumber.getChildren();
		Label label = (Label) controls[0];
		assertEquals("4711-a", label.getText());

		statusNumber.setNumber(null);

		assertEquals("", label.getText());

	}

	/**
	 * Tests the method {@code setNumber(int)}.
	 */
	public void testSetNumberInt() {

		statusNumber.setNumber(12);

		Control[] controls = statusNumber.getChildren();
		Label label = (Label) controls[0];
		assertEquals("0000012", label.getText());

		statusNumber.setNumber(0);

		assertEquals("", label.getText());

		statusNumber.setNumber(12345678);

		assertEquals("12345678", label.getText());

	}

}
