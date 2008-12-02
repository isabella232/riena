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
package org.eclipse.riena.example.client.controllers;

import java.util.Collection;

import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.NavigationNodeUtility;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.IUIFilterContainer;
import org.eclipse.riena.ui.filter.impl.UIFilterProviderAccessor;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

/**
 *
 */
public class FilterExampleSubModuleController extends SubModuleController {

	private final static String SELECTED_TEXT = "deactivate"; //$NON-NLS-1$
	private final static String NOT_SELECTED_TEXT = "activate"; //$NON-NLS-1$

	@Override
	public void configureRidgets() {
		super.configureRidgets();

		IToggleButtonRidget menuToolAction = (IToggleButtonRidget) getRidget("menuToolItemBtn"); //$NON-NLS-1$
		updateToggleText(menuToolAction);
		menuToolAction.addListener(new IActionListener() {
			public void callback() {
				doMenuToolItemFilter();
			}
		});

		IToggleButtonRidget ridgetAction = (IToggleButtonRidget) getRidget("ridgetBtn"); //$NON-NLS-1$
		updateToggleText(ridgetAction);
		ridgetAction.addListener(new IActionListener() {
			public void callback() {
				doRidgetFilter();
			}
		});

		IToggleButtonRidget navigationAction = (IToggleButtonRidget) getRidget("navigationBtn"); //$NON-NLS-1$
		updateToggleText(navigationAction);
		navigationAction.addListener(new IActionListener() {
			public void callback() {
				doNavigationFilter();
			}
		});

	}

	private void doMenuToolItemFilter() {

		IToggleButtonRidget menuToolAction = (IToggleButtonRidget) getRidget("menuToolItemBtn"); //$NON-NLS-1$
		if (menuToolAction.isSelected()) {
			doAddFilter("rienaExample.menuToolBar"); //$NON-NLS-1$
		} else {
			doRemoveFilter("rienaExample.menuToolBar"); //$NON-NLS-1$
		}
		updateToggleText(menuToolAction);

	}

	private void doRidgetFilter() {

		IToggleButtonRidget ridgetAction = (IToggleButtonRidget) getRidget("ridgetBtn"); //$NON-NLS-1$
		if (ridgetAction.isSelected()) {
			doAddFilter("rienaExample.ridget"); //$NON-NLS-1$
		} else {
			doRemoveFilter("rienaExample.ridget"); //$NON-NLS-1$
		}
		updateToggleText(ridgetAction);

	}

	private void doNavigationFilter() {

		IToggleButtonRidget navigationAction = (IToggleButtonRidget) getRidget("navigationBtn"); //$NON-NLS-1$
		if (navigationAction.isSelected()) {
			doAddFilter("rienaExample.navigation"); //$NON-NLS-1$
		} else {
			doRemoveFilter("rienaExample.navigation"); //$NON-NLS-1$
		}
		updateToggleText(navigationAction);

	}

	/**
	 * Adds a filter to a node.
	 */
	private void doAddFilter(String filterId) {

		IApplicationNode applNode = getNavigationNode().getParentOfType(IApplicationNode.class);
		IUIFilterContainer container = UIFilterProviderAccessor.current().getUIFilterProvider().provideFilter(filterId);
		IUIFilter filter = container.getFilter();
		Collection<String> targetNodeIds = container.getFilterTargetNodeIds();
		for (String targetNodeId : targetNodeIds) {
			INavigationNode<?> node = NavigationNodeUtility.findNodeLongId(targetNodeId, applNode);
			if (node != null) {
				node.addFilter(filter);
			}
		}

	}

	/**
	 * Removes a filter from a node.
	 */
	private void doRemoveFilter(String filterId) {

		IApplicationNode applNode = getNavigationNode().getParentOfType(IApplicationNode.class);
		IUIFilterContainer container = UIFilterProviderAccessor.current().getUIFilterProvider().provideFilter(filterId);
		IUIFilter filter = container.getFilter();
		Collection<String> targetNodeIds = container.getFilterTargetNodeIds();
		for (String targetNodeId : targetNodeIds) {
			INavigationNode<?> node = NavigationNodeUtility.findNodeLongId(targetNodeId, applNode);
			if (node != null) {
				node.removeFilter(filter.getFilterID());
			}
		}

	}

	/**
	 * Updates the text of the given toggle button.
	 * 
	 * @param toggle
	 *            - toggle button
	 */
	private void updateToggleText(IToggleButtonRidget toggle) {

		if (toggle.isSelected()) {
			toggle.setText(SELECTED_TEXT);
		} else {
			toggle.setText(NOT_SELECTED_TEXT);
		}

	}

}
