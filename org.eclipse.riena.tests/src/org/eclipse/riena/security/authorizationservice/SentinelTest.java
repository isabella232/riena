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
package org.eclipse.riena.security.authorizationservice;

import java.io.InputStream;

import javax.security.auth.Subject;

import org.eclipse.riena.internal.security.authorizationservice.AuthorizationService;
import org.eclipse.riena.security.common.ISubjectHolderService;
import org.eclipse.riena.security.common.authentication.SimplePrincipal;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;
import org.eclipse.riena.security.common.authorization.Sentinel;
import org.eclipse.riena.security.simpleservices.authorizationservice.store.FilePermissionStore;
import org.eclipse.riena.tests.RienaTestCase;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * Tests the Sentinal which means we are testing for permissions without
 * actually activating java security. Permissions are checked by the Sentinel
 * instead
 */
public class SentinelTest extends RienaTestCase {
	private ServiceRegistration fileStoreReg;
	private ServiceRegistration authorizationServiceReg;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		// activate RienaPolicy
		// I need to add a FilePermissionStore so that it can read the
		// permissions for this test TODO
		InputStream inputStream = this.getClass().getResourceAsStream("policy-def-test.xml"); //$NON-NLS-1$
		FilePermissionStore store = new FilePermissionStore(inputStream);
		fileStoreReg = getContext().registerService(IPermissionStore.ID, store, null);
		ServiceReference ref = getContext().getServiceReference(IAuthorizationService.ID);
		if (ref != null) {
			ref.getBundle().stop();
		}
		authorizationServiceReg = getContext().registerService(IAuthorizationService.ID, new AuthorizationService(),
				null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		fileStoreReg.unregister();
		authorizationServiceReg.unregister();
	}

	public void testWithoutUser() {
		boolean result = Sentinel.checkAccess(new TestcasePermission("testPerm"));
		assertFalse("no permission if there is no subject", result);
	}

	public void testValidUser() {
		Subject subject = new Subject();
		subject.getPrincipals().add(new SimplePrincipal("testuser"));
		ISubjectHolderService subjectHolderService = (ISubjectHolderService) getContext().getService(
				getContext().getServiceReference(ISubjectHolderService.ID));
		subjectHolderService.fetchSubjectHolder().setSubject(subject);

		boolean result = Sentinel.checkAccess(new TestcasePermission("testPerm"));
		assertTrue("has permission since valid subject", result);
	}

	public void testValidUserMissingPermissions() {
		Subject subject = new Subject();
		subject.getPrincipals().add(new SimplePrincipal("anotheruser"));
		ISubjectHolderService subjectHolderService = (ISubjectHolderService) getContext().getService(
				getContext().getServiceReference(ISubjectHolderService.ID));
		subjectHolderService.fetchSubjectHolder().setSubject(subject);

		boolean result = Sentinel.checkAccess(new TestcasePermission("testPerm"));
		assertFalse("has no permission since subject has no permission", result);

	}
}
