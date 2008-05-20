package org.eclipse.riena.internal.security.simpleservices;

import java.io.InputStream;

import org.eclipse.riena.core.RienaActivator;
import org.eclipse.riena.core.service.ServiceDescriptor;
import org.eclipse.riena.security.authorizationservice.IPermissionStore;
import org.eclipse.riena.security.sessionservice.ISessionStore;
import org.eclipse.riena.security.simpleservices.authorizationservice.store.FilePermissionStore;
import org.eclipse.riena.security.simpleservices.sessionservice.store.MemoryStore;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.security.simpleservices.simple.services";

	private ServiceRegistration memoryStore;
	private ServiceRegistration filepermissionstore;

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
		// bring up a simple in memory session store
		memoryStore = getContext().registerService(ISessionStore.class.getName(), new MemoryStore(),
				ServiceDescriptor.newDefaultServiceProperties());

		// bring up a simple authorization store for permissions
		InputStream inputStream = this.getClass().getResourceAsStream("policy-def.xml");
		FilePermissionStore store = new FilePermissionStore(inputStream);
		filepermissionstore = context.registerService(IPermissionStore.class.getName(), store, ServiceDescriptor
				.newDefaultServiceProperties());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		memoryStore.unregister();
		filepermissionstore.unregister();
		super.stop(context);
	}

}
