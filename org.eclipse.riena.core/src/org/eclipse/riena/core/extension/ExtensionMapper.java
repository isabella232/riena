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
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.RegistryFactory;

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

	private ExtensionMapper() {
		throw new IllegalStateException("Should never be invoked!"); //$NON-NLS-1$
	}

	/**
	 * Static method to read extensions
	 * 
	 * @param <T>
	 * @param symbolReplace
	 *            on true symbol replacement occurs (via
	 *            <code>StringVariableManager</code>)
	 * @param extensionDesc
	 * @param componentType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] map(final boolean symbolReplace, final ExtensionDescriptor extensionDesc,
			final Class<T> componentType, boolean nonSpecific) {
		final IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
		final IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(extensionDesc.getExtensionPointId());
		if (extensionPoint == null)
			throw new IllegalArgumentException("Extension point " + extensionDesc.getExtensionPointId()
					+ " does not exist");
		final IExtension[] extensions = extensionPoint.getExtensions();
		if (extensions.length == 0)
			return (T[]) Array.newInstance(componentType, 0);

		final List<Object> list = new ArrayList<Object>();
		if (nonSpecific) {
			if (extensionDesc.isHomogeneous())
				for (final IConfigurationElement element : extensionPoint.getConfigurationElements())
					list.add(InterfaceBean.newInstance(symbolReplace, componentType, element));
			else
				list.add(InterfaceBean.newInstance(symbolReplace, componentType, new Wrapper(extensionPoint)));
		} else
			for (IExtension extension : extensions)
				list.add(InterfaceBean.newInstance(symbolReplace, componentType, new Wrapper(extension)));

		return list.toArray((T[]) Array.newInstance(componentType, list.size()));
	}

	/**
	 * Wrap an IExtension or an IExtensionPoint so that it behaves almost like a
	 * IConfigurationElement.
	 */
	private static class Wrapper implements IConfigurationElement {

		private final IExtensionPoint extensionPoint;
		private final IExtension extension;

		private Wrapper(final IExtensionPoint extensionPoint) {
			Assert.isNotNull(extensionPoint, "extensionPoint must not be null.");
			this.extensionPoint = extensionPoint;
			this.extension = null;
		}

		private Wrapper(final IExtension extension) {
			Assert.isNotNull(extension, "extension must not be null.");
			this.extension = extension;
			this.extensionPoint = null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.runtime.IConfigurationElement#createExecutableExtension
		 * (java.lang.String)
		 */
		public Object createExecutableExtension(String propertyName) throws CoreException {
			throw new UnsupportedOperationException("IExtension/Point does not support createExecutableExtension()");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.runtime.IConfigurationElement#getAttribute(java.
		 * lang.String)
		 */
		public String getAttribute(String name) throws InvalidRegistryObjectException {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.runtime.IConfigurationElement#getAttributeAsIs(java
		 * .lang.String)
		 */
		public String getAttributeAsIs(String name) throws InvalidRegistryObjectException {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.runtime.IConfigurationElement#getAttributeNames()
		 */
		public String[] getAttributeNames() throws InvalidRegistryObjectException {
			return new String[0];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IConfigurationElement#getChildren()
		 */
		public IConfigurationElement[] getChildren() throws InvalidRegistryObjectException {
			if (extension != null)
				return extension.getConfigurationElements();

			List<IConfigurationElement> elements = new ArrayList<IConfigurationElement>();
			for (final IExtension extension : extensionPoint.getExtensions())
				elements.addAll(Arrays.asList(extension.getConfigurationElements()));

			return elements.toArray(new IConfigurationElement[elements.size()]);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.runtime.IConfigurationElement#getChildren(java.lang
		 * .String)
		 */
		public IConfigurationElement[] getChildren(String name) throws InvalidRegistryObjectException {
			final IConfigurationElement[] configurationElements = extension != null ? extension
					.getConfigurationElements() : extensionPoint.getConfigurationElements();

			final List<IConfigurationElement> elements = new ArrayList<IConfigurationElement>();
			for (final IConfigurationElement configurationElement : configurationElements)
				if (configurationElement.getName().equals(name))
					elements.add(configurationElement);

			return elements.toArray(new IConfigurationElement[elements.size()]);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IConfigurationElement#getContributor()
		 */
		public IContributor getContributor() throws InvalidRegistryObjectException {
			return extension != null ? extension.getContributor() : extensionPoint.getContributor();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.runtime.IConfigurationElement#getDeclaringExtension
		 * ()
		 */
		public IExtension getDeclaringExtension() throws InvalidRegistryObjectException {
			throw new UnsupportedOperationException("IExtensionPoint does not support getDeclaringExtension()");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IConfigurationElement#getName()
		 */
		public String getName() throws InvalidRegistryObjectException {
			return extension != null ? extension.getLabel() : extensionPoint.getLabel();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IConfigurationElement#getNamespace()
		 */
		public String getNamespace() throws InvalidRegistryObjectException {
			return extension != null ? extension.getNamespace() : extensionPoint.getNamespace();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.runtime.IConfigurationElement#getNamespaceIdentifier
		 * ()
		 */
		public String getNamespaceIdentifier() throws InvalidRegistryObjectException {
			return extension != null ? extension.getNamespaceIdentifier() : extensionPoint.getNamespaceIdentifier();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IConfigurationElement#getParent()
		 */
		public Object getParent() throws InvalidRegistryObjectException {
			throw new UnsupportedOperationException("IExtensionPoint does not support getParent()");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IConfigurationElement#getValue()
		 */
		public String getValue() throws InvalidRegistryObjectException {
			throw new UnsupportedOperationException("IExtensionPoint does not support getValue()");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IConfigurationElement#getValueAsIs()
		 */
		public String getValueAsIs() throws InvalidRegistryObjectException {
			throw new UnsupportedOperationException("IExtensionPoint does not support getValueAsIs()");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IConfigurationElement#isValid()
		 */
		public boolean isValid() {
			return extension != null ? extension.isValid() : extensionPoint.isValid();
		}

	}

}