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
package org.eclipse.riena.internal.navigation.ui.swt.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.runtime.IExecutableExtension;

import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;

/**
 * Handler for navigation related actions.
 */
abstract class AbstractNavigationHandler extends AbstractHandler implements IExecutableExtension {

	/**
	 * TODO [ev] docs
	 */
	@SuppressWarnings("unchecked")
	protected final IModuleNode[] collectSubModules(IApplicationNode application) {
		List<IModuleNode> modules = new ArrayList<IModuleNode>();
		INavigationNode<?> subApplication = findActive((List) application.getChildren());
		if (subApplication instanceof ISubApplicationNode) {
			List<IModuleGroupNode> groups = ((ISubApplicationNode) subApplication).getChildren();
			for (IModuleGroupNode moduleGroup : groups) {
				modules.addAll(moduleGroup.getChildren());
			}
		}
		return modules.toArray(new IModuleNode[modules.size()]);
	}

	/**
	 * TODO [ev] docs
	 */
	protected final INavigationNode<?> findActive(List<INavigationNode<?>> children) {
		INavigationNode<?> result = null;
		Iterator<INavigationNode<?>> iter = children.iterator();
		while (result == null && iter.hasNext()) {
			INavigationNode<?> candidate = iter.next();
			if (candidate.isActivated()) {
				result = candidate;
			}
		}
		return result;
	}

	/**
	 * TODO [ev] docs
	 */
	protected final INavigationNode<?> findNextNode(INavigationNode<?>[] nodes) {
		INavigationNode<?> result = null;
		int selectedCount = 0;
		for (INavigationNode<?> node : nodes) {
			if (isSelected(node)) {
				selectedCount++;
			} else {
				if (selectedCount == 1 && result == null) { // previous else implies !selected
					result = node;
				}
			}
		}
		if (selectedCount == 1 && result == null && !isSelected(nodes[0])) { // wrap around
			result = nodes[0];
		}
		return selectedCount == 1 ? result : null;
	}

	/**
	 * TODO [ev] docs
	 */
	protected final INavigationNode<?> findPreviousNode(INavigationNode<?>[] nodes) {
		INavigationNode<?> result = null;
		int selectedCount = 0;
		for (int i = nodes.length - 1; i >= 0; i--) {
			INavigationNode<?> node = nodes[i];
			if (isSelected(node)) {
				selectedCount++;
			} else {
				if (selectedCount == 1 && result == null) { // previous else implies !selected
					result = node;
				}
			}
		}
		int lastApp = nodes.length - 1;
		if (selectedCount == 1 && result == null && !isSelected(nodes[lastApp])) {
			result = nodes[lastApp];
		}
		return selectedCount == 1 ? result : null;
	}

	// helping methods
	//////////////////

	private boolean isSelected(INavigationNode<?> node) {
		return node.isSelected() && node.getParent().isSelected();
	}

}
