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

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;

/**
 * Close the currently active module.
 */
public class CloseModule extends AbstractHandler {

	@SuppressWarnings("unchecked")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IApplicationNode application = ApplicationNodeManager.getApplicationNode();
		INavigationNode<?> subApplication = findActive((List) application.getChildren());
		if (subApplication != null) {
			INavigationNode<?> moduleGroup = findActive((List) subApplication.getChildren());
			if (moduleGroup != null) {
				INavigationNode<?> module = findActive((List) moduleGroup.getChildren());
				if (module instanceof IModuleNode) {
					((IModuleNode) module).dispose();
				}
			}
		}
		return null;
	}

	// helping methods
	//////////////////

	protected final INavigationNode<?> findActive(List<INavigationNode<?>> children) {
		INavigationNode<?> result = null;
		Iterator<INavigationNode<?>> iter = children.iterator();
		while (result == null && iter.hasNext()) {
			INavigationNode<?> candidate = iter.next();
			if (candidate.isActivated()) {
				result = candidate;
			}
		}
		return result;
	}

}
