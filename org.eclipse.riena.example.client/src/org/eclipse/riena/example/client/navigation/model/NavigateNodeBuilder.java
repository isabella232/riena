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
package org.eclipse.riena.example.client.navigation.model;

import org.eclipse.riena.example.client.application.IExampleIcons;
import org.eclipse.riena.internal.example.client.Activator;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeBuilder;
import org.eclipse.riena.navigation.INavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.osgi.framework.Bundle;

/**
 *
 */
public class NavigateNodeBuilder implements INavigationNodeBuilder {

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeBuilder#buildNode(org.eclipse.riena.navigation.INavigationNodeId)
	 */
	public INavigationNode<?> buildNode(INavigationNodeId INavigationNodeId) {
		ModuleGroupNode moduleGroup = new ModuleGroupNode("Navigate");
		moduleGroup.setPresentWithSingleModule(false);
		ModuleNode module = new ModuleNode("Navigate");
		module.setIcon(createIconPath(IExampleIcons.ICON_GREEN_LED));
		moduleGroup.addChild(module);
		SubModuleNode subModule = new SubModuleNode("Navigate");
		subModule.setPresentationId(new NavigationNodeId("org.eclipse.riena.example.navigate.form"));
		module.addChild(subModule);
		return moduleGroup;
	}

	protected String createIconPath(String subPath) {
		Bundle bundle = Activator.getDefault().getBundle();

		if (bundle == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder(bundle.getSymbolicName());
		builder.append(":"); //$NON-NLS-1$
		builder.append(subPath);
		return builder.toString();
	}

}
