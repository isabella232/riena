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

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.RemoteServiceFactory;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.eclipse.riena.security.common.session.ISessionHolderService;
import org.eclipse.riena.security.services.itest.MyCallbackHandler;
import org.eclipse.riena.tests.RienaTestCase;
import org.osgi.framework.ServiceReference;

/**
 * @author campo
 * 
 */
public class AuthenticationLoginModuleITest extends RienaTestCase {

	private IRemoteServiceRegistration authenticationService;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		startBundles("org\\.eclipse\\.riena.communication.core", null);
		startBundles("org\\.eclipse\\.riena.communication.factory.hessian", null);
		startBundles("org\\.eclipse\\.riena.communication.registry", null);
		authenticationService = new RemoteServiceFactory().createAndRegisterProxy(IAuthenticationService.class,
				"http://localhost:8080/hessian/AuthenticationService", "hessian", "org.eclipse.riena.authenticationservice");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		authenticationService.unregister();
	}

	public void testLogin() throws LoginException {
		LoginContext lc = new LoginContext("Test", new MyCallbackHandler("testuser", "testpass"));
		lc.login();
		System.out.println("subject:" + lc.getSubject());
		System.out.println("login in sucessful");
	}

	public void testRemoteLogin() throws LoginException {
		LoginContext lc = new LoginContext("Remote", new MyCallbackHandler("testuser", "testpass"));
		lc.login();
		ServiceReference ref = Activator.getContext().getServiceReference(IAuthenticationService.ID);
		IAuthenticationService as = (IAuthenticationService) Activator.getContext().getService(ref);
		System.out.println("subject:" + lc.getSubject());
		System.out.println("login in sucessful");
		ISessionHolderService shs = (ISessionHolderService) Activator.getContext().getService(
				Activator.getContext().getServiceReference(ISessionHolderService.ID));
		as.logout(shs.fetchSessionHolder().getSession());
		System.out.println("logoff sucessful");
	}
}
