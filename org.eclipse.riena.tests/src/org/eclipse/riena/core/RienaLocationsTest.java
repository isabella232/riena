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
package org.eclipse.riena.core;

import java.io.File;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Platform;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.tests.Activator;

/**
 * Test the {@code RienaLocations} class.
 */
@NonUITestCase
public class RienaLocationsTest extends TestCase {

	public void testGetDataArea() {
		File dataArea = RienaLocations.getDataArea();
		assertTrue(dataArea.isDirectory());
		File expected = new File(Platform.getInstanceLocation().getURL().getFile(), RienaLocations.RIENA_NAME);
		assertEquals(expected, dataArea);
	}

	public void testGetDataAreaForBundle() {
		File dataArea = RienaLocations.getDataArea(Activator.getDefault().getBundle());
		assertTrue(dataArea.isDirectory());
		assertEquals(new File(new File(Platform.getInstanceLocation().getURL().getFile(), RienaLocations.RIENA_NAME),
				Activator.getDefault().getBundle().getSymbolicName()), dataArea);
	}
}
