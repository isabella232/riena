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

import org.eclipse.riena.core.util.ServiceAccessor;
import org.eclipse.riena.core.wire.WireWith;
import org.eclipse.riena.internal.core.ignore.Nop;
import org.eclipse.riena.internal.navigation.Activator;
import org.eclipse.riena.navigation.INavigationNodeProvider;

/**
 *
 */
@WireWith(NavigationNodeProviderAccessorWiring.class)
public final class NavigationNodeProviderAccessor extends ServiceAccessor<INavigationNodeProvider> {

	private final static NavigationNodeProviderAccessor NNPA = new NavigationNodeProviderAccessor();

	/**
	 * Default Constructor
	 */
	private NavigationNodeProviderAccessor() {
		super(Activator.getDefault().getContext(), new ServiceAccessor.IBindHook<INavigationNodeProvider>() {

			public void onBind(INavigationNodeProvider service) {
				Nop.reason("No interest!"); //$NON-NLS-1$
			}

			public void onUnbind(INavigationNodeProvider service) {
				// TODO Is this a good idea to let the clean-up be done here! Shouldn´t the service cleaned-up when it is going done? 
				service.cleanUp();
			}
		});

	}

	public static INavigationNodeProvider getNavigationNodeProvider() {
		return NNPA.getService();
	}

}
