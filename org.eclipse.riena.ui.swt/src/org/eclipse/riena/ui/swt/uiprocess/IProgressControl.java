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
package org.eclipse.riena.ui.swt.uiprocess;

/**
 * 
 */
public interface IProgressControl {

	/**
	 * start showing progress/process
	 */
	void start();

	/**
	 * stop showing progress/process
	 */
	void stop();

	void showProcessing();

	void showProgress(int value, int maxValue);

	void setDescription(String text);

	void setTitle(String text);

	void addCancelListener(ICancelListener listener);

	void removeCancelListener(ICancelListener listener);

}