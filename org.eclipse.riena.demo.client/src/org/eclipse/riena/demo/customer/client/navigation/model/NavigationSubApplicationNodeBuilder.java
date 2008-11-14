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
package org.eclipse.riena.demo.customer.client.navigation.model;

import org.eclipse.riena.demo.customer.client.application.ExampleIcons;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProvider;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProviderAccessor;

public class NavigationSubApplicationNodeBuilder extends NavigationNodeBuilder {

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#buildNode(org.eclipse.riena.navigation.NavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public INavigationNode<?> buildNode(NavigationNodeId navigationNodeId, NavigationArgument navigationArgument) {
		SwtViewProvider presentation = SwtViewProviderAccessor.getViewProvider();

		ISubApplicationNode subApplication = new SubApplicationNode(navigationNodeId, "Customer Record"); //$NON-NLS-1$
		subApplication.setIcon(createIconPath(ExampleIcons.ICON_APPLICATION));
		presentation.present(subApplication, "customerRecord"); //$NON-NLS-1$
		subApplication.setSelected(true);

		// getNavigationNode().navigate(new
		// NavigationNodeId("org.eclipse.riena.example.client.CustomerRecord"));

		return subApplication;
	}

}
