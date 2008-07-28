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
package org.eclipse.riena.core.logging;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.internal.tests.Activator;
import org.osgi.service.log.LogService;

/**
 *
 */
public class EkkeTest extends TestCase {

	public void testIt() {
		Logger logger = new LogUtil(Activator.getDefault().getContext()).getLogger(null);
		logger.log(LogService.LOG_DEBUG, "Halloe");

		final Set<String> VALID_CODES = new HashSet<String>() {
			{
				add("XZ13s");
				add("AB21/X");
				add("YYLEX");
				add("AR2D");
			}
		};

		Set[] set = new Set[5];
		set[0] = new HashSet<String>();

	}
}
