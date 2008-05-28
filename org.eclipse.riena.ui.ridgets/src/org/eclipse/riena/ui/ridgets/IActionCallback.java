/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets;

public interface IActionCallback {

	/**
	 * @param param
	 *            parameter for the callback. can be null.
	 * @throws InterruptedException
	 */
	void callback(Object param) throws InterruptedException;

	/**
	 * contains all activities to be executed in case of any unexpected
	 * exceptions. E.g. The recovery of the submodule action components and the
	 * cursor shape.
	 */
	void rollback();

}
