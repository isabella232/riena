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
package org.eclipse.riena.ui.filter.impl;

import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.ui.filter.IUIFilterProvider;

/**
 *
 */
@Deprecated
public final class UIFilterProviderAccessor {

	/**
	 * Default Constructor
	 */
	private UIFilterProviderAccessor() {
		// utility
	}

	/**
	 * @return
	 * @deprecated This should be replaced with
	 *             {@code
	 *             Service.get(Activator.getDefault().getContext(),
	 *             IUIFilterProvider.class);} or with
	 *             {@code With.service(..).doo(...);}
	 */
	@Deprecated
	static public IUIFilterProvider getFilterProvider() {
		return Service.get(IUIFilterProvider.class);
	}

}
