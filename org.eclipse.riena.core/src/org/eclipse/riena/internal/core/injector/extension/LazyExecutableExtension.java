/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.core.injector.extension;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.core.Activator;

/**
 * An invocation handler that lazily creates an executable extension on demand.
 */
final class LazyExecutableExtension implements InvocationHandler {

	private final IConfigurationElement configurationElement;
	private final String attributeName;
	private final boolean wire;
	private volatile Object delegate;

	/**
	 * @param configurationElement
	 * @param attributeName
	 * @param wire
	 * @return
	 */
	static Object newInstance(final IConfigurationElement configurationElement, final String attributeName,
			final boolean wire) {
		final String className = configurationElement.getAttribute(attributeName);
		if (className == null) {
			return null;
		}
		final Bundle bundle = ContributorFactoryOSGi.resolve(configurationElement.getContributor());
		if (bundle == null) {
			throw new IllegalStateException("Could not resolve bundle for configuration element " //$NON-NLS-1$
					+ configurationElement.getName());
		}
		try {
			final Class<?> clazz = bundle.loadClass(className);
			final Class<?>[] interfaces = clazz.getInterfaces();
			if (interfaces.length == 0) {
				throw new IllegalStateException("Executable extension " + className + " within configuration element " //$NON-NLS-1$ //$NON-NLS-2$
						+ configurationElement.getName() + " does not have any interfaces, but they are required."); //$NON-NLS-1$
			}
			return Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, new LazyExecutableExtension(
					configurationElement, attributeName, wire));
		} catch (final ClassNotFoundException e) {
			throw new IllegalStateException("Could not load class " + className + " from bundle " //$NON-NLS-1$ //$NON-NLS-2$
					+ bundle.getSymbolicName(), e);
		}
	}

	private LazyExecutableExtension(final IConfigurationElement configurationElement, final String attributeName,
			final boolean wire) {
		this.configurationElement = configurationElement;
		this.attributeName = attributeName;
		this.wire = wire;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		synchronized (this) {
			if (delegate == null) {
				delegate = configurationElement.createExecutableExtension(attributeName);
				if (wire) {
					// Try wiring the created executable extension
					final Bundle bundle = ContributorFactoryOSGi.resolve(configurationElement.getContributor());
					final BundleContext context = bundle != null ? bundle.getBundleContext() : Activator.getDefault()
							.getContext();
					Wire.instance(delegate).andStart(context);
				}
			}
		}
		return method.invoke(delegate, args);
	}

}
