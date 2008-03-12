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
package org.eclipse.riena.core.extension.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;

/**
 * ExtensionUtility maps Extensions to Interfaces. Extension properties can then
 * be read by access the getters in the interface definition. ExtensionUtility
 * instantiates generic proxies to do this.
 * 
 * The ExtensionUtility does not know the schema so it can only trust that the
 * extension and the interface match.
 * 
 */
@Deprecated
public class ExtensionUtility {

	/**
	 * Static method to read extensions
	 * 
	 * @param extensionPoint
	 * @param interf
	 * @return
	 */
	@Deprecated
	public static <T> T[] readExtensions(String extensionPoint, Class<T> interf) {
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		IExtensionPoint extPoint = registry.getExtensionPoint(extensionPoint);
		if (extPoint == null) {
			throw new IllegalArgumentException("extension point " + extensionPoint + " does not exist");
		}
		IExtension[] cfgExtensions = extPoint.getExtensions();
		if (cfgExtensions.length == 0) {
			return (T[]) Array.newInstance(interf, 0);
		}
		ArrayList<Object> list = new ArrayList<Object>();
		for (IExtension cfgExt : cfgExtensions) {
			for (int x = 0; x < cfgExt.getConfigurationElements().length; x++) {
				list.add(Proxy.newProxyInstance(interf.getClassLoader(), new Class[] { interf },
						new InterfaceInvocationHandler(cfgExt.getConfigurationElements()[x], interf)));
			}
		}

		T[] objects = (T[]) Array.newInstance(interf, list.size());
		list.toArray(objects);
		return objects;
	}

	/**
	 * InvocationHandler for managing proxies for the extension to interface
	 * proxy mapping
	 * 
	 */
	private static class InterfaceInvocationHandler implements InvocationHandler {

		private IConfigurationElement config;
		private Class<?> interf;

		InterfaceInvocationHandler(IConfigurationElement config, Class<?> interf) {
			this.config = config;
			this.interf = interf;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
				Method[] meths = interf.getMethods();
				for (Method meth : meths) {
					if (meth.getName().equals(method.getName())) {
						Class<?> retType = meth.getReturnType();
						String name;
						if (method.getName().startsWith("get")) {
							name = method.getName().substring("get".length());
						} else {
							name = method.getName().substring("is".length());
						}
						name = name.substring(0, 1).toLowerCase() + name.substring(1);
						if (retType.equals(String.class)) {
							return config.getAttribute(name);
						} else if (retType.equals(Boolean.TYPE)) {
							return Boolean.valueOf(config.getAttribute(name));
						} else if (retType.isInterface()) {
							IConfigurationElement cfgElement = config.getChildren(name)[0];
							if (cfgElement == null) {
								return null;
							}
							return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { retType },
									new InterfaceInvocationHandler(cfgElement, retType));
						}
					}
				}
				throw new UnsupportedOperationException("property for method " + method.getName() + " not found.");
			}
			if (method.getName().startsWith("create")) {
				String name = method.getName().substring("create".length());
				name = name.substring(0, 1).toLowerCase() + name.substring(1);
				return config.createExecutableExtension(name);
			}
			throw new UnsupportedOperationException("only getXXX, isXXX or createXXX is supported");
		}
	}

}
