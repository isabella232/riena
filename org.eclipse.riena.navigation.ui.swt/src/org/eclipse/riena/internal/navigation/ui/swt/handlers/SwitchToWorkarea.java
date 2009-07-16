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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewId;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProvider;

/**
 * Switch focus to the 'work area'.
 */
public class SwitchToWorkarea extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		String viewId = getViewId(getActiveNode());
		if (viewId != null) {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
			IWorkbenchPage page = window.getActivePage();
			for (IViewReference viewRef : page.getViewReferences()) {
				if (viewId.equals(getFullId(viewRef))) {
					IViewPart view = viewRef.getView(false);
					if (view != null) {
						view.setFocus();
					}
					break;
				}
			}
		}
		return null;
	}

	// helping methods
	//////////////////

	private INavigationNode<?> getActiveNode() {
		IApplicationNode appNode = ApplicationNodeManager.getApplicationNode();
		return appNode.getNavigationProcessor().getSelectedNode();
	}

	private String getFullId(IViewReference viewRef) {
		String result = viewRef.getId();
		if (viewRef.getSecondaryId() != null) {
			result = result + ":" + viewRef.getSecondaryId(); //$NON-NLS-1$
		}
		return result;
	}

	private String getViewId(INavigationNode<?> node) {
		String result = null;
		if (node != null) {
			SwtViewId viewId = SwtViewProvider.getInstance().getSwtViewId(node);
			if (viewId != null) {
				result = viewId.getCompoundId();
			}
		}
		return result;
	}

}
