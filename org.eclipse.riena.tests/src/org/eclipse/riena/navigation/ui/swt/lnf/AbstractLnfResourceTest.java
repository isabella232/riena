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
package org.eclipse.riena.navigation.ui.swt.lnf;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.swt.lnf.AbstractLnfResource;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Display;

/**
 * Tests of the class <code>AbstractLnfResource</code>.
 */
@NonUITestCase
public class AbstractLnfResourceTest extends TestCase {

	private MockLnfResource lnfResource;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		lnfResource = new MockLnfResource();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		lnfResource.dispose();
		lnfResource = null;
	}

	/**
	 * Tests the class <code>dispose()</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testDispose() throws Exception {

		Color color = (Color) lnfResource.getResource();
		assertNotNull(color);
		assertFalse(color.isDisposed());

		lnfResource.dispose();
		assertTrue(color.isDisposed());

	}

	/**
	 * Tests the class <code>GetResource()</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testGetResource() throws Exception {

		Color color1 = (Color) lnfResource.getResource();
		assertNotNull(color1);
		assertFalse(color1.isDisposed());

		lnfResource.dispose();

		Color color2 = (Color) lnfResource.getResource();
		assertNotNull(color2);
		assertFalse(color2.isDisposed());
		assertNotSame(color1, color2);

	}

	private static class MockLnfResource extends AbstractLnfResource {

		/**
		 * @see org.eclipse.riena.ui.swt.lnf.ILnfResource#createResource()
		 */
		public Resource createResource() {
			Color color = new Color(Display.getDefault(), 1, 1, 1);
			return color;
		}

	}

}
