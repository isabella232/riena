/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.core;

/**
 * This interface should be implemented by classes that allow to dispose them,
 * i.e. free resources - and allow clients to check their state.
 * 
 * @since 1.3
 */
public interface IDisposable {

	/**
	 * Disposes this object, i.e. free any resources.
	 * 
	 * @return <code>true</code> if the disposing was successful,
	 *         <code>false</code> otherwise
	 * @since 2.1
	 */
	boolean dispose();

	/**
	 * Check whether this object has already disposed or not.
	 * 
	 * @return disposed or not
	 */
	boolean isDisposed();
}
