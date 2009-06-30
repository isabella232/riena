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
package org.eclipse.riena.internal.navigation.ui.swt.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;

/**
 * Close the currently active module-group.
 */
public class CloseModuleGroup extends AbstractNavigationHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		// assumes there is only one application node
		IApplicationNode application = ApplicationNodeManager.getApplicationNode();
		IModuleGroupNode moduleGroup = findModuleGroup(application);
		if (moduleGroup != null && isCloseable(moduleGroup)) {
			// TODO [ev] focus on previous module group 
			((IModuleGroupNode) moduleGroup).dispose();
		}
		return null;
	}

	// helping methods
	//////////////////

	private boolean isCloseable(IModuleGroupNode moduleGroup) {
		boolean result = true;
		for (IModuleNode module : moduleGroup.getChildren()) {
			result = result && module.isClosable();
		}
		return result;
	}

}
