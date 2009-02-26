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
	 * @param listObservableValue
	 *            An observable list of objects.
	 * @param rowValueClass
	 *            The class of the values in the list.
	 * @param renderingMethod
	 *            A method of rowValueClass that returns the value to display
	 *            for each entry of the combo box combo box (null for {@code
	 *            toString()}).
	 * @param selectionObservableValue
	 *            A non-null observable value holding the selection.
	 */
	void bindToModel(IObservableList listObservableValue, Class<? extends Object> rowValueClass,
			String renderingMethod, IObservableValue selectionObservableValue);

	/**
	 * @param listPojo
	 *            An object holding a list of values (objects).
	 * @param listPropertyName
	 *            The property name to access the list.
	 * @param rowValueClass
	 *            The class of the values in the list.
	 * @param renderingMethod
	 *            A method of rowValueClass that returns the value to display
	 *            for each entry of the combo box combo box (null for {@code
	 *            toString()}).
	 * @param selectionPojo
	 *            A non-null object holding the selection.
	 * @param selectionPropertyName
	 *            The property name to access the selection (non-null).
	 */
	void bindToModel(Object listPojo, String listPropertyName, Class<? extends Object> rowValueClass,
			String renderingMethod, Object selectionPojo, String selectionPropertyName);

	/**
	 * @param listPojo
	 *            An object holding a list of values (objects).
	 * @param listPropertyName
	 *            The property name to access the list.
	 * @param rowValueClass
	 *            The class of the values in the list.
	 * @param renderingMethod
	 *            A method of rowValueClass that returns the value to display
	 *            for each entry of the combo box combo box (null for {@code
	 *            toString()}).
	 * @param selectionPojo
	 *            A non-null object holding the selection.
	 * @param selectionPropertyName
	 *            The property name to access the selection.
	 * @param entryFactory
	 *            Factory for creating new entries for this ComboBox
	 */
	void bindToModel(Object listPojo, String listPropertyName, Class<? extends Object> rowValueClass,
			String renderingMethod, Object selectionPojo, String selectionPropertyName, IComboEntryFactory entryFactory);

	/**
	 * Return the observable list holding the list.
	 * 
	 * @return the observable list.
	 */
	IObservableList getObservableList();

	/**
	 * Return the current selection. Will return null if either nothing or the
	 * "empty selection item" is selected.
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
	 */
	void setSelection(int index);

	/**
	 * Return the index of the current selection. Will return -1 if either
	 * nothing or the "empty selection item" is selected.
	 * 
	 * @return index of the current selection or -1 if none
	 */
	int getSelectionIndex();

	/**
	 * Returns the item that represents 'no selection'. When the selected item
	 * is equal to this item the adapter is marked just as if no item was
	 * selected. This entry could be represented by an empty item or something
	 * like "[Please select...]".
	 * 
	 * @return The item that represents 'no selection'.
	 */
	Object getEmptySelectionItem();

	/**
	 * Sets the item that represents 'no selection'. When the selected item is
	 * equal to this item the adapter is marked just as if no item was selected.
	 * This entry could be represented by an empty item or something like
	 * "[Please select...]".
	 * 
	 * @param emptySelectionItem
	 *            The item that represents 'no selection'.
	 */
	void setEmptySelectionItem(Object emptySelectionItem);

	/**
	 * Return true if the receivers drop down list is mutable, ie items not yet
	 * contained in the list will automatically be added if entered into the
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
	 * @return True if edited values will be added to the list of options, false
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
