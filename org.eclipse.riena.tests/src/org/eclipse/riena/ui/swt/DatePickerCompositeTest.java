/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.ui.swt.DatePickerComposite.DatePicker;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link DatePickerComposite}.
 */
@UITestCase
public class DatePickerCompositeTest extends TestCase {

	private Shell shell;
	private DatePickerComposite dpComposite;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		dpComposite = new DatePickerComposite(shell, SWT.NONE);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		SwtUtilities.disposeWidget(shell);
	}

	/**
	 * As per Bug 312541.
	 */
	public void testReopenAfterESC() {
		UITestHelper.fireSelectionEvent(getPickerButton());

		assertFalse(getPickerShell().isDisposed());
		assertTrue(getPicker().isVisible());

		// simulates an ESC keypress, which disposes the shell
		getPickerShell().dispose();

		assertTrue(getPickerShell().isDisposed());

		UITestHelper.fireSelectionEvent(getPickerButton());

		assertFalse(getPickerShell().isDisposed());
		assertTrue(getPicker().isVisible());
	}

	// helping methods
	//////////////////

	private DatePicker getPicker() {
		return ReflectionUtils.getHidden(dpComposite, "datePicker");
	}

	private Shell getPickerShell() {
		return ReflectionUtils.getHidden(getPicker(), "shell");
	}

	private Button getPickerButton() {
		return ReflectionUtils.getHidden(dpComposite, "pickerButton");
	}

}
