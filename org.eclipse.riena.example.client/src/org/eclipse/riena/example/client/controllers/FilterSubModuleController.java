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
import org.eclipse.riena.navigation.IApplicationNode;
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
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.filter.RidgetUIFilterAttributeDisabledMarker;
import org.eclipse.riena.ui.ridgets.filter.RidgetUIFilterAttributeHiddenMarker;
import org.eclipse.riena.ui.ridgets.filter.RidgetUIFilterAttributeMandatoryMarker;
import org.eclipse.riena.ui.ridgets.filter.RidgetUIFilterAttributeOutputMarker;

/**
 *
 */
public class FilterSubModuleController extends SubModuleController {

	private IComboBoxRidget filterTypeValues;
	private IComboBoxRidget globalFilterTypeValues;
	private FilterModel filterModel;
	private FilterModel globalFilterModel;
	private MarkerModel markerModel;
	private IActionRidget addFilter;
	private IActionRidget globalAddFilter;
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

		initMarkerGroup();
		initLocalFilterGroup();
		initGlobalFilterGroup();

		rebindFilterTypeValues(filterModel, filterTypeValues, addFilter);
		rebindFilterTypeValues(globalFilterModel, globalFilterTypeValues, globalAddFilter);

	}

	private void initMarkerGroup() {

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

	}

	private void initLocalFilterGroup() {

		IComboBoxRidget ridgetID = (IComboBoxRidget) getRidget("ridgetID"); //$NON-NLS-1$
		filterModel = new FilterModel();
		ridgetID.bindToModel(filterModel, "ids", FilterModel.class, null, filterModel, "selectedId"); //$NON-NLS-1$ //$NON-NLS-2$
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

	private void initGlobalFilterGroup() {

		ITextFieldRidget ridgetID = (ITextFieldRidget) getRidget("globalRidgetID"); //$NON-NLS-1$
		globalFilterModel = new FilterModel();
		ridgetID.bindToModel(globalFilterModel, "selectedId"); //$NON-NLS-1$
		ridgetID.updateFromModel();

		ISingleChoiceRidget filterType = (ISingleChoiceRidget) getRidget("globalFilterType"); //$NON-NLS-1$		
		filterType.addPropertyChangeListener(new FilterTypeChangeListener());
		filterType.bindToModel(globalFilterModel, "types", globalFilterModel, "selectedType"); //$NON-NLS-1$ //$NON-NLS-2$
		filterType.updateFromModel();

		globalFilterTypeValues = (IComboBoxRidget) getRidget("globalFilterTypeValues"); //$NON-NLS-1$
		globalFilterTypeValues.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (globalAddFilter != null) {
					globalAddFilter.setEnabled(evt.getNewValue() != null);
				}
			}
		});

		globalAddFilter = (IActionRidget) getRidget("globalAddFilter"); //$NON-NLS-1$
		globalAddFilter.addListener(new IActionListener() {
			public void callback() {
				doGlobalAddFilter();
			}
		});

		IActionRidget removeFilters = (IActionRidget) getRidget("globalRemoveFilters"); //$NON-NLS-1$
		removeFilters.addListener(new IActionListener() {
			public void callback() {
				doGlobalRemoveFilters();
			}
		});

	}

	private void doAddFilter() {
		IUIFilter filter = new UIFilter();
		filter.addFilterAttribute(createFilterAttribute(filterModel));
		getNavigationNode().addFilter(filter);
	}

	private void doGlobalAddFilter() {
		IUIFilter filter = new UIFilter();
		filter.addFilterAttribute(createFilterAttribute(globalFilterModel));
		IApplicationNode applNode = getNavigationNode().getParentOfType(IApplicationNode.class);
		applNode.addFilter(filter);
	}

	private IUIFilterAttribute createFilterAttribute(FilterModel model) {

		IUIFilterAttribute attribute = null;

		String id = model.getSelectedId();
		Object filterValue = model.getSelectedFilterTypeValue();

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

	private void doGlobalRemoveFilters() {
		IApplicationNode applNode = getNavigationNode().getParentOfType(IApplicationNode.class);
		applNode.removeAllFilters();
	}

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

	private void doAddMarker() {
		if (markerModel.getSelectedId() != null) {
			AbstractSWTRidget ridget = (AbstractSWTRidget) getRidget(markerModel.getSelectedId());
			ridget.addMarker(markerModel.getSelectedMarker().getMarker());
		}
	}

	private void doRemoveMarker() {
		if (markerModel.getSelectedId() != null) {
			AbstractSWTRidget ridget = (AbstractSWTRidget) getRidget(markerModel.getSelectedId());
			ridget.removeMarker(markerModel.getSelectedMarker().getMarker());
		}
	}

	private class FilterTypeChangeListener implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			rebindFilterTypeValues(filterModel, filterTypeValues, addFilter);
			rebindFilterTypeValues(globalFilterModel, globalFilterTypeValues, globalAddFilter);
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

		private MarkerWrapper selectedMarker;
		private List<MarkerWrapper> markerWrappers;

		public void setSelectedMarker(MarkerWrapper selectedMarker) {
			this.selectedMarker = selectedMarker;
		}

		public MarkerWrapper getSelectedMarker() {
			return selectedMarker;
		}

		public List<MarkerWrapper> getMarkers() {
			if (markerWrappers == null) {
				markerWrappers = new ArrayList<MarkerWrapper>(markers.length);
				for (IMarker marker : markers) {
					markerWrappers.add(new MarkerWrapper(marker));
				}
			}
			return markerWrappers;
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

	private class MarkerWrapper {

		private IMarker marker;

		public MarkerWrapper(IMarker marker) {
			this.marker = marker;
		}

		public IMarker getMarker() {
			return marker;
		}

		@Override
		public String toString() {
			if (getMarker() == null) {
				return "";
			}
			return getMarker().getClass().getSimpleName();
		}

	}

}
