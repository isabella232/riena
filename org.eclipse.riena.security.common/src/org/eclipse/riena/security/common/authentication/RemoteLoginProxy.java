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
package org.eclipse.riena.security.common.authentication;

import java.security.Principal;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.login.LoginException;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.security.common.Activator;
import org.eclipse.riena.security.common.ISubjectHolderService;
import org.eclipse.riena.security.common.authentication.credentials.AbstractCredential;

/**
 * 
 */
public class RemoteLoginProxy {

	private IAuthenticationService authenticationService;
	private ISubjectHolderService subjectHolderService;
	private final String loginContext;
	private AuthenticationTicket ticket;
	private Subject subject;

	public RemoteLoginProxy(String loginContext, Subject subject) {
		super();
		this.loginContext = loginContext;
		this.subject = subject;
		Inject.service(IAuthenticationService.class.getName()).useRanking().into(this).andStart(
				Activator.getDefault().getContext());
		Inject.service(ISubjectHolderService.class.getName()).useRanking().into(this).andStart(
				Activator.getDefault().getContext());
	}

	public void bind(IAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public void unbind(IAuthenticationService authenticationService) {
		if (this.authenticationService == authenticationService) {
			authenticationService = null;
		}
	}

	public void bind(ISubjectHolderService subjectHolderService) {
		this.subjectHolderService = subjectHolderService;
	}

	public void unbind(ISubjectHolderService subjectHolderService) {
		if (this.subjectHolderService == subjectHolderService) {
			this.subjectHolderService = null;
		}
	}

	public boolean login(Callback[] callbacks) throws LoginException {
		try {
			AbstractCredential[] creds = Callback2CredentialConverter.callbacks2Credentials(callbacks);
			if (authenticationService == null) {
				throw new AuthenticationFailure("no authentication service"); //$NON-NLS-1$
			}
			ticket = authenticationService.login(loginContext, creds);
			for (Principal principal : ticket.getPrincipals()) {
				subject.getPrincipals().add(principal);
			}
			subjectHolderService.fetchSubjectHolder().setSubject(subject);
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

	public boolean logout() throws LoginException {
		try {
			authenticationService.logout();
			subject.getPrincipals().clear(); // TODO only remove the principals for a particular session if possible
		} catch (AuthenticationFailure e) {
			throw new LoginException(e.getMessage());
		}
		return true;
	}
}
