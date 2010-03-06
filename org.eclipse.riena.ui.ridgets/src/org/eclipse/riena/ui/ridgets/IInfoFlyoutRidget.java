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
 * A notification that the turns up on top of the SubModuleView. No user
 * interaction is possible and it closes after a few seconds.
 * <p>
 * It is possible to set a message and an icon.
 * 
 * @since 2.0
 */
public interface IInfoFlyoutRidget extends IRidget {

	/**
	 * The icon to show.
	 * 
	 * @param icon
	 *            an icon; may be null.
	 */
	void setIcon(String icon);

	/**
	 * The message to show.
	 * 
	 * @param message
	 *            a message; non null
	 */
	void setMessage(String message);

	/**
	 * Open (show) this ridget.
	 */
	void open();

}
