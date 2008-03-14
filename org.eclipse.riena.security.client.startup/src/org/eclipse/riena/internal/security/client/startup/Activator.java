package org.eclipse.riena.internal.security.client.startup;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.RemoteServiceFactory;
import org.eclipse.riena.core.RienaActivator;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.security.client.startup"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	private IRemoteServiceRegistration authenticationReg;
	private IRemoteServiceRegistration authorizationReg;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		authenticationReg = new RemoteServiceFactory().createAndRegisterProxy(IAuthenticationService.class,
				"http://${securityhost}/hessian/AuthenticationService", "hessian", //$NON-NLS-1$ //$NON-NLS-2$
				"org.eclipse.riena.security.authentication.config"); //$NON-NLS-1$

		authorizationReg = new RemoteServiceFactory().createAndRegisterProxy(IAuthorizationService.class, "hessian", //$NON-NLS-1$
				"http://${securityhost}/hessian/AuthorizazionServiceWS", //$NON-NLS-1$
				"org.eclipse.riena.security.authorizationservice.config"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		if (authenticationReg != null) {
			authenticationReg.unregister();
		}
		if (authorizationReg != null) {
			authorizationReg.unregister();
		}
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
