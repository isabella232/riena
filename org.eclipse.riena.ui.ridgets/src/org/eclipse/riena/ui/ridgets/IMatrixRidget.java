/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2008 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/

package org.eclipse.riena.ui.ridgets;

import org.eclipse.core.databinding.observable.list.IObservableList;

/**
 * Interface for a MatrixRidget, which displays some data in a matrix-style. The
 * columns of the matrix may have a header text.
 * 
 * @author Erich Achilles
 */

public interface IMatrixRidget extends IRidget {

	/**
	 * Binds the matrix to the model data.
	 * 
	 * @param rowBeansObservable
	 *            An observable list with a list of beans.
	 * @param rowBeanClass
	 *            The class of the beans in the list.
	 * @param columnPropertyNames
	 *            The property names of the properties of the beans to be
	 *            displayed in the columns.
	 * @param columnHeaders
	 *            The titles of the columns to be displayed in the header.
	 */
	void bindToModel(IObservableList rowBeansObservable, Class<? extends Object> rowBeanClass,
			String[] columnPropertyNames, String[] columnHeaders);

	/**
	 * Binds the matrix to the model data.
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
	 *            The titles of the columns to be displayed in the header.
	 */
	void bindToModel(Object rowBeansBean, String rowBeansPropertyName, Class<? extends Object> rowBeanClass,
			String[] columnPropertyNames, String[] columnHeaders);

	/**
	 * Specifies whether header should be visible or not.
	 * 
	 * @param showHeader
	 *            if true, header of matrix is shown
	 */
	public void setShowHeader(boolean showHeader);

}
