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
package org.eclipse.riena.core.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.core.util.PropertiesUtils;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 * The ServiceInjector simplifies finding OSGi Services and injects them into a
 * target object. An target object defines named bind and unbind methods. The
 * ServiceInjector calls the bind method when the specified service was
 * registered or modified. ServiceInjector calls the unbind method when the
 * specified service becomes unregistered.
 * <p>
 * ServiceInjector tracks the specified OSGi Service after calls
 * {@link #start()} and stop tracks after calls {@link #stop()}.
 * 
 * @deprecated please use instead <code>ServiceId</code> and
 *             <code>Injector</code>
 */
@Deprecated
public class ServiceInjector {
	private boolean started = false;
	private List<ServiceReference> trackedServiceRefs;
	private BundleContext context;
	private ServiceListener serviceListner;
	private Object target;
	private String bindMethod;
	private String unbindMethod;
	private String filter;
	private String serviceId;
	private boolean onlyInjectHighestRanking;
	public final static String DEFAULT_RANKING = "-100";

	/**
	 * public constructor for ServiceInjector. only used for testing
	 */
	public ServiceInjector() {
		super();
	}

	/**
	 * Creates an ServiceInjector with the given parameters.
	 * 
	 * @param context
	 *            context <code>BundleContext</code> object against which the
	 *            tracking is done.
	 * @param serviceId
	 *            of the OSGi service to track
	 * @param target
	 *            becomes the OSGi service injected
	 * @param bindMethod
	 *            to call at target after the OSGi Service becomes registered or
	 *            modified
	 * @param unbindMethod
	 *            to call at target after the OSGi Service becomes unregistered
	 */
	public ServiceInjector(BundleContext context, String serviceId, Object target, String bindMethod,
			String unbindMethod) {
		this(context, serviceId, "(objectClass=" + serviceId + ")", target, bindMethod, unbindMethod);
	}

	/**
	 * Creates ServiceInjector with the given parameters and filter.
	 * 
	 * @param context
	 *            context <code>BundleContext</code> object against which the
	 *            tracking is done.
	 * @param serviceId
	 *            of the OSGi service to track
	 * @param filter
	 *            object to select the services to be tracked.
	 * @param target
	 *            becomes the OSGi service injected
	 * @param bindMethod
	 *            to call at target after the OSGi Service becomes registered or
	 *            modified
	 * @param unbindMethod
	 *            to call at target after the OSGi Service becomes unregistered
	 */
	public ServiceInjector(BundleContext context, String serviceId, String filter, Object target, String bindMethod,
			String unbindMethod) {
		super();
		this.context = context;
		this.serviceId = serviceId;
		this.filter = filter;
		if (!methodExists(target, bindMethod)) {
			System.out.println("bindMethod '" + bindMethod + "' does not exist in target class: "
					+ target.getClass().getName());
			throw new AssertionError("bindMethod does not exist");
		}
		if (!methodExists(target, unbindMethod)) {
			System.out.println("unbindMethod '" + unbindMethod + "' does not exist in target class: "
					+ target.getClass().getName());
			throw new AssertionError("unbindMethod does not exist");
		}
		this.bindMethod = bindMethod;
		this.unbindMethod = unbindMethod;
		this.target = target;
		serviceListner = new MyServiceListener();
		trackedServiceRefs = new ArrayList<ServiceReference>(1);

	}

	public boolean methodExists(Object target, String methodName) {
		Method[] methods = target.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Starts to track the specified OSGi Service
	 */
	public synchronized void start() {
		try {
			// try to find the service initial
			ServiceReference[] serviceRefs = context.getServiceReferences(serviceId, filter);
			// add an service listener for register or unregister
			// register the service listener before we go through the reference
			// list since its very more likely that no service is registered
			// between getServiceReferences and addServiceListener
			context.addServiceListener(serviceListner, filter);
			// then go through the list of references
			if (serviceRefs != null) {
				for (ServiceReference serviceRef : serviceRefs) {
					bind(serviceRef);
				}
			}
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
		started = true;
	}

	/**
	 * Stops to track the specified OSGi Service
	 */
	public synchronized void stop() {
		started = false;
		context.removeServiceListener(serviceListner);
		synchronized (trackedServiceRefs) {

			// copy list to array so that I iterate through array and still
			// remove entries from List concurrently
			ServiceReference[] serviceRefs = trackedServiceRefs
					.toArray(new ServiceReference[trackedServiceRefs.size()]);
			for (ServiceReference serviceRef : serviceRefs) {
				unbind(serviceRef);
			}
		}
	}

	protected void handleEvent(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.REGISTERED:
			bind(event.getServiceReference());
			break;
		case ServiceEvent.UNREGISTERING:
			unbind(event.getServiceReference());
			break;
		}

	}

	private void bind(ServiceReference serviceRef) {
		if (bindMethod == null) {
			return;
		}
		synchronized (trackedServiceRefs) {
			if (trackedServiceRefs.contains(serviceRef)) {
				return;
			}
			Object property = serviceRef.getProperty("objectClass");
			String objectClass = PropertiesUtils.accessProperty(property, null);
			Class<?> clazz = loadClass(objectClass);
			if (clazz != null) {
				Object service = context.getService(serviceRef);
				invoke(bindMethod, clazz, service);
			}
			trackedServiceRefs.add(serviceRef);
		}
	}

	private void unbind(ServiceReference serviceRef) {
		if (unbindMethod == null) {
			return;
		}

		synchronized (trackedServiceRefs) {
			if (!trackedServiceRefs.contains(serviceRef)) {
				return;
			}
			Object property = serviceRef.getProperty("objectClass");
			String objectClass = PropertiesUtils.accessProperty(property, null);
			Class<?> clazz = loadClass(objectClass);
			if (clazz != null) {
				Object service = context.getService(serviceRef);
				invoke(unbindMethod, clazz, service);
			}
			trackedServiceRefs.remove(serviceRef);
			context.ungetService(serviceRef);
		}
	}

	private Class<?> loadClass(String clazz) {
		try {
			return context.getBundle().loadClass(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void invoke(String methodName, Class<?> serviceClass, Object service) {
		try {
			Method method = target.getClass().getMethod(methodName, new Class[] { serviceClass });
			method.invoke(target, new Object[] { service });
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Disposes this ServiceInjector.
	 */
	public void dispose() {
		if (started) {
			stop();
		}
		context = null;
		serviceId = null;
		filter = null;
		target = null;
		bindMethod = null;
		unbindMethod = null;
		serviceListner = null;
		trackedServiceRefs = null;
	}

	class MyServiceListener implements ServiceListener {
		public void serviceChanged(ServiceEvent event) {
			if (serviceId != null) {
				Object property = event.getServiceReference().getProperty("objectClass");
				String objectClass = PropertiesUtils.accessProperty(property, null);
				// if serviceId set than only handle this event if id is equal.
				if (objectClass.equals(serviceId)) {
					handleEvent(event);
				}
			} else {
				// if no serviceId set handle any event
				handleEvent(event);
			}
		}
	}
}