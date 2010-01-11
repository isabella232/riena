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
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import org.eclipse.riena.example.client.controllers.TableSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT {@link ITableRidget} sample.
 */
public class TableSubModuleView extends SubModuleView<TableSubModuleController> {

	public static final String ID = TableSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		Group group1 = createTableGroup(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(group1);
	}

	// helping methods
	// ////////////////

	private Group createTableGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "&Table:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).applyTo(group);

		Composite tableComposite = new Composite(group, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tableComposite);

		Table table = UIControlsFactory.createTable(tableComposite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION,
				"table"); //$NON-NLS-1$
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn columnWord = new TableColumn(table, SWT.LEFT);
		TableColumn columnUppercase = new TableColumn(table, SWT.LEFT);
		TableColumn columnACount = new TableColumn(table, SWT.LEFT);

		TableColumnLayout layout = new TableColumnLayout();
		layout.setColumnData(columnWord, new ColumnWeightData(30));
		layout.setColumnData(columnUppercase, new ColumnWeightData(30));
		layout.setColumnData(columnACount, new ColumnWeightData(30));
		tableComposite.setLayout(layout);

		Composite buttonComposite = createButtonComposite(group);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(buttonComposite);

		return group;
	}

	private Composite createButtonComposite(Group group) {
		Composite buttonComposite = UIControlsFactory.createComposite(group);
		GridLayoutFactory.fillDefaults().numColumns(4).equalWidth(false).applyTo(buttonComposite);

		Button buttonPrintSelection = UIControlsFactory.createButtonCheck(buttonComposite, "buttonPrintSelection"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.END, SWT.CENTER).hint(120, SWT.DEFAULT).applyTo(
				buttonPrintSelection);

		UIControlsFactory.createButton(buttonComposite, "", "buttonAddSibling"); //$NON-NLS-1$ //$NON-NLS-2$

		UIControlsFactory.createButton(buttonComposite, "", "buttonRename"); //$NON-NLS-1$ //$NON-NLS-2$

		UIControlsFactory.createButton(buttonComposite, "", "buttonDelete"); //$NON-NLS-1$ //$NON-NLS-2$

		return buttonComposite;
	}
}
