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
package org.eclipse.riena.core.logging;

import org.eclipse.riena.internal.core.logging.LoggerMill;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.NonUITestCase;

/**
 *
 */
@NonUITestCase
public class LoggerMillTest extends RienaTestCase {

	public void testUseRienaDefaultLogging() {
		System.clearProperty(LoggerMill.RIENA_DEFAULT_LOGGING);
		assertTrue(LoggerMill.useDefaultLogging());
		System.setProperty(LoggerMill.RIENA_DEFAULT_LOGGING, "false");
		assertFalse(LoggerMill.useDefaultLogging());
		System.setProperty(LoggerMill.RIENA_DEFAULT_LOGGING, "true");
		assertTrue(LoggerMill.useDefaultLogging());
	}
}
