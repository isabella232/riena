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
import org.eclipse.riena.navigation.INavigationNodeProvider;

/**
 *
 */
public final class NavigationNodeProviderAccessor {
	private static NavigationNodeProviderAccessor psa = null;
	private INavigationNodeProvider service = null;

	/**
	 * Default Constructor
	 */
	private NavigationNodeProviderAccessor() {
		Inject.service(INavigationNodeProvider.class.getName()).useRanking().into(this).andStart(
				Activator.getDefault().getContext());

	}

	static public NavigationNodeProviderAccessor current() {
		if (psa == null) {
			return initNavigationNodeProviderAccessor();
		}
		return psa;
	}

	static private NavigationNodeProviderAccessor initNavigationNodeProviderAccessor() {
		psa = new NavigationNodeProviderAccessor();

		return psa;
	}

	public INavigationNodeProvider getNavigationNodeProvider() {

		return service;

	}

	public void bind(INavigationNodeProvider s) {
		service = s;
	}

	public void unbind(INavigationNodeProvider dep) {
		service.cleanUp();
		service = null;
	}

}
