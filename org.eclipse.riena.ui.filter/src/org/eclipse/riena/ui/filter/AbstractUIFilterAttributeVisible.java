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
package org.eclipse.riena.ui.filter;

/**
 * Filter attribute for visibility.
 */
public abstract class AbstractUIFilterAttributeVisible implements IUIFilterAttribute {

	private boolean visible;

	/**
	 * Creates a new filter attribute for visibility.
	 * 
	 * @param visible
	 *            - {@code true} for visible; {@code false} for hidden
	 */
	public AbstractUIFilterAttributeVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Returns whether the UI element should be visible or hidden
	 * 
	 * @return {@code true} for visible; {@code false} for hidden
	 */
	public boolean isVisible() {
		return visible;
	}

}
