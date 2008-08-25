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
package org.eclipse.riena.sample.app.client.mail;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;

/**
 * Creates a new module group with an associated message view.
 */
public class OpenViewHandler extends AbstractHandler implements IHandler {

	private static IModuleGroupNode GROUP;
	private int count = 0;

	public static void setGroup(IModuleGroupNode group) {
		GROUP = group;
	}

	public Object execute(ExecutionEvent event) {
		if (GROUP != null) {
			String title = "me@this.com (" + ++count + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			IModuleNode moduleAccount1 = NodeFactory.createModule(title, GROUP);
			SwtPresentationManagerAccessor.getManager().registerView(View.ID, false);
			NodeFactory.createSubMobule("Inbox", moduleAccount1, View.ID); //$NON-NLS-1$
			NodeFactory.createSubMobule("Drafts", moduleAccount1, View.ID); //$NON-NLS-1$
			NodeFactory.createSubMobule("Sent", moduleAccount1, View.ID); //$NON-NLS-1$
		}
		return null;
	}
}
