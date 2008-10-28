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
import org.eclipse.riena.ui.ridgets.util.IComboEntryFactory;

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
	 *            An observable list with a list of beans.
	 * @param rowBeanClass
	 *            The class of the beans in the list.
	 * @param renderingMethod
	 *            The method used to render the beans in the list of the combo
	 *            box.
	 * @param selectionObservableValue
	 *            An observable value holding the selection.
	 */
	void bindToModel(IObservableList listObservableValue, Class<? extends Object> rowBeanClass, String renderingMethod,
			IObservableValue selectionObservableValue);

	/**
	 * @param listBean
	 *            An object holding the list of beans.
	 * @param listPropertyName
	 *            The property name to access the list.
	 * @param rowBeanClass
	 *            The class of the beans in the list.
	 * @param renderingMethod
	 *            The method used to render the beans in the list of the combo
	 *            box.
	 * @param selectionBean
	 *            An object holding the selection.
	 * @param selectionPropertyName
	 *            The property name to access the selection.
	 */
	void bindToModel(Object listBean, String listPropertyName, Class<? extends Object> rowBeanClass,
			String renderingMethod, Object selectionBean, String selectionPropertyName);

	/**
	 * @param listBean
	 *            An object holding the list of beans.
	 * @param listPropertyName
	 *            The property name to access the list.
	 * @param rowBeanClass
	 *            The class of the beans in the list.
	 * @param renderingMethod
	 *            The method used to render the beans in the list of the combo
	 *            box.
	 * @param selectionBean
	 *            An object holding the selection.
	 * @param selectionPropertyName
	 *            The property name to access the selection.
	 * @param entryFactory
	 *            Factory for creating new entries for this ComboBox
	 */
	void bindToModel(Object listBean, String listPropertyName, Class<? extends Object> rowBeanClass,
			String renderingMethod, Object selectionBean, String selectionPropertyName,
			IComboEntryFactory entryFactory);

	/**
	 * Return the observable list holding the list.
	 * 
	 * @return the observable list.
	 */
	IObservableList getObservableList();

	/**
	 * Return the observable value holding the selection.
	 * 
	 * @return the observable value.
	 * 
	 * @deprecated use BeansObservables.observeValue(this,
	 *             IComboRidget.PROPERTY_SELECTION);
	 */
	IObservableValue getSelectionObservable();

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
	 * Return true if the receiver is editable. By default, a
	 * <code>IComboRidget</code> is not editable.
	 * 
	 * @return true if the receiver is editable, otherwise false
	 * @deprecated use isReadonly() instead
	 */
	boolean isEditable();

	/**
	 * Set the editability of the <code>IComboRidget</code> edit field. If the
	 * edit field is editable arbitrary text can be entered into the entry field
	 * - the text does need not to be an element of the
	 * <code>IComboRidget</code> drop down list. Entering text not contained in
	 * the <code>IComboRidget</code> drop down list will be added to the list if
	 * and only if the <code>IComboRidget</code> is mutable
	 * {@link IComboRidget#isListMutable()}.
	 * 
	 * @param editable
	 *            true if the receiver is editable, otherwise false
	 * @deprecated use setReadonly instead
	 */
	void setEditable(boolean editable);

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
