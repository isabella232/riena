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

/**
 * A ridget with two areas. The master area shows a table of objects, from which
 * one can be selected. The details are allows the user to edit some details of
 * the currently selected object/row.
 * <p>
 * This ridget is an {@link IComplexRidget} an {@link ITableRidget} to show the
 * available objects and several {@link IActionRidget}s to add, delete, update
 * the row elements.
 * <p>
 * The UI of the details area is created by implementing an
 * MasterDetailComposite. The binding between UI and ridgets is done by
 * implementing an {@link IMasterDetailsDelegate} and introducing it to this
 * ridget via {@link #setDelegate(IMasterDetailsDelegate)}.
 * 
 * @author Erich Achilles
 */
public interface IMasterDetailsRidget extends IRidget, IComplexRidget {

	/**
	 * Provide this ridget with an {@link IMasterDetailsDelegate} instance,
	 * which will manage the content of details area.
	 * 
	 * @param delegate
	 *            an {@link IMasterDetailsDelegate}; never null
	 */
	void setDelegate(IMasterDetailsDelegate delegate);

	/**
	 * Binds the table to the model data.
	 * 
	 * @param rowBeansObservable
	 *            An observable list with a list of beans.
	 * @param rowBeanClass
	 *            The class of the beans in the list.
	 * @param columnPropertyNames
	 *            The property names of the properties of the beans to be
	 *            displayed in the columns. A non-null String array.
	 * @param columnHeaders
	 *            The titles of the columns to be displayed in the header. May
	 *            be null if no headers should be shown for this table.
	 *            Individual array entries may be null, which will show an empty
	 *            title in the header of that column.
	 * @throws RuntimeException
	 *             when columnHeaders is non-null and the the number of
	 *             columnHeaders does not match the number of
	 *             columnPropertyNames
	 */
	void bindToModel(IObservableList rowBeansObservable, Class<? extends Object> rowBeanClass,
			String[] columnPropertyNames, String[] columnHeaders);

	/**
	 * @param rowBeansBean
	 *            A bean that has a property with a list of beans.
	 * @param rowBeansPropertyName
	 *            The name of the property with the list on beans.
	 * @param rowBeanClass
	 *            The class of the beans in the list.
	 * @param columnPropertyNames
	 *            The property names of the properties of the beans to be
	 *            displayed in the columns.
	 * @param headerNames
	 *            The titles of the columns to be displayed in the header. May
	 *            be null if no headers should be shown for this table.
	 *            Individual array entries may be null, which will show an empty
	 *            title in the header of that column.
	 */
	void bindToModel(Object rowBeansBean, String rowBeansPropertyName, Class<? extends Object> rowBeanClass,
			String[] columnPropertyNames, String[] headerNames);

	/**
	 * Should return true if one of the details of this Master Detail construct
	 * has changed
	 * 
	 * @return true if some detail changed
	 */
	boolean isDetailsChanged();

	/**
	 * Clears the detail ridgets, that is, empty textfields, uncheck checkboxes
	 * and so on.
	 */
	void clearDetails();

	/**
	 * Creates a workingCopy. The workingCopy object represents the model behind
	 * the detail fields. It is always an instance of the bean class of the
	 * master model.
	 * 
	 * @return an Object instance; never null.
	 */
	Object createWorkingCopy();

	/**
	 * States whether the input is valid. For example checks if a textfield
	 * should contain digits or not.
	 * 
	 * @return true if input is valid
	 */
	boolean isInputValid();

	/**
	 * Copies the content of one given bean instance into another given bean
	 * instance.
	 * 
	 * 
	 * @param source
	 *            The source bean. If null, a fresh instance obtained from
	 *            {@link #createWorkingCopyObject()} will be used as the source.
	 * @param target
	 *            The target bean. If null, a fresh instance obtained from
	 *            {@link #createWorkingCopyObject()} will be used as the target
	 * @return returns the target bean; never null.
	 */
	Object copyBean(Object source, Object target);

	/**
	 * Updates the data shown in the 'details' area, based on the current
	 * selected object in the 'master' area.
	 */
	void updateDetails();

	/**
	 * Adds a new bean instance which represents the actual content of the
	 * details to the master model.
	 */
	void addToMaster();

	/**
	 * Returns the currently object corresponding to the currently selected row
	 * in the ridget.
	 * 
	 * @return the actual selection in the ridget or null (if nothing is
	 *         selected)
	 */
	Object getSelection();

	/**
	 * Returns the workingCopy object. The workingCopyObject represents the
	 * model behind the detail fields.Its always an instance of the bean class
	 * of the master model.
	 * 
	 * @return an Object; never null.
	 */
	Object getWorkingCopy();

	/**
	 * Set the new selection. This behaves exactly like an interactive selection
	 * change (i.e. the user selecting another bean).
	 * 
	 * @param newSelection
	 *            the newly selected bean of the master, or null to clear the
	 *            selection
	 */
	void setSelection(final Object newSelection);

	/**
	 * Copies the content of the actually selected master bean to the detail
	 * fields.
	 */
	void copyFromMasterToDetails();

	/**
	 * Copies the content of the details into the bean behind the actual
	 * selected row of the master. If nothing is selected, a new bean is added
	 * to the master model.
	 */
	boolean copyFromDetailsToMaster();

}
