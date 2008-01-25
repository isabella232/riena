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
package org.eclipse.riena.internal.sample.app.server;

import java.util.Hashtable;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.riena.communication.core.publisher.RSDPublisherProperties;
import org.eclipse.riena.sample.app.common.model.ICustomerSearch;
import org.eclipse.riena.sample.app.common.model.ICustomers;
import org.eclipse.riena.sample.app.common.model.IHelloWorldService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	private static final String REMOTE_PROTOCOL_HESSIAN = "hessian";

	// The shared instance
	private static Activator plugin;

	private Customers customers;
	private ServiceRegistration regCustomerSearch;
	private ServiceRegistration regCustomers;
	private HelloWorldService helloWorldService;
	private ServiceRegistration regHelloWorldService;

	/**
	 * The constructor
	 */
	public Activator() {

		super();

		customers = new Customers();
		helloWorldService = new HelloWorldService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {

		startCustomerSearch(context);
		startCustomers(context);
		startHelloWorldService(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {

		stopCustomerSearch();
		stopCustomers();
		stopHelloWorldService();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	private void startCustomerSearch(BundleContext context) {

		Hashtable<String, String> properties = new Hashtable<String, String>(3);

		properties.put(RSDPublisherProperties.PROP_IS_REMOTE, "true");
		properties.put(RSDPublisherProperties.PROP_REMOTE_PROTOCOL, REMOTE_PROTOCOL_HESSIAN);
		properties.put(RSDPublisherProperties.PROP_REMOTE_PATH, "/CustomerSearchWS");

		regCustomerSearch = context.registerService(ICustomerSearch.ID, customers, properties);
	}

	public void stopCustomerSearch() {

		regCustomerSearch.unregister();
		regCustomerSearch = null;
	}

	private void startCustomers(BundleContext context) {

		Hashtable<String, String> properties = new Hashtable<String, String>(3);

		properties.put(RSDPublisherProperties.PROP_IS_REMOTE, "true");
		properties.put(RSDPublisherProperties.PROP_REMOTE_PROTOCOL, REMOTE_PROTOCOL_HESSIAN);
		properties.put(RSDPublisherProperties.PROP_REMOTE_PATH, "/CustomersWS");

		regCustomers = context.registerService(ICustomers.ID, customers, properties);
	}

	public void stopCustomers() {

		regCustomers.unregister();
		regCustomers = null;
	}

	private void startHelloWorldService(BundleContext context) {

		Hashtable<String, String> properties = new Hashtable<String, String>(3);

		properties.put(RSDPublisherProperties.PROP_IS_REMOTE, "true");
		properties.put(RSDPublisherProperties.PROP_REMOTE_PROTOCOL, REMOTE_PROTOCOL_HESSIAN);
		properties.put(RSDPublisherProperties.PROP_REMOTE_PATH, "/HelloWorldServiceWS");

		regHelloWorldService = context.registerService(IHelloWorldService.ID, helloWorldService, properties);
	}

	public void stopHelloWorldService() {

		regHelloWorldService.unregister();
		regHelloWorldService = null;
	}
}
