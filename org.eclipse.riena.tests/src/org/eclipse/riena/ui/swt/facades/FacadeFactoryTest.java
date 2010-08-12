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
package org.eclipse.riena.ui.swt.facades;

import org.eclipse.swt.SWT;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the {@code FacadeFactory}.
 */
@NonUITestCase
public class FacadeFactoryTest extends RienaTestCase {

	public void testBadCast() {
		SWT.getPlatform();
		try {
			FacadeFactory.newFacade(TestBadCastFacade.class);
			fail();
		} catch (final RuntimeException e) {
			assertTrue(e.getCause() instanceof ClassCastException);
		}
	}

	public void testInstantiationBug() {
		try {
			FacadeFactory.newFacade(TestInstantiationBugFacade.class);
			fail();
		} catch (final RuntimeException e) {
			assertTrue(e.getCause() instanceof NullPointerException);
		}
	}

	public void testSomethingGood() {
		final GCFacade gcFacade = FacadeFactory.newFacade(GCFacade.class);
		assertTrue(gcFacade.getClass() != GCFacade.class);
		assertTrue(gcFacade instanceof GCFacade);
	}
}
