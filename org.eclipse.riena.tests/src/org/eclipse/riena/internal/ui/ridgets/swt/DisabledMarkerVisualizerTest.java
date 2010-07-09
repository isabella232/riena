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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;

/**
 * Tests for {@link DisabledMarkerVisualizer}.
 */
@UITestCase
public class DisabledMarkerVisualizerTest extends RienaTestCase {

	private Shell shell;
	private DisabledMarkerVisualizer visualizer;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		visualizer = new DisabledMarkerVisualizer(null);
	}

	@Override
	protected void tearDown() throws Exception {
		if (shell != null) {
			shell.dispose();
			shell = null;
		}
		super.tearDown();
	}

	// testing methods
	//////////////////

	public void testGetChildrenFromComposite() {
		final Composite parent = new Composite(shell, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Text(parent, SWT.NONE);

		final Control[] result = ReflectionUtils.invokeHidden(visualizer, "getChildren", parent);

		assertEquals(2, result.length);
		assertTrue(result[0] instanceof Label);
		assertTrue(result[1] instanceof Text);
	}

	/**
	 * As per Bug 318301.
	 */
	public void testGetChildrenFromCCombo() {
		final CCombo parent = new CCombo(shell, SWT.NONE);

		final Control[] result = ReflectionUtils.invokeHidden(visualizer, "getChildren", parent);

		assertEquals(2, result.length);
		assertTrue(result[0] instanceof Text);
		assertTrue(result[1] instanceof Button);
	}

}
