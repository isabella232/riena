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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.navigation.ui.filter.NavigationUIFilterAttributeDisabledMarker;
import org.eclipse.riena.internal.navigation.ui.filter.NavigationUIFilterAttributeHiddenMarker;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.IUIFilterAttribute;
import org.eclipse.riena.ui.filter.IUIFilterNavigationMarkerAttribute;
import org.eclipse.riena.ui.filter.impl.UIFilter;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboBoxRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;

/**
 * Controller of the sub module that demonstrates UI filters for navigation
 * nodes.
 */
public class FilterNavigationSubModuleController extends SubModuleController {

	private IActionRidget addFilter;
	private IComboBoxRidget filterTypeValues;
	private FilterModel filterModel;

	/**
	 * Enumeration of different kind of UI filters.
	 */
	private enum FilterType {

		MARKER("Marker", new DisabledMarker(), new HiddenMarker()); //$NON-NLS-1$

		private String text;
		private Object[] args;

		private FilterType(String text, Object... args) {
			this.text = text;
			this.args = args;
		}

		@Override
		public String toString() {
			return text;
		}

		public Object[] getArgs() {
			return args;
		}

	}

	@Override
	public void afterBind() {

		super.afterBind();

		initNavigationFilterGroup();

		rebindFilterTypeValues(filterModel, filterTypeValues, addFilter);

	}

	/**
	 * Initializes the ridgets for adding UI filters.
	 */
	private void initNavigationFilterGroup() {

		ITextFieldRidget ridgetID = (ITextFieldRidget) getRidget("nodeLabel"); //$NON-NLS-1$
		filterModel = new FilterModel();
		ridgetID.bindToModel(filterModel, "nodeLabel"); //$NON-NLS-1$
		ridgetID.updateFromModel();

		ISingleChoiceRidget filterType = (ISingleChoiceRidget) getRidget("filterType"); //$NON-NLS-1$		
		filterType.addPropertyChangeListener(new FilterTypeChangeListener());
		filterType.bindToModel(filterModel, "types", filterModel, "selectedType"); //$NON-NLS-1$ //$NON-NLS-2$
		filterType.updateFromModel();

		filterTypeValues = (IComboBoxRidget) getRidget("filterTypeValues"); //$NON-NLS-1$
		filterTypeValues.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (addFilter != null) {
					addFilter.setEnabled(evt.getNewValue() != null);
				}
			}
		});

		addFilter = (IActionRidget) getRidget("addFilter"); //$NON-NLS-1$
		addFilter.addListener(new IActionListener() {
			public void callback() {
				doAddFilter();
			}
		});

		IActionRidget removeFilters = (IActionRidget) getRidget("removeFilters"); //$NON-NLS-1$
		removeFilters.addListener(new IActionListener() {
			public void callback() {
				doRemoveFilters();
			}
		});

	}

	/**
	 * The combo box for filter values is update with the given model. Also the
	 * add button is enabled or disabled.
	 * 
	 * @param model
	 * @param typeValues
	 *            - combo box
	 * @param add
	 *            - add button
	 */
	private void rebindFilterTypeValues(FilterModel model, IComboBoxRidget typeValues, IActionRidget add) {

		if (model == null) {
			return;
		}
		model.setSelectedFilterTypeValue(null);
		if (typeValues != null) {
			typeValues.bindToModel(new WritableList(Arrays.asList(model.getSelectedType().getArgs()), Object.class),
					FilterModel.class, null, BeansObservables.observeValue(model, "selectedFilterTypeValue")); //$NON-NLS-1$
			typeValues.updateFromModel();
		}
		if (add != null) {
			add.setEnabled(model.getSelectedFilterTypeValue() != null);
		}

	}

	/**
	 * Adds a filter to a node.
	 */
	private void doAddFilter() {

		List<INavigationNode<?>> nodes = findNodes(filterModel.getNodeLabel());
		for (INavigationNode<?> node : nodes) {
			IUIFilter filter = new UIFilter();
			filter.addFilterAttribute(createFilterAttribute(filterModel, node));

			node.addFilter(filter);
		}

	}

	/**
	 * Removes all filters form a node.
	 */
	private void doRemoveFilters() {

		List<INavigationNode<?>> nodes = findNodes(filterModel.getNodeLabel());
		for (INavigationNode<?> node : nodes) {
			node.removeAllFilters();
		}

	}

	/**
	 * Creates a filter attribute for a ridget, dependent on the selected type
	 * of filter.
	 * 
	 * @param model
	 *            - model with selections.
	 * @return filter attribute
	 */
	private IUIFilterAttribute createFilterAttribute(FilterModel model, INavigationNode<?> node) {

		IUIFilterAttribute attribute = null;

		Object filterValue = model.getSelectedFilterTypeValue();
		FilterType type = model.getSelectedType();

		switch (type) {
		case MARKER:
			if (filterValue instanceof DisabledMarker) {
				attribute = new NavigationUIFilterAttributeDisabledMarker(node.getNodeId().getInstanceId());
			} else if (filterValue instanceof HiddenMarker) {
				attribute = new NavigationUIFilterAttributeHiddenMarker(node.getNodeId().getInstanceId());
			}

			((IUIFilterNavigationMarkerAttribute) attribute).setNode("org.eclipse.riena.example.uiProcess.demo1");
			break;
		}

		return attribute;

	}

	/**
	 * Returns all node with the given label.
	 * 
	 * @param label
	 * @return list of found nodes.
	 */
	private List<INavigationNode<?>> findNodes(String label) {

		List<INavigationNode<?>> nodes = new ArrayList<INavigationNode<?>>();

		IApplicationNode applNode = getNavigationNode().getParentOfType(IApplicationNode.class);
		findNodes(label, applNode, nodes);

		return nodes;

	}

	private void findNodes(String label, INavigationNode<?> node, List<INavigationNode<?>> nodes) {

		if (StringUtils.equals(node.getLabel(), label)) {
			nodes.add(node);
		}
		List<?> children = node.getChildren();
		for (Object child : children) {
			if (child instanceof INavigationNode<?>) {
				findNodes(label, (INavigationNode<?>) child, nodes);
			}
		}

	}

	/**
	 * After changing the type the combo box with the values must be updated.
	 */
	private class FilterTypeChangeListener implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			rebindFilterTypeValues(filterModel, filterTypeValues, addFilter);
		}

	}

	/**
	 * Model with the filter types and value of the filter group.
	 */
	private class FilterModel {

		private String nodeLabel;
		private List<FilterType> types;
		private FilterType selectedType;
		private Object selectedFilterTypeValue;

		public FilterModel() {
			nodeLabel = ""; //$NON-NLS-1$
		}

		public List<FilterType> getTypes() {
			if (types == null) {
				types = new ArrayList<FilterType>();
				types.add(FilterType.MARKER);
			}
			return types;
		}

		public void setSelectedType(FilterType selectedType) {
			this.selectedType = selectedType;
		}

		public FilterType getSelectedType() {
			if (selectedType == null) {
				selectedType = getTypes().get(0);
			}
			return selectedType;
		}

		public void setSelectedFilterTypeValue(Object selectedFilterTypeValue) {
			this.selectedFilterTypeValue = selectedFilterTypeValue;
		}

		public Object getSelectedFilterTypeValue() {
			return selectedFilterTypeValue;
		}

		public void setNodeLabel(String nodeLabel) {
			this.nodeLabel = nodeLabel;
		}

		public String getNodeLabel() {
			return nodeLabel;
		}

	}

}
