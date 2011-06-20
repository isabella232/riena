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

/**
 *
 */
public class TableRidgetTableViewer extends TableViewer {

	private final TableRidget tableRidget;

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

}
