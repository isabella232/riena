package org.eclipse.riena.internal.security.services;

import java.util.Hashtable;

import org.eclipse.riena.core.RienaActivator;
import org.eclipse.riena.core.service.ServiceDescriptor;
import org.eclipse.riena.internal.security.authenticationservice.AuthenticationService;
import org.eclipse.riena.internal.security.authorizationservice.AuthorizationService;
import org.eclipse.riena.internal.security.sessionservice.SessionService;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;
import org.eclipse.riena.security.server.session.ISessionService;
import org.eclipse.riena.security.sessionservice.ISessionProvider;
import org.eclipse.riena.security.sessionservice.SessionProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.security.services";

	// The shared instance
	private static Activator plugin;
	private ServiceRegistration authenticationService;
	private ServiceRegistration sessionService;
	private ServiceRegistration sessionProvider;
	private ServiceRegistration authorizationService;

	/**
	 * The constructor
	 */
	public Activator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		// register AuthenticationService
		Hashtable<String, Object> properties = ServiceDescriptor.newDefaultServiceProperties();
		properties.put("riena.remote", Boolean.TRUE.toString());
		properties.put("riena.remote.protocol", "hessian");
		properties.put("riena.remote.path", IAuthenticationService.WS_ID);
		authenticationService = getContext().registerService(IAuthenticationService.ID, new AuthenticationService(), properties);

		// register SessionService
		properties = ServiceDescriptor.newDefaultServiceProperties();
		properties.put("riena.remote", Boolean.TRUE.toString());
		properties.put("riena.remote.protocol", "hessian");
		properties.put("riena.remote.path", ISessionService.WS_ID);
		sessionService = getContext().registerService(ISessionService.ID, new SessionService(), properties);

		// register AuthorizationService
		properties = ServiceDescriptor.newDefaultServiceProperties();
		properties.put("riena.remote", Boolean.TRUE.toString());
		properties.put("riena.remote.protocol", "hessian");
		properties.put("riena.remote.path", IAuthorizationService.WS_ID);
		authorizationService = getContext().registerService(IAuthorizationService.ID, new AuthorizationService(), properties);

		// register SessionProvider
		sessionProvider = getContext().registerService(ISessionProvider.ID, new SessionProvider(), ServiceDescriptor.newDefaultServiceProperties());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		authenticationService.unregister();
		authorizationService.unregister();
		sessionService.unregister();
		sessionProvider.unregister();
		plugin = null;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
