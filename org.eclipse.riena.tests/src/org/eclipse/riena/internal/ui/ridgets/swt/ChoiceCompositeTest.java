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

import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.UITestCase;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests for the class {@link ChoiceComposite}.
 */
@UITestCase
public class ChoiceCompositeTest extends RienaTestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		Display display = Display.getDefault();

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
	}

	// testing methods
	// ////////////////

	public void testConstructor() {
		try {
			new ChoiceComposite(null, SWT.NONE, false);
			fail();
		} catch (RuntimeException rex) {
			ok();
		}

		ChoiceComposite control1 = new ChoiceComposite(shell, SWT.NONE, false);
		assertFalse(control1.isMultipleSelection());

		ChoiceComposite control2 = new ChoiceComposite(shell, SWT.NONE, true);
		assertTrue(control2.isMultipleSelection());
	}

	public void testSetOrientation() {
		ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);

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
		} catch (RuntimeException rex) {
			// expected and unchanged
			assertEquals(SWT.VERTICAL, control.getOrientation());
		}
	}

	public void testSetForeground() {
		ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		Button child1 = new Button(control, SWT.RADIO);

		Color colorGreen = control.getDisplay().getSystemColor(SWT.COLOR_GREEN);

		assertTrue(!colorGreen.equals(control.getForeground()));

		control.setForeground(colorGreen);

		assertEquals(colorGreen, control.getForeground());
		assertEquals(colorGreen, child1.getForeground());
	}

	public void testSetBackground() {
		ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		Button child1 = new Button(control, SWT.RADIO);

		Color colorRed = control.getDisplay().getSystemColor(SWT.COLOR_RED);

		assertTrue(!colorRed.equals(control.getBackground()));

		control.setBackground(colorRed);

		assertEquals(colorRed, control.getBackground());
		assertEquals(colorRed, child1.getBackground());
	}

	public void testSetEnabled() {
		ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		Button child1 = new Button(control, SWT.RADIO);

		assertTrue(control.getEnabled());
		assertTrue(child1.getEnabled());

		control.setEnabled(false);

		assertFalse(control.getEnabled());
		assertFalse(child1.getEnabled());

		control.setEnabled(true);

		assertTrue(control.getEnabled());
		assertTrue(child1.getEnabled());
	}

}