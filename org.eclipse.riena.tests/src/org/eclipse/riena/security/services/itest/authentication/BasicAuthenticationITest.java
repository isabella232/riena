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
package org.eclipse.riena.security.services.itest.authentication;

import javax.security.auth.Subject;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.RemoteFailure;
import org.eclipse.riena.communication.core.factory.RemoteServiceFactory;
import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.security.common.BasicAuthenticationCallHook;
import org.eclipse.riena.security.common.ISubjectHolderService;
import org.eclipse.riena.security.common.authentication.SimplePrincipal;
import org.eclipse.riena.security.server.session.ISessionService;
import org.eclipse.riena.tests.RienaTestCase;
import org.osgi.framework.ServiceRegistration;

/**
 * 
 */
public class BasicAuthenticationITest extends RienaTestCase {

	private IRemoteServiceRegistration sessionServiceRegistration;

	private final static String TESTURL = "http://localhost:8080/junit/protected";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		startBundles("org\\.eclipse\\.equinox\\.cm.*", null); //$NON-NLS-1$
		startBundles("org\\.eclipse\\.equinox\\.log.*", null); //$NON-NLS-1$
		startBundles("org\\.eclipse\\.riena.communication.core", null); //$NON-NLS-1$
		startBundles("org\\.eclipse\\.riena.communication.factory.hessian", null); //$NON-NLS-1$

		sessionServiceRegistration = new RemoteServiceFactory().createAndRegisterProxy(ISessionService.class, TESTURL,
				"hessian", null); //$NON-NLS-1$
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		sessionServiceRegistration.unregister();
	}

	/**
	 * nomen est omen
	 */
	public void testNoCallHook() {
		try {
			ISessionService sessionService = (ISessionService) Activator.getContext().getService(
					Activator.getContext().getServiceReference(ISessionService.class.getName()));

			sessionService.isValidSession(null);
			fail("RemoteFailure HTTP=401 expected"); //$NON-NLS-1$
		} catch (RemoteFailure e) {
			assertTrue(e.getCause().getCause().getMessage().contains("401")); //$NON-NLS-1$
		}
	}

	/**
	 * nomen est omen
	 */
	public void testWithCallHookNoAuthorization() {
		try {
			ISessionService sessionService = (ISessionService) Activator.getContext().getService(
					Activator.getContext().getServiceReference(ISessionService.class.getName()));

			ServiceRegistration serviceReg = Activator.getContext().registerService(ICallHook.class.getName(),
					new BasicAuthenticationCallHook(), null);

			sessionService.isValidSession(null);

			serviceReg.unregister();
			fail("RemoteFailure HTTP=401 expected"); //$NON-NLS-1$
		} catch (RemoteFailure e) {
			assertTrue(e.getCause().getCause().getMessage().contains("401")); //$NON-NLS-1$
		}

	}

	/**
	 * nomen est omen
	 */
	public void testWithCallHookWithInvalidAuthorization() {
		try {
			ISessionService sessionService = (ISessionService) Activator.getContext().getService(
					Activator.getContext().getServiceReference(ISessionService.class.getName()));

			ServiceRegistration serviceReg = Activator.getContext().registerService(ICallHook.class.getName(),
					new BasicAuthenticationCallHook(), null);

			ISubjectHolderService subjectHolderService = (ISubjectHolderService) Activator.getContext().getService(
					Activator.getContext().getServiceReference(ISubjectHolderService.class.getName()));

			Subject subject = new Subject();
			subject.getPrincipals().add(new SimplePrincipal("christian"));
			subject.getPrivateCredentials().add("password");
			subjectHolderService.fetchSubjectHolder().setSubject(subject);

			sessionService.isValidSession(null);

			serviceReg.unregister();
			fail("RemoteFailure HTTP=401 expected"); //$NON-NLS-1$
		} catch (RemoteFailure e) {
			assertTrue(e.getCause().getCause().getMessage().contains("401")); //$NON-NLS-1$
		}

	}

	/**
	 * nomen est omen
	 */
	public void testWithCallHookWithValidAuthorization() {
		try {
			ISessionService sessionService = (ISessionService) Activator.getContext().getService(
					Activator.getContext().getServiceReference(ISessionService.class.getName()));

			ServiceRegistration serviceReg = Activator.getContext().registerService(ICallHook.class.getName(),
					new BasicAuthenticationCallHook(), null);

			ISubjectHolderService subjectHolderService = (ISubjectHolderService) Activator.getContext().getService(
					Activator.getContext().getServiceReference(ISubjectHolderService.class.getName()));

			Subject subject = new Subject();
			subject.getPrincipals().add(new SimplePrincipal("scp")); //$NON-NLS-1$
			subject.getPrivateCredentials().add("scptestpassword"); //$NON-NLS-1$
			subjectHolderService.fetchSubjectHolder().setSubject(subject);

			sessionService.isValidSession(null);

			serviceReg.unregister();
			fail("RemoteFailure with Protocol Error expected"); //$NON-NLS-1$
			// ok()
		} catch (RemoteFailure e) {
			assertFalse(e.getCause().getCause().getMessage().contains("401")); //$NON-NLS-1$
			assertTrue(e.getCause().getCause().getMessage().contains("expected boolean")); //$NON-NLS-1$
		}

	}
}
