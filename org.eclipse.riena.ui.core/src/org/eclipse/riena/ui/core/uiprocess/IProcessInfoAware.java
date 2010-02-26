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
package org.eclipse.riena.ui.core.uiprocess;

/**
 * Interface for classes managing instances of {@link ProcessInfo}
 */
public interface IProcessInfoAware {

	/**
	 * 
	 * @return the {@link ProcessInfo} object
	 */
	ProcessInfo getProcessInfo();

	/**
	 * sets the {@link ProcessInfo} object
	 * 
	 * @param processInfo
	 */
	void setProcessInfo(ProcessInfo processInfo);

}
