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

import static org.eclipse.riena.core.extension.InterfaceBean.MethodKind.CREATE;
import static org.eclipse.riena.core.extension.InterfaceBean.MethodKind.GET;
import static org.eclipse.riena.core.extension.InterfaceBean.MethodKind.IS;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.logging.ConsoleLogger;
import org.osgi.framework.Bundle;
import org.osgi.service.log.LogService;

/**
 * InvocationHandler for proxies that map to configuration elements.
 */
class InterfaceBean implements InvocationHandler {

	private final Class<?> interfaceType;
	private final IConfigurationElement configurationElement;
	private final boolean symbolReplace;
	private final Map<Method, Object> resolved;
	private static final String[] ALLOWED_PREFIXES = { GET.toString(), IS.toString(), CREATE.toString() };

	private final static Logger LOGGER = new ConsoleLogger(ExtensionMapper.class.getName());

	static Object newInstance(boolean symbolReplace, Class<?> interfaceType, IConfigurationElement configurationElement) {
		return Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType }, new InterfaceBean(
				interfaceType, symbolReplace, configurationElement));
	}

	private InterfaceBean(Class<?> interfaceType, boolean symbolReplace, IConfigurationElement configurationElement) {
		this.interfaceType = interfaceType;
		this.configurationElement = configurationElement;
		this.symbolReplace = symbolReplace;
		this.resolved = new HashMap<Method, Object>();
	}

	/*
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		final MethodKind methodKind = MethodKind.of(method);
		if (methodKind == GET || methodKind == IS) {
			// those results will be cached
			synchronized (resolved) {
				Object result = resolved.get(method);
				if (result == null) {
					result = invokeNonCached(method, methodKind);
					resolved.put(method, result);
				}
				return result;
			}
		}
		if (methodKind == CREATE) {
			// obviously those will NOT be cached
			String name = getAttributeName(method, methodKind);
			if (configurationElement.getAttribute(name) == null)
				return null;
			if (method.isAnnotationPresent(CreateLazy.class))
				return LazyExecutableExtension.newInstance(configurationElement, name);
			return configurationElement.createExecutableExtension(name);
		}
		if (method.getParameterTypes().length == 0) {
			if (method.getName().equals("toString")) //$NON-NLS-1$
				return toString();
			if (method.getName().equals("hashCode")) //$NON-NLS-1$
				return hashCode();
			if (method.getName().equals("clone")) //$NON-NLS-1$
				return clone();
			if (method.getName().equals("finalize")) //$NON-NLS-1$
				finalize();
		}
		if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == Object.class
				&& method.getName().equals("equals")) //$NON-NLS-1$
			return equals(args[0]);
		throw new UnsupportedOperationException("Only " + Arrays.toString(ALLOWED_PREFIXES) + " are supported."); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private Object invokeNonCached(final Method method, final MethodKind methodKind) {
		final Class<?> returnType = method.getReturnType();
		final String name = getAttributeName(method, methodKind);
		if (returnType == String.class && name == null)
			return modify(configurationElement.getValue());
		if (returnType == String.class)
			return modify(configurationElement.getAttribute(name));
		if (returnType.isPrimitive())
			return coerce(returnType, modify(configurationElement.getAttribute(name)));
		if (returnType == Bundle.class && method.isAnnotationPresent(MapContributor.class))
			return ContributorFactoryOSGi.resolve(configurationElement.getContributor());
		if (returnType.isInterface()) {
			final IConfigurationElement[] cfgElements = configurationElement.getChildren(name);
			if (cfgElements.length == 0)
				return null;
			if (cfgElements.length == 1)
				return Proxy.newProxyInstance(returnType.getClassLoader(), new Class[] { returnType },
						new InterfaceBean(returnType, symbolReplace, cfgElements[0]));
			throw new IllegalStateException(
					"Got more than one configuration element but the interface expected exactly one, .i.e no array type has been specified for: " + method); //$NON-NLS-1$
		}
		if (returnType.isArray() && returnType.getComponentType().isInterface()) {
			final IConfigurationElement[] cfgElements = configurationElement.getChildren(name);
			final Object[] result = (Object[]) Array.newInstance(returnType.getComponentType(), cfgElements.length);
			for (int i = 0; i < cfgElements.length; i++) {
				result[i] = Proxy.newProxyInstance(returnType.getComponentType().getClassLoader(),
						new Class[] { returnType.getComponentType() }, new InterfaceBean(returnType.getComponentType(),
								symbolReplace, cfgElements[i]));
			}
			return result;
		}

		throw new UnsupportedOperationException("property for method " + method.getName() + " not found."); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Cloning a interface bean is not supported."); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		try {
			InvocationHandler handler = Proxy.getInvocationHandler(obj);
			if (handler instanceof InterfaceBean)
				return configurationElement.equals(((InterfaceBean) handler).configurationElement);
		} catch (IllegalArgumentException e) {
			// fall thru
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		// nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return configurationElement.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder bob = new StringBuilder("Dynamic proxy for "); //$NON-NLS-1$
		bob.append(interfaceType.getName()).append(':');
		final String[] names = configurationElement.getAttributeNames();
		for (String name : names) {
			bob.append(name).append('=').append(configurationElement.getAttribute(name)).append(',');
		}
		bob.setLength(bob.length() - 1);
		return bob.toString();
	}

	private String getAttributeName(final Method method, final MethodKind methodKind) {
		final Annotation annotation = method.getAnnotation(MapName.class);
		if (annotation != null)
			return ((MapName) annotation).value();
		if (method.isAnnotationPresent(MapValue.class))
			return null;

		// No annotations
		if (method.getName().equals(methodKind.prefix))
			return null;
		final String name = method.getName().substring(methodKind.prefix.length());
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	private Object coerce(final Class<?> toType, final String value) {
		if (toType == Long.TYPE)
			return Long.valueOf(value);
		if (toType == Integer.TYPE)
			return Integer.valueOf(value);
		if (toType == Boolean.TYPE)
			return Boolean.valueOf(value);
		if (toType == Float.TYPE)
			return Float.valueOf(value);
		if (toType == Double.TYPE)
			return Double.valueOf(value);
		if (toType == Short.TYPE)
			return Short.valueOf(value);
		if (toType == Character.TYPE)
			return Character.valueOf(value.charAt(0));
		if (toType == Byte.TYPE)
			return Byte.valueOf(value);
		return value;
	}

	private String modify(final String value) {
		if (!symbolReplace || value == null)
			return value;

		IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
		if (variableManager == null)
			return value;

		try {
			return variableManager.performStringSubstitution(value);
		} catch (CoreException e) {
			LOGGER.log(LogService.LOG_ERROR, "Could not perfrom string substitution for '" + value + "' .", e); //$NON-NLS-1$ //$NON-NLS-2$
			return value;
		}
	}

	enum MethodKind {
		GET("get"), IS("is"), CREATE("create"), OTHER; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		private final String prefix;

		private MethodKind(final String kind) {
			this.prefix = kind;
		}

		private MethodKind() {
			this.prefix = null;
		}

		/**
		 * @param method
		 * @return
		 */
		private static MethodKind of(final Method method) {
			final String name = method.getName();
			if (name.startsWith(GET.prefix))
				return GET;
			if (name.startsWith(IS.prefix))
				return IS;
			if (name.startsWith(CREATE.prefix))
				return CREATE;
			return OTHER;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return prefix;
		}

	}
}