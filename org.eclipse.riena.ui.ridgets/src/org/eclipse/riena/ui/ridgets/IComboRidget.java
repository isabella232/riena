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
package org.eclipse.riena.ui.ridgets;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;

import org.eclipse.riena.ui.common.IComboEntryFactory;

/**
 * @author Frank Schepp
 */
public interface IComboRidget extends IMarkableRidget {

	/**
	 * The name of the bound read-write <em>selection</em> property.
	 */
	String PROPERTY_SELECTION = "selection"; //$NON-NLS-1$

	/**
	 * @param optionValues
	 *            An observable list with a list of options.
	 * @param rowClass
	 *            The class of the objects in the list.
	 * @param renderingMethod
	 *            A property of rowClass that returns the value to display for
	 *            each option of the combo box combo box (examples: 'name' ->
	 *            getName(); null for {@code toString()}).
	 * @param selectionValue
	 *            A non-null observable value holding the selection.
	 */
	void bindToModel(IObservableList optionValues, Class<? extends Object> rowClass, String renderingMethod,
			IObservableValue selectionValue);

	/**
	 * @param listHolder
	 *            An object holding a list of values (objects).
	 * @param listPropertyName
	 *            The property name to access the list.
	 * @param rowClass
	 *            The class of the objects in the list.
	 * @param renderingMethod
	 *            A property of rowClass that returns the value to display for
	 *            each option of the combo box combo box (examples: 'name' ->
	 *            getName(); null for {@code toString()}).
	 * @param selectionHolder
	 *            A non-null object holding the selection.
	 * @param selectionPropertyName
	 *            The property name to access the selection (non-null).
	 */
	void bindToModel(Object listHolder, String listPropertyName, Class<? extends Object> rowClass,
			String renderingMethod, Object selectionHolder, String selectionPropertyName);

	/**
	 * @param listHolder
	 *            An object holding a list of values (objects).
	 * @param listPropertyName
	 *            The property name to access the list.
	 * @param rowClass
	 *            The class of the objects in the list.
	 * @param renderingMethod
	 *            A property of rowClass that returns the value to display for
	 *            each option of the combo box combo box (examples: 'name' ->
	 *            getName(); null for {@code toString()}).
	 * @param selectionHolder
	 *            A non-null object holding the selection.
	 * @param selectionPropertyName
	 *            The property name to access the selection.
	 * @param entryFactory
	 *            Factory for creating new options for this ComboBox
	 * @deprecated
	 */
	void bindToModel(Object listHolder, String listPropertyName, Class<? extends Object> rowClass,
			String renderingMethod, Object selectionHolder, String selectionPropertyName,
			IComboEntryFactory entryFactory);

	/**
	 * Return the observable list holding the options.
	 * 
	 * @return the observable list of options.
	 */
	IObservableList getObservableList();

	/**
	 * Return the current selection. Will return null if either nothing or the
	 * "empty selection option" is selected.
	 * 
	 * @return the current selection or null if none.
	 * @see #setEmptySelectionItem(Object)
	 */
	Object getSelection();

	/**
	 * Set the current selection to newSelection.
	 * 
	 * @param newSelection
	 */
	void setSelection(Object newSelection);

	/**
	 * Set the current selection to index.
	 * 
	 * @param index
	 *            the 0-based index; -1 to unselect
	 */
	void setSelection(int index);

	/**
	 * Return the index of the current selection. Will return -1 if either
	 * nothing or the "empty selection option" is selected.
	 * 
	 * @return index of the current selection or -1 if none
	 */
	int getSelectionIndex();

	/**
	 * Returns the option that represents 'no selection'.
	 * <p>
	 * When this option is selected the ridget behaves just as if nothing was
	 * selected. This option could be represented by an empty value or something
	 * like "[Please select...]".
	 * 
	 * @return The option that represents 'no selection'.
	 */
	Object getEmptySelectionItem();

	/**
	 * Sets the option that represents 'no selection'.
	 * <p>
	 * When this option is selected the ridget behaves just as if nothing was
	 * selected. This option could be represented by an value or something like
	 * "[Please select...]".
	 * 
	 * @param emptySelection
	 *            The option that represents 'no selection'.
	 */
	void setEmptySelectionItem(Object emptySelection);

	/**
	 * Return true if the receiver's drop down list is mutable, ie. options snot
	 * yet contained in the list will automatically be added if entered into the
	 * text field.
	 * 
	 * @return true if the receiver is mutable, otherwise false
	 */
	boolean isListMutable();

	/**
	 * Set the mutability of the <code>IComboRidget</code> drop down list. If
	 * the list is mutable text entered into the entry field (see
	 * {@link IComboRidget#isEditable()}) that is not yet contained in the
	 * receivers drop down list will automatically be added to the list. Note
	 * that setting this value to <code>true</code> only makes sense if the list
	 * model this ridget is bound to (see
	 * {@link IComboRidget#bindToModel(IObservableList, Class, String, IObservableValue)}
	 * ) is mutable, too.
	 * 
	 * @param mutable
	 *            true if the receiver is mutable, otherwise false
	 */
	void setListMutable(boolean mutable);

	/**
	 * Indicates whether the text in the textfield can be edited by the user.
	 * 
	 * @return true if the combobox is readonly, false if it can be edited.
	 */
	boolean isReadonly();

	/**
	 * Sets the readonly state of the combobox. If it is readonly, the text in
	 * the textfield can not be edited.
	 * 
	 * @param readonly
	 *            The new readonly state.
	 */
	void setReadonly(boolean readonly);

	/**
	 * Indicates whether an edited text will be added to the list of options.
	 * The list of options is the value of the combobox provided by the value
	 * provider. The addable state will not be significant if the adapter is
	 * readonly.
	 * 
	 * @return true if edited values will be added to the list of options, false
	 *         otherwise.
	 */
	boolean isAddable();

	/**
	 * Sets the addable state of the combobox. If addable, edited values will be
	 * added to the list of options. The addable state will not be significant
	 * if the adapter is readonly.
	 * 
	 * @param addable
	 *            The new addable state.
	 */
	void setAddable(boolean addable);

}
