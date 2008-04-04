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
package org.eclipse.riena.security.services.itest.authentication;

import java.security.Principal;
import java.util.Arrays;

import javax.security.auth.Subject;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.RemoteServiceFactory;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.security.common.ISubjectHolderService;
import org.eclipse.riena.security.common.authentication.AuthenticationFailure;
import org.eclipse.riena.security.common.authentication.AuthenticationTicket;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.eclipse.riena.security.common.authentication.credentials.AbstractCredential;
import org.eclipse.riena.security.common.authentication.credentials.NameCredential;
import org.eclipse.riena.security.common.authentication.credentials.PasswordCredential;
import org.eclipse.riena.security.server.session.ISessionService;
import org.eclipse.riena.tests.RienaTestCase;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/**
 * Test client for authentication service.
 * 
 */
public class AuthenticationClientITest extends RienaTestCase {

	private final static Logger LOGGER = Activator.getDefault().getLogger(AuthenticationClientITest.class.getName());
	private IRemoteServiceRegistration sessionServiceRegistration;
	private IRemoteServiceRegistration authenticationServiceRegistration;

	protected void setUp() throws Exception {
		super.setUp();
		startBundles("org\\.eclipse\\.equinox\\.cm.*", null);
		startBundles("org\\.eclipse\\.equinox\\.log.*", null);
		startBundles("org\\.eclipse\\.riena.communication.core", null);
		startBundles("org\\.eclipse\\.riena.communication.factory.hessian", null);
		sessionServiceRegistration = new RemoteServiceFactory().createAndRegisterProxy(ISessionService.class,
				"http://localhost:8080/hessian/SessionService", "hessian", "org.eclipse.riena.sessionservice");
		authenticationServiceRegistration = new RemoteServiceFactory().createAndRegisterProxy(
				IAuthenticationService.class, "http://localhost:8080/hessian/AuthenticationService", "hessian",
				"org.eclipse.riena.authenticationservice");

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		sessionServiceRegistration.unregister();
		authenticationServiceRegistration.unregister();
	}

	/**
	 * org.eclipse.riena.tests the webservice call to authentication service
	 * with typeMapping
	 * 
	 * @throws Exception
	 */
	public void testLogin() throws Exception {
		trace("Looking up Authentication Service...: ");

		ServiceReference ref = getContext().getServiceReference(IAuthenticationService.class.getName());
		IAuthenticationService authenticationService = (IAuthenticationService) getContext().getService(ref);

		trace("Service looked up: " + authenticationService.getClass().getName());

		AbstractCredential[] creds = new AbstractCredential[2];
		NameCredential nc = new NameCredential("username: ", "xx");
		nc.setName("cca");
		creds[0] = nc;
		PasswordCredential pc = new PasswordCredential("password: ", false);
		pc.setPassword("christian".toCharArray());
		creds[1] = pc;
		trace("Add credential: " + Arrays.toString(creds));

		AuthenticationTicket ticket = authenticationService.login("Test", creds);

		trace("Return from login() - ticket: " + ticket);

		assertNotNull(ticket);
		assertNotNull(ticket.getSession());
		assertNotNull(ticket.getPrincipals());

		trace("Login successful - ticket: " + ticket);

		// sign off
		authenticationService.logout(ticket.getSession());

		trace("Logoff successful.");
	}

	public void testInvalidLogin() throws Exception {

		try {
			ServiceReference ref = getContext().getServiceReference(IAuthenticationService.class.getName());
			IAuthenticationService authenticationService = (IAuthenticationService) getContext().getService(ref);
			AbstractCredential[] creds = new AbstractCredential[2];
			NameCredential nc = new NameCredential("username: ", "xx");
			nc.setName("john");
			creds[0] = nc;
			PasswordCredential pc = new PasswordCredential("password: ", false);
			pc.setPassword("jane".toCharArray());
			creds[1] = pc;
			AuthenticationTicket ticket = authenticationService.login("Test", creds);
			throw new RuntimeException("exception expected");
		} catch (AuthenticationFailure e) {
			// ok();
		}
	}

	public void testSubjectLogin() throws Exception {
		ServiceReference ref = getContext().getServiceReference(IAuthenticationService.class.getName());
		IAuthenticationService authenticationService = (IAuthenticationService) getContext().getService(ref);

		trace("Service looked up: " + authenticationService.getClass().getName());

		AbstractCredential[] creds = new AbstractCredential[2];
		NameCredential nc = new NameCredential("username: ", "xx");
		nc.setName("cca");
		creds[0] = nc;
		PasswordCredential pc = new PasswordCredential("password: ", false);
		pc.setPassword("christian".toCharArray());
		creds[1] = pc;

		AuthenticationTicket ticket = authenticationService.login("Test", creds);

		trace("Return from login() - ticket: " + ticket);
		Subject subject = new Subject();
		for (Principal p : ticket.getPrincipals()) {
			subject.getPrincipals().add(p);
		}
		ServiceReference ref2 = getContext().getServiceReference(ISubjectHolderService.class.getName());
		ISubjectHolderService subHolderService = (ISubjectHolderService) getContext().getService(ref2);
		subHolderService.fetchSubjectHolder().setSubject(subject);

		assertTrue(subHolderService.fetchSubjectHolder().getSubject() == subject);
	}

	private void trace(String msg) {
		LOGGER.log(LogService.LOG_INFO, "|--->" + msg);
	}

}