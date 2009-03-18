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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * This composite contains a table (the "master") of n columns, as well as add,
 * remove and update buttons. It also contains an arbitratry composite (the
 * "details"), which are updated automatically when the selected row in the
 * table changes.
 * 
 * @see IMasterDetailsRidget
 */
public abstract class MasterDetailsComposite extends Composite implements IComplexComponent {

	/**
	 * Binding id of the table control {@value} .
	 */
	public static final String BIND_ID_TABLE = "mdTable"; //$NON-NLS-1$
	/**
	 * Binding id of the new button {@value} .
	 */
	public static final String BIND_ID_NEW = "mdNewButton"; //$NON-NLS-1$
	/**
	 * Binding id of the remove button {@value} .
	 */
	public static final String BIND_ID_REMOVE = "mdRemoveButton"; //$NON-NLS-1$
	/**
	 * Binding id of the apply button {@value} .
	 */
	public static final String BIND_ID_APPLY = "mdApplyButton"; //$NON-NLS-1$

	private final List<Object> controls = new ArrayList<Object>();

	private Table table;
	private Composite details;

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
			details = createComposite(getDetailsStyle());
			createDetails(details);
		}
		createMaster(createComposite(getMasterStyle()));
		if (orientation == SWT.BOTTOM) {
			details = createComposite(getDetailsStyle());
			createDetails(details);
		}
		setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
	}

	public final Table getTable() {
		return table;
	}

	public final Composite getDetails() {
		return details;
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
	protected final void addUIControl(Object uiControl, String bindingId) {
		Assert.isNotNull(uiControl);
		Assert.isNotNull(bindingId);
		controls.add(uiControl);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(uiControl, bindingId);
	}

	/**
	 * TODO [ev] docs
	 */
	protected abstract void createDetails(Composite parent);

	/**
	 * Returns the style bits for the 'details' composite. Subclasses may
	 * override, but has to return a value that is supported by
	 * {@link Composite}.
	 * 
	 * @return {@code SWT.NONE}
	 */
	protected int getDetailsStyle() {
		return SWT.NONE;
	}

	/**
	 * Returns the style bits for the 'master' composite. Subclasses may
	 * override, but has to return a value that is supported by
	 * {@link Composite}.
	 * 
	 * @return {@code SWT.NONE}
	 */
	protected int getMasterStyle() {
		return SWT.NONE;
	}

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

	// TODO [ev] make protected for more flexibility
	private void createButtons(Composite parent) {
		Composite compButton = UIControlsFactory.createComposite(parent);
		GridDataFactory.fillDefaults().applyTo(compButton);
		RowLayout buttonLayout = new RowLayout(SWT.VERTICAL);
		buttonLayout.marginTop = 0;
		buttonLayout.marginLeft = 0;
		buttonLayout.marginRight = 0;
		buttonLayout.fill = true;
		compButton.setLayout(buttonLayout);
		Button btnAdd = UIControlsFactory.createButton(compButton, "New"); //$NON-NLS-1$
		addUIControl(btnAdd, BIND_ID_NEW);
		Button btnRemove = UIControlsFactory.createButton(compButton, "Remove"); //$NON-NLS-1$
		addUIControl(btnRemove, BIND_ID_REMOVE);
		Button btnUpdate = UIControlsFactory.createButton(compButton, "Apply"); //$NON-NLS-1$
		addUIControl(btnUpdate, BIND_ID_APPLY);
	}

	private Composite createComposite(int style) {
		Composite result = UIControlsFactory.createComposite(this, style);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(result);
		return result;
	}

	// TODO [ev] make protected for more flexibility
	private void createMaster(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		createTable(parent);
		createButtons(parent);
	}

	// TODO [ev] make protected for more flexibility
	private void createTable(Composite parent) {
		Composite compTable = UIControlsFactory.createComposite(parent);
		compTable.setLayout(new TableColumnLayout());
		table = new Table(compTable, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		int wHint = 200;
		int hHint = (table.getItemHeight() * 8) + table.getHeaderHeight();
		GridDataFactory.fillDefaults().grab(true, false).hint(wHint, hHint).applyTo(compTable);
		addUIControl(table, BIND_ID_TABLE);
	}
}
