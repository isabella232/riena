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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import junit.framework.TestCase;

import org.eclipse.riena.tests.collect.NonUITestCase;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Tests of the class <code>EmbeddedBorderRenderer</code>.
 */
@NonUITestCase
public class EmbeddedBorderRendererTest extends TestCase {

	/**
	 * Test of the method <code>computeInnerBounds(Rectangle)</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testComputeInnerBounds() throws Exception {

		Rectangle outerBounds = new Rectangle(12, 24, 36, 48);
		EmbeddedBorderRenderer renderer = new EmbeddedBorderRenderer();
		Rectangle innerBounds = renderer.computeInnerBounds(outerBounds);
		assertEquals(outerBounds.x + 2, innerBounds.x);
		assertEquals(outerBounds.y + 2, innerBounds.y);
		assertEquals(outerBounds.width - 4, innerBounds.width);
		assertEquals(outerBounds.height - 4, innerBounds.height);

	}

}
