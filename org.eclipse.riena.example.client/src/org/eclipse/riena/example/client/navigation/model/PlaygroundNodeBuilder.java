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

import org.eclipse.riena.example.client.controllers.BlockingSubModuleController;
import org.eclipse.riena.example.client.controllers.ChoiceSubModuleController;
import org.eclipse.riena.example.client.controllers.ComboSubModuleController;
import org.eclipse.riena.example.client.controllers.DialogSubModuleController;
import org.eclipse.riena.example.client.controllers.FocusableSubModuleController;
import org.eclipse.riena.example.client.controllers.ListSubModuleController;
import org.eclipse.riena.example.client.controllers.MarkerSubModuleController;
import org.eclipse.riena.example.client.controllers.MessageBoxSubModuleController;
import org.eclipse.riena.example.client.controllers.RidgetsSubModuleController;
import org.eclipse.riena.example.client.controllers.StatuslineSubModuleController;
import org.eclipse.riena.example.client.controllers.SystemPropertiesSubModuleController;
import org.eclipse.riena.example.client.controllers.TableSubModuleController;
import org.eclipse.riena.example.client.controllers.TextDateSubModuleController;
import org.eclipse.riena.example.client.controllers.TextNumericSubModuleController;
import org.eclipse.riena.example.client.controllers.TextSubModuleController;
import org.eclipse.riena.example.client.controllers.TreeSubModuleController;
import org.eclipse.riena.example.client.controllers.TreeTableSubModuleController;
import org.eclipse.riena.example.client.controllers.ValidationSubModuleController;
import org.eclipse.riena.example.client.views.BlockingSubModuleView;
import org.eclipse.riena.example.client.views.ChoiceSubModuleView;
import org.eclipse.riena.example.client.views.ComboSubModuleView;
import org.eclipse.riena.example.client.views.DialogSubModuleView;
import org.eclipse.riena.example.client.views.FocusableSubModuleView;
import org.eclipse.riena.example.client.views.ListSubModuleView;
import org.eclipse.riena.example.client.views.MarkerSubModuleView;
import org.eclipse.riena.example.client.views.MessageBoxSubModuleView;
import org.eclipse.riena.example.client.views.NoControllerSubModuleView;
import org.eclipse.riena.example.client.views.RidgetsSubModuleView;
import org.eclipse.riena.example.client.views.StatuslineSubModuleView;
import org.eclipse.riena.example.client.views.SystemPropertiesSubModuleView;
import org.eclipse.riena.example.client.views.TableSubModuleView;
import org.eclipse.riena.example.client.views.TextDateSubModuleView;
import org.eclipse.riena.example.client.views.TextNumericSubModuleView;
import org.eclipse.riena.example.client.views.TextSubModuleView;
import org.eclipse.riena.example.client.views.TreeSubModuleView;
import org.eclipse.riena.example.client.views.TreeTableSubModuleView;
import org.eclipse.riena.example.client.views.ValidationSubModuleView;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeBuilder;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 *
 */
public class PlaygroundNodeBuilder implements INavigationNodeBuilder {

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeBuilder#buildNode(org.eclipse.riena.navigation.NavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public INavigationNode<?> buildNode(NavigationNodeId navigationNodeId, NavigationArgument navigationArgument) {

		IModuleGroupNode moduleGroup = new ModuleGroupNode(navigationNodeId);

		IModuleNode playgroundModule = new ModuleNode(null, "Playground"); //$NON-NLS-1$
		moduleGroup.addChild(playgroundModule);

		ISubModuleNode buttonsSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.buttons"), "Buttons"); //$NON-NLS-1$ //$NON-NLS-2$
		buttonsSubModule.setViewId(RidgetsSubModuleView.ID);
		buttonsSubModule.setControllerClassForView(RidgetsSubModuleController.class);
		playgroundModule.addChild(buttonsSubModule);
		ISubModuleNode choiceSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.choice"), "Choice"); //$NON-NLS-1$ //$NON-NLS-2$
		choiceSubModule.setViewId(ChoiceSubModuleView.class.getName());
		choiceSubModule.setControllerClassForView(ChoiceSubModuleController.class);
		playgroundModule.addChild(choiceSubModule);

		ISubModuleNode comboSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.combo"), "Combo"); //$NON-NLS-1$ //$NON-NLS-2$
		comboSubModule.setViewId(ComboSubModuleView.ID);
		comboSubModule.setControllerClassForView(ComboSubModuleController.class);
		playgroundModule.addChild(comboSubModule);

		ISubModuleNode listSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.list"), "List"); //$NON-NLS-1$ //$NON-NLS-2$
		listSubModule.setViewId(ListSubModuleView.ID);
		listSubModule.setControllerClassForView(ListSubModuleController.class);
		playgroundModule.addChild(listSubModule);

		ISubModuleNode textSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.text"), "Text"); //$NON-NLS-1$ //$NON-NLS-2$
		textSubModule.setViewId(TextSubModuleView.ID);
		textSubModule.setControllerClassForView(TextSubModuleController.class);
		playgroundModule.addChild(textSubModule);

		ISubModuleNode textNumbersSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.text.numeric"), "Text (Numeric)"); //$NON-NLS-1$ //$NON-NLS-2$
		textNumbersSubModule.setViewId(TextNumericSubModuleView.ID);
		textNumbersSubModule.setControllerClassForView(TextNumericSubModuleController.class);
		playgroundModule.addChild(textNumbersSubModule);

		ISubModuleNode textDateSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.text.date"), "Text (Date)"); //$NON-NLS-1$ //$NON-NLS-2$
		textDateSubModule.setViewId(TextDateSubModuleView.ID);
		textDateSubModule.setControllerClassForView(TextDateSubModuleController.class);
		playgroundModule.addChild(textDateSubModule);

		ISubModuleNode markerSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.marker"), "Marker"); //$NON-NLS-1$ //$NON-NLS-2$
		markerSubModule.setViewId(MarkerSubModuleView.ID);
		markerSubModule.setControllerClassForView(MarkerSubModuleController.class);
		playgroundModule.addChild(markerSubModule);
		//
		//		ISubModuleNode filterSubModule = new SubModuleNode(new NavigationNodeId(
		//				"org.eclipse.riena.example.filterridget"), "UI-Filter (Ridgets)"); //$NON-NLS-1$ //$NON-NLS-2$
		//		playgroundModule.addChild(filterSubModule);
		//
		//		ISubModuleNode filterNavigationSubModule = new SubModuleNode(new NavigationNodeId(
		//				"org.eclipse.riena.example.filternavigation"), "UI-Filter (Navigation)"); //$NON-NLS-1$ //$NON-NLS-2$
		//		playgroundModule.addChild(filterNavigationSubModule);

		ISubModuleNode focusableSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.focusable"), "Focusable"); //$NON-NLS-1$ //$NON-NLS-2$
		focusableSubModule.setViewId(FocusableSubModuleView.ID);
		focusableSubModule.setControllerClassForView(FocusableSubModuleController.class);
		playgroundModule.addChild(focusableSubModule);

		ISubModuleNode validationSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.validation"), "Validation"); //$NON-NLS-1$ //$NON-NLS-2$
		validationSubModule.setViewId(ValidationSubModuleView.ID);
		validationSubModule.setControllerClassForView(ValidationSubModuleController.class);
		playgroundModule.addChild(validationSubModule);

		ISubModuleNode treeSubModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.tree"), "Tree"); //$NON-NLS-1$ //$NON-NLS-2$
		treeSubModule.setViewId(TreeSubModuleView.ID);
		treeSubModule.setControllerClassForView(TreeSubModuleController.class);
		playgroundModule.addChild(treeSubModule);

		ISubModuleNode treeTableSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.treeTable"), "Tree Table"); //$NON-NLS-1$ //$NON-NLS-2$
		treeTableSubModule.setViewId(TreeTableSubModuleView.ID);
		treeTableSubModule.setControllerClassForView(TreeTableSubModuleController.class);
		playgroundModule.addChild(treeTableSubModule);

		ISubModuleNode tableSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.table"), "Table"); //$NON-NLS-1$ //$NON-NLS-2$
		tableSubModule.setViewId(TableSubModuleView.ID);
		tableSubModule.setControllerClassForView(TableSubModuleController.class);
		playgroundModule.addChild(tableSubModule);

		ISubModuleNode systemPropertiesSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.systemProperties"), "System Properties"); //$NON-NLS-1$ //$NON-NLS-2$
		systemPropertiesSubModule.setViewId(SystemPropertiesSubModuleView.ID);
		systemPropertiesSubModule.setControllerClassForView(SystemPropertiesSubModuleController.class);
		playgroundModule.addChild(systemPropertiesSubModule);

		ISubModuleNode statusLineSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.statusLine"), "Statusline"); //$NON-NLS-1$ //$NON-NLS-2$
		statusLineSubModule.setViewId(StatuslineSubModuleView.ID);
		statusLineSubModule.setControllerClassForView(StatuslineSubModuleController.class);
		playgroundModule.addChild(statusLineSubModule);

		ISubModuleNode blockingSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.blocking"), "Blocking"); //$NON-NLS-1$ //$NON-NLS-2$
		blockingSubModule.setViewId(BlockingSubModuleView.ID);
		blockingSubModule.setControllerClassForView(BlockingSubModuleController.class);
		playgroundModule.addChild(blockingSubModule);

		ISubModuleNode noControllerSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.noController"), "View without Controller"); //$NON-NLS-1$ //$NON-NLS-2$
		noControllerSubModule.setViewId(NoControllerSubModuleView.ID);
		playgroundModule.addChild(noControllerSubModule);

		ISubModuleNode dialogSubModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.dialog"), "Dialog"); //$NON-NLS-1$ //$NON-NLS-2$
		dialogSubModule.setViewId(DialogSubModuleView.ID);
		dialogSubModule.setControllerClassForView(DialogSubModuleController.class);
		playgroundModule.addChild(dialogSubModule);

		ISubModuleNode messageBoxSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.messageBox"), "Message Box"); //$NON-NLS-1$ //$NON-NLS-2$
		messageBoxSubModule.setViewId(MessageBoxSubModuleView.ID);
		messageBoxSubModule.setControllerClassForView(MessageBoxSubModuleController.class);
		playgroundModule.addChild(messageBoxSubModule);

		return moduleGroup;
	}
}
