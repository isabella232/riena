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
package org.eclipse.riena.core.extension;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.IConfigurationElement;
import org.osgi.framework.Bundle;

/**
 * An invocation handler that lazily creates an executable extension on demand.
 */
class LazyExecutableExtension implements InvocationHandler {

	private final IConfigurationElement configurationElement;
	private final String name;
	private Object delegate;

	/**
	 * @param configurationElement
	 * @param name
	 * @return
	 */
	static Object newInstance(final IConfigurationElement configurationElement, final String name) {
		final String className = configurationElement.getAttribute(name);
		if (className == null)
			return null;
		final Bundle bundle = ContributorFactoryOSGi.resolve(configurationElement.getContributor());
		if (bundle == null)
			throw new IllegalStateException("Could not resolve bundle for configuration element " //$NON-NLS-1$
					+ configurationElement.getName());
		try {
			final Class<?> clazz = bundle.loadClass(className);
			final Class<?>[] interfaces = clazz.getInterfaces();
			if (interfaces.length == 0)
				throw new IllegalStateException("Executable extension " + className + " within configuration element " //$NON-NLS-1$ //$NON-NLS-2$
						+ configurationElement.getName() + " does not have any interfaces, but they are required."); //$NON-NLS-1$
			return Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, new LazyExecutableExtension(
					configurationElement, name));
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Could not load class " + className + " from bundle " //$NON-NLS-1$ //$NON-NLS-2$
					+ bundle.getSymbolicName(), e);
		}
	}

	private LazyExecutableExtension(final IConfigurationElement configurationElement, final String name) {
		this.configurationElement = configurationElement;
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		synchronized (this) {
			if (delegate == null)
				delegate = configurationElement.createExecutableExtension(name);
		}
		return method.invoke(delegate, args);
	}

}
