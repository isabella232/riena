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
package org.eclipse.riena.navigation.ui.controllers;

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.riena.navigation.model.SimpleNavigationNodeAdapater;
import org.eclipse.riena.navigation.ui.ridgets.INavigationTreeRidget;
import org.eclipse.riena.navigation.ui.ridgets.INavigationTreeRidgetListener;
import org.eclipse.riena.navigation.ui.ridgets.NavigationTreeRidgetAdapter;
import org.eclipse.riena.ui.ridgets.IContextUpdateListener;
import org.eclipse.riena.ui.ridgets.IStatuslineRidget;
import org.eclipse.riena.ui.ridgets.IUIProcessRidget;
import org.eclipse.riena.ui.ridgets.IVisualContextManager;

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
	private IStatuslineRidget statuslineRidget;
	private IUIProcessRidget uiProcessRidget;

	private NodeListener contextUpdater = new NodeListener();

	private List<IContextUpdateListener> listeners = new ArrayList<IContextUpdateListener>();

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

	/**
	 * @return the navigationTree
	 */
	public INavigationTreeRidget getNavigationTree() {
		return navigationTree;
	}

	/**
	 * @param navigationTree
	 *            the navigationTree to set
	 */
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
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {
		if (getNavigationTree() != null) {
			getNavigationTree().showRoot(getNavigationNode());
		}
	}

	@SuppressWarnings("unchecked")
	private class NodeListener extends SimpleNavigationNodeAdapater {

		@Override
		public void activated(INavigationNode source) {
			contextUpdated(source);
		}

		@Override
		public void beforeDeactivated(INavigationNode source) {
			for (IContextUpdateListener listener : listeners) {
				listener.beforeContextUpdate(source);
			}
		}

		@Override
		public void deactivated(INavigationNode source) {
			contextUpdated(source);
		}

		private void contextUpdated(INavigationNode source) {
			for (IContextUpdateListener listener : listeners) {
				listener.contextUpdated(source);
			}
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
		 * org.eclipse.riena.navigation.model.NavigationNodeAdapter#afterDeactivated
		 * (org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterDeactivated(ISubApplicationNode source) {
			super.afterDeactivated(source);
			if (getStatuslineRidget() != null) {
				getStatuslineRidget().hidePopups();
			}
			if (getUiProcessRidget() != null) {
				getUiProcessRidget().deactivate();
			}
		}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.ui.controllers.NavigationNodeController#
	 * afterBind()
	 */
	@Override
	public void afterBind() {
		super.afterBind();
		initRidgets();
	}

	private void initRidgets() {
		initUiProcessRidget();
	}

	private void initUiProcessRidget() {
		IUIProcessRidget uiProcessRidget = getUiProcessRidget();
		if (uiProcessRidget == null) {
			return;
		}
		uiProcessRidget.setContextLocator(new IVisualContextManager() {

			@SuppressWarnings("unchecked")
			public List<Object> getActiveContexts(List<Object> contexts) {
				List nodes = new ArrayList();
				for (Object object : contexts) {
					if (object instanceof INavigationNode) {
						INavigationNode<?> node = (INavigationNode) object;
						if (node.isActivated()) {
							nodes.add(node);
						}
					}
				}
				return nodes;
			}

			public void addContextUpdateListener(IContextUpdateListener listener, Object context) {
				if (context instanceof INavigationNode<?>) {
					INavigationNode<?> node = (INavigationNode<?>) context;
					node.addSimpleListener(contextUpdater);
					listeners.add(listener);
				}
			}

		});

	}

	/**
	 * @return the statuslineRidget
	 */
	public IStatuslineRidget getStatuslineRidget() {
		return statuslineRidget;
	}

	/**
	 * @param statuslineRidget
	 *            the statuslineRidget to set
	 */
	public void setStatuslineRidget(IStatuslineRidget statuslineRidget) {
		this.statuslineRidget = statuslineRidget;
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
