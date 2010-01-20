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
package org.eclipse.riena.ui.swt.utils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.common.IComplexComponent;

/**
 * TODO [ev] javadoc
 */
public abstract class SWTControlFinder {

	private Composite start;

	public SWTControlFinder(Composite composite) {
		Assert.isNotNull(composite);
		start = composite;
	}

	public void run() {
		if (start == null) {
			throw new IllegalStateException("cannot run more than once!"); //$NON-NLS-1$
		}
		addUIControls(start);
		start = null;
	}

	public boolean contains(Control control) {
		return false;
	}

	public void handleControl(Control control) {
		if ((control instanceof Composite) && !(control instanceof IComplexComponent)) {
			addUIControls((Composite) control);
		}
	}

	public abstract void handleBoundControl(Control control, String bindingProperty);

	// protected methods
	////////////////////

	protected final void addUIControls(Composite composite) {
		SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		for (Control control : composite.getChildren()) {
			String bindingProperty = locator.locateBindingProperty(control);
			if (StringUtils.isGiven(bindingProperty) && !contains(control)) {
				handleBoundControl(control, bindingProperty);
			}
			handleControl(control);
		}
	}
}
