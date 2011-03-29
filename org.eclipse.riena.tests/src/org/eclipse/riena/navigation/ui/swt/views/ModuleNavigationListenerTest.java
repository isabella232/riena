/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 * Tests of the class {@link ModuleNavigationListener}.
 */
@UITestCase
public class ModuleNavigationListenerTest extends RienaTestCase {

	public void testIsSelectable() throws Exception {
		final Tree tree = new Tree(new Shell(Display.getDefault()), SWT.NONE);
		final ModuleNavigationListener listener = new ModuleNavigationListener(tree);
		final TreeItem item = new TreeItem(tree, SWT.NONE);
		final SubModuleNode s1 = new SubModuleNode("s1");
		item.setData(s1);
		assertTrue(invokeIsSelectable(listener, item));
		s1.setSelectable(false);
		assertFalse(invokeIsSelectable(listener, item));
		final SubModuleNode s2 = new SubModuleNode("s2");
		s1.addChild(s2);
		assertTrue(invokeIsSelectable(listener, item));
	}

	private static Boolean invokeIsSelectable(final ModuleNavigationListener listener, final TreeItem item) {
		return (Boolean) ReflectionUtils.invokeHidden(listener, "isSelectable", item);
	}

}
