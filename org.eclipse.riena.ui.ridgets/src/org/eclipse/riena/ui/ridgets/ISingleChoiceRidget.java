/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets;

import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;

/**
 * A single choice ridget that allows the selection of a single option.
 * 
 * @author Frank Schepp
 */
public interface ISingleChoiceRidget extends IChoiceRidget {
	/**
	 * Return the observable value holding the selection.
	 * 
	 * @return the observable value.
	 * 
	 * @deprecated use BeansObservables.observeValue(this,
	 *             IChoiceRidget.PROPERTY_SELECTION);
	 */
	IObservableValue getSelectionObservable();

	/**
	 * @param listObservableValue
	 *            An observable list with a list of options.
	 * @param selectionObservableValue
	 *            An observable value holding the selection.
	 */
	void bindToModel(IObservableList listObservableValue, IObservableValue selectionObservableValue);

	/**
	 * @param listBean
	 *            An object holding the list of options.
	 * @param listPropertyName
	 *            The property name to access the list.
	 * @param selectionBean
	 *            An object holding the selection.
	 * @param selectionPropertyName
	 *            The property name to access the selection.
	 */
	void bindToModel(Object listBean, String listPropertyName, Object selectionBean, String selectionPropertyName);

	/**
	 * @param optionValues
	 *            The list of values to provide as choice list.
	 * @param optionLabels
	 *            The list of labels to present the choice list.
	 * @param selectionBean
	 *            An object holding the selection.
	 * @param selectionPropertyName
	 *            The property name to access the selection.
	 */
	void bindToModel(List<? extends Object> optionValues, List<String> optionLabels, Object selectionBean,
			String selectionPropertyName);

	/**
	 * Return the selection.
	 * 
	 * @return the selection.
	 */
	Object getSelection();

	/**
	 * Set the selection.
	 * 
	 * @param selection
	 *            the selection.
	 */
	void setSelection(Object selection);
}
