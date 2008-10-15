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
package org.eclipse.riena.ui.filter.impl;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.ui.filter.IUIFilterProvider;
import org.eclipse.riena.ui.internal.Activator;

/**
 *
 */
public final class UIFilterProviderAccessor {
	private static UIFilterProviderAccessor psa = null;
	private IUIFilterProvider service = null;

	/**
	 * Default Constructor
	 */
	private UIFilterProviderAccessor() {
		Inject.service(IUIFilterProvider.class.getName()).useRanking().into(this).andStart(
				Activator.getDefault().getContext());

	}

	static public UIFilterProviderAccessor current() {
		if (psa == null) {
			return initUIFilterProviderAccessor();
		}
		return psa;
	}

	static private UIFilterProviderAccessor initUIFilterProviderAccessor() {
		psa = new UIFilterProviderAccessor();

		return psa;
	}

	public IUIFilterProvider getUIFilterProvider() {

		return service;

	}

	public void bind(IUIFilterProvider s) {
		service = s;
	}

	public void unbind(IUIFilterProvider dep) {

		service = null;
	}

}
