package org.eclipse.riena.internal.sample.app.client;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.RemoteServiceFactory;
import org.eclipse.riena.sample.app.common.model.ICustomerSearch;
import org.eclipse.riena.sample.app.common.model.IHelloWorldService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.sample.app.client.helloworld";

	// The shared instance
	private static Activator plugin;

	private static BundleContext bundleContext;
	private IRemoteServiceRegistration helloWorldServiceReg;
	private IRemoteServiceRegistration customerSearchService;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		// hack to force riena.core to load

		plugin = this;
		bundleContext = context;

		// register hessian proxy for riena remote service
		helloWorldServiceReg = new RemoteServiceFactory().createAndRegisterProxy(IHelloWorldService.class,
				"http://localhost:8080/hessian/HelloWorldServiceWS", "hessian",
				"de.compeople.scp.sample.app.client.config");

		customerSearchService = new RemoteServiceFactory().createAndRegisterProxy(ICustomerSearch.class,
				"http://localhost:8080/hessian/CustomerSearchWS", "hessian",
				"de.compeople.scp.sample.app.client.config");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		bundleContext = null;

		if (helloWorldServiceReg != null) {
			helloWorldServiceReg.unregister();
			helloWorldServiceReg = null;
		}

		if (customerSearchService != null) {
			customerSearchService.unregister();
			customerSearchService = null;
		}
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public static BundleContext getContext() {
		return bundleContext;
	}

}
