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

import org.eclipse.core.runtime.Assert;

/**
 * 
 */
public class ExtensionId {

	private String extensionPointId;
	private Class<?> interfaceType;
	private int minOccurences = 0;
	private int maxOccurences = UNBOUNDED;

	public static final int UNBOUNDED = Integer.MAX_VALUE;

	/**
	 * @param extensionPointId
	 * @param interfaceType
	 */
	public ExtensionId(String extensionPointId, Class<?> interfaceType) {
		Assert.isNotNull(extensionPointId, "The extension id must not be null."); //$NON-NLS-1$
		this.extensionPointId = extensionPointId;
		this.interfaceType = interfaceType;
	}

	/**
	 * @param extension
	 */
	public ExtensionId(String extension) {
		this(extension, null);
	}

	/**
	 * @param target
	 */
	public ExtensionInjector injectInto(Object target) {
		Assert.isNotNull(target, "The target must not be null."); //$NON-NLS-1$
		return new ExtensionInjector(this, target);
	}

	/**
	 * @param min
	 * @param max
	 * @return
	 */
	public ExtensionId expectingMinMax(int min, int max) {
		Assert.isLegal(max >= min, "min must not be greater than max.");
		Assert.isLegal(min >= 0, "min must be greater or equal than zero.");
		Assert.isLegal(max > 0, "min must be greater than zero.");
		this.minOccurences = min;
		this.maxOccurences = max;
		return this;
	}

	/**
	 * @param exactly
	 * @return
	 */
	public ExtensionId expectingExactly(int exactly) {
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

	boolean requiresArrayUpdateMethod() {
		return minOccurences > 1 || maxOccurences > 1;
	}
}
