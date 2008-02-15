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
package org.eclipse.riena.core.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 * The is the service injector. See {@link ServiceId} for explanation and usage.
 */
public class Injector {

	/**
	 * Default ´bind´ method name.
	 */
	public static final String DEFAULT_BIND_METHOD_NAME = "bind";

	/**
	 * Default ´unbind´ method name.
	 */
	public static final String DEFAULT_UNBIND_METHOD_NAME = "unbind";

	private ServiceId serviceId;
	private Object target;
	private String bindMethodName = null;
	private String unbindMethodName = null;
	private boolean started = false;
	private BundleContext context = null;
	private List<ServiceReference> trackedServiceRefs = null;
	private ServiceListener serviceListener;

	private RuntimeException lastError;

	/**
	 * Constructor for the <code>injectInto()</code> of <code>ServiceId</code>.
	 * 
	 * @param serviceId
	 * @param target
	 */
	Injector(ServiceId serviceId, Object target) {
		this.serviceId = serviceId;
		this.target = target;
	}

	/**
	 * Start the binding/un-binding/tracking for the target.
	 * 
	 * @throws IllegalStateException
	 *             if injector has already been started.
	 * @throws IllegalArgumentException
	 *             if the bind/un-bind methods are wrong
	 * @param context
	 * @return this injector
	 */
	public Injector start(BundleContext context) {
		if (started)
			throw new IllegalStateException("Injector already started!");
		started = true;
		lastError = null;
		this.context = context;
		if (bindMethodName == null)
			bindMethodName = DEFAULT_BIND_METHOD_NAME;
		assertMethod("Bind method", bindMethodName);
		if (unbindMethodName == null)
			unbindMethodName = DEFAULT_UNBIND_METHOD_NAME;
		assertMethod("Unbind method", unbindMethodName);
		serviceListener = new InjectorServiceListener();
		trackedServiceRefs = new ArrayList<ServiceReference>(1);
		start();
		if (lastError != null)
			throw lastError;
		return this;
	}

	/**
	 * Stops tracking the specified service
	 */
	public void stop() {
		if (!started)
			return;
		context.removeServiceListener(serviceListener);

		// copy list to array so that I iterate through array and still
		// remove entries from List concurrently
		ServiceReference[] serviceRefs;
		synchronized (trackedServiceRefs) {
			serviceRefs = trackedServiceRefs.toArray(new ServiceReference[trackedServiceRefs.size()]);
		}
		for (ServiceReference serviceRef : serviceRefs)
			unbind(serviceRef);
		serviceListener = null;
		trackedServiceRefs = null;
		started = false;
	}

	/**
	 * Specify the bind method. If not specified
	 * {@link #DEFAULT_BIND_METHOD_NAME} will be used.
	 * 
	 * @throws IllegalStateException
	 *             if the the injector had already been started
	 * @param bindMethodName
	 * @return this injector
	 */
	public Injector bind(String bindMethodName) {
		if (started)
			throw new IllegalStateException("Injector already started!");
		this.bindMethodName = bindMethodName;
		return this;
	}

	/**
	 * Specify the un-bind method. If not specified
	 * {@link #DEFAULT_UNBIND_METHOD_NAME} will be used.
	 * 
	 * @throws IllegalStateException
	 *             if the the injector had already been started
	 * @param unbindMethodName
	 * @return this injector
	 */
	public synchronized Injector unbind(String unbindMethodName) {
		if (started)
			throw new IllegalStateException("Injector already started!");
		this.unbindMethodName = unbindMethodName;
		return this;
	}

	public Throwable getLastError() {
		return lastError;
	}

	private void assertMethod(String message, String methodName) {
		Method[] methods = target.getClass().getMethods();
		for (Method method : methods)
			if (method.getName().equals(methodName))
				return;

		throw new IllegalArgumentException(message + " '" + methodName + "' does not exist in target class '"
				+ target.getClass().getName());
	}

	/**
	 * Starts to track the specified OSGi Service
	 */
	private void start() {
		try {
			ServiceReference[] serviceRefs = null;
			// try to find the service initially
			if (serviceId.usesRanking()) {
				ServiceReference serviceRef = context.getServiceReference(serviceId.getServiceId());
				if (serviceRef != null)
					serviceRefs = new ServiceReference[] { serviceRef };
			} else
				serviceRefs = context.getServiceReferences(serviceId.getServiceId(), serviceId.getFilter());

			// add an service listener for register or unregister
			// register the service listener before we go through the reference
			// list since its very more likely that no service is registered
			// between getServiceReferences and addServiceListener
			context.addServiceListener(serviceListener, serviceId.getFilter());
			// then go through the list of references
			if (serviceRefs != null)
				for (ServiceReference serviceRef : serviceRefs)
					bind(serviceRef);
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
		started = true;
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
		synchronized (trackedServiceRefs) {
			if (trackedServiceRefs.contains(serviceRef))
				return;

			Object service = context.getService(serviceRef);
			if (service != null) {
				Method method = findMatchingMethod(target.getClass(), bindMethodName, service.getClass());
				invoke(method, service);
			}
			trackedServiceRefs.add(serviceRef);
		}
	}

	private void unbind(ServiceReference serviceRef) {
		synchronized (trackedServiceRefs) {
			if (!trackedServiceRefs.contains(serviceRef))
				return;

			Object service = context.getService(serviceRef);
			if (service != null) {
				Method method = findMatchingMethod(target.getClass(), unbindMethodName, service.getClass());
				invoke(method, service);
			}
			trackedServiceRefs.remove(serviceRef);
			context.ungetService(serviceRef);
		}
	}

	/**
	 * @param method
	 * @param service
	 */
	private void invoke(Method method, Object service) {
		if (method == null) {
			lastError = new IllegalArgumentException("Bind/Unbind method '" + method
					+ "' does not exist in target class '" + target.getClass().getName());
			// TODO logging
			return;
		}
		try {
			method.invoke(target, service);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private static Method findMatchingMethod(Class<?> targetType, String methodName, Class<?> parameterType) {
		assert targetType != null;
		assert methodName != null;
		assert parameterType != null;

		List<Method> targetedMethods = new ArrayList<Method>(1);
		Method[] methods = targetType.getMethods();
		for (Method method : methods) {
			if (!method.getName().equals(methodName))
				continue;

			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length != 1)
				continue;

			if (parameterTypes[0].isAssignableFrom(parameterType))
				targetedMethods.add(method);
		}

		if (targetedMethods.size() == 1)
			return targetedMethods.get(0);

		if (targetedMethods.isEmpty())
			return null;

		// find most specific method
		Class<?> superType = parameterType;
		while (superType != null) {
			for (Method method : targetedMethods)
				if (!method.getParameterTypes()[0].isAssignableFrom(superType))
					return method;
			superType = superType.getSuperclass();
		}

		return targetedMethods.get(0);
	}

	class InjectorServiceListener implements ServiceListener {
		public void serviceChanged(ServiceEvent event) {
			int eventType = event.getType();
			if (eventType != ServiceEvent.REGISTERED && eventType != ServiceEvent.UNREGISTERING)
				return;
			Object value = event.getServiceReference().getProperty(Constants.OBJECTCLASS);
			Assert.isTrue(value instanceof String[], "Contract vioaltion: property ´objectClass´ is not a String[]");
			String[] types = (String[]) value;
			for (String type : types)
				if (type.equals(serviceId.getServiceId())) {
					handleEvent(event);
					return;
				}
		}
	}

}
