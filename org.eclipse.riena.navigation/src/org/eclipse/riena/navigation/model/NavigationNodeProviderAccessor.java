/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.navigation.INavigationNodeProvider;

/**
 * Get convenient access to the configured {@code INavigationNodeProvider}.
 * 
 * @deprecated use instead {@code NavigationNodeProvider.getInstance()}
 */
@Deprecated
public final class NavigationNodeProviderAccessor {

	/**
	 * Default Constructor
	 */
	private NavigationNodeProviderAccessor() {
		//	utility
	}

	/**
	 * @return
	 * @deprecated use instead {@code NavigationNodeProvider.getInstance()}
	 */
	@Deprecated
	public static INavigationNodeProvider getNavigationNodeProvider() {
		return NavigationNodeProvider.getInstance();
	}

}
