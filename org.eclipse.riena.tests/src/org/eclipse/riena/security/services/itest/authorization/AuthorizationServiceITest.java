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
package org.eclipse.riena.security.services.itest.authorization;

import java.security.AccessControlException;

import javax.security.auth.login.LoginContext;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.RemoteServiceFactory;
import org.eclipse.riena.sample.app.common.model.Customer;
import org.eclipse.riena.sample.app.common.model.ICustomerSearch;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;
import org.eclipse.riena.security.common.session.ISessionHolderService;
import org.eclipse.riena.security.services.itest.MyCallbackHandler;
import org.eclipse.riena.tests.RienaTestCase;
import org.osgi.framework.ServiceReference;

/**
 * 
 */
public class AuthorizationServiceITest extends RienaTestCase {

	private IRemoteServiceRegistration authenticationService;
	private IRemoteServiceRegistration authorizationService;
	private IRemoteServiceRegistration customerService;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		startBundles("org\\.eclipse\\.riena.communication.core", null);
		startBundles("org\\.eclipse\\.riena.communication.factory.hessian", null);
		startBundles("org\\.eclipse\\.riena.communication.registry", null);
		authenticationService = new RemoteServiceFactory().createAndRegisterProxy(IAuthenticationService.class,
				"http://localhost:8080/hessian/AuthenticationService", "hessian",
				"org.eclipse.riena.authenticationservice");
		authorizationService = new RemoteServiceFactory().createAndRegisterProxy(IAuthorizationService.class,
				"http://localhost:8080/hessian/AuthorizationService", "hessian",
				"org.eclipse.riena.authorizationservice");
		customerService = new RemoteServiceFactory().createAndRegisterProxy(ICustomerSearch.class,
				"http://localhost:8080/hessian/CustomerSearchWS", "hessian", "org.eclipse.riena.customersearchservice");
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
		LoginContext lc = new LoginContext("Remote", new MyCallbackHandler("testuser", "testpass"));
		lc.login();
		ServiceReference ref = getContext().getServiceReference(IAuthenticationService.class.getName());
		IAuthenticationService as = (IAuthenticationService) getContext().getService(ref);
		System.out.println("subject:" + lc.getSubject());
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
		Customer[] foundCustomers = cs.findCustomer(cust);
		assertTrue(foundCustomers != null);
		assertTrue(foundCustomers.length > 0);
		assertTrue(foundCustomers[0].getLastName().equals("Solo"));

		as.logout(shs.fetchSessionHolder().getSession());
		System.out.println("logoff sucessful");
	}

	public void testLoginWithUserWithoutRights() throws Exception {
		LoginContext lc = new LoginContext("Remote", new MyCallbackHandler("testuser2", "testpass2"));
		lc.login();
		ServiceReference ref = getContext().getServiceReference(IAuthenticationService.class.getName());
		IAuthenticationService as = (IAuthenticationService) getContext().getService(ref);
		System.out.println("subject:" + lc.getSubject());
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
			cs.findCustomer(cust);
			fail("findCustomer must not work for testuser2 since it has to authorization");
			// assertTrue(foundCustomers != null);
			// assertTrue(foundCustomers.length > 0);
			// assertTrue(foundCustomers[0].getLastName().equals("Solo"));
		} catch (AccessControlException ex) {
			// expected exception
		}

		as.logout(shs.fetchSessionHolder().getSession());
		System.out.println("logoff sucessful");
	}
}
