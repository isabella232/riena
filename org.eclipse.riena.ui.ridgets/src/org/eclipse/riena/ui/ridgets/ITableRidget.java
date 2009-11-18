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

import java.util.Comparator;

import org.eclipse.core.databinding.observable.list.IObservableList;

import org.eclipse.riena.ui.common.ISortableByColumn;

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
	 *            The titles of the columns to be displayed in the table header.
	 *            May be null if no headers should be shown for this table.
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
	 * Binds the table to the model data.
	 * 
	 * @param listHolder
	 *            An object that has a property with a list of objects.
	 * @param listPropertyName
	 *            Property for accessing the list of objects.
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
	void bindToModel(Object listHolder, String listPropertyName, Class<? extends Object> rowClass,
			String[] columnPropertyNames, String[] columnHeaders);

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the bound control is double-clicked.
	 * <p>
	 * Adding the same listener several times has no effect.
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
	 *            a non-null {@link IActionListener} instance
	 * @throws RuntimeException
	 *             if listener is null
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
	 * Set the {@link ColumnFormatter} to be used for the column at columnIndex.
	 * <p>
	 * Note: changing column formatters on a table ridget that is already bound
	 * to a model, requires calling {@link #updateFromModel()} to apply the new
	 * format.
	 * 
	 * @param columnIndex
	 *            a columnIndex in the allowed range ( 0 &lt;= columnIndex &lt;
	 *            numColumns )
	 * @param formatter
	 *            an IColumnFormatter instance; null removes the previously used
	 *            formatter from the selected column
	 */
	void setColumnFormatter(int columnIndex, IColumnFormatter formatter);

	/**
	 * TODO [ev] javadoc -- experimental, may be removed without notice
	 * 
	 * @since 1.2
	 */
	void setColumnWidths(Object[] widths);

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
