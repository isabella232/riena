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

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;

/**
 * This composite contains a Table widget (the "master") of n columns, as well
 * as new, remove and update buttons. It also contains an arbitratry composite
 * (the "details"), which is updated automatically when the selected row in the
 * table changes.
 * <p>
 * See {@link AbstractMasterDetailsComposite} for details.
 * 
 * @see IMasterDetailsRidget
 * 
 * @since 1.2
 */
public class MasterDetailsComposite extends AbstractMasterDetailsComposite implements IComplexComponent {

	/**
	 * Creates a master detail composite with the given style and SWT.BOTTOM
	 * orientation. See parent class for details.
	 * 
	 * @see AbstractMasterDetailsComposite#AbstractMasterDetailsComposite(Composite,
	 *      int, int)
	 */
	public MasterDetailsComposite(Composite parent, int style) {
		this(parent, style, SWT.BOTTOM);
	}

	/**
	 * Creates a master detail composite with the given style and SWT.BOTTOM
	 * orientation. See parent class for details.
	 * 
	 * @see AbstractMasterDetailsComposite#AbstractMasterDetailsComposite(Composite,
	 *      int, int)
	 */
	public MasterDetailsComposite(Composite parent, int style, int orientation) {
		super(parent, style, orientation);
	}

	/**
	 * Returns the Table control of the 'master' area/
	 * 
	 * @return a Table; never null
	 */
	public final Table getTable() {
		return (Table) super.getTable();
	}

	protected Table createTable(Composite compTable, TableColumnLayout layout) {
		Table table = new Table(compTable, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		return table;
	}

}
