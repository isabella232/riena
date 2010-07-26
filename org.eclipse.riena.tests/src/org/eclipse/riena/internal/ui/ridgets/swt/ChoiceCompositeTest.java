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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.swt.ChoiceComposite;

/**
 * Tests for the class {@link ChoiceComposite}.
 */
@UITestCase
public class ChoiceCompositeTest extends RienaTestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		final Display display = Display.getDefault();

		shell = new Shell(display);
		shell.setLayout(new FillLayout());

		shell.setSize(130, 100);
		shell.setLocation(0, 0);
		shell.open();
	}

	@Override
	protected void tearDown() throws Exception {
		shell.dispose();
		shell = null;

		super.tearDown();
	}

	// testing methods
	// ////////////////

	public void testConstructor() {
		try {
			new ChoiceComposite(null, SWT.NONE, false);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		final ChoiceComposite control1 = new ChoiceComposite(shell, SWT.NONE, false);
		assertFalse(control1.isMultipleSelection());

		final ChoiceComposite control2 = new ChoiceComposite(shell, SWT.NONE, true);
		assertTrue(control2.isMultipleSelection());
	}

	public void testSetOrientation() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);

		assertEquals(SWT.VERTICAL, control.getOrientation());

		control.setOrientation(SWT.HORIZONTAL);

		assertEquals(SWT.HORIZONTAL, control.getOrientation());
		assertTrue(control.getLayout() instanceof RowLayout);
		assertEquals(SWT.HORIZONTAL, ((RowLayout) control.getLayout()).type);

		control.setOrientation(SWT.VERTICAL);

		assertEquals(SWT.VERTICAL, control.getOrientation());
		assertTrue(control.getLayout() instanceof FillLayout);
		assertEquals(SWT.VERTICAL, ((FillLayout) control.getLayout()).type);

		try {
			control.setOrientation(SWT.NONE);
			fail();
		} catch (final RuntimeException rex) {
			// expected and unchanged
			assertEquals(SWT.VERTICAL, control.getOrientation());
		}
	}

	public void testSetForeground() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		final Button child1 = new Button(control, SWT.RADIO);

		final Color colorGreen = control.getDisplay().getSystemColor(SWT.COLOR_GREEN);

		assertTrue(!colorGreen.equals(control.getForeground()));

		control.setForeground(colorGreen);

		assertEquals(colorGreen, control.getForeground());
		assertEquals(colorGreen, child1.getForeground());
	}

	public void testSetBackground() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		final Button child1 = new Button(control, SWT.RADIO);

		final Color colorRed = control.getDisplay().getSystemColor(SWT.COLOR_RED);

		assertTrue(!colorRed.equals(control.getBackground()));

		control.setBackground(colorRed);

		assertEquals(colorRed, control.getBackground());
		assertEquals(colorRed, child1.getBackground());
	}

	public void testSetEnabled() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		final Button child1 = new Button(control, SWT.RADIO);

		assertTrue(control.getEnabled());
		assertTrue(child1.getEnabled());

		control.setEnabled(false);

		assertFalse(control.getEnabled());
		assertFalse(child1.getEnabled());

		control.setEnabled(true);

		assertTrue(control.getEnabled());
		assertTrue(child1.getEnabled());
	}

	/**
	 * As per Bug 317568.
	 */
	public void testSetMarginsVerticalOrientation() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		assertEquals(SWT.VERTICAL, control.getOrientation());

		assertEquals(0, control.getMargins().x);
		assertEquals(0, control.getMargins().y);
		assertEquals(0, ((FillLayout) control.getLayout()).marginHeight);
		assertEquals(0, ((FillLayout) control.getLayout()).marginWidth);

		control.setMargins(10, 20);

		assertEquals(10, control.getMargins().x);
		assertEquals(20, control.getMargins().y);
		assertEquals(10, ((FillLayout) control.getLayout()).marginHeight);
		assertEquals(20, ((FillLayout) control.getLayout()).marginWidth);
	}

	/**
	 * As per Bug 317568.
	 */
	public void testSetMarginsHorizontalOrientation() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		control.setOrientation(SWT.HORIZONTAL);
		assertEquals(SWT.HORIZONTAL, control.getOrientation());

		assertEquals(0, control.getMargins().x);
		assertEquals(0, control.getMargins().y);
		assertEquals(0, ((RowLayout) control.getLayout()).marginHeight);
		assertEquals(0, ((RowLayout) control.getLayout()).marginWidth);

		control.setMargins(10, 20);

		assertEquals(10, control.getMargins().x);
		assertEquals(20, control.getMargins().y);
		assertEquals(10, ((RowLayout) control.getLayout()).marginHeight);
		assertEquals(20, ((RowLayout) control.getLayout()).marginWidth);
	}

	/**
	 * As per Bug 317568.
	 */
	public void testSetMarginsNegativeValue() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);

		try {
			control.setMargins(-5, 10);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			control.setMargins(10, -5);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	/**
	 * As per Bug 317568.
	 */
	public void testSetSpacingVerticalOrientation() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		assertEquals(SWT.VERTICAL, control.getOrientation());

		assertEquals(3, control.getSpacing().x);
		assertEquals(0, control.getSpacing().y);
		assertEquals(0, ((FillLayout) control.getLayout()).spacing);

		control.setSpacing(0, 10);

		assertEquals(0, control.getSpacing().x);
		assertEquals(10, control.getSpacing().y);
		assertEquals(10, ((FillLayout) control.getLayout()).spacing);
	}

	/**
	 * As per Bug 317568.
	 */
	public void testSetSpacingHorizontalOrientation() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		control.setOrientation(SWT.HORIZONTAL);
		assertEquals(SWT.HORIZONTAL, control.getOrientation());

		assertEquals(3, control.getSpacing().x);
		assertEquals(0, control.getSpacing().y);
		assertEquals(3, ((RowLayout) control.getLayout()).spacing);

		control.setSpacing(10, 0);

		assertEquals(10, control.getSpacing().x);
		assertEquals(0, control.getSpacing().y);
		assertEquals(10, ((RowLayout) control.getLayout()).spacing);
	}

	/**
	 * As per Bug 317568.
	 */
	public void testSetSpacingNegativeValue() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);

		try {
			control.setSpacing(-5, 10);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			control.setSpacing(10, -5);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

}