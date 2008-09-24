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
 * A filter attribute can manipulate one attribute of an UI element (i.g. marker
 * of a ridget or visibility of a navigation node).
 */
public interface IUIFilterAttribute {

	/**
	 * Returns whether the given object matches the conditions of this
	 * attribute.
	 * 
	 * @param object
	 *            - object to check
	 * @return {@code true} if the given object matches; otherwise {@code false}
	 *         .
	 */
	boolean matches(Object object);

	/**
	 * Applys this attribute to the given object.
	 * 
	 * @param object
	 */
	void apply(Object object);

	/**
	 * Removes this attribute from the given object.
	 * 
	 * @param object
	 */
	void remove(Object object);

}
