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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.util.List;

import org.eclipse.swt.nebula.widgets.compositetable.CompositeTable;

/**
 * TODO [ev] docs
 */
public class CompositeTableRidget extends AbstractSelectableIndexedRidget {

	@Override
	public CompositeTable getUIControl() {
		return (CompositeTable) super.getUIControl();
	}

	@Override
	public int getSelectionIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getSelectionIndices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOfOption(Object option) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected List<?> getRowObservables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void bindUIControl() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, CompositeTable.class);
	}

	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	@Override
	protected void unbindUIControl() {
		// TODO Auto-generated method stub

	}

}
