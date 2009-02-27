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
package org.eclipse.riena.internal.security.sessionservice;

import java.security.Principal;

import org.eclipse.riena.security.common.session.Session;
import org.eclipse.riena.security.server.session.ISessionService;
import org.eclipse.riena.security.server.session.SessionFailure;
import org.eclipse.riena.security.sessionservice.ISessionProvider;
import org.eclipse.riena.security.sessionservice.ISessionStore;
import org.eclipse.riena.security.sessionservice.SessionEntry;

/**
 * Class to maintain global sessions for user. Sessions are identified by id and
 * associated with a user (Principal).
 * 
 */
public class SessionService implements ISessionService {

	private ISessionStore store;
	private ISessionProvider sessionProvider;

	/**
	 * constructor
	 */
	public SessionService() {
		super();
	}

	public void bind(ISessionStore store) {
		this.store = store;
	}

	public void unbind(ISessionStore store) {
		if (this.store == store) {
			this.store = null;
		}
	}

	public void bind(ISessionProvider provider) {
		this.sessionProvider = provider;
	}

	public void unbind(ISessionProvider provider) {
		if (this.sessionProvider == provider) {
			this.sessionProvider = null;
		}
	}

	/**
	 * create a local session controller with a specific store
	 * 
	 * @param store
	 */
	public SessionService(ISessionStore store, ISessionProvider sessionProvider) {
		super();
		this.store = store;
		this.sessionProvider = sessionProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.security.server.session.ISessionService#generateSession
	 * (java.security.Principal[])
	 */
	public Session generateSession(Principal[] principals) {
		// Assert.isNotNull( principal.getName(),"userid must not be null" );
		assert store != null : "store instance is null"; //$NON-NLS-1$

		Session session = sessionProvider.createSession(principals);

		if (session == null) {
			throw new SessionFailure("internal error, generating a session failed"); //$NON-NLS-1$
		}
		SessionEntry entry = new SessionEntry(session, principals);
		store.write(entry);
		return session;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.security.server.session.ISessionService#findPrincipals
	 * (org.eclipse.riena.security.common.session.Session)
	 * 
	 * @pre session!=null
	 */
	public Principal[] findPrincipals(Session session) {
		// Assert.isNotNull( session,"session is not null");
		SessionEntry entry = store.read(session);
		if (entry == null) {
			return null;
		}
		return entry.getPrincipals().toArray(new Principal[entry.getPrincipals().size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.security.server.session.ISessionService#isValidSession
	 * (org.eclipse.riena.security.common.session.Session)
	 */
	public boolean isValidSession(Session session) {
		SessionEntry entry = store.read(session);
		return entry != null && entry.getValid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.security.server.session.ISessionService#hasSession(
	 * org.eclipse.riena.security.common.session.Session)
	 */
	public boolean hasSession(Session session) {
		SessionEntry entry = store.read(session);
		return entry != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.security.server.session.ISessionService#invalidateSession
	 * (org.eclipse.riena.security.common.session.Session)
	 */
	public void invalidateSession(Session session) {
		store.delete(session);
	}

}
