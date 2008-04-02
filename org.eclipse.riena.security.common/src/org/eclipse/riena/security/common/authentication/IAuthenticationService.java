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
package org.eclipse.riena.security.common.authentication;

import org.eclipse.riena.security.common.authentication.credentials.AbstractCredential;
import org.eclipse.riena.security.common.session.Session;

/**
 * The authentication interface.
 * 
 */
public interface IAuthenticationService {

	String WS_ID = "/AuthenticationService";

	/**
	 * login operation.
	 * 
	 * @param principalID
	 *            principal ID
	 * @param credentials
	 *            credential container
	 * @return a security ticket
	 * @throws AuthenticationException
	 *             thrown in cases of application errors
	 * @throws AuthenticationFailure
	 *             thrown in cases of fatal error occurrences
	 * @pre principalID != null
	 * @pre credentials != null
	 */
	// ISecurityTicket login(PrincipalID principalID, Credentials credentials)
	// throws AuthenticationException, AuthenticationFailure;
	/**
	 * Login as a proxy on behalf of someone else. The proxy login requires that
	 * a user has already been logged in, to become a proxy for another user.
	 * 
	 * @param proxyTicket
	 *            the security ticket returned from a previous login()
	 * @param principalID
	 *            principal ID of the represented user
	 * @param principalCredentials
	 *            credential container of the represented user
	 * @return a security ticket
	 * @throws AuthenticationException
	 *             thrown in cases of application errors
	 * @throws AuthenticationFailure
	 *             thrown in cases of fatal error occurrences
	 * @pre proxyTicket != null
	 * @pre principalID != null
	 * @pre principalCredentials != null
	 */
	// ISecurityTicket proxyLogin(ISecurityTicket proxyTicket, PrincipalID
	// principalID, Credentials principalCredentials) throws
	// AuthenticationException,
	// AuthenticationFailure;
	/**
	 * change the value credential(s).
	 * 
	 * @param ticket
	 *            the principal object reference
	 * @param oldCredentials
	 *            the old credentials container
	 * @param newCredentials
	 *            the new credentials container
	 * @throws AuthenticationFailure
	 *             thrown in cases of fatal error occurrences
	 * @pre ticket != null
	 * @pre ticket.getSession() != null
	 * @pre oldCredentials != null
	 * @pre newCredentials != null
	 */
	// void changeCredentials(ISecurityTicket ticket, Credentials
	// oldCredentials, Credentials newCredentials) throws AuthenticationFailure;
	/**
	 * logoff operation.
	 * 
	 * @param loginContext
	 *            TODO
	 * @param ticket
	 *            a security ticket
	 * @throws AuthenticationFailure
	 *             thrown in cases of fatal error occurrences
	 * @pre ticket != null
	 * @pre ticket.getSession() != null
	 */
	// void logoff(ISecurityTicket ticket) throws AuthenticationFailure;
	AuthenticationTicket login(String loginContext, AbstractCredential[] credentials);

	void logout(Session session);

}
