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
package org.eclipse.riena.communication.core.factory;

import java.lang.reflect.Proxy;
import java.util.Hashtable;

import org.eclipse.riena.communication.core.IRemoteServiceReference;
import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.IRemoteServiceRegistry;
import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.eclipse.riena.communication.core.publisher.RSDPublisherProperties;
import org.eclipse.riena.internal.communication.core.Activator;
import org.eclipse.riena.internal.communication.core.factory.CallHooksProxy;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationPlugin;

/**
 * The IRemoteServiceFactory creates {@link IRemoteServiceReference} for given
 * service end point description. The IRemoteServiceReference holds a proxy
 * reference instance to the service end point. The RemoteService Factory can
 * register an IRemoteServiceReference into the {@link IRemoteServiceRegistry}.
 * To create a {@link IRemoteServiceReference} call {@link #createProxy(..)}.
 * To create and register a "remote" OSGi Service in one step call
 * {@link #createAndRegisterProxy(..)}
 * <p>
 * This RemoteServiceFactory do not create IRemoteServiceReference itself. This
 * RemoteServiceFactory delegates the behaviour to a protocol specifically
 * {@link IRemoteServiceFactory} OSGi Service. This RemoteServiceFactory finds a
 * corresponding IRemoteServiceFactory by the filter
 * "riena.remote.protocol=[aProtocol]" (e.g. aProtcol replaced with 'hessian').
 * <p>
 * This RemoteServiceFactory does nothing if no protocol specifically
 * IRemoteServiceFactory is available.
 * <p>
 * <b>NOTE</b><br>
 * The Riena communication bundle content includes generic class loading and object
 * instantiation or delegates this behavior to other Riena communication bundles. Riena
 * supports Eclipse-BuddyPolicy concept. For further information about Riena
 * class loading and instanciation please read /readme.txt.
 * 
 * @author Alexander Ziegler
 * @author Christian Campo
 * 
 */
public class RemoteServiceFactory {

	private static final String HOST_ID = RemoteServiceFactory.class.getName();
	private BundleContext context;

	/**
	 * Creates a RemoteServiceFactory instance with the default bundle context.
	 * Prerequisite: application bundle should registered as
	 * Eclipse-RegisterBuddy. Sample Manifest.mf of foo.myapplication.api:
	 * 
	 * Eclipse-RegisterBuddy: org.eclipse.riena.communication.core
	 * 
	 */
	public RemoteServiceFactory() {
		this(Activator.getContext());
	}

	/**
	 * Creates a RemoteServiceFactory instance with the given bundle context.
	 * This bundle context should refer the service interface type.
	 * 
	 * @param context
	 */
	public RemoteServiceFactory(BundleContext context) {
		this.context = context;
	}

	/**
	 * Creates and registers a protocol specifically remote service reference
	 * and registers the reference into the {@link IRemoteServiceRegistry}. A
	 * registered reference becomes automatically registered as "remote" OSGi
	 * Service within the local OSGi container. Answers the registration object
	 * for the reference. If no protocol specific {@link IRemoteServiceFactory}
	 * OSGI Service available answers <code>null</code>.
	 * 
	 * @param interfaceClass
	 * @param url
	 * @param protocol
	 * @return the registration object or <code>null</code>
	 * 
	 */
	public IRemoteServiceRegistration createAndRegisterProxy(
			Class<?> interfaceClass, String url, String protocol,
			String configid) {
		return createAndRegisterProxy(interfaceClass, url, protocol, configid,
				HOST_ID);
	}

	/**
	 * Creates and registers a protocol specifically remote service reference
	 * and registers the reference into the {@link IRemoteServiceRegistry}. A
	 * registered reference becomes automatically registered as "remote" OSGi
	 * Service within the local OSGi container. Answers the registration object
	 * for the reference. If no protocol specific {@link IRemoteServiceFactory}
	 * OSGI Service available answers <code>null</code>.<br>
	 * <p>
	 * The hostId identifies who is responsible for this remote service
	 * registration
	 * 
	 * @param interfaceClass
	 * @param url
	 * @param protocol
	 * @param hostId
	 * @return the registration object or <code>null</code>
	 */
	public IRemoteServiceRegistration createAndRegisterProxy(
			Class<?> interfaceClass, String url, String protocol,
			String configid, String hostId) {
		RemoteServiceDescription rsd = createDescription(interfaceClass, url,
				protocol, configid);
		return createAndRegisterProxy(rsd);
	}

	/**
	 * Creates and registers a protocol specifically remote service reference
	 * and registers the reference into the {@link IRemoteServiceRegistry}. A
	 * registered reference becomes automatically registered as "remote" OSGi
	 * Service within the local OSGi container. Answers the registration object
	 * for the reference. If no protocol specific {@link IRemoteServiceFactory}
	 * OSGI Service available answers <code>null</code>.
	 * 
	 * @param rsDesc
	 * @return the registration object or <code>null</code>
	 */
	public IRemoteServiceRegistration createAndRegisterProxy(
			RemoteServiceDescription rsDesc) {
		return createAndRegisterProxy(rsDesc, HOST_ID);
	}

	/**
	 * Creates and registers a protocol specifically remote service reference
	 * and registers the reference into the {@link IRemoteServiceRegistry}. A
	 * registered reference becomes automatically registered as "remote" OSGi
	 * Service within the local OSGi container. Answers the registration object
	 * for the reference. If no protocol specific {@link IRemoteServiceFactory}
	 * OSGI Service available answers <code>null</code>.<br>
	 * <p>
	 * The hostId identifies who is responsible for this remote service
	 * registration
	 * 
	 * @param rsDesc
	 * @param hostId
	 * @return the registration object or <code>null</code>
	 */
	public IRemoteServiceRegistration createAndRegisterProxy(
			RemoteServiceDescription rsDesc, String hostId) {
		// create proxy first
		final IRemoteServiceReference rsRef = createProxy(rsDesc);
		if (rsRef == null) {
			System.out
					.println("Riena::RemoteServiceFactory::could not create proxy for "
							+ rsDesc);
			return null;
		}
		rsRef.setHostId(hostId);
		// get registry and register directly
		ServiceReference refRegistry = context
				.getServiceReference(IRemoteServiceRegistry.ID);
		if (refRegistry != null) {
			IRemoteServiceRegistry registry = (IRemoteServiceRegistry) context
					.getService(refRegistry);
			if (registry != null) {
				IRemoteServiceRegistration reg = registry
						.registerService(rsRef);
				return reg;
			}
		}
		ServiceListener sl = new ServiceListener() {
			public void serviceChanged(ServiceEvent event) {
				ServiceReference refRegistry = event.getServiceReference();
				IRemoteServiceRegistry registry = (IRemoteServiceRegistry) Activator
						.getContext().getService(refRegistry);
				if (registry != null) {
					registry.registerService(rsRef);
				}
			}
		};
		try {
			Activator.getContext().addServiceListener(sl,
					"(objectClass=" + IRemoteServiceRegistry.ID + ")");
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Creates a protocol specifically proxy reference (IRemoteServcieRefernce)
	 * with given end point parameters. Answers the IRemoteServiceReference. If
	 * no protocol specific {@link IRemoteServiceFactory} OSGI Service available
	 * answers <code>null</code>.
	 * 
	 * @param interfaceClass
	 * @param url
	 * @param protocol
	 * @return the proxy references or <code>null</code>
	 */
	public IRemoteServiceReference createProxy(Class<?> interfaceClass,
			String url, String protocol, String configid) {
		RemoteServiceDescription rsd = createDescription(interfaceClass, url,
				protocol, configid);
		return createProxy(rsd);
	}

	private RemoteServiceDescription createDescription(Class<?> interfaceClass,
			String url, String protocol, String configid) {
		RemoteServiceDescription rsd = new RemoteServiceDescription();
		rsd.setServiceInterfaceClass(interfaceClass);
		rsd.setServiceInterfaceClassName(interfaceClass.getName());
		rsd.setURL(url);
		rsd.setProtocol(protocol);
		rsd.setConfigid(configid);
		return rsd;
	}

	/**
	 * Creates a protocol specifically IRemoteServcieRefernce for the given end
	 * point description. Answers the IRemoteServiceReference.. If no end point
	 * specific {@link IRemoteServiceFactory} OSGI Service available answers
	 * <code>null</code>.
	 * 
	 * @param rsd
	 * @return the proxy references or <code>null</code>
	 */
	public IRemoteServiceReference createProxy(RemoteServiceDescription rsd) {
		// consult ConfigurationPlugins for URL
		try {
			ServiceReference[] pluginRefs = Activator.getContext()
					.getServiceReferences(ConfigurationPlugin.class.getName(),
							null);
			Hashtable<String, String> props = new Hashtable<String, String>();
			props.put(RSDPublisherProperties.PROP_URL, rsd.getURL());
			if (pluginRefs != null) {
				for (ServiceReference pluginRef : pluginRefs) {
					ConfigurationPlugin plugin = (ConfigurationPlugin) Activator
							.getContext().getService(pluginRef);
					if (plugin != null) {
						plugin.modifyConfiguration(null, props);
					}
				}
			}
			rsd.setURL(props.get(RSDPublisherProperties.PROP_URL));
		} catch (InvalidSyntaxException e1) {
			e1.printStackTrace();
		}
		ServiceReference[] references;
		if (rsd.getProtocol() == null) {
			return null;
		}
		// find a factory for this specific protocol
		String filter = "(" + IRemoteServiceFactory.PROP_PROTOCOL + "="
				+ rsd.getProtocol() + ")";
		try {
			references = context.getServiceReferences(IRemoteServiceFactory.ID,
					filter);
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
			return null;
		}
		// no factory for this protocol
		if (references == null) {
			System.out
					.println("Riena::RemoteServiceFactory:: WARN no IRemoteServiceFactory serviceRef available protocol ["
							+ rsd.getProtocol() + "]");
			return null;
		}

		ServiceReference refFactory = null;
		IRemoteServiceFactory factory = null;
		for (ServiceReference reference : references) {
			factory = (IRemoteServiceFactory) context.getService(reference);
			if (factory != null) {
				refFactory = reference;
				break;
			}
		}

		// could not get instance for existing reference
		if (factory == null) {
			System.out
					.println("Riena::RemoteServiceFactory:: WARN no IRemoteServiceFactory service available protocol ["
							+ rsd.getProtocol()
							+ "] id ["
							+ rsd.getServiceInterfaceClassName() + "]");
			return null;
		}
		System.out
				.println("Riena::RemoteServiceFactory:: DEBUG IRemoteServiceFactory found protocol ["
						+ rsd.getProtocol() + "] " + factory);

		// ask factory to create a proxy for me, and intercept the calls with a
		// CallHooksProxy instance
		try {
			IRemoteServiceReference rsr = factory.createProxy(rsd);
			CallHooksProxy callHooksProxy = new CallHooksProxy(rsr
					.getServiceInstance());
			callHooksProxy.setRemoteServiceDescription(rsd);
			callHooksProxy.setMessageContextAccessor(factory
					.getMessageContextAccessor());
			rsr.setServiceInstance(Proxy.newProxyInstance(rsd
					.getServiceInterfaceClass().getClassLoader(),
					new Class[] { rsd.getServiceInterfaceClass() },
					callHooksProxy));
			return rsr;
		} finally {
			context.ungetService(refFactory);
		}
	}

	/**
	 * This RemoteServiceFactory is responsible for all protocols because it
	 * delegates the behavior to {@link IRemoteServiceFactory} OSGi Services.
	 * 
	 * @return all protocols
	 */
	// public String getProtocol() {
	// return "*";
	// }
	/**
	 * Load class types for the given intefaceClassName.
	 * 
	 * @param interfaceClassName
	 * @return the class type for the given class Name
	 * 
	 * @throws ClassNotFoundException
	 */
	public Class<?> loadClass(String interfaceClassName)
			throws ClassNotFoundException {

		return getClass().getClassLoader().loadClass(interfaceClassName);
	}

}
