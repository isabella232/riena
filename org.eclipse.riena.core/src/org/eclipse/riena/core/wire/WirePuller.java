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
package org.eclipse.riena.core.wire;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.osgi.framework.BundleContext;

/**
 * The {@code WirePuler} is responsible for the wiring of a bean.
 */
public class WirePuller {

	private Object bean;
	private BundleContext context;
	private List<IWiring> wirings;
	private State state = State.PENDING;

	// Only for unit testing of classes using accessors
	private static Map<Class<?>, Class<? extends IWiring>> wireWrapMocks;

	// This is the post-fix for the wire wrap class when created by convention 
	private static final String WIRE_WRAP_POSTFIX = "WireWrap"; //$NON-NLS-1$

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
	public WirePuller andStart(BundleContext context) {
		Assert.isLegal(context != null, "context must be given."); //$NON-NLS-1$
		Assert.isLegal(state == State.PENDING, "state must be pending."); //$NON-NLS-1$
		wire(bean.getClass(), context);
		state = State.STARTED;
		this.context = context;
		return this;
	}

	/**
	 * Stop the wiring.
	 */
	public void stop() {
		if (state != State.STARTED) {
			return;
		}
		if (wirings != null) {
			for (IWiring wiring : wirings) {
				wiring.unwire(bean, context);
			}
		}
		state = State.STOPPED;
	}

	private void wire(Class<?> beanClass, BundleContext context) {
		if (beanClass == null || beanClass == Object.class) {
			return;
		}
		Class<? extends IWiring> wireWrapClass = getWireWrapClass(beanClass, context);
		IWiring wiring = getWireWrap(wireWrapClass);
		add(wiring);
		wire(beanClass.getSuperclass(), context);
		if (wiring != null) {
			wiring.wire(bean, context);
		}
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
	 * @param context
	 * @return
	 */
	private IWiring getWireWrap(Class<? extends IWiring> wireWrapClass) {
		if (wireWrapClass == null) {
			return null;
		}
		try {
			return wireWrapClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException("Could not create instance of wire wrap " + wireWrapClass, e); //$NON-NLS-1$
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Could not create instance of wire wrap " + wireWrapClass, e); //$NON-NLS-1$
		}
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends IWiring> getWireWrapClass(Class<?> beanClass, BundleContext context) {
		// If mocks are defined, we use them instead of the regular mechanism.
		if (wireWrapMocks != null) {
			return wireWrapMocks.get(beanClass);
		}
		WireWith wireWrapAnnotation = beanClass.getAnnotation(WireWith.class);
		// Does the bean have a wiring annotation? Yes, use this class for wiring.
		if (wireWrapAnnotation != null) {
			return wireWrapAnnotation.value();
		}
		return null;
	}

	/**
	 * For testing purposes it is sometimes necessary to change the default
	 * behavior for retrieving the wiring classes. With this method it is
	 * possible to define a map that tells the {@codeWirePuller} how to find the
	 * wiring classes.
	 * 
	 * @param wireWrapMocks
	 */
	public static void injectWireMocks(Map<Class<?>, Class<? extends IWiring>> wireWrapMocks) {
		WirePuller.wireWrapMocks = wireWrapMocks;
	}

	private enum State {
		STARTED, STOPPED, PENDING
	}
}
