/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IMenuItemRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT {@link IMenuItemRidget} (context menu) sample.
 */
public class ContextMenuSubModuleView extends SubModuleView {
	public ContextMenuSubModuleView() {
	}

	public static final String ID = ContextMenuSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		GridLayoutFactory groupGLF = GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2);
		GridDataFactory groupGDF = GridDataFactory.fillDefaults().grab(true, false);
		GridDataFactory labelGDF = GridDataFactory.swtDefaults().hint(100, SWT.DEFAULT);

		Group groupSystem = UIControlsFactory.createGroup(parent, "Text with System-Context Menu:"); //$NON-NLS-1$
		groupGLF.applyTo(groupSystem);
		groupGDF.applyTo(groupSystem);

		Label label1 = UIControlsFactory.createLabel(groupSystem, "Text:"); //$NON-NLS-1$
		labelGDF.applyTo(label1);
		UIControlsFactory.createText(groupSystem, SWT.NONE, "textFieldSystem"); //$NON-NLS-1$

		Group groupText = UIControlsFactory.createGroup(parent, "Text with Context Menu:"); //$NON-NLS-1$
		groupGLF.applyTo(groupText);
		groupGDF.applyTo(groupText);

		UIControlsFactory.createLabel(groupText, "Text:"); //$NON-NLS-1$
		Text textField = UIControlsFactory.createText(groupText, SWT.NONE, "textField"); //$NON-NLS-1$
		textField.setMenu(createMenuWithFactory(textField));
		Label label2 = UIControlsFactory.createLabel(groupText, "Hide 'Clear':"); //$NON-NLS-1$
		labelGDF.applyTo(label2);
		UIControlsFactory.createButtonCheck(groupText, "", "markerButton"); //$NON-NLS-1$ //$NON-NLS-2$

		Group groupTable = UIControlsFactory.createGroup(parent, "Table with Context Menu:"); //$NON-NLS-1$
		groupGLF.numColumns(1).applyTo(groupTable);
		groupGDF.applyTo(groupTable);

		Table table = UIControlsFactory.createTable(groupTable, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setMenu(createContextMenuForTable(table));

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		addUIControl(table, "table"); //$NON-NLS-1$
		TableColumn colFirstName = new TableColumn(table, SWT.LEFT);
		colFirstName.setWidth(200);
		TableColumn colLastName = new TableColumn(table, SWT.LEFT);
		colLastName.setWidth(200);
		TableColumnLayout layout = new TableColumnLayout();
		layout.setColumnData(colFirstName, new ColumnWeightData(30));
		layout.setColumnData(colLastName, new ColumnWeightData(30));
	}

	private Menu createMenuWithFactory(Control parent) {
		Menu menu = UIControlsFactory.createMenu(parent);
		UIControlsFactory.createMenuItem(menu, "Clear", "textClear"); //$NON-NLS-1$ //$NON-NLS-2$
		MenuItem itemEdit = UIControlsFactory.createMenuItem(menu, "Set Text", SWT.CASCADE); //$NON-NLS-1$

		Menu menuEdit = UIControlsFactory.createMenu(itemEdit);
		UIControlsFactory.createMenuItem(menuEdit, "foo", "itemFoo"); //$NON-NLS-1$ //$NON-NLS-2$ 
		UIControlsFactory.createMenuItem(menuEdit, "bar", "itemBar"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createMenuItem(menuEdit, "baz", "itemBaz"); //$NON-NLS-1$ //$NON-NLS-2$
		itemEdit.setMenu(menuEdit);
		return menu;
	}

	private Menu createContextMenuForTable(Control parent) {
		Menu menu = UIControlsFactory.createMenu(parent);
		UIControlsFactory.createMenuItem(menu, "Delete", "tableRemove"); //$NON-NLS-1$ //$NON-NLS-2$
		return menu;
	}

}
