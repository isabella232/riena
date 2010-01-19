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
package org.eclipse.riena.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.riena.internal.core.test.RienaTestCase;

/**
 * Test the {@code JavaSystemPropertiesResolver}.
 */
public class JavaSystemPropertiesResolverTest extends RienaTestCase {

	public void testResolveUserDir() throws CoreException {
		String expected = System.getProperty("user.dir");
		assertEquals(expected, VariableManagerUtil.substitute("${java.system.property:user.dir}"));
	}

	public void testResolveRienaHostName() throws CoreException, UnknownHostException {
		String expected = InetAddress.getLocalHost().getHostName();
		assertEquals(expected, VariableManagerUtil.substitute("${java.system.property:riena.host.name}"));
	}

	public void testResolveRienaHostAddress() throws CoreException, UnknownHostException {
		String expected = InetAddress.getLocalHost().getHostAddress();
		assertEquals(expected, VariableManagerUtil.substitute("${java.system.property:riena.host.address}"));
	}

}
