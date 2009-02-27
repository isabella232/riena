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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.SynchronousBundleListener;

/**
 * The {@code WirePuler} is responsible for the wiring of a bean.
 */
public class WirePuller {

	private Object bean;
	private BundleContext context;
	private List<IWiring> wirings;
	private State state = State.PENDING;
	private BundleListener bundleStoppingListener;

	// Only for unit testing of classes using accessors
	private static Map<Class<?>, Class<? extends IWiring>> wiringMocks;

	/**
	 * @param bean
	 */
	WirePuller(Object bean) {
		Assert.isLegal(bean != null, "bean must exist"); //$NON-NLS-1$
		this.bean = bean;
	}

	/**
	 * Start the wiring.
	 * 
	 * @param context
	 * @return the bean
	 */
	public WirePuller andStart(final BundleContext context) {
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
		state = State.STOPPED;
	}

	private void wire(Class<?> beanClass) {
		if (beanClass == null || beanClass == Object.class) {
			return;
		}
		Class<? extends IWiring> wiringClass = getWiringClass(beanClass);
		IWiring wiring = getWiring(wiringClass);
		addIfNotNull(wiring);
		wire(beanClass.getSuperclass());
		if (wiring != null) {
			wiring.wire(bean, context);
		}
	}

	/**
	 * Collect the wirings.
	 * 
	 * @param wiring
	 */
	private void addIfNotNull(IWiring wiring) {
		if (wiring == null) {
			return;
		}
		if (wirings == null) {
			wirings = new ArrayList<IWiring>(2);
		}
		wirings.add(wiring);
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
