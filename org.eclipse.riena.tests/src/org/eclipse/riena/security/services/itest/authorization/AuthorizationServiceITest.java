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
package org.eclipse.riena.security.services.itest.authorization;

import java.net.URL;
import java.security.AccessControlException;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.sample.app.common.model.Customer;
import org.eclipse.riena.sample.app.common.model.ICustomerSearch;
import org.eclipse.riena.security.authentication.callbackhandler.TestLocalCallbackHandler;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;
import org.eclipse.riena.security.common.session.ISessionHolderService;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.IntegrationTestCase;

import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.osgi.framework.ServiceReference;

/**
 * 
 */
@IntegrationTestCase
public class AuthorizationServiceITest extends RienaTestCase {

	private IRemoteServiceRegistration authenticationService;
	private IRemoteServiceRegistration authorizationService;
	private IRemoteServiceRegistration customerService;

	private static final String JAAS_CONFIG_FILE = "config/sample_jaas.config"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		startBundles("org\\.eclipse\\.riena.communication.core", null);
		startBundles("org\\.eclipse\\.riena.communication.factory.hessian", null);
		startBundles("org\\.eclipse\\.riena.communication.registry", null);
		authenticationService = Register.remoteProxy(IAuthenticationService.class).usingUrl(
				"http://localhost:8080/hessian/AuthenticationService").withProtocol("hessian").andStart(
				Activator.getDefault().getContext());
		authorizationService = Register.remoteProxy(IAuthorizationService.class).usingUrl(
				"http://localhost:8080/hessian/AuthorizationService").withProtocol("hessian").andStart(
				Activator.getDefault().getContext());
		customerService = Register.remoteProxy(ICustomerSearch.class).usingUrl(
				"http://localhost:8080/hessian/CustomerSearchWS").withProtocol("hessian").andStart(
				Activator.getDefault().getContext());
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		authenticationService.unregister();
		authorizationService.unregister();
		customerService.unregister();
	}

	public void testLoginWithUserWithRights() throws Exception {
		TestLocalCallbackHandler.setSuppliedCredentials("testuser", "testpass");

		URL configUrl = Activator.getDefault().getContext().getBundle().getEntry(JAAS_CONFIG_FILE);
		ILoginContext secureContext = LoginContextFactory.createContext("Remote", configUrl);

		secureContext.login();

		ServiceReference ref = getContext().getServiceReference(IAuthenticationService.class.getName());
		IAuthenticationService as = (IAuthenticationService) getContext().getService(ref);
		System.out.println("subject:" + secureContext.getSubject());
		System.out.println("login in sucessful");
		ISessionHolderService shs = (ISessionHolderService) getContext().getService(
				getContext().getServiceReference(ISessionHolderService.class.getName()));

		// call the customerService
		ICustomerSearch cs = (ICustomerSearch) getContext().getService(
				getContext().getServiceReference(ICustomerSearch.class.getName()));
		Customer cust = new Customer();
		cust.setLastName("Solo");
		cust.setFirstName("Han");
		cust.setCustomerNumber(1);
		Customer[] foundCustomers = cs.findCustomerWithPermission(cust);
		assertTrue(foundCustomers != null);
		assertTrue(foundCustomers.length > 0);
		assertTrue(foundCustomers[0].getLastName().equals("Solo"));

		as.logout();
		System.out.println("logoff sucessful");
	}

	public void testLoginWithUserWithoutRights() throws Exception {
		TestLocalCallbackHandler.setSuppliedCredentials("testuser1", "testpass2");

		URL configUrl = Activator.getDefault().getContext().getBundle().getEntry(JAAS_CONFIG_FILE);
		ILoginContext secureContext = LoginContextFactory.createContext("Remote", configUrl);

		secureContext.login();

		ServiceReference ref = getContext().getServiceReference(IAuthenticationService.class.getName());
		IAuthenticationService as = (IAuthenticationService) getContext().getService(ref);
		System.out.println("subject:" + secureContext.getSubject());
		System.out.println("login in sucessful");
		ISessionHolderService shs = (ISessionHolderService) getContext().getService(
				getContext().getServiceReference(ISessionHolderService.class.getName()));

		try {
			// call the customerService
			ICustomerSearch cs = (ICustomerSearch) getContext().getService(
					getContext().getServiceReference(ICustomerSearch.class.getName()));
			Customer cust = new Customer();
			cust.setLastName("Solo");
			cust.setFirstName("Han");
			cust.setCustomerNumber(1);
			cs.findCustomerWithPermission(cust);
			fail("findCustomerWithPermission must not work for testuser1 since it has to authorization");
			// assertTrue(foundCustomers != null);
			// assertTrue(foundCustomers.length > 0);
			// assertTrue(foundCustomers[0].getLastName().equals("Solo"));
		} catch (AccessControlException ex) {
			ok("expected exception");
		}

		as.logout();
		System.out.println("logoff sucessful");
	}
}
