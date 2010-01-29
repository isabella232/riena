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
package org.eclipse.riena.navigation.ui.swt.presentation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.riena.navigation.ApplicationModelFailure;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 * Manages the reference between the navigation nodes and the view id's
 */
public class SwtViewProvider {

	private final Map<INavigationNode<?>, SwtViewId> views;
	private final Map<String, Integer> viewCounter;
	private final HashMap<String, Boolean> viewShared;

	private static final SwtViewProvider SWT_VIEW_PROVIDER = new SwtViewProvider();

	/**
	 * Gets the singleton SwtViewProvider.
	 * 
	 * @return
	 * @since 1.2
	 */
	public static SwtViewProvider getInstance() {
		return SWT_VIEW_PROVIDER;
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

	public SwtViewId getSwtViewId(INavigationNode<?> node) {

		SwtViewId swtViewId = views.get(node);
		if (swtViewId == null) {
			ISubModuleNode submodule = node.getTypecastedAdapter(ISubModuleNode.class);
			// submodules need special treatment as they may be shared 
			if (submodule != null && submodule.getNodeId() != null) {
				swtViewId = createAndRegisterSwtViewId(submodule);
			} else {
				swtViewId = createAndRegisterSwtViewId(node);
			}
		}

		return swtViewId;
	}

	public void unregisterSwtViewId(INavigationNode<?> node) {
		views.remove(node);
	}

	private SwtViewId createAndRegisterSwtViewId(INavigationNode<?> node) {

		SwtViewId swtViewId = null;
		IWorkareaDefinition def = WorkareaManager.getInstance().getDefinition(node);
		String viewId = getViewId(node, def);

		swtViewId = new SwtViewId(viewId, getNextSecondaryId(viewId));
		views.put(node, swtViewId);

		return swtViewId;
	}

	private SwtViewId createAndRegisterSwtViewId(ISubModuleNode submodule) {

		SwtViewId swtViewId = null;
		IWorkareaDefinition def = WorkareaManager.getInstance().getDefinition(submodule);
		String viewId = getViewId(submodule, def);

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

	private String getViewId(INavigationNode<?> node, IWorkareaDefinition def) {
		if (def == null) {
			throw new ApplicationModelFailure("no work area definition for node " + node.getNodeId()); //$NON-NLS-1$
		}
		Object viewId = def.getViewId();
		if (viewId == null) {
			throw new ApplicationModelFailure("viewId is null for nodeId " + node.getNodeId()); //$NON-NLS-1$
		}
		if (!(viewId instanceof String)) {
			throw new ApplicationModelFailure("viewId is not a String for nodeId " + node.getNodeId()); //$NON-NLS-1$
		}
		return (String) viewId;
	}

	private String getNextSecondaryId(String pViewId) {
		if (viewCounter.get(pViewId) == null) {
			viewCounter.put(pViewId, 0);
		}
		viewCounter.put(pViewId, viewCounter.get(pViewId) + 1);
		return String.valueOf(viewCounter.get(pViewId));
	}

	private boolean isViewShared(String viewId) {
		Boolean result = this.viewShared.get(viewId);
		return result == null ? false : result;
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
			if (nextViewId == null) {
				continue;
			}
			if (nextViewId.getId().equals(pId) && //
					(secondary == null || secondary.equals(nextViewId.getSecondary()))) {
				if (ignoreSharedState || !isViewShared(pId) || next.isActivated()) {
					return next.getTypecastedAdapter(pClass);
				}
			}
		}
		return null;
	}
}
