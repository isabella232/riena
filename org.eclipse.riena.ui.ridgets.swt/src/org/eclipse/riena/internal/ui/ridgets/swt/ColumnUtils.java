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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.Widget;

/**
 * TODO [ev] javadoc
 */
public final class ColumnUtils {

	public static final void applyColumnWidths(Table control, ColumnLayoutData[] columnWidths) {
		applyColumnWidths(control, columnWidths, control.getColumnCount());
	}

	public static final void applyColumnWidths(Tree control, ColumnLayoutData[] columnWidths) {
		applyColumnWidths(control, columnWidths, control.getColumnCount());
	}

	// helping methods
	//////////////////

	private static void applyColumnWidths(Composite control, ColumnLayoutData[] columnWidths, final int expectedCols) {
		final ColumnLayoutData[] columnData;
		if (columnWidths == null || columnWidths.length != expectedCols) {
			columnData = new ColumnLayoutData[expectedCols];
			for (int i = 0; i < expectedCols; i++) {
				columnData[i] = new ColumnWeightData(1, true);
			}
		} else {
			columnData = columnWidths;
		}
		Composite parent = control.getParent();
		if (control.getLayout() instanceof TableLayout) {
			// TableLayout: use columnData instance for each column, apply to control
			TableLayout layout = new TableLayout();
			for (int index = 0; index < expectedCols; index++) {
				layout.addColumnData(columnData[index]);
			}
			control.setLayout(layout);
			parent.layout(true, true);
		} else if ((control instanceof Tree && control.getLayout() == null && parent.getLayout() == null && parent
				.getChildren().length == 1)
				|| parent.getLayout() instanceof TreeColumnLayout) {
			// TreeColumnLayout: use columnData instance for each column, apply to parent
			TreeColumnLayout layout = new TreeColumnLayout();
			for (int index = 0; index < expectedCols; index++) {
				Widget column = getColumn(control, index);
				layout.setColumnData(column, columnData[index]);
			}
			parent.setLayout(layout);
			parent.layout();
		} else if ((control instanceof Table && control.getLayout() == null && parent.getLayout() == null && parent
				.getChildren().length == 1)
				|| parent.getLayout() instanceof TableColumnLayout) {
			// TableColumnLayout: use columnData instance for each column, apply to parent
			TableColumnLayout layout = new TableColumnLayout();
			for (int index = 0; index < expectedCols; index++) {
				Widget column = getColumn(control, index);
				layout.setColumnData(column, columnData[index]);
			}
			parent.setLayout(layout);
			parent.layout(true, true);
		} else {
			// Other: manually compute width for each columnm, apply to TableColumn
			// 1. absolute widths: apply absolute widths first
			// 2. relative widths:
			//    compute remaining width and total weight; for each column: apply
			//    the largest value of either the relative width or the minimum width
			int widthRemaining = control.getClientArea().width;
			int totalWeights = 0;
			for (int index = 0; index < expectedCols; index++) {
				ColumnLayoutData data = columnData[index];
				if (data instanceof ColumnPixelData) {
					ColumnPixelData pixelData = (ColumnPixelData) data;
					int width = pixelData.width;
					if (pixelData.addTrim) {
						width = width + getColumnTrim();
					}
					configureColumn(control, index, width, data.resizable);
					widthRemaining = widthRemaining - width;
				} else if (data instanceof ColumnWeightData) {
					totalWeights = totalWeights + ((ColumnWeightData) data).weight;
				}
			}
			int slice = totalWeights > 0 ? Math.max(0, widthRemaining / totalWeights) : 0;
			for (int index = 0; index < expectedCols; index++) {
				if (columnData[index] instanceof ColumnWeightData) {
					ColumnWeightData data = (ColumnWeightData) columnData[index];
					int width = Math.max(data.minimumWidth, data.weight * slice);
					configureColumn(control, index, width, data.resizable);
				}
			}
		}
	}

	private static void configureColumn(Control control, int index, int width, boolean resizable) {
		Widget column = getColumn(control, index);
		if (column instanceof TreeColumn) {
			((TreeColumn) column).setWidth(width);
			((TreeColumn) column).setResizable(resizable);
		} else if (column instanceof TableColumn) {
			((TableColumn) column).setWidth(width);
			((TableColumn) column).setResizable(resizable);
		}
	}

	private static Widget getColumn(Control control, int index) {
		if (control instanceof Table) {
			return ((Table) control).getColumn(index);
		}
		if (control instanceof Tree) {
			return ((Tree) control).getColumn(index);
		}
		throw new IllegalArgumentException("unsupported type: " + control); //$NON-NLS-1$
	}

	private static int getColumnTrim() {
		int result = 3;
		if (Util.isWindows()) {
			result = 4;
		} else if (Util.isMac()) {
			result = 24;
		}
		return result;
	}

	private ColumnUtils() {
		// utility class
	}
}
