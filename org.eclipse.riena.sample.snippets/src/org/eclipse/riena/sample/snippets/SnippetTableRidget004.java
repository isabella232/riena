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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import org.eclipse.riena.beans.common.ListBean;
import org.eclipse.riena.beans.common.StringBean;
import org.eclipse.riena.internal.ui.ridgets.swt.TableRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;

/**
 * Demonstrates a table ridget with multi-line wrapping.
 * 
 * TODO [ev] highly experimental -- might be removed soon
 */
public class SnippetTableRidget004 {

	private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum lectus augue, pulvinar quis cursus nec, imperdiet nec ante. Cras sit amet arcu et enim adipiscing pellentesque. Suspendisse mi felis, dictum a lobortis nec, placerat in diam. Proin lobortis tortor at nunc facilisis aliquet. Praesent eget dignissim orci. Ut iaculis bibendum.";

	public SnippetTableRidget004(Shell shell) {
		Table table = new Table(shell, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		int numColumns = 3;
		TableColumn[] columns = new TableColumn[numColumns];
		for (int i = 0; i < numColumns; i++) {
			columns[i] = new TableColumn(table, SWT.LEFT);
		}
		TableColumnLayout layout = new TableColumnLayout();
		for (int i = 0; i < numColumns; i++) {
			layout.setColumnData(columns[i], new ColumnWeightData(120, 120));
		}
		shell.setLayout(layout);

		ITableRidget tableRidget = (ITableRidget) SwtRidgetFactory.createRidget(table);
		String[] columnPropertyNames = { StringBean.PROP_VALUE, StringBean.PROP_VALUE, StringBean.PROP_VALUE };
		String[] columnHeaders = { "Column 1", "Column 2", "Column 3" };
		ListBean values = createInput();
		tableRidget.bindToModel(values, ListBean.PROPERTY_VALUES, StringBean.class, columnPropertyNames, columnHeaders);
		tableRidget.updateFromModel();
		((TableRidget) tableRidget).setWrapping(true); // TODO [ev] move to interface
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			new SnippetTableRidget004(shell);
			// shell.pack();
			shell.setSize(500, 400);
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

	private ListBean createInput() {
		List<StringBean> values = new ArrayList<StringBean>();
		for (int i = 0; i < 10; i++) {
			values.add(new StringBean(LOREM_IPSUM));
		}
		return new ListBean(values);
	}

}
