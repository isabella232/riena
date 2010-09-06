/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
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
import java.util.Map.Entry;

import org.eclipse.riena.core.singleton.SessionSingletonProvider;
import org.eclipse.riena.navigation.ApplicationModelFailure;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 * Manages the reference between the navigation nodes and the view id's
 */
public class SwtViewProvider {

	private final Map<INavigationNode<?>, SwtViewId> views;
	private final Map<String, Integer> viewCounter;
	private final HashMap<String, Boolean> viewShared;

	private static final SessionSingletonProvider<SwtViewProvider> SVP = new SessionSingletonProvider<SwtViewProvider>(
			SwtViewProvider.class);

	/**
	 * Gets the singleton SwtViewProvider.
	 * 
	 * @return
	 * @since 1.2
	 */
	public static SwtViewProvider getInstance() {
		return SVP.getInstance();
	}

	/**
	 * Create new instance and initialize
	 */
	protected SwtViewProvider() {
		super();
		viewCounter = new LinkedHashMap<String, Integer>();
		views = new LinkedHashMap<INavigationNode<?>, SwtViewId>();
		viewShared = new HashMap<String, Boolean>();
	}

	public SwtViewId getSwtViewId(final INavigationNode<?> node) {

		SwtViewId swtViewId = views.get(node);
		if (swtViewId == null) {
			final ISubModuleNode submodule = node.getTypecastedAdapter(ISubModuleNode.class);
			// submodules need special treatment as they may be shared 
			if (submodule != null && submodule.getNodeId() != null) {
				swtViewId = createAndRegisterSwtViewId(submodule);
			} else {
				swtViewId = createAndRegisterSwtViewId(node);
			}
		}

		return swtViewId;
	}

	public void unregisterSwtViewId(final INavigationNode<?> node) {
		views.remove(node);
	}

	public void replaceNavigationNodeId(final INavigationNode<?> node, final NavigationNodeId newId) {
		final SwtViewId swtViewId = views.get(node);
		if (swtViewId != null) {
			views.remove(node);
			node.setNodeId(newId);
			views.put(node, swtViewId);
		}
	}

	private SwtViewId createAndRegisterSwtViewId(final INavigationNode<?> node) {

		final IWorkareaDefinition def = WorkareaManager.getInstance().getDefinition(node);
		final String viewId = getViewId(node, def);

		final SwtViewId swtViewId = new SwtViewId(viewId, getNextSecondaryId(viewId));
		views.put(node, swtViewId);

		return swtViewId;
	}

	private SwtViewId createAndRegisterSwtViewId(final ISubModuleNode submodule) {

		final IWorkareaDefinition def = WorkareaManager.getInstance().getDefinition(submodule);
		final String viewId = getViewId(submodule, def);

		if (viewShared.get(viewId) != null) {
			if (def.isViewShared() != viewShared.get(viewId)) {
				throw new ApplicationModelFailure(
						"Inconsistent view usage. The view with the id \"" //$NON-NLS-1$
								+ viewId
								+ "\" is already used with a different 'shared' state. A view must be defined in all workarea definitions as either shared or not shared."); //$NON-NLS-1$
			}
		} else {
			viewShared.put(viewId, def.isViewShared());
		}
		SwtViewId swtViewId = null;
		if (def.isViewShared()) {
			if (views.get(submodule) == null) {
				viewCounter.put(viewId, 0);
			}
			if (viewCounter.get(viewId) == 0) {
				// first node with this view
				swtViewId = new SwtViewId(viewId, "shared"); //$NON-NLS-1$
				views.put(submodule, swtViewId);
				viewCounter.put(viewId, 1);
			} else {
				// view has been referenced already
				swtViewId = views.get(getNavigationNode(viewId, null, ISubModuleNode.class, true));
				views.put(submodule, swtViewId);
			}
		} else {
			swtViewId = new SwtViewId(viewId, getNextSecondaryId(viewId));
			views.put(submodule, swtViewId);
		}

		return swtViewId;
	}

	private String getViewId(final INavigationNode<?> node, final IWorkareaDefinition def) {
		if (def == null) {
			throw new ApplicationModelFailure("no work area definition for node " + node.getNodeId()); //$NON-NLS-1$
		}
		final Object viewId = def.getViewId();
		if (viewId == null) {
			throw new ApplicationModelFailure("viewId is null for nodeId " + node.getNodeId()); //$NON-NLS-1$
		}
		if (!(viewId instanceof String)) {
			throw new ApplicationModelFailure("viewId is not a String for nodeId " + node.getNodeId()); //$NON-NLS-1$
		}
		return (String) viewId;
	}

	private String getNextSecondaryId(final String pViewId) {
		if (viewCounter.get(pViewId) == null) {
			viewCounter.put(pViewId, 0);
		}
		viewCounter.put(pViewId, viewCounter.get(pViewId) + 1);
		return String.valueOf(viewCounter.get(pViewId));
	}

	private boolean isViewShared(final String viewId) {
		final Boolean result = this.viewShared.get(viewId);
		return result == null ? false : result;
	}

	public <N extends INavigationNode<?>> N getNavigationNode(final String pId, final Class<N> pClass) {
		return getNavigationNode(pId, null, pClass);
	}

	public <N extends INavigationNode<?>> N getNavigationNode(final String pId, final String secondary,
			final Class<N> pClass) {
		return getNavigationNode(pId, secondary, pClass, false);

	}

	public <N extends INavigationNode<?>> N getNavigationNode(final String pId, final String secondary,
			final Class<N> pClass, final boolean ignoreSharedState) {
		for (final Entry<INavigationNode<?>, SwtViewId> entry : views.entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}
			if (entry.getValue().getId().equals(pId) && //
					(secondary == null || secondary.equals(entry.getValue().getSecondary()))) {
				if (ignoreSharedState || !isViewShared(pId) || entry.getKey().isActivated()) {
					return entry.getKey().getTypecastedAdapter(pClass);
				}
			}
		}
		return null;
	}
}
