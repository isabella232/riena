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
package org.eclipse.riena.ui.core.marker;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.core.marker.AbstractMarker;

/**
 * TODO [ev] docs
 * 
 * @since 2.0
 * @noinstantiate PROVISIONAL - MAY CHANGE
 */
public class RowErrorMessageMarker extends AbstractMarker implements IMessageMarker {

	private final Object rowValue;

	/**
	 * @param pMessage
	 */
	public RowErrorMessageMarker(String pMessage, Object rowValue) {
		super(false);
		Assert.isNotNull(rowValue);
		this.rowValue = rowValue;
		String message = pMessage == null ? "" : pMessage; //$NON-NLS-1$
		setAttribute(MESSAGE, message);
	}

	public String getMessage() {
		return (String) super.getAttribute(MESSAGE);
	}

	/**
	 * TODO [ev] docs
	 */
	public Object getRowValue() {
		return rowValue;
	}

	@Override
	public int hashCode() {
		return rowValue.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof RowErrorMessageMarker)) {
			return false;
		}
		return rowValue == ((RowErrorMessageMarker) other).rowValue;
	}

}
