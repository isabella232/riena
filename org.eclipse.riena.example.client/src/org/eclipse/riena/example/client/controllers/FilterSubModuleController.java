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
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.IUIFilterAttribute;
import org.eclipse.riena.ui.filter.UIFilter;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboBoxRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.filter.RidgetUIFilterAttributeDisabledMarker;
import org.eclipse.riena.ui.ridgets.filter.RidgetUIFilterAttributeHiddenMarker;
import org.eclipse.riena.ui.ridgets.filter.RidgetUIFilterAttributeMandatoryMarker;
import org.eclipse.riena.ui.ridgets.filter.RidgetUIFilterAttributeOutputMarker;

/**
 *
 */
public class FilterSubModuleController extends SubModuleController {

	private IComboBoxRidget filterTypeValues;
	private FilterModel filterModel;
	private IActionRidget addFilter;

	private enum FilterType {

		MARKER("Marker", new MandatoryMarker(false), new HiddenMarker(false), new OutputMarker(false),
				new DisabledMarker(false));

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

		IComboBoxRidget ridgetID = (IComboBoxRidget) getRidget("ridgetID");
		filterModel = new FilterModel();
		ridgetID.bindToModel(filterModel, "ids", FilterModel.class, null, filterModel, "selectedId");
		ridgetID.updateFromModel();

		ISingleChoiceRidget filterType = (ISingleChoiceRidget) getRidget("filterType"); //$NON-NLS-1$		
		filterType.addPropertyChangeListener(new FilterTypeChangeListener());
		filterType.bindToModel(filterModel, "types", filterModel, "selectedType");
		filterType.updateFromModel();

		filterTypeValues = (IComboBoxRidget) getRidget("filterTypeValues");
		filterTypeValues.addPropertyChangeListener(new FilterValueListener());

		addFilter = (IActionRidget) getRidget("addFilter");
		addFilter.addListener(new IActionListener() {
			public void callback() {
				doAdd();
			}
		});

		IActionRidget removeFilters = (IActionRidget) getRidget("removeFilters");
		removeFilters.addListener(new IActionListener() {
			public void callback() {
				doRemove();
			}
		});

		rebindFilterTypeValues();

	}

	private void doAdd() {
		IUIFilter filter = new UIFilter();
		filter.addFilterAttribute(createFilterAttribute());
		getNavigationNode().addFilter(filter);
	}

	private IUIFilterAttribute createFilterAttribute() {

		IUIFilterAttribute attribute = null;

		String id = filterModel.getSelectedId();
		Object filterValue = filterModel.getSelectedFilterTypeValue();

		if (filterValue instanceof OutputMarker) {
			attribute = new RidgetUIFilterAttributeOutputMarker(id, (OutputMarker) filterValue);
		} else if (filterValue instanceof DisabledMarker) {
			attribute = new RidgetUIFilterAttributeDisabledMarker(id, (DisabledMarker) filterValue);
		} else if (filterValue instanceof MandatoryMarker) {
			attribute = new RidgetUIFilterAttributeMandatoryMarker(id, (MandatoryMarker) filterValue);
		} else if (filterValue instanceof HiddenMarker) {
			attribute = new RidgetUIFilterAttributeHiddenMarker(id, (HiddenMarker) filterValue);
		}

		return attribute;

	}

	private void doRemove() {
		getNavigationNode().removeAllFilters();
	}

	private void rebindFilterTypeValues() {

		filterModel.setSelectedFilterTypeValue(null);
		if (filterTypeValues != null) {
			filterTypeValues.bindToModel(new WritableList(Arrays.asList(filterModel.getSelectedType().getArgs()),
					Object.class), FilterModel.class, null, BeansObservables.observeValue(filterModel,
					"selectedFilterTypeValue"));
			filterTypeValues.updateFromModel();
		}
		if (addFilter != null) {
			addFilter.setEnabled(filterModel.getSelectedFilterTypeValue() != null);
		}

	}

	private class FilterTypeChangeListener implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			rebindFilterTypeValues();
		}

	}

	private class FilterValueListener implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			if (addFilter != null) {
				addFilter.setEnabled(evt.getNewValue() != null);
			}
		}

	}

	private class FilterModel {

		private List<String> ids;
		private String selectedId;
		private List<FilterType> types;
		private FilterType selectedType;
		private Object selectedFilterTypeValue;

		public List<String> getIds() {
			if (ids == null) {
				ids = new ArrayList<String>();
				Collection<? extends IRidget> ridgets = getRidgets();
				for (IRidget ridget : ridgets) {
					if ((ridget.getID() != null) && (ridget.getID().startsWith("ui_"))) {
						ids.add(ridget.getID());
					}
				}
			}
			return ids;
		}

		public void setSelectedId(String selectedId) {
			this.selectedId = selectedId;
		}

		public String getSelectedId() {
			if (selectedId == null) {
				selectedId = getIds().get(0);
			}
			return selectedId;
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

	}

}
