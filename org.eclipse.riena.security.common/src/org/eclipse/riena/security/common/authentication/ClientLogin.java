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

import java.security.Principal;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.login.LoginException;

import org.eclipse.riena.core.service.ServiceInjector;
import org.eclipse.riena.internal.security.common.Activator;
import org.eclipse.riena.security.common.ISubjectHolderService;
import org.eclipse.riena.security.common.authentication.credentials.AbstractCredential;

/**
 * 
 */
public class ClientLogin {

	private IAuthenticationService authenticationService;
	private ISubjectHolderService subjectHolderService;
	private String loginContext;
	private AuthenticationTicket ticket;
	private Subject subject;

	public ClientLogin(String loginContext, Subject subject) {
		super();
		this.loginContext = loginContext;
		this.subject = subject;
		new ServiceInjector(Activator.getContext(), IAuthenticationService.ID, this, "bindAuthenticationService", "unbindAuthenticationService").start();
		new ServiceInjector(Activator.getContext(), ISubjectHolderService.ID, this, "bindSubjectHolderService", "unbindSubjectHolderService").start();
	}

	public void bindAuthenticationService(IAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public void unbindAuthenticationService(IAuthenticationService subHolderService) {
		if (this.authenticationService == authenticationService) {
			authenticationService = null;
		}
	}

	public void bindSubjectHolderService(ISubjectHolderService subjectHolderService) {
		this.subjectHolderService = subjectHolderService;
	}

	public void unbindSubjectHolderService(ISubjectHolderService subjectHolderService) {
		if (this.subjectHolderService == subjectHolderService) {
			this.subjectHolderService = null;
		}
	}

	public boolean login(Callback[] callbacks) throws LoginException {
		try {
			AbstractCredential[] creds = Callback2CredentialConverter.callbacks2Credentials(callbacks);
			ticket = authenticationService.login(loginContext, creds);
			return true;
		} catch (AuthenticationFailure failure) {
			throw new LoginException(failure.getMessage());
		}
	}

	public boolean commit() {
		Set<Principal> pSet = subject.getPrincipals();
		for (Principal p : ticket.getPrincipals()) {
			pSet.add(p);
		}
		subjectHolderService.fetchSubjectHolder().setSubject(subject);
		return true;
	}
}