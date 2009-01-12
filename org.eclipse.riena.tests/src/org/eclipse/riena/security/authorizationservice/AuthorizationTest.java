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

import javax.security.auth.Subject;

import org.eclipse.riena.internal.security.authorizationservice.AuthorizationService;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.security.common.SubjectAccessor;
import org.eclipse.riena.security.common.authentication.SimplePrincipal;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;
import org.eclipse.riena.security.simpleservices.authorizationservice.store.FilePermissionStore;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.NonUITestCase;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

@NonUITestCase
public class AuthorizationTest extends RienaTestCase {

	//	private ServiceRegistration fileStoreReg;
	private ServiceRegistration authorizationServiceReg;

	protected void setUp() throws Exception {
		super.setUp();
		// create FilePermissionStore which we inject into a local AuthorizationService
		InputStream inputStream = this.getClass().getResourceAsStream("policy-def-test.xml"); //$NON-NLS-1$
		FilePermissionStore store = new FilePermissionStore(inputStream);
		ServiceReference ref = getContext().getServiceReference(IAuthorizationService.class.getName());
		if (ref != null && ref.getBundle().getState() == Bundle.ACTIVE
				&& ref.getBundle() != Activator.getDefault().getBundle()) {
			ref.getBundle().stop();
		}
		// create and register a local AuthorizationService with a dummy permission store
		AuthorizationService authorizationService = new AuthorizationService();
		authorizationServiceReg = getContext().registerService(IAuthorizationService.class.getName(),
				authorizationService, null);
		// inject my test filestore
		authorizationService.bind(store);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		authorizationServiceReg.unregister();
	}

	public void testWithoutUser() {
		boolean result = new BusinessTestCase().hasPermission();
		assertFalse("BusinessTestCase must fail without user", result);
	}

	public void testWithValidUser() {
		Subject subject = new Subject();
		subject.getPrincipals().add(new SimplePrincipal("testuser"));
		SubjectAccessor.setSubject(subject);

		boolean result = new BusinessTestCase().hasPermission();

		assertTrue("BusinessTestCase must work with valid user", result);
	}

	public void testWithInvalidUser() {
		Subject subject = new Subject();
		subject.getPrincipals().add(new SimplePrincipal("anotheruser"));
		SubjectAccessor.setSubject(subject);

		boolean result = new BusinessTestCase().hasPermission();

		assertFalse("BusinessTestCase must fail with invalid user", result);
	}
}
