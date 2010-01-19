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
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

/**
 * A dynamic variable resolver for Java system properties. <br>
 * Additionally this resolver resolves currently also two <i>synthetic</i>
 * properties:
 * <ul>
 * <li>riena.host.name - the value of {@code
 * InetAddress.getLocalHost().getHostName()}</li>
 * <li>riena.host.address - the value of {@code
 * InetAddress.getLocalHost().getHostAddress()}</li>
 * </ul>
 * 
 * @since 2.0
 */
public class JavaSystemPropertiesResolver implements IDynamicVariableResolver {

	/*
	 * {@inheritDoc}
	 */
	public String resolveValue(final IDynamicVariable variable, final String argument) throws CoreException {
		if (argument == null) {
			return null;
		}
		final String value = lookForSyntheticProperties(argument);
		if (value != null) {
			return value;
		}
		return System.getProperty(argument);
	}

	/**
	 * @param argument
	 * @return
	 */
	private String lookForSyntheticProperties(final String argument) {
		if (argument.equals(HOST_ADDRESS)) {
			return getHostAddress();
		} else if (argument.equals(HOST_NAME)) {
			return getHostName();
		}
		return null;
	}

	/**
	 * Prefix for "riena" variables.
	 */
	private static final String RIENA = "riena."; //$NON-NLS-1$

	/**
	 * ´Synthetic´ property that retrieves the hosts ip address, i.e. {@code
	 * InetAddress.getLocalHost().getHostAddress()}
	 */
	private static final String HOST_ADDRESS = RIENA + "host.address"; //$NON-NLS-1$

	/**
	 * ´Synthetic´ property that retrieves the host name, i.e. {@code
	 * InetAddress.getLocalHost().getHostName()}
	 */
	private static final String HOST_NAME = RIENA + "host.name"; //$NON-NLS-1$

	private static final String UNKNOWN = "?"; //$NON-NLS-1$

	private String hostName;

	private String getHostName() {
		if (hostName == null) {
			try {
				hostName = InetAddress.getLocalHost().getHostName();
			} catch (final UnknownHostException e) {
				hostName = UNKNOWN;
			}
		}
		return hostName;
	}

	private String hostAddress;

	private String getHostAddress() {
		if (hostAddress == null) {
			try {
				hostAddress = InetAddress.getLocalHost().getHostAddress();
			} catch (final UnknownHostException e) {
				hostAddress = UNKNOWN;
			}
		}
		return hostAddress;
	}

}
