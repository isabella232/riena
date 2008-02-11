/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.service;

import junit.framework.TestCase;

@Deprecated
public class ServiceInjectorTest extends TestCase {

	public void testValidateMethod() {
		ServiceInjector si = new ServiceInjector();
		Object to = new Object() {
			public void method1() {

			}
		};
		assertTrue(si.methodExists(to, "method1"));
		assertFalse(si.methodExists(to, "method2"));
	}
}
