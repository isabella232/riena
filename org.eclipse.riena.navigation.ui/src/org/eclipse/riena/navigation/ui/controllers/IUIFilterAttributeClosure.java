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
package org.eclipse.riena.navigation.ui.controllers;

import org.eclipse.riena.ui.filter.IUIFilterAttribute;

/**
 * This closures executes a method of {@link IUIFilterAttribute}.
 */
public interface IUIFilterAttributeClosure {

	/**
	 * Executes a method of {@link IUIFilterAttribute}.
	 * 
	 * @param attr
	 *            - filter attribute
	 * @param obj
	 *            - object that is given as a parameter to the method
	 */
	void exeute(IUIFilterAttribute attr, Object obj);

}
