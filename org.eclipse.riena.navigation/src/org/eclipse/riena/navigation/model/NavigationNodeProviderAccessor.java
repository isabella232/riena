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

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.core.wire.WireWith;
import org.eclipse.riena.internal.navigation.Activator;
import org.eclipse.riena.navigation.INavigationNodeProvider;

/**
 * Get convenient access to the configured {@code INavigationNodeProvider}.
 */
@WireWith(NavigationNodeProviderAccessorWiring.class)
public final class NavigationNodeProviderAccessor {

	private static boolean initialized;
	private final static NavigationNodeProviderAccessor NNPA = new NavigationNodeProviderAccessor();

	private INavigationNodeProvider provider;

	/**
	 * Default Constructor
	 */
	private NavigationNodeProviderAccessor() {
		//	utility
	}

	public static INavigationNodeProvider getNavigationNodeProvider() {
		return NNPA.getProvider();
	}

	private synchronized INavigationNodeProvider getProvider() {
		if (!initialized) {
			initialize();
			initialized = true;
		}
		return provider;
	}

	private void initialize() {
		Wire.instance(this).andStart(Activator.getDefault().getContext());
	}

	/**
	 * Configure the navigation node provider to be used. If there is more than
	 * one implementation we take the one having the highest priority according
	 * to attribute 'priority'. If the priority is not specified it is assumed
	 * to be zero (the default value). All implementations sharing the same
	 * priority are considered equivalent and an arbitrary one is chosen.
	 * 
	 * @param availableExtensions
	 *            Array containing all currently available navigation node
	 *            provider implementations. This may change over time as plugins
	 *            are activated or deactivated
	 */
	public void update(INavigationNodeProviderExtension[] availableExtensions) {

		if (provider != null) {
			provider.cleanUp();
		}
		INavigationNodeProviderExtension found = null;
		int maxPriority = Integer.MIN_VALUE;

		provider = null;
		for (INavigationNodeProviderExtension probe : availableExtensions) {
			int p = probe.getPriority();
			if (found == null || p > maxPriority) {
				found = probe;
				maxPriority = p;
			}
		}

		if (found != null) {
			provider = found.createClass();
		}
	}

	@ExtensionInterface
	public interface INavigationNodeProviderExtension {

		String getId();

		int getPriority();

		INavigationNodeProvider createClass();
	}

}
