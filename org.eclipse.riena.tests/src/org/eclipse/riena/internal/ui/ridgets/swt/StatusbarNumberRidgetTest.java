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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IStatusbarNumberRidget;
import org.eclipse.riena.ui.swt.StatusbarNumber;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 *
 */
public class StatusbarNumberRidgetTest extends AbstractSWTRidgetTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#createRidget()
	 */
	@Override
	protected IRidget createRidget() {
		return new StatusbarNumberRidget();
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#createUIControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createUIControl(Composite parent) {
		return new StatusbarNumber(parent, SWT.NONE);
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#getRidget()
	 */
	@Override
	protected IStatusbarNumberRidget getRidget() {
		return (IStatusbarNumberRidget) super.getRidget();
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#getUIControl()
	 */
	@Override
	protected StatusbarNumber getUIControl() {
		return (StatusbarNumber) super.getUIControl();
	}

	/**
	 * Returns the label of {@code StatusbarNumber}.
	 * 
	 * @return label
	 */
	private Label getLabel() {
		StatusbarNumber statusbarNumber = getUIControl();
		Control[] controls = statusbarNumber.getChildren();
		return (Label) controls[0];
	}

	/**
	 * Tests the method {@code setNumber(int)}.
	 */
	public void testSetNumber() {

		getRidget().setNumber(4711);
		assertEquals("0004711", getLabel().getText());

	}

	/**
	 * Tests the method {@code setNumberString(String)}.
	 */
	public void testSetNumberString() {

		getRidget().setNumberString("0815-12");
		assertEquals("0815-12", getLabel().getText());

	}

}
