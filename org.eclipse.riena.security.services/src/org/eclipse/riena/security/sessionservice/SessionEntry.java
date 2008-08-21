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
package org.eclipse.riena.security.sessionservice;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.riena.security.common.session.Session;

/**
 * Entry of a session store
 * 
 */
public class SessionEntry {

	/** <code>session</code> */
	private Session session;
	/** <code>principals</code> */
	private Set<Principal> principalSet;

	private boolean valid;

	/**
	 * Creates a new instance of <code>SessionEntry</code>
	 * 
	 * @param session
	 * @param principal
	 * @param credential
	 */
	public SessionEntry(Session session, Principal[] principals) {
		this.session = session;
		principalSet = new HashSet<Principal>();
		for (Principal p : principals) {
			principalSet.add(p);
		}
		this.valid = true;
	}

	/**
	 * @return Returns the session.
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param session
	 *            The session to set.
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * @return Returns the userid.
	 */
	public Set<Principal> getPrincipals() {
		return principalSet;
	}

	/**
	 * @return true if valid
	 */
	public boolean getValid() {
		return valid;
	}

	/**
	 * @param valid
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
