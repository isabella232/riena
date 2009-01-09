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
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

/**
 *
 */
public class FilterExampleSubModuleController extends SubModuleController {

	public enum FilterId {
		MENUITEM("rienaExample.menuToolBar"), //$NON-NLS-1$
		NAVIGATION("rienaExample.navigation"), //$NON-NLS-1$
		RIDGET("rienaExample.ridget"), //$NON-NLS-1$
		RIDGET_01("rienaExample.ridget01"), //$NON-NLS-1$
		RIDGET_HIDE("rienaExample.ridgetHide"), //$NON-NLS-1$
		RIDGET_DISABLE("rienaExample.ridgetDisable"); //$NON-NLS-1$

		private final String id;

		FilterId(String id) {
			this.id = id;
		}

		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return id;
		}

	}

	private final static String SELECTED_TEXT = "deactivate"; //$NON-NLS-1$
	private final static String NOT_SELECTED_TEXT = "activate"; //$NON-NLS-1$

	@Override
	public void configureRidgets() {

		super.configureRidgets();

		IToggleButtonRidget navigationAction = (IToggleButtonRidget) getRidget("navigationBtn"); //$NON-NLS-1$
		updateToggleText(navigationAction);
		navigationAction.addListener(new IActionListener() {
			public void callback() {
				doFilter(FilterId.NAVIGATION, "navigationBtn"); //$NON-NLS-1$
			}
		});

		IToggleButtonRidget menuToolAction = (IToggleButtonRidget) getRidget("menuToolItemBtn"); //$NON-NLS-1$
		updateToggleText(menuToolAction);
		menuToolAction.addListener(new IActionListener() {
			public void callback() {
				doFilter(FilterId.MENUITEM, "menuToolItemBtn"); //$NON-NLS-1$
			}
		});

		IToggleButtonRidget ridgetAction = (IToggleButtonRidget) getRidget("ridgetBtn"); //$NON-NLS-1$
		updateToggleText(ridgetAction);
		ridgetAction.addListener(new IActionListener() {
			public void callback() {
				doFilter(FilterId.RIDGET, "ridgetBtn"); //$NON-NLS-1$
			}
		});

		IToggleButtonRidget ridgetDisableAction = (IToggleButtonRidget) getRidget("ridgetDisableBtn"); //$NON-NLS-1$
		updateToggleText(ridgetAction);
		ridgetDisableAction.addListener(new IActionListener() {
			public void callback() {
				doFilter(FilterId.RIDGET_DISABLE, "ridgetDisableBtn"); //$NON-NLS-1$
			}
		});

		IToggleButtonRidget ridgetHideAction = (IToggleButtonRidget) getRidget("ridgetHideBtn"); //$NON-NLS-1$
		updateToggleText(ridgetAction);
		ridgetHideAction.addListener(new IActionListener() {
			public void callback() {
				doFilter(FilterId.RIDGET_HIDE, "ridgetHideBtn"); //$NON-NLS-1$
			}
		});

		IToggleButtonRidget ridget01Action = (IToggleButtonRidget) getRidget("ridget01Btn"); //$NON-NLS-1$
		updateToggleText(ridgetAction);
		ridget01Action.addListener(new IActionListener() {
			public void callback() {
				doFilter(FilterId.RIDGET_01, "ridget01Btn"); //$NON-NLS-1$
			}
		});

		MySampleBean sampleBean = new MySampleBean();

		ITextRidget sampleText = (ITextRidget) getRidget("sampleText"); //$NON-NLS-1$
		sampleText.setMandatory(true);
		sampleText.bindToModel(sampleBean, "text"); //$NON-NLS-1$
		sampleText.updateFromModel();

	}

	private void doFilter(FilterId filterId, String buttonRidgetId) {

		IToggleButtonRidget menuToolAction = (IToggleButtonRidget) getRidget(buttonRidgetId);

		IApplicationNode applNode = getNavigationNode().getParentOfType(IApplicationNode.class);
		IUIFilterContainer container = UIFilterProviderAccessor.current().getUIFilterProvider().provideFilter(
				filterId.toString());
		IUIFilter filter = container.getFilter();
		Collection<String> targetNodeIds = container.getFilterTargetNodeIds();
		for (String targetNodeId : targetNodeIds) {
			INavigationNode<?> node = NavigationNodeUtility.findNodeLongId(targetNodeId, applNode);
			if (menuToolAction.isSelected()) {
				node.addFilter(filter);
			} else {
				node.removeFilter(filter.getFilterID());
			}
		}

		updateToggleText(menuToolAction);

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

	private static class MySampleBean {

		private String text = ""; //$NON-NLS-1$

		public void setText(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}

	}

}
