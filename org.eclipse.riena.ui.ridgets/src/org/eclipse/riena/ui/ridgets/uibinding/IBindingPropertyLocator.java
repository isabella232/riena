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

	/**
	 * Value ({@value} ) used to separate compound id's. Example:
	 * &quot;parent.child&quot;.
	 */
	String SEPARATOR = "."; //$NON-NLS-1$

	/**
	 * Returns the ID of the given UI control.
	 * 
	 * @param uiControl
	 *            UI control; may be null
	 * @return the ID String or null, if uiControl is null or disposed
	 */
	String locateBindingProperty(Object uiControl);

}
