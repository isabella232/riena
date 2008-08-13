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

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeBuilder;
import org.eclipse.riena.navigation.INavigationNodeId;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 *
 */
public class PlaygroundNodeBuilder implements INavigationNodeBuilder {

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeBuilder#buildNode(org.eclipse.riena.navigation.INavigationNodeId)
	 */
	public INavigationNode<?> buildNode(INavigationNodeId navigationNodeId) {

		IModuleGroupNode moduleGroup = new ModuleGroupNode(navigationNodeId, "Playground"); //$NON-NLS-1$

		IModuleNode playgroundModule = new ModuleNode(null, "Playground"); //$NON-NLS-1$
		moduleGroup.addChild(playgroundModule);

		ISubModuleNode buttonsSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.buttons"), "Buttons"); //$NON-NLS-1$ //$NON-NLS-2$
		playgroundModule.addChild(buttonsSubModule);

		ISubModuleNode choiceSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.choice"), "Choice"); //$NON-NLS-1$ //$NON-NLS-2$
		playgroundModule.addChild(choiceSubModule);

		ISubModuleNode comboSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.combo"), "Combo"); //$NON-NLS-1$ //$NON-NLS-2$
		playgroundModule.addChild(comboSubModule);

		ISubModuleNode listSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.list"), "List"); //$NON-NLS-1$ //$NON-NLS-2$
		playgroundModule.addChild(listSubModule);

		ISubModuleNode textSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.text"), "Text"); //$NON-NLS-1$ //$NON-NLS-2$
		playgroundModule.addChild(textSubModule);

		ISubModuleNode markerSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.marker"), "Marker"); //$NON-NLS-1$ //$NON-NLS-2$
		playgroundModule.addChild(markerSubModule);

		ISubModuleNode focusableSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.focusable"), "Focusable"); //$NON-NLS-1$ //$NON-NLS-2$
		playgroundModule.addChild(focusableSubModule);

		ISubModuleNode validationSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.validation"), "Validation"); //$NON-NLS-1$ //$NON-NLS-2$
		playgroundModule.addChild(validationSubModule);

		ISubModuleNode treeSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.tree"), "Tree"); //$NON-NLS-1$ //$NON-NLS-2$
		playgroundModule.addChild(treeSubModule);

		ISubModuleNode treeTableSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.treeTable"), "Tree Table"); //$NON-NLS-1$ //$NON-NLS-2$
		playgroundModule.addChild(treeTableSubModule);

		ISubModuleNode tableSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.table"), "Table"); //$NON-NLS-1$ //$NON-NLS-2$
		playgroundModule.addChild(tableSubModule);

		ISubModuleNode systemPropertiesSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.systemProperties"), "System Properties"); //$NON-NLS-1$ //$NON-NLS-2$
		playgroundModule.addChild(systemPropertiesSubModule);

		ISubModuleNode statusLineSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.statusLine"), "Statusline"); //$NON-NLS-1$ //$NON-NLS-2$
		playgroundModule.addChild(statusLineSubModule);

		return moduleGroup;
	}

}
