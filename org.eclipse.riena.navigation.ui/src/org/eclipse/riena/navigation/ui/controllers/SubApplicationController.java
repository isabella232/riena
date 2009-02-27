/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.controllers;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.IModuleGroupNodeListener;
import org.eclipse.riena.navigation.listener.IModuleNodeListener;
import org.eclipse.riena.navigation.listener.ISubApplicationNodeListener;
import org.eclipse.riena.navigation.listener.ISubModuleNodeListener;
import org.eclipse.riena.navigation.listener.ModuleGroupNodeListener;
import org.eclipse.riena.navigation.listener.ModuleNodeListener;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.listener.SubApplicationNodeListener;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.ui.ridgets.INavigationTreeRidget;
import org.eclipse.riena.navigation.ui.ridgets.INavigationTreeRidgetListener;
import org.eclipse.riena.navigation.ui.ridgets.NavigationTreeRidgetAdapter;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IUIProcessRidget;

/**
 * Implements the Controller for a Module Sub Application
 */
public class SubApplicationController extends NavigationNodeController<ISubApplicationNode> {

	private INavigationTreeRidget navigationTree;
	private INavigationTreeRidgetListener navigationTreeRidgetListener;
	private ISubApplicationNodeListener subApplicationListener;
	private ISubModuleNodeListener subModuleNodeListener;
	private IModuleNodeListener moduleNodeListener;
	private IModuleGroupNodeListener moduleGroupNodeListener;
	private NavigationTreeObserver navigationTreeObserver;
	private IUIProcessRidget uiProcessRidget;

	/**
	 * Create a new Controller, find the corresponding subApplication for the
	 * passed ID
	 */
	public SubApplicationController(ISubApplicationNode pSubApplication) {
		super(pSubApplication);
		navigationTreeRidgetListener = new NavigationTreeRidgetListener();
		subApplicationListener = new MySubApplicationNodeListener();
		subModuleNodeListener = new MySubModuleNodeListener();
		moduleNodeListener = new MyModuleNodeListener();
		moduleGroupNodeListener = new MyModuleGroupNodeListener();
		navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(subApplicationListener);
		navigationTreeObserver.addListener(moduleGroupNodeListener);
		navigationTreeObserver.addListener(moduleNodeListener);
		navigationTreeObserver.addListener(subModuleNodeListener);
		navigationTreeObserver.addListenerTo(pSubApplication);
	}

	public INavigationTreeRidget getNavigationTree() {
		return navigationTree;
	}

	public void setNavigationTree(INavigationTreeRidget pNavigationTree) {
		if (getNavigationTree() != null) {
			getNavigationTree().removeListener(navigationTreeRidgetListener);
		}
		this.navigationTree = pNavigationTree;
		getNavigationTree().addListener(navigationTreeRidgetListener);
	}

	private static class NavigationTreeRidgetListener extends NavigationTreeRidgetAdapter {

		/**
		 * @see org.eclipse.riena.navigation.ui.ridgets.NavigationTreeRidgetAdapter#nodeSelected(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void nodeSelected(INavigationNode<?> node) {
			super.nodeSelected(node);
			node.activate();
		}
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {
		if (getNavigationTree() != null) {
			getNavigationTree().showRoot(getNavigationNode());
		}
	}

	private class MySubModuleNodeListener extends SubModuleNodeListener {
		/**
		 * @see org.eclipse.riena.navigation.model.NavigationTreeAdapter#childAdded(org.eclipse.riena.navigation.ISubModuleNode,
		 *      org.eclipse.riena.navigation.ISubModuleNode)
		 */
		@Override
		public void childAdded(ISubModuleNode source, ISubModuleNode childAdded) {
			super.childAdded(source, childAdded);
			if (getNavigationTree() != null) {
				getNavigationTree().childAdded(childAdded);
			}
		}

	}

	private class MySubApplicationNodeListener extends SubApplicationNodeListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.riena.navigation.model.NavigationNodeAdapter#afterActivated
		 * (org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterActivated(ISubApplicationNode source) {
			if (getUiProcessRidget() != null) {
				getUiProcessRidget().activate();
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.model.NavigationTreeAdapter#childAdded(org.eclipse.riena.navigation.ISubApplicationNode,
		 *      org.eclipse.riena.navigation.IModuleGroupNode)
		 */
		@Override
		public void childAdded(ISubApplicationNode source, IModuleGroupNode childAdded) {
			super.childAdded(source, childAdded);
			if (getNavigationTree() != null) {
				getNavigationTree().childAdded(childAdded);
			}
		}

	}

	private class MyModuleNodeListener extends ModuleNodeListener {
		/**
		 * @see org.eclipse.riena.navigation.model.NavigationTreeAdapter#childAdded(org.eclipse.riena.navigation.IModuleNode,
		 *      org.eclipse.riena.navigation.ISubModuleNode)
		 */
		@Override
		public void childAdded(IModuleNode source, ISubModuleNode childAdded) {
			super.childAdded(source, childAdded);
			if (getNavigationTree() != null) {
				getNavigationTree().childAdded(childAdded);
			}
		}

	}

	private class MyModuleGroupNodeListener extends ModuleGroupNodeListener {

		/**
		 * @see org.eclipse.riena.navigation.model.NavigationTreeAdapter#childAdded(org.eclipse.riena.navigation.IModuleGroupNode,
		 *      org.eclipse.riena.navigation.IModuleNode)
		 */
		@Override
		public void childAdded(IModuleGroupNode source, IModuleNode childAdded) {
			super.childAdded(source, childAdded);
			if (getNavigationTree() != null) {
				getNavigationTree().childAdded(childAdded);
			}
		}
	}

	@Override
	public void afterBind() {
		super.afterBind();
		initUiProcessRidget();
	}

	NodeEventDelegation contextUpdater = new NodeEventDelegation();

	private void initUiProcessRidget() {
		if (uiProcessRidget == null) {
			// fallback
			uiProcessRidget = (IUIProcessRidget) getRidget("uiProcessRidget"); //$NON-NLS-1$
			if (uiProcessRidget == null) {
				return;
			}
		}
		uiProcessRidget.setContextLocator(contextUpdater);
	}

	/**
	 * Returns the ridget of a menu action.
	 * 
	 * @param id
	 *            - id of the menu item
	 * @return action ridget; {@code null} if no action ridget was found
	 */
	public IActionRidget getMenuActionRidget(String id) {

		String menuItemId = IActionRidget.BASE_ID_MENUACTION + id;
		return getActionRidget(menuItemId);

	}

	/**
	 * Returns the ridget of a tool bar action.
	 * 
	 * @param id
	 *            - id of the tool bar button
	 * @return action ridget; {@code null} if no action ridget was found
	 */
	public IActionRidget getToolbarActionRidget(String id) {

		String menuItemId = IActionRidget.BASE_ID_TOOLBARACTION + id;
		return getActionRidget(menuItemId);

	}

	/**
	 * Returns the action ridget with given id.
	 * 
	 * @param id
	 *            - id of the ridget
	 * @return action ridget; {@code null} if no action ridget was found
	 */
	private IActionRidget getActionRidget(String id) {

		IRidget ridget = getRidget(id);
		if (ridget instanceof IActionRidget) {
			return (IActionRidget) ridget;
		} else {
			return null;
		}

	}

	/**
	 * @return the progressBoxRidget
	 */
	public IUIProcessRidget getUiProcessRidget() {
		return uiProcessRidget;
	}

	/**
	 * @param uiProcessRidget
	 *            the progressBoxRidget to set
	 */
	public void setUiProcessRidget(IUIProcessRidget uiProcessRidget) {
		this.uiProcessRidget = uiProcessRidget;
	}

}
