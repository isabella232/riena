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
package org.eclipse.riena.ui.ridgets.uibinding;

/**
 * Helper class to get the ID of a UI control used for binding.
 */
public interface IBindingPropertyLocator {

	String SEPARATOR = "."; //$NON-NLS-1$

	/**
	 * Returns the ID of the given UI control.
	 * 
	 * @param uiControl
	 *            UI control
	 * @return ID
	 */
	String locateBindingProperty(Object uiControl);

}
