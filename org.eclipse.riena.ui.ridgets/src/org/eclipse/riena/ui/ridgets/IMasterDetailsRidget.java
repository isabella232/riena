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
	 * Binds the table to the model data.
	 * 
	 * @param rowObservables
	 *            An observable list of objects (non-null).
	 * @param rowClass
	 *            The class of the objects in the list.
	 * @param columnPropertyNames
	 *            The list of property names that are to be displayed in the
	 *            columns. One property per column. Each object in
	 *            rowObservables must have a corresponding getter. This
	 *            parameter must be a non-null String array.
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
	void bindToModel(IObservableList rowObservables, Class<? extends Object> rowClass, String[] columnPropertyNames,
			String[] columnHeaders);

	/**
	 * @param listHolder
	 *            An object that has a property with a list of objects.
	 * @param listPropertyName
	 *            Property for accessing the list of objects.
	 * @param rowClass
	 *            Property for accessing the list of objects.
	 * @param columnPropertyNames
	 *            The list of property names that are to be displayed in the
	 *            columns. One property per column. Each object in
	 *            rowObservables must have a corresponding getter. This
	 *            parameter must be a non-null String array.
	 * @param headerNames
	 *            The titles of the columns to be displayed in the header. May
	 *            be null if no headers should be shown for this table.
	 *            Individual array entries may be null, which will show an empty
	 *            title in the header of that column.
	 */
	void bindToModel(Object listHolder, String listPropertyName, Class<? extends Object> rowClass,
			String[] columnPropertyNames, String[] headerNames);

	/**
	 * Returns the currently object corresponding to the currently selected row
	 * in the ridget.
	 * 
	 * @return the actual selection in the ridget or null (if nothing is
	 *         selected)
	 */
	Object getSelection();

	/**
	 * Returns the directWriting setting. The default is false.
	 * <p>
	 * See {@link #setDirectWriting(boolean)} for details.
	 * 
	 * @since 1.2
	 */
	boolean isDirectWriting();

	/**
	 * Provide this ridget with an {@link IMasterDetailsDelegate} instance,
	 * which will manage the content of details area.
	 * 
	 * @param delegate
	 *            an {@link IMasterDetailsDelegate}; never null
	 */
	void setDelegate(IMasterDetailsDelegate delegate);

	/**
	 * When direct writing is enabled, changes in the details area will be
	 * immediatelly and automatically applied back to the model. When adding new
	 * rows, these will be immediatelly added to the table. Since there is no
	 * need to apply, the 'Apply' button will not be shown.
	 * <p>
	 * The default setting for direct writing is false.
	 * 
	 * @param directWriting
	 *            The new direct writing setting.
	 * 
	 * @since 1.2
	 */
	void setDirectWriting(boolean directWriting);

	/**
	 * Set the new selection. This behaves exactly like an interactive selection
	 * change (i.e. the user selecting another value).
	 * 
	 * @param newSelection
	 *            the newly selected value of this ridget, or null to clear the
	 *            selection
	 */
	void setSelection(final Object newSelection);
}
