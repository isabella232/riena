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
import org.eclipse.riena.ui.swt.lnf.renderer.EmbeddedTitlebarRenderer;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link EmbeddedTitleBar}.
 */
public class EmbeddedTitleBarTest extends TestCase {

	private Shell shell;
	private EmbeddedTitleBar titleBar;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		titleBar = new EmbeddedTitleBar(shell, SWT.NONE);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		SwtUtilities.disposeWidget(titleBar);
		SwtUtilities.disposeWidget(shell);
	}

	/**
	 * Tests the method {@code addListeners}.
	 */
	public void testAddListeners() {
		// method addListeners was already called while creating instance
		assertEquals(1, titleBar.getListeners(SWT.Paint).length);
	}

	/**
	 * Tests the method {@code getLnfTitlebarRenderer}.
	 */
	public void testGetLnfTitlebarRenderer() {

		EmbeddedTitlebarRenderer renderer = ReflectionUtils.invokeHidden(titleBar, "getLnfTitlebarRenderer",
				new Object[] {});
		assertNotNull(renderer);

	}

}
