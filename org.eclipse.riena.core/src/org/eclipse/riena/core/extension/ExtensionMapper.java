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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.osgi.framework.BundleContext;

/**
 * The <code>ExtensionMapper</code> maps interfaces to extensions. Extension
 * properties (attributes, sub-elements and element value) can then accessed by
 * <i>getters</i> in the interface definition.<br>
 * It is only necessary to define the interfaces for the mapping. The
 * <code>ExtensionMapper</code> creates dynamic proxies for retrieving the data
 * from the <code>ExtensionRegistry</code>.<br>
 * 
 * The ExtensionMapper does not evaluate the extension schema, so it can only
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
 * new instance of the attribute´s value each time it is called. If the
 * extension attribute is not defined <code>null</code> will be returned.</li>
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
public class ExtensionMapper {

	// private final static Logger LOGGER = new
	// ConsoleLogger(ExtensionMapper.class.getName());

	/**
	 * Static method to read extensions
	 * 
	 * @param <T>
	 * @param context
	 *            if given, symbol replacement occurs (via ConfigurationPlugin)
	 * @param extensionDesc
	 * @param componentType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] read(BundleContext context, ExtensionDescriptor extensionDesc, Class<?> componentType) {
		final IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
		final IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(extensionDesc.getExtensionPointId());
		if (extensionPoint == null)
			throw new IllegalArgumentException("Extension point " + extensionDesc.getExtensionPointId()
					+ " does not exist");

		final IExtension[] extensions = extensionPoint.getExtensions();
		if (extensions.length == 0)
			return (T[]) Array.newInstance(componentType, 0);

		final List<Object> list = new ArrayList<Object>();
		for (final IExtension extension : extensions)
			for (final IConfigurationElement element : extension.getConfigurationElements())
				list.add(InterfaceBean.newInstance(context, componentType, element));

		return list.toArray((T[]) Array.newInstance(componentType, list.size()));
	}

}