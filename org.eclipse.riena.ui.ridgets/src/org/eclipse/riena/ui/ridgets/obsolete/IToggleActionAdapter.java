/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
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
	void setSelected( boolean selected );

}