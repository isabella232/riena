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
package org.eclipse.riena.internal.communication.publisher.hessian;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.eclipse.riena.communication.core.hooks.IServiceMessageContext;
import org.eclipse.riena.communication.core.hooks.IServiceMessageContextAccessor;
import org.eclipse.riena.communication.core.publisher.IServicePublisher;
import org.osgi.service.log.LogService;

/**
 * This is a Hessian based implementation of {@link IServicePublisher}. it
 * publish or unpublish OSGi Services as service end points for Hessian
 * protocol.
 * <p>
 * HessianRemoteServicePublisher becomes registered as OSGi Service with name
 * {@link IServicePublisher#ID}. The OSGi Service set the property
 * "riena.protocol=hessian".
 * 
 * @author Alexander Ziegler
 * @author Christian Campo
 * 
 */
public class HessianRemoteServicePublisher implements IServicePublisher {
	private final static String PROTOCOL = "hessian"; //$NON-NLS-1$
	private final static String SERVLET_PATH = "/hessian"; //$NON-NLS-1$
	private IServiceMessageContextAccessor mca = new MsgCxtAcc();

	private HashMap<String, RemoteServiceDescription> webServiceDescriptions;

	private final static Logger LOGGER = Activator.getDefault().getLogger(HessianRemoteServicePublisher.class);
	private final static String PORT = System.getProperty("org.eclipse.equinox.http.jetty.http.port"); // get the jetty PORT //$NON-NLS-1$

	public HessianRemoteServicePublisher() {
		webServiceDescriptions = new HashMap<String, RemoteServiceDescription>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seexeval.rcplabs.hessianx.server.IWebServicePublisher#publishService(
	 * RemoteServiceDescription rsd )
	 */
	public synchronized String publishService(RemoteServiceDescription rsd) {
		if (!testInterface(rsd.getServiceInterfaceClass())) {
			String error = "cannot publish " //$NON-NLS-1$
					+ rsd + " because its interface contains multiple methods with the same name." //$NON-NLS-1$
					+ "That is not allowed for remote services (even if they have a different signature)."; //$NON-NLS-1$
			throw new RuntimeException(error);
		}

		String localhost = "localhost"; //$NON-NLS-1$
		try {
			localhost = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO: ok??
		}
		if (PORT != null) {
			localhost = localhost + ":" + PORT; //$NON-NLS-1$
		}
		String url = "http://" + localhost + SERVLET_PATH + rsd.getPath(); //$NON-NLS-1$
		rsd.setURL(url);
		webServiceDescriptions.put(SERVLET_PATH + rsd.getPath(), rsd);
		LOGGER.log(LogService.LOG_DEBUG, "published web service. " + rsd); //$NON-NLS-1$
		LOGGER.log(LogService.LOG_DEBUG, "web service count: " + webServiceDescriptions.size()); //$NON-NLS-1$
		return url;
	}

	private boolean testInterface(Class<?> interfaceClazz) {
		Set<String> methods = new HashSet<String>();
		Method[] declaredMethods = interfaceClazz.getDeclaredMethods();
		for (Method method : declaredMethods) {
			if (methods.contains(method.getName())) {
				return false;
			}
			methods.add(method.getName());
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * xeval.rcplabs.hessianx.server.IWebServicePublisher#unpublishService(java
	 * .lang.String)
	 */
	public synchronized void unpublishService(RemoteServiceDescription rsd) {
		webServiceDescriptions.remove(SERVLET_PATH + rsd.getPath());
		LOGGER.log(LogService.LOG_DEBUG, "unpublished web service. " + rsd); //$NON-NLS-1$
		LOGGER.log(LogService.LOG_DEBUG, "web service count: " + webServiceDescriptions.size()); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.IRemoteServiceProtocol#getProtocol()
	 */
	public String getProtocol() {
		return PROTOCOL;
	}

	public synchronized RemoteServiceDescription findService(String requestURI) {
		RemoteServiceDescription rsd = webServiceDescriptions.get(requestURI);
		return rsd;
	}

	public IServiceMessageContextAccessor getMessageContextAccessor() {
		return mca;
	}

	static class MsgCxtAcc implements IServiceMessageContextAccessor {
		MsgCxtAcc() {
			super();
		}

		public IServiceMessageContext getMessageContext() {
			return MessageContextAccessor.getMessageContext();
		}
	}
}
