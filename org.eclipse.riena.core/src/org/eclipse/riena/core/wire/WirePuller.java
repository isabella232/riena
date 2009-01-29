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

import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.internal.core.ignore.Nop;
import org.osgi.framework.BundleContext;

/**
 * The {@code WirePuler} is responsible for actual wiring of the beans.
 */
public class WirePuller {

	private Object bean;
	private String beanClassName;
	private Class<?> beanClass;
	//	private boolean serviceRegististrationRequired;
	//	private String[] serviceNames;
	//	private Dictionary<String, Object> properties;
	private Class<? extends IWireWrap> wireWrapClass;
	private IWireWrap wireWrap;

	// This is the post-fix for the wire wrap class when created by convention 
	private static final String WIRE_WRAP_POSTFIX = "WireWrap"; //$NON-NLS-1$

	/**
	 * @param beanClass
	 */
	WirePuller(Class<?> beanClass) {
		Assert.isLegal(beanClass != null, "beanClass must exist"); //$NON-NLS-1$
		this.beanClass = beanClass;
	}

	/**
	 * @param bean
	 */
	WirePuller(String beanClassName) {
		Assert.isLegal(beanClassName != null, "bean class name must exist"); //$NON-NLS-1$
		this.beanClassName = beanClassName;
	}

	/**
	 * @param bean
	 */
	WirePuller(Object bean) {
		Assert.isLegal(bean != null, "bean must exist"); //$NON-NLS-1$
		this.bean = bean;
	}

	//	/**
	//	 * Register the bean as an OSGi service under the given service names.
	//	 * 
	//	 * @param serviceNames
	//	 */
	//	public WirePuller registerAs(String... serviceNames) {
	//		Assert.isLegal(!serviceRegististrationRequired, "Service name has already been registered."); //$NON-NLS-1$
	//		Assert.isLegal(serviceNames != null, "service name must exist"); //$NON-NLS-1$
	//		serviceRegististrationRequired = true;
	//		this.serviceNames = serviceNames;
	//		return this;
	//	}
	//
	//	/**
	//	 * Register the bean as an OSGi service under the given service classes.
	//	 * 
	//	 * @param serviceClasses
	//	 */
	//	public WirePuller registerAs(Class<?>... serviceClasses) {
	//		Assert.isLegal(!serviceRegististrationRequired, "Service name has already been registered."); //$NON-NLS-1$
	//		Assert.isLegal(serviceClasses != null, "service class must exist"); //$NON-NLS-1$
	//		String[] names = new String[serviceClasses.length];
	//		for (int i = 0; i < serviceClasses.length; i++) {
	//			names[i] = serviceClasses[i].getName();
	//		}
	//		registerAs(names);
	//		return this;
	//	}
	//
	//	/**
	//	 * Use the given {@code Dictionary} for registering the bean as an OSGi
	//	 * service.
	//	 * 
	//	 * @param hashMap
	//	 * @return
	//	 */
	//	public WirePuller withProperties(Dictionary<String, Object> properties) {
	//		Assert.isLegal(properties != null, "properties must exist"); //$NON-NLS-1$
	//		this.properties = properties;
	//		return this;
	//	}

	/**
	 * Explicitly specify the wire-wrap class. An instance of this class will be
	 * used to perform the wiring of the bean. <br>
	 * <b>Note:</b>When not specifying a wire-wrap a convention for retrieving a
	 * wire-wrap will be used. The convention is to post fix the bean class name
	 * with "WireWrap" and use this class as wire-wrap. This will be done for
	 * all super classes of the bean class in order to perform proper wiring of
	 * super classes.
	 * 
	 * @param wireWrapClass
	 * @return
	 */
	public WirePuller withWireWrap(Class<? extends IWireWrap> wireWrapClass) {
		Assert.isLegal(wireWrap == null, "wire wrap instance already given"); //$NON-NLS-1$
		Assert.isLegal(wireWrapClass != null, "wireWrapClass must exist"); //$NON-NLS-1$
		this.wireWrapClass = wireWrapClass;
		return this;
	}

	/**
	 * Explicitly specify a wire-wrap instance. An instance of this class will
	 * be used to perform the wiring of the bean. <br>
	 * <b>Note:</b>When not specifying a wire-wrap a convention for retrieving a
	 * wire-wrap will be used. The convention is to post fix the bean class name
	 * with "WireWrap" and use this class as wire-wrap. This will be done for
	 * all super classes of the bean class in order to perform proper wiring of
	 * super classes.
	 * 
	 * @param wireWrap
	 * @return
	 */
	public WirePuller withWireWrap(IWireWrap wireWrap) {
		Assert.isLegal(wireWrapClass == null, "wire wrap class already given"); //$NON-NLS-1$
		Assert.isLegal(wireWrap != null, "wireWrap must exist"); //$NON-NLS-1$
		this.wireWrap = wireWrap;
		return this;
	}

	/**
	 * Start the wiring.
	 * 
	 * @param context
	 * @return the bean
	 */
	public Object andStart(BundleContext context) {
		if (beanClassName != null) {
			try {
				beanClass = context.getBundle().loadClass(beanClassName);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("Could not load class " + beanClassName, e); //$NON-NLS-1$
			}
		}
		if (beanClass != null) {
			try {
				bean = beanClass.newInstance();
			} catch (InstantiationException e) {
				throw new IllegalArgumentException("Could not instantiate beanClass " + beanClass, e); //$NON-NLS-1$
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException("Could not instantiate beanClass " + beanClass, e); //$NON-NLS-1$
			}
		}
		Assert.isLegal(bean != null, "There is no bean but it should!"); //$NON-NLS-1$

		wire(bean, bean.getClass(), wireWrap, wireWrapClass, context);
		//		if (serviceRegististrationRequired) {
		//			if (serviceNames.length == 0) {
		//				serviceNames = new String[] { bean.getClass().getName() };
		//			}
		//			context.registerService(serviceNames, bean, properties);
		//		}
		return bean;
	}

	private void wire(Object bean, Class<?> beanClass, IWireWrap wireWrap, Class<? extends IWireWrap> wireWrapClass,
			BundleContext context) {
		if (beanClass == null || beanClass == Object.class) {
			return;
		}
		if (wireWrap == null && wireWrapClass == null) {
			wireWrapClass = getConventionWireWrapClass(beanClass, context);
		}
		if (wireWrap == null) {
			wireWrap = getWireWrap(wireWrapClass);
		}
		wire(bean, beanClass.getSuperclass(), null, null, context);
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
	private Class<? extends IWireWrap> getConventionWireWrapClass(Class<?> beanClass, BundleContext context) {
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
}
