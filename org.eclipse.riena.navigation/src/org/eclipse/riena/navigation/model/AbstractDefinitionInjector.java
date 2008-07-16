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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.navigation.Activator;
import org.eclipse.riena.navigation.IPresentationDefinition;

/**
 * Base class for riena presentation factories.
 */
public abstract class AbstractDefinitionInjector<E extends IPresentationDefinition> {

	protected ExtensionInjectionHelper<E> target = null;

	/**
	 * Injects the extension point provided by a subclass into a given target.
	 * The target returns a set of presentation definitions
	 */
	protected void init(Class<? extends IPresentationDefinition> interfaceType) {
		Inject.extension(getExtensionPointId()).useType(interfaceType).into(target).andStart(
				Activator.getDefault().getContext());
	}

	protected abstract String getExtensionPointId();

}
