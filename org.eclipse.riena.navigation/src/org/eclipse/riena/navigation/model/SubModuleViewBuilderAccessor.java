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
import org.eclipse.riena.navigation.ISubModuleViewBuilder;

/**
 *
 */
public final class SubModuleViewBuilderAccessor {
	private static SubModuleViewBuilderAccessor psa = null;
	private ISubModuleViewBuilder service = null;

	/**
	 * Default Constructor
	 */
	private SubModuleViewBuilderAccessor() {
		Inject.service(ISubModuleViewBuilder.class.getName()).useRanking().into(this).andStart(
				Activator.getDefault().getContext());

	}

	static public SubModuleViewBuilderAccessor current() {
		if (psa == null) {
			return initSubmoduleViewBuilderAccessor();
		}
		return psa;
	}

	static private SubModuleViewBuilderAccessor initSubmoduleViewBuilderAccessor() {
		psa = new SubModuleViewBuilderAccessor();

		return psa;
	}

	public ISubModuleViewBuilder getSubModuleViewBuilder() {

		return service;

	}

	public void bind(ISubModuleViewBuilder s) {
		service = s;
	}

	public void unbind(ISubModuleViewBuilder dep) {
		service.cleanUp();
		service = null;
	}

}
