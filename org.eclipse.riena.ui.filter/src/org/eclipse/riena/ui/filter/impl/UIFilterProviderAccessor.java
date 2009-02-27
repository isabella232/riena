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
package org.eclipse.riena.ui.filter.impl;

import org.eclipse.riena.core.util.ServiceAccessor;
import org.eclipse.riena.core.wire.WireWith;
import org.eclipse.riena.internal.ui.filter.Activator;
import org.eclipse.riena.ui.filter.IUIFilterProvider;

/**
 *
 */
@WireWith(UIFilterProviderAccessorWiring.class)
public final class UIFilterProviderAccessor extends ServiceAccessor<UIFilterProvider> {

	private final static UIFilterProviderAccessor FILTER_PROVIDER_ACCESSOR = new UIFilterProviderAccessor();

	/**
	 * Default Constructor
	 */
	private UIFilterProviderAccessor() {
		super(Activator.getDefault().getContext());
	}

	static public IUIFilterProvider getFilterProvider() {
		return FILTER_PROVIDER_ACCESSOR.getService();
	}

}
