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
package org.eclipse.riena.ui.ridgets;

/**
 * Invoked when a close operation on a window was performed. May be used to
 * query the users conviction and to prevent the closing.
 * 
 * @author Carsten Drossel
 */
public interface IWindowClosingPolicy {

	/**
	 * Invoked when a close operation on the window was performed. May prevent
	 * the closing.
	 * 
	 * @return true to close the window, false to cancel the closing.
	 */
	boolean allowsClose();

}
