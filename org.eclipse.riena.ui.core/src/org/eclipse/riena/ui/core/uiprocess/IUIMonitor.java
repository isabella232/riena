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

import org.eclipse.core.runtime.IAdaptable;

/**
 * {@link IUIMonitor}s implement callback functions that are called on the user
 * interface thread
 */
public interface IUIMonitor extends IAdaptable {

	/**
	 * Handle process progress. Synchronized with UI-Thread
	 * 
	 * @param progress
	 */
	void updateProgress(int progress);

	/**
	 * The first Update of the UI. Synchronized with UI-Thread
	 * 
	 * @param progress
	 */
	void initialUpdateUI(int totalWork);

	/**
	 * Called when the process has finished. Synchronized with UI-Thread
	 * 
	 * @param progress
	 */
	void finalUpdateUI();

	boolean isActive(IUIMonitorContainer container);

}
