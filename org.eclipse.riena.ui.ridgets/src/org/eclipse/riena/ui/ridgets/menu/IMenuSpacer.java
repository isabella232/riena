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
package org.eclipse.riena.ui.ridgets.menu;

import org.eclipse.riena.ui.ridgets.IRidget;

/**
 * 
 */
public interface IMenuSpacer extends IMenuItem, IRidget {

	/**
	 * @return the size of the spacer or null if all available space is used
	 */
	Integer getSize();

	/**
	 * @param size
	 *            the size of the spacer or null if all available space should
	 *            be used
	 */
	void setSize(Integer size);
}
