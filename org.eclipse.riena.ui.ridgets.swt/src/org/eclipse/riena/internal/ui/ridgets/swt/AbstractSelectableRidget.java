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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.ListBinding;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.databinding.IUnboundPropertyObservable;
import org.eclipse.riena.ui.ridgets.databinding.UnboundPropertyWritableList;

/**
 * Default implementation of an {@link ISelectableRidget}. This ridget than can
 * observe single and multiple selection of a widget and bind the selection
 * state to model elements.
 */
public abstract class AbstractSelectableRidget extends AbstractSWTRidget implements ISelectableRidget {

	/** A single selected bean Object. */
	private final WritableValue singleSelectionObservable;
	/** A list of selected bean Objects. */
	private final WritableList multiSelectionObservable;
	/** Binds the singleSelectionObservable to some model. */
	private Binding singleSelectionBinding;
	/** Binds the multiSelectionObservable to some model. */
	private ListBinding multiSelectionBinding;
	/** The selection type. */
	private SelectionType selectionType = SelectionType.SINGLE;

	public AbstractSelectableRidget() {
		singleSelectionObservable = new SingleSelectionObservable();
		multiSelectionObservable = new MultiSelectionObservable();
	}

	public final void bindMultiSelectionToModel(IObservableList modelObservableList) {
		if (multiSelectionBinding != null) {
			multiSelectionBinding.dispose();
		}
		DataBindingContext dbc = new DataBindingContext();
		multiSelectionBinding = (ListBinding) dbc.bindList(multiSelectionObservable, modelObservableList,
				new UpdateListStrategy(UpdateListStrategy.POLICY_UPDATE), new UpdateListStrategy(
						UpdateListStrategy.POLICY_ON_REQUEST));
	}

	public final void bindMultiSelectionToModel(Object pojo, String propertyName) {
		IObservableList observableList = new UnboundPropertyWritableList(pojo, propertyName);
		bindMultiSelectionToModel(observableList);
	}

	public final void bindSingleSelectionToModel(IObservableValue modelObservableValue) {
		if (singleSelectionBinding != null) {
			singleSelectionBinding.dispose();
		}
		DataBindingContext dbc = new DataBindingContext();
		singleSelectionBinding = dbc.bindValue(singleSelectionObservable, modelObservableValue,
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), new UpdateValueStrategy(
						UpdateValueStrategy.POLICY_ON_REQUEST));
	}

	public final void bindSingleSelectionToModel(Object pojo, String propertyName) {
		IObservableValue observableValue = PojoObservables.observeValue(pojo, propertyName);
		bindSingleSelectionToModel(observableValue);
	}

	public final void clearSelection() {
		singleSelectionObservable.setValue(null);
	}

	public boolean containsOption(Object option) {
		if (getRowObservables() == null) {
			return false;
		}
		return getRowObservables().contains(option);
	}

	public final IObservableList getMultiSelectionObservable() {
		return multiSelectionObservable;
	}

	public final List<Object> getSelection() {
		List<Object> result = new ArrayList<Object>();
		if (SelectionType.SINGLE.equals(selectionType)) {
			if (singleSelectionObservable.getValue() != null) {
				result.add(singleSelectionObservable.getValue());
			}
		} else if (SelectionType.MULTI.equals(selectionType)) {
			result.addAll(Arrays.asList(multiSelectionObservable.toArray()));
		}
		return result;
	}

	public final SelectionType getSelectionType() {
		return selectionType;
	}

	public final IObservableValue getSingleSelectionObservable() {
		return singleSelectionObservable;
	}

	public void setSelection(List<?> newSelection) {
		assertIsBoundToModel();
		List<?> knownElements = getKnownElements(newSelection);
		if (SelectionType.SINGLE.equals(selectionType)) {
			Object value = knownElements.size() > 0 ? knownElements.get(0) : null;
			singleSelectionObservable.setValue(value);
		} else if (SelectionType.MULTI.equals(selectionType)) {
			ListDiff diff = Diffs.computeListDiff(multiSelectionObservable, knownElements);
			if (diff.getDifferences().length > 0) {
				multiSelectionObservable.clear();
				multiSelectionObservable.addAll(knownElements);
			}
		}
	}

	public final void setSelection(Object newSelection) {
		assertIsBoundToModel();
		List<Object> list = Arrays.asList(new Object[] { newSelection });
		setSelection(list);
	}

	public void setSelectionType(SelectionType selectionType) {
		Assert.isNotNull(selectionType, "selectionType cannot be null"); //$NON-NLS-1$
		if (SelectionType.NONE.equals(selectionType)) {
			throw new IllegalArgumentException("SelectionType.NONE is not supported by the UI-control"); //$NON-NLS-1$
		}
		if (!this.selectionType.equals(selectionType)) {
			this.selectionType = selectionType;
		}
	}

	public final void updateMultiSelectionFromModel() {
		if (multiSelectionBinding != null) {
			IObservable model = multiSelectionBinding.getModel();
			if (model instanceof IUnboundPropertyObservable) {
				((UnboundPropertyWritableList) model).updateFromBean();
			}
			multiSelectionBinding.updateModelToTarget();
		}
	}

	public final void updateSingleSelectionFromModel() {
		if (singleSelectionBinding != null) {
			IObservable model = singleSelectionBinding.getModel();
			if (model instanceof IUnboundPropertyObservable) {
				((UnboundPropertyWritableList) model).updateFromBean();
			}
			singleSelectionBinding.updateModelToTarget();
		}
	}

	// protected methods
	// //////////////////

	/**
	 * Return an observable list of objects which can be selected through this
	 * ridget.
	 * 
	 * @return a List instance or null, if the ridget has not been bound to a
	 *         model
	 */
	abstract protected List<?> getRowObservables();

	/**
	 * Throws an exception if no model observables are available (i.e.
	 * getRowObservables() == null).
	 */
	protected final void assertIsBoundToModel() {
		if (getRowObservables() == null) {
			throw new BindingException("ridget not bound to model"); //$NON-NLS-1$
		}
	}

	// helping methods
	// ////////////////

	private List<Object> getKnownElements(List<?> elements) {
		List<Object> result = new ArrayList<Object>();
		Collection<?> rowObservables = getRowObservables();
		for (Object element : elements) {
			if (rowObservables.contains(element)) {
				result.add(element);
			}
		}
		return result;
	}

	// helping classes
	// ////////////////

	/**
	 * Observable value bean for single selection. This class is used by this
	 * ridget to monitor and maintain the selection state for single selection
	 * and fire appropriate events.
	 */
	private final class SingleSelectionObservable extends WritableValue {

		SingleSelectionObservable() {
			super(null, Object.class);
		}

		@Override
		protected void fireValueChange(ValueDiff diff) {
			super.fireValueChange(diff);
			String key = ISelectableRidget.PROPERTY_SELECTION;
			Object oldValue = diff.getOldValue();
			Object newValue = diff.getNewValue();
			AbstractSelectableRidget.this.firePropertyChange(key, oldValue, newValue);
		}
	};

	/**
	 * Observable list bean for multiple selection. This class is used by this
	 * ridget to monitor and maintain the selection state for multiple selection
	 * and fire appropriate events.
	 */
	private final class MultiSelectionObservable extends WritableList {

		MultiSelectionObservable() {
			super(new ArrayList<Object>(), Object.class);
		}

		@Override
		protected void fireListChange(ListDiff diff) {
			super.fireListChange(diff);
			List<Object> newSelection = Arrays.asList(toArray());
			List<Object> oldSelection = computeOldSelection(diff, newSelection);
			String key = ISelectableRidget.PROPERTY_SELECTION;
			AbstractSelectableRidget.this.propertyChangeSupport.firePropertyChange(key, oldSelection, newSelection);
		}

		private List<Object> computeOldSelection(ListDiff diff, List<Object> newSelection) {
			List<Object> oldSelection = new ArrayList<Object>();
			oldSelection.addAll(newSelection);
			for (ListDiffEntry entry : diff.getDifferences()) {
				Object element = entry.getElement();
				if (entry.isAddition()) {
					oldSelection.remove(element);
				} else {
					oldSelection.add(element);
				}
			}
			return oldSelection;
		}
	}

}
