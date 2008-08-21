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
package org.eclipse.riena.ui.ridgets.util.beans;

/**
 * A runtime exception that describes access problems when getting/setting a
 * Java Bean property.
 */
public class PropertyAccessFailure extends PropertyFailure {

	/**
	 * Constructor for exception if accessing a bean property fails
	 * 
	 * @param message
	 * @param cause
	 */
	public PropertyAccessFailure(String message, Throwable cause) {
		super(message, cause);
	}
}
