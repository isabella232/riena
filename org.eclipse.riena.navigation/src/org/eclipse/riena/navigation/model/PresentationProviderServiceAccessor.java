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
import org.eclipse.riena.navigation.IPresentationProviderService;

/**
 *
 */
public class PresentationProviderServiceAccessor {
	private static PresentationProviderServiceAccessor psa = null;
	private IPresentationProviderService service = null;

	/**
	 * Default Constructor
	 */
	private PresentationProviderServiceAccessor() {
		Inject.service(IPresentationProviderService.class.getName()).useRanking().into(this).andStart(
				Activator.getDefault().getContext());

	}

	static public PresentationProviderServiceAccessor current() {
		if (psa == null)
			return initPresentationServiceAccessor();
		return psa;
	}

	static private PresentationProviderServiceAccessor initPresentationServiceAccessor() {
		psa = new PresentationProviderServiceAccessor();

		return psa;
	}

	public IPresentationProviderService getPresentationDefinitionService() {

		return service;

	}

	public void bind(IPresentationProviderService s) {
		service = s;
	}

	public void unbind(IPresentationProviderService dep) {
		service.cleanUp();
		service = null;
	}

}
