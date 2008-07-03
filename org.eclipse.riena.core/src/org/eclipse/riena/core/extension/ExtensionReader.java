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
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.logging.ConsoleLogger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationPlugin;
import org.osgi.service.log.LogService;

/**
 * ExtensionReader maps Extensions to Interfaces. Extension properties can then
 * be read by access the getters in the interface definition. ExtensionReader
 * instantiates generic proxies to do this.<br>
 * 
 * The ExtensionReader does not know the schema so it can only trust that the
 * extension and the interface match.
 */
public class ExtensionReader {

	private final static Logger LOGGER = new ConsoleLogger(ExtensionReader.class.getName());

	/**
	 * Static method to read extensions
	 * 
	 * @param <T>
	 * @param context
	 *            if not null, symbol replacement occurs (ConfigurationPlugin)
	 * @param extensionPointId
	 * @param interfaceType
	 * @return
	 */
	public static <T> T[] read(BundleContext context, String extensionPointId, Class<T> interfaceType) {
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
				list.add(InterfaceBean.newInstance(context, interfaceType, element));

		T[] objects = (T[]) Array.newInstance(interfaceType, list.size());
		return list.toArray(objects);
	}

	/**
	 * InvocationHandler for managing proxies for the extension to interface
	 * proxy mapping
	 * 
	 */
	private static class InterfaceBean implements InvocationHandler {

		private IConfigurationElement configurationElement;
		private BundleContext context;
		private Map<Method, Object> resolved;
		private static final String CREATE_METHOD_PREFIX = "create"; //$NON-NLS-1$
		private static final String IS_METHOD_PREFIX = "is"; //$NON-NLS-1$
		private static final String GETTER_METHOD_PREFIX = "get"; //$NON-NLS-1$
		private static final String[] ALLOWED_PREFIXES = { GETTER_METHOD_PREFIX, IS_METHOD_PREFIX, CREATE_METHOD_PREFIX };
		private static final String MAGIC_KEY = "$magic$"; //$NON-NLS-1$

		static Object newInstance(BundleContext context, Class<?> interfaceType,
				IConfigurationElement configurationElement) {
			return Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType },
					new InterfaceBean(context, configurationElement));
		}

		InterfaceBean(BundleContext context, IConfigurationElement configurationElement) {
			this.configurationElement = configurationElement;
			this.context = context;
			this.resolved = new HashMap<Method, Object>();
		}

		/*
		 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
		 * java.lang.reflect.Method, java.lang.Object[])
		 */
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getName().startsWith(GETTER_METHOD_PREFIX) || method.getName().startsWith(IS_METHOD_PREFIX)) {
				// those results will be cached
				synchronized (resolved) {
					Object result = resolved.get(method);
					if (result == null) {
						result = invokeNonCached(method);
						resolved.put(method, result);
					}
					return result;
				}
			}
			if (method.getName().startsWith(CREATE_METHOD_PREFIX)) {
				// obviously those will NOT be cached
				String name = getAttributeName(method, CREATE_METHOD_PREFIX);
				return configurationElement.createExecutableExtension(name);
			}
			throw new UnsupportedOperationException("Only " + Arrays.toString(ALLOWED_PREFIXES) + " are supported."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		private Object invokeNonCached(Method method) {
			Class<?> returnType = method.getReturnType();
			String name = getAttributeName(method,
					method.getName().startsWith(GETTER_METHOD_PREFIX) ? GETTER_METHOD_PREFIX : IS_METHOD_PREFIX);
			if (returnType == String.class)
				return modify(configurationElement.getAttribute(name));
			if (returnType.isPrimitive())
				return coerce(returnType, modify(configurationElement.getAttribute(name)));
			if (returnType.isInterface()) {
				IConfigurationElement[] cfgElements = configurationElement.getChildren(name);
				if (cfgElements.length == 0)
					return null;
				if (cfgElements.length == 1)
					return Proxy.newProxyInstance(returnType.getClassLoader(), new Class[] { returnType },
							new InterfaceBean(context, cfgElements[0]));
				throw new IllegalStateException(
						"Got more than one configuration element but the interface expected exactly one, .i.e no array type has been specified for: " + method); //$NON-NLS-1$
			}
			if (returnType.isArray() && returnType.getComponentType().isInterface()) {
				IConfigurationElement[] cfgElements = configurationElement.getChildren(name);
				Object[] result = (Object[]) Array.newInstance(returnType.getComponentType(), cfgElements.length);
				for (int i = 0; i < cfgElements.length; i++) {
					result[i] = Proxy.newProxyInstance(returnType.getComponentType().getClassLoader(),
							new Class[] { returnType.getComponentType() }, new InterfaceBean(context, cfgElements[i]));
				}
				return result;
			}

			throw new UnsupportedOperationException("property for method " + method.getName() + " not found."); //$NON-NLS-1$ //$NON-NLS-2$
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

		private String modify(String value) {
			if (context == null || value == null)
				return value;

			Dictionary<Object, Object> properties = new Hashtable<Object, Object>();
			properties.put(MAGIC_KEY, value);
			try {
				ServiceReference[] references = context.getServiceReferences(ConfigurationPlugin.class.getName(), null);
				for (ServiceReference reference : references) {
					ConfigurationPlugin translator = (ConfigurationPlugin) context.getService(reference);
					if (translator == null)
						continue;
					try {
						translator.modifyConfiguration(null, properties);
					} catch (Throwable throwable) {
						LOGGER.log(LogService.LOG_ERROR, "Configuration plugin " + reference //$NON-NLS-1$
								+ " failed when modifying " + properties + ".", throwable); //$NON-NLS-1$ //$NON-NLS-2$
					}
					context.ungetService(reference);
				}
			} catch (InvalidSyntaxException e) {
				// Should never occur because no filter is set.
			}
			return (String) properties.get(MAGIC_KEY);
		}
	}

}
