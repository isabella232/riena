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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import junit.framework.TestCase;

import org.eclipse.riena.tests.collect.NonUITestCase;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * Tests of the class <code>ShellBorderRenderer</code>.
 */
@NonUITestCase
public class ShellBorderRendererTest extends TestCase {

	/**
	 * Test of the method <code>getCompelteBorderWidth</code>.
	 */
	public void testGetCompelteBorderWidth() {
		RienaDefaultLnf originalLnf = LnfManager.getLnf();
		try {
			MyLnf lnf = new MyLnf();
			LnfManager.setLnf(lnf);
			lnf.initialize();
			ShellBorderRenderer renderer = new ShellBorderRenderer();

			lnf.setPadding(20);
			int expected = 20 + renderer.getBorderWidth();
			assertEquals(expected, renderer.getCompleteBorderWidth());

			lnf.removePadding();
			expected = renderer.getBorderWidth();
			assertEquals(expected, renderer.getCompleteBorderWidth());

			lnf.setPadding(1.2);
			expected = renderer.getBorderWidth();
			assertEquals(expected, renderer.getCompleteBorderWidth());

			renderer.dispose();
		} finally {
			LnfManager.setLnf(originalLnf);
		}
	}

	/**
	 * Look and Feel where it is possible to change the setting for padding.
	 */
	private static class MyLnf extends RienaDefaultLnf {

		public void removePadding() {
			getSettingTable().remove(LnfKeyConstants.TITLELESS_SHELL_PADDING);
		}

		public void setPadding(Object padding) {
			getSettingTable().put(LnfKeyConstants.TITLELESS_SHELL_PADDING, padding);
		}

	}

}
