/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.obsolete;

/**
 * action adapter with extra "selected" state
 * 
 * @author Juergen Becker
 * @author Carsten Drossel
 */
public interface IToggleActionAdapter extends IMenuActionAdapter {

	/**
	 * Comment for <code>PROPERTY_SELECTED</code>
	 */
	String PROPERTY_SELECTED = "selected";

	/**
	 * @return true is adapter is selected
	 */
	boolean isSelected();

	/**
	 * @param selected
	 */
	void setSelected(boolean selected);

}