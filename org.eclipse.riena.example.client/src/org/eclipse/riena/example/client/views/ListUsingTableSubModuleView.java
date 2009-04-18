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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Experimental view using a single column Table instead of a List. This
 * improves look&feel by using the default background color for the list rather
 * than white. But if the list is sorted the background color is different under
 * some MS Windows versions to highlight the sorted column...
 */
public class ListUsingTableSubModuleView extends ListSubModuleView {

	public static final String ID = ListUsingTableSubModuleView.class.getName();

	@Override
	protected Control createListControl(Composite parent) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(false, true).span(2, 1).hint(200, SWT.DEFAULT).applyTo(tableComposite);

		int style = SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.NO_BACKGROUND;
		Table listPersons = new Table(tableComposite, style);
		listPersons.setLinesVisible(false);
		TableColumn listColumn = new TableColumn(listPersons, SWT.LEFT);

		TableColumnLayout layout = new TableColumnLayout();
		layout.setColumnData(listColumn, new ColumnWeightData(1, false));
		tableComposite.setLayout(layout);

		return listPersons;
	}
}
