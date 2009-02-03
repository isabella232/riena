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

import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.internal.core.ignore.Nop;
import org.osgi.framework.BundleContext;

/**
 * The {@code WirePuler} is responsible for the wiring of a bean.
 */
public class WirePuller {

	private Object bean;

	// Only for unit testing of classes using accessors
	private static Map<Class<?>, Class<? extends IWireWrap>> wireWrapMocks;

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
	public Object andStart(BundleContext context) {
		wire(bean, bean.getClass(), context);
		return bean;
	}

	private void wire(Object bean, Class<?> beanClass, BundleContext context) {
		IWireWrap wireWrap;
		Class<? extends IWireWrap> wireWrapClass;

		if (beanClass == null || beanClass == Object.class) {
			return;
		}
		wireWrapClass = getWireWrapClass(beanClass, context);
		wireWrap = getWireWrap(wireWrapClass);
		wire(bean, beanClass.getSuperclass(), context);
		if (wireWrap != null) {
			wireWrap.wire(bean, context);
		}
	}

	/**
	 * @param context
	 * @return
	 */
	private IWireWrap getWireWrap(Class<? extends IWireWrap> wireWrapClass) {
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
	private Class<? extends IWireWrap> getWireWrapClass(Class<?> beanClass, BundleContext context) {
		// If mocks are defined, we use them instead of the regular mechanism.
		if (wireWrapMocks != null) {
			return wireWrapMocks.get(beanClass);
		}
		WireWrap wireWrapAnnotation = beanClass.getAnnotation(WireWrap.class);
		// Does the bean have a wire-wrap annotation with a special wire-wrap class? Yes, use this class for wiring.
		if (wireWrapAnnotation != null && wireWrapAnnotation.value() != WireWrap.UseDefaultWiring.class) {
			return wireWrapAnnotation.value();
		}
		// Otherwise try to find the wire-wrap class by convention
		String wireWrapClassName = beanClass.getName() + WIRE_WRAP_POSTFIX;
		Class<?> assumedClass = null;
		try {
			assumedClass = Class.forName(wireWrapClassName, true, bean.getClass().getClassLoader());
			Assert.isLegal(IWireWrap.class.isAssignableFrom(assumedClass), "Found wire wrap " + assumedClass //$NON-NLS-1$
					+ " is not of type " + IWireWrap.class); //$NON-NLS-1$
			return (Class<? extends IWireWrap>) assumedClass;
		} catch (ClassNotFoundException e) {
			Nop.reason("Fall through, attempt to load class with bundle class loader"); //$NON-NLS-1$
		}
		try {
			assumedClass = context.getBundle().loadClass(wireWrapClassName);
			Assert.isLegal(IWireWrap.class.isAssignableFrom(assumedClass), "Found wire wrap " + assumedClass //$NON-NLS-1$
					+ " is not of type " + IWireWrap.class); //$NON-NLS-1$
			return (Class<? extends IWireWrap>) assumedClass;
		} catch (ClassNotFoundException e) {
			Nop.reason("Fall through, maybe this class has no convention wire wrap class, because of generic usage."); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * For testing purposes it is sometimes necessary to change the default
	 * behavior for retrieving the wire-wrap classes. With this method it is
	 * possible to define a map that tells the {@codeWirePuller} how to find the
	 * wire-wrap classes.
	 * 
	 * @param wireWrapMocks
	 */
	public static void injectWireMocks(Map<Class<?>, Class<? extends IWireWrap>> wireWrapMocks) {
		WirePuller.wireWrapMocks = wireWrapMocks;
	}
}
