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
 * A single choice ridget that allows the selection of a single option.
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
	 * @param optionValues
	 *            An observable list with a list of options.
	 * @param selectionValue
	 *            An observable value holding the selection.
	 */
	void bindToModel(IObservableList optionValues, IObservableValue selectionValue);

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
