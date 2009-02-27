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

/**
 * Provides formatting options for a single column. Can be used with
 * column-based ridgets (ITableRidget, ITreeTableRidget).
 * <p>
 * The class follows the adapter pattern and provides a default implementation
 * for all methods. Implementors should only override the formatting methods
 * that apply to their use case.
 * <p>
 * Toolkit's may provide default implementation of this interface. See
 * ColumnFormatter for an SWT-specific implementation.
 * 
 * @see ITableRidget#setColumnFormatter(int, ColumnFormatter)
 * @see ITreeTableRidget#setColumnFormatter(int, ColumnFormatter)
 */
public interface IColumnFormatter {

	/**
	 * Returns the text for a column's row.
	 * 
	 * @param element
	 *            the row element
	 * @return a text (String) for this element, or null, if no text should be
	 *         shown
	 */
	String getText(Object element);

	/**
	 * Returns the image for a column's row.
	 * 
	 * @param element
	 *            the row element
	 * @return an image for this element, or null, if no image should be shown.
	 *         Implementors may return a type specific to their ui-toolkit.
	 */
	Object getImage(Object element);

	/**
	 * Returns the foreground color for a column's row.
	 * 
	 * @param element
	 *            the row element
	 * @return the foreground color for this element or null to use the default
	 *         foreground color. Implementors may return a type specific to
	 *         their ui-toolkit.
	 */
	public Object getForeground(Object element);

	/**
	 * Returns the background color for a column's row.
	 * 
	 * @param element
	 *            the row element
	 * @return the background color for this element or null to use the default
	 *         background color. Implementors may return a type specific to
	 *         their ui-toolkit.
	 */
	public Object getBackground(Object element);

	/**
	 * Returns the font for a column's row.
	 * 
	 * @param element
	 *            the row element
	 * @return the font color for this element or null to use the default font.
	 *         Implementors may return a type specific to their ui-toolkit.
	 */
	public Object getFont(Object element);

}
