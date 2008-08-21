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
package org.eclipse.riena.security.authorizationservice;

import java.io.InputStream;
import java.security.PrivilegedAction;

import javax.security.auth.Subject;

import org.eclipse.riena.security.common.authentication.SimplePrincipal;
import org.eclipse.riena.security.common.authorization.RienaPolicy;
import org.eclipse.riena.security.simpleservices.authorizationservice.store.FilePermissionStore;
import org.eclipse.riena.tests.RienaTestCase;
import org.osgi.framework.ServiceRegistration;

public class AuthorizationTest extends RienaTestCase {

	private ServiceRegistration fileStoreReg;

	protected void setUp() throws Exception {
		super.setUp();
		// activate RienaPolicy
		// I need to add a FilePermissionStore so that it can read the
		// permissions for this test TODO
		InputStream inputStream = this.getClass().getResourceAsStream("policy-def-test.xml"); //$NON-NLS-1$
		FilePermissionStore store = new FilePermissionStore(inputStream);
		fileStoreReg = getContext().registerService(IPermissionStore.class.getName(), store, null);
		// ServiceReference ref =
		// getContext().getServiceReference(IAuthorizationService
		// .class.getName());
		// if (ref != null) {
		// ref.getBundle().stop();
		// }
		// authorizationServiceReg =
		// getContext().registerService(IAuthorizationService.ID,
		// new AuthorizationService(), null);

		RienaPolicy.init();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		fileStoreReg.unregister();
		// authorizationServiceReg.unregister();
	}

	public void testWithoutUser() {
		boolean result = new BusinessTestCase().hasPermission();
		assertFalse("BusinessTestCase must fail without user", result);
	}

	public void testWithValidUser() {
		Subject subject = new Subject();
		subject.getPrincipals().add(new SimplePrincipal("testuser"));

		Boolean result = (Boolean) Subject.doAsPrivileged(subject, new PrivilegedAction() {

			public Object run() {
				return Boolean.valueOf(new BusinessTestCase().hasPermission());
			}
		}, null);
		assertTrue("BusinessTestCase must work with valid user", result.equals(Boolean.TRUE));
	}

	public void testWithInvalidUser() {
		Subject subject = new Subject();
		subject.getPrincipals().add(new SimplePrincipal("anotheruser"));
		Boolean result = (Boolean) Subject.doAsPrivileged(subject, new PrivilegedAction() {

			public Object run() {
				return Boolean.valueOf(new BusinessTestCase().hasPermission());
			}
		}, null);
		assertTrue("BusinessTestCase must fail with invalid user", result.equals(Boolean.FALSE));
	}
}
