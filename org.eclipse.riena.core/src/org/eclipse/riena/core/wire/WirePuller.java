/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.wire;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.SynchronousBundleListener;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.core.injector.IStoppable;
import org.eclipse.riena.core.injector.extension.ExtensionInjector;
import org.eclipse.riena.core.injector.service.ServiceInjector;
import org.eclipse.riena.core.util.Iter;
import org.eclipse.riena.internal.core.ignore.IgnoreFindBugs;
import org.eclipse.riena.internal.core.wire.ExtensionInjectorBuilder;
import org.eclipse.riena.internal.core.wire.ServiceInjectorBuilder;

/**
 * The {@code WirePuler} is responsible for the wiring of a bean.
 */
public class WirePuller implements IStoppable {

	private final Object bean;
	private BundleContext context;
	private List<IWiring> wirings;
	private List<IStoppable> injections;
	private State state = State.PENDING;
	private BundleListener bundleStoppingListener;

	// Only for unit testing of classes using accessors
	private static Map<Class<?>, Class<? extends IWiring>> wiringMocks;

	/**
	 * @param bean
	 */
	WirePuller(final Object bean) {
		Assert.isLegal(bean != null, "bean must exist"); //$NON-NLS-1$
		this.bean = bean;
	}

	/**
	 * Start the wiring.
	 * 
	 * @param context
	 * @return the bean
	 */
	public synchronized WirePuller andStart(final BundleContext context) {
		Assert.isLegal(context != null, "context must be given."); //$NON-NLS-1$
		Assert.isLegal(state == State.PENDING, "state must be pending."); //$NON-NLS-1$
		this.context = context;
		wire(bean.getClass());
		state = State.STARTED;
		bundleStoppingListener = new BundleStoppingListener();
		context.addBundleListener(bundleStoppingListener);
		return this;
	}

	/**
	 * Stop the wiring.
	 */
	public synchronized void stop() {
		if (state != State.STARTED) {
			return;
		}
		context.removeBundleListener(bundleStoppingListener);
		if (wirings != null) {
			for (IWiring wiring : wirings) {
				wiring.unwire(bean, context);
			}
		}
		if (injections != null) {
			for (IStoppable stoppable : Iter.ableReverse(injections)) {
				stoppable.stop();
			}
		}
		state = State.STOPPED;
	}

	private void wire(Class<?> beanClass) {
		if (beanClass == null || beanClass == Object.class) {
			return;
		}
		IWiring wiring = getWiring(getWiringClass(beanClass));
		add(wiring);
		wire(beanClass.getSuperclass());
		if (wiring != null) {
			wiring.wire(bean, context);
		}
		injectIntoAnnotatedMethods(bean, beanClass);
	}

	private void injectIntoAnnotatedMethods(Object bean, Class<?> beanClass) {
		for (Method method : beanClass.getDeclaredMethods()) {
			if (method.isAnnotationPresent(InjectService.class)) {
				injectServiceInto(bean, method);
			} else if (method.isAnnotationPresent(InjectExtension.class)) {
				injectExtensionInto(bean, method);
			}
		}
	}

	private void injectServiceInto(Object bean, Method method) {
		ServiceInjectorBuilder bob = new ServiceInjectorBuilder(bean, method);
		ServiceInjector injector = bob.build();
		add(injector.andStart(context));
	}

	private void injectExtensionInto(Object bean, Method method) {
		ExtensionInjectorBuilder bob = new ExtensionInjectorBuilder(bean, method);
		ExtensionInjector injector = bob.build();
		add(injector.andStart(context));
	}

	/**
	 * Collect the wirings.
	 * 
	 * @param wiring
	 */
	private void add(IWiring wiring) {
		if (wiring == null) {
			return;
		}
		if (wirings == null) {
			wirings = new ArrayList<IWiring>(2);
		}
		wirings.add(wiring);
	}

	/**
	 * Collect the stoppable injections.
	 * 
	 * @param stoppable
	 */
	private void add(IStoppable stoppable) {
		if (injections == null) {
			injections = new ArrayList<IStoppable>();
		}
		injections.add(stoppable);
	}

	/**
	 * @param context
	 * @return
	 */
	private IWiring getWiring(Class<? extends IWiring> wiringClass) {
		if (wiringClass == null) {
			return null;
		}
		try {
			return wiringClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException("Could not create instance of wiring " + wiringClass, e); //$NON-NLS-1$
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Could not create instance of wiring " + wiringClass, e); //$NON-NLS-1$
		}
	}

	/**
	 * @return
	 */
	private Class<? extends IWiring> getWiringClass(Class<?> beanClass) {
		// If mocks are defined, we use them instead of the regular mechanism.
		if (wiringMocks != null) {
			return wiringMocks.get(beanClass);
		}
		WireWith wiringAnnotation = beanClass.getAnnotation(WireWith.class);
		// Does the bean have a wiring annotation? Yes, use this class for wiring.
		if (wiringAnnotation != null) {
			return wiringAnnotation.value();
		}
		return null;
	}

	/**
	 * Stops this wire-puller if the owning bundle is stopping.
	 */
	private class BundleStoppingListener implements SynchronousBundleListener {
		@IgnoreFindBugs(value = "IS2_INCONSISTENT_SYNC", justification = "Field context of WirePuller is written only before this listener is added, field bundle of BundleContext is immutable.")
		public void bundleChanged(BundleEvent event) {
			if (event.getBundle() != context.getBundle()) {
				return;
			}
			if (event.getType() == BundleEvent.STOPPING) {
				stop();
			}
		}
	}

	/**
	 * For testing purposes it is sometimes necessary to change the default
	 * behavior for retrieving the wiring classes. With this method it is
	 * possible to define a map that tells the {@codeWirePuller} how to find the
	 * wiring classes.
	 * 
	 * @param wiringMocks
	 */
	public static void injectWiringMocks(Map<Class<?>, Class<? extends IWiring>> wiringMocks) {
		WirePuller.wiringMocks = wiringMocks;
	}

	private enum State {
		STARTED, STOPPED, PENDING
	}
}
