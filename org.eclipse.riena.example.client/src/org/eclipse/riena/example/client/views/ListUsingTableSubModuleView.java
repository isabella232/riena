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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Experimental view using a single column Table instead of a List. This
 * improves look&feel by using the default background color for the list rather
 * than white. But if the list is sorted the background color is different under
 * some MS Windows versions to highlight the sorted column...
 */
public class ListUsingTableSubModuleView extends ListSubModuleView {

	public static final String ID = ListUsingTableSubModuleView.class.getName();

	@Override
	protected Group createListGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "&Persons:"); //$NON-NLS-1$
		group.setLayout(new GridLayout(2, true));

		Composite tableComposite = new Composite(group, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tableComposite);
		int style = SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.NO_BACKGROUND;
		final Table listPersons = new Table(tableComposite, style);
		listPersons.setLinesVisible(false);
		TableColumn listColumn = new TableColumn(listPersons, SWT.LEFT);
		TableColumnLayout layout = new TableColumnLayout();
		layout.setColumnData(listColumn, new ColumnWeightData(1, false));
		tableComposite.setLayout(layout);

		GridDataFactory.fillDefaults().grab(false, true).span(2, 1).applyTo(tableComposite);
		addUIControl(listPersons, "listPersons"); //$NON-NLS-1$

		Button buttonSort = UIControlsFactory.createButtonCheck(group);
		GridDataFactory.fillDefaults().grab(false, true).span(2, 1).applyTo(buttonSort);
		addUIControl(buttonSort, "buttonSort"); //$NON-NLS-1$

		Button buttonAdd = UIControlsFactory.createButton(group);
		addUIControl(buttonAdd, "buttonAdd"); //$NON-NLS-1$
		int xHint = UIControlsFactory.getWidthHint(buttonAdd);
		GridDataFactory.fillDefaults().hint(xHint, SWT.DEFAULT).applyTo(buttonAdd);

		Button buttonRemove = UIControlsFactory.createButton(group);
		addUIControl(buttonRemove, "buttonRemove"); //$NON-NLS-1$
		xHint = UIControlsFactory.getWidthHint(buttonRemove);
		GridDataFactory.fillDefaults().hint(xHint, SWT.DEFAULT).applyTo(buttonRemove);

		return group;
	}
}
