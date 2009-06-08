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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import org.eclipse.riena.ui.swt.EmbeddedTitleBar;

/**
 * Switch focus to the 'work area'.
 */
public class SwitchToWorkarea extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (window != null) {
			EmbeddedTitleBar titleBar = findEmbeddedTitleBar(window.getShell());
			if (titleBar != null) {
				titleBar.getParent().setFocus();
			}
		}
		return null;
	}

	private EmbeddedTitleBar findEmbeddedTitleBar(Composite parent) {
		EmbeddedTitleBar result = null;
		Control[] children = parent.getChildren();
		for (int i = 0; result == null && i < children.length; i++) {
			if (children[i] instanceof EmbeddedTitleBar) {
				result = (EmbeddedTitleBar) children[i];
			}
		}
		for (int i = 0; result == null && i < children.length; i++) {
			if (children[i] instanceof Composite) {
				result = findEmbeddedTitleBar((Composite) children[i]);
			}
		}
		return result;
	}

}
