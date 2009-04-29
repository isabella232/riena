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
package org.eclipse.riena.security.common.authentication;

import org.eclipse.riena.security.common.authentication.credentials.AbstractCredential;

/**
 * The authentication interface.
 * 
 */
public interface IAuthenticationService {

	String WS_ID = "/AuthenticationService"; //$NON-NLS-1$

	/**
	 * Login with specified loginContext and credentials
	 * 
	 * @param loginContext
	 *            name of the equinox.security / JAAS security context
	 * @param credentials
	 *            list of credentials that need to be verified for a successful
	 *            login
	 * 
	 * @throws AuthenticationFailure
	 * 
	 * @return
	 */
	AuthenticationTicket login(String loginContext, AbstractCredential[] credentials) throws AuthenticationFailure;

	/**
	 * Logoff currently logged in user and invalidate current session
	 * 
	 * @throws AuthenticationFailure
	 */
	void logout() throws AuthenticationFailure;

}
