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
package org.eclipse.riena.ui.swt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

/**
 * This composite contains a table (the "master") of n columns, as well as add,
 * remove and update buttons. It also contains an arbitratry composite (the
 * "details"), which are updated automatically when the selected row in the
 * table changes.
 * 
 * @see IMasterDetailsRidget
 */
public abstract class MasterDetailsComposite extends Composite implements IComplexComponent {

	private final List<Object> controls = new ArrayList<Object>();
	private Table table;

	/**
	 * TODO [ev] docs
	 */
	public MasterDetailsComposite(Composite parent, int style) {
		this(parent, style, SWT.BOTTOM);
	}

	/**
	 * TODO [ev] docs
	 */
	public MasterDetailsComposite(Composite parent, int style, int orientation) {
		super(parent, style);
		checkOrientation(orientation);

		setLayout(new GridLayout(1, false));
		if (orientation == SWT.TOP) {
			createDetails(createComposite(SWT.NONE));
		}
		createMaster(createComposite(SWT.NONE));
		if (orientation == SWT.BOTTOM) {
			createDetails(createComposite(SWT.NONE));
		}
		setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
	}

	public final Table getTable() {
		return table;
	}

	// protected methods
	////////////////////

	/**
	 * TODO [ev] docs
	 */
	public final List<Object> getUIControls() {
		return Collections.unmodifiableList(controls);
	}

	/**
	 * TODO [ev] docs
	 */
	protected final void addUIContol(Object uiControl, String bindingId) {
		Assert.isNotNull(uiControl);
		Assert.isNotNull(bindingId);
		controls.add(uiControl);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(uiControl, bindingId);
	}

	/**
	 * TODO [ev] docs
	 */
	protected abstract void createDetails(Composite parent);

	// helping methods
	//////////////////

	private void checkOrientation(int orientation) {
		int[] allowedValues = { SWT.TOP, SWT.BOTTOM };
		for (int value : allowedValues) {
			if (orientation == value) {
				return;
			}
		}
		throw new IllegalArgumentException("unsupported orientation: " + orientation); //$NON-NLS-1$
	}

	private Composite createComposite(int style) {
		Composite result = new Composite(this, style);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(result);
		return result;
	}

	private void createMaster(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		Composite compTable = new Composite(parent, SWT.NONE);
		compTable.setLayout(new TableColumnLayout());
		table = new Table(compTable, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		int wHint = 200;
		int hHint = (table.getItemHeight() * 8) + table.getHeaderHeight();
		GridDataFactory.fillDefaults().grab(true, false).hint(wHint, hHint).applyTo(compTable);
		addUIContol(table, "mdTable"); //$NON-NLS-1$

		Composite compButton = new Composite(parent, SWT.NONE);
		GridDataFactory.fillDefaults().applyTo(compButton);
		RowLayout buttonLayout = new RowLayout(SWT.VERTICAL);
		buttonLayout.marginTop = 0;
		buttonLayout.marginLeft = 0;
		buttonLayout.marginRight = 0;
		buttonLayout.fill = true;
		compButton.setLayout(buttonLayout);
		Button btnAdd = new Button(compButton, SWT.PUSH);
		btnAdd.setText("Add");
		addUIContol(btnAdd, "mdAddButton"); //$NON-NLS-1$
		Button btnRemove = new Button(compButton, SWT.PUSH);
		btnRemove.setText("Remove");
		addUIContol(btnRemove, "mdRemoveButton"); //$NON-NLS-1$
		Button btnUpdate = new Button(compButton, SWT.PUSH);
		btnUpdate.setText("Update");
		addUIContol(btnUpdate, "mdUpdateButton"); //$NON-NLS-1$
	}
}
