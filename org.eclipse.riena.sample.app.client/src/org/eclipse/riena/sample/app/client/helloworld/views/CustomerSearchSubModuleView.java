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
package org.eclipse.riena.sample.app.client.helloworld.views;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProviderAccessor;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.sample.app.client.helloworld.controllers.CustomerSearchSubModuleController;
import org.eclipse.riena.sample.app.common.model.Customer;
import org.eclipse.riena.ui.workarea.WorkareaManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class CustomerSearchSubModuleView extends SubModuleView<CustomerSearchSubModuleController> {

	public final static String ID = CustomerSearchSubModuleView.class.getName();

	private static final int FIELD_WIDTH = 100;
	private final static int TOP = 10;
	private static final int LEFT = 10;
	private static final int SECTION_LABEL_WIDTH = 100;
	private static final int LABEL_WIDTH = 90;
	private static final int LINE_GAP = 12;
	private static final int COL_GAP = 30;

	private Customer sample;

	private Composite parent;

	private Table searchResultTable;

	private Text kundennummerText;

	private Text nameText;

	private Text vornameText;

	public CustomerSearchSubModuleView() {
		sample = new Customer();
	}

	@Override
	public void basicCreatePartControl(Composite parent) {
		this.parent = parent;
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new FormLayout());

		Label personLabel = createSectionLabel(parent, "Person");
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, TOP);
		fd.left = new FormAttachment(0, LEFT);
		personLabel.setLayoutData(fd);

		Label kundennummerLabel = new Label(parent, SWT.LEFT);
		kundennummerLabel.setText("Kundennummer");
		kundennummerLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(personLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(personLabel, SECTION_LABEL_WIDTH, SWT.LEFT);
		kundennummerLabel.setLayoutData(fd);

		kundennummerText = new Text(parent, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(kundennummerLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(kundennummerLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		kundennummerText.setLayoutData(fd);

		Label nameLabel = new Label(parent, SWT.LEFT);
		nameLabel.setText("Name");
		nameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(kundennummerLabel, LINE_GAP);
		fd.left = new FormAttachment(kundennummerLabel, 0, SWT.LEFT);
		nameLabel.setLayoutData(fd);

		nameText = new Text(parent, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(nameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(kundennummerText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		nameText.setLayoutData(fd);
		addUIControl(nameText, "lastNameRidget");

		Label vornameLabel = new Label(parent, SWT.LEFT);
		vornameLabel.setText("Vorname");
		vornameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(nameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(nameLabel, COL_GAP + FIELD_WIDTH + LABEL_WIDTH, SWT.LEFT);
		vornameLabel.setLayoutData(fd);

		vornameText = new Text(parent, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(vornameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(vornameLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		vornameText.setLayoutData(fd);
		addUIControl(vornameText, "firstNameRidget");

		Button searchButton = new Button(parent, 0);
		searchButton.setText("Suchen");
		fd = new FormData();
		fd.top = new FormAttachment(vornameText, LINE_GAP);
		fd.left = new FormAttachment(vornameText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		searchButton.setLayoutData(fd);
		addUIControl(searchButton, "searchAction");

		Button clearButton = new Button(parent, 0);
		clearButton.setText("Clear");
		fd = new FormData();
		fd.top = new FormAttachment(vornameText, LINE_GAP);
		fd.left = new FormAttachment(nameText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		clearButton.setLayoutData(fd);
		addUIControl(clearButton, "clearAction");

		// create table
		searchResultTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		searchResultTable.setLinesVisible(true);
		addUIControl(searchResultTable, "tableRidget");
		// searchResultTable.addSelectionListener(new CustomerOpener());
		TableColumn customerNumberColumn = new TableColumn(searchResultTable, SWT.CENTER);
		TableColumn lastNameColumn = new TableColumn(searchResultTable, SWT.CENTER);
		TableColumn firstNameColumn = new TableColumn(searchResultTable, SWT.CENTER);
		TableColumn phoneColumn = new TableColumn(searchResultTable, SWT.CENTER);
		customerNumberColumn.setWidth(80);
		firstNameColumn.setWidth(120);
		lastNameColumn.setWidth(120);
		phoneColumn.setWidth(100);
		searchResultTable.setHeaderVisible(true);

		// layout table
		fd = new FormData();
		fd.top = new FormAttachment(searchButton, LINE_GAP);
		fd.left = new FormAttachment(0, LEFT);
		fd.height = 200;
		fd.width = 500;
		searchResultTable.setLayoutData(fd);

		// open button
		Button openButton = new Button(parent, 0);
		openButton.setText("Open");
		fd = new FormData();
		fd.top = new FormAttachment(searchResultTable, LINE_GAP);
		fd.left = new FormAttachment(searchResultTable, 0, SWT.CENTER);
		fd.width = FIELD_WIDTH;
		openButton.setLayoutData(fd);
		addUIControl(openButton, "openAction");

	}

	private class CustomerOpener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (e.getSource() instanceof Button) {
				openCustomer();
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			if (e.getSource() instanceof Table) {
				openCustomer();
			}
		}

	}

	private ISubModuleNode getNode() {
		return SwtViewProviderAccessor.getViewProvider().getNavigationNode(this.getViewSite().getId(),
				this.getViewSite().getSecondaryId(), ISubModuleNode.class);
	}

	protected void openCustomer() {
		Customer selected = ((Customer) searchResultTable.getSelection()[0].getData());

		ISubModuleNode node = getNode();
		SubModuleNode cNode = new SubModuleNode(null, selected.getFirstName());
		cNode.setContext(Customer.class.getName(), selected);
		WorkareaManager.getInstance().registerDefinition(cNode, CustomerDetailsSubModuleView.ID);
		node.addChild(cNode);
		cNode.activate();
	}

	private Label createSectionLabel(Composite parent, String text) {

		Label label = new Label(parent, SWT.LEFT);
		label.setText(text);
		label.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		return label;

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected CustomerSearchSubModuleController createController(ISubModuleNode subModuleNode) {
		return new CustomerSearchSubModuleController(getNode());
	}

}
