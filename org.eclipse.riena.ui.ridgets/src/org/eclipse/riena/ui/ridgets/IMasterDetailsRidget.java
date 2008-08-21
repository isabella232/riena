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

/**
 * An implementation of this interface offers the user a ridget which allows to
 * edit a couple of details from beans which represent the bean entries of a
 * table. It is a complex ridget which is composed of IActionRidgets to add and
 * delete table entries and to take over changes on existing table entries.
 * Further on we have a table (the master) an a couple of ridgets which
 * represent the details of the master
 * 
 * @author Erich Achilles
 */
/**
 * 
 */
public interface IMasterDetailsRidget<E> extends IRidget, IComplexRidget {

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
	 * Creates a workingCopyObject. The workingCopyObject represents the model
	 * behind the detail fields.Its always an instance of the bean class of the
	 * master model.
	 * 
	 * @return
	 */
	E createWorkingCopyObject();

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
	 * @param from
	 *            the source bean
	 * @param to
	 *            the target bean
	 * @return returns the target bean
	 */
	E copyBean(E from, E to);

	/**
	 * Updates all details from the model. Typically an implementation calls
	 * updateFromModel for every given detail ridget.
	 */
	void updateDetails();

	/**
	 * Adds a new bean instance which represents the actual content of the
	 * details to the master model.
	 */
	void addToMaster();

	/**
	 * Returns a bean which represents the content of the bean behind the
	 * selected row of the master.
	 * 
	 * @return the actual selecetion of the master
	 */
	E getMasterSelection();

	/**
	 * Returns the workingCopy object. The workingCopyObject represents the
	 * model behind the detail fields.Its always an instance of the bean class
	 * of the master model.
	 * 
	 * @return
	 */
	E getWorkingCopy();

	/**
	 * Set the new selection exactly like interactive selection change.
	 * 
	 * @param newSelection
	 *            the newly selected bean of the master
	 * @param triggerChanged
	 *            if true, a selection changes should be triggered
	 */
	void setSelection(final E newSelection, boolean triggerChanged);

	/**
	 * This method is called by the Master Detail ridget when the selection is
	 * changed. Overload this method to react on selection changed.
	 * 
	 * @param newSelection
	 */
	void selectionChanged(E newSelection);

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
