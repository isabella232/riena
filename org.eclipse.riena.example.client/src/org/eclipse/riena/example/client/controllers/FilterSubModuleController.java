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
import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidget;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
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
	private MarkerModel markerModel;
	private IActionRidget addFilter;
	private IActionRidget addMarker;
	private IActionRidget removeMarker;
	private IMarker[] markers = new IMarker[] { new ErrorMarker(), new MandatoryMarker(), new HiddenMarker(),
			new OutputMarker(), new DisabledMarker() };

	private enum FilterType {

		MARKER("Marker", new MandatoryMarker(false), new HiddenMarker(false), new OutputMarker(false), //$NON-NLS-1$
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

		IComboBoxRidget ridgetToMarkID = (IComboBoxRidget) getRidget("ridgetToMarkID"); //$NON-NLS-1$
		markerModel = new MarkerModel();
		ridgetToMarkID.bindToModel(markerModel, "ids", MarkerModel.class, null, markerModel, "selectedId"); //$NON-NLS-1$ //$NON-NLS-2$
		ridgetToMarkID.updateFromModel();

		IComboBoxRidget markersCombo = (IComboBoxRidget) getRidget("markers"); //$NON-NLS-1$
		markersCombo.bindToModel(markerModel, "markers", MarkerModel.class, null, markerModel, "selectedMarker"); //$NON-NLS-1$ //$NON-NLS-2$
		markersCombo.updateFromModel();

		addMarker = (IActionRidget) getRidget("addMarker"); //$NON-NLS-1$
		addMarker.addListener(new IActionListener() {
			public void callback() {
				doAddMarker();
			}
		});

		removeMarker = (IActionRidget) getRidget("removeMarker"); //$NON-NLS-1$
		removeMarker.addListener(new IActionListener() {
			public void callback() {
				doRemoveMarker();
			}
		});

		IComboBoxRidget ridgetID = (IComboBoxRidget) getRidget("ridgetID"); //$NON-NLS-1$
		filterModel = new FilterModel();
		ridgetID.bindToModel(filterModel, "ids", FilterModel.class, null, filterModel, "selectedId"); //$NON-NLS-1$ //$NON-NLS-2$
		ridgetID.updateFromModel();

		ISingleChoiceRidget filterType = (ISingleChoiceRidget) getRidget("filterType"); //$NON-NLS-1$		
		filterType.addPropertyChangeListener(new FilterTypeChangeListener());
		filterType.bindToModel(filterModel, "types", filterModel, "selectedType"); //$NON-NLS-1$ //$NON-NLS-2$
		filterType.updateFromModel();

		filterTypeValues = (IComboBoxRidget) getRidget("filterTypeValues"); //$NON-NLS-1$
		filterTypeValues.addPropertyChangeListener(new FilterValueListener());

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

		rebindFilterTypeValues();

	}

	private void doAddFilter() {
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

	private void doRemoveFilters() {
		getNavigationNode().removeAllFilters();
	}

	private void rebindFilterTypeValues() {

		filterModel.setSelectedFilterTypeValue(null);
		if (filterTypeValues != null) {
			filterTypeValues.bindToModel(new WritableList(Arrays.asList(filterModel.getSelectedType().getArgs()),
					Object.class), FilterModel.class, null, BeansObservables.observeValue(filterModel,
					"selectedFilterTypeValue")); //$NON-NLS-1$
			filterTypeValues.updateFromModel();
		}
		if (addFilter != null) {
			addFilter.setEnabled(filterModel.getSelectedFilterTypeValue() != null);
		}

	}

	private void doAddMarker() {
		if (markerModel.getSelectedId() != null) {
			AbstractSWTRidget ridget = (AbstractSWTRidget) getRidget(markerModel.getSelectedId());
			ridget.addMarker(markerModel.getSelectedMarker());
		}
	}

	private void doRemoveMarker() {
		if (markerModel.getSelectedId() != null) {
			AbstractSWTRidget ridget = (AbstractSWTRidget) getRidget(markerModel.getSelectedId());
			ridget.removeMarker(markerModel.getSelectedMarker());
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

	private abstract class AbstractModel {

		private List<String> ids;
		private String selectedId;

		public List<String> getIds() {
			if (ids == null) {
				ids = new ArrayList<String>();
				Collection<? extends IRidget> ridgets = getRidgets();
				for (IRidget ridget : ridgets) {
					if ((ridget.getID() != null) && (ridget.getID().startsWith("ui_"))) { //$NON-NLS-1$
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

	}

	private class MarkerModel extends AbstractModel {

		private IMarker selectedMarker;

		public void setSelectedMarker(IMarker selectedMarker) {
			this.selectedMarker = selectedMarker;
		}

		public IMarker getSelectedMarker() {
			return selectedMarker;
		}

		public List<IMarker> getMarkers() {
			return Arrays.asList(markers);
		}

	}

	private class FilterModel extends AbstractModel {

		private List<FilterType> types;
		private FilterType selectedType;
		private Object selectedFilterTypeValue;

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
