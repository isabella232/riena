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
package org.eclipse.riena.ui.ridgets.listener;

/**
 * TODO [ev] docs -- provisional -- may change
 * 
 * @since 2.0
 */
public final class ClickEvent {

	private final int columnIndex;
	private final int button;
	private final Object rowData;

	public ClickEvent(int columnIndex, int button, Object rowData) {
		this.columnIndex = columnIndex;
		this.button = button;
		this.rowData = rowData;
	}

	/**
	 * the button that was pressed or released; 1 for the first button, 2 for
	 * the second button, and 3 for the third button, etc.
	 */
	public int getButton() {
		return button;
	}

	/**
	 * Returns the zero-based index of the column which was clicked
	 */
	public int getColumnIndex() {
		return columnIndex;
	}

	public Object getRow() {
		return rowData;
	}

	@Override
	public String toString() {
		return String.format("ClickEvent [columnIndex=%d, button=%d, rowData=%s]", columnIndex, button, String //$NON-NLS-1$
				.valueOf(rowData));
	}

}
