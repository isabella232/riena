package org.eclipse.riena.example.client.controllers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.IUIFilterContainer;
import org.eclipse.riena.ui.filter.impl.UIFilterProviderAccessor;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

public class FilterExternalDefinitionSubModuleController extends SubModuleController {

	@Override
	public void afterBind() {

		super.afterBind();
		initRidgets();
	}

	/**
	 * Initializes the ridgets .
	 */
	private void initRidgets() {

		IActionRidget removeFilters = (IActionRidget) getRidget("removeOffline"); //$NON-NLS-1$
		removeFilters.addListener(new IActionListener() {
			public void callback() {
				doRemoveFilters();
			}
		});

		IActionRidget addFilters = (IActionRidget) getRidget("addOffline"); //$NON-NLS-1$
		addFilters.addListener(new IActionListener() {
			public void callback() {
				doAddFilters();
			}
		});

	}

	/**
	 * Adds a filter to a node.
	 */
	private void doAddFilters() {

		IUIFilterContainer container = UIFilterProviderAccessor.current().getUIFilterProvider().provideFilter(
				"rienaExample.offline");

		IUIFilter filter = container.getFilter();

		String targetNodeId = container.getFilterTargetNodeId();

		List<INavigationNode<?>> nodes = findNodes(targetNodeId);

		if (nodes != null && !nodes.isEmpty()) {
			nodes.get(0).addFilter(filter);
		}

	}

	/**
	 * Removes all filters form a node.
	 */
	private void doRemoveFilters() {

		IUIFilterContainer container = UIFilterProviderAccessor.current().getUIFilterProvider().provideFilter(
				"rienaExample.offline");

		IUIFilter filter = container.getFilter();

		String targetNodeId = container.getFilterTargetNodeId();

		List<INavigationNode<?>> nodes = findNodes(targetNodeId);

		if (nodes != null && !nodes.isEmpty()) {
			nodes.get(0).removeFilter(filter.getFilterID());
		}

	}

	/**
	 * Returns all node with the given label.
	 * 
	 * @param label
	 * @return list of found nodes.
	 */
	private List<INavigationNode<?>> findNodes(String id) {

		List<INavigationNode<?>> nodes = new ArrayList<INavigationNode<?>>();

		IApplicationNode applNode = getNavigationNode().getParentOfType(IApplicationNode.class);
		findNodes(id, applNode, nodes);

		return nodes;

	}

	private void findNodes(String id, INavigationNode<?> node, List<INavigationNode<?>> nodes) {

		if (node.getNodeId() != null && StringUtils.equals(node.getNodeId().getTypeId(), id)) {
			nodes.add(node);
		}
		List<?> children = node.getChildren();
		for (Object child : children) {
			if (child instanceof INavigationNode<?>) {
				findNodes(id, (INavigationNode<?>) child, nodes);
			}
		}

	}

}
