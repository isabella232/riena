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
package org.eclipse.riena.internal.communication.sample.pingpong.server;

import java.util.Hashtable;

import org.eclipse.riena.communication.core.publisher.RSDPublisherProperties;
import org.eclipse.riena.communication.sample.pingpong.common.IPingPong;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The Ping Pong sample shows how to publish an OSGi Services.
 * 
 * This sample Activator registers the IPingPong OSGi Service. This service
 * becomes published by the Riena Communication Hessian Publisher
 * 
 * @author Alexander Ziegler
 * 
 */
public class Activator implements BundleActivator {
	private ServiceRegistration pingPongRegHessian;

	// private ServiceRegistration pingPongRegXFire;

	public void start(BundleContext context) throws Exception {
		// create hessian service
		PingPong pingPong = new PingPong();
		Hashtable<String, String> properties = new Hashtable<String, String>(3);

		properties.put(RSDPublisherProperties.PROP_IS_REMOTE, "true");
		properties.put(RSDPublisherProperties.PROP_REMOTE_PROTOCOL, "hessian");
		properties.put(RSDPublisherProperties.PROP_REMOTE_PATH, "/PingPongWS");

		pingPongRegHessian = context.registerService(IPingPong.ID, pingPong, properties);

		// create xfire service
		// pingPong = new PingPong();
		// properties = new Hashtable<String, String>(3);
		//
		// properties.put(RSDPublisherProperties.PROP_IS_REMOTE, "true");
		// properties.put(RSDPublisherProperties.PROP_REMOTE_PROTOCOL, "xfire");
		// properties.put(RSDPublisherProperties.PROP_REMOTE_PATH,
		// "/PingPongXFireWS");
		//
		// pingPongRegXFire = context.registerService(IPingPong.ID, pingPong,
		// properties);

	}

	public void stop(BundleContext context) throws Exception {
		pingPongRegHessian.unregister();
		pingPongRegHessian = null;
		// pingPongRegXFire.unregister();
		// pingPongRegXFire = null;
	}

}
