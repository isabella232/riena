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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.core.util.StringUtils;

/**
 * TODO [ev] docs
 */
public final class BindingUtil {

	// TODO [ev] test this
	public static List<Object> getControlsWithBindingProperty(Composite composite) {
		Map<String, Control> result = new HashMap<String, Control>();
		collectControlsWithBindingProperty(composite, result);
		return new ArrayList<Object>(result.values());
	}

	// helping methods
	//////////////////

	private BindingUtil() {
		// prevent instatiation
	}

	private static void collectControlsWithBindingProperty(Composite parent, Map<String, Control> result) {
		SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		for (Control control : parent.getChildren()) {
			String bindingProperty = locator.locateBindingProperty(control);
			if (StringUtils.isGiven(bindingProperty)) {
				if (result.containsKey(bindingProperty)) {
					Control firstControl = result.get(bindingProperty);
					String msg = String.format("Binding property '%s' used by several widgets: %s and %s", //$NON-NLS-1$
							bindingProperty, firstControl, control);
					// TODO [ev] throw exception or log and ignore?
					throw new RuntimeException(msg);
				} else {
					result.put(bindingProperty, control);
				}
			}
			if (control instanceof Composite) {
				collectControlsWithBindingProperty((Composite) control, result);
			}
		}
	}

}
