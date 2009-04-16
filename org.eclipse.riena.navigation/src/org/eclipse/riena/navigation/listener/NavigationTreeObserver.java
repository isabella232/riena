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
package org.eclipse.riena.navigation.listener;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.INavigationNode.State;
import org.eclipse.riena.ui.filter.IUIFilter;

/**
 * listens on a sub tree of a passed node and fires the changes on all elements.
 * All added and remove children are automatically listened on
 */
public class NavigationTreeObserver {

	private IApplicationNodeListener applicationNodeListener;
	private Set<IApplicationNodeListener> applicationNodeListeners;
	private ISubApplicationNodeListener subApplicationListener;
	private Set<ISubApplicationNodeListener> subApplicationListeners;
	private IModuleGroupNodeListener moduleGroupNodeListener;
	private Set<IModuleGroupNodeListener> moduleGroupNodeListeners;
	private IModuleNodeListener moduleNodeListener;
	private Set<IModuleNodeListener> moduleNodeListeners;
	private ISubModuleNodeListener subModuleNodeListener;
	private Set<ISubModuleNodeListener> subModuleNodeListeners;

	/**
	 * creates the instance
	 */
	public NavigationTreeObserver() {
		super();
		applicationNodeListener = new MyApplicationNodeListener();
		applicationNodeListeners = new HashSet<IApplicationNodeListener>();
		subApplicationListener = new MySubApplicationNodeListener();
		subApplicationListeners = new HashSet<ISubApplicationNodeListener>();
		moduleGroupNodeListener = new MyModuleGroupNodeListener();
		moduleGroupNodeListeners = new HashSet<IModuleGroupNodeListener>();
		moduleNodeListener = new MyModuleNodeListener();
		moduleNodeListeners = new HashSet<IModuleNodeListener>();
		subModuleNodeListener = new MySubModuleNodeListener();
		subModuleNodeListeners = new HashSet<ISubModuleNodeListener>();
	}

	/**
	 * Add the passed listener
	 * 
	 * @param pListener
	 *            - the listener to add
	 */
	public void addListener(IApplicationNodeListener pListener) {
		applicationNodeListeners.add(pListener);
	}

	/**
	 * Remove the passed listener
	 * 
	 * @param pListener
	 *            - the listener to remove
	 */
	public void removeListener(IApplicationNodeListener pListener) {
		applicationNodeListeners.remove(pListener);
	}

	/**
	 * Add the passed listener
	 * 
	 * @param pListener
	 *            - the listener to add
	 */
	public void addListener(ISubApplicationNodeListener pListener) {
		subApplicationListeners.add(pListener);
	}

	/**
	 * Remove the passed listener
	 * 
	 * @param pListener
	 *            - the listener to remove
	 */
	public void removeListener(ISubApplicationNodeListener pListener) {
		subApplicationListeners.remove(pListener);
	}

	/**
	 * Add the passed listener
	 * 
	 * @param pListener
	 *            - the listener to add
	 */
	public void addListener(IModuleGroupNodeListener pListener) {
		moduleGroupNodeListeners.add(pListener);
	}

	/**
	 * Remove the passed listener
	 * 
	 * @param pListener
	 *            - the listener to remove
	 */
	public void removeListener(IModuleGroupNodeListener pListener) {
		moduleGroupNodeListeners.remove(pListener);
	}

	/**
	 * Add the passed listener
	 * 
	 * @param pListener
	 *            - the listener to add
	 */
	public void addListener(IModuleNodeListener pListener) {
		moduleNodeListeners.add(pListener);
	}

	/**
	 * Remove the passed listener
	 * 
	 * @param pListener
	 *            - the listener to remove
	 */
	public void removeListener(IModuleNodeListener pListener) {
		moduleNodeListeners.remove(pListener);
	}

	/**
	 * Add the passed listener
	 * 
	 * @param pListener
	 *            - the listener to add
	 */
	public void addListener(ISubModuleNodeListener pListener) {
		subModuleNodeListeners.add(pListener);
	}

	/**
	 * Remove the passed listener
	 * 
	 * @param pListener
	 *            - the listener to remove
	 */
	public void removeListener(ISubModuleNodeListener pListener) {
		subModuleNodeListeners.remove(pListener);
	}

	/**
	 * @return the applicationNodeListeners
	 */
	private Set<IApplicationNodeListener> getApplicationNodeListeners() {
		return applicationNodeListeners;
	}

	/**
	 * @return the subApplicationListeners
	 */
	private Set<ISubApplicationNodeListener> getSubApplicationListeners() {
		return subApplicationListeners;
	}

	/**
	 * @return the moduleGroupNodeListeners
	 */
	private Set<IModuleGroupNodeListener> getModuleGroupNodeListeners() {
		return moduleGroupNodeListeners;
	}

	/**
	 * @return the moduleNodeListeners
	 */
	private Set<IModuleNodeListener> getModuleNodeListeners() {
		return moduleNodeListeners;
	}

	/**
	 * @return the subModuleNodeListeners
	 */
	private Set<ISubModuleNodeListener> getSubModuleNodeListeners() {
		return subModuleNodeListeners;
	}

	public void addListenerTo(IApplicationNode pApplicationNode) {
		pApplicationNode.addListener(applicationNodeListener);
		for (ISubApplicationNode next : pApplicationNode.getChildren()) {
			addListenerTo(next);
		}
	}

	public void addListenerTo(ISubApplicationNode pSubApplication) {
		pSubApplication.addListener(subApplicationListener);
		for (IModuleGroupNode next : pSubApplication.getChildren()) {
			addListenerTo(next);
		}
	}

	public void addListenerTo(IModuleGroupNode pModuleGroupNode) {
		pModuleGroupNode.addListener(moduleGroupNodeListener);
		for (IModuleNode next : pModuleGroupNode.getChildren()) {
			addListenerTo(next);
		}
	}

	public void addListenerTo(IModuleNode pModuleNode) {
		pModuleNode.addListener(moduleNodeListener);
		for (ISubModuleNode next : pModuleNode.getChildren()) {
			addListenerTo(next);
		}
	}

	public void addListenerTo(ISubModuleNode pSubModuleNode) {
		pSubModuleNode.addListener(subModuleNodeListener);
		for (ISubModuleNode next : pSubModuleNode.getChildren()) {
			addListenerTo(next);
		}
	}

	public void removeListenerFrom(IApplicationNode pApplicationNode) {
		for (ISubApplicationNode next : pApplicationNode.getChildren()) {
			removeListenerFrom(next);
		}
		pApplicationNode.removeListener(applicationNodeListener);
	}

	public void removeListenerFrom(ISubApplicationNode pSubApplication) {
		for (IModuleGroupNode next : pSubApplication.getChildren()) {
			removeListenerFrom(next);
		}
		pSubApplication.removeListener(subApplicationListener);
	}

	public void removeListenerFrom(IModuleGroupNode pModuleGroupNode) {
		for (IModuleNode next : pModuleGroupNode.getChildren()) {
			removeListenerFrom(next);
		}
		pModuleGroupNode.removeListener(moduleGroupNodeListener);
	}

	public void removeListenerFrom(IModuleNode pModuleNode) {
		for (ISubModuleNode next : pModuleNode.getChildren()) {
			removeListenerFrom(next);
		}
		pModuleNode.removeListener(moduleNodeListener);
	}

	public void removeListenerFrom(ISubModuleNode pSubModuleNode) {
		for (ISubModuleNode next : pSubModuleNode.getChildren()) {
			removeListenerFrom(next);
		}
		pSubModuleNode.removeListener(subModuleNodeListener);
	}

	private class MyApplicationNodeListener extends ApplicationNodeListener {

		@Override
		public void filterAdded(IApplicationNode source, IUIFilter filter) {
			super.filterAdded(source, filter);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.filterAdded(source, filter);
			}
		}

		@Override
		public void filterRemoved(IApplicationNode source, IUIFilter filter) {
			super.filterRemoved(source, filter);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.filterRemoved(source, filter);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#activated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void activated(IApplicationNode source) {
			super.activated(source);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.activated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeActivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeActivated(IApplicationNode source) {
			super.beforeActivated(source);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.beforeActivated(source);
			}
		}

		@Override
		public void block(IApplicationNode source, boolean block) {

			super.block(source, block);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.block(source, block);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterActivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterActivated(IApplicationNode source) {
			super.afterActivated(source);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.afterActivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#deactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void deactivated(IApplicationNode source) {
			super.deactivated(source);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.deactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeDeactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeDeactivated(IApplicationNode source) {
			super.beforeDeactivated(source);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.beforeDeactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterDeactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterDeactivated(IApplicationNode source) {
			super.afterDeactivated(source);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.afterDeactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#disposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void disposed(IApplicationNode source) {
			super.disposed(source);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.disposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeDisposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeDisposed(IApplicationNode source) {
			super.beforeDisposed(source);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.beforeDisposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterDisposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterDisposed(IApplicationNode source) {
			super.afterDisposed(source);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.afterDisposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#childAdded(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childAdded(IApplicationNode source, ISubApplicationNode childAdded) {
			super.childAdded(source, childAdded);
			addListenerTo(childAdded);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.childAdded(source, childAdded);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#childRemoved(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childRemoved(IApplicationNode source, ISubApplicationNode childRemoved) {
			super.childRemoved(source, childRemoved);
			removeListenerFrom(childRemoved);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.childRemoved(source, childRemoved);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#expandedChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void expandedChanged(IApplicationNode source) {
			super.expandedChanged(source);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.expandedChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#labelChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void labelChanged(IApplicationNode source) {
			super.labelChanged(source);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.labelChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#markerChanged(org.eclipse.riena.navigation.INavigationNode,
		 *      IMarker)
		 */
		@Override
		public void markerChanged(IApplicationNode source, IMarker marker) {
			super.markerChanged(source, marker);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.markerChanged(source, marker);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#parentChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void parentChanged(IApplicationNode source) {
			super.parentChanged(source);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.parentChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#presentationChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void presentationChanged(IApplicationNode source) {
			super.presentationChanged(source);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.presentationChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#selectedChildChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void selectedChanged(IApplicationNode source) {
			super.selectedChanged(source);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.selectedChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#stateChanged(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode.State,
		 *      org.eclipse.riena.navigation.INavigationNode.State)
		 */
		@Override
		public void stateChanged(IApplicationNode source, State oldState, State newState) {
			super.stateChanged(source, oldState, newState);
			for (IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.stateChanged(source, oldState, newState);
			}
		}

	}

	private class MySubApplicationNodeListener extends SubApplicationNodeListener {

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#activated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void activated(ISubApplicationNode source) {
			super.activated(source);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.activated(source);
			}
		}

		@Override
		public void filterAdded(ISubApplicationNode source, IUIFilter filter) {
			super.filterAdded(source, filter);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.filterAdded(source, filter);
			}
		}

		@Override
		public void filterRemoved(ISubApplicationNode source, IUIFilter filter) {
			super.filterRemoved(source, filter);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.filterRemoved(source, filter);
			}
		}

		@Override
		public void block(ISubApplicationNode source, boolean block) {
			super.block(source, block);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.block(source, block);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeActivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeActivated(ISubApplicationNode source) {
			super.beforeActivated(source);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.beforeActivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterActivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterActivated(ISubApplicationNode source) {
			super.afterActivated(source);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.afterActivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#deactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void deactivated(ISubApplicationNode source) {
			super.deactivated(source);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.deactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeDeactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeDeactivated(ISubApplicationNode source) {
			super.beforeDeactivated(source);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.beforeDeactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterDeactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterDeactivated(ISubApplicationNode source) {
			super.afterDeactivated(source);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.afterDeactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#disposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void disposed(ISubApplicationNode source) {
			super.disposed(source);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.disposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeDisposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeDisposed(ISubApplicationNode source) {
			super.beforeDisposed(source);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.beforeDisposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterDisposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterDisposed(ISubApplicationNode source) {
			super.afterDisposed(source);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.afterDisposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#childAdded(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childAdded(ISubApplicationNode source, IModuleGroupNode childAdded) {
			super.childAdded(source, childAdded);
			addListenerTo(childAdded);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.childAdded(source, childAdded);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#childRemoved(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childRemoved(ISubApplicationNode source, IModuleGroupNode childRemoved) {
			super.childRemoved(source, childRemoved);
			removeListenerFrom(childRemoved);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.childRemoved(source, childRemoved);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#expandedChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void expandedChanged(ISubApplicationNode source) {
			super.expandedChanged(source);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.expandedChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#labelChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void labelChanged(ISubApplicationNode source) {
			super.labelChanged(source);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.labelChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#markerChanged(org.eclipse.riena.navigation.INavigationNode,
		 *      IMarker)
		 */
		@Override
		public void markerChanged(ISubApplicationNode source, IMarker marker) {
			super.markerChanged(source, marker);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.markerChanged(source, marker);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#parentChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void parentChanged(ISubApplicationNode source) {
			super.parentChanged(source);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.parentChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#presentationChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void presentationChanged(ISubApplicationNode source) {
			super.presentationChanged(source);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.presentationChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#selectedChildChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void selectedChanged(ISubApplicationNode source) {
			super.selectedChanged(source);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.selectedChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#stateChanged(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode.State,
		 *      org.eclipse.riena.navigation.INavigationNode.State)
		 */
		@Override
		public void stateChanged(ISubApplicationNode source, State oldState, State newState) {
			super.stateChanged(source, oldState, newState);
			for (ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.stateChanged(source, oldState, newState);
			}
		}

	}

	private class MyModuleGroupNodeListener extends ModuleGroupNodeListener {

		@Override
		public void filterAdded(IModuleGroupNode source, IUIFilter filter) {
			super.filterAdded(source, filter);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.filterAdded(source, filter);
			}
		}

		@Override
		public void filterRemoved(IModuleGroupNode source, IUIFilter filter) {
			super.filterRemoved(source, filter);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.filterRemoved(source, filter);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#activated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void activated(IModuleGroupNode source) {
			super.activated(source);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.activated(source);
			}
		}

		@Override
		public void block(IModuleGroupNode source, boolean block) {
			super.block(source, block);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.block(source, block);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeActivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeActivated(IModuleGroupNode source) {
			super.beforeActivated(source);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.beforeActivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterActivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterActivated(IModuleGroupNode source) {
			super.afterActivated(source);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.afterActivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#deactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void deactivated(IModuleGroupNode source) {
			super.deactivated(source);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.deactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeDeactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeDeactivated(IModuleGroupNode source) {
			super.beforeDeactivated(source);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.beforeDeactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterDeactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterDeactivated(IModuleGroupNode source) {
			super.afterDeactivated(source);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.afterDeactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#disposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void disposed(IModuleGroupNode source) {
			super.disposed(source);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.disposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeDisposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeDisposed(IModuleGroupNode source) {
			super.beforeDisposed(source);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.beforeDisposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterDisposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterDisposed(IModuleGroupNode source) {
			super.afterDisposed(source);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.afterDisposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#childAdded(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childAdded(IModuleGroupNode source, IModuleNode childAdded) {
			super.childAdded(source, childAdded);
			addListenerTo(childAdded);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.childAdded(source, childAdded);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#childRemoved(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childRemoved(IModuleGroupNode source, IModuleNode childRemoved) {
			super.childRemoved(source, childRemoved);
			removeListenerFrom(childRemoved);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.childRemoved(source, childRemoved);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#expandedChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void expandedChanged(IModuleGroupNode source) {
			super.expandedChanged(source);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.expandedChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#labelChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void labelChanged(IModuleGroupNode source) {
			super.labelChanged(source);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.labelChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#markerChanged(org.eclipse.riena.navigation.INavigationNode,
		 *      IMarker)
		 */
		@Override
		public void markerChanged(IModuleGroupNode source, IMarker marker) {
			super.markerChanged(source, marker);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.markerChanged(source, marker);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#parentChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void parentChanged(IModuleGroupNode source) {
			super.parentChanged(source);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.parentChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#presentationChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void presentationChanged(IModuleGroupNode source) {
			super.presentationChanged(source);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.presentationChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#selectedChildChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void selectedChanged(IModuleGroupNode source) {
			super.selectedChanged(source);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.selectedChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#stateChanged(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode.State,
		 *      org.eclipse.riena.navigation.INavigationNode.State)
		 */
		@Override
		public void stateChanged(IModuleGroupNode source, State oldState, State newState) {
			super.stateChanged(source, oldState, newState);
			for (IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.stateChanged(source, oldState, newState);
			}
		}
	}

	private class MyModuleNodeListener extends ModuleNodeListener {

		@Override
		public void filterAdded(IModuleNode source, IUIFilter filter) {
			super.filterAdded(source, filter);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.filterAdded(source, filter);
			}
		}

		@Override
		public void filterRemoved(IModuleNode source, IUIFilter filter) {
			super.filterRemoved(source, filter);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.filterRemoved(source, filter);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#activated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void activated(IModuleNode source) {
			super.activated(source);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.activated(source);
			}
		}

		@Override
		public void block(IModuleNode source, boolean block) {
			super.block(source, block);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.block(source, block);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeActivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeActivated(IModuleNode source) {
			super.beforeActivated(source);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.beforeActivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterActivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterActivated(IModuleNode source) {
			super.afterActivated(source);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.afterActivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#deactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void deactivated(IModuleNode source) {
			super.deactivated(source);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.deactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeDeactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeDeactivated(IModuleNode source) {
			super.beforeDeactivated(source);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.beforeDeactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterDeactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterDeactivated(IModuleNode source) {
			super.afterDeactivated(source);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.afterDeactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#disposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void disposed(IModuleNode source) {
			super.disposed(source);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.disposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeDisposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeDisposed(IModuleNode source) {
			super.beforeDisposed(source);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.beforeDisposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterDisposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterDisposed(IModuleNode source) {
			super.afterDisposed(source);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.afterDisposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#childAdded(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childAdded(IModuleNode source, ISubModuleNode childAdded) {
			super.childAdded(source, childAdded);
			addListenerTo(childAdded);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.childAdded(source, childAdded);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#childRemoved(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childRemoved(IModuleNode source, ISubModuleNode childRemoved) {
			super.childRemoved(source, childRemoved);
			removeListenerFrom(childRemoved);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.childRemoved(source, childRemoved);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#expandedChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void expandedChanged(IModuleNode source) {
			super.expandedChanged(source);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.expandedChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#labelChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void labelChanged(IModuleNode source) {
			super.labelChanged(source);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.labelChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#markerChanged(org.eclipse.riena.navigation.INavigationNode,
		 *      IMarker)
		 */
		@Override
		public void markerChanged(IModuleNode source, IMarker marker) {
			super.markerChanged(source, marker);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.markerChanged(source, marker);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#parentChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void parentChanged(IModuleNode source) {
			super.parentChanged(source);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.parentChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#presentationChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void presentationChanged(IModuleNode source) {
			super.presentationChanged(source);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.presentationChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#selectedChildChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void selectedChanged(IModuleNode source) {
			super.selectedChanged(source);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.selectedChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#stateChanged(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode.State,
		 *      org.eclipse.riena.navigation.INavigationNode.State)
		 */
		@Override
		public void stateChanged(IModuleNode source, State oldState, State newState) {
			super.stateChanged(source, oldState, newState);
			for (IModuleNodeListener next : getModuleNodeListeners()) {
				next.stateChanged(source, oldState, newState);
			}
		}
	}

	private class MySubModuleNodeListener extends SubModuleNodeListener {

		@Override
		public void filterAdded(ISubModuleNode source, IUIFilter filter) {
			super.filterAdded(source, filter);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.filterAdded(source, filter);
			}
		}

		@Override
		public void filterRemoved(ISubModuleNode source, IUIFilter filter) {
			super.filterRemoved(source, filter);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.filterRemoved(source, filter);
			}
		}

		@Override
		public void block(ISubModuleNode source, boolean block) {
			super.block(source, block);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.block(source, block);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#activated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void activated(ISubModuleNode source) {
			super.activated(source);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.activated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeActivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeActivated(ISubModuleNode source) {
			super.beforeActivated(source);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.beforeActivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterActivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterActivated(ISubModuleNode source) {
			super.afterActivated(source);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.afterActivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#deactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void deactivated(ISubModuleNode source) {
			super.deactivated(source);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.deactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeDeactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeDeactivated(ISubModuleNode source) {
			super.beforeDeactivated(source);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.beforeDeactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterDeactivated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterDeactivated(ISubModuleNode source) {
			super.afterDeactivated(source);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.afterDeactivated(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#disposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void disposed(ISubModuleNode source) {
			super.disposed(source);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.disposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#beforeDisposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void beforeDisposed(ISubModuleNode source) {
			super.beforeDisposed(source);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.beforeDisposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#afterDisposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void afterDisposed(ISubModuleNode source) {
			super.afterDisposed(source);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.afterDisposed(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#childAdded(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childAdded(ISubModuleNode source, ISubModuleNode childAdded) {
			super.childAdded(source, childAdded);
			addListenerTo(childAdded);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.childAdded(source, childAdded);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#childRemoved(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childRemoved(ISubModuleNode source, ISubModuleNode childRemoved) {
			super.childRemoved(source, childRemoved);
			removeListenerFrom(childRemoved);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.childRemoved(source, childRemoved);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#expandedChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void expandedChanged(ISubModuleNode source) {
			super.expandedChanged(source);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.expandedChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#labelChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void labelChanged(ISubModuleNode source) {
			super.labelChanged(source);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.labelChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#markerChanged(org.eclipse.riena.navigation.INavigationNode,
		 *      IMarker)
		 */
		@Override
		public void markerChanged(ISubModuleNode source, IMarker marker) {
			super.markerChanged(source, marker);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.markerChanged(source, marker);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#parentChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void parentChanged(ISubModuleNode source) {
			super.parentChanged(source);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.parentChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#presentationChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void presentationChanged(ISubModuleNode source) {
			super.presentationChanged(source);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.presentationChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#selectedChildChanged(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void selectedChanged(ISubModuleNode source) {
			super.selectedChanged(source);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.selectedChanged(source);
			}
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#stateChanged(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode.State,
		 *      org.eclipse.riena.navigation.INavigationNode.State)
		 */
		@Override
		public void stateChanged(ISubModuleNode source, State oldState, State newState) {
			super.stateChanged(source, oldState, newState);
			for (ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.stateChanged(source, oldState, newState);
			}
		}
	}

}
