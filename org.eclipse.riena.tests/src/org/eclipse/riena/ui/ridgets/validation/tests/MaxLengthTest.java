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
package org.eclipse.riena.ui.ridgets.validation.tests;

import junit.framework.TestCase;

import org.eclipse.riena.ui.ridgets.validation.MaxLength;

/**
 * Tests for the MaxLength rule.
 */
public class MaxLengthTest extends TestCase {

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testLength() throws Exception {

		MaxLength rule = new MaxLength(10);

		assertTrue(rule.validate("abcde").isOK());
		assertTrue(rule.validate("abcdeabcde").isOK());

		assertFalse(rule.validate("abcdeabcdefg").isOK());
	}

}
