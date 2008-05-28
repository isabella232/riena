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
package org.eclipse.riena.ui.core.uiprocess;

/**
 * Interface for serialization to the UI-Thread of the underlying Widget
 * Toolkit.
 * 
 */
public interface IUISynchronizer {

	/**
	 * serializes to the UI-Thread
	 * 
	 * @param runnable
	 */
	void synchronize(Runnable runnable);
}
