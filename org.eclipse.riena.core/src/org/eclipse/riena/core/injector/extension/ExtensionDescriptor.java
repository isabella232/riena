/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.injector.extension;

import org.osgi.framework.BundleContext;

import org.eclipse.core.runtime.Assert;

/**
 * ExtensionDescriptor and ExtensionInjector simplify locating configuration
 * (extensions) and injects them into a target object. To do so the
 * ExtensionInjector tracks the extension registry for changes of appearing and
 * disappearing extensions and injects them into the target. A target object
 * defines a named and typed bind (update) method. The ExtensionInjector calls
 * the update method when the specified extension/point was registered or
 * modified.<br>
 * The type of the update method can be either of type array of <i>interface
 * type</i> or just <i>interface type</i>. The <i>interface type</i> is just a
 * simple java interface with <i>getters</i> where their name corresponds to
 * attribute names or element names for nested extensions of an extension.<br>
 * For a detailed description of the interface/extension mapping
 * {@link ExtensionMapper}
 * <p>
 * The extension injector starts tracking the specified extension with
 * {@link #andStart(BundleContext)}. It stops tracking with {@link #stop()}.<br>
 * Configuration modifications as defined by <code>ConfigurationPlugin</code>
 * will also be performed.
 * <p>
 * The ExtensionDescriptor and ExtensionInjector are implemented as a ´fluent
 * interface´ allowing constructs like:
 * <ol>
 * <li>Inject.extension("id1").into(target).andStart(context)</li>
 * <li>
 * Inject.extension("id2").useType(interface).into(target).update("configure").
 * andStart(context)</li>
 * <li>Inject.extension("id3").expectExactly(1).into(target).andStart(context)</li>
 * <li>..</li>
 * </ol>
 * <p>
 * This fluent interface makes a few assumptions (defaults) that makes writing
 * extension injectors short and expressive , e.g. item one the list, means try
 * to retrieve <i>interface type</i> by reflection, expect zero to ´unbound´
 * occurrences of extensions and the update method name is "update".
 * <p>
 * The expected cardinality of extensions (min/max occurrences) can be specified
 * with <code>expectingMinMax()</code> or with <code>expectingExactly()</code>.
 */
public class ExtensionDescriptor {

	private final String extensionPointId;
	private boolean homogeneous = true;
	private Class<?> interfaceType;
	private int minOccurences = 0;
	private int maxOccurences = UNBOUNDED;

	public static final int UNBOUNDED = Integer.MAX_VALUE;

	/**
	 * Create an extension descriptor for the given extension point id.
	 * 
	 * @param extensionPointId
	 */
	public ExtensionDescriptor(final String extensionPointId) {
		Assert.isNotNull(extensionPointId, "The extension id must not be null."); //$NON-NLS-1$
		this.extensionPointId = extensionPointId;
	}

	/**
	 * Define the interface type used as a bean for injecting. <br>
	 * If not defined the extension injector tries to figure it out by
	 * reflection and the name of the ´update´ method.
	 * 
	 * @param interfaceType
	 * @return itself
	 */
	public ExtensionDescriptor useType(final Class<?> interfaceType) {
		Assert.isNotNull(interfaceType, "Interface type must not be null."); //$NON-NLS-1$
		Assert.isTrue(interfaceType.isInterface(), "Interface type must be an interface."); //$NON-NLS-1$
		Assert.isTrue(this.interfaceType == null, "Interface type has already been set."); //$NON-NLS-1$
		this.interfaceType = interfaceType;
		return this;
	}

	/**
	 * Set that the extension is heterogeneous.<br>
	 * A homogeneous extension (that is the default) is an extension that only
	 * has sub-elements of the same element type. A heterogeneous extension is
	 * an extension that has sub-elements of various types.
	 * 
	 * @return itself
	 */
	public ExtensionDescriptor heterogeneous() {
		homogeneous = false;
		return this;
	}

	/**
	 * Inject the ´extensions´ into the specified target object.
	 * 
	 * @param target
	 * @return itself
	 */
	public ExtensionInjector into(final Object target) {
		Assert.isNotNull(target, "The target must not be null."); //$NON-NLS-1$
		return new ExtensionInjector(this, target);
	}

	/**
	 * Defines the minimum and the maximum occurrences of extensions allowed for
	 * the extension point. These values should correspond to the definitions
	 * within the extension point schema.<br>
	 * Future implementations might retrieve these values from the schema.
	 * 
	 * @param min
	 * @param max
	 * @return itself
	 */
	public ExtensionDescriptor expectingMinMax(final int min, final int max) {
		Assert.isLegal(max >= min, "min must not be greater than max."); //$NON-NLS-1$
		Assert.isLegal(min >= 0, "min must be greater or equal than zero."); //$NON-NLS-1$
		Assert.isLegal(max > 0, "max must be greater than zero."); //$NON-NLS-1$
		this.minOccurences = min;
		this.maxOccurences = max;
		return this;
	}

	/**
	 * Defines that the minimum and the maximum occurrences of extensions is the
	 * same.
	 * 
	 * @param exactly
	 * @return itself
	 */
	public ExtensionDescriptor expectingExactly(final int exactly) {
		return expectingMinMax(exactly, exactly);
	}

	/**
	 * @return the extension point id
	 */
	String getExtensionPointId() {
		return extensionPointId;
	}

	/**
	 * @return the interfaceType
	 */
	Class<?> getInterfaceType() {
		return interfaceType;
	}

	/**
	 * @return the minOccurences
	 */
	int getMinOccurences() {
		return minOccurences;
	}

	/**
	 * @return the maxOccurences
	 */
	int getMaxOccurences() {
		return maxOccurences;
	}

	/**
	 * @return the form
	 */
	boolean isHomogeneous() {
		return homogeneous;
	}

	/**
	 * @return
	 */
	boolean requiresArrayUpdateMethod() {
		return minOccurences > 1 || maxOccurences > 1;
	}
}
