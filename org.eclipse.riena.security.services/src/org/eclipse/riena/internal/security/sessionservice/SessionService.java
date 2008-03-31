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
package org.eclipse.riena.internal.security.sessionservice;

import java.security.Principal;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.security.services.Activator;
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
		Inject.service(ISessionStore.ID).into(this).andStart(Activator.getContext());
		Inject.service(ISessionProvider.ID).into(this).andStart(Activator.getContext());
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

	/**
	 * @see de.compeople.spirit.security.server.session.ISessionService#
	 *      generateSession(java.security.Principal,
	 *      org.eclipse.riena.security.common.IPrincipalLocation,
	 *      de.compeople.spirit.security.base.authentication.Credentials)
	 * @pre principal!=null
	 */
	public Session generateSession(Principal[] principals) {
		// Assert.isNotNull( principal.getName(),"userid must not be null" );
		assert store != null : "store instance is null";

		Session session = sessionProvider.createSession(principals);

		if (session == null) {
			throw new SessionFailure("internal error, generating a session failed");
		}
		SessionEntry entry = new SessionEntry(session, principals);
		store.write(entry);
		return session;
	}

	/**
	 * @see de.compeople.spirit.security.server.session.ISessionService#registerSession(de.compeople.spirit.security.base.authentication.ISecurityTicket)
	 */
	// public boolean registerSession(ISecurityTicket ticket) {
	// SessionEntry entry = new SessionEntry(ticket.getSession(),
	// ticket.getAuthenticationContext().getPrincipal(), null);
	// store.write(entry);
	// return true;
	// }
	/**
	 * @see de.compeople.spirit.security.server.session.ISessionService#findPrincipals(org.eclipse.riena.security.common.session.ISession)
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

	/**
	 * @see de.compeople.spirit.security.server.session.ISessionService#isValidSession(Session)
	 */
	public boolean isValidSession(Session session) {
		SessionEntry entry = store.read(session);
		return entry != null && entry.getValid();
	}

	/**
	 * @see de.compeople.spirit.security.server.session.ISessionService#hasSession(Session)
	 */
	public boolean hasSession(Session session) {
		SessionEntry entry = store.read(session);
		return entry != null;
	}

	/**
	 * @see de.compeople.spirit.security.server.session.ISessionService#invalidateSession(Session)
	 */
	public void invalidateSession(Session session) {
		store.delete(session);
	}

}