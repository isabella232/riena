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
package org.eclipse.riena.sample.snippets;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import org.eclipse.riena.beans.common.DayPojo;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates automatic table column creation and setting the column widths.
 */
public class SnippetTableRidget004 {

	public SnippetTableRidget004(Shell shell) {
		Table table = new Table(shell, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		// TableColumns will be created as needed when 'bind' is invoked.
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		ITableRidget tableRidget = (ITableRidget) SwtRidgetFactory.createRidget(table);

		// set the widths
		ColumnLayoutData[] widths = { new ColumnPixelData(100, false), new ColumnWeightData(2, false),
				new ColumnWeightData(2, false), new ColumnWeightData(1, false), new ColumnWeightData(1, false), };
		tableRidget.setColumnWidths(widths);

		String[] columnPropertyNames = { "english", "german", "french", "spanish", "italian" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		String[] columnHeaders = { "English", "German", "French", "Spain", "Italian" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		IObservableList input = new WritableList(DayPojo.createWeek(), DayPojo.class);
		tableRidget.bindToModel(input, DayPojo.class, columnPropertyNames, columnHeaders);
		tableRidget.updateFromModel();
	}

	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetTableRidget004.class.getSimpleName());
			new SnippetTableRidget004(shell);
			shell.setSize(500, 300);
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} finally {
			display.dispose();
		}
	}
}
