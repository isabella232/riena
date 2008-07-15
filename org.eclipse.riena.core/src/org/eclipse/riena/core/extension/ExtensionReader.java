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

import static org.eclipse.riena.core.extension.ExtensionReader.InterfaceBean.MethodKind.CREATE;
import static org.eclipse.riena.core.extension.ExtensionReader.InterfaceBean.MethodKind.GET;
import static org.eclipse.riena.core.extension.ExtensionReader.InterfaceBean.MethodKind.IS;

import java.lang.annotation.Annotation;
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
 * The <code>ExtensionReader</code> maps interfaces to extensions. Extension
 * properties (attributes, sub-elements and element value) can then accessed by
 * <i>getters</i> in the interface definition.<br>
 * It is only necessary to define the interfaces for the mapping. The
 * <code>ExtensionReader</code> creates dynamic proxies for retrieving the data
 * from the <code>ExtensionRegistry</code>.<br>
 * 
 * The ExtensionReader does not evaluate the extension schema, so it can only
 * trust that the extension and the interface match.<br>
 * <br>
 * The basic rules for the mapping are:
 * <ul>
 * <li>one interface maps to one extension element type</li>
 * <li>an interface can only contain <i>getters</i> prefixed with:
 * <ul>
 * <li>get...</li>
 * <li>is...</li>
 * <li>create...</li>
 * </ul>
 * The return type, the prefix and the name of the <i>getters</i> determine the
 * mapping:
 * <ul>
 * <li>If the return type is an interface or an array of interfaces and the
 * prefix is <code>get</code> than the mapping tries to resolve to a nested
 * element or to nested elements.</li>
 * <li>If the return type is a <i>primitive</i> type or <code>String</code> and
 * the prefix is <code>get</code> than the mapping tries to resolve to an
 * attribute of the extension element enforcing type coercion.</br> The prefix
 * <code>is</code> can be used instead of the prefix <code>get</code> for
 * boolean return types.</li>
 * <li>If the prefix is <code>create</code> than the mapping tries to create an
 * new instance of the attribute´s value each time it is called.</li>
 * </ul>
 * <li>The names of the <i>getters</i> name the element names and attribute
 * names. A simple name mangling is performed, e.g for the method
 * <code>getDatabaseURL</code> the mapping looks for the name
 * <code>databaseURL</code>.<br>
 * To enforce another name mapping for a method the annotation
 * <code>@MapName("name")</code> can be used to specify the name of the element
 * or attribute.<br>
 * The extension element´s value can be retrieved by either using the method
 * <code>get()</code> or annotate a <i>getter</i> with <code>@MapValue()</code>.
 * The return type must be <code>String</code>.
 * </ul>
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
	@SuppressWarnings("unchecked")
	public static <T> T[] read(BundleContext context, String extensionPointId, Class<T> interfaceType) {
		final IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
		final IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(extensionPointId);
		if (extensionPoint == null)
			throw new IllegalArgumentException("Extension point " + extensionPointId + " does not exist");

		final IExtension[] extensions = extensionPoint.getExtensions();
		if (extensions.length == 0)
			return (T[]) Array.newInstance(interfaceType, 0);

		final List<Object> list = new ArrayList<Object>();
		for (final IExtension extension : extensions)
			for (final IConfigurationElement element : extension.getConfigurationElements())
				list.add(InterfaceBean.newInstance(context, interfaceType, element));

		return list.toArray((T[]) Array.newInstance(interfaceType, list.size()));
	}

	/**
	 * InvocationHandler for managing proxies for the extension to interface
	 * proxy mapping
	 * 
	 */
	static class InterfaceBean implements InvocationHandler {

		private final Class<?> interfaceType;
		private final IConfigurationElement configurationElement;
		private final BundleContext context;
		private final Map<Method, Object> resolved;
		private static final String[] ALLOWED_PREFIXES = { GET.toString(), IS.toString(), CREATE.toString() };
		private static final String MAGIC_KEY = "$magic$"; //$NON-NLS-1$

		static Object newInstance(BundleContext context, Class<?> interfaceType,
				IConfigurationElement configurationElement) {
			return Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType },
					new InterfaceBean(interfaceType, context, configurationElement));
		}

		private InterfaceBean(Class<?> interfaceType, BundleContext context, IConfigurationElement configurationElement) {
			this.interfaceType = interfaceType;
			this.configurationElement = configurationElement;
			this.context = context;
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
				return configurationElement.createExecutableExtension(name);
			}
			if (method.getParameterTypes().length == 0 && method.getName().equals("toString")) //$NON-NLS-1$
				return "Dynamic proxy for " + interfaceType.getName(); //$NON-NLS-1$
			throw new UnsupportedOperationException("Only " + Arrays.toString(ALLOWED_PREFIXES) + " are supported."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		private Object invokeNonCached(Method method, MethodKind methodKind) {
			final Class<?> returnType = method.getReturnType();
			final String name = getAttributeName(method, methodKind);
			if (returnType == String.class && name == null)
				return modify(configurationElement.getValue());
			if (returnType == String.class)
				return modify(configurationElement.getAttribute(name));
			if (returnType.isPrimitive())
				return coerce(returnType, modify(configurationElement.getAttribute(name)));
			if (returnType.isInterface()) {
				final IConfigurationElement[] cfgElements = configurationElement.getChildren(name);
				if (cfgElements.length == 0)
					return null;
				if (cfgElements.length == 1)
					return Proxy.newProxyInstance(returnType.getClassLoader(), new Class[] { returnType },
							new InterfaceBean(returnType, context, cfgElements[0]));
				throw new IllegalStateException(
						"Got more than one configuration element but the interface expected exactly one, .i.e no array type has been specified for: " + method); //$NON-NLS-1$
			}
			if (returnType.isArray() && returnType.getComponentType().isInterface()) {
				final IConfigurationElement[] cfgElements = configurationElement.getChildren(name);
				final Object[] result = (Object[]) Array.newInstance(returnType.getComponentType(), cfgElements.length);
				for (int i = 0; i < cfgElements.length; i++) {
					result[i] = Proxy.newProxyInstance(returnType.getComponentType().getClassLoader(),
							new Class[] { returnType.getComponentType() }, new InterfaceBean(returnType
									.getComponentType(), context, cfgElements[i]));
				}
				return result;
			}

			throw new UnsupportedOperationException("property for method " + method.getName() + " not found."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		private String getAttributeName(Method method, MethodKind methodKind) {
			Annotation annotation = method.getAnnotation(MapName.class);
			if (annotation != null)
				return ((MapName) annotation).value();
			annotation = method.getAnnotation(MapValue.class);
			if (annotation != null)
				return null;

			// No annotations
			if (method.getName().equals(methodKind.prefix))
				return null;
			String name = method.getName().substring(methodKind.prefix.length());
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

			final Dictionary<Object, Object> properties = new Hashtable<Object, Object>();
			properties.put(MAGIC_KEY, value);
			try {
				final ServiceReference[] references = context.getServiceReferences(ConfigurationPlugin.class.getName(),
						null);
				for (final ServiceReference reference : references) {
					final ConfigurationPlugin translator = (ConfigurationPlugin) context.getService(reference);
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

		enum MethodKind {
			GET("get"), IS("is"), CREATE("create"), OTHER; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			private final String prefix;

			private MethodKind(String kind) {
				this.prefix = kind;
			}

			private MethodKind() {
				this.prefix = null;
			}

			/**
			 * @param method
			 * @return
			 */
			private static MethodKind of(Method method) {
				String name = method.getName();
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
}