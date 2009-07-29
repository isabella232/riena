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
package org.eclipse.riena.internal.security.services;

import java.util.Hashtable;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.core.RienaActivator;
import org.eclipse.riena.core.RienaConstants;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.security.authenticationservice.AuthenticationService;
import org.eclipse.riena.internal.security.authorizationservice.AuthorizationService;
import org.eclipse.riena.internal.security.sessionservice.SessionService;
import org.eclipse.riena.security.authorizationservice.IPermissionStore;
import org.eclipse.riena.security.common.ISubjectHolder;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;
import org.eclipse.riena.security.common.session.ISessionHolder;
import org.eclipse.riena.security.server.session.ISessionService;
import org.eclipse.riena.security.sessionservice.ISessionProvider;
import org.eclipse.riena.security.sessionservice.ISessionStore;
import org.eclipse.riena.security.sessionservice.SessionProvider;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.security.services"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;
		createAuthenticationServiceAndInjectors();
		createAuthorizationServiceAndInjectors();
		createSessionServiceAndInjectors();
		createSessionProviderAndInjectors();

	}

	private void createAuthenticationServiceAndInjectors() {
		// register AuthenticationService
		IAuthenticationService authenticationService = new AuthenticationService();
		Hashtable<String, Object> properties = RienaConstants.newDefaultServiceProperties();
		properties.put("riena.remote", Boolean.TRUE.toString()); //$NON-NLS-1$
		properties.put("riena.remote.protocol", "hessian"); //$NON-NLS-1$ //$NON-NLS-2$
		properties.put("riena.remote.path", IAuthenticationService.WS_ID); //$NON-NLS-1$
		getContext().registerService(IAuthenticationService.class.getName(), authenticationService, properties);

		// start injectors
		Inject.service(ISessionService.class).useRanking().into(authenticationService).andStart(
				Activator.getDefault().getContext());
		Inject.service(ISubjectHolder.class).useRanking().into(authenticationService).andStart(
				Activator.getDefault().getContext());
		Inject.service(ISessionHolder.class).useRanking().into(authenticationService).andStart(
				Activator.getDefault().getContext());
	}

	private void createAuthorizationServiceAndInjectors() {
		// register AuthorizationService
		IAuthorizationService authorizationService = new AuthorizationService();
		Hashtable<String, Object> properties = RienaConstants.newDefaultServiceProperties();
		properties.put("riena.remote", Boolean.TRUE.toString()); //$NON-NLS-1$
		properties.put("riena.remote.protocol", "hessian"); //$NON-NLS-1$ //$NON-NLS-2$
		properties.put("riena.remote.path", IAuthorizationService.WS_ID); //$NON-NLS-1$
		getContext().registerService(IAuthorizationService.class.getName(), authorizationService, properties);

		Inject.service(IPermissionStore.class).useRanking().into(authorizationService).andStart(
				Activator.getDefault().getContext());
	}

	private void createSessionServiceAndInjectors() {
		// register SessionService
		Hashtable<String, Object> properties = RienaConstants.newDefaultServiceProperties();
		properties.put("riena.remote", Boolean.TRUE.toString()); //$NON-NLS-1$
		properties.put("riena.remote.protocol", "hessian"); //$NON-NLS-1$ //$NON-NLS-2$
		properties.put("riena.remote.path", ISessionService.WS_ID); //$NON-NLS-1$
		ISessionService sessionService = new SessionService();
		getContext().registerService(ISessionService.class.getName(), sessionService, properties);

		Inject.service(ISessionStore.class).into(sessionService).andStart(Activator.getDefault().getContext());
		Inject.service(ISessionProvider.class).into(sessionService).andStart(Activator.getDefault().getContext());
	}

	private void createSessionProviderAndInjectors() {
		// register SessionProvider
		getContext().registerService(ISessionProvider.class.getName(), new SessionProvider(),
				RienaConstants.newDefaultServiceProperties());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.plugin = null;
		super.stop(context);
	}

	/**
	 * Get the plugin instance.
	 * 
	 * @return
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
