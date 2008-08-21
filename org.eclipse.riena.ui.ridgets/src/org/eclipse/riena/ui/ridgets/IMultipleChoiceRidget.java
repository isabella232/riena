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

/**
 * A multiple choice ridget that allows the selection of multiple options.
 * 
 * @author Frank Schepp
 */
public interface IMultipleChoiceRidget extends IChoiceRidget {
	/**
	 * Return the observable list holding the selected options.
	 * 
	 * @return the observable list of selected options.
	 */
	IObservableList getObservableSelectionList();

	/**
	 * @param optionValues
	 *            An observable list with a list of options.
	 * @param selectionValues
	 *            An observable value holding the list of selected options.
	 */
	void bindToModel(IObservableList optionValues, IObservableList selectionValues);

	/**
	 * @param listBean
	 *            An object holding the list of options.
	 * @param listPropertyName
	 *            The property name to access the list.
	 * @param selectionBean
	 *            An object holding the list of selected options.
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
	 *            An object holding the list of selected options.
	 * @param selectionPropertyName
	 *            The property name to access the selection.
	 */
	void bindToModel(List<? extends Object> optionValues, List<String> optionLabels, Object selectionBean,
			String selectionPropertyName);

	/**
	 * Return the selection.
	 * 
	 * @return the selection list.
	 */
	List getSelection();

	/**
	 * Set the selection.
	 * 
	 * @param selection
	 *            the selection list.
	 */
	void setSelection(List selection);
}
