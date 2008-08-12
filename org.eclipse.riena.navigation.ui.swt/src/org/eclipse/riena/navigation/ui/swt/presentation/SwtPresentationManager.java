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
package org.eclipse.riena.navigation.ui.swt.presentation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.riena.navigation.IAction;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeId;
import org.eclipse.riena.navigation.IPresentationProviderService;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.model.PresentationProviderServiceAccessor;

/**
 * Manages the reference between the navigation nodes and the view id's
 */
public class SwtPresentationManager {

	private Map<INavigationNode<?>, SwtViewId> views;
	private Map<IAction, String> actions;
	private Map<String, Integer> viewCounter;
	private HashMap<String, Boolean> viewShared;
	private Map<ISubModuleNode, Boolean> activated;
	private SubModuleNodeListener subModuleNodeObserver;

	/**
	 * Create new instance and initialize
	 */
	public SwtPresentationManager() {
		super();
		viewCounter = new LinkedHashMap<String, Integer>();
		views = new LinkedHashMap<INavigationNode<?>, SwtViewId>();
		actions = new LinkedHashMap<IAction, String>();
		viewShared = new HashMap<String, Boolean>();
		activated = new LinkedHashMap<ISubModuleNode, Boolean>();
		subModuleNodeObserver = new SubModuleObserver();

	}

	public SwtViewId getSwtViewId(INavigationNode<?> pNode) {
		SwtViewId swtViewId = views.get(pNode);
		if (swtViewId == null) {
			INavigationNodeId nodeId = pNode.getNodeId();
			if (nodeId != null) {
				String viewId = (String) getPresentationProviderService().provideView(nodeId);
				boolean isViewShared = getPresentationProviderService().isViewShared(nodeId);
				viewShared.put(viewId, isViewShared);
				if (isViewShared && pNode instanceof ISubModuleNode) {
					ISubModuleNode subModuleNode = (ISubModuleNode) pNode;
					activated.put(subModuleNode, subModuleNode.isActivated());
					subModuleNode.addListener(subModuleNodeObserver);
					if (views.get(pNode) == null) {
						viewCounter.put(viewId, 0);
					}
					SwtViewId id = null;
					if (viewCounter.get(viewId) == 0) {
						// first node with this view
						id = new SwtViewId(viewId, "shared"); //$NON-NLS-1$
						views.put(pNode, id);
						viewCounter.put(viewId, 1);
					} else {
						// view has been referenced already
						id = views.get(getNavigationNode(viewId, null, ISubModuleNode.class, true));
						views.put(pNode, id);
					}
					return id;

				} else {
					swtViewId = new SwtViewId(viewId, getNextSecondaryId(viewId));
					views.put(pNode, swtViewId);
				}
			}
		}

		return swtViewId;
	}

	private final class SubModuleObserver extends SubModuleNodeListener {

		@Override
		public void activated(ISubModuleNode source) {
			markActivated(source, true);
		}

		private void markActivated(ISubModuleNode source, boolean active) {
			SwtViewId sourceId = views.get(source);
			for (INavigationNode<?> node : views.keySet()) {
				if (views.get(node) == sourceId) {
					ISubModuleNode subNode = node.getTypecastedAdapter(ISubModuleNode.class);
					if (subNode != null) {
						activated.put(subNode, subNode == source);
					}
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.riena.navigation.listener.NavigationNodeListener#deactivated
		 * (org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void deactivated(ISubModuleNode source) {
			super.deactivated(source);
			markActivated(source, false);
		}
	}

	public void present(INavigationNode<?> pNode, String pViewId) {
		if (viewCounter.get(pViewId) == null) {
			viewCounter.put(pViewId, 0);
		}
		ISubModuleNode subNode = pNode.getTypecastedAdapter(ISubModuleNode.class);
		if (subNode != null) {
			boolean shared = isViewShared(pViewId);
			if (shared) {
				// only shared views for subModuleNodes
				activated.put(subNode, false);
				subNode.addListener(subModuleNodeObserver);

				// there can be different nodes with the same viewId and
				// secondary
				// id
				if (viewCounter.get(pViewId) == 0) {
					// first node with this view
					SwtViewId id = new SwtViewId(pViewId, "shared"); //$NON-NLS-1$
					views.put(pNode, id);
					viewCounter.put(pViewId, 1);
				} else {
					// view has been referenced already
					SwtViewId id = views.get(getNavigationNode(pViewId, null, ISubModuleNode.class, true));
					views.put(pNode, id);
				}
				return;
			}
		}
		// classic way with one view per node
		views.put(pNode, new SwtViewId(pViewId, getNextSecondaryId(pViewId)));
	}

	private String getNextSecondaryId(String pViewId) {
		if (viewCounter.get(pViewId) == null) {
			viewCounter.put(pViewId, 0);
		}
		viewCounter.put(pViewId, viewCounter.get(pViewId) + 1);
		return String.valueOf(viewCounter.get(pViewId));
	}

	public void registerView(String viewId, boolean shared) {
		viewShared.put(viewId, shared);
	}

	private boolean isViewShared(String viewId) {
		Boolean result = this.viewShared.get(viewId);
		return result == null ? false : result;
	}

	public void present(IAction pAction, String pActionId) {
		actions.put(pAction, pActionId);
	}

	public String getActionId(IAction pAction) {
		return actions.get(pAction);
	}

	public IAction getAction(String pActionId) {
		for (IAction nextAction : actions.keySet()) {
			if (actions.get(nextAction).equals(pActionId)) {
				return nextAction;
			}
		}

		return null;
	}

	public <N extends INavigationNode<?>> N getNavigationNode(String pId, Class<N> pClass) {
		return getNavigationNode(pId, null, pClass);
	}

	public <N extends INavigationNode<?>> N getNavigationNode(String pId, String secondary, Class<N> pClass) {
		return getNavigationNode(pId, secondary, pClass, false);

	}

	public <N extends INavigationNode<?>> N getNavigationNode(String pId, String secondary, Class<N> pClass,
			boolean ignoreSharedState) {
		for (INavigationNode<?> next : views.keySet()) {
			SwtViewId nextViewId = views.get(next);
			if (nextViewId.getId().equals(pId) && //
					(secondary == null || secondary.equals(nextViewId.getSecondary()))) {
				if (ignoreSharedState || !isViewShared(pId) || next.isActivated()) {
					return next.getTypecastedAdapter(pClass);
				}
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	protected IPresentationProviderService getPresentationProviderService() {
		// TODO: handling if no service found ???
		return PresentationProviderServiceAccessor.current().getPresentationProviderService();
	}

}
