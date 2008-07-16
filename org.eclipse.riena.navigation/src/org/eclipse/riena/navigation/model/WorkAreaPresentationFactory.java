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

import org.eclipse.riena.navigation.IWorkAreaPresentationDefinition;

/**
 * 
 */
public class WorkAreaPresentationFactory extends AbstractWorkAreaPresentationFactory<IWorkAreaPresentationDefinition> {

	private static final String ID = "org.eclipse.riena.navigation.WorkAreaPresentation";

	public WorkAreaPresentationFactory() {

		target = new ExtensionInjectionHelper<IWorkAreaPresentationDefinition>();
		init(IWorkAreaPresentationDefinition.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.model.AbstractPresentationFactory#
	 * getExtensionPointId()
	 */
	@Override
	protected String getExtensionPointId() {
		return ID;
	}

}
