package org.eclipse.riena.internal.demo.customer.server;

import org.eclipse.riena.communication.publisher.Publish;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.demo.customer.common.ICustomerDemoService;
import org.eclipse.riena.demo.customer.server.ICustomerRepository;
import org.eclipse.riena.demo.customer.server.CustomerRepository;
import org.eclipse.riena.demo.customer.server.CustomerDemoService;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.demo.customer.server";

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		CustomerDemoService personenService = new CustomerDemoService();
		context.registerService(ICustomerDemoService.class.getName(), personenService, null);
		Publish.service(ICustomerDemoService.class).usingPath(ICustomerDemoService.WS_ID).withProtocol("hessian").andStart(context);
		Inject.service(ICustomerRepository.class).into(personenService).andStart(context);
		context.registerService(ICustomerRepository.class.getName(), new CustomerRepository(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
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
