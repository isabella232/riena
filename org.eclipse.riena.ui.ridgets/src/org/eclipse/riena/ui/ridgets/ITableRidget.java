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

import java.util.Comparator;

import org.eclipse.core.databinding.observable.list.IObservableList;

/**
 * Ridget for a table.
 */
public interface ITableRidget extends ISelectableIndexedRidget, ISortableByColumn {

	/**
	 * Return an observable list of objects which can be selected through this
	 * ridget.
	 * 
	 * @return an IObservableList instance or null, if the ridget has not been
	 *         bound to a model
	 */
	IObservableList getObservableList();

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
	 *            The titles of the columns to be displayed in the table header.
	 *            May be null if no headers should be shown for this table.
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
	 * Binds the table to the model data.
	 * 
	 * @param rowBeansBean
	 *            A bean that has a property with a list of beans.
	 * @param rowBeansPropertyName
	 *            The name of the property with the list on beans.
	 * @param rowBeanClass
	 *            The class of the beans in the list.
	 * @param columnPropertyNames
	 *            The property names of the properties of the beans to be
	 *            displayed in the columns.
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
	void bindToModel(Object rowBeansBean, String rowBeansPropertyName, Class<? extends Object> rowBeanClass,
			String[] columnPropertyNames, String[] columnHeaders);

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the bound control is double-clicked.
	 * <p>
	 * The same listener may be added more than once, and will be called as many
	 * times as it is added.
	 * 
	 * @param listener
	 *            a non-null {@link IActionListener} instance
	 * @throws RuntimeException
	 *             if listener is null
	 */
	void addDoubleClickListener(IActionListener listener);

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the bound control is double-clicked.
	 * 
	 * @param listener
	 *            an {@link IActionListener} instance
	 */
	void removeDoubleClickListener(IActionListener listener);

	/**
	 * Set the {@link Comparator} to be used when sorting column at columnIndex.
	 * 
	 * @param columnIndex
	 *            a columnIndex in the allowed range: ( 0 &lt;= columnIndex &lt;
	 *            numColumns )
	 * @param comparator
	 *            a Comparator instance; may be null
	 * @throws RuntimeException
	 *             if columnIndex is out of range
	 */
	void setComparator(int columnIndex, Comparator<Object> comparator);

	/**
	 * Return true, if this table allows columns to be re-arranged by the user.
	 * The default value is false.
	 */
	boolean hasMoveableColumns();

	/**
	 * Set to true, if this table should allow columns to be re-arranged by the
	 * user.
	 * 
	 * @param moveableColumns
	 *            true, if column should be re-arrangable by the user; false
	 *            otherwise.
	 */
	void setMoveableColumns(boolean moveableColumns);
}
