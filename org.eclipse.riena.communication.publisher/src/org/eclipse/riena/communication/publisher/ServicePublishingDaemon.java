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
package org.eclipse.riena.communication.publisher;

import static org.eclipse.riena.communication.core.publisher.RSDPublisherProperties.PROP_IS_REMOTE;
import static org.eclipse.riena.communication.core.publisher.RSDPublisherProperties.PROP_REMOTE_PROTOCOL;

import org.eclipse.riena.internal.communication.publisher.Activator;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 * This class listens for new services and checks whether they have properties
 * that are requiring them to be published. If starts publication using
 * RemoteServicePublisher.
 * 
 * @see RemoteServicePublisher
 */
public class ServicePublishingDaemon {

	public static final String FILTER_REMOTE = "(&(" + PROP_IS_REMOTE + "=true)(" + PROP_REMOTE_PROTOCOL + "=*)" + ")";

	public ServicePublishingDaemon() {
		super();
		try {
			ServiceReference[] refs = Activator.getContext().getServiceReferences(null, FILTER_REMOTE);
			for (ServiceReference ref : refs) {
				publish(ref);
			}
		} catch (InvalidSyntaxException e1) {
			e1.printStackTrace();
		}

		ServiceListener listener = new ServiceListener() {
			public void serviceChanged(ServiceEvent event) {
				if (event.getType() == ServiceEvent.REGISTERED) {
					publish(event.getServiceReference());
				} else {
					if (event.getType() == ServiceEvent.UNREGISTERING) {
						unpublish(event.getServiceReference());
					}
				}
			}
		};
		try {
			Activator.getContext().addServiceListener(listener, FILTER_REMOTE);
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
	}

	private void publish(ServiceReference serviceReference) {

	}

	private void unpublish(ServiceReference serviceReference) {

	}

}
