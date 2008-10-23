package org.eclipse.riena.navigation.ui.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeController;
import org.eclipse.riena.navigation.listener.NavigationNodeListener;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.IUIFilterAttribute;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;

/**
 * This class applies the UI filters after a filter was added or removed; also
 * this controller was activated.
 * 
 * @param <N>
 *            type of the navigation node
 */
public class NavigationUIFilterApplier<N> extends NavigationNodeListener {

	private final static IUIFilterAttributeClosure APPLY_CLOSURE = new ApplyClosure();
	private final static IUIFilterAttributeClosure REMOVE_CLOSURE = new RemoveClosure();

	/**
	 * Applies all the filters of the given node (and all filters of the parent
	 * nodes) to the given node.
	 * 
	 * @param node
	 *            - navigation node
	 */
	private void applyFilters(INavigationNode<?> node) {

		if (node == null) {
			return;
		}

		Collection<IUIFilter> filters = new ArrayList<IUIFilter>();
		collectFilters(node, filters);
		for (IUIFilter filter : filters) {
			applyFilter(node, filter, APPLY_CLOSURE);
		}

	}

	/**
	 * Adds the filters of the given node and of the parents nodes to the given
	 * collection of filters.
	 * 
	 * @param node
	 *            - navigation node
	 * @param filters
	 *            - collection of UI filters.
	 */
	private void collectFilters(INavigationNode<?> node, Collection<IUIFilter> filters) {

		if (node == null) {
			return;
		}

		if (node.getFilters() != null) {
			filters.addAll(node.getFilters());
		}
		collectFilters(node.getParent(), filters);

	}

	/**
	 * Executes the closure for the given filter to the given node and all of
	 * its child nodes.
	 * 
	 * @param node
	 *            - navigation node
	 * @param filter
	 *            - UI filter
	 * @param closure
	 *            - closure to execute
	 */
	private void applyFilter(INavigationNode<?> node, IUIFilter filter, IUIFilterAttributeClosure closure) {

		Collection<? extends IUIFilterAttribute> filterItems = filter.getFilterAttributes();
		for (IUIFilterAttribute filterAttribute : filterItems) {
			applyFilterAttribute(node, filterAttribute, closure);
		}

		List<?> children = node.getChildren();
		for (Object child : children) {
			if (child instanceof INavigationNode<?>) {
				applyFilter((INavigationNode<?>) child, filter, closure);
			}
		}

	}

	/**
	 * Executes the closure for the given filter attribute to the given node and
	 * all the ridgets.
	 * 
	 * @param node
	 *            - navigation node
	 * @param filterAttribute
	 *            - filter attribute
	 * @param closure
	 *            - closure to execute
	 */
	private void applyFilterAttribute(INavigationNode<?> node, IUIFilterAttribute filterAttribute,
			IUIFilterAttributeClosure closure) {

		if (filterAttribute.matches(node)) {
			closure.exeute(filterAttribute, node);
		}

		INavigationNodeController controller = node.getNavigationNodeController();
		if (controller instanceof IRidgetContainer) {
			IRidgetContainer container = (IRidgetContainer) controller;
			for (IRidget ridget : container.getRidgets()) {
				if (filterAttribute.matches(ridget)) {
					closure.exeute(filterAttribute, ridget);
				}
			}
		}

	}

	@Override
	public void afterActivated(INavigationNode source) {
		super.afterActivated(source);
		applyFilters(source);
	}

	@Override
	public void filterAdded(INavigationNode source, IUIFilter filter) {
		super.filterAdded(source, filter);
		applyFilter(source, filter, APPLY_CLOSURE);
	}

	@Override
	public void filterRemoved(INavigationNode source, IUIFilter filter) {
		super.filterRemoved(source, filter);
		applyFilter(source, filter, REMOVE_CLOSURE);
	}

	/**
	 * Closure to execute the {@code apply} method of {@link IUIFilterAttribute}
	 * .
	 * */
	private static class ApplyClosure implements IUIFilterAttributeClosure {

		public void exeute(IUIFilterAttribute attr, Object obj) {
			attr.apply(obj);
		}

	}

	/**
	 * Closure to execute the {@code remove} method of
	 * {@link IUIFilterAttribute}.
	 */
	private static class RemoveClosure implements IUIFilterAttributeClosure {

		public void exeute(IUIFilterAttribute attr, Object obj) {
			attr.remove(obj);
		}

	}

}