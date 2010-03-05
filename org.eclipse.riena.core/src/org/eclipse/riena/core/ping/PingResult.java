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
package org.eclipse.riena.core.ping;

/**
 * The class containing the result of a ping.
 */
public class PingResult {

	private final PingFingerprint fingerprint;

	/**
	 * Create the ping result.
	 * 
	 * @param fingerprint
	 */
	public PingResult(final PingFingerprint fingerprint) {
		this.fingerprint = fingerprint;
	}

	/**
	 * Return the fingerprint of the visited {@link IPingable IPingable}.
	 * 
	 * @return the fingerprint of the visited {@link IPingable IPingable}.
	 */
	public PingFingerprint getPingFingerprint() {
		return fingerprint;
	}

}
