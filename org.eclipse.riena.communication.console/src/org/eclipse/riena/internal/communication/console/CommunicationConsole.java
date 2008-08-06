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
package org.eclipse.riena.internal.communication.console;

import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.eclipse.riena.communication.core.IRemoteServiceReference;
import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.IRemoteServiceRegistry;
import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.eclipse.riena.communication.core.publisher.IServicePublishBinder;

public class CommunicationConsole implements CommandProvider {
	IServicePublishBinder servicePublisher;
	IRemoteServiceRegistry serviceRegistry;

	public String getHelp() {
		return "---riena communication---\n\tremotestatus - list all services";
	}

	public void _remotestatus(CommandInterpreter ci) throws Exception {
		printToPublishServices();
		printPublishedServices();
		printRegisteredRemoteServices();

	}

	private void printToPublishServices() {
		if (servicePublisher == null) {
			return;
		}
		RemoteServiceDescription[] rsDescs = servicePublisher.getAllServices();

		// boolean found = false;
		for (RemoteServiceDescription rsDesc : rsDescs) {
			if (rsDesc.getState() == RemoteServiceDescription.State.UNREGISTERED) {
				// found = true;
				System.out.println("Riena:: not published end point=(" + rsDesc + ")");
			}
		}
	}

	private void printPublishedServices() {
		if (servicePublisher == null) {
			return;
		}
		RemoteServiceDescription[] rsDescs = servicePublisher.getAllServices();
		boolean found = false;
		for (RemoteServiceDescription rsDesc : rsDescs) {
			if (rsDesc.getState() == RemoteServiceDescription.State.REGISTERED) {
				found = true;
				System.out.println("Riena:: published end point=(" + rsDesc + ")");
			}
		}
		if (!found) {
			System.out.println("Riena:: no OSGi services published");
			return;
		}

	}

	private void printRegisteredRemoteServices() {
		if (serviceRegistry == null) {
			// System.out.println("Riena:: no OSGi services registered");
			return;
		}
		List<IRemoteServiceRegistration> rsRegs = serviceRegistry.registeredServices("*");
		if (rsRegs.size() == 0) {
			System.out.println("Riena:: no Remote OSGi services registered");
			return;
		}
		for (IRemoteServiceRegistration rsReg : rsRegs) {
			IRemoteServiceReference rsRef = rsReg.getReference();
			System.out.println("Riena:: registered remoteServiceRef=(" + rsRef + ")");
		}
	}

	/**
	 * @param servicePublisher
	 *            the servicePublisher to bind
	 */
	public void bind(IServicePublishBinder servicePublisher) {
		this.servicePublisher = servicePublisher;
	}

	/**
	 * @param servicePublisher
	 *            the servicePublisher to unbind
	 */
	public void unbind(IServicePublishBinder servicePublisher) {
		this.servicePublisher = null;
	}

	/**
	 * @param serviceRegistry
	 *            the serviceRegistry to bind
	 */
	public void bind(IRemoteServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	/**
	 * @param serviceRegistry
	 *            the serviceRegistry to unbind
	 */
	public void unbind(IRemoteServiceRegistry serviceRegistry) {
		this.serviceRegistry = null;
	}

}
