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
import org.eclipse.riena.navigation.IPresentationDefinitionService;

/**
 *
 */
public class PresentationDefinitionServiceAccessor {
	private static PresentationDefinitionServiceAccessor psa = null;
	private IPresentationDefinitionService service = null;

	/**
	 * Default Constructor
	 */
	private PresentationDefinitionServiceAccessor() {
		Inject.service(IPresentationDefinitionService.class.getName()).useRanking().into(this).andStart(
				Activator.getDefault().getContext());

	}

	static public PresentationDefinitionServiceAccessor current() {
		if (psa == null)
			return initPresentationServiceAccessor();
		return psa;
	}

	static private PresentationDefinitionServiceAccessor initPresentationServiceAccessor() {
		psa = new PresentationDefinitionServiceAccessor();

		return psa;
	}

	public IPresentationDefinitionService getPresentationDefinitionService() {

		return service;

	}

	public void bind(IPresentationDefinitionService s) {
		service = s;
	}

	public void unbind(IPresentationDefinitionService dep) {
		service = null;
	}

}
