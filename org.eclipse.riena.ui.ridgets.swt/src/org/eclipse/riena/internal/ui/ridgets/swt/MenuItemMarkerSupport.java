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

import java.beans.PropertyChangeSupport;

import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.swt.widgets.MenuItem;

/**
 *
 */
public class MenuItemMarkerSupport extends AbstractMarkerSupport {

	/**
	 * @param ridget
	 * @param propertyChangeSupport
	 */
	public MenuItemMarkerSupport(IMarkableRidget ridget, PropertyChangeSupport propertyChangeSupport) {
		super(ridget, propertyChangeSupport);
	}

	@Override
	public void updateMarkers() {
		updateMenuItem();
	}

	@Override
	protected void handleMarkerAttributesChanged() {
		updateMenuItem();
		super.handleMarkerAttributesChanged();
	}

	private void updateDisabled(MenuItem item) {
		item.setEnabled(ridget.isEnabled());
	}

	private void updateMenuItem() {
		MenuItem item = (MenuItem) ridget.getUIControl();
		if (item != null) {
			updateDisabled(item);
		}
	}

}
