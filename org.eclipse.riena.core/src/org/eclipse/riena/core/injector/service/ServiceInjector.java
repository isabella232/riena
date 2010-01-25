/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.injector.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.injector.IStoppable;
import org.eclipse.riena.core.injector.InjectionFailure;
import org.eclipse.riena.internal.core.Activator;

/**
 * The is the abstract base class for the specialized service injectors. See
 * {@link ServiceDescriptor} for explanation and usage. It provides common
 * functionality for the ranking injector and the filtering injector.
 */
public abstract class ServiceInjector implements IStoppable {

	/**
	 * Default ´bind´ method name.
	 */
	public static final String DEFAULT_BIND_METHOD_NAME = "bind"; //$NON-NLS-1$

	/**
	 * Default ´unbind´ method name.
	 */
	public static final String DEFAULT_UNBIND_METHOD_NAME = "unbind"; //$NON-NLS-1$

	private final ServiceDescriptor serviceDesc;
	private final Object target;
	private BundleContext context;
	private final String filter;
	private String bindMethodName;
	private List<Method> bindMethodProspects;
	private String unbindMethodName;
	private List<Method> unbindMethodProspects;
	private State state;
	private ServiceListener serviceListener;
	private BundleListener bundleListener;

	private enum State {
		INITIAL, STARTING, STARTED, STOPPING, STOPPED
	};

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), ServiceInjector.class);

	/**
	 * Constructor for the <code>injectInto()</code> of
	 * <code>ServiceDescriptor</code>.
	 * 
	 * @param serviceDesc
	 * @param target
	 */
	ServiceInjector(final ServiceDescriptor serviceDesc, final Object target) {
		this.state = State.INITIAL;
		this.serviceDesc = serviceDesc;
		this.target = target;
		final StringBuilder bob = new StringBuilder().append("(").append(Constants.OBJECTCLASS).append("=").append( //$NON-NLS-1$ //$NON-NLS-2$
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
	 * <p>
	 * If there are matching and available services then they will be bound
	 * before returning from this method.
	 * 
	 * @throws some_kind_of_unchecked_exception
	 *             if injector has already been started or stopped.
	 * @throws some_kind_of_unchecked_exception
	 *             if the bind/un-bind methods are wrong
	 * @param context
	 * @return this injector
	 */
	public ServiceInjector andStart(final BundleContext context) {
		Assert.isNotNull(context, "Bundle context must be not null."); //$NON-NLS-1$
		Assert.isTrue(state == State.INITIAL, "ServiceInjector already started or stopped!"); //$NON-NLS-1$
		this.state = State.STARTING;
		this.context = context;
		if (bindMethodName == null) {
			bindMethodName = DEFAULT_BIND_METHOD_NAME;
		}
		bindMethodProspects = collectMethods("Bind method", bindMethodName); //$NON-NLS-1$
		if (unbindMethodName == null) {
			unbindMethodName = DEFAULT_UNBIND_METHOD_NAME;
		}
		unbindMethodProspects = collectMethods("Unbind method", unbindMethodName); //$NON-NLS-1$

		doStart();
		// for sure
		registerServiceListener();
		registerBundleListener();

		state = State.STARTED;
		return this;
	}

	/**
	 * The <code>doStart</code> method is required to bind all matching the
	 * services with the target.
	 */
	protected abstract void doStart();

	/**
	 * Stops tracking the specified service.
	 * <p>
	 * All prior bound services will be unbound before returning from this
	 * method.
	 */
	public synchronized void stop() {
		if (state != State.STARTED) {
			return;
		}
		state = State.STOPPING;
		unregisterBundleListener();
		unregisterServiceListener();
		doStop();
		bindMethodProspects = null;
		unbindMethodProspects = null;
		state = State.STOPPED;
	}

	/**
	 * The <code>doStop></code> method is required to unbind all bound services
	 * from the target.
	 */
	protected abstract void doStop();

	/**
	 * Specify the bind method. If not specified
	 * {@link #DEFAULT_BIND_METHOD_NAME} will be used.
	 * 
	 * @throws some_kind_of_unchecked_exception
	 *             if injector has already been started or stopped.
	 * @param bindMethodName
	 * @return this injector
	 */
	public ServiceInjector bind(final String bindMethodName) {
		Assert.isTrue(state == State.INITIAL, "ServiceInjector already started or stopped!"); //$NON-NLS-1$
		this.bindMethodName = bindMethodName;
		return this;
	}

	/**
	 * Specify the un-bind method. If not specified
	 * {@link #DEFAULT_UNBIND_METHOD_NAME} will be used.
	 * 
	 * @throws some_kind_of_unchecked_exception
	 *             if injector has already been started or stopped.
	 * @param unbindMethodName
	 * @return this injector
	 */
	public synchronized ServiceInjector unbind(final String unbindMethodName) {
		Assert.isTrue(state == State.INITIAL, "ServiceInjector already started or stopped!"); //$NON-NLS-1$
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
		if (serviceListener == null) {
			serviceListener = new InjectorServiceListener();
		}
		try {
			context.addServiceListener(serviceListener, filter);
		} catch (final InvalidSyntaxException e) {
			throw new InjectionFailure("The specified filter has syntax errors.", e); //$NON-NLS-1$
		}
	}

	/**
	 * Unregisters the listener for service events.
	 */
	private void unregisterServiceListener() {
		if (serviceListener == null) {
			return;
		}
		context.removeServiceListener(serviceListener);
	}

	/**
	 * 
	 */
	private void registerBundleListener() {
		if (bundleListener == null) {
			bundleListener = new InjectorBundleListener();
		}
		context.addBundleListener(bundleListener);
	}

	/**
	 * 
	 */
	private void unregisterBundleListener() {
		if (bundleListener != null) {
			context.removeBundleListener(bundleListener);
			bundleListener = null;
		}
	}

	private List<Method> collectMethods(final String message, final String methodName) {
		final List<Method> prospects = new ArrayList<Method>();
		final Method[] methods = target.getClass().getMethods();
		for (final Method method : methods) {
			if (method.getName().equals(methodName) && method.getParameterTypes().length == 1) {
				prospects.add(method);
			}
		}

		if (prospects.size() != 0) {
			return prospects;
		}

		throw new InjectionFailure(message + " '" + methodName + "' does not exist in target class '" //$NON-NLS-1$ //$NON-NLS-2$
				+ target.getClass().getName());
	}

	protected void handleEvent(final ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.REGISTERED:
			doBind(event.getServiceReference());
			break;
		case ServiceEvent.UNREGISTERING:
			doUnbind(event.getServiceReference());
			break;
		default:
			// ignore
			break;
		}
	}

	protected abstract void doBind(final ServiceReference serviceRef);

	protected abstract void doUnbind(final ServiceReference serviceRef);

	/**
	 * Get all service references for the service.
	 * 
	 * @return array of service references
	 */
	protected ServiceReference[] getServiceReferences() {
		try {
			return context.getServiceReferences(serviceDesc.getServiceClazz(), filter);
		} catch (final InvalidSyntaxException e) {
			throw new InjectionFailure("The specified filter has syntax errors.", e); //$NON-NLS-1$
		}
	}

	/**
	 * Find the matching bind method for the specified service reference.
	 * 
	 * @param service
	 * @return the bind method
	 */
	protected void invokeBindMethod(final ServiceReference serviceRef) {
		if (serviceRef == null) {
			return;
		}
		// increments service use count, now it is ´1´
		final Object service = context.getService(serviceRef);
		if (service == null) {
			return;
		}
		invokeMethod(bindMethodProspects, service);
	}

	/**
	 * Find the matching unbind method for the specified service reference.
	 * 
	 * @param service
	 * @return the unbind method
	 */
	protected void invokeUnbindMethod(final ServiceReference serviceRef) {
		if (serviceRef == null) {
			return;
		}
		// need to get the service object, increments the use count, now it is
		// ´2´
		final Object service = context.getService(serviceRef);
		if (service == null) {
			return;
		}
		invokeMethod(unbindMethodProspects, service);
		// decrement the use count from prior getService(), now it is ´1´
		context.ungetService(serviceRef);
		// decrement the use count from from prior bind, now it is ´0´
		context.ungetService(serviceRef);
	}

	private void invokeMethod(final List<Method> methods, final Object service) {
		assert service != null;

		final Method method = findMatchingMethod(methods, service);
		if (method == null) {
			return;
		}
		invoke(method, service);
	}

	private Method findMatchingMethod(final List<Method> methods, final Object service) {
		assert methods != null;
		assert service != null;

		final Class<?> parameterType = service.getClass();
		final List<Method> targetedMethods = new ArrayList<Method>(1);
		for (final Method method : methods) {
			final Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes[0].isAssignableFrom(parameterType)) {
				targetedMethods.add(method);
			}
		}

		if (targetedMethods.size() == 1) {
			return targetedMethods.get(0);
		}

		if (targetedMethods.isEmpty()) {
			LOGGER.log(LogService.LOG_ERROR, "Could not find a matching Bind/Unbind method from '" + methods //$NON-NLS-1$
					+ "' for target class '" + target.getClass().getName() + "'."); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}
		// find most specific method
		Class<?> superType = parameterType;
		while (superType != null) {
			for (final Method method : targetedMethods) {
				if (!method.getParameterTypes()[0].isAssignableFrom(superType)) {
					return method;
				}
			}
			superType = superType.getSuperclass();
		}

		return targetedMethods.get(0);
	}

	/**
	 * @param method
	 * @param service
	 */
	private void invoke(final Method method, final Object service) {
		assert method != null;
		assert service != null;
		Throwable t = null;
		String cause = null;

		try {
			method.invoke(target, service);
		} catch (final SecurityException e) {
			t = e;
			cause = "Security exception on invoking '"; //$NON-NLS-1$ 
		} catch (final IllegalArgumentException e) {
			t = e;
			cause = "Illegal argument exception on invoking '"; //$NON-NLS-1$
		} catch (final IllegalAccessException e) {
			t = e;
			cause = "Illegal access exception on invoking '"; //$NON-NLS-1$
		} catch (final InvocationTargetException e) {
			t = e.getTargetException();
			cause = "Invocation target exception on invoking '"; //$NON-NLS-1$
		}
		if (t != null) {
			final String message = cause + method + "' on '" + target.getClass().getName() + "'."; //$NON-NLS-1$ //$NON-NLS-2$
			LOGGER.log(LogService.LOG_ERROR, message, t);
			throw new InjectionFailure(message, t);
		}
	}

	/**
	 * The service listener for this injector.
	 */
	class InjectorServiceListener implements ServiceListener {
		public void serviceChanged(final ServiceEvent event) {
			final int eventType = event.getType();
			if (eventType == ServiceEvent.REGISTERED || eventType == ServiceEvent.UNREGISTERING) {
				handleEvent(event);
			}
		}
	}

	/**
	 * The bundle listener for this injector.
	 */
	class InjectorBundleListener implements SynchronousBundleListener {
		public void bundleChanged(final BundleEvent event) {
			if (event.getBundle() != context.getBundle()) {
				return;
			}
			if (event.getType() == BundleEvent.STOPPING) {
				stop();
			}
		}
	}

}
