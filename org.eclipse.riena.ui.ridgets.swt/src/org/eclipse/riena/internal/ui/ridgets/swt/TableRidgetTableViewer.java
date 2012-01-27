/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Table;

/**
 * A viewer of a SWT {@link Table} control that also know the corresponding
 * Ridget.
 */
public class TableRidgetTableViewer extends TableViewer {

	private final TableRidget tableRidget;

	private boolean allowRefresh = true;

	/**
	 * Creates a table viewer on the SWT table that's binded with the given
	 * Ridget.
	 * 
	 * @param tableRidget
	 *            Ridget of the {@link Table}
	 */
	public TableRidgetTableViewer(final TableRidget tableRidget) {
		super(tableRidget.getUIControl());
		this.tableRidget = tableRidget;
	}

	@Override
	public ViewerRow getViewerRow(final Point point) {
		return super.getViewerRow(point);
	}

	public TableRidget getTableRidget() {
		return tableRidget;
	}

	public void setAllowRefresh(final boolean allowRefresh) {
		this.allowRefresh = allowRefresh;
	}

	@Override
	public void refresh() {
		if (!allowRefresh()) {
			return;
		}
		super.refresh();
	}

	private boolean allowRefresh() {
		return allowRefresh;
	}

}
