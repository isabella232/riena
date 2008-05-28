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
package org.eclipse.riena.ui.ridgets;

import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;

/**
 * A Ridget that supports single and multiple selection.
 */
public interface ISelectableRidget extends IMarkableRidget {

	/**
	 * Property name of the single selection property.
	 * <p>
	 * If the selection type is set to multiple selection and changes the
	 * {@link #PROPERTY_SINGLE_SELECTION} will still be fired. The single
	 * selection will then contain one of the selected items.
	 * 
	 * @deprecated use {@link #PROPERTY_SELECTION}
	 */
	String PROPERTY_SINGLE_SELECTION = "singleSelection"; //$NON-NLS-1$

	/**
	 * Property name of the multiple selection property.
	 * <p>
	 * If the selection type is set to single selection the
	 * {@link #PROPERTY_MULTI_SELECTION} will still be fired. The multiple
	 * selection will be either empty or contain the single selected item.
	 * 
	 * @deprecated use {@link #PROPERTY_SELECTION}
	 */
	String PROPERTY_MULTI_SELECTION = "selection"; //$NON-NLS-1$

	/**
	 * Property name of the selection property.
	 * <p>
	 * This property will be fired every time the selection changes. If the
	 * selection type is single selection, the selection will contain zero or
	 * one items. If the selection type is multiple selection, the selection
	 * will contain zero or more items.
	 */
	String PROPERTY_SELECTION = "selection"; //$NON-NLS-1$

	/**
	 * The selection type.
	 */
	enum SelectionType {
		/** no selection */
		NONE,
		/** single selection */
		SINGLE,
		/** multiple selection */
		MULTI
	}

	/**
	 * @return The selection type.
	 */
	SelectionType getSelectionType();

	/**
	 * Sets the selection type.
	 * 
	 * @param selectionType
	 *            The new selection type. Never null.
	 * @throws RuntimeException
	 *             (a) if the given selectionType is not supported by the
	 *             ridget; (b) if selectionType is null
	 */
	void setSelectionType(SelectionType selectionType);

	/**
	 * @return An observable value that holds the single selection of the table.
	 *         If the selection type is set to multiple selection the single
	 *         selection will contain one of the selected items.
	 */
	IObservableValue getSingleSelectionObservable();

	/**
	 * Binds an observable value to the single selection.
	 * 
	 * @see #getSingleSelectionObservable()
	 * @param observableValue
	 *            The single selection model.
	 */
	void bindSingleSelectionToModel(IObservableValue observableValue);

	/**
	 * Binds an bean property to the single selection.
	 * 
	 * @see #getSingleSelectionObservable()
	 * @param bean
	 *            The bean holding the single selection model property.
	 * @param propertyName
	 *            The name of the single selection model property.
	 */
	void bindSingleSelectionToModel(Object bean, String propertyName);

	/**
	 * Updates the single selection from the single selection model.
	 * 
	 * @see #bindSingleSelectionToModel(IObservableValue)
	 * @see #bindSingleSelectionToModel(Object, String)
	 */
	void updateSingleSelectionFromModel();

	/**
	 * @return An observable list that holds the multiple selection of the
	 *         table. If the selection type is set to single selection the
	 *         multiple selection will be either empty or contain the single
	 *         selected item.
	 */
	IObservableList getMultiSelectionObservable();

	/**
	 * Binds an observable list to the multiple selection.
	 * 
	 * @see #getMultiSelectionObservable()
	 * @param observableList
	 *            The multiple selection model.
	 */
	void bindMultiSelectionToModel(IObservableList observableList);

	/**
	 * Binds an bean property to the multiple selection. The property must be a
	 * Collection.
	 * 
	 * @see #getMultiSelectionObservable()
	 * @param bean
	 *            The bean holding the multiple selection model property.
	 * @param propertyName
	 *            The name of the multiple selection model property.
	 */
	void bindMultiSelectionToModel(Object bean, String propertyName);

	/**
	 * Updates the multiple selection from the multiple selection model.
	 * 
	 * @see #bindMultiSelectionToModel(IObservableList)
	 * @see #bindMultiSelectionToModel(Object, String)
	 */
	void updateMultiSelectionFromModel();

	/**
	 * Deselects all selected items.
	 */
	void clearSelection();

	/**
	 * Returns a list of the selected items.
	 * 
	 * @return list of selected items; never null; may be empty.
	 */
	List<Object> getSelection();

	/**
	 * Returns an array of indices of the selected items.
	 * 
	 * @return indices of the selected items; never null; may be empty
	 */
	int[] getSelectionIndices();

	/**
	 * Return the index of the first selected item or -1 if none.
	 * 
	 * @return index of the first selected item or -1 if none
	 */
	int getSelectionIndex();

	/**
	 * Selects the given items. Items that are not in the set of selectable
	 * options will be ignored.
	 * 
	 * @param newSelection
	 *            a List of items to select. Never null. May be empty.
	 * @throws RuntimeException
	 *             (a) when there is no bound model to select from; (b) if
	 *             newSelection is null.
	 */
	void setSelection(List<?> newSelection);

	/**
	 * Selects the given item. If the item is not in the set of selectable
	 * options it will be ignored.
	 * 
	 * @param newSelection
	 *            item to select
	 * @throws RuntimeException
	 *             when there is no bound model to select from
	 */
	void setSelection(Object newSelection);

	/**
	 * Selects the item of the given row.
	 * 
	 * @param index
	 *            a 0-based index of the row to select.
	 * @throws RuntimeException
	 *             (a) if the index is out of bounds (index &lt; 0 || index &ge;
	 *             getOptionCount()); (b) when there is no bound model to select
	 *             from
	 */
	void setSelection(int index);

	/**
	 * Selects the items of the given rows.
	 * 
	 * @param indices
	 *            indices of the rows to select.
	 * @throws RuntimeException
	 *             (a) if the index is out of bounds (index &lt; 0 || index &ge;
	 *             getOptionCount()); (b) when there is no bound model to select
	 *             from
	 */
	void setSelection(int[] indices);

	/**
	 * Returns one of the options among which to select.
	 * 
	 * @param index
	 *            the index of the option
	 * @return An option.
	 * @throws RuntimeException
	 *             if the index is out of bounds (index &lt; 0 || index &gt;=
	 *             getOptionCount())
	 */
	Object getOption(int index);

	/**
	 * Index of the option among the selectable options.
	 * 
	 * @param option
	 *            An option.
	 * @return The index of the option or -1 if the given option is not amongst
	 *         the selectable options.
	 */
	int indexOfOption(Object option);

	/**
	 * Indicates whether the specified option is one of the options among which
	 * to select.
	 * 
	 * @param option
	 *            An option.
	 * @return true if the options is one of the selectable options, false
	 *         otherwise.
	 */
	boolean containsOption(Object option);

	/**
	 * @return The number of options among which to select.
	 */
	int getOptionCount();

}
