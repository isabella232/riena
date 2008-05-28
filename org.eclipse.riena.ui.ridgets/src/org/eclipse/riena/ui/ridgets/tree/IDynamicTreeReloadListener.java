/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

import java.util.EventListener;

/**
 * Defines an object which listens for <code>DynamicTreeReloadEvent</code> 's.
 * 
 * @author Thorsten Schenkel
 */
public interface IDynamicTreeReloadListener extends EventListener {

	/**
	 * Invoked when reload of tree elements has started or stopped.
	 * 
	 * @param e a <code>DynamicTreeReloadEvent</code> object.
	 */
	void reload( DynamicTreeReloadEvent e );
} // end interface
