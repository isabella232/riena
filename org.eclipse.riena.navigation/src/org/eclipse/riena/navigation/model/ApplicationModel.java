/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.internal.navigation.Activator;
import org.eclipse.riena.navigation.IApplicationModel;
import org.eclipse.riena.navigation.IApplicationModelListener;
import org.eclipse.riena.navigation.IPresentationDefinitionService;
import org.eclipse.riena.navigation.ISubApplication;
import org.osgi.framework.ServiceReference;

/**
 * Default implementation for the ApplicationModel
 */
public class ApplicationModel extends NavigationNode<IApplicationModel, ISubApplication, IApplicationModelListener>
		implements IApplicationModel {

	/**
	 * 
	 */
	public ApplicationModel() {
		super();
		initializeNavigationProcessor();
	}

	/**
	 * @param children
	 */
	public ApplicationModel(ISubApplication... children) {
		super(children);
		initializeNavigationProcessor();
	}

	/**
	 * @param label
	 * @param children
	 */
	public ApplicationModel(String label, ISubApplication... children) {
		super(label, children);
		initializeNavigationProcessor();
	}

	/**
	 * @param label
	 */
	public ApplicationModel(String label) {
		super(label);
		initializeNavigationProcessor();
	}

	protected void initializeNavigationProcessor() {
		setNavigationProcessor(new NavigationProcessor(getPresentationDefinitionService()));
	}

	protected IPresentationDefinitionService getPresentationDefinitionService() {

		// TODO: use service accessor/provider

		ServiceReference ref = Activator.getDefault().getContext().getServiceReference(
				IPresentationDefinitionService.class.getName());
		if (ref != null) {
			IPresentationDefinitionService service = (IPresentationDefinitionService) Activator.getDefault()
					.getContext().getService(ref);
			return service;
		} else {
			// TODO: some handling ???
			return null;
		}

	}

}
