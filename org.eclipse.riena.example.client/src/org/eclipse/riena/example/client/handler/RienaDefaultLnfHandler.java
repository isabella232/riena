package org.eclipse.riena.example.client.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.riena.navigation.ui.swt.views.NavigationTreeViewPart;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/

/**
 * 
 */
public class RienaDefaultLnfHandler extends AbstractHandler {

	/**
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		LnfManager.setLnf(getNewLnf());

		IViewReference[] viewRefs = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getViewReferences();
		for (IViewReference viewReference : viewRefs) {
			IViewPart viewPart = viewReference.getView(true);
			if (viewPart instanceof NavigationTreeViewPart) {
				((NavigationTreeViewPart) viewPart).rebuild();
				break;
			}
		}

		Shell shell = HandlerUtil.getActiveShell(event);
		shell.setRedraw(false);
		shell.setRedraw(true);
		return null;

	}

	protected RienaDefaultLnf getNewLnf() {
		return new RienaDefaultLnf();
	}

}
