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
package org.eclipse.riena.internal.communication.sample.pingpong.client;

import org.eclipse.riena.communication.sample.pingpong.common.IPingPong;
import org.eclipse.riena.communication.sample.pingpong.common.Ping;
import org.eclipse.riena.communication.sample.pingpong.common.Pong;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 * The Ping Pong sample shows how to find a "remote" OSGi Services.
 * 
 * 
 * This sample Activator finds an IPingPong "remote" OSGi Service by the
 * ServiceRegistry. Sends a Ping to the IPingPong service and writes the pong
 * result into the console.
 * 
 * @author Alexander Ziegler
 * 
 */
public class Activator implements BundleActivator {

	private BundleContext context;

	public void start(BundleContext context) throws Exception {
		this.context = context;
		ServiceReference pingPongRef = context.getServiceReference(IPingPong.class.getName());
		if (pingPongRef != null) {
			new PingClient().sendPing(pingPongRef);
		} else {
			context.addServiceListener(new PingClient(), "(objectClass=" + IPingPong.class.getName() + ")");
		}
	}

	public void stop(BundleContext context) throws Exception {
		this.context = null;
	}

	class PingClient implements ServiceListener {

		public void serviceChanged(ServiceEvent event) {

			ServiceReference pingPongRef = event.getServiceReference();
			sendPing(pingPongRef);

		}

		void sendPing(ServiceReference pingPongRef) {
			IPingPong pingPong = (IPingPong) context.getService(pingPongRef);
			if (pingPong == null) {
				return;
			}

			Ping ping = new Ping();
			ping.setText("I ping you and you pong me");

			Pong pong = pingPong.ping(ping);
			System.out.println("PingPong::Client:: " + pong);

		}
	}

}
