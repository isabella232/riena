/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.extension;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;

/**
 * ExtensionReader maps Extensions to Interfaces. Extension properties can then
 * be read by access the getters in the interface definition. ExtensionReader
 * instantiates generic proxies to do this.<br>
 * 
 * The ExtensionReader does not know the schema so it can only trust that the
 * extension and the interface match.
 */
public class ExtensionReader {

	/**
	 * Static method to read extensions
	 * 
	 * @param extensionPoint
	 * @param interfaceType
	 * @return
	 */
	public static <T> T[] read(String extensionPointId, Class<T> interfaceType) {
		IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
		IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(extensionPointId);
		if (extensionPoint == null)
			throw new IllegalArgumentException("Extension point " + extensionPointId + " does not exist");

		IExtension[] extensions = extensionPoint.getExtensions();
		if (extensions.length == 0)
			return (T[]) Array.newInstance(interfaceType, 0);

		List<Object> list = new ArrayList<Object>();
		for (IExtension extension : extensions)
			for (IConfigurationElement element : extension.getConfigurationElements())
				list.add(Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType },
						new InterfaceInvocationHandler(element)));

		T[] objects = (T[]) Array.newInstance(interfaceType, list.size());
		return list.toArray(objects);
	}

	/**
	 * InvocationHandler for managing proxies for the extension to interface
	 * proxy mapping
	 * 
	 */
	private static class InterfaceInvocationHandler implements InvocationHandler {

		private IConfigurationElement configurationElement;
		private static final String CREATE_METHOD_PREFIX = "create"; //$NON-NLS-1$
		private static final String IS_METHOD_PREFIX = "is"; //$NON-NLS-1$
		private static final String GETTER_METHOD_PREFIX = "get"; //$NON-NLS-1$
		private static final String[] ALLOWED_PREFIXES = { GETTER_METHOD_PREFIX, IS_METHOD_PREFIX, CREATE_METHOD_PREFIX };

		InterfaceInvocationHandler(IConfigurationElement configurationElement) {
			this.configurationElement = configurationElement;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getName().startsWith(GETTER_METHOD_PREFIX) || method.getName().startsWith(IS_METHOD_PREFIX)) {
				Class<?> returnType = method.getReturnType();
				String name = method.getName().startsWith(GETTER_METHOD_PREFIX) ? getAttributeName(method,
						GETTER_METHOD_PREFIX) : getAttributeName(method, IS_METHOD_PREFIX);

				if (returnType == String.class)
					return intercept(configurationElement.getAttribute(name));
				if (returnType.isPrimitive())
					return coerce(returnType, intercept(configurationElement.getAttribute(name)));
				if (returnType.isInterface()) {
					IConfigurationElement cfgElement = configurationElement.getChildren(name)[0];
					if (cfgElement == null) {
						return null;
					}
					return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { returnType },
							new InterfaceInvocationHandler(cfgElement));
				}
				throw new UnsupportedOperationException("property for method " + method.getName() + " not found.");
			}
			if (method.getName().startsWith(CREATE_METHOD_PREFIX)) {
				String name = getAttributeName(method, CREATE_METHOD_PREFIX);
				return configurationElement.createExecutableExtension(name);
			}
			throw new UnsupportedOperationException("Only " + Arrays.toString(ALLOWED_PREFIXES) + " are supported.");
		}

		private String getAttributeName(Method method, String prefix) {
			String name = method.getName().substring(prefix.length());
			return name.substring(0, 1).toLowerCase() + name.substring(1);
		}

		private Object coerce(Class<?> toType, String value) {
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

		private String intercept(String value) {
			// TODO Place to intercept the values with ConfigurationPlugin
			return value;
		}
	}

}
