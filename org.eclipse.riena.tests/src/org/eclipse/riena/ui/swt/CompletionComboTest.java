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
package org.eclipse.riena.ui.swt;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link CompletionCombo}.
 */
@UITestCase
public class CompletionComboTest extends TestCase {

	private Shell shell;
	private CompletionCombo combo;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		shell.setLayout(new RowLayout());
		combo = new CompletionCombo(shell, SWT.NONE);
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.disposeWidget(shell);
	}

	// testing methods
	//////////////////

	public void testSetBackground() {
		Color red = combo.getDisplay().getSystemColor(SWT.COLOR_RED);
		combo.setBackground(red);

		assertEquals(red, combo.getBackground());
		assertEquals(red, combo.getTextBackground());
		assertEquals(red, combo.getListBackground());
	}

	public void testSetTextBackground() {
		Color red = combo.getDisplay().getSystemColor(SWT.COLOR_RED);
		Color green = combo.getDisplay().getSystemColor(SWT.COLOR_GREEN);
		combo.setBackground(red);
		combo.setTextBackground(green);

		assertEquals(red, combo.getBackground());
		assertEquals(green, combo.getTextBackground());
		assertEquals(red, combo.getListBackground());
	}

	public void testSetListBackground() {
		Color red = combo.getDisplay().getSystemColor(SWT.COLOR_RED);
		Color green = combo.getDisplay().getSystemColor(SWT.COLOR_GREEN);
		Color blue = combo.getDisplay().getSystemColor(SWT.COLOR_GREEN);
		combo.setBackground(red);
		combo.setTextBackground(green);
		combo.setListBackground(blue);

		assertEquals(red, combo.getBackground());
		assertEquals(green, combo.getTextBackground());
		assertEquals(blue, combo.getListBackground());
	}

}
