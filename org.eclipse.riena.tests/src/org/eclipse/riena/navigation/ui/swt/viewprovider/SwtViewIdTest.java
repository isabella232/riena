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
package org.eclipse.riena.navigation.ui.swt.viewprovider;

import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewId;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.NonUITestCase;

/**
 * Test of the class <code>SwtViewId</code>.
 */
@NonUITestCase
public class SwtViewIdTest extends RienaTestCase {

	/**
	 * Tests the constructor <code>SwtViewId(String,String)</code>
	 */
	public void testSwtViewIdStringString() {

		SwtViewId viewId = new SwtViewId("a", "b");
		assertEquals("a", viewId.getId());
		assertEquals("b", viewId.getSecondary());

	}

	/**
	 * Tests the constructor <code>SwtViewId(String)</code>
	 */
	public void testSwtViewIdString() {

		SwtViewId viewId = new SwtViewId("a:b");
		assertEquals("a", viewId.getId());
		assertEquals("b", viewId.getSecondary());

		try {
			new SwtViewId(null);
			fail("Exception expected");
		} catch (RuntimeException e) {
			ok("Exception expected");
		}

		try {
			new SwtViewId("ab");
			fail("Exception expected");
		} catch (RuntimeException e) {
			ok("Exception expected");
		}

		try {
			new SwtViewId(null);
			fail("Exception expected");
		} catch (RuntimeException e) {
			ok("Exception expected");
		}

	}

	/**
	 * Tests the method <code>getCompound()</code>.
	 */
	public void testGetCompoundId() {

		SwtViewId viewId = new SwtViewId("a", "b");
		assertEquals("a:b", viewId.getCompoundId());

	}

}
