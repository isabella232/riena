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
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.logging.ConsoleLogger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/**
 * The is the abstract base class for the specialized service injectors. See
 * {@link ServiceDescriptor} for explanation and usage. It provides common
 * functionality for the ranking injector and the filtering injector.
 */
public abstract class ServiceInjector {

	/**
	 * Default ´bind´ method name.
	 */
	public static final String DEFAULT_BIND_METHOD_NAME = "bind"; //$NON-NLS-1$

	/**
	 * Default ´unbind´ method name.
	 */
	public static final String DEFAULT_UNBIND_METHOD_NAME = "unbind"; //$NON-NLS-1$

	private ServiceDescriptor serviceDesc;
	private BundleContext context = null;
	private String filter;
	private Object target;
	private String bindMethodName = null;
	private List<Method> bindMethodProspects = null;
	private String unbindMethodName = null;
	private List<Method> unbindMethodProspects = null;
	private boolean started = false;
	private ServiceListener serviceListener;

	private final static Logger LOGGER = new ConsoleLogger(ServiceInjector.class.getName());

	/**
	 * Constructor for the <code>injectInto()</code> of
	 * <code>ServiceDescriptor</code>.
	 * 
	 * @param serviceDesc
	 * @param target
	 */
	ServiceInjector(ServiceDescriptor serviceDesc, Object target) {
		this.serviceDesc = serviceDesc;
		this.target = target;
		StringBuilder bob = new StringBuilder().append("(").append(Constants.OBJECTCLASS).append("=").append( //$NON-NLS-1$ //$NON-NLS-2$
				serviceDesc.getServiceClazz()).append(")"); //$NON-NLS-1$
		if (serviceDesc.getFilter() != null) {
			bob.insert(0, "(&"); //$NON-NLS-1$
			bob.append(serviceDesc.getFilter());
			bob.append(')');
		}
		this.filter = bob.toString();
	}

	/**
	 * Start the binding/un-binding/tracking for the target.
	 * 
	 * @throws some_kind_of_unchecked_exception
	 *             if injector has already been started.
	 * @throws some_kind_of_unchecked_exception
	 *             if the bind/un-bind methods are wrong
	 * @param context
	 * @return this injector
	 */
	public ServiceInjector andStart(BundleContext context) {
		Assert.isNotNull(context, "Bundle context must be not null.");
		Assert.isTrue(!started, "ServiceInjector already started!");
		started = true;
		this.context = context;
		if (bindMethodName == null)
			bindMethodName = DEFAULT_BIND_METHOD_NAME;
		bindMethodProspects = collectMethods("Bind method", bindMethodName);
		if (unbindMethodName == null)
			unbindMethodName = DEFAULT_UNBIND_METHOD_NAME;
		unbindMethodProspects = collectMethods("Unbind method", unbindMethodName);

		doStart();
		// for sure
		registerServiceListener();

		started = true;
		return this;
	}

	protected abstract void doStart();

	/**
	 * Stops tracking the specified service
	 */
	public void stop() {
		if (!started)
			return;
		unregisterServiceListener();
		doStop();
		bindMethodProspects = null;
		unbindMethodProspects = null;
		started = false;
	}

	protected abstract void doStop();

	/**
	 * Specify the bind method. If not specified
	 * {@link #DEFAULT_BIND_METHOD_NAME} will be used.
	 * 
	 * @throws some_kind_of_unchecked_exception
	 *             if the the injector had already been started
	 * @param bindMethodName
	 * @return this injector
	 */
	public ServiceInjector bind(String bindMethodName) {
		Assert.isTrue(!started, "ServiceInjector already started!");
		this.bindMethodName = bindMethodName;
		return this;
	}

	/**
	 * Specify the un-bind method. If not specified
	 * {@link #DEFAULT_UNBIND_METHOD_NAME} will be used.
	 * 
	 * @throws some_kind_of_unchecked_exception
	 *             if the the injector had already been started
	 * @param unbindMethodName
	 * @return this injector
	 */
	public synchronized ServiceInjector unbind(String unbindMethodName) {
		Assert.isTrue(!started, "ServiceInjector already started!");
		this.unbindMethodName = unbindMethodName;
		return this;
	}

	/**
	 * Registers the listener for service events. Can be called by subclasses
	 * within their {@link #doStart()} method for be timely closer to some other
	 * actions. However, if this is not done it will be called by this base
	 * class.
	 */
	protected void registerServiceListener() {
		if (serviceListener == null)
			serviceListener = new InjectorServiceListener();
		try {
			context.addServiceListener(serviceListener, filter);
		} catch (InvalidSyntaxException e) {
			throw new IllegalArgumentException("The specified filter has syntax errors.", e);
		}
	}

	/**
	 * Unregisters the listener for service events.
	 */
	private void unregisterServiceListener() {
		if (serviceListener == null)
			return;
		context.removeServiceListener(serviceListener);
		serviceListener = null;
	}

	private List<Method> collectMethods(String message, String methodName) {
		List<Method> prospects = new ArrayList<Method>();
		Method[] methods = target.getClass().getMethods();
		for (Method method : methods)
			if (method.getName().equals(methodName) && method.getParameterTypes().length == 1)
				prospects.add(method);

		if (prospects.size() != 0)
			return prospects;

		throw new IllegalArgumentException(message + " '" + methodName + "' does not exist in target class '"
				+ target.getClass().getName());
	}

	protected void handleEvent(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.REGISTERED:
			doBind(event.getServiceReference());
			break;
		case ServiceEvent.UNREGISTERING:
			doUnbind(event.getServiceReference());
			break;
		}
	}

	protected abstract void doBind(ServiceReference serviceRef);

	protected abstract void doUnbind(ServiceReference serviceRef);

	/**
	 * Get all service references for the service.
	 * 
	 * @return array of service references
	 */
	protected ServiceReference[] getServiceReferences() {
		try {
			return context.getServiceReferences(serviceDesc.getServiceClazz(), filter);
		} catch (InvalidSyntaxException e) {
			throw new IllegalArgumentException("The specified filter has syntax errors.", e);
		}
	}

	/**
	 * Find the matching bind method for the specified service reference.
	 * 
	 * @param service
	 * @return the bind method
	 */
	protected void invokeBindMethod(ServiceReference serviceRef) {
		if (serviceRef == null)
			return;
		// increments service use count, now it is ´1´
		Object service = context.getService(serviceRef);
		if (service == null)
			return;
		invokeMethod(bindMethodProspects, service);
	}

	/**
	 * Find the matching unbind method for the specified service reference.
	 * 
	 * @param service
	 * @return the unbind method
	 */
	protected void invokeUnbindMethod(ServiceReference serviceRef) {
		if (serviceRef == null)
			return;
		// need to get the service object, increments the use count, now it is
		// ´2´
		Object service = context.getService(serviceRef);
		if (service == null)
			return;
		invokeMethod(unbindMethodProspects, service);
		// decrement the use count from prior getService(), now it is ´1´
		context.ungetService(serviceRef);
		// decrement the use count from from prior bind, now it is ´0´
		context.ungetService(serviceRef);
	}

	private void invokeMethod(List<Method> methods, Object service) {
		assert service != null;

		Method method = findMatchingMethod(methods, service);
		if (method == null)
			return;
		invoke(method, service);
	}

	private Method findMatchingMethod(List<Method> methods, Object service) {
		assert methods != null;
		assert service != null;

		Class<?> parameterType = service.getClass();
		List<Method> targetedMethods = new ArrayList<Method>(1);
		for (Method method : methods) {
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes[0].isAssignableFrom(parameterType))
				targetedMethods.add(method);
		}

		if (targetedMethods.size() == 1)
			return targetedMethods.get(0);

		if (targetedMethods.isEmpty()) {
			LOGGER.log(LogService.LOG_ERROR, "Could not find a matching Bind/Unbind method from '" + methods
					+ "' for target class '" + target.getClass().getName() + "'.");
			return null;
		}
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

	/**
	 * @param method
	 * @param service
	 */
	private void invoke(Method method, Object service) {
		assert method != null;
		assert service != null;

		try {
			method.invoke(target, service);
		} catch (SecurityException e) {
			LOGGER.log(LogService.LOG_ERROR, "Security exception on invoking '" + method + "' on '"
					+ target.getClass().getName() + "'.", e);
		} catch (IllegalArgumentException e) {
			LOGGER.log(LogService.LOG_ERROR, "Illegal argument exception on invoking '" + method + "' on '"
					+ target.getClass().getName() + "'.", e);
		} catch (IllegalAccessException e) {
			LOGGER.log(LogService.LOG_ERROR, "Illegal access exception on invoking '" + method + "' on '"
					+ target.getClass().getName() + "'.", e);
		} catch (InvocationTargetException e) {
			LOGGER.log(LogService.LOG_ERROR, "Invocation target exception on invoking '" + method + "' on '"
					+ target.getClass().getName() + "'.", e);
		}
	}

	/**
	 * The service listener for this injector.
	 */
	class InjectorServiceListener implements ServiceListener {
		public void serviceChanged(ServiceEvent event) {
			int eventType = event.getType();
			if (eventType == ServiceEvent.REGISTERED || eventType == ServiceEvent.UNREGISTERING)
				handleEvent(event);
		}
	}

}
