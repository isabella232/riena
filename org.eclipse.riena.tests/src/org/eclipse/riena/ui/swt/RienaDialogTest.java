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

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link RienaDialog}.
 */
public class RienaDialogTest extends TestCase {

	private Shell shell;
	private MyRienaDialog dlg;

	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		dlg = new MyRienaDialog(shell);
	}

	@Override
	protected void tearDown() throws Exception {
		dlg = null;
		SwtUtilities.disposeWidget(shell);
	}

	/**
	 * Tests the <i>private</i> method {@code evaluateStyle()}.
	 */
	public void testEvaluateStyle() {

		dlg.setShellStyle(SWT.MAX | SWT.MIN | SWT.RESIZE | SWT.CLOSE);
		ReflectionUtils.invokeHidden(dlg, "evaluateStyle");
		assertTrue(dlg.isCloseable());
		assertTrue(dlg.isMaximizeable());
		assertTrue(dlg.isMinimizeable());
		assertTrue(dlg.isResizeable());

		dlg.setShellStyle(SWT.CLOSE);
		ReflectionUtils.invokeHidden(dlg, "evaluateStyle");
		assertTrue(dlg.isCloseable());
		assertFalse(dlg.isMaximizeable());
		assertFalse(dlg.isMinimizeable());
		assertFalse(dlg.isResizeable());

		dlg.setShellStyle(SWT.MAX);
		ReflectionUtils.invokeHidden(dlg, "evaluateStyle");
		assertFalse(dlg.isCloseable());
		assertTrue(dlg.isMaximizeable());
		assertFalse(dlg.isMinimizeable());
		assertFalse(dlg.isResizeable());

		dlg.setShellStyle(SWT.MIN);
		ReflectionUtils.invokeHidden(dlg, "evaluateStyle");
		assertFalse(dlg.isCloseable());
		assertFalse(dlg.isMaximizeable());
		assertTrue(dlg.isMinimizeable());
		assertFalse(dlg.isResizeable());

		dlg.setShellStyle(SWT.RESIZE);
		ReflectionUtils.invokeHidden(dlg, "evaluateStyle");
		assertFalse(dlg.isCloseable());
		assertFalse(dlg.isMaximizeable());
		assertFalse(dlg.isMinimizeable());
		assertTrue(dlg.isResizeable());

	}

	/**
	 * Tests the <i>private</i> method {@code updateDialogStyle()}.
	 */
	public void testUpdateDialogStyle() {

		dlg.setHideOsBorder(true);
		dlg.setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
		ReflectionUtils.invokeHidden(dlg, "updateDialogStyle");
		int style = dlg.getShellStyle();
		assertFalse((style & SWT.DIALOG_TRIM) == SWT.DIALOG_TRIM);
		assertTrue((style & SWT.NO_TRIM) == SWT.NO_TRIM);
		assertTrue((style & SWT.RESIZE) == SWT.RESIZE);
		assertTrue((style & SWT.APPLICATION_MODAL) == SWT.APPLICATION_MODAL);

		dlg.setHideOsBorder(false);
		dlg.setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
		ReflectionUtils.invokeHidden(dlg, "updateDialogStyle");
		style = dlg.getShellStyle();
		assertTrue((style & SWT.DIALOG_TRIM) == SWT.DIALOG_TRIM);
		assertTrue((style & SWT.RESIZE) == SWT.RESIZE);
		assertTrue((style & SWT.APPLICATION_MODAL) == SWT.APPLICATION_MODAL);

	}

	/**
	 * This class changes the visibility of some method for testing.
	 */
	private class MyRienaDialog extends RienaDialog {

		public MyRienaDialog(Shell shell) {
			super(shell);
		}

		@Override
		public void setShellStyle(int newShellStyle) {
			super.setShellStyle(newShellStyle);
		}

		@Override
		public int getShellStyle() {
			return super.getShellStyle();
		}

	}

}
