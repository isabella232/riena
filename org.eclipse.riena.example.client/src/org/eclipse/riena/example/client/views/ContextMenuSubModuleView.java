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

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.example.client.controllers.ContextMenuSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IMenuItemRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT {@link IMenuItemRidget} (context menu) sample.
 */
public class ContextMenuSubModuleView extends SubModuleView<ContextMenuSubModuleController> {
	public ContextMenuSubModuleView() {
	}

	public static final String ID = ContextMenuSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		GridLayoutFactory.swtDefaults().margins(5, 5).equalWidth(true).applyTo(parent);

		Group groupSystem = UIControlsFactory.createGroup(parent, "Text with System-Contextmenu:"); //$NON-NLS-1$
		groupSystem.setLayout(new GridLayout(2, true));
		UIControlsFactory.createLabel(groupSystem, "Text:"); //$NON-NLS-1$
		UIControlsFactory.createText(groupSystem, SWT.None, "textFieldSystem"); //$NON-NLS-1$

		Group groupText = UIControlsFactory.createGroup(parent, "Text with Contextmenu:"); //$NON-NLS-1$
		groupText.setLayout(new GridLayout(2, true));
		UIControlsFactory.createLabel(groupText, "Text:"); //$NON-NLS-1$
		Text textField = UIControlsFactory.createText(groupText, SWT.None, "textField"); //$NON-NLS-1$
		textField.setMenu(createMenuWithFactory(textField));
		UIControlsFactory.createLabel(groupText, "Hide MenuItem:"); //$NON-NLS-1$
		UIControlsFactory.createButtonCheck(groupText, "markerButton"); //$NON-NLS-1$

		Group groupTable = UIControlsFactory.createGroup(parent, "Table with Contextmenu:"); //$NON-NLS-1$
		groupTable.setLayout(new GridLayout(1, true));

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
		UIControlsFactory.createMenuItem(menu, "Select All", "textSelectAll"); //$NON-NLS-1$ //$NON-NLS-2$
		MenuItem itemEdit = UIControlsFactory.createMenuItem(menu, "Edit", SWT.CASCADE); //$NON-NLS-1$

		Menu menuEdit = UIControlsFactory.createMenu(itemEdit);
		UIControlsFactory.createMenuItem(menuEdit, "Cut"); //$NON-NLS-1$ 
		UIControlsFactory.createMenuItem(menuEdit, "Copy"); //$NON-NLS-1$
		UIControlsFactory.createMenuItem(menuEdit, "Paste"); //$NON-NLS-1$
		itemEdit.setMenu(menuEdit);
		return menu;
	}

	private Menu createContextMenuForTable(Control parent) {
		Menu menu = UIControlsFactory.createMenu(parent);
		UIControlsFactory.createMenuItem(menu, "Delete", "tableRemove"); //$NON-NLS-1$ //$NON-NLS-2$
		return menu;
	}

}
