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
package org.eclipse.riena.navigation.ui.swt.component;

import junit.framework.TestCase;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

/**
 * Tests of the class <code>ModuleItem</code>.
 */
public class ModuleItemTest extends TestCase {

	private Shell shell;
	private ModuleGroupWidget parent;
	private ModuleGroupNode mgNode;
	private ModuleNavigationComponent moduleCmp;
	private ModuleGroupNavigationComponent moduleGroupCmp;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		mgNode = new ModuleGroupNode();
		parent = new ModuleGroupWidget(shell, SWT.NONE, mgNode);
		moduleGroupCmp = new ModuleGroupNavigationComponent(new ModuleGroupNode(), shell, null);
		moduleCmp = new ModuleNavigationComponent(new ModuleNode(), moduleGroupCmp);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		moduleGroupCmp = null;
		moduleCmp = null;
		parent.dispose();
		parent = null;
		shell.dispose();
		shell = null;
		mgNode = null;
	}

	/**
	 * Tests the constructor <code>ModuleItem</code>.
	 */
	public void testModuleItem() {

		ModuleItem item = new ModuleItem(parent, moduleCmp);
		assertFalse(item.isHover());
		assertFalse(item.isPressed());
		assertNotNull(item.getBody());
		assertNotNull(item.getTree());
		item.dispose();

	}

	/**
	 * Tests the method <code>createSubModuleTree()</code>.
	 */
	public void testCreateSubModuleTree() {

		ModuleItem item = new ModuleItem(parent, moduleCmp);
		item.createSubModuleTree();
		Tree tree = item.getTree();
		assertNotNull(tree);
		assertFalse(tree.getLinesVisible());

		// listeners added?
		assertTrue(tree.isListening(SWT.Collapse));
		assertTrue(tree.isListening(SWT.Expand));
		assertTrue(tree.isListening(SWT.Paint));
		assertTrue(tree.isListening(SWT.Selection));

		item.dispose();

	}

	/**
	 * Tests the method <code>dispose()</code>.
	 */
	public void testDispose() {

		ModuleItem item = new ModuleItem(parent, moduleCmp);
		Composite body = item.getBody();
		Tree tree = item.getTree();
		item.dispose();
		assertTrue(body.isDisposed());
		assertTrue(tree.isDisposed());
		assertNull(item.getBody());
		assertNull(item.getTree());

	}

	/**
	 * Tests the method <code>getModuleNode()</code>.
	 */
	public void testGetModuleNode() {

		IModuleNode moduleNode = new ModuleNode();
		IModuleGroupNode mgNode = new ModuleGroupNode();
		ModuleGroupNavigationComponent mgCmp = new ModuleGroupNavigationComponent(mgNode, shell, null);
		ModuleNavigationComponent moduleCmp = new ModuleNavigationComponent(moduleNode, mgCmp);
		ModuleItem item = new ModuleItem(parent, moduleCmp);
		assertSame(moduleNode, item.getModuleNode());
		item.dispose();

	}

}
