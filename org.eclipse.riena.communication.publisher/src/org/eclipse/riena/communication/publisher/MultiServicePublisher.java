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

import static org.eclipse.riena.communication.core.publisher.RSDPublisherProperties.PROP_REMOTE_PATH;

import org.eclipse.riena.communication.core.publisher.IServicePublishBinder;
import org.eclipse.riena.communication.core.publisher.RSDPublisherProperties;
import org.eclipse.riena.communication.core.util.CommunicationUtil;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.communication.publisher.Activator;

import org.eclipse.core.runtime.Assert;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 * 
 */
public class MultiServicePublisher {

	private String filter;
	private BundleContext context;
	private String protocol;

	private IServicePublishBinder binder;

	// public static final String FILTER_REMOTE = "(&(" + PROP_IS_REMOTE +
	// "=true)("
	// + PROP_REMOTE_PROTOCOL + "=*)" + ")";

	public MultiServicePublisher() {
		super();
		Inject.service(IServicePublishBinder.class.getName()).useRanking().into(this).andStart(
				Activator.getDefault().getContext());
	}

	public MultiServicePublisher useFilter(String filter) {
		Assert.isNotNull(filter);
		this.filter = filter;
		return this;
	}

	public MultiServicePublisher withProtocol(String protocol) {
		Assert.isNotNull(protocol);
		this.protocol = protocol;
		return this;
	}

	public void andStart(BundleContext context) {
		Assert.isNotNull(filter);
		this.context = context;

		try {
			ServiceReference[] refs = this.context.getServiceReferences(null, filter);
			if (refs != null) {
				for (ServiceReference ref : refs) {
					publish(ref);
				}
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
			Activator.getDefault().getContext().addServiceListener(listener, filter);
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}

		return;
	}

	public void bind(IServicePublishBinder binder) {
		this.binder = binder;
	}

	public void unbind(IServicePublishBinder binder) {
		this.binder = null;
	}

	private void publish(ServiceReference serviceReference) {
		String usedProtocol;
		if (protocol != null) {
			usedProtocol = protocol;
		} else {
			usedProtocol = CommunicationUtil.accessProperty(serviceReference
					.getProperty(RSDPublisherProperties.PROP_REMOTE_PROTOCOL), null);
		}
		String path = CommunicationUtil.accessProperty(serviceReference.getProperty(PROP_REMOTE_PATH), null);
		binder.publish(serviceReference, path, usedProtocol);

	}

	private void unpublish(ServiceReference serviceReference) {
		binder.unpublish(serviceReference);
	}

}
