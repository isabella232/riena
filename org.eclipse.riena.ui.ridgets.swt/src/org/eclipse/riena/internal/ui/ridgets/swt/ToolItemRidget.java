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

import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Ridget of a tool item.
 */
public class ToolItemRidget extends AbstractItemRidget {

	@Override
	protected void bindUIControl() {
		super.bindUIControl();
		ToolItem toolItem = getUIControl();
		if (toolItem != null) {
			toolItem.addSelectionListener(getActionObserver());
		}
	}

	@Override
	protected void unbindUIControl() {
		ToolItem toolItem = getUIControl();
		if (toolItem != null) {
			toolItem.removeSelectionListener(getActionObserver());
		}
		super.unbindUIControl();
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		assertType(uiControl, ToolItem.class);
	}

	@Override
	public ToolItem getUIControl() {
		return (ToolItem) super.getUIControl();
	}

	@Override
	protected AbstractMarkerSupport createMarkerSupport() {
		return new ToolItemMarkerSupport(this, propertyChangeSupport);
	}

}
