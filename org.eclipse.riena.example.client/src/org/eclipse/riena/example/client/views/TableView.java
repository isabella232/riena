/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
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
import org.eclipse.riena.example.client.controllers.TableViewController;
import org.eclipse.riena.internal.example.client.utils.UIControlsFactory;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * SWT {@link ITreeRidget} sample.
 */
public class TableView extends SubModuleNodeView<TableViewController> {

	public static final String ID = TableView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(1, false));

		Group group1 = createTableGroup(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(group1);
	}

	@Override
	protected TableViewController createController(ISubModuleNode subModuleNode) {
		return new TableViewController(subModuleNode);
	}

	// helping methods
	// ////////////////

	private Group createTableGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "&Table:");
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).applyTo(group);

		Composite treeComposite = new Composite(group, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(treeComposite);

		Table table = new Table(treeComposite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		addUIControl(table, "table"); //$NON-NLS-1$

		TableColumn columnWord = new TableColumn(table, SWT.LEFT);
		TableColumn columnUppercase = new TableColumn(table, SWT.LEFT);
		TableColumn columnACount = new TableColumn(table, SWT.LEFT);

		TableColumnLayout layout = new TableColumnLayout();
		layout.setColumnData(columnWord, new ColumnWeightData(30));
		layout.setColumnData(columnUppercase, new ColumnWeightData(30));
		layout.setColumnData(columnACount, new ColumnWeightData(30));
		treeComposite.setLayout(layout);

		Composite buttonComposite = createButtonComposite(group);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(buttonComposite);

		return group;
	}

	private Composite createButtonComposite(Group group) {
		Composite buttonComposite = UIControlsFactory.createComposite(group);
		GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(buttonComposite);

		Button buttonAddSibling = UIControlsFactory.createButton(buttonComposite);
		int widthHint = UIControlsFactory.getWidthHint(buttonAddSibling);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.END, SWT.BEGINNING).hint(widthHint, SWT.DEFAULT)
				.applyTo(buttonAddSibling);
		addUIControl(buttonAddSibling, "buttonAddSibling"); //$NON-NLS-1$

		Button buttonRename = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonRename, "buttonRename"); //$NON-NLS-1$

		Button buttonDelete = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonDelete, "buttonDelete"); //$NON-NLS-1$

		return buttonComposite;
	}
}
