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
package org.eclipse.riena.security.sessionservice;

import java.security.Principal;

import org.eclipse.riena.security.common.session.Session;

/**
 * Store for sessions
 * 
 */
public interface ISessionStore {

	/** <code>SESSION_STORE_ID</code> */
	String SESSION_STORE_ID = "spirit.securityimplementation.server.SessionStore"; //$NON-NLS-1$

	/**
	 * reads the sessions for one principal from the store
	 * 
	 * @param principal
	 *            principal for which sessions should be read
	 * @return returns an array of session objects
	 */
	Session[] read(Principal principal);

	/**
	 * reads the session entry object for a session id
	 * 
	 * @param session
	 *            session id
	 * @return session entry
	 */
	SessionEntry read(Session session);

	/**
	 * writes or update a session entry
	 * 
	 * @param entry
	 *            session entry to write
	 */
	void write(SessionEntry entry);

	/**
	 * deletes the session entries for a session id
	 * 
	 * @param session
	 *            session id
	 */
	void delete(Session session);

	/**
	 * deletes the session entries for a principal
	 * 
	 * @param principal
	 */
	void delete(Principal principal);

}
